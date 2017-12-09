app.factory("actividadFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {

		traerActividad : function(pagina) {
			return Restangular.allUrl("planificacion/consultar/actividad/pagina="+pagina).getList();
		},

		traerActividadFiltro : function(pagina,codigo,nombre) {
			var url = "planificacion/consultar/actividad/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			return Restangular.allUrl(url).getList();
		},
		
		traerActividadEditar : function(id) {
			var url = "planificacion/actividad/"+id+"/-1";

			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/actividad/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);