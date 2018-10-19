package com.piotrjasina.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class MethodDto {
    String signature;
    String hash;
}
