package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.CrawlService;
import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.mapper.LocationStatisticResponseMapper;
import com.jpromi.operation_point.model.LocationStatisticResponse;
import com.jpromi.operation_point.repository.CrawlServiceRepository;
import com.jpromi.operation_point.repository.OperationRepository;
import com.jpromi.operation_point.service.OperationService;
import com.jpromi.operation_point.service.OperationVariableService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/embed")
@RestController("EmbedController")
public class EmbedController {
    private final OperationService operationService;
    private final CrawlServiceRepository crawlServiceRepository;
    private final OperationVariableService operationVariableService;
    private final LocationStatisticResponseMapper locationStatisticResponseMapper;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmbedController(OperationService operationService, CrawlServiceRepository crawlServiceRepository, OperationVariableService operationVariableService, LocationStatisticResponseMapper locationStatisticResponseMapper, SpringTemplateEngine templateEngine) {
        this.operationService = operationService;
        this.crawlServiceRepository = crawlServiceRepository;
        this.operationVariableService = operationVariableService;
        this.locationStatisticResponseMapper = locationStatisticResponseMapper;
        this.templateEngine = templateEngine;
    }

    @GetMapping(value = "/vector/map/country.svg", produces = "image/svg+xml")
    public ResponseEntity<String> vectorMapCountry(
            @RequestParam(required = false, defaultValue = "dark", name = "style") String styleType,
            @RequestParam(required = false, defaultValue = "false", name = "stroke") boolean withStroke,
            HttpServletRequest request,
            HttpServletResponse response
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

        Map<String, Object> variables = new HashMap<>();
        variables.put("style", svgStyle);
        variables.put("showStroke", withStroke);
        variables.put("isDarkMode", styleType.equals("dark"));
        variables.put("disabledFill", styleType.equals("dark") ? "#1F2225" : "#BFC1C3");

        // Thymeleaf WebContext (Thymeleaf 3.1+)
        ServletContext servletContext = request.getServletContext();
        JakartaServletWebApplication webApp = JakartaServletWebApplication.buildApplication(servletContext);
        IWebExchange webExchange = webApp.buildExchange(request, response);

        WebContext ctx = new WebContext(webExchange, request.getLocale(), variables);

        // process SVG template
        String svg = templateEngine.process("embed/vector/at", ctx);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/svg+xml;charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=op-austria.svg")
                .body(svg);
    }

    @GetMapping(value = "/vector/map/{federalState}.svg", produces = "image/svg+xml")
    public ResponseEntity<String> vectorMapFederalState(
            @PathVariable String federalState,
            @RequestParam(required = false, defaultValue = "dark", name = "style") String styleType,
            @RequestParam(required = false, defaultValue = "false", name = "stroke") boolean withStroke,
            HttpServletRequest request,
            HttpServletResponse response
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
            if (entry.getKey() != null && entry.getValue() != null) {
                svgStyle += "#" + entry.getKey().toLowerCase() + " { fill: " + entry.getValue() + "; } ";
            }
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("style", svgStyle);
        variables.put("showStroke", withStroke);

        // Thymeleaf WebContext (Thymeleaf 3.1+)
        ServletContext servletContext = request.getServletContext();
        JakartaServletWebApplication webApp = JakartaServletWebApplication.buildApplication(servletContext);
        IWebExchange webExchange = webApp.buildExchange(request, response);

        WebContext ctx = new WebContext(webExchange, request.getLocale(), variables);

        // process SVG template
        String svg = templateEngine.process("embed/vector/" + operationVariableService.getFederalStateId(federalState), ctx);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/svg+xml;charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=op-" + operationVariableService.getFederalStateId(federalState) + ".svg")
                .body(svg);
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
