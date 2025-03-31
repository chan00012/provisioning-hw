package com.voxloud.provisioning.service.confighandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.dto.DeviceConfiguration;
import com.voxloud.provisioning.dto.OverrideFragments;
import com.voxloud.provisioning.entity.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConferenceConfigurationModellerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ConferenceConfigurationModeller conferenceConfigurationModeller;

    private Device device;
    private OverrideFragments overrideFragments;

    @BeforeEach
    public void setUp() {
        // Set default values for the fields
        ReflectionTestUtils.setField(conferenceConfigurationModeller, "defaultDomain", "default.domain.com");
        ReflectionTestUtils.setField(conferenceConfigurationModeller, "defaultPort", 5060);
        ReflectionTestUtils.setField(conferenceConfigurationModeller, "defaultCodecs", "codec1,codec2");

        // Initialize a Device object
        device = new Device();
        device.setUsername("user");
        device.setPassword("pass");
        device.setMacAddress("00:11:22:33:44:55");

        // Initialize OverrideFragments
        overrideFragments = OverrideFragments.builder()
                .domain("override.domain.com")
                .port(5070)
                .timeout(30L)
                .build();
    }

    @Test
    void testModelWithoutOverrideFragments() throws Exception {
        // GIVEN
        DeviceConfiguration expectedConfig = DeviceConfiguration.builder()
                .username("user")
                .password("pass")
                .domain("default.domain.com")
                .port(5060)
                .codecs(Arrays.asList("codec1", "codec2"))
                .build();

        when(objectMapper.writeValueAsString(expectedConfig)).thenReturn("expectedJson");

        // WHEN
        String result = conferenceConfigurationModeller.model(device);

        // THEN
        assertEquals("expectedJson", result);
    }

    @Test
    void testModelWithOverrideFragments() throws Exception {
        //GIVEN
        String overrideJson = "{\"domain\":\"override.domain.com\",\"port\":5070,\"timeout\":30}";
        device.setOverrideFragment(overrideJson);

        // Set up the expected DeviceConfiguration with overrides
        DeviceConfiguration expectedConfig = DeviceConfiguration.builder()
                .username("user")
                .password("pass")
                .domain("override.domain.com")
                .port(5070)
                .codecs(Arrays.asList("codec1", "codec2"))
                .timeout(30L)
                .build();


        when(objectMapper.readValue(overrideJson, OverrideFragments.class)).thenReturn(overrideFragments);
        when(objectMapper.writeValueAsString(expectedConfig)).thenReturn("expectedJsonWithOverrides");

        // WHEN
        String result = conferenceConfigurationModeller.model(device);

        // THEN
        assertEquals("expectedJsonWithOverrides", result);
    }
}
