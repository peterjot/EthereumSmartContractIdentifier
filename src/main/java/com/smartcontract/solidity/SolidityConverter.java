package com.smartcontract.solidity;

import com.smartcontract.solidity.dto.SolidityFileDto;
import com.smartcontract.solidity.dto.SolidityFunctionDto;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

class SolidityConverter {

    private SolidityConverter() {
        throw new UnsupportedOperationException();
    }

    static <T extends Collection<SolidityFile>> Set<SolidityFileDto> fromEntity(T solidityFunctions) {
        return solidityFunctions
                .stream()
                .map(SolidityConverter::fromEntity)
                .collect(toSet());
    }

    static SolidityFileDto fromEntity(SolidityFile solidityFile) {
        return new SolidityFileDto(
                solidityFile.getSourceCodeHash(),
                solidityFile.getSourceCode(),
                fromFunctionEntity(solidityFile.getSolidityFunctions())
        );
    }

    private static SolidityFunctionDto fromFunctionEntity(SolidityFunction solidityFunction) {
        return new SolidityFunctionDto(solidityFunction.getSelector(), solidityFunction.getSignature());
    }

    private static <T extends Collection<SolidityFunction>>
    Set<SolidityFunctionDto> fromFunctionEntity(T solidityFunctions) {

        return solidityFunctions
                .stream()
                .map(SolidityConverter::fromFunctionEntity)
                .collect(toSet());
    }
}
