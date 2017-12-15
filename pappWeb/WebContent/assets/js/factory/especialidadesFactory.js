app.factory("especialidadesFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");
	var fuerza = null;

	return {
		setFuerza : function(newfuerza) {
			fuerza = newfuerza;
		},
		
		traerEspecialidades : function(pagina) {
			var url = "administrar/consultar/especialidades/pagina="+pagina;
			if (fuerza != null) {
				url += "&fuerza=" + fuerza;
			}
			console.log(fuerza);
			console.log(url);
			return Restangular.allUrl(url).getList();
		},
		
		traerEspecialidadesFiltro : function(pagina,codigo,nombre,sigla,tipo,estado) {
			var url = "administrar/consultar/especialidades/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;
			if(sigla!=null && sigla != "") url += "&sigla=" + sigla;
			if(tipo!=null && tipo != "") url += "&tipo=" + tipo;
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