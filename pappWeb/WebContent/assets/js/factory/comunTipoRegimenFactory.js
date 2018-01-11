app.factory("ComunTipoRegimenFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/comun");

    return {
        traer: function (pagina, ejercicio) {
        	var url = "comun/tiporegimen";
        	return Restangular.allUrl(url).customGET();
        },
    }
}]);