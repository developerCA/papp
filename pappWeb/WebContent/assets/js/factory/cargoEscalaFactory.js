app.factory("CargoEscalaFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
				
		traer : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/cargoescala/pagina="+pagina).getList();
			  
		},
		
		traerFiltro : function(pagina,codigo,nombrecargo,grupoocupacional,estado) {
			  
			var url = "administrar/consultar/cargoescala/pagina="+pagina;

			if(grupoocupacional!=null && grupoocupacional != "") url += "&grupoocupacional=" + grupoocupacional;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombrecargo!=null && nombrecargo != "") url += "&npnombrecargo=" + nombrecargo;
			if(grupoocupacional!=null && grupoocupacional != "") url += "&npgrupoocupacional=" + grupoocupacional;
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerObj : function(id) {
			  
			var url = "administrar/cargoescala/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/cargoescala/";
			return Restangular.allUrl(url).customPOST(objeto);
		},		
	
	}
} ]);