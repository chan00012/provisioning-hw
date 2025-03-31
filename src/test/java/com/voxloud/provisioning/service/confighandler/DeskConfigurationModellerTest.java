package com.voxloud.provisioning.service.confighandler;

import com.voxloud.provisioning.entity.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DeskConfigurationModellerTest {

    @InjectMocks
    private DeskConfigurationModeller deskConfigurationModeller;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(deskConfigurationModeller, "defaultDomain", "default.com");
        ReflectionTestUtils.setField(deskConfigurationModeller, "defaultPort", 5060);
        ReflectionTestUtils.setField(deskConfigurationModeller, "defaultCodecs", "G711,G729");
    }

    @Test
    void testModelWithDefaultValues() {
        Device device = new Device();
        device.setUsername("user123");
        device.setPassword("pass123");
        device.setOverrideFragment(null);

        String result = deskConfigurationModeller.model(device);

        String expectedConfig = "username=user123\n" +
                "password=pass123\n" +
                "domain=default.com\n" +
                "port=5060\n" +
                "codecs=G711,G729\n";

        assertEquals(expectedConfig, result);
    }

    @Test
    void testModelWithOverrideFragments() {
        Device device = new Device();
        device.setUsername("user123");
        device.setPassword("pass123");
        device.setOverrideFragment("domain=custom.com\nport=5070\ntimeout=30");

        String result = deskConfigurationModeller.model(device);

        String expectedConfig = "username=user123\n" +
                "password=pass123\n" +
                "domain=custom.com\n" +
                "port=5070\n" +
                "codecs=G711,G729\n" +
                "timeout=30\n";

        assertEquals(expectedConfig, result);
    }
}
