app.factory("GradoEscalaFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
				
		traer : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/gradoescala/pagina="+pagina).getList();
			  
		},
		
		traerFiltro : function(pagina,codigo,nombregrado,nombrefuerza,grupoocupacional,estado) {
			  
			var url = "administrar/consultar/gradoescala/pagina="+pagina;

			if(grupoocupacional!=null && grupoocupacional != "") url += "&grupoocupacional=" + grupoocupacional;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombregrado!=null && nombregrado != "") url += "&npnombregrado=" + nombregrado;
			if(nombrefuerza!=null && nombrefuerza != "") url += "&npnombrefuerza=" + nombrefuerza;
			if(grupoocupacional!=null && grupoocupacional != "") url += "&npgrupoocupacional=" + grupoocupacional;
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			console.log(url);
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerObj : function(id) {
			  
			var url = "administrar/gradoescala/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/gradoescala/";
			return Restangular.allUrl(url).customPOST(objeto);
		},		
	
	}
} ]);