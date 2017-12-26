app.factory("institutoEntidadFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/estructuraorganica");

	return {

		traerInstitutoEntidad : function(pagina) {
			  return Restangular.allUrl("estructuraorganica/consultar/institutoentidad/pagina="+pagina+"&estado=A&filas=25").getList();
		},
		
		traerInstitutoEntidadFiltro : function(pagina,codigoInstitucion,nombreInstitucion,codigoEntidad,nombreEntidad,ejercicoFiscal,estado) {
			var url = "estructuraorganica/consultar/institutoentidad/pagina="+pagina+"&filas=25";

			if(codigoInstitucion!=null && codigoInstitucion != "") url += "&codigoInstitucion=" + codigoInstitucion;	
			if(nombreInstitucion!=null && nombreInstitucion != "") url += "&nombreInstitucion=" + nombreInstitucion;	
			if(codigoEntidad!=null && codigoEntidad != "") url += "&codigo=" + codigoEntidad;	
			if(nombreEntidad!=null && nombreEntidad != "") url += "&nombre=" + nombreEntidad;	
			if(ejercicoFiscal!=null && ejercicoFiscal != "") url += "&ejercicoFiscal=" + ejercicoFiscal;	
			if(estado!=null && estado != "") url += "&estado=" + estado;

			console.log("FILTRO: "+url);
			return Restangular.allUrl(url).getList();
		},

		traerInstitutoEntidadUno : function(id) {
			var url = "estructuraorganica/institutoentidad/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "estructuraorganica/institutoentidad/";

			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);