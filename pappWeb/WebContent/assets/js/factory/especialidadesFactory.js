app.factory("especialidadesFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");

	return {
		
		
		traerEspecialidades : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/especialidades/pagina="+pagina).getList();
			  
		},
		
		traerEspecialidadesFiltro : function(pagina,codigo,nombre,grado,padre,estado) {
			  
			var url = "administrar/consultar/especialidades/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(estado!=null && estado != "") url += "&estado=" + estado;	
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerEspecialidadesEditar : function(id) {
			  
			var url = "administrar/especialidades/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/especialidades/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);