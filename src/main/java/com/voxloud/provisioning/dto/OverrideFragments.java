package com.voxloud.provisioning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OverrideFragments {
    private String domain;
    private Integer port;
    private Long timeout;
}
