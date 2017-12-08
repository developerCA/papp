app.factory("MatrizIndicadoresFactory", [ "Restangular", function(Restangular) {

	return {
		traerUsuario : function() {
//			var url = "seguridad/usuario/-1/-1";
			var url = "seguridad/";

			return Restangular.allUrl(url).customGET();
		},
	}
} ]);