package API.Location;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Point
{
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lng")
    private double longitude;

    public double getLongitude()
    {
        return longitude;
    }
    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
}

