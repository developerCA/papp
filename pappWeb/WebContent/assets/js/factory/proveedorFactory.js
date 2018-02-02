app.factory("proveedorFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/estructuraorganica");

	return {
		traer: function(pagina) {
			  return Restangular.allUrl("estructuraorganica/consultar/unidad/pagina="+pagina).getList();
		},

		traerFiltro: function(
			pagina,
			idEjerciciofiscal,
			nombre,
			codigo,
			estado
		) {
			var URL = "estructuraorganica/consultar/unidad/" +
				"pagina=" + pagina +
				"&filas=10" +
				"&ejerciciofiscal=" + idEjerciciofiscal;

			if(nombre!=null && nombre != "") URL += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") URL += "&codigopresup=" + codigo;	
			if(estado!=null && estado != "" ) URL += "&estado=" + estado;

			return Restangular.allUrl(URL).getList();
		},

		modalTraerFiltro: function(
			pagina,
			codigo,
			nombre,
			nombremostrado,
			razonsocial,
			estado,
			proveedor
		) {
			var URL = "administrar/consultar/busquedasocionegocio/" +
				"pagina=" + pagina +
				"&filas=10";

			if(codigo!=null && codigo != "") URL += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") URL += "&nombre=" + nombre;	
			if(nombremostrado!=null && nombremostrado != "") URL += "&nombremostrado=" + nombremostrado;	
			if(estado!=null && estado != "" ) URL += "&estado=" + estado;
			if(razonsocial!=null && razonsocial != "") URL += "&razonsocial=" + razonsocial;	
			if(proveedor!=null && proveedor != "") URL += "&proveedor=" + proveedor;	

			return Restangular.allUrl(URL).customGET();
		},
	}
} ]);