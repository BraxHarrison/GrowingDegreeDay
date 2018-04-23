package gdd.harrison.memdust.growingdegreeday;

import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class GDDDataOrganizer {

    private String latitude;
    private String longitude;
    private String[] URLs = new String[4];
    private GDDDataCalculator calculator = new GDDDataCalculator();
    private String[] fetchedData;
    private double maturityValue = 72.0;
    private int currentDay;
    private String gddStartMonth;
    private int gddStartDay;
    private int firstDayGDDNumber;
    private int trimIndex;

    private void buildURLs(){
        URLs[0] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetCurrentData=1&lat=" + latitude +"&long=" +longitude;
        URLs[1] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetMinimumPreviousData=1&lat=" + latitude +"&long=" +longitude;
        URLs[2] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetAllData=1&lat=" + latitude +"&long=" +longitude;
        URLs[3] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetForecastData=1&lat=" + latitude +"&long=" +longitude;
    }

    void setMaturityValue(double maturityValue){
        this.maturityValue = maturityValue;
    }

    void setFirstDayOfGDD(int dayNumber){
        this.firstDayGDDNumber = dayNumber;
    }

    void updateIndexOfFirstDay(){
        setFirstDayOfGDD(calculator.calculateDayNumber(calculateMonthNumber(gddStartMonth), gddStartDay));
    }

    String[] getFirstDayOfGDDValue(ArrayList<Double> data){
        double firstGDD = data.get(firstDayGDDNumber - 1);
        double[] newData = new double[data.size() - firstDayGDDNumber - 1];
        for (int i = 0; i < newData.length; i++){
            newData[i] = data.get(i+firstDayGDDNumber-1) - firstGDD;
        }
        String[] newDataAsStringArray = new String[newData.length];
        for (int i = 0; i < newData.length;  i++){
            newDataAsStringArray[i] = String.valueOf(newData[i]);
        }
        return newDataAsStringArray;
    }

    String[] trimCurrentArray(String[] data){
        double firstGDD = Double.parseDouble(data[firstDayGDDNumber - 1]);
        trimIndex = data.length - firstDayGDDNumber - 1;
        double[] newData = new double[data.length - firstDayGDDNumber - 1];
        for (int i = 0; i < newData.length; i++){
            newData[i] = Double.parseDouble(data[i+firstDayGDDNumber-1]) - firstGDD;
        }
        String[] newDataAsStringArray = new String[newData.length];
        for (int i = 0; i < newData.length;  i++){
            newDataAsStringArray[i] = String.valueOf(newData[i]);
        }
        return newDataAsStringArray;
    }


    int calculateMonthNumber(String month){
        switch (month) {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
        }
        return 0;
    }
    void setGDDStartDay(int gddStartDay){
        this.gddStartDay = gddStartDay;
    }

    void setGddStartMonth(String GDDStartMonth){
        gddStartMonth = GDDStartMonth;
    }

    String[] beginRetrievingData(){
        buildURLs();
        return attemptToRetrieveDataInBackground();
    }

    void setLatitude(String latitude){
        this.latitude = latitude;
    }

    void setLongitude(String longitude){
        this.longitude = longitude;
    }

    String getBlackLayer(){
        return String.valueOf(calculator.calculateBlackLayer(maturityValue));
    }

    String getSilkLayer(){
        return String.valueOf(calculator.calculateSilkLayer(maturityValue));
    }

    String getCornStages(){
        return Arrays.toString(calculator.calculateCornLayers());
    }

    String getCurrentTrimmedData(){
        return removeExcessCharacters(Arrays.toString(trimCurrentArray(fetchedData[0].split(" "))));
    }

    String getCurrentLayerOfData(){
        String[] projection = getGDDProjection().split(",");
        String[] currentData = getCurrentTrimmedData().split(",");
        double[] doubleProjectionAndCurrentData = new double[projection.length + currentData.length];
        System.out.println(doubleProjectionAndCurrentData.length);
        for (int i = 0; i < currentData.length; i++){
            doubleProjectionAndCurrentData[i] = Double.parseDouble(currentData[i]);
        }
        int projectionIndex = 0;
        for (int j = currentData.length; j < doubleProjectionAndCurrentData.length; j++){
            doubleProjectionAndCurrentData[j] = Double.parseDouble(projection[projectionIndex]);
            projectionIndex++;
        }
        return removeExcessCharacters(Arrays.toString(calculator.calculateLayerGivenListOfGDDs(doubleProjectionAndCurrentData, maturityValue)));
    }

    String getAccumulatedAverage(){
        String[] accumulatedDataIntoArray = fetchedData[2].split(" ");
        return removeExcessCharacters(Arrays.toString(getFirstDayOfGDDValue(calculator.calculateTotalGDDAverage(organizeAccumulatedData(accumulatedDataIntoArray)))));
    }

    String getGDDProjection(){
        String[] accumulatedModels = fetchedData[3].split(" ");
        String[] accumulatedDataIntoArray = fetchedData[2].split(" ");
        String[] accumulatedCurrentData = trimCurrentArray(fetchedData[0].split(" "));
        String gddProjection = removeExcessCharacters(String.valueOf(calculator.calculateGDDProjection(organizeAccumulatedData(accumulatedDataIntoArray),
                organizeModels(accumulatedModels, calculator.calculateDayNumber(calculator.getCurrentMonth(), calculator.getCurrentDayOfMonth())),
                Double.parseDouble(accumulatedCurrentData[accumulatedCurrentData.length-1]))));
        currentDay = calculator.getModelDay();
        return gddProjection;
    }

    String getFreezeData(){
        String[] accumulatedMinTemps = fetchedData[1].split(" ");
        return removeExcessCharacters(String.valueOf(calculator.combineFreezeDataArraysForEntireYear(organizeAccumulatedData(accumulatedMinTemps),32)));
    }

    int getCurrentDay(){
        return trimIndex;
    }


    private ArrayList<ArrayList<Double>> organizeAccumulatedData(String[] accumulatedData){
        ArrayList<ArrayList<Double>> listOfYears = new ArrayList<>();
        int yearIndex = 0;
        int currentYear = calculator.getCurrentYear();
        if (calculator.isLeapYear(currentYear)){
            for (int year = 1981; year < currentYear; year++){
                int daysInYear = calculator.determineNumberOfDaysInYear(year);
                listOfYears.add(getCorrectIndicesForLeapYear(accumulatedData,yearIndex,yearIndex+daysInYear));
                yearIndex = yearIndex + daysInYear;
            }
        }
        else{
            for (int year = 1981; year < currentYear; year++){
                int daysInYear = calculator.determineNumberOfDaysInYear(year);
                listOfYears.add(getCorrectIndicesForNonLeapYear(accumulatedData,yearIndex, yearIndex+daysInYear));
                yearIndex = yearIndex + daysInYear;
            }
        }
        return listOfYears;
    }

    private ArrayList<ArrayList<Double>> organizeModels(String[] modelData, int dayNumber){
        ArrayList<ArrayList<Double>> listOfModels = new ArrayList<>();
        int modelBeginIndex = 0;
        for (int i = 0; i < modelData.length/dayNumber; i++){
            ArrayList<Double> singleModel = new ArrayList<>();
            for (int j = modelBeginIndex; j < modelData.length; j++){
                singleModel.add(Double.parseDouble(modelData[modelBeginIndex]));
            }
            modelBeginIndex = modelBeginIndex + dayNumber;
            listOfModels.add(singleModel);
        }
        return listOfModels;
    }

    private ArrayList<Double> getCorrectIndicesForNonLeapYear(String[] accumulatedData, int yearBeginIndex, int yearEndIndex){
        ArrayList<Double> dataForSingleYear = new ArrayList<>();
        int numberOfDaysInYear = yearEndIndex - yearBeginIndex;
        for (int i = 0; i < numberOfDaysInYear; i++){
            if ((numberOfDaysInYear == 366) && (i >= 59)){
                dataForSingleYear.add(Double.parseDouble(accumulatedData[i-1]));
            }
            else{
                dataForSingleYear.add(Double.parseDouble(accumulatedData[i]));
            }
        }
        return dataForSingleYear;
    }

    private ArrayList<Double> getCorrectIndicesForLeapYear(String[] accumulatedData, int yearBeginIndex, int yearEndIndex){
        ArrayList<Double> dataForSingleYear = new ArrayList<>();
        int numberOfDaysInYear = yearEndIndex - yearBeginIndex;
        for (int i = 0; i < numberOfDaysInYear + 1; i++){
            if (i == 59){
                dataForSingleYear.add(Double.parseDouble(accumulatedData[i-1]));
            }
            else if(i > 59){
                dataForSingleYear.add(Double.parseDouble(accumulatedData[i-1]));
            }
            else{
                dataForSingleYear.add(Double.parseDouble(accumulatedData[i]));
            }
        }
        return dataForSingleYear;
    }

    private String removeExcessCharacters(String unformattedString){
        String halfFormattedString = unformattedString.replace("[", "");
        return halfFormattedString.replace("]", "");
    }


    private String[] attemptToRetrieveDataInBackground(){
        GDDDataRetriever dataRetriever = new GDDDataRetriever();
        String[] result = new String[4];
        try{
            result = dataRetriever.execute(URLs[0], URLs[1], URLs[2], URLs[3]).get();
            fetchedData = result;
        }
        catch(Exception e){
            Log.d("CREATION","There was an exception in trying to get the data asynchronously.");
        }
        if(result[0].length() <= 2){
            Log.d("CREATION","There is no data for the selected location");
        }
        return result;
    }

    public HashMap<String,Integer> getAllCornStages(){
        String[] vStages = getCornStages().split(" ");
        HashMap<String,Integer> allCornStages = new HashMap<>();
        for(int i = 0; i<vStages.length;i++){
            vStages[i] = vStages[i].replace("[","");
            vStages[i] = vStages[i].replace(",","");
            vStages[i] = vStages[i].replace("]","");
        }
        allCornStages.put("v2",Math.round(Float.parseFloat(vStages[0])));
        allCornStages.put("v4",Math.round(Float.parseFloat(vStages[1])));
        allCornStages.put("v6",Math.round(Float.parseFloat(vStages[2])));
        allCornStages.put("v8",Math.round(Float.parseFloat(vStages[3])));
        allCornStages.put("v10",Math.round(Float.parseFloat(vStages[4])));
        allCornStages.put("silking",Math.round(Float.parseFloat(getSilkLayer())));
        allCornStages.put("black",Math.round(Float.parseFloat(getBlackLayer())));
        return allCornStages;
    }









}
