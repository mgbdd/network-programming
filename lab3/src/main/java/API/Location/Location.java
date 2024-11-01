package API.Location;

import API.Places.Feature;
import API.Weather.Weather;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    @JsonProperty("name")
    private String locationName; //название локации
    @JsonProperty("point")
    private Point point;
    @JsonProperty("country")
    private String country;
    @JsonProperty("city")
    private String city;
    private Weather weather;
    private List<Feature> places;

    public String getLocationName() { return locationName; }
    public void setLocationName(String name) { this.locationName = name; }

    public Point getPoint()
    {
        return point;
    }
    public void setPoint(Point point)
    {
        this.point = point;
    }


    public String getCountry()
    {
        return country;
    }
    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    public void setWeather(Weather weather)
    {
        this.weather = weather;
    }
    public void addPlace(Feature place)
    {
        places.add(place);
    }

}



