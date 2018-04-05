package gdd.harrison.memdust.growingdegreeday;

import android.content.Context;

public class GDDDataOrganizer {

    private String latitude;
    private String longitude;
    private GDDDataRetriever dataRetriever = new GDDDataRetriever();
    private String[] URLs = new String[4];
    private Context mainHubContext;

    private void buildURLs(){
        URLs[0] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetCurrentData=1&lat=" + latitude +"&long=" +longitude;
        URLs[1] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetMinimumCurrentData=1&lat=" + latitude +"&long=" +longitude;
        URLs[2] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetAllData=1&lat=" + latitude +"&long=" +longitude;
        URLs[3] = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetForecastData=1&lat=" + latitude +"&long=" +longitude;
    }

    protected void setContext(Context context){
        mainHubContext = context;
    }

    String[] beginRetrievingData(){
        buildURLs();
        return attemptToRetrieveDataInBackground();
    }

    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
    }

    private String[] attemptToRetrieveDataInBackground(){
        String[] result = new String[4];
        try{
            result = dataRetriever.execute(URLs[0], URLs[1], URLs[2], URLs[3]).get();
        }
        catch(Exception e){
            System.out.println("There was an exception.");
        }
        return result;
    }









}
