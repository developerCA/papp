app.factory("unidadFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/estructuraorganica");

	return {
		
		
		traerUnidades : function(pagina) {
			 
			  return Restangular.allUrl("estructuraorganica/consultar/unidad/pagina="+pagina).getList();
			  
		},

//** Unidades Arbol
		traerUnidadesArbol : function(pagina,estructuraorganica,estado) {
			  return Restangular.allUrl("estructuraorganica/consultar/unidadarbol/pagina="+pagina+"&unidadarbolerganicaid="+estructuraorganica+"&estado="+estado).getList();
		},
		
		traerUnidadesArbolhijos : function(pagina,estructuraorganica,padre,estado) {
			 
			  return Restangular.allUrl("estructuraorganica/consultar/unidadarbol/pagina="+pagina+"&unidadarbolerganicaid="+estructuraorganica+"&id="+padre+"&estado="+estado).getList();
			  
		},
		
		traerUnidadArbol : function(id) {
			  
			var url = "estructuraorganica/unidadarbol/"+id+"/0/0";
		    console.log(url);
		    return Restangular.allUrl(url).customGET();
			  
		},
		
//** Plaza Empleo
		traerUnidadesArbolPlazaEmpleado : function(pagina,estructuraorganica,estado) {
			  return Restangular.allUrl(
					  "estructuraorganica/consultar/unidadarboldetail/pagina="+pagina
					  +"&unidadarbolerganicaid="+estructuraorganica
					  +"&estado="+estado
			  ).getList();
		},

		traerUnidadesFiltro : function(pagina,nombre,codigo,estado) {
			  
			var url = "estructuraorganica/consultar/unidad/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigopresup=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerUnidad : function(id) {
			  
			var url = "estructuraorganica/unidad/"+id+"/0/0";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "estructuraorganica/unidad/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		guardarArbol:function(objeto){
			var url = "estructuraorganica/unidadarbol/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);