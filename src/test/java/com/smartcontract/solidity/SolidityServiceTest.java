package com.smartcontract.solidity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
public class SolidityServiceTest {

    @MockBean
    SolidityFileRepository solidityFileRepository;
    private SolidityService solidityService;

    @Before
    public void setUp() {
        solidityService = new SolidityService(solidityFileRepository);
    }


    @Test
    public void shouldGetFunctionsFromFile() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<>(Arrays.asList(
                new SolidityFunction("10fdf92a", "commentFromAccount(uint256)"),
                new SolidityFunction("8ebb4c15", "comments(uint256)"),
                new SolidityFunction("cd65eabe", "commentsFromPost(uint256,uint256)"),
                new SolidityFunction("5a9cfac8", "hasPosts()"),
                new SolidityFunction("09787a2c", "newComment(uint256,string)"),
                new SolidityFunction("23bcaae9", "newPost(string)")
                ,
                new SolidityFunction("0b1e7f83", "posts(uint256)"),
                new SolidityFunction("acdc2cde", "postsFromAccount(address,uint256)")));

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }

    @Test
    @Ignore
    //TODO: W przypadku jednego pliku z wieloma kontraktami, jest wyszukiwanych wiecej nic w bytekodzie
    public void test2() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<>(Arrays.asList(
                new SolidityFunction("81830593", "adminAddr()"),
                new SolidityFunction("3420c428", "AdminPercent()"),
                new SolidityFunction("09efa259", "AdvertisePersent()"),
                new SolidityFunction("799ea371", "DividendsPercent()"),
                new SolidityFunction("235abffd", "FirstLevelReferrerPercent()"),
                new SolidityFunction("a040efeb", "SecondLevelReferrerPercent()"),
                new SolidityFunction("975b6f28", "advertiseAddr()"),
                new SolidityFunction("ecbdbb32", "balanceETH()"),
                new SolidityFunction("17bd6e37", "bestInvestor()"),
                new SolidityFunction("5c3026d8", "bestPromoter()"),
                new SolidityFunction("88072c78", "dividendsPeriod()"),
                new SolidityFunction("7404417c", "doWaiver()"),
                new SolidityFunction("ed442e14", "getDividends()"),
                new SolidityFunction("03f9c793", "invest(address)"),
                new SolidityFunction("dbcbaca4", "investorInfo(address)"),
                new SolidityFunction("653c3174", "investorsNumber()"),
                new SolidityFunction("73ad468a", "maxBalance()"),
                new SolidityFunction("3d7ac9f8", "minInvesment()"),
                new SolidityFunction("06fdde03", "name()"),
                new SolidityFunction("8da5cb5b", "owner()"),
                new SolidityFunction("984d4a93", "setAdminsAddress(address)"),
                new SolidityFunction("cb192f2c", "setAdvertisingAddress(address)"),
                new SolidityFunction("535bc861", "statistic(uint256)"),
                new SolidityFunction("5216aeec", "totalInvested()"),
                new SolidityFunction("29b8caff", "totalInvestors()"),
                new SolidityFunction("eafecc7a", "waveStartup()"))
        );

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test2.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, containsInAnyOrder(expectedSolidityFunctions));
    }

    @Test
    public void test3() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<>(Arrays.asList(
                new SolidityFunction("52382794", "ODDS(uint256)"),
                new SolidityFunction("e210c049", "AMOUNTS(uint256)"),
                new SolidityFunction("753feadd", "MASKS(uint256)"),
                new SolidityFunction("c9e525df", "N()"),
                new SolidityFunction("68be9822", "OWNER_AMOUNT()"),
                new SolidityFunction("833afd43", "OWNER_MIN()"),
                new SolidityFunction("2e1371bd", "OWNER_PERCENT()"),
                new SolidityFunction("22b80ff8", "_cash()"),
                new SolidityFunction("f851a440", "admin()"),
                new SolidityFunction("793cd71e", "cashOut()"),
                new SolidityFunction("502d0c30", "firstBN()"),
                new SolidityFunction("1ccf6955", "getBets(uint256)"),
                new SolidityFunction("a2f77bcc", "getGame(uint256)"),
                new SolidityFunction("41c0e1b5", "kill()"),
                new SolidityFunction("60b79784", "lockedIn()"),
                new SolidityFunction("7206a199", "open(uint256,bytes32,uint256)"),
                new SolidityFunction("f74797d1", "open2(uint256,bytes32,bytes32,uint256)"),
                new SolidityFunction("f20eaeb8", "output()"),
                new SolidityFunction("8da5cb5b", "owner()"),
                new SolidityFunction("03edf914", "placeBet(uint256,uint8)"),
                new SolidityFunction("715018a6", "renounceOwnership()"),
                new SolidityFunction("704b6c02", "setAdmin(address)"),
                new SolidityFunction("8afc2d35", "setN(uint8)"),
                new SolidityFunction("31af8eb0", "setOwnerMin(uint256)"),
                new SolidityFunction("ac7e64d5", "setOwnerPercent(uint256)"),
                new SolidityFunction("f2fde38b", "transferOwnership(address)"),
                new SolidityFunction("3ccfd60b", "withdraw()")
        ));

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test3.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }


    @Test
    public void shouldGetFunctionsFromFileWhenGeneratingGetterForMappingAndArray() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<>(Arrays.asList(
                new SolidityFunction("e5265c8a", "mymap1(uint256,uint232)"),
                new SolidityFunction("f55f218e", "mymap2(uint256,uint256,uint256)"),
                new SolidityFunction("238f6a0d", "mymap3(uint256,uint256,uint256,uint256)"),
                new SolidityFunction("5732e7a9", "mymap4(uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256)")));

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test4MappingAndArrays.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }

    @Test
    public void diffrentArrays() throws Exception {

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test5Arrays.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        System.out.println("AAAAAAAAAAAAAA"+actualSolidityFunctions);
        assertThat(actualSolidityFunctions.size(), equalTo(8));
    }

    @Test
    public void test6() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<>(Arrays.asList(
                new SolidityFunction("3e3ee859", "NewQuestion(string,bytes32)"),
                new SolidityFunction("f50ab247", "StopGame()"),
                new SolidityFunction("3853682c", "Try(string)"),
                new SolidityFunction("59988dce", "newQuestioner(address)"),
                new SolidityFunction("3fad9ae0", "question()"),
                new SolidityFunction("fd26c460", "set_game(string,string)")
        ));

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test6.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }
}
