app.factory("proyectoFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {

		traerProyecto : function(pagina) {
			return Restangular.allUrl("planificacion/consultar/proyecto/pagina="+pagina).getList();
		},

		traerProyectoFiltro : function(pagina,codigo,nombre) {
			var url = "planificacion/consultar/proyecto/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			return Restangular.allUrl(url).getList();
		},
		
		traerProyectoEditar : function(id) {
			var url = "planificacion/proyecto/"+id+"/-1";

			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/proyecto/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);