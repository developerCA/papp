app.factory("proveedorrucFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		traer : function(pagina) {
			  return Restangular.allUrl("administrar/consultar/proveedorruc/pagina="+pagina+"&filas=10").customGET();
		},
		
		traerFiltro : function(pagina, codigo, nombremostrado, nombrecomercial, representantelegal, estado) {
			var url = "administrar/consultar/proveedorruc/pagina="+pagina+"&filas=10";

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombremostrado!=null && nombremostrado != "") url += "&nombremostrado=" + nombremostrado;
			if(nombrecomercial!=null && nombrecomercial != "") url += "&nombrecomercial=" + nombrecomercial;
			if(representantelegal!=null && representantelegal != "") url += "&representantelegal=" + representantelegal;
			if(estado!=null && estado != "") url += "&estado=" + estado;

			return Restangular.allUrl(url).customGET();
		},
		
		traerEditar : function(id) {
			var url = "administrar/proveedorruc/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "administrar/proveedorruc/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);