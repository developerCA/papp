app.factory("empleadosFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		traerEmpleados : function(pagina) {
			  return Restangular.allUrl("administrar/consultar/empleado/pagina="+pagina).getList();
		},
		
		traerEmpleadosFiltro : function(pagina,codigo,nombre,tipo,estado) {
			var url = "administrar/consultar/empleado/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombre!=null && nombre != "") url += "&nombremostrado=" + nombre;
			if(tipo!=null && tipo != "") url += "&emptipo=" + tipo;
			if(estado!=null && estado != "") url += "&estado=" + estado;

			return Restangular.allUrl(url).getList();
		},
		
		traerEmpleadosEditar : function(id) {
			var url = "administrar/empleado/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		traerGradoEscalaEditar : function(id) {
			var url = "administrar/gradoescala/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		traerEspecialidadesEditar : function(id) {
			var url = "administrar/especialidades/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

		traerClasificacionEditar : function(id) {
			var url = "administrar/clasificacion/"+id+"/-1";

		    return Restangular.allUrl(url).customGET();
		},

        traerTipoIdentidicacionEditar: function (id) {
            var url = "administrar/tipoidentificacion/" + id + "/-1";

            return Restangular.allUrl(url).customGET();
        },

		guardar:function(objeto){
			var url = "administrar/empleado/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);