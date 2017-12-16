app.factory("claseDocumentoFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/comun");

	return {
		traerClases : function(ejercicio) {
			var url = "comun/tipodocumentoclasedocumento@" + ejercicio;

			return Restangular.allUrl(url).getList();
		},

		traerClasesFiltro : function(pagina,ejercicio,nombre,codigo,estado) {
			var url = "comun/tipodocumentoclasedocumento@" + ejercicio;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;

			return Restangular.allUrl(url).getList();
		}
	}
} ]);