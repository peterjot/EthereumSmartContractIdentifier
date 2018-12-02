package com.piotrjasina.solidity;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Function {
    String signature;
    String selector;
}
