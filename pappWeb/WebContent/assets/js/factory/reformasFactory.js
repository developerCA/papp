app.factory("reformasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traer: function(
			pagina,
			ejefiscal
		) {
			var url = "ejecucion/consultar/reforma";
			var tObj = {
				filas: "10",
				pagina: pagina.toString(),
				ejerciciofiscalid: ejefiscal.toString()
			}
			return Restangular.allUrl(url).customPOST(tObj);
		},

		traerFiltro: function(
			pagina,
			ejefiscal,
			fechainicial,
			fechafinal,
			codigo,
			estado,
			ejerciciofiscal,
			tipo
		) {
			var url = "ejecucion/consultar/reforma";
			var tObj = {
				filas: "10",
				pagina: pagina.toString(),
				ejerciciofiscalid: ejefiscal.toString()
			}

			if(fechainicial != null && fechainicial != "") tObj.fechainicial= "" + encodeURIComponent(fechainicial);	
			if(fechafinal != null && fechafinal != "") tObj.fechafinal= "" + encodeURIComponent(fechafinal);	
			if(codigo != null && codigo != "") tObj.codigo= "" + codigo;	
			if(estado != null && estado != "") tObj.estado= "" + estado;	
			if(ejerciciofiscal!= null && ejerciciofiscal != "") tObj.ejerciciofiscal= "" + ejerciciofiscal;	
			if(tipo != null && tipo != "") tObj.tipo= "" + tipo;

			return Restangular.allUrl(url).customPOST(tObj);
		},

		traerNuevo: function(
			ejefiscal
		) {
			var url = "ejecucion/nuevo/reforma/" + ejefiscal;
		    return Restangular.allUrl(url).customGET();
		},

		traerEditar: function(
			id
		) {
			var url = "ejecucion/reforma/"+id+"/0";
		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(
			objeto
		){
			var url = "ejecucion/reforma/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		solicitar:function(
			id,
			tipo,
			cur,
			observacion
		){
			var url = "ejecucion/flujoreforma/" + id + "/" + tipo;
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
			var url = "ejecucion/nuevo/certificacionlinea/" + id ;
			return Restangular.allUrl(url).customGET();
		},

		editarLinea:function(
			id
		){
			var url = "ejecucion/certificacionlinea/" + id.id + "/" + id.lineaid;
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

		eliminarLinea:function(
			id,
			linea
		){
			var url = "ejecucion/certificacionlinea/" +
				id + "/" +
				linea;
			return Restangular.allUrl(url).customDELETE();
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
			var url = "ejecucion/certificacionlinea";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
