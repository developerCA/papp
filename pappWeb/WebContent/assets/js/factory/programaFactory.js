app.factory("programaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {

		traerPrograma : function(pagina) {
			return Restangular.allUrl("planificacion/consultar/programa/pagina="+pagina).getList();
		},

		traerProgramaFiltro : function(pagina,codigo,nombre) {
			var url = "planificacion/consultar/programa/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			return Restangular.allUrl(url).getList();
		},
		
		traerProgramaEditar : function(id) {
			var url = "planificacion/programa/"+id+"/-1";

			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/programa/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);