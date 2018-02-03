app.factory("contratoFactory", [ "Restangular",
function(Restangular) {

	var service = Restangular.service("/administrar");

	return {
		traer : function(
			pagina,
			codigo,
			nombre,
			fecha,
			estado
		) {
			var url = "administrar/consultar/contrato/" +
				"pagina=" + pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(fecha!=null && fecha != "") url += "&fecha=" + fecha;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;

			return Restangular.allUrl(url).getList();
		},
	}
} ]);