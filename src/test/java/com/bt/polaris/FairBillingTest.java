package com.bt.polaris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static com.bt.polaris.FairBilling.processLogFile;

class FairBillingTest {
    @Test
    public void processLogFileTest(){
        String actualResult = processLogFile("C:\\Users\\615111023\\Code\\fair-billing\\src\\test\\resources\\logfile.txt");
        String Expected = "ALICE99 4 240\r\n" +
                "CHARLIE 3 37\r\n";
        assertEquals(Expected,actualResult);
    }
    @Test
    public void processLogFileTestWithInvalidInput(){
        String actualResult = processLogFile("C:\\Users\\615111023\\Code\\fair-billing\\src\\test\\resources\\logfile2.txt");
        String Expected = "ALICE99 4 240\r\n" +
                "CHARLIE 4 38\r\n";
        assertEquals(Expected,actualResult);
    }

    @Test
    public void processLogFileTestWithStartOnly(){
        String actualResult = processLogFile("C:\\Users\\615111023\\Code\\fair-billing\\src\\test\\resources\\logfile3.txt");
        String Expected = "ALICE99 1 0\r\n";
        assertEquals(Expected,actualResult);
    }
    @Test
    public void processLogFileTestWithTwoExtraEnd(){
        String actualResult = processLogFile("C:\\Users\\615111023\\Code\\fair-billing\\src\\test\\resources\\logfile4.txt");
        String Expected = "ALICE99 3 124\r\n";
        assertEquals(Expected,actualResult);
    }
}