package gdd.harrison.memdust.growingdegreeday;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

class GDDDataCalculator {

    int modelDay;

    public int getModelDay(){
        return modelDay;
    }

    double calculateBlackLayer(double maturityValue){
        return (24.16* maturityValue) - 15.388;
    }

    double calculateSilkLayer(double maturityValue){
        return (11.459*maturityValue) +100.27;
    }

    ArrayList<Double> calculateGDDProjection(String[] accumulatedData, String[] modelData, String[] currentData){
        int month = getCurrentMonth();
        int dayOfMonth = getCurrentDayOfMonth();
        int dayNumber = calculateDayNumber(month, dayOfMonth);
        modelDay = dayNumber;
        ArrayList<Double> gddProjection = new ArrayList<>();
        ArrayList<ArrayList<String>> listOfAllModels = organizeModels(modelData, dayNumber);
        int modelNumber = 0;
        Double[] gddForecast = new Double[listOfAllModels.size()];
        for(ArrayList<String> s : listOfAllModels){
            gddForecast[modelNumber]= Double.parseDouble(s.get(dayNumber-2));
            modelNumber++;
            }
        gddProjection.add(calculateModelAverage(gddForecast, currentData));
        ArrayList<Double> totalAverage = calculateTotalGDDAverage(accumulatedData);
        System.out.println(totalAverage);
        for (int i = dayNumber - 1; i < totalAverage.size(); i++){
            gddProjection.add(totalAverage.get(i));
        }
        System.out.println(gddProjection);
        return gddProjection;
        }


    private double calculateModelAverage(Double[] forecast, String[] currentData) {
        System.out.println(Arrays.toString(forecast));
        double sum = 0;
        for (int i = 0; i < forecast.length; i++){
            sum = sum + forecast[i];
        }
        System.out.println((sum/forecast.length));
        double amountOfChange = Double.parseDouble(currentData[currentData.length-1]) - (sum/forecast.length);
        return Double.parseDouble(currentData[currentData.length-1]) + amountOfChange;
    }

    ArrayList<Double> calculateTotalMedians(String[] accumulatedData){
        ArrayList<Double> medians = new ArrayList<>();
        ArrayList<ArrayList<String>> listOfAllYears = organizeAccumulatedGDDs(accumulatedData);
        for (int i = 0; i<listOfAllYears.get(0).size();i++){
            Double[] listOfDoubles = new Double[listOfAllYears.size()];
            for (int j = 0; j < listOfAllYears.size(); j++){
                listOfDoubles[j] = Double.parseDouble(listOfAllYears.get(j).get(i));
            }
            Arrays.sort(listOfDoubles);
            medians.add(calculateMedian(listOfDoubles));
        }
        return medians;
    }

    Double calculateMedian (Double[] list){
        int middleNumber = list.length/2;
        if (list.length%2 == 0){
            return (list[middleNumber] + list[middleNumber -1])/2.0;
        }
        else{
            return list[middleNumber];
        }
    }

    ArrayList<Double> calculateTotalGDDAverage(String[] accumulatedData) {
        ArrayList<Double> averages = new ArrayList<>();
        ArrayList<ArrayList<String>> listOfAllYears = organizeAccumulatedGDDs(accumulatedData);
        for (int j = 0; j < listOfAllYears.get(0).size() - 1; j++){
            double sumOfAllYearsOnSingleDay = 0;
            for (int i = 0; i < listOfAllYears.size(); i++){
                sumOfAllYearsOnSingleDay = sumOfAllYearsOnSingleDay + Double.parseDouble(listOfAllYears.get(i).get(j));
            }
            averages.add(sumOfAllYearsOnSingleDay/listOfAllYears.size());
        }
        return averages;
    }

    ArrayList<ArrayList<String>> organizeModels(String[] modelData, int dayNumber){
        ArrayList<ArrayList<String>> listOfModels = new ArrayList<>();
        int modelBeginIndex = 0;
        for (int i = 0; i < modelData.length/dayNumber; i++){
            ArrayList<String> singleModel = new ArrayList<>();
            for (int j = modelBeginIndex; j <= modelBeginIndex + dayNumber-1; j++){
                singleModel.add(modelData[j]);
            }
            System.out.println(singleModel);
            modelBeginIndex = modelBeginIndex + dayNumber;
            listOfModels.add(singleModel);
        }
        return listOfModels;
    }

    private ArrayList<ArrayList<String>> organizeAccumulatedGDDs(String[] accumulatedData){
        ArrayList<ArrayList<String>> listOfYears = new ArrayList<>();
        int yearIndex = 0;
        int currentYear = getCurrentYear();
        if (isLeapYear(currentYear)){
            for (int year = 1981; year < currentYear; year++){
                int daysInYear = determineNumberOfDaysInYear(year);
                listOfYears.add(getCorrectIndicesForLeapYear(accumulatedData,yearIndex,yearIndex+daysInYear));
                yearIndex = yearIndex + daysInYear;
            }
        }
        else{
            for (int year = 1981; year < currentYear; year++){
                int daysInYear = determineNumberOfDaysInYear(year);
                listOfYears.add(getCorrectIndicesForNonLeapYear(accumulatedData,yearIndex, yearIndex+daysInYear));
                yearIndex = yearIndex + daysInYear;
            }
        }
        return listOfYears;
    }

    private ArrayList<String> getCorrectIndicesForNonLeapYear(String[] accumulatedData, int yearBeginIndex, int yearEndIndex){
        ArrayList<String> dataForSingleYear = new ArrayList<>();
        int numberOfDaysInYear = yearEndIndex - yearBeginIndex;
        for (int i = 0; i < numberOfDaysInYear; i++){
            if ((numberOfDaysInYear == 366) && (i >= 59)){
                dataForSingleYear.add(accumulatedData[i-1]);
            }
            else{
                dataForSingleYear.add(accumulatedData[i]);
            }

        }
        return dataForSingleYear;
    }

    private ArrayList<String> getCorrectIndicesForLeapYear(String[] accumulatedData, int yearBeginIndex, int yearEndIndex){
        ArrayList<String> dataForSingleYear = new ArrayList<>();
        int numberOfDaysInYear = yearEndIndex - yearBeginIndex;
        for (int i = 0; i < numberOfDaysInYear + 1; i++){
            if (i == 59){
                dataForSingleYear.add(accumulatedData[i-1]);
            }
            else if(i > 59){
                dataForSingleYear.add(accumulatedData[i-1]);
            }
            else{
                dataForSingleYear.add(accumulatedData[i]);
            }
        }
        return dataForSingleYear;
    }

    private int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    private int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    private int getCurrentDayOfMonth(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    private int calculateDayNumber(int month, int day){
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

    private int determineNumberOfDaysInYear(int currentYear){
        if (isLeapYear(currentYear)) {
            return 366;
        }
        else{
            return 365;
        }

    }

    private boolean isLeapYear(int currentYear) {
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
