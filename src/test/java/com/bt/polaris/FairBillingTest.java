package com.bt.polaris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static com.bt.polaris.FairBilling.processLogFile;

class FairBillingTest {
    @Test
    public void processLogFileTest(){
        String actualResult = processLogFile("C:\\Users\\615111023\\Code\\fair-billing\\logfile2.txt");
        String Expected = "ALICE99 4 240\n" +
                "CHARLIE 4 38\n";
        assertEquals(Expected,actualResult);
    }

}