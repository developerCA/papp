app.factory("ordenDevengoLineasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
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
			nivelactividad
		){
			var url = "ejecucion/consultar/subitemunidadinfo/nivelactividad=" + nivelactividad;
			return Restangular.allUrl(url).customGET();
		},

		obtenerOtros:function(
			idsubitem,
			idordengasto
		){
			var url = "ejecucion/datoslineaordend/" + idsubitem + "/" + idordengasto;
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
			id
		){
			var url = "ejecucion/consultar/subitemordengasto/" +
				"pagina=1" +
				"&ordengastoid=" + id;
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
