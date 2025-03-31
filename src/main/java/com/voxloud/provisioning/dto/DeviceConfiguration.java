package com.voxloud.provisioning.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class DeviceConfiguration {
    private final String username;
    private final String password;
    private final String domain;
    private final Integer port;
    private final List<String> codecs;
    private final Long timeout;
}
