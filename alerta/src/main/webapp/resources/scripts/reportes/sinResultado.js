var ViewReportNResult = function () {

	var bloquearUI = function(mensaje){
		var loc = window.location;
	    var pathName = loc.pathname.substring(0,loc.pathname.indexOf('/', 1)+1);
	    var mess = '<img src=' + pathName + 'resources/img/ajax-loading.gif>' + mensaje;
	    $.blockUI({ message: mess,
	    	css: {
	            border: 'none',
	            padding: '15px',
	            backgroundColor: '#000',
	            '-webkit-border-radius': '10px',
	            '-moz-border-radius': '10px',
	            opacity: .5,
	            color: '#fff'
	            	}
	    });
	};

    return {
        //main function to initiate the module
        init: function (parametros) {
			var responsiveHelper_data_result = undefined;
			var breakpointDefinition = {
				tablet : 1024,
				phone : 480
			};
			var title = "";

            var fecha = new Date();
            var fechaFormateada = (fecha.getDate()<10?'0'+fecha.getDate():fecha.getDate())
                                  +''+(fecha.getMonth()+1<10?'0'+fecha.getMonth()+1:fecha.getMonth()+1)
                                  +''+fecha.getFullYear();
			/* TABLETOOLS */
			var table1 = $('#notices_result').dataTable({

				// Tabletools options:
				"sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-6 hidden-xs'T>r>"+
						"t"+
						"<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-sm-6 col-xs-12'p>>",
		        "oTableTools": {
		        	"aButtons": [
             	                {
             	                    "sExtends":    "collection",
             	                    "sButtonText": "Exportar",
             	                    "aButtons": [
             	                                 {
             	                                     "sExtends": "csv",
             	                                     "sFileName": fechaFormateada+"-NotificacionesSinResultado.csv",
             	                                     "sTitle": "Notificaciones sin resultado",
             	                                     "oSelectorOpts": { filter: 'applied', order: 'current' }
             	                                 },
             	                                 {
             	                                     "sExtends": "pdf",
             	                                     "sFileName": fechaFormateada+"-NotificacionesSinResultado.pdf",
                                                     "sTitle": "Notificaciones Sin Resultado",
             	                                     "oSelectorOpts": { filter: 'applied', order: 'current' },
             	                                     "sPdfOrientation": "landscape"
             	                                 }
             	                                 ]
             	                }
             	            ],
		            "sSwfPath": parametros.dataTablesTTSWF
		        },
				"autoWidth" : true,
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_data_result) {
						responsiveHelper_data_result = new ResponsiveDatatablesHelper($('#notices_result'), breakpointDefinition);
					}
				},
				"rowCallback" : function(nRow) {
					responsiveHelper_data_result.createExpandIcon(nRow);
				},
				"drawCallback" : function(oSettings) {
					responsiveHelper_data_result.respond();
				}
			});


			/* END TABLETOOLS */

             $('#parameters_form').validate({
    			// Rules for form validation
    				rules : {
    					codArea : {
    						required : true
    					},
    					fechaInicial : {
    						required : true
    					},
    					fechaFinal : {
    						required : true
    					},
    					codDepartamento : {
    						required : true
    					},
    					codMunicipio : {
    						required : true
    					},
    					codUnidadAtencion : {
    						required : true
    					},
    					codSilaisAtencion: {
    						required : true
                        },
                        tipoNotificacion: {
                            required : true
                        },
                        codZona: {
                            required : true
                        }
    				},
    				// Do not change code below
    				errorPlacement : function(error, element) {
    					error.insertAfter(element.parent());
    				},
    				submitHandler: function (form) {
                        //table1.fnClearTable();
                        //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                        getData();
                    }
            });

            $('#codArea').change(
            		function() {
                        var opcion = $('#codArea').find('option:selected').val();
            			if (opcion == "AREAREP|PAIS"){
            				$('#silais').hide();
            				$('#departamento').hide();
            				$('#municipio').hide();
            				$('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').hide();
                        }
            			else if (opcion == "AREAREP|SILAIS"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').hide();
            				$('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').hide();
                        }
            			else if (opcion == "AREAREP|DEPTO"){
            				$('#silais').hide();
            				$('#departamento').show();
            				$('#municipio').hide();
            				$('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').hide();
                        }
            			else if (opcion == "AREAREP|MUNI"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').show();
            				$('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').hide();
                        }
            			else if (opcion == "AREAREP|UNI"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').show();
            				$('#unidad').show();
                            $('#dSubUnits').show();
                            $('#zona').hide();
                        }
                        else if (opcion == "AREAREP|ZE"){
                            $('#silais').hide();
                            $('#departamento').hide();
                            $('#municipio').hide();
                            $('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').show();
                            $("#codZona").val("").change();
                        }
                    });

            function getData() {
            	bloquearUI(parametros.blockMess);
                table1.fnClearTable();
                $.getJSON(parametros.sActionUrl, $('#parameters_form').serialize(), function(data) {
            		var encontrado = false;
            		for(var row in data){
                        var actionUrl;
                        switch (data[row].codtipoNoti) {
                            case 'TPNOTI|SINFEB':
                                actionUrl = parametros.febrilesUrl+data[row].idNotificacion;
                                break;
                            case 'TPNOTI|IRAG':
                                actionUrl = parametros.iragUrl+data[row].idNotificacion;
                                break;
                            case 'TPNOTI|PCNT':
                                actionUrl = parametros.pacienteUrl+data[row].idNotificacion;
                                break;
                            default:
                                actionUrl = '#';
                                break;
                        }
                            table1.fnAddData([data[row].persona, data[row].edad, data[row].sexo, data[row].embarazada, data[row].municipio, data[row].tipoNoti, data[row].fechaRegistro, data[row].fechaInicioSintomas,data[row].SILAIS,data[row].unidad,'<a target="_blank" title="Ver" href=' + actionUrl + ' class="btn btn-primary btn-xs"><i class="fa fa-mail-forward"></i></a>']);
                            encontrado = true;
                    }
                    if(!encontrado){
	            		showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
	            		title='';
            		}
            		setTimeout($.unblockUI, 500);
    			})
    			.fail(function(XMLHttpRequest, textStatus, errorThrown) {
    				alert(" status: " + textStatus + " er:" + errorThrown);
				    setTimeout($.unblockUI, 5);
				});
            }

	    	function showMessage(title,content,color,icon,timeout){
	    		$.smallBox({
				    title: title,
				    content: content,
				    color: color,
				    iconSmall: icon,
				    timeout: timeout
				    });
	    	}
        }
    };

}();