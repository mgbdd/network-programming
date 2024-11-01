package API.Description;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionResponse {

    @JsonProperty("rate")
    private String rate;

    @JsonProperty("wikipedia_extracts")
    private Wikipedia wikiExtracts;

    public String getRate() { return rate; }
    public void setRate(String rate) { this.rate = rate; }

    public Wikipedia getWikiExtracts() { return wikiExtracts; }
    public void setWikiExtracts(Wikipedia wikiExtracts) { this.wikiExtracts = wikiExtracts; }
}
