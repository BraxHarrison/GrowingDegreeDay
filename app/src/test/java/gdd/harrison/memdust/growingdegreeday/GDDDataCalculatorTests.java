package gdd.harrison.memdust.growingdegreeday;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

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

    @Test
    public void checkCalculatingAverageCorrectly(){
        ArrayList<ArrayList<Double>> testAveragesArrayList = new ArrayList<>();
        ArrayList<Double> newListOfGDD = new ArrayList<Double>(){{
            add(0.0);
            add(1.0);
            add(4.0);
            add(6.0);
            add(12.0);
        }};
        ArrayList<Double> newListOfGDD2 = new ArrayList<Double>(){{
            add(0.0);
            add(1.0);
            add(6.0);
            add(10.0);
            add(15.0);
        }};
        ArrayList<Double> newListOfGDD3 = new ArrayList<Double>(){{
            add(12.0);
            add(14.0);
            add(16.0);
            add(20.0);
            add(28.0);
        }};
        testAveragesArrayList.add(newListOfGDD);
        testAveragesArrayList.add(newListOfGDD2);
        testAveragesArrayList.add(newListOfGDD3);
        ArrayList<Double> shouldBeResult = new ArrayList<Double>(){{
            add(4.0);
            add(5.333);
            add(8.667);
            add(12.0);
            add(18.333);
        }};
        assertEquals(shouldBeResult, calculator.calculateTotalGDDAverage(testAveragesArrayList));
    }

    @Test
    public void testRoundingIsCorrect(){
        assertEquals(3.333, calculator.round(3.3333),0.0001);
    }

    @Test
    public void testRoundingUpIsCorrect(){
        assertEquals(3.334, calculator.round(3.3336),0.0001);
    }

    @Test
    public void testMonthAndDayGivenADayOfTheYear(){
        int[] correctAnswer = new int[]{4,24};
        assertArrayEquals(correctAnswer, calculator.calculateMonthAndDayGivenADay(114));
    }

    @Test
    public void testDayNumberCalculatingCorrect(){
        int monthIndex = 0;
        int dayIndex = 3;
        assertEquals(3, calculator.calculateDayNumber(monthIndex,dayIndex));
    }

    @Test
    public void testNumberOfDaysInNormalYear(){
        int year = 2019;
        assertEquals(365, calculator.determineNumberOfDaysInYear(year));
    }

    @Test
    public void testNumberOfDaysInLeapYear(){
        int year = 2020;
        assertEquals(366, calculator.determineNumberOfDaysInYear(year));
    }

    @Test
    public void testCorrectDeterminingOfNormalYear(){
        int year = 2019;
        assertEquals(false, calculator.isLeapYear(year));
    }

    @Test
    public void testCorrectDeterminingOfActualLeapYear(){
        int year = 2020;
        assertEquals(true, calculator.isLeapYear(year));
    }

    @Test
    public void testCorrectDeterminingOfDivisbleBy4NonLeapYear(){
        int year = 1900;
        assertEquals(false, calculator.isLeapYear(year));
    }

    @Test
    public void testCorrectCalculationOfV2(){
        assertEquals(273.0, calculator.calculateV2Layer(), 0.0001);
    }

    @Test
    public void testCorrectCalculationOfVLayer(){
        //This will calculate V4 for kicks
        assertEquals(441.0, calculator.calculateVLayer(calculator.calculateV2Layer()), 0.0001);
    }

    @Test
    public void testCorrectCalculationOfLayersGivenListOfGDDs(){
        double maturityValue = 72.0;
        double[] values = new double[]{0.0, 274.0, 442.0, 610.0, 777.0, 946.0, 1725.0};
        String[] answers = new String[]{"", "V2", "V4", "V6", "V8", "Silking Layer", "Black Layer"};
        assertArrayEquals(answers, calculator.calculateLayerGivenListOfGDDs(values,maturityValue));
    }
}
