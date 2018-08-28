app.factory("perfilesPermisosFactory", [ "Restangular", function(Restangular) {
	var service = Restangular.service("/administrar");

	return {
		traer : function(pagina) {
			  return Restangular.allUrl("seguridad/consultar/perfilpermiso/pagina="+pagina+"&filas=10").customGET();
		},

		traerFiltro : function(pagina,permisoid,nombre) {
			var url = "seguridad/consultar/perfilpermiso/pagina="+pagina+"&filas=10";

			if(permisoid!=null && permisoid != "") url += "&permisoid=" + permisoid;
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;

			return Restangular.allUrl(url).customGET();
		},
	}
} ]);