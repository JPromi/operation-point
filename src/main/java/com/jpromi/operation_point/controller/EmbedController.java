package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.CrawlService;
import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.mapper.LocationStatisticResponseMapper;
import com.jpromi.operation_point.model.LocationStatisticResponse;
import com.jpromi.operation_point.repository.CrawlServiceRepository;
import com.jpromi.operation_point.repository.OperationRepository;
import com.jpromi.operation_point.service.OperationService;
import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/embed")
public class EmbedController {

    @Autowired
    private OperationService operationService;

    @Autowired
    private CrawlServiceRepository crawlServiceRepository;

    @Autowired
    private OperationVariableService operationVariableService;

    @Autowired
    private LocationStatisticResponseMapper locationStatisticResponseMapper;

    @GetMapping(value = "/vector/map/country.svg", produces = "image/svg+xml")
    public String vectorMapCountry(
            @RequestParam(required = false, defaultValue = "dark", name = "style") String styleType,
            @RequestParam(required = false, defaultValue = "false", name = "stroke") boolean withStroke,
            Model model
    ) {
        Map<String, String> federalStateColors = new HashMap<>();
        List<String> federalStates = List.of("la", "ua", "bl", "st", "ty", "ct", "sb", "vi", "vb");

        // get statistics
        List<Operation> operations = operationService.getActiveOperations();
        List<LocationStatisticResponse> statisticResponses = new ArrayList<>();
        if (operations != null && !operations.isEmpty()) {
            statisticResponses = locationStatisticResponseMapper.fromOperationFederalState(operations);
        }

        // get active services
        List<CrawlService> services = crawlServiceRepository.findAll();

        // define colors
        for (String stateId : federalStates) {
            LocationStatisticResponse stat = statisticResponses.stream().filter(s -> s.getNameId().equals(stateId)).findFirst().orElse(null);
            if (stat != null && stat.getCountActive() > 0) {
                federalStateColors.put(stateId, getMapTintColor(stat.getCountActive(), styleType.equals("dark")));
            } else {
                federalStateColors.put(stateId, getMapTintColor(0L, styleType.equals("dark")));
            }
        }

        for (CrawlService service : services) {
            if (!service.getIsEnabled()) {
                federalStateColors.put(service.getName().substring(service.getName().length() - 2), "url(#diagonalHatch)");
            }
        }

        String svgStyle = "#stroke { fill: " + (styleType.equals("dark") ? "#151718" : "#fff") + "}";

        // generate style
        for (Map.Entry<String, String> entry : federalStateColors.entrySet()) {
            svgStyle += "#" + entry.getKey().toLowerCase() + " { fill: " + entry.getValue() + "; } ";
        }

        model.addAttribute("style", svgStyle);
        model.addAttribute("showStroke", withStroke);
        model.addAttribute("isDarkMode", styleType.equals("dark"));
        model.addAttribute("disabledFill", styleType.equals("dark") ? "#1F2225" : "#BFC1C3");

        return "embed/vector/at";
}

    @GetMapping(value = "/vector/map/{federalState}.svg", produces = "image/svg+xml")
    public String vectorMapFederalState(
            @PathVariable String federalState,
            @RequestParam(required = false, defaultValue = "dark", name = "style") String styleType,
            @RequestParam(required = false, defaultValue = "false", name = "stroke") boolean withStroke,
            Model model
    ) {
        federalState = operationVariableService.getFederalState(federalState);
        System.out.println("federalState: " + federalState);
        if(federalState == null || federalState.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Federal state not found");
        }
        Map<String, String> districtColors = new HashMap<>();

        // get statistics
        List<Operation> operations = operationService.getActiveOperationsByFederalState(federalState);
        List<LocationStatisticResponse> statisticResponses = new ArrayList<>();
        if (operations != null && !operations.isEmpty()) {
            statisticResponses = locationStatisticResponseMapper.fromOperationDistrict(operations, federalState);
        }

        for (LocationStatisticResponse stat : statisticResponses) {
            districtColors.put(stat.getNameId(), getMapTintColor(stat.getCountActive(), styleType.equals("dark")));
        }

        // generate style
        String svgStyle = "#stroke { fill: " + (styleType.equals("dark") ? "#151718" : "#fff") + "}" +
                "#map { fill: " + (styleType.equals("dark") ? "#1F2225" : "#BFC1C3" ) + "; } ";

        for (Map.Entry<String, String> entry : districtColors.entrySet()) {
            svgStyle += "#" + entry.getKey().toLowerCase() + " { fill: " + entry.getValue() + "; } ";
        }

        model.addAttribute("style", svgStyle);
        model.addAttribute("showStroke", withStroke);

        return "embed/vector/" + operationVariableService.getFederalStateId(federalState);
    }

    private String getMapTintColor(Long operationCount, Boolean isDarkMode) {
        if (isDarkMode) {
            if (operationCount == 0) {
                return "rgb(44, 47, 50)";
            }
            return "rgb(255, " + (255 - Math.min(operationCount, 20) * 7.5) + ", 0)";
        } else {
            if (operationCount == 0) {
                return "rgb(179, 180, 181)";
            }
            return "rgb(255, " + (225 - Math.min(operationCount, 20) * 7.5) + ", 0)";
        }
    }
}
