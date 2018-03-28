package gdd.harrison.memdust.growingdegreeday;

public class GDDDataOrganizer {

    private String latitude = "40.116";
    private String longitude = "-88.182";
    private String currentYearGDDData;
    private String currentYearMinTemp;
    private String allYearsGDDAccumulations;
    private String currentForecast;
    private GDDDataRetriever dataRetriever = new GDDDataRetriever();


    private String buildCurrentYearGDDDataURL(){
        return "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetCurrentData=1&lat=" + latitude +"&long=" +longitude;
    }

    private String buildCurrentYearMinTempURL(){
        return "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetMinimumCurrentData=1&lat=" + latitude +"&long=" +longitude;
    }

    private String buildGDDAccumulationsURL(){
        return "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetAllData=1&lat=" + latitude +"&long=" +longitude;
    }

    private String buildCurrentForecastURL(){
        return "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetForecastData=1&lat=" + latitude +"&long=" +longitude;
    }

    String beginRetrievingCurrentYearData(){
        currentYearGDDData = attemptToRetrieveDataInBackground(buildCurrentYearGDDDataURL());
        return currentYearGDDData;
    }

    String beginRetrievingCurrentYearMinData(){
        currentYearMinTemp = attemptToRetrieveDataInBackground(buildCurrentYearMinTempURL());
        return currentYearMinTemp;
    }

    String beginRetrievingAllData(){
        allYearsGDDAccumulations = attemptToRetrieveDataInBackground(buildGDDAccumulationsURL());
        return allYearsGDDAccumulations;
    }

    String beginRetrievingCurrentForecast(){
        currentForecast = attemptToRetrieveDataInBackground(buildCurrentForecastURL());
        return currentForecast;
    }

    private String attemptToRetrieveDataInBackground(String URL){
        String result = "";
        String currentGDDData = URL;
        try{
            result = dataRetriever.execute(currentGDDData).get();
        }
        catch(Exception e){
            System.out.println("There was an exception.");
        }
        return result;
    }









}
