package com.smartcontract.bytecode;

import com.smartcontract.solidity.SolidityService;
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
