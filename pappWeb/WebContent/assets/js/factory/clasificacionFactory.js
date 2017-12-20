app.factory("clasificacionFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");
	var fuerza = null;

	return {
		setFuerza : function(newfuerza) {
			fuerza = newfuerza;
		},
		
		traerClasificaciones : function(pagina) {
			var url = "administrar/consultar/clasificacion/pagina="+pagina;
			return Restangular.allUrl(url).getList();
		},
		
		traerClasificacionesFuerza : function(pagina) {
			var url = "administrar/consultar/fuerzaclasificacion/pagina="+pagina;
			if (fuerza != null) {
				url += "&npfuerzaid=" + fuerza;
			}
			console.log(fuerza);
			console.log(url);
			return Restangular.allUrl(url).getList();
		},

		traerClasificacionesActivos : function(pagina,estado) {
			var url = "administrar/consultar/clasificacion/pagina="+pagina;

			if(estado!=null && estado != "" ) url += "&estado=" + estado;
						 
			return Restangular.allUrl(url).getList();
		},

		traerClasificacionesFuerzaFiltro : function(pagina,codigo,nombre, sigla, estado) {
			var url = "administrar/consultar/fuerzaclasificacion/pagina="+pagina;

			if (fuerza != null) {
				url += "&fuerza=" + fuerza;
			}
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(sigla!=null && sigla != "") url += "&sigla=" + sigla;
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
						 
			return Restangular.allUrl(url).getList();
		},

		traerClasificacionesFiltro : function(pagina,codigo,nombre, sigla, estado) {
			var url = "administrar/consultar/clasificacion/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(sigla!=null && sigla != "") url += "&sigla=" + sigla;
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
						 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerClasificacion : function(id) {
			  
			var url = "administrar/clasificacion/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/clasificacion/";
			return Restangular.allUrl(url).customPOST(objeto);
		},			
	
	}
} ]);