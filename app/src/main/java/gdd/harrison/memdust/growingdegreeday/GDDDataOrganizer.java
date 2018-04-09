package gdd.harrison.memdust.growingdegreeday;

class GDDDataOrganizer {

    protected String latitude;
    protected String longitude;
    private String[] URLs = new String[4];
    private GDDDataCalculator calculator = new GDDDataCalculator();
    private String[] fetchedData;
    private double maturityValue = 72.0;
    private String[] cornGrowingStages = new String[5];

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

    private double getSilkLayer(){
        return calculator.calculateSilkLayer(maturityValue);
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
