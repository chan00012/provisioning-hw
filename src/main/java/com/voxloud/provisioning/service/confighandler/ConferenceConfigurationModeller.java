package com.voxloud.provisioning.service.confighandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.dto.DeviceConfiguration;
import com.voxloud.provisioning.dto.OverrideFragments;
import com.voxloud.provisioning.entity.Device;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor
class ConferenceConfigurationModeller implements ConfigurationModeller {
    private final ObjectMapper objectMapper;

    @Value("${provisioning.domain}")
    private String defaultDomain;
    @Value("${provisioning.port}")
    private Integer defaultPort;
    @Value("${provisioning.codecs}")
    private String defaultCodecs;

    @Override
    public Device.DeviceModel getSupportedModel() {
        return Device.DeviceModel.CONFERENCE;
    }

    @SneakyThrows
    @Override
    public String model(Device device) {
        DeviceConfiguration deviceConfiguration = DeviceConfiguration.builder()
                .username(device.getUsername())
                .password(device.getPassword())
                .domain(defaultDomain)
                .port(defaultPort)
                .codecs(Arrays.stream(defaultCodecs.split(",")).collect(Collectors.toList()))
                .build();

        if (device.getOverrideFragment() != null && !device.getOverrideFragment().isEmpty()) {
            log.info("Override fragments is present for device: {}", device.getMacAddress());
            OverrideFragments overrideFragments = objectMapper.readValue(device.getOverrideFragment(), OverrideFragments.class);

            deviceConfiguration = deviceConfiguration.toBuilder()
                    .domain(overrideFragments.getDomain())
                    .port(overrideFragments.getPort())
                    .timeout(overrideFragments.getTimeout())
                    .build();
        }

        return objectMapper.writeValueAsString(deviceConfiguration);
    }
}
