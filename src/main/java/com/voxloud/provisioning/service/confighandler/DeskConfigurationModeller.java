package com.voxloud.provisioning.service.confighandler;

import com.voxloud.provisioning.dto.DeviceConfiguration;
import com.voxloud.provisioning.dto.OverrideFragments;
import com.voxloud.provisioning.entity.Device;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.Properties;

@Log4j2
@Component
public class DeskConfigurationModeller implements ConfigurationModeller {
    @Value("${provisioning.domain}")
    private String defaultDomain;
    @Value("${provisioning.port}")
    private Integer defaultPort;
    @Value("${provisioning.codecs}")
    private String defaultCodecs;

    @Override
    public Device.DeviceModel getSupportedModel() {
        return Device.DeviceModel.DESK;
    }

    @SneakyThrows
    @Override
    public String model(Device device) {
        DeviceConfiguration deviceConfiguration = DeviceConfiguration.builder()
                .username(device.getUsername())
                .password(device.getPassword())
                .domain(defaultDomain)
                .port(defaultPort)
                .build();

        if (device.getOverrideFragment() != null && !device.getOverrideFragment().isEmpty()) {
            log.info("override fragment is {}", device.getOverrideFragment());
            Properties properties = new Properties();
            properties.load(new StringReader(device.getOverrideFragment()));

            OverrideFragments overrideFragments = OverrideFragments.builder()
                    .domain(properties.getProperty("domain"))
                    .port(Integer.parseInt(properties.getProperty("port")))
                    .timeout(Long.parseLong(properties.getProperty("timeout")))
                    .build();

            deviceConfiguration = deviceConfiguration.toBuilder()
                    .domain(overrideFragments.getDomain())
                    .port(overrideFragments.getPort())
                    .timeout(overrideFragments.getTimeout())
                    .build();
        }

        StringBuilder configString = new StringBuilder();
        configString.append("username=").append(deviceConfiguration.getUsername()).append(System.lineSeparator());
        configString.append("password=").append(deviceConfiguration.getPassword()).append(System.lineSeparator());
        configString.append("domain=").append(deviceConfiguration.getDomain()).append(System.lineSeparator());
        configString.append("port=").append(deviceConfiguration.getPort()).append(System.lineSeparator());
        configString.append("codecs=").append(defaultCodecs).append(System.lineSeparator());

        if (deviceConfiguration.getTimeout() != null) {
            configString.append("timeout=").append(deviceConfiguration.getTimeout()).append(System.lineSeparator());
        }

        return configString.toString();
    }
}
