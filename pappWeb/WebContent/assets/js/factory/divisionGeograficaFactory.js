app.factory("divisionGeograficaFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerDivisiones : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/divisiongeografica/pagina="+pagina).getList();
			  
		},
		
		traerDivisionesTipo : function(pagina,tipo) {
			  
			  return Restangular.allUrl("administrar/consultar/divisiongeografica/pagina="+pagina+"&tipo="+tipo).getList();
			  
		},
		
		traerDivisionesFiltro : function(pagina,nombre,codigo,estado,tipo) {
			  
			var url = "administrar/consultar/divisiongeografica/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			if(tipo!=null && tipo != "" ) url += "&tipo=" + tipo;
			 
			return Restangular.allUrl(url).getList();
			  
		},

		traerDivisionesFullFiltro : function(pagina,tipo,padreid) {
			  
			var url = "administrar/consultar/divisiongeografica/pagina="+pagina+"&estado=A";

			if(tipo!=null && tipo != "") url += "&tipo=" + tipo;
			if(padreid!=null && padreid != "" ) url += "&padreid=" + padreid;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerDivisionesTipoFiltro : function(pagina,tipo,nombre,codigo,estado) {
			  
			var url = "administrar/consultar/divisiongeografica/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			if(tipo!=null && tipo != "" ) url += "&tipo=" + tipo;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerDivision : function(id) {
			  
			var url = "administrar/divisiongeografica/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/divisiongeografica/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);