package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SegUsuario {
    @JsonProperty("id")
    private String id;

    @JsonProperty("perfil")
    private String perfil;

    @JsonProperty("usuario")
    private String usuario;

    @JsonProperty("clave")
    private String clave;

    @JsonProperty("movil")
    private String movil;

    @JsonProperty("email")
    private String email;

    @JsonProperty("estado")
    private Boolean estado;

    public SegUsuario() {

    }

    public SegUsuario(String id, String perfil, String usuario, String clave, String movil, String email, Boolean estado) {
        this.id = id;
        this.perfil = perfil;
        this.usuario = usuario;
        this.clave = clave;
        this.movil = movil;
        this.email = email;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "SegUsuario{" +
                "id='" + id + '\'' +
                ", perfil='" + perfil + '\'' +
                ", usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                ", movil='" + movil + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                '}';
    }
}
