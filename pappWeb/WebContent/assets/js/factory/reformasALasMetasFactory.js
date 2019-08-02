app.factory("reformasALasMetasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traer: function(
			pagina,
			ejefiscal
		) {
			return this.traerFiltro(
					pagina,
					ejefiscal,
					null,
					null,
					null,
					null
				);
		},

		traerFiltro: function(
			pagina,
			ejefiscal,
			codigo,
			fechainicial,
			fechafinal,
			estado
		) {
			var url = "ejecucion/consultar/reformameta";
			var tObj = {
				filas: "10",
				pagina: pagina.toString()
			}

			if(ejefiscal != null && ejefiscal != "") tObj.ejerciciofiscalid= "" + ejefiscal;	
			if(codigo != null && codigo != "") tObj.codigo= "" + codigo;	
			if(fechainicial != null && fechainicial != "") tObj.fechainicial= "" + encodeURIComponent(fechainicial);	
			if(fechafinal != null && fechafinal != "") tObj.fechafinal= "" + encodeURIComponent(fechafinal);	
			if(estado != null && estado != "") tObj.estado= "" + estado;	
			
			return Restangular.allUrl(url).customPOST(tObj);
		},

		traerNuevo: function(
			ejefiscal
		) {
			var url = "ejecucion/nuevo/reformameta/" + ejefiscal;
		    return Restangular.allUrl(url).customGET();
		},

		traerEditar: function(
			id
		) {
			var url = "ejecucion/reformameta/"+id+"/0";
		    return Restangular.allUrl(url).customGET();
		},

		traerEditarMeta: function(
			reforma
		) {
			var url = "ejecucion/obtenerreformametasubtarea";
		    return Restangular.allUrl(url).customPOST(reforma);
		},

		editarLineaMeta: function(
			anio,
			reformaliea
		) {
			var url = "ejecucion/metareforma/rm/"+anio;
		    return Restangular.allUrl(url).customPOST(reformaliea);
		},

		editarLineaMetaSubtareaMeta: function(
			anio,
			reformaliea
		) {
			var url = "ejecucion/metareforma/rmm/"+anio;
		    return Restangular.allUrl(url).customPOST(reformaliea);
		},

		guardar:function(
			objeto
		){
			var url = "ejecucion/reformameta/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		solicitar:function(
			id,
			tipo,
			cur,
			observacion
		){
			var url = "ejecucion/flujoreformameta/" + id + "/" + tipo;
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
			var url = "ejecucion/nuevo/reformametalinea/" + id ;
			return Restangular.allUrl(url).customGET();
		},

		editarLinea:function(
			id
		){
			var url = "ejecucion/reformametalinea/" + id.id + "/" + id.lineaid;
			return Restangular.allUrl(url).customGET();
		},

		obtenerTotal:function(
			tablarelacionid,
			id
		){
			var url = "ejecucion/valordisponiblesi/" + tablarelacionid + "/" + id;
			return Restangular.allUrl(url).customGET();
		},

		obtenerTodoSubtarea:function(
			tablarelacionid
		){
			var url = "ejecucion/datossubtarea/" + tablarelacionid + "/0";
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
			var url = "ejecucion/reformametalinea/" +
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
			var url = "ejecucion/reformametalinea";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		guardarLineaMeta:function(
			objeto
		){
			var url = "planificacion/cronograma";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		editarLineaDistMeta:function(
			id
		){
			var url = "ejecucion/reformametasubtarea/" + id.id + "/" + id.lineaid;
			return Restangular.allUrl(url).customGET();
		},

		guardarLineaSubtarea:function(
			objeto
		){
			var url = "ejecucion/reformametasubtarea";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
