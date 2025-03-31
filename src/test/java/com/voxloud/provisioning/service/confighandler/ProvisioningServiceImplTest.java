package com.voxloud.provisioning.service.confighandler;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.NotFoundException;
import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.service.ProvisioningServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProvisioningServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ConfigurationModeller configurationModeller;

    @InjectMocks
    private ProvisioningServiceImpl provisioningService;

    private Device device;

    @BeforeEach
    void setUp() {
        device = new Device();
        device.setMacAddress("00:1A:2B:3C:4D:5E");
        device.setModel(Device.DeviceModel.DESK);
    }

    @Test
    void getProvisioningFile_ShouldReturnConfig_WhenDeviceExists() {
        when(deviceRepository.findByMacAddress(device.getMacAddress())).thenReturn(Optional.of(device));
        when(configurationModeller.getSupportedModel()).thenReturn(Device.DeviceModel.DESK);
        when(configurationModeller.model(device)).thenReturn("mocked-config");

        provisioningService = new ProvisioningServiceImpl(deviceRepository, Collections.singletonList(configurationModeller));

        String result = provisioningService.getProvisioningFile(device.getMacAddress());

        assertEquals("mocked-config", result);
        verify(deviceRepository, times(1)).findByMacAddress(device.getMacAddress());
        verify(configurationModeller, times(1)).model(device);
    }

    @Test
    void getProvisioningFile_ShouldThrowNotFoundException_WhenDeviceNotFound() {
        when(deviceRepository.findByMacAddress(device.getMacAddress())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> provisioningService.getProvisioningFile(device.getMacAddress()));
    }
}