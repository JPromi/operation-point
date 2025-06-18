package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import com.jpromi.operation_point.service.FiredepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FiredepartmentRepository firedepartmentRepository;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "admin/logout";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/dashboard/firedepartment")
    public String firedepartmentList(Model model) {
        List<Firedepartment> firedepartments = firedepartmentRepository.findAll();
        model.addAttribute("firedepartments", firedepartments);
        return "admin/firedepartment-list";
    }
}
