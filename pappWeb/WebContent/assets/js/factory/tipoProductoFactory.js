app.factory("TipoProductoFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/comun");

    return {
        traer: function (pagina, ejercicio) {
        	var url = "comun/tipoproducto";
        	return Restangular.allUrl(url).customGET();
        },
    }
}]);