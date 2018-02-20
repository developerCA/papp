app.factory("organismoFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");
	var organismo = "organismo";

	return {
		iniciar: function() {
			organismo = "organismo";
		},

		traer : function(pagina,ejercicio) {
			  return Restangular.allUrl("administrar/consultar/" + organismo + "/pagina="+pagina+ "&filas=10&ejerciciofiscalid=" + ejercicio).customGET();
		},
		
		traerFiltro : function(pagina,ejercicio,codigo,nombre,estado) {
			var url = "administrar/consultar/" + organismo + "/pagina="+pagina+ "&filas=10&ejerciciofiscalid=" + ejercicio;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;

			return Restangular.allUrl(url).customGET();
		},

		editar : function(id) {
			var url = "administrar/" + organismo + "/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "administrar/" + organismo + "/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		prestamo:function(){
			organismo = "organismoprestamo";
		},
	}
} ]);