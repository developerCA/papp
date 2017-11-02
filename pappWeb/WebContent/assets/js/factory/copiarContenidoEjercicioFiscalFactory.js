app.factory("copiarContenidoEjercicioFiscalFactory", [ "Restangular", function(Restangular) {
		
	var service = Restangular.service("/administrar");

	return {
		guardar:function(objeto){
			var url = "administrar/copiardatos/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}

} ]);