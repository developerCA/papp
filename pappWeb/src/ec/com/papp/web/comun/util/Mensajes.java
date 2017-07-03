package ec.com.papp.web.comun.util;

public class Mensajes {

	private String msg;
	private String type;
	private Boolean show;
	private String seccion;
	private Boolean link;
	
	public Mensajes() {
		// TODO Auto-generated constructor stub
		this.show=false;
		this.link=false;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getShow() {
		return show;
	}
	public void setShow(Boolean show) {
		this.show = show;
	}
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public Boolean getLink() {
		return link;
	}
	public void setLink(Boolean link) {
		this.link = link;
	}
}
