package API.Location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoResponse {
    @JsonProperty("hits")
    private List<Location> hits;

    public List<Location> getHits(){return hits;}
    public void setHits(List<Location> hits){this.hits = hits;}
}
