package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class ProvisioningController {

    private final ProvisioningService provisioningService;

    @GetMapping("/provisioning/{macAddress}")
    public ResponseEntity<String> getProvisioningConfiguration(@PathVariable String macAddress) {
        log.info("get provisioning configuration for macAddress: {}", macAddress);
        String configuration = provisioningService.getProvisioningFile(macAddress);

        return ResponseEntity.ok(configuration);
    }
}