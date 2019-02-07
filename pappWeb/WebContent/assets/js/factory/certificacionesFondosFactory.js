app.factory("certificacionesFondosFactory", [ "Restangular", function(Restangular) {

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
			precompromiso,
			valorinicial,
			valorfinal,
			fechainicial,
			fechafinal,
			estado
		) {
			var url = "ejecucion/consultar/certificacion";
			var tObj = {
				filas: "10",
				pagina: pagina.toString()
			}

			if(ejefiscal != null && ejefiscal != "") tObj.certificacionejerfiscalid= "" + ejefiscal;	
			if(codigo != null && codigo != "") tObj.codigo= "" + codigo;	
			if(precompromiso != null && precompromiso != "") tObj.numprecompromiso= "" + precompromiso;	
			if(valorinicial!= null && valorinicial != "") tObj.valorinicial= "" + valorinicial;	
			if(valorfinal != null && valorfinal != "") tObj.valorfinal= "" + valorfinal;	
			if(fechainicial != null && fechainicial != "") tObj.fechainicial= "" + encodeURIComponent(fechainicial);	
			if(fechafinal != null && fechafinal != "") tObj.fechafinal= "" + encodeURIComponent(fechafinal);	
			if(estado != null && estado != "") tObj.estado= "" + estado;	

			return Restangular.allUrl(url).customPOST(tObj);
		},

		modalTraerFiltro: function(
			pagina,
			ejefiscal,
			unidadid,
			codigo,
			descripcion
		) {
			var url = "ejecucion/consultar/certificacionbusqueda";
			var tObj = {
				filas: "10",
				pagina: pagina.toString(),
				ejerciciofiscal: ejefiscal.toString(),
				estado: "AP",
				unidadid: unidadid.toString()
			}

			if(codigo != null && codigo != "") tObj.codigo = "" + codigo;	
			if(descripcion != null && descripcion != "") tObj.descripcion = "" + descripcion;	

			return Restangular.allUrl(url).customPOST(tObj);
		},

		traerCertificacionesFondosNuevo: function(
			ejefiscal
		) {
			var url = "ejecucion/nuevo/certificacion/" + ejefiscal;
		    return Restangular.allUrl(url).customGET();
		},

		traerCertificacionesFondosEditar: function(
			id
		) {
			var url = "ejecucion/certificacion/"+id+"/-1";
		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(
			objeto
		){
			var url = "ejecucion/certificacion/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		solicitar:function(
			id,
			tipo,
			cur,
			observacion
		){
			var url = "ejecucion/flujo/" + id + "/" + tipo;
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
			tablarelacionid,
			id
		){
			var url = "ejecucion/valordisponiblesi/" + tablarelacionid + "/" + id;
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
