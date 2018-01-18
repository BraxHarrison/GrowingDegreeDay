package gdd.harrison.memdust.growingdegreeday;

import com.google.android.gms.maps.model.LatLng;

public class LocationInfo {

    private String locName;
    private LatLng latLong;
    private float avgGDD;

    public LocationInfo(String locName){
        this.locName = locName;
    }

    public void setAvgGDD(float avgGDD){
        this.avgGDD = avgGDD;
    }
    public float getAvgGDD(){
        return avgGDD;
    }
    public void setLatLong(LatLng latLong){
        this.latLong = latLong;
    }
    public LatLng getLatLong(){
        return latLong;
    }

}
