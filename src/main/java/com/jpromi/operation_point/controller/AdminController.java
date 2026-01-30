package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.entity.*;
import com.jpromi.operation_point.model.AppUserForm;
import com.jpromi.operation_point.model.CrawlServiceForm;
import com.jpromi.operation_point.model.FiredepartmentForm;
import com.jpromi.operation_point.repository.*;
import com.jpromi.operation_point.service.FileStorageService;
import com.jpromi.operation_point.service.FiredepartmentService;
import com.jpromi.operation_point.service.UnitService;
import com.jpromi.operation_point.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final FiredepartmentRepository firedepartmentRepository;
    private final UnitRepository unitRepository;
    private final FiredepartmentService firedepartmentService;
    private final UnitService unitService;
    private final CrawlServiceRepository crawlServiceRepository;
    private final AppUserRepository appUserRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(FiredepartmentRepository firedepartmentRepository, UnitRepository unitRepository, FiredepartmentService firedepartmentService, UnitService unitService, CrawlServiceRepository crawlServiceRepository, AppUserRepository appUserRepository, UserService userService, FileStorageService fileStorageService, UserRepository userRepository) {
        this.firedepartmentRepository = firedepartmentRepository;
        this.unitRepository = unitRepository;
        this.firedepartmentService = firedepartmentService;
        this.unitService = unitService;
        this.crawlServiceRepository = crawlServiceRepository;
        this.appUserRepository = appUserRepository;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
    }

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
    public String firedepartmentList(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "friendlyName") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @PageableDefault(size = 50) Pageable pageable,
            Model model
    ) {
        Sort.Direction direction = dir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(direction, sort)
        );

        Page<Firedepartment> page = firedepartmentRepository.findByFriendlyNameContainingIgnoreCase(q, p);

        model.addAttribute("page", page);
        model.addAttribute("q", q);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "admin/firedepartment-list";
    }

    @GetMapping("/dashboard/firedepartment/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public String firedepartmentDetail(@PathVariable UUID uuid, Model model) {
        Optional<Firedepartment> firedepartment = firedepartmentRepository.findByUuid(uuid);

        if (firedepartment.isEmpty()) {
            return "redirect:/admin/dashboard/firedepartment";
        }

        List<FiredepartmentLink> links = new ArrayList<>(firedepartment.get().getLinks());

        for (int i = 0; i < 3; i++) {
            links.add(FiredepartmentLink.builder().build());
        }

        model.addAttribute("firedepartment", firedepartment.get());
        model.addAttribute("links", links);
        model.addAttribute("logoUrl", fileStorageService.getExternalUrl(firedepartment.get().getLogo()));
        model.addAttribute("bannerUrl", fileStorageService.getExternalUrl(firedepartment.get().getBanner()));
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
                firedepartment.setNameId((updatedFiredepartment.getNameId() != null && !updatedFiredepartment.getNameId().isEmpty()) ? sanitize(updatedFiredepartment.getNameId()) : null);
                firedepartment.setFriendlyName((updatedFiredepartment.getFriendlyName() != null && !updatedFiredepartment.getFriendlyName().isEmpty()) ? updatedFiredepartment.getFriendlyName() : null);
                firedepartment.setAtFireDepartmentId((updatedFiredepartment.getAtFireDepartmentId() != null && !updatedFiredepartment.getAtFireDepartmentId().isEmpty()) ? updatedFiredepartment.getAtFireDepartmentId() : null);
                firedepartment.setAddressCity((updatedFiredepartment.getAddressCity() != null && !updatedFiredepartment.getAddressCity().isEmpty()) ? updatedFiredepartment.getAddressCity() : null);
                firedepartment.setAddressStreet((updatedFiredepartment.getAddressStreet() != null && !updatedFiredepartment.getAddressStreet().isEmpty()) ? updatedFiredepartment.getAddressStreet() : null);
                firedepartment.setAddressZipcode((updatedFiredepartment.getAddressZipcode() != null && !updatedFiredepartment.getAddressZipcode().isEmpty()) ? updatedFiredepartment.getAddressZipcode() : null);
                firedepartment.setAddressFederalState((updatedFiredepartment.getAddressFederalState() != null && !updatedFiredepartment.getAddressFederalState().isEmpty()) ? updatedFiredepartment.getAddressFederalState() : null);
                firedepartment.setAddressCountry((updatedFiredepartment.getAddressCountry() != null && !updatedFiredepartment.getAddressCountry().isEmpty()) ? updatedFiredepartment.getAddressCountry() : null);
                firedepartment.setIsVolunteer(updatedFiredepartment.getIsVolunteer() != null ? updatedFiredepartment.getIsVolunteer() : false);
                firedepartment.setIsHidden(updatedFiredepartment.getIsHidden() != null ? updatedFiredepartment.getIsHidden() : false);

                // save images
                if (updatedFiredepartment.getLogo() != null && !updatedFiredepartment.getLogo().isEmpty() && !updatedFiredepartment.getLogoDelete()) {
                    firedepartment.setLogo(
                            fileStorageService.saveFile(updatedFiredepartment.getLogo())
                    );
                } else if (updatedFiredepartment.getLogoDelete()) {
                    FileData logo = firedepartment.getLogo();
                    firedepartment.setLogo(null);
                    fileStorageService.deleteFile(logo);
                }

                if (updatedFiredepartment.getBanner() != null && !updatedFiredepartment.getBanner().isEmpty() && !updatedFiredepartment.getBannerDelete()) {
                    firedepartment.setBanner(
                            fileStorageService.saveFile(updatedFiredepartment.getBanner())
                    );
                } else if (updatedFiredepartment.getBannerDelete()) {

                    firedepartment.setBanner(null);
                    fileStorageService.deleteFile(firedepartment.getBanner());
                }

                List<FiredepartmentLink> existingLinks = firedepartment.getLinks();
                List<FiredepartmentLink> oldLinksFinal = new ArrayList<>(existingLinks);
                existingLinks.clear();

                // update links
                for (FiredepartmentForm.FiredepartmentFormLinks link : updatedFiredepartment.getLinks()) {
                    if (link.getId() != null) {
                        // update existing link
                        Optional<FiredepartmentLink> existingLink = oldLinksFinal.stream()
                                .filter(l -> l.getId().equals(link.getId()))
                                .findFirst();

                        if (existingLink.isPresent()) {
                            if(
                                (link.getName() != null && !link.getName().isEmpty()) ||
                                (link.getUrl() != null && !link.getUrl().isEmpty())
                            ) {
                                existingLink.get().setName((link.getName() != null && !link.getName().isEmpty()) ? link.getName() : null);
                                existingLink.get().setType((link.getType() != null && !link.getType().isEmpty()) ? link.getType() : null);
                                existingLink.get().setUrl((link.getUrl() != null && !link.getUrl().isEmpty()) ? link.getUrl() : null);
                                existingLinks.add(existingLink.get());
                            }
                        }
                    } else if (
                            (link.getName() != null && !link.getName().isEmpty()) ||
                            (link.getUrl() != null && !link.getUrl().isEmpty())
                    ) {
                        // create new link
                        FiredepartmentLink newLink = FiredepartmentLink.builder()
                                .firedepartment(firedepartment)
                                .name((link.getName() != null && !link.getName().isEmpty()) ? link.getName() : null)
                                .type((link.getType() != null && !link.getType().isEmpty()) ? link.getType() : null)
                                .url((link.getUrl() != null && !link.getUrl().isEmpty()) ? link.getUrl() : null)
                                .build();
                        existingLinks.add(newLink);
                    }
                }

                firedepartment.setLinks(existingLinks);

                firedepartmentRepository.save(firedepartment);
            }
            return "redirect:/admin/dashboard/firedepartment";
        }
    }

    // Unit
    @GetMapping("/dashboard/unit")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public String unitList(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "friendlyName") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @PageableDefault(size = 50) Pageable pageable,
            Model model
            ) {
        Sort.Direction direction = dir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(direction, sort)
        );

        Page<Unit> page = unitRepository.findByFriendlyNameContainingIgnoreCase(q, p);

        model.addAttribute("page", page);
        model.addAttribute("q", q);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
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
                unit.setFriendlyName((updatedUnit.getFriendlyName() != null && !updatedUnit.getFriendlyName().isEmpty()) ? updatedUnit.getFriendlyName() : null);
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
                existingService.get().setEnabled(service.getIsEnabled());
                crawlServiceRepository.save(existingService.get());
            }
        }
        return "redirect:/admin/root/crawler";
    }

    @GetMapping("/root/user")
    @PreAuthorize("hasRole('ADMIN')")
    public String userDashboard(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "asc") String dir,
            @PageableDefault(size = 50) Pageable pageable,
            Model model
    ) {
        Sort.Direction direction = dir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(direction, "username")
        );

        Page<AppUser> page = appUserRepository.findByUsernameContainingIgnoreCase(q, p);

        model.addAttribute("page", page);
        model.addAttribute("q", q);
        model.addAttribute("dir", dir);
        return "admin/root/user-list";
    }

    @GetMapping("/root/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String userDetail(@PathVariable("id") Long id, Model model) {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            return "redirect:/admin/root/user";
        }
        model.addAttribute("user", user.get());
        return "admin/root/user-details";
    }

    @GetMapping("/root/user/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String userDetailNew(Model model) {
        AppUser user = AppUser.builder().id(0L).username("").role("").build();
        model.addAttribute("user", user);
        return "admin/root/user-details";
    }

    @PostMapping("/root/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@PathVariable("id") Long id, AppUserForm updatedUser) {
        Optional<AppUser> existingUser = appUserRepository.findById(id);
        if (existingUser.isPresent()) {
            AppUser user = existingUser.get();
            if (updatedUser.getDelete() != null && updatedUser.getDelete()) {
                userRepository.delete(user);
                return "redirect:/admin/root/user";
            } else {
                user.setUsername(updatedUser.getUsername());
                user.setRole(updatedUser.getRole());
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    user.setPassword(updatedUser.getPassword());
                    userService.hashUser(user);
                }
                userRepository.save(user);
                return "redirect:/admin/root/user";
            }
        }
        return "redirect:/admin/root/user";
    }

    @PostMapping("/root/user/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(AppUserForm updatedUser) {

        AppUser user = AppUser.builder()
                .username(updatedUser.getUsername())
                .role(updatedUser.getRole())
                .build();

        user.setPassword(updatedUser.getPassword());

        try {
            userService.hashUser(user);
            userService.createUser(user);

            return "redirect:/admin/root/user";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  "redirect:/admin/root/user";
    }

    private String sanitize(String input) {
        if (input == null) return null;
        String sanitized = input.trim().toLowerCase();
        sanitized = sanitized.replaceAll("\\s+", "-");
        sanitized = sanitized.replaceAll("[^a-z0-9\\-_]", "");
        return sanitized;
    }

}
