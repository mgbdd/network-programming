package API.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {
    @JsonProperty("speed")
    private double speed;

    public double getSpeed() { return speed; }

    public void setSpeed(double speed) { this.speed = speed; }
}
