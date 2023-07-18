package com.bt.polaris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.Stack;

class FairBillingTest {
    PersonSessionData TestSessionData;
    LocalTime earliestTime = LocalTime.parse("00:00:00");
    LocalTime latestTime = LocalTime.parse("23:59:59");

    LocalTime startTime = LocalTime.parse("02:02:25");

    LocalTime endTime = LocalTime.parse("02:02:30");
    LocalTime endTime2 = LocalTime.parse("23:59:58");

    @BeforeEach
    public void init() {
        TestSessionData = new PersonSessionData();
    }

    @Test
    public void addStartTest(){
        TestSessionData.addStart(startTime);

        Stack<LocalTime> ExpectedSessionStacks = new Stack<>();
        ExpectedSessionStacks.push(startTime);

        assertEquals(1, TestSessionData.sessionCounts);
        assertEquals(0L, TestSessionData.sessionDurations);
        assertEquals(ExpectedSessionStacks, TestSessionData.sessionStacks);

    }
    @Test
    public void addEndWithMatchingStartTest(){
        TestSessionData.addStart(startTime);

        TestSessionData.addEnd(endTime,earliestTime);

        assertEquals(1, TestSessionData.sessionCounts);
        assertEquals(5L, TestSessionData.sessionDurations);
        assertTrue(TestSessionData.sessionStacks.empty());

    }
    @Test
    public void addEndWithoutMatchingStartTest(){
        TestSessionData.addEnd(endTime,earliestTime);

        assertEquals(1, TestSessionData.sessionCounts);
        assertEquals(7350L, TestSessionData.sessionDurations);
        assertTrue(TestSessionData.sessionStacks.empty());
    }

    @Test
    public void addExtraStartTest() {
        TestSessionData.sessionStacks.push(endTime);
        TestSessionData.sessionStacks.push(endTime2);

        TestSessionData.addExtraStart(latestTime);

        assertEquals(0, TestSessionData.sessionCounts);
        assertEquals(79050L, TestSessionData.sessionDurations);
        assertTrue(TestSessionData.sessionStacks.empty());
    }

    @Test
    public void outputResultTest() {
        String result = TestSessionData.outputResult();
        assertEquals("0 0", result);
    }
}