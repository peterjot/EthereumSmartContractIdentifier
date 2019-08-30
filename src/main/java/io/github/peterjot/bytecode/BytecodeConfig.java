package io.github.peterjot.bytecode;

import io.github.peterjot.solidity.SolidityService;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BytecodeConfig {

    @Bean
    BytecodeService bytecodeService(@NonNull SolidityService solidityService) {
        return new BytecodeService(
                solidityService,
                new Disassembler());
    }
}
