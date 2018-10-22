package com.piotrjasina.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class MethodDto {
    String signature;
    String hash;
}
