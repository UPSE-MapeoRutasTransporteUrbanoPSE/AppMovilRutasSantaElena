package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Javier on 12/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ruta {

    @JsonProperty("id")
    private String id;
    @JsonProperty("linea")
    private String linea;
    @JsonProperty("listasPuntos")
    private List<Point> listasPuntos;
    @JsonProperty("listasParadas")
    private List<String> listasParadas;
    @JsonProperty("lugaresInteres")
    private String lugaresInteres;
    @JsonProperty("estado")
    private Boolean estado;

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<String> getListasParadas() {
        return listasParadas;
    }

    public void setListasParadas(List<String> listasParadas) {
        this.listasParadas = listasParadas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public List<Point> getListasPuntos() {
        return listasPuntos;
    }

    public void setListasPuntos(List<Point> listasPuntos) {
        this.listasPuntos = listasPuntos;
    }

    public String getLugaresInteres() {
        return lugaresInteres;
    }

    public void setLugaresInteres(String lugaresInteres) {
        this.lugaresInteres = lugaresInteres;
    }


    public Ruta(String id, String linea, List<Point> listasPuntos, List<String> listasParadas, String lugaresInteres, Boolean estado) {
        this.id = id;
        this.linea = linea;
        this.listasPuntos = listasPuntos;
        this.listasParadas = listasParadas;
        this.lugaresInteres = lugaresInteres;
        this.estado = true;
    }

    public Ruta() {

    }

    @Override
    public String toString() {
        return "Ruta{" +
                "id='" + id + '\'' +
                ", linea='" + linea + '\'' +
                ", listasPuntos=" + listasPuntos +
                ", listasParadas=" + listasParadas +
                ", lugaresInteres=" + lugaresInteres +
                ", estado=" + estado +
                '}';
    }
}

