package gdd.harrison.memdust.growingdegreeday;

public class GDDDataOrganizer {

    private String latitude = "40.116";
    private String longitude = "-88.182";
    private String[] data;
    private GDDDataRetriever dataRetriever = new GDDDataRetriever();
    private String[] URLs = new String[4];

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
