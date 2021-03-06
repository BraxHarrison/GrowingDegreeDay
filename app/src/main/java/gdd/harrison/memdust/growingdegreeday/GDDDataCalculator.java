package gdd.harrison.memdust.growingdegreeday;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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

    ArrayList<Integer> combineFreezeDataArraysForEntireYear(ArrayList<ArrayList<Double>> accumulatedFreezeData, int freezingTemperature){
        int[] firstFreeze = createFirstFreezeDataArray(accumulatedFreezeData,freezingTemperature);
        int[] lastFreeze = createLastFreezeDataArray(accumulatedFreezeData,freezingTemperature);
        ArrayList<Integer> combinedFreezes = new ArrayList<>();
        for (int i = 0; i < firstFreeze.length; i++){
            combinedFreezes.add(firstFreeze[i]+lastFreeze[i]);
        }
        return combinedFreezes;
    }

    int[] createFirstFreezeDataArray(ArrayList<ArrayList<Double>> accumulatedFreezeData, int freezingTemperature){
        int[] firstFreezeDayCounts = new int[determineNumberOfDaysInYear(getCurrentYear())];
        for(int j = 0; j < accumulatedFreezeData.size();j++) {
            for (int i = 231; i < firstFreezeDayCounts.length; i++) {
                if (accumulatedFreezeData.get(j).get(i) <= freezingTemperature) {
                    firstFreezeDayCounts[i]++;
                    break;
                }
            }
        }
        return firstFreezeDayCounts;
    }

    int[] createLastFreezeDataArray(ArrayList<ArrayList<Double>> accumulatedFreezeData, double freezingTemperature){
        int[] lastFreezeDayCounts = new int[determineNumberOfDaysInYear(getCurrentYear())];
        for (ArrayList<Double> singleYearFreezeData: accumulatedFreezeData){
            boolean foundLastFreezeAlready = false;
            for (int i = 230; i >= 0; i--){
                if ((singleYearFreezeData.get(i) <= freezingTemperature) && (!foundLastFreezeAlready)){
                    foundLastFreezeAlready = true;
                }
            }
        }
        return lastFreezeDayCounts;
    }

    ArrayList<Double> calculateGDDProjection(ArrayList<ArrayList<Double>> accumulatedData, ArrayList<ArrayList<Double>> listOfAllModels, double currentDayData){
        int month = getCurrentMonth();
        int dayOfMonth = getCurrentDayOfMonth();
        int dayNumber = calculateDayNumber(month, dayOfMonth);
        modelDay = dayNumber;
        ArrayList<Double> gddProjection = new ArrayList<>();
        int modelNumber = 0;
        Double[] gddForecast = new Double[listOfAllModels.size()];
        for(ArrayList<Double> s : listOfAllModels){
            gddForecast[modelNumber]= s.get(dayNumber-2);
            modelNumber++;
            }
        gddProjection.add(calculateModelAverage(gddForecast, currentDayData));
        ArrayList<Double> totalAverage = calculateTotalGDDAverage(accumulatedData);
        for (int i = dayNumber - 1; i < totalAverage.size(); i++){
            gddProjection.add(totalAverage.get(i));
        }
        return gddProjection;
        }


    double calculateModelAverage(Double[] forecast, double lastDataDay) {
        double sum = 0;
        for (Double aForecast : forecast) {
            sum = sum + aForecast;
        }
        double amountOfChange = lastDataDay - (sum/forecast.length);
        return lastDataDay + amountOfChange;
    }

    ArrayList<Double> calculateTotalGDDAverage(ArrayList<ArrayList<Double>> listOfAllYears) {
        ArrayList<Double> averages = new ArrayList<>();
        for (int j = 0; j < listOfAllYears.get(0).size(); j++){
            double sumOfAllYearsOnSingleDay = 0;
            for (int i = 0; i < listOfAllYears.size(); i++){
                sumOfAllYearsOnSingleDay = sumOfAllYearsOnSingleDay + listOfAllYears.get(i).get(j);
            }
            averages.add(round(sumOfAllYearsOnSingleDay/listOfAllYears.size()));
        }
        return averages;
    }

    double round(double value){
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    int getCurrentDayOfMonth(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    int[] calculateMonthAndDayGivenADay(int dayOfYear){
        int[] daysInMonth;
        int[] monthAndDay = new int[2];
        if (isLeapYear(getCurrentYear())){
            daysInMonth = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
        }
        else{
            daysInMonth = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
        }
        int currentDayMonthCount = dayOfYear;
        for (int i = 0; i<daysInMonth.length; i++){
            if (currentDayMonthCount <= daysInMonth[i]){
                monthAndDay[0] = i + 1;
                monthAndDay[1] = currentDayMonthCount;
                return monthAndDay;
            }
            else{
                currentDayMonthCount = currentDayMonthCount - daysInMonth[i];
            }
        }
        return monthAndDay;
    }

    int calculateDayNumber(int month, int day){
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



    int determineNumberOfDaysInYear(int currentYear){
        if (isLeapYear(currentYear)) {
            return 366;
        }
        else{
            return 365;
        }
    }

    boolean isLeapYear(int currentYear) {
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

    protected String[] calculateLayerGivenListOfGDDs(double[] GDDs, double maturityValue) {
        String[] listOfLayersWithRespectToGDD = new String[GDDs.length];
        for (int i = 0; i < GDDs.length; i++) {
            if ((GDDs[i] >= calculateV2Layer()) && (GDDs[i] < calculateVLayer(calculateV2Layer()))) {
                listOfLayersWithRespectToGDD[i] = "V2";
            } else if ((GDDs[i] >= calculateVLayer(calculateV2Layer())) && (GDDs[i] < calculateVLayer(calculateV2Layer() + 168.0))) {
                listOfLayersWithRespectToGDD[i] = "V4";
            } else if ((GDDs[i] >= calculateVLayer(calculateV2Layer() + 168.0)) && (GDDs[i] < calculateVLayer(calculateV2Layer() + (2.0 * 168.0)))) {
                listOfLayersWithRespectToGDD[i] = "V6";
            } else if ((GDDs[i] >= calculateVLayer(calculateV2Layer() + (2.0 * 168.0))) && (GDDs[i] < calculateVLayer(calculateV2Layer() + (3.0 * 168.0)))) {
                listOfLayersWithRespectToGDD[i] = "V8";
            } else if ((GDDs[i] >= calculateVLayer(calculateV2Layer() + (3.0 * 168.0))) && (GDDs[i] < calculateSilkLayer(maturityValue))) {
                listOfLayersWithRespectToGDD[i] = "V10";
            } else if ((GDDs[i] >= calculateSilkLayer(maturityValue)) && (GDDs[i] < calculateBlackLayer(maturityValue))) {
                listOfLayersWithRespectToGDD[i] = "Silking Layer";
            } else if ((GDDs[i] >= calculateBlackLayer(maturityValue))) {
                listOfLayersWithRespectToGDD[i] = "Black Layer";
            }
            else{
                listOfLayersWithRespectToGDD[i] = "";
            }
        }
        return listOfLayersWithRespectToGDD;
    }

}
