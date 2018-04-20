package gdd.harrison.memdust.growingdegreeday;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by dgree on 4/20/2018.
 */

public class GDDDataCalculatorTests {

    GDDDataCalculator calculator = new GDDDataCalculator();

    @Test
    public void checkDayAndMonthCorrect(){
        int[] testArray = new int[]{1,3};
        assertArrayEquals(testArray , calculator.calculateMonthAndDayGivenADay(3));
    }
}
