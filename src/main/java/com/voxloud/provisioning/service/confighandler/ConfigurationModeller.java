package com.voxloud.provisioning.service.confighandler;

import com.voxloud.provisioning.entity.Device;

public interface ConfigurationModeller {

    Device.DeviceModel getSupportedModel();

    String model(Device device);
}
