'use strict';
/**
 * controller for angular-menu
 * 
 */
app
		.controller(
				'EmpresaCtrl',
				[
						"$scope",
						"$filter",
						"ngTableParams",
						"SweetAlert",
						"empresaFactory",
						function($scope, $filter, ngTableParams, SweetAlert,
								empresaFactory) {

							$scope.data = [];
							$scope.empresa = {};
							$scope.edit = true;
							
							$scope.myImage = '';
						    $scope.myCroppedImage = '';
						    $scope.cropType = "square";
						    
						    var handleFileSelect = function (evt) {
						      
						    	var file = evt.currentTarget.files[0];
						        var reader = new FileReader();
						        reader.onload = function (evt) {
						            $scope.$apply(function ($scope) {
						                $scope.myImage = evt.target.result;
						            });
						        };
						        reader.readAsDataURL(file);
						    };
						    
						    angular.element(document.querySelector('#fileInput')).on('change', handleFileSelect);
						    
						    

							$scope.traerEmpresas = function() {
								empresaFactory.lista().then(function(resp) {

									$scope.data = resp;

								})
							};

							$scope.nuevo = function() {

								empresaFactory.nuevo().then(function(resp) {
									
									$scope.edit = false;
									$scope.empresa = resp.objeto;
									$scope.myCroppedImage=null;
									$scope.myImage = null;
									console.log($scope.empresa);
								})

							};

							$scope
									.$watch(
											'data',
											function() {

												$scope.tableParams = new ngTableParams(
														{
															page : 1,
															count : 5,
															filter : {
																nombre : '',
																ruc : ''
															}
														},
														{
															total : $scope.data.length, // length
															getData : function(
																	$defer,
																	params) {

																var orderedData = params
																		.filter() ? $filter(
																		'filter')
																		(
																				$scope.data,
																				params
																						.filter())
																		: $scope.data;
																$scope.empresas = orderedData
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

																$defer
																		.resolve($scope.empresas);
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

									$scope.empresa = {};
									form.$setPristine(true);
									$scope.edit = true;
									$scope.myCroppedImage=null;

								}
							};

							$scope.registrar = function(form) {

								$scope.empresa.logo_data=$scope.myCroppedImage;
							
								
								
								empresaFactory
										.registrar($scope.empresa)
										.then(
												function(r) {

													if (r.estado) {
														SweetAlert
																.swal(
																		"Listo!",
																		"La informacion se registro satisfactoriamente!",
																		"success");
														$scope.traerEmpresas();
														$scope.empresa = {};
														$scope.myCroppedImage=null;
														$scope.myImage = null;
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

							$scope.eliminar = function(codigoEmpresa) {
								SweetAlert
										.swal(
												{
													title : "Esta seguro de eliminar la empresa ?",
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

														empresaFactory
																.eliminar(
																		codigoEmpresa)
																.then(
																		function(
																				r) {
																			$scope
																					.traerEmpresas();

																			SweetAlert
																					.swal({
																						title : "Eliminado!",
																						text : "El registro se eliminó satisfactoriamente",
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

							$scope.editar = function(codigoEmpresa) {
								$scope.buscarEmpresa(codigoEmpresa);

							};

							$scope.buscarEmpresa = function(codigoEmpresa) {

								empresaFactory.buscar(codigoEmpresa).then(
										function(resp) {

											$scope.empresa = resp.objeto;
											console.log($scope.empresa);
											$scope.edit = false;
										});
							};

						} ]);