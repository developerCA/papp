app.factory("ordenDevengoFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traer: function(
			pagina,
			ejefiscal
		) {
			var url = "ejecucion/consultar/ordendevengo";
			var tObj = {
				filas: "10",
				pagina: pagina.toString(),
				certificacionejerfiscalid: ejefiscal.toString()
			}
			return Restangular.allUrl(url).customPOST(tObj);
		},

		traerFiltro: function(
			pagina,
			ejefiscal,
			codigo,
			precompromiso,
			valorinicial,
			valorfinal,
			fechainicial,
			fechafinal,
			estado
		) {
			var url = "ejecucion/consultar/ordendevengo";
			var tObj = {
				filas: "10",
				pagina: pagina.toString(),
				certificacionejerfiscalid: ejefiscal.toString()
			}

			if(codigo != null && codigo != "") tObj.codigo= "" + codigo;	
			if(precompromiso != null && precompromiso != "") tObj.numprecompromiso= "" + precompromiso;	
			if(valorinicial!= null && valorinicial != "") tObj.valorinicial= "" + valorinicial;	
			if(valorfinal != null && valorfinal != "") tObj.valorfinal= "" + valorfinal;	
			if(fechainicial != null && fechainicial != "") tObj.fechainicial= "" + encodeURIComponent(fechainicial);	
			if(fechafinal != null && fechafinal != "") tObj.fechafinal= "" + encodeURIComponent(fechafinal);	
			if(estado != null && estado != "") tObj.estado= "" + estado;	

			return Restangular.allUrl(url).customPOST(tObj);
		},

		traerNuevo: function(
			ejefiscal
		) {
			var url = "ejecucion/nuevo/ordendevengo/" + ejefiscal;
		    return Restangular.allUrl(url).customGET();
		},

		traerEditar: function(
			id
		) {
			var url = "ejecucion/ordendevengo/"+id+"/0";
		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(
			objeto
		){
			var url = "ejecucion/ordendevengo/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		solicitar:function(
			id,
			tipo,
			cur,
			observacion
		){
			var url = "ejecucion/flujodevengo/" + id + "/" + tipo;
			var tObj = {};
			if (cur != null) {
				tObj.cur = cur;
			};
			if (observacion != null) {
				tObj.observacion = observacion;
			};
			return Restangular.allUrl(url).customPOST(tObj);
		},

		liquidarManualMente:function(
			id,
			motivo
		){
			var url = "ejecucion/flujo/" + id + "/SO";
			return Restangular.allUrl(url).customGET();
		},

		eliminar:function(
			id
		){
			var url = "ejecucion/flujo/" + id + "/EL";
			return Restangular.allUrl(url).customGET();
		},

		nuevoLinea:function(
			id
		){
			var url = "ejecucion/nuevo/ordendevengolinea/" + id ;
			return Restangular.allUrl(url).customGET();
		},

		editarLinea:function(
			id
		){
			var url = "ejecucion/ordendevengolinea/" + id.id + "/" + id.lineaid;
			return Restangular.allUrl(url).customGET();
		},

		obtenerTotal:function(
			tablarelacionid
		){
			var url = "ejecucion/valordisponiblesi/" + tablarelacionid + "/0";
			return Restangular.allUrl(url).customGET();
		},

		listarSubtareas:function(
			ejerciciofiscal,
			unidad
		){
			var url = "planificacion/consultar/nivelactividad/" +
				"nivelactividadejerfiscalid=" + ejerciciofiscal +
				"&nivelactividadunidadid=" + unidad +
				"&tipo=ST";
			return Restangular.allUrl(url).customGET();
		},

		listarItems:function(
			ejerciciofiscal,
			unidad
		){
			var url = "planificacion/consultar/nivelactividad/" +
				"nivelactividadejerfiscalid=" + ejerciciofiscal +
				"&nivelactividadpadreid=" + unidad +
				"&tipo=IT";
			return Restangular.allUrl(url).customGET();
		},

		listarSubItems:function(
			ejerciciofiscal,
			unidad
		){
			var url = "planificacion/consultar/nivelactividad/" +
				"nivelactividadejerfiscalid=" + ejerciciofiscal +
				"&nivelactividadpadreid=" + unidad +
				"&tipo=SI";
			return Restangular.allUrl(url).customGET();
		},

		obtenerDetalles:function(
			id
		){
			var url = "ejecucion/consultar/subitareainfo/" +
				"nivelactividad=" + id;
			return Restangular.allUrl(url).customGET();
		},

		guardarLinea:function(
			objeto
		){
			var url = "ejecucion/ordendevengolinea";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
