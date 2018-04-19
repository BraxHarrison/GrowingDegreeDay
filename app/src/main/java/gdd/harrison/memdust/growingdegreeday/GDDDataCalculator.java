package gdd.harrison.memdust.growingdegreeday;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

class GDDDataCalculator {

    private int modelDay;

    int getModelDay(){
        return modelDay;
    }

    double calculateBlackLayer(double maturityValue){
        return (24.16* maturityValue) - 15.388;
    }

    double calculateSilkLayer(double maturityValue){
        return (11.459*maturityValue) +100.27;
    }

    ArrayList<Double> calculateGDDProjection(ArrayList<ArrayList<String>> accumulatedData, ArrayList<ArrayList<String>> listOfAllModels, double currentDayData){
        int month = getCurrentMonth();
        int dayOfMonth = getCurrentDayOfMonth();
        int dayNumber = calculateDayNumber(month, dayOfMonth);
        modelDay = dayNumber;
        ArrayList<Double> gddProjection = new ArrayList<>();
        int modelNumber = 0;
        Double[] gddForecast = new Double[listOfAllModels.size()];
        for(ArrayList<String> s : listOfAllModels){
            gddForecast[modelNumber]= Double.parseDouble(s.get(dayNumber-2));
            modelNumber++;
            }
        gddProjection.add(calculateModelAverage(gddForecast, currentDayData));
        ArrayList<Double> totalAverage = calculateTotalGDDAverage(accumulatedData);
        for (int i = dayNumber - 1; i < totalAverage.size(); i++){
            gddProjection.add(totalAverage.get(i));
        }
        System.out.println(gddProjection);
        return gddProjection;
        }


    private double calculateModelAverage(Double[] forecast, double lastDataDay) {
        double sum = 0;
        for (Double aForecast : forecast) {
            sum = sum + aForecast;
        }
        double amountOfChange = lastDataDay - (sum/forecast.length);
        return lastDataDay + amountOfChange;
    }

    ArrayList<Double> calculateTotalGDDAverage(ArrayList<ArrayList<String>> listOfAllYears) {
        ArrayList<Double> averages = new ArrayList<>();
        for (int j = 0; j < listOfAllYears.get(0).size() - 1; j++){
            double sumOfAllYearsOnSingleDay = 0;
            for (int i = 0; i < listOfAllYears.size(); i++){
                sumOfAllYearsOnSingleDay = sumOfAllYearsOnSingleDay + Double.parseDouble(listOfAllYears.get(i).get(j));
            }
            averages.add(sumOfAllYearsOnSingleDay/listOfAllYears.size());
        }
        return averages;
    }

    protected int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    protected int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    protected int getCurrentDayOfMonth(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    protected int calculateDayNumber(int month, int day){
        int DayNumber = 0;
        for (int i = 1; i <= month; i++){
            if ((i == 1) || (i ==3) || (i ==5) || (i == 7) || (i ==8) || (i ==10) || (i == 12)){
                DayNumber = DayNumber + 31;
            }
            else if ((i==4)||(i==6)||(i==9)||(i==11)){
                DayNumber = DayNumber + 30;
            }
            else if(isLeapYear(getCurrentYear())){
                DayNumber = DayNumber + 29;
            }
            else{
                DayNumber = DayNumber + 28;
            }
        }
        DayNumber = DayNumber + day;
        return DayNumber;
    }

    protected int determineNumberOfDaysInYear(int currentYear){
        if (isLeapYear(currentYear)) {
            return 366;
        }
        else{
            return 365;
        }

    }

    protected boolean isLeapYear(int currentYear) {
        return currentYear % 4 == 0 && (currentYear % 100 != 0 || currentYear % 400 == 0);
    }

    double[] calculateCornLayers(){
        double[] cornLayers = new double[5];
        cornLayers[0] = calculateV2Layer();
        double previousLayer = cornLayers[0];
        for (int i = 1; i < cornLayers.length; i++){
            cornLayers[i] = calculateVLayer(previousLayer);
            previousLayer = cornLayers[i];
        }
        return cornLayers;
    }

    double calculateV2Layer(){
        return 105.0 + 168.0;
    }

    double calculateVLayer(double previousV){
        return previousV + 168.0;
    }

}
