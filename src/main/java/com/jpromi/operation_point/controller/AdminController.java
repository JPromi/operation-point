package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.CrawlService;
import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.Unit;
import com.jpromi.operation_point.enitiy.AppUser;
import com.jpromi.operation_point.model.CrawlServiceForm;
import com.jpromi.operation_point.model.FiredepartmentForm;
import com.jpromi.operation_point.repository.AppUserRepository;
import com.jpromi.operation_point.repository.CrawlServiceRepository;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import com.jpromi.operation_point.repository.UnitRepository;
import com.jpromi.operation_point.service.FiredepartmentService;
import com.jpromi.operation_point.service.UnitService;
import com.jpromi.operation_point.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CrawlServiceRepository crawlServiceRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        if(!appUserRepository.existsBy()) {
            return "redirect:/admin/init";
        } else {
            return "admin/login";
        }
    }

    @GetMapping("/init")
    public String init() {
        if(!appUserRepository.existsBy()) {
            return "admin/init";
        } else {
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/init")
    public String initPost(@RequestParam String username, @RequestParam String password) {
        if(!appUserRepository.existsBy()) {
            AppUser user = AppUser.builder()
                    .password(password)
                    .username(username)
                    .role("ADMIN")
                    .build();

            userService.hashUser(user);
            userService.createUser(user);
            return "redirect:/admin/login";
        } else {
            return "redirect:/admin/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // Firedepartment
    @GetMapping("/dashboard/firedepartment")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public String firedepartmentList(Model model) {
        List<Firedepartment> firedepartments = firedepartmentRepository.findAllByOrderByNameAsc();
        model.addAttribute("firedepartments", firedepartments);
        return "admin/firedepartment-list";
    }

    @GetMapping("/dashboard/firedepartment/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public String firedepartmentDetail(@PathVariable UUID uuid, Model model) {
        Optional<Firedepartment> firedepartment = firedepartmentRepository.findByUuid(uuid);
        if (firedepartment.isEmpty()) {
            return "redirect:/admin/dashboard/firedepartment";
        }
        model.addAttribute("firedepartment", firedepartment.get());
        return "admin/firedepartment-details";
    }

    @PostMapping("/dashboard/firedepartment/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public String unitList(Model model) {
        List<Unit> units = unitRepository.findAllByOrderByNameAsc();
        model.addAttribute("units", units);
        return "admin/unit-list";
    }

    @GetMapping("/dashboard/unit/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public String unitDetail(@PathVariable UUID uuid, Model model) {
        Optional<Unit> unit = unitRepository.findByUuid(uuid);
        if (unit.isEmpty()) {
            return "redirect:/admin/dashboard/unit";
        }
        model.addAttribute("unit", unit.get());
        return "admin/unit-details";
    }

    @PostMapping("/dashboard/unit/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
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

    // Admin
    @GetMapping("/root")
    @PreAuthorize("hasRole('ADMIN')")
    public String rootDashboard() {
        return "admin/root/root-dashboard";
    }

    @GetMapping("/root/crawler")
    @PreAuthorize("hasRole('ADMIN')")
    public String crawlerDashboard(Model model) {
        List<CrawlService> services = crawlServiceRepository.findAllByOrderByNameAsc();
        model.addAttribute("services", services);
        return "admin/root/crawler";
    }

    @PostMapping("/root/crawler")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCrawlService(@ModelAttribute("services") CrawlServiceForm services) {
        List<CrawlService> servicesOriginal = crawlServiceRepository.findAllByOrderByNameAsc();
        for (CrawlServiceForm.CrawlServiceFormService service : services.getServices()) {
            Optional<CrawlService> existingService = servicesOriginal.stream()
                    .filter(s -> s.getName().equals(service.getName()))
                    .findFirst();
            if (existingService.isPresent()) {
                existingService.get().setIsEnabled(service.getIsEnabled());
                crawlServiceRepository.save(existingService.get());
            }
        }
        return "redirect:/admin/root/crawler";
    }

}
