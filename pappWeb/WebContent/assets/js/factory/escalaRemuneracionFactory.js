app.factory("escalaRemuneracionFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traer : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/escalarmu/pagina="+pagina).getList();
			  
		},
		
		traerFiltro : function(pagina,grupoocupacional,codigo,grado,estado) {
			  
			var url = "administrar/consultar/escalarmu/pagina="+pagina;

			if(grupoocupacional!=null && grupoocupacional != "") url += "&grupoocupacional=" + grupoocupacional;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(grado!=null && grado != "") url += "&gradocodigo=" + grado;
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
						 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerObj : function(id) {
			  
			var url = "administrar/escalarmu/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/escalarmu/";
			return Restangular.allUrl(url).customPOST(objeto);
		},		
	
	}
} ]);