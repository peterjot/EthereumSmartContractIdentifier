package io.github.peterjot.solidity;

import io.github.peterjot.solidity.dto.SolidityFileDto;
import io.github.peterjot.solidity.dto.SolidityFunctionDto;
import lombok.NonNull;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

class SolidityConverter {

    private SolidityConverter() {
        throw new UnsupportedOperationException();
    }

    static <T extends Collection<SolidityFile>> Set<SolidityFileDto> fromEntity(@NonNull T solidityFunctions) {
        return solidityFunctions
                .stream()
                .map(SolidityConverter::fromEntity)
                .collect(toSet());
    }

    static SolidityFileDto fromEntity(@NonNull SolidityFile solidityFile) {
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
