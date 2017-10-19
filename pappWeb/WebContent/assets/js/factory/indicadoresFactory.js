app.factory("indicadoresFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {

		traerIndicadores : function(pagina) {
			  return Restangular.allUrl("planificacion/consultar/indicador/pagina="+pagina).getList();
		},

		traerIndicadoresFiltro : function(pagina,codigo,nombre) {
			var url = "planificacion/consultar/indicador/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && fuerza != "") url += "&nombre=" + fuerza;	

			return Restangular.allUrl(url).getList();
		},

		traerIndicadoresEditar : function(id) {
			var url = "planificacion/indicador/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/indicador/";

			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);