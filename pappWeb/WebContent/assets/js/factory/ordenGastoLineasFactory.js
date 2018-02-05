app.factory("ordenGastoLineasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		nuevoLinea:function(
			id
		){
			var url = "ejecucion/nuevo/ordengastolinea/" + id ;
			return Restangular.allUrl(url).customGET();
		},

		editarLinea:function(
			id
		){
			var url = "ejecucion/ordengastolinea/" + id.id + "/" + id.lineaid;
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

		listarSubItems:function(
			id
		){
			var url = "ejecucion/consultar/subitemcertificado/" +
				"pagina=1" +
				"&certificacionid=" + id;

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
			var url = "ejecucion/ordengastolinea";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
