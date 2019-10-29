app.factory("unidadFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/estructuraorganica");

	return {
		traerUnidades : function(pagina) {
			  return Restangular.allUrl("estructuraorganica/consultar/unidad/pagina="+pagina).getList();
		},

		traerFiltro: function(
			pagina,
			idEjerciciofiscal,
			nombre,
			codigo,
			estado
		) {
			var URL = "estructuraorganica/consultar/unidad/" +
				"pagina=" + pagina +
				"&filas=10" +
				"&ejerciciofiscal=" + idEjerciciofiscal;

			if(nombre!=null && nombre != "") URL += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") URL += "&codigopresup=" + codigo;	
			if(estado!=null && estado != "" ) URL += "&estado=" + estado;

			return Restangular.allUrl(URL).getList();
		},

		traerPlanificacionFiltro: function(
			pagina,
			idEjerciciofiscal,
			codigo,
			nombre,
			estado
		) {
			var URL = "planificacion/consultar/planificacion/" +
				"pagina=" + pagina +
				"&filas=10" +
				"&ejerciciofiscal=" + idEjerciciofiscal +
				"&estadoaprobado=1";

			if(nombre!=null && nombre != "") URL += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") URL += "&codigopresup=" + codigo;	
			if(estado!=null && estado != "" ) URL += "&estado=" + estado;

			return Restangular.allUrl(URL).getList();
		},

		traerPlanificacionFiltroAprobado: function(
			pagina,
			idEjerciciofiscal,
			codigo,
			nombre,
			estado,
			estadoaprobado
		) {
			var URL = "planificacion/consultar/planificacion/" +
				"pagina=" + pagina +
				"&filas=10" +
				"&ejerciciofiscal=" + idEjerciciofiscal;

			if(nombre!=null && nombre != "") URL += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") URL += "&codigopresup=" + codigo;	
			if(estado!=null && estado != "" ) URL += "&estado=" + estado;
			if(estadoaprobado!=null && estadoaprobado != "" ) URL += "&estadoaprobado=" + estadoaprobado;

			return Restangular.allUrl(URL).getList();
		},

//** Unidades Arbol
		traerUnidadesArbol : function(pagina,estructuraorganica,estado) {
			return Restangular.allUrl(
			  	"estructuraorganica/consultar/unidadarbol/pagina="+pagina+
			  		"&unidadarbolerganicaid="+estructuraorganica+
			  		"&estado="+estado
	  		).getList();
		},
		
		traerUnidadesArbolhijos : function(pagina,estructuraorganica,padre,estado) {
			  return Restangular.allUrl("estructuraorganica/consultar/unidadarbol/pagina="+pagina+"&unidadarbolerganicaid="+estructuraorganica+"&id="+padre+"&estado="+estado).getList();
		},

		traerUnidadArbol : function(id) {
			var url = "estructuraorganica/unidadarbol/"+id+"/0/0";
		    //console.log(url);
		    return Restangular.allUrl(url).customGET();
		},

		traerUnidadArbolDetail : function(id) {
			var url = "estructuraorganica/unidadarboldetail/"+id+"/0/0";
		    //console.log(url);
		    return Restangular.allUrl(url).customGET();
		},

		traerUnidadArbolFiltro: function(
			pagina,
			id,
			institucion,
			institucionentidad,
			estado
		) {
			var URL = "estructuraorganica/consultar/unidadarbol/" +
					"pagina="+pagina +
					"&filas=10";

			if(id!=null && id != "") URL += "&id=" + id;
			if(institucion!=null && institucion != "") URL += "&institucion=" + institucion;	
			if(institucionentidad!=null && institucionentidad != "") URL += "&institucionentidad=" + institucionentidad;	
			//if(estado!=null && estado != "" ) URL += "&estado=" + estado;

			return Restangular.allUrl(URL).getList();
		},
		
//** Plaza Empleo
		traerUnidadesArbolPlazaEmpleado : function(pagina,estructuraorganica,estado,codigo) {
			var url = "estructuraorganica/consultar/unidadarbolplaza/pagina="+pagina
				+"&unidadarbolerganicaid="+estructuraorganica
				+"&estado="+estado;
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	

			return Restangular.allUrl(
				url
			).getList();
		},
		
		traerPlazaEmpleadosEditar : function(id, plazaid) {
			var url = "estructuraorganica/unidadarbolplaza/"+id+"/"+plazaid+"/0";
		    return Restangular.allUrl(url).customGET();
		},
		
		eliminarPlazaEmpleadosEditar : function(id, plazaid) {
			var url = "estructuraorganica/unidadarbolplaza/"+id+"/"+plazaid+"/0";
		    return Restangular.allUrl(url).customDELETE();
		},

		traerUnidadesFiltro : function(pagina,nombre,codigo,estado) {
			var url = "estructuraorganica/consultar/unidad/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigopresup=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;

			return Restangular.allUrl(url).getList();
		},
		
		traerUnidad : function(id) {
			var url = "estructuraorganica/unidad/"+id+"/0/0";

			return Restangular.allUrl(url).customGET();
		},
		
		guardar:function(objeto){
			var url = "estructuraorganica/unidad/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		guardarArbol:function(objeto){
			var url = "estructuraorganica/unidadarbol/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		guardarArbolPlaza:function(objeto){
			var url = "estructuraorganica/unidadarbolplaza/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		guardarEmpleadosPlaza:function(objeto){
			var url = "estructuraorganica/unidadarbolplazaempleado/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);