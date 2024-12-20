package API.Description;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wikipedia {
    @JsonProperty("text")
    private String text;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
