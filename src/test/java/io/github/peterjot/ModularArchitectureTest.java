package io.github.peterjot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "io.github.peterjot")
public class ModularArchitectureTest {

    @ArchTest
    public static final ArchRule solidity_module_should_not_depend_on_bytecode_module =
            noClasses()
                    .that()
                    .resideInAPackage("..solidity..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..bytecode..");
}
