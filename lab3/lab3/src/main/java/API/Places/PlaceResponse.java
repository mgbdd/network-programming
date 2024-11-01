package API.Places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceResponse {
    @JsonProperty("features")
    private Feature[] features;

    public Feature[] getFeatures() { return features; }
    public void setFeatures(Feature[] features) { this.features = features; }
}
