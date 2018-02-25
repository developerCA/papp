app.factory("ordenGastoFactory", [ "Restangular",
function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traer : function(
			pagina,
			ejefiscal
		) {
			var url = "ejecucion/consultar/ordengasto";
			var tObj = {
				filas: "10",
				pagina: pagina.toString(),
				ordengastoejerfiscalid: ejefiscal.toString()
			}
			return Restangular.allUrl(url).customPOST(tObj);
		},

		traerFiltro : function(
			pagina,
			ejefiscal,
			codigo,
			compromiso,
			certificacion,
			valorinicial,
			valorfinal,
			fechainicial,
			fechafinal,
			estado
		) {
			var url = "ejecucion/consultar/ordengasto";
			var tObj = {
				filas: "10",
				pagina: pagina.toString(),
				ordengastoejerfiscalid: ejefiscal.toString()
			}

			if(codigo != null && codigo != "") tObj.codigo= "" + codigo;	
			if(compromiso != null && compromiso != "") tObj.compromiso= "" + compromiso;	
			if(certificacion != null && certificacion != "") tObj.certificacion= "" + certificacion;	
			if(valorinicial!= null && valorinicial != "") tObj.valorinicial= "" + valorinicial;	
			if(valorfinal != null && valorfinal != "") tObj.valorfinal= "" + valorfinal;	
			if(fechainicial != null && fechainicial != "") tObj.fechainicial= "" + fechainicial;	
			if(fechafinal != null && fechafinal != "") tObj.fechafinal= "" + fechafinal;
			if(estado != null && estado != "") tObj.estado= "" + estado;	

			return Restangular.allUrl(url).customPOST(tObj);
		},

		traerContratoFiltro : function(
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

		nuevo : function(ejefisca) {
			var url = "ejecucion/nuevo/ordengasto/" + ejefisca;

		    return Restangular.allUrl(url).customGET();
		},

		editar : function(id) {
			var url = "ejecucion/ordengasto/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},
		
		guardar:function(objeto){
			var url = "ejecucion/ordengasto/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		eliminar:function(id){
			//var url = "ejecucion/ordengasto/";
			//return Restangular.allUrl(url).customPOST(objeto);
		},

		solicitar:function(
			id,
			tipo,
			cur,
			observacion
		){
			var url = "ejecucion/flujoordenes/" + id + "/" + tipo;
			var tObj = {};
			if (cur != null) {
				tObj.cur = cur;
			};
			if (observacion != null) {
				tObj.observacion = observacion;
			};
			return Restangular.allUrl(url).customPOST(tObj);
		},

		eliminarLinea:function(
			id,
			linea
		){
			var url = "ejecucion/ordengastolinea/" +
				id + "/" +
				linea;
			return Restangular.allUrl(url).customDELETE();
		},
	}
} ]);