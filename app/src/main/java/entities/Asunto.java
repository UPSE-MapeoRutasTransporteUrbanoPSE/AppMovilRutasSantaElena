package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Asunto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("asuntos")
    private List<String> asuntos = new ArrayList<String>();

    public Asunto() {
        super();
    }
    public Asunto(String id, List<String> asuntos) {
        super();
        this.id = id;
        this.asuntos = asuntos;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<String> getAsuntos() {
        return asuntos;
    }
    public void setAsuntos(List<String> asuntos) {
        this.asuntos = asuntos;
    }

    @Override
    public String toString() {
        return "Asunto{" +
                "id='" + id + '\'' +
                ", asuntos=" + asuntos +
                '}';
    }
}
