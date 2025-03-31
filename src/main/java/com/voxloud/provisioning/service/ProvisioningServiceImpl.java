package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.NotFoundException;
import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.service.confighandler.ConfigurationModeller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {
    private final DeviceRepository deviceRepository;
    private final List<ConfigurationModeller> configurationModellers;

    public String getProvisioningFile(String macAddress) {
        Device device = deviceRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new NotFoundException("No such device: " + macAddress));

        ConfigurationModeller handler = configurationModellers.stream()
                .filter(it -> it.getSupportedModel().equals(device.getModel()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No such model: " + device.getModel()));

        return handler.model(device);
    }
}
