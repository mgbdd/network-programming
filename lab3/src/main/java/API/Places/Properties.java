package API.Places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties {
    @JsonProperty("xid")
    private String xid;

    @JsonProperty("name")
    private String name;

    public String getXid() { return xid; }
    public void setXid(String xid) { this.xid = xid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
