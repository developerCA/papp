app.factory("institutoEntidadFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/estructuraorganica");

	return {

		traerInstitutoEntidad : function(pagina,ejercicoFiscal) {
			return Restangular.allUrl("estructuraorganica/consultar/institutoentidad/" +
				"pagina=" + pagina +
				"&estado=A" +
				"&filas=25" +
				(ejercicoFiscal != null? "&ejerciciofiscalid=" + ejercicoFiscal: "")
			).getList();
		},
		
		traerInstitutoEntidadFiltro : function(pagina,codigoInstitucion,nombreInstitucion,codigoEntidad,nombreEntidad,ejercicoFiscal,estado) {
			var url = "estructuraorganica/consultar/institutoentidad/pagina="+pagina+"&filas=25";

			if(codigoInstitucion!=null && codigoInstitucion != "") url += "&codigoInstitucion=" + codigoInstitucion;	
			if(nombreInstitucion!=null && nombreInstitucion != "") url += "&nombreInstitucion=" + nombreInstitucion;	
			if(codigoEntidad!=null && codigoEntidad != "") url += "&codigo=" + codigoEntidad;	
			if(nombreEntidad!=null && nombreEntidad != "") url += "&nombre=" + nombreEntidad;	
			if(ejercicoFiscal!=null && ejercicoFiscal != "") url += "&ejerciciofiscalid=" + ejercicoFiscal;	
			if(estado!=null && estado != "") url += "&estado=" + estado;

			console.log("FILTRO: "+url);
			return Restangular.allUrl(url).getList();
		},

		traerInstitutoEntidadUno : function(id) {
			var url = "estructuraorganica/institutoentidad/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		traerEjerciciosFiscales : function() {
			  return Restangular.allUrl("administrar/consultar/ejerciciofiscal/pagina=1&estado=A").getList();
		},

		guardar:function(objeto){
			var url = "estructuraorganica/institutoentidad/";

			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);