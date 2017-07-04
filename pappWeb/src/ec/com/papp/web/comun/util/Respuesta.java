package ec.com.papp.web.comun.util;

import net.sf.json.JSONObject;

public class Respuesta {

	private Boolean estado;
	private JSONObject json;
	private Mensajes mensajes;
	
	
	public Mensajes getMensajes() {
		return mensajes;
	}
	public void setMensajes(Mensajes mensajes) {
		this.mensajes = mensajes;
	}
	public Respuesta() {
		super();
		this.estado=true;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
	
}
