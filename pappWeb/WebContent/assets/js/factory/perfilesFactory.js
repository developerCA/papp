app.factory("PerfilesFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/seguridad");

	return {
		traer : function(pagina) {
			  return Restangular.allUrl("seguridad/consultar/perfil/pagina="+pagina).getList();
		},

		traerFiltro : function(pagina,nombre) {
			var url = "seguridad/consultar/perfil/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerPermiso : function(id) {
			var url = "seguridad/perfil/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "seguridad/perfil/";

			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);