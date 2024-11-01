package API.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    @JsonProperty("description")
    private String description;

    public String getDescription() { return description; }
    public void setDescription(String desc) { this.description = desc; }
}
