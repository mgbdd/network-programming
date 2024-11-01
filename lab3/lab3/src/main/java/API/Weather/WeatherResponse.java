package API.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("weather")
    private Weather[] weather; // Массив объектов Weather

    @JsonProperty("main")
    private Main main; // Объект Main

    @JsonProperty("wind")
    private Wind wind; // Объект Wind

    public Weather[] getWeather() { return weather; }

    public void setWeather(Weather[] weather) { this.weather = weather; }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
