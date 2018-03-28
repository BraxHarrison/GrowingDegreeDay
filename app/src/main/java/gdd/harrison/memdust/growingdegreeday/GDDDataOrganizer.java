package gdd.harrison.memdust.growingdegreeday;

import com.google.android.gms.maps.model.LatLng;

public class GDDDataOrganizer {

    private String latitude = "40.116";
    private String longitude = "-88.182";
    private String currentYearGDDData;
    private String currentYearMinTemp;
    private String allYearsGDDAccumulations;
    private String currentForecast;
    private GDDDataRetriever dataRetriever = new GDDDataRetriever();


    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
    }

    public void setCurrentYearGDDData(String currentYearGDDData){
        this.currentYearGDDData = currentYearGDDData;
    }

    public void setCurrentYearMinTemp(String currentYearMinTemp){
        this.currentYearMinTemp = currentYearMinTemp;
    }

    public void setAllYearsGDDAccumulations(String allYearsGDDAccumulations){
        this.allYearsGDDAccumulations = allYearsGDDAccumulations;
    }

    public void setCurrentForecast(String currentForecast){
        this.currentForecast = currentForecast;
    }

    private String buildCurrentYearGDDDataURL(){
        return "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetCurrentData=1&lat=" + latitude +"&long=" +longitude;
    }

    String beginRetrievingData(){
        return attemptToRetrieveDataInBackground(buildCurrentYearGDDDataURL());
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
