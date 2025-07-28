package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.Unit;
import com.jpromi.operation_point.model.FiredepartmentForm;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import com.jpromi.operation_point.repository.UnitRepository;
import com.jpromi.operation_point.service.FiredepartmentService;
import com.jpromi.operation_point.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FiredepartmentRepository firedepartmentRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private FiredepartmentService firedepartmentService;

    @Autowired
    private UnitService unitService;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // Firedepartment
    @GetMapping("/dashboard/firedepartment")
    public String firedepartmentList(Model model) {
        List<Firedepartment> firedepartments = firedepartmentRepository.findAll();
        model.addAttribute("firedepartments", firedepartments);
        return "admin/firedepartment-list";
    }

    @GetMapping("/dashboard/firedepartment/{uuid}")
    public String firedepartmentDetail(@PathVariable UUID uuid, Model model) {
        Optional<Firedepartment> firedepartment = firedepartmentRepository.findByUuid(uuid);
        if (firedepartment.isEmpty()) {
            return "redirect:/admin/dashboard/firedepartment";
        }
        model.addAttribute("firedepartment", firedepartment.get());
        return "admin/firedepartment-details";
    }

    @PostMapping("/dashboard/firedepartment/{uuid}")
    public String updateFiredepartment(@PathVariable UUID uuid, FiredepartmentForm updatedFiredepartment) {
        if(updatedFiredepartment.getIsWrongAssignment() != null && updatedFiredepartment.getIsWrongAssignment()) {
            Unit unit = firedepartmentService.assignAsUnit(firedepartmentRepository.findByUuid(uuid).get());
            return "redirect:/admin/dashboard/unit/" + unit.getUuid();
        } else {
            Optional<Firedepartment> existingFiredepartment = firedepartmentRepository.findByUuid(uuid);
            if (existingFiredepartment.isPresent()) {
                Firedepartment firedepartment = existingFiredepartment.get();
                firedepartment.setFriendlyName(updatedFiredepartment.getFriendlyName());
                firedepartment.setAtFireDepartmentId(updatedFiredepartment.getAtFireDepartmentId());
                firedepartment.setAddressCity(updatedFiredepartment.getAddressCity());
                firedepartment.setAddressStreet(updatedFiredepartment.getAddressStreet());
                firedepartment.setAddressZipcode(updatedFiredepartment.getAddressZipcode());
                firedepartment.setAddressCountry(updatedFiredepartment.getAddressCountry());
                firedepartment.setIsVolunteer(updatedFiredepartment.getIsVolunteer());
                firedepartment.setWebsite(updatedFiredepartment.getWebsite());

                firedepartmentRepository.save(firedepartment);
            }
            return "redirect:/admin/dashboard/firedepartment";
        }
    }

    // Unit
    @GetMapping("/dashboard/unit")
    public String unitList(Model model) {
        List<Unit> units = unitRepository.findAll();
        model.addAttribute("units", units);
        return "admin/unit-list";
    }

    @GetMapping("/dashboard/unit/{uuid}")
    public String unitDetail(@PathVariable UUID uuid, Model model) {
        Optional<Unit> unit = unitRepository.findByUuid(uuid);
        if (unit.isEmpty()) {
            return "redirect:/admin/dashboard/unit";
        }
        model.addAttribute("unit", unit.get());
        return "admin/unit-details";
    }

    @PostMapping("/dashboard/unit/{uuid}")
    public String updateUnit(@PathVariable UUID uuid, FiredepartmentForm updatedUnit) {
        if (updatedUnit.getIsWrongAssignment() != null && updatedUnit.getIsWrongAssignment()) {
            Firedepartment firedepartment = unitService.assignAsFiredepartment(unitRepository.findByUuid(uuid).get());
            return "redirect:/admin/dashboard/firedepartment/" + firedepartment.getUuid();
        } else {
            Optional<Unit> existingUnit = unitRepository.findByUuid(uuid);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setFriendlyName(updatedUnit.getFriendlyName());
                unitRepository.save(unit);
            }
            return "redirect:/admin/dashboard/unit";
        }
    }

}
