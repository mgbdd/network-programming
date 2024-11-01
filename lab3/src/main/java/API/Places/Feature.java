package API.Places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature {
    @JsonProperty("properties")
    private Properties properties;

    public Properties getProperties() { return properties; }
    public void setProperties(Properties properties) { this.properties = properties; }

}
