app.factory("institutoEntidadFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/estructuraorganica");

	return {

		traerInstitutoEntidad : function(pagina) {
			  return Restangular.allUrl("estructuraorganica/consultar/institutoentidad/pagina="+pagina+"&estado=A").getList();
		},
		
		traerInstitutoEntidadFiltro : function(pagina,nombre) {
			var url = "estructuraorganica/consultar/institutoentidad/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			return Restangular.allUrl(url).getList();
		},

		traerInstitutoEntidadUno : function(id) {
			var url = "estructuraorganica/institutoentidad/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "estructuraorganica/institutoentidad/";

			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);