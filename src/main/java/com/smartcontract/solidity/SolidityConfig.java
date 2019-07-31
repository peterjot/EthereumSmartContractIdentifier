package com.smartcontract.solidity;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolidityConfig {

    @Bean
    SolidityService solidityService(@NonNull SolidityFileRepository solidityFileRepository) {
        return new SolidityService(
                new SourceCodeParser(),
                solidityFileRepository
        );
    }
}
