package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Parada {

	@JsonProperty("id")
	private String id;
	@JsonProperty("nombre")
	private String nombre;
	@JsonProperty("urlFoto")
	private String urlFoto;
	@JsonProperty("coordenada")
    private Point coordenada;
	@JsonProperty("estado")
	private Boolean estado;


	public Parada(String nombre, String urlFoto, Point coordenada) {
		super();
		this.nombre = nombre;
		this.urlFoto = urlFoto;
		this.coordenada = coordenada;
		this.estado=true;
	}
	public Parada() {
		this.estado=true;
		//super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrlFoto() {
		return urlFoto;
	}
	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
	public Point getCoordenada() {
		return coordenada;
	}
	public void setCoordenada(Point coordenada) {
		this.coordenada = coordenada;
	}


	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Parada{" +
				"id='" + id + '\'' +
				", nombre='" + nombre + '\'' +
				", urlFoto='" + urlFoto + '\'' +
				", coordenada=" + coordenada +
				", estado=" + estado +
				'}';
	}
}

