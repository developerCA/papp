'use strict';
/**
 * controller for angular-menu
 * 
 */
app
		.controller(
				'ServiciosCtrl',
				[
						"$scope",
						"$filter",
						"ngTableParams",
						"SweetAlert",
						"serviciosFactory",
						function($scope, $filter, ngTableParams, SweetAlert,
								serviciosFactory) {

							$scope.data = [];
							$scope.servicio={};
							$scope.edit = true;
						
							$scope.traerServicios = function() {
								serviciosFactory.listaServicios().then(
										function(resp) {

											$scope.data = resp;

										})
							};

							$scope.nuevo = function() {

								serviciosFactory.nuevo().then(function(resp) {

									$scope.edit = false;
									$scope.servicio = resp.objeto;
								
								})

							};


							$scope
									.$watch(
											'data',
											function() {

												$scope.tableParams = new ngTableParams(
														{
															page : 1, // show
																		// first
																		// page
															count : 5, // count
																		// per
																		// page
															filter : {
																nombre : ''
															}
														},
														{
															total : $scope.data.length, // length
																						// of
																						// data
															getData : function(
																	$defer,
																	params) {
																// use build-in
																// angular
																// filter
																var orderedData = params
																		.filter() ? $filter(
																		'filter')
																		(
																				$scope.data,
																				params
																						.filter())
																		: $scope.data;
																$scope.servicios = orderedData
																		.slice(
																				(params
																						.page() - 1)
																						* params
																								.count(),
																				params
																						.page()
																						* params
																								.count());
																params
																		.total(orderedData.length);
																// set total for
																// recalc
																// pagination
																$defer
																		.resolve($scope.servicios);
															}
														});
											});

							$scope.form = {

								submit : function(form) {

									var firstError = null;
									if (form.$invalid) {

										var field = null, firstError = null;
										for (field in form) {
											if (field[0] != '$') {
												if (firstError === null
														&& !form[field].$valid) {
													firstError = form[field].$name;
												}

												if (form[field].$pristine) {
													form[field].$dirty = true;
												}
											}
										}

										angular.element(
												'.ng-invalid[name='
														+ firstError + ']')
												.focus();
										
										return;

									} else {
										$scope.registrar(form);

									}

								},
								resetear : function(form) {

									$scope.servicio = {};
									form.$setPristine(true);
									$scope.edit = true;
									

								}
							};

							$scope.registrar = function(form) {

								serviciosFactory
										.registrar($scope.servicio)
										.then(
												function(r) {

													if (r.estado) {
														SweetAlert
																.swal(
																		"Listo!",
																		"La informacion se registro satisfactoriamente!",
																		"success");
														$scope.traerServicios();
														$scope.servicio = {};
														form.$setPristine(true);
														$scope.edit = true;
												} else {
														SweetAlert.swal(
																"Error!",
																r.mensaje,
																"error");

													}

												})

							};

							$scope.eliminarServicio = function(
									codigoServicio) {
								SweetAlert
										.swal(
												{
													title : "Esta seguro de eliminar el servicio ?",
													text : "Es posible que la información que va a eliminar no se visualice en otros procesos del sistema!",
													type : "warning",
													showCancelButton : true,
													confirmButtonColor : "#DD6B55",
													confirmButtonText : "Si,estoy seguro",
													cancelButtonText : "No",
													closeOnConfirm : false,
													closeOnCancel : false
												},
												function(isConfirm) {
													if (isConfirm) {

														serviciosFactory
																.eliminar(
																		codigoServicio)
																.then(
																		function(
																				r) {
																			$scope
																					.traerServicios();

																			SweetAlert
																					.swal({
																						title : "Eliminado!",
																						text : "El registro se eliminó; satisfactoriamente",
																						type : "success",
																						confirmButtonColor : "#007AFF"
																					});
																		});

													} else {

														SweetAlert.swal({
															title : "",
															timer : 1
														});
													}
												});

							};

							$scope.editarServicio = function(
									codigoServicio) {
								$scope.buscarServicio(codigoServicio);
								
							};

							$scope.buscarServicio = function(
									codigoServicio) {

								serviciosFactory
										.buscar(codigoServicio)
										.then(
												function(resp) {

													$scope.servicio = resp.objeto;
													$scope.edit=false;
												});
							};

							
							
							
						} ]);