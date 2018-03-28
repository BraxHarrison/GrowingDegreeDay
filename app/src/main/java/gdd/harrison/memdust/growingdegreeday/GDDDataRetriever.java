package gdd.harrison.memdust.growingdegreeday;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GDDDataRetriever extends AsyncTask<String, Void, String> {
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = params[0];

        return attemptToRetrieveData(stringUrl);
    }

    private String attemptToRetrieveData(String stringURL) {
        try {
            URL gddURL = new URL(stringURL);
            HttpURLConnection connection = (HttpURLConnection) gddURL.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            return readDataFromWebsite(reader, stringBuilder);
        } catch (Exception e) {
            System.out.println("There was an issue grabbing the data from the website.");
        }
        return null;
    }

    private String readDataFromWebsite(BufferedReader reader, StringBuilder stringBuilder)throws Exception{
        String inputLine;
        String result = "";
        while ((inputLine = reader.readLine()) != null){
            result = stringBuilder.append(inputLine).toString();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}
