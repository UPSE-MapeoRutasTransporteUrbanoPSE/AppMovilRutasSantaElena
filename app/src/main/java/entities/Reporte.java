package entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Javier on 29/05/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reporte {

    @JsonProperty("id")
    private String id;
    @JsonProperty("idUsuario")
    private String idUsuario;
    @JsonProperty("asunto")
    private String asunto;
    @JsonProperty("numeroDisco")
    private String numeroDisco;
    @JsonProperty("ubicacion")
    private String ubicacion; //(String)
    @JsonProperty("fecha")
    private Date fecha;
    @JsonProperty("idCooperativa")
    private String idCooperativa;
    @JsonProperty("mensaje")
    private String mensaje;
    @JsonProperty("placa")
    private String placa;
    @JsonProperty("estado")
    private boolean estado;



    public Reporte() {

    }

    public Reporte(String id, String idUsuario, String asunto, String numeroDisco, String ubicacion, Date fecha, String idCooperativa, String mensaje, String placa, boolean estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.numeroDisco = numeroDisco;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.idCooperativa = idCooperativa;
        this.mensaje = mensaje;
        this.placa = placa;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getNumeroDisco() {
        return numeroDisco;
    }

    public void setNumeroDisco(String numeroDisco) {
        this.numeroDisco = numeroDisco;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdCooperativa() {
        return idCooperativa;
    }

    public void setIdCooperativa(String idCooperativa) {
        this.idCooperativa = idCooperativa;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Reporte{" +
                "id='" + id + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", asunto='" + asunto + '\'' +
                ", numeroDisco='" + numeroDisco + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha=" + fecha +
                ", idCooperativa='" + idCooperativa + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", placa='" + placa + '\'' +
                ", estado=" + estado +
                '}';
    }
}
