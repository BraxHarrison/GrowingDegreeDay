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

    @Test
    public void checkBlackLayer(){
        double maturityValue = 72.0;
        assertEquals(1724.132, calculator.calculateBlackLayer(maturityValue),0.001);
    }

    @Test
    public void checkSilkingLayerCalculationCorrect(){
        double maturityValue = 72.0;
        assertEquals(925.318, calculator.calculateSilkLayer(maturityValue), 0.001);
    }

    @Test
    public void checkModelAverageCalculatesCorrectly(){
        Double[] modelReadings = new Double[]{0.0, 12.0, 13.0, 14.0, 16.0};
        assertEquals(373.0, calculator.calculateModelAverage(modelReadings, 192.0), 0.0001);
    }
}
