var ViewReportDay = function () {

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

			/* TABLETOOLS */
/*			var table1 = $('#data_result').dataTable({

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
             	                                     "sFileName": "ddd"+"-*.csv",
             	                                     "sTitle": "ddd",
             	                                     "oSelectorOpts": { filter: 'applied', order: 'current' }
             	                                 },
             	                                 {
             	                                     "sExtends": "pdf",
             	                                     "sFileName": "DD"+"-*.pdf",
	            	                                     "sTitle": ":fff:",
	            	                                     "sPdfMessage": "FF",
             	                                     "oSelectorOpts": { filter: 'applied', order: 'current' },
             	                                     "sPdfOrientation": "landscape"
             	                                 }
             	                                 ]
             	                }
             	            ],
		            "sSwfPath": parametros.dataTablesTTSWF
		        },
		        "aoColumns" : [{sClass: "aw-center"},{sClass: "aw-right" },{sClass: "aw-right" }
		                   ],
				"autoWidth" : true,
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_data_result) {
						responsiveHelper_data_result = new ResponsiveDatatablesHelper($('#data_result'), breakpointDefinition);
					}
				},
				"rowCallback" : function(nRow) {
					responsiveHelper_data_result.createExpandIcon(nRow);
				},
				"drawCallback" : function(oSettings) {
					responsiveHelper_data_result.respond();
				}
			});
*/

			/* END TABLETOOLS */

            var colors = ["#0066FF","#FF0000","#009900","#FF6600","#FF3399","#008B8B","#663399","#FFD700","#0000FF","#DC143C","#32CD32","#FF8C00","#C71585","#20B2AA","#6A5ACD","#9ACD32"];
            var lineOptions = {
                ///Boolean - Whether grid lines are shown across the chart
                scaleShowGridLines : true,
                //String - Colour of the grid lines
                scaleGridLineColor : "rgba(0,0,0,0.04)",
                //Number - Width of the grid lines
                scaleGridLineWidth : 1,
                //Boolean - Whether the line is curved between points
                bezierCurve : false,
                //Number - Tension of the bezier curve between points
                bezierCurveTension : 0.4,
                //Boolean - Whether to show a dot for each point
                pointDot : true,
                //Number - Radius of each point dot in pixels
                pointDotRadius : 4,
                //Number - Pixel width of point dot stroke
                pointDotStrokeWidth : 1,
                //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
                pointHitDetectionRadius : 20,
                //Boolean - Whether to show a stroke for datasets
                datasetStroke : true,
                //Number - Pixel width of dataset stroke
                datasetStrokeWidth : 2,
                //Boolean - Whether to fill the dataset with a colour
                datasetFill : true,
                //Boolean - Re-draw chart on page resize
                responsive: true,
                //String - A legend template
                legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].lineColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"
            };

            $('#parameters_form').validate({
    			// Rules for form validation
    				rules : {
                        factor : {
    						required : true
    					},
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
                $.getJSON(parametros.sActionUrl, $('#parameters_form').serialize(), function(data) {
            		title = $('#tipoNotificacion option:selected').text();
            		var encontrado = false;
            		if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
    					title = title + '</br>'+parametros.nicaragua;
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
    					title = title + '</br>'+$('#codSilaisAtencion option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
    					title = title + '</br>'+parametros.departamento+' '+$('#codDepartamento option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
    					title = title + '</br>'+parametros.municipio+' '+$('#codMunicipio option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
    				//	title = title + '</br>'+parametros.unidad+' '+$('#codUnidadAtencion option:selected').text();

                        var ckeckd = $('#ckUS').is(':checked');

                        if (ckeckd) {
                            title = title + '</br>' + $('#areaL').val() + '</br>'+ $('#codUnidadAtencion option:selected').text();
                        } else {
                            title = title + '</br>' + parametros.unidad + '</br>'  + $('#codUnidadAtencion option:selected').text();

                        }

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|ZE"){
                        title = title + '</br>'+parametros.zona+' '+$('#codZona option:selected').text();
                    }
            		title = title + '</br>'+parametros.desde+' '+$('#fechaInicial').val() +' '+parametros.hasta+' '+$('#fechaFinal').val();

                    for(var row in data){
                        data[row][0] = new Date(data[row][0]);
                        encontrado = true;
                    }
                    g1 = new Dygraph(document.getElementById("noroll"),data,
                        {
                            rollPeriod : 1,
                            showRoller : true,
                            labels: [ "x", parametros.casos ],
                            customBars : false,
                            title : title,
                            ylabel : parametros.casos,
                            legend : 'always',
                            labelsDivStyles : {
                                'textAlign' : 'right'
                            },
                            showRangeSelector : true
                        });
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
            // Convert Hex color to RGB
            function convertHex(hex,opacity){
                hex = hex.replace('#','');
                r = parseInt(hex.substring(0,2), 16);
                g = parseInt(hex.substring(2,4), 16);
                b = parseInt(hex.substring(4,6), 16);

                // Add Opacity to RGB to obtain RGBA
                result = 'rgba('+r+','+g+','+b+','+opacity/100+')';
                return result;
            }
        }
    };

}();