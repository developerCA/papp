app.factory("empleadosFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/estructuraorganica");

	return {
		
		
		traerEmpleados : function(pagina) {
			  
			  return Restangular.allUrl("estructuraorganica/consultar/empleado/pagina="+pagina).getList();
			  
		},
		
		traerEmpleadosFiltro : function(pagina,codigo,nombre,estado) {
			  
			var url = "estructuraorganica/consultar/empleado/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;
			if(estado!=null && estado != "") url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerEmpleadosEditar : function(id) {
			  
			var url = "estructuraorganica/empleado/"+id+"/-1/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "estructuraorganica/empleado/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);