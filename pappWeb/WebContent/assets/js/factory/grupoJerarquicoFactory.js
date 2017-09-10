app.factory("grupoJerarquicoFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerGrupos : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/grupo/pagina="+pagina).getList();
			  
		},
		
		traerGruposFiltro : function(pagina,nombre,codigo,estado,sigla) {
			  
			var url = "administrar/consultar/grupo/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			if(sigla!=null && sigla != "" ) url += "&sigla=" + sigla;
			
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerGrupo : function(id) {
			  
			var url = "administrar/grupo/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/grupo/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);