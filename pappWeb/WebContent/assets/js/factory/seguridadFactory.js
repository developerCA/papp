app.factory("seguridadFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/seguridad");

	return {
		
		
		traerMenu : function(pagina) {
			  
			  return Restangular.allUrl("seguridad/consultar/menu/pagina="+pagina).getList();
		},
	
	}
} ]);