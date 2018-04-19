package gdd.harrison.memdust.growingdegreeday;

import java.util.ArrayList;
import java.util.Arrays;

class GDDDataOrganizer {

    private String latitude;
    private String longitude;
    private String[] URLs = new String[4];
    private GDDDataCalculator calculator = new GDDDataCalculator();
    private String[] fetchedData;
    private double maturityValue = 72.0;
    private int currentDay;

    private void buildURLs(){
        URLs[0] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetCurrentData=1&lat=" + latitude +"&long=" +longitude;
        URLs[1] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetMinimumCurrentData=1&lat=" + latitude +"&long=" +longitude;
        URLs[2] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetAllData=1&lat=" + latitude +"&long=" +longitude;
        URLs[3] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetForecastData=1&lat=" + latitude +"&long=" +longitude;
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

    String getAccumulatedAverage(){
        String[] accumulatedDataIntoArray = fetchedData[2].split(" ");
        return removeExcessCharacters(String.valueOf(calculator.calculateTotalGDDAverage(organizeAccumulatedGDDs(accumulatedDataIntoArray))));
    }


    String getGDDProjection(){
        String[] accumulatedModels = fetchedData[3].split(" ");
        String[] accumulatedDataIntoArray = fetchedData[2].split(" ");
        String[] accumulatedCurrentData = fetchedData[0].split(" ");
        String gddProjection = removeExcessCharacters(String.valueOf(calculator.calculateGDDProjection(organizeAccumulatedGDDs(accumulatedDataIntoArray),
                organizeModels(accumulatedModels, calculator.calculateDayNumber(calculator.getCurrentMonth(), calculator.getCurrentDayOfMonth())),
                Double.parseDouble(accumulatedCurrentData[accumulatedCurrentData.length-1]))));
        currentDay = calculator.getModelDay();
        return gddProjection;
    }

    int getCurrentDay(){
        return currentDay;
    }


    private ArrayList<ArrayList<Double>> organizeAccumulatedGDDs(String[] accumulatedData){
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
            System.out.println("There was an exception in trying to get the data asynchronously.");
        }
        return result;
    }









}
