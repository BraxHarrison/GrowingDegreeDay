package gdd.harrison.memdust.growingdegreeday;

import java.util.ArrayList;
import java.util.Calendar;

class GDDDataCalculator {

    double calculateBlackLayer(double maturityValue){
        return (24.16* maturityValue) - 15.388;
    }

    double calculateSilkLayer(double maturityValue){
        return (11.459*maturityValue) +100.27;
    }

    /*ArrayList<Double> calculateTotalMedians(String[] accumulatedData){
        ArrayList<Double> medians = new ArrayList<>();
        ArrayList<ArrayList<String>> listOfAllYears = organizeAccumulatedGDDs(accumulatedData);
        for (int i = 0; i<listOfAllYears.get(0).size();i++){
            Double[] listOfDoubles = new Double[listOfAllYears.size()];
            for (int j = 0; j < listOfAllYears.size());
        }
    }*/

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


    double calculateV2Layer(){
        return 105.0 + 168.0;
    }

    double calculateVLayer(double previousV){
        return previousV + 168.0;
    }

}
