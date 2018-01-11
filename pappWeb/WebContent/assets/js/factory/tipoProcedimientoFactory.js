app.factory("ProcedimientoFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/comun");

    return {
        traer: function (pagina, ejercicio) {
        	var url = "comun/procedimiento";
        	return Restangular.allUrl(url).customGET();
        },
    }
}]);