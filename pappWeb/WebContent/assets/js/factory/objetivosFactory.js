app.factory("objetivosFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerObjetivos : function(pagina, ejercicio) {
			var url = "planificacion/consultar/objetivogrilla/pagina=" + pagina + "&objetivoejerciciofiscalid=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerObjetivosFiltro : function(pagina, codigo, descripcion) {
			var url = "planificacion/consultar/objetivogrilla/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(descripcion!=null && descripcion != "") url += "&descripcion=" + descripcion;	

			return Restangular.allUrl(url).getList();
		},

		traerObjetivosEditar : function(id) {
			var url = "planificacion/objetivogrilla/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/objetivogrilla/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
