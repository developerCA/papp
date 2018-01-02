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
	}
} ]);