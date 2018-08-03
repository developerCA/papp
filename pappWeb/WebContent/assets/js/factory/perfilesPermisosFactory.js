app.factory("perfilesPermisosFactory", [ "Restangular", function(Restangular) {
	var service = Restangular.service("/administrar");

	return {
		traer : function(pagina) {
			  return Restangular.allUrl("seguridad/consultar/perfil/pagina="+pagina).getList();
		},

		traerFiltro : function(pagina,permisoid,nombre) {
			var url = "seguridad/consultar/perfil/pagina="+pagina;

			if(permisoid!=null && permisoid != "") url += "&permisoid=" + permisoid;
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;

			return Restangular.allUrl(url).getList();
		},
	}
} ]);