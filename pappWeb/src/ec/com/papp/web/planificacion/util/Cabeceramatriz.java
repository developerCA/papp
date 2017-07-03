package ec.com.papp.web.planificacion.util;

import javax.persistence.Transient;

public class Cabeceramatriz {

	
	private String actividadcodigo;
	
	private String proyectocodigo;
	
	private String programacodigo;
	
	private String subactividadcodigo;
	
	private String actividadnombre;
	
	private String proyectonombre;
	
	private String programanombre;
	
	private String subactividadnombre;
		
	private Long idsubactividad;

	public String getActividadcodigo() {
		return actividadcodigo;
	}

	public void setActividadcodigo(String actividadcodigo) {
		this.actividadcodigo = actividadcodigo;
	}

	public String getProyectocodigo() {
		return proyectocodigo;
	}

	public void setProyectocodigo(String proyectocodigo) {
		this.proyectocodigo = proyectocodigo;
	}

	public String getProgramacodigo() {
		return programacodigo;
	}

	public void setProgramacodigo(String programacodigo) {
		this.programacodigo = programacodigo;
	}

	public String getSubactividadcodigo() {
		return subactividadcodigo;
	}

	public void setSubactividadcodigo(String subactividadcodigo) {
		this.subactividadcodigo = subactividadcodigo;
	}

	public String getActividadnombre() {
		return actividadnombre;
	}

	public void setActividadnombre(String actividadnombre) {
		this.actividadnombre = actividadnombre;
	}

	public String getProyectonombre() {
		return proyectonombre;
	}

	public void setProyectonombre(String proyectonombre) {
		this.proyectonombre = proyectonombre;
	}

	public String getProgramanombre() {
		return programanombre;
	}

	public void setProgramanombre(String programanombre) {
		this.programanombre = programanombre;
	}

	public String getSubactividadnombre() {
		return subactividadnombre;
	}

	public void setSubactividadnombre(String subactividadnombre) {
		this.subactividadnombre = subactividadnombre;
	}

	public Long getIdsubactividad() {
		return idsubactividad;
	}

	public void setIdsubactividad(Long idsubactividad) {
		this.idsubactividad = idsubactividad;
	}
}
