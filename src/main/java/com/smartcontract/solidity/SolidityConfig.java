package com.smartcontract.solidity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolidityConfig {

    @Bean
    SolidityService solidityService(SolidityFileRepository solidityFileRepository) {
        return new SolidityService(
                solidityFileRepository,
                new SourceCodeParser());
    }
}
