package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.FiredepartmentChange;
import com.jpromi.operation_point.mapper.FiredepartmentChangeMapper;
import com.jpromi.operation_point.mapper.FiredepartmentResponseMapper;
import com.jpromi.operation_point.model.CommunityImporvementFiredepartmentRequest;
import com.jpromi.operation_point.model.FiredepartmentResponse;
import com.jpromi.operation_point.repository.FiredepartmentChangeRepository;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/community-improvement")
@RestController("CommunityImprovementController")
public class CommunityImprovementController {

    @Autowired
    private FiredepartmentChangeMapper firedepartmentChangeMapper;

    @Autowired
    private FiredepartmentResponseMapper firedepartmentResponseMapper;

    @Autowired
    private FiredepartmentChangeRepository firedepartmentChangeRepository;

    @PostMapping(value = "firedepartment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> improveFiredepartment(@ModelAttribute CommunityImporvementFiredepartmentRequest body, HttpServletRequest request) {
        if (body.getChangedEmail() == null || body.getChangedEmail().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        String userAgent = request.getHeader("User-Agent");

        FiredepartmentChange change = firedepartmentChangeMapper.fromCommunityImporvementFiredepartmentRequest(body, null, null, ip, userAgent);
        change.setChangeType("update");
        change = firedepartmentChangeRepository.save(change);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "firedepartment/validate")
    public ResponseEntity<Void> validateFiredepartment(@RequestBody UUID changeUuid) {
        Optional<FiredepartmentChange> changeOpt = firedepartmentChangeRepository.findByUuid(changeUuid);

        if (changeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        changeOpt.get().setIsVerified(true);
        firedepartmentChangeRepository.save(changeOpt.get());
        return ResponseEntity.ok().build();
    }

    @GetMapping("firedepartment/{uuid}")
    public ResponseEntity<FiredepartmentResponse> getAll(@PathVariable UUID uuid) {
        Optional<FiredepartmentChange> changeOpt = firedepartmentChangeRepository.findByUuidAndIsVerifiedTrue(uuid);

        if (changeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(firedepartmentResponseMapper.fromFiredepartmentChange(changeOpt.get()));
        }

    }
}
