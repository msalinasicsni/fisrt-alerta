var ViewReportWeek = function () {

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
            var fecha = new Date();
            var fechaFormateada = (fecha.getDate()<10?'0'+fecha.getDate():fecha.getDate())
                +''+(fecha.getMonth()+1<10?'0'+fecha.getMonth()+1:fecha.getMonth()+1)
                +''+fecha.getFullYear();
			var table1 = $('#data_result').dataTable({

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
             	                                     "sFileName": fechaFormateada+"-ReportePorSemana.csv",
             	                                     "sTitle": "ddd",
             	                                     "oSelectorOpts": { filter: 'applied', order: 'current' }
             	                                 },
             	                                 {
             	                                     "sExtends": "pdf",
             	                                     "sFileName": fechaFormateada+"-ReportePorSemana.pdf",
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

            // Se agrega método de validación 'greaterOrEqualThan', para validar que la semana final sea mayor o igual a la semana inicial
            jQuery.validator.addMethod("greaterOrEqualThan",
                function (value, element, param) {
                    var $min = $(param[0]);
                    if (this.settings.onfocusout) {
                        $min.off(".validate-greaterOrEqualThan").on("blur.validate-greaterOrEqualThan", function () {
                            $(element).valid();
                        });
                    }
                    return parseInt(value) >= parseInt($min.val());
                }, function (param){
                    var msg = parametros.msg_greaterOrEqualThan.replace(/\{0\}/,param[1]).replace(/\{1\}/,param[2]);
                    return msg;
                });
            // Se agrega método de validación 'lessOrEqualThan', para validar que la semana inicial sea menor o igual a la semana final
            /*jQuery.validator.addMethod("lessOrEqualThan",
                function (value, element, param) {
                    var $min = $(param[0]);
                    if (this.settings.onfocusout) {
                        $min.off(".validate-lessOrEqualThan").on("blur.validate-lessOrEqualThan", function () {
                            $(element).valid();
                        });
                    }
                    return parseInt(value) <= parseInt($min.val());
                }, function (param){
                    var msg = parametros.msg_lessOrEqualThan.replace(/\{0\}/,param[1]).replace(/\{1\}/,param[2]);
                    return msg;
                });*/

            $('#parameters_form').validate({
    			// Rules for form validation
    				rules : {
                        factor : {
    						required : true
    					},
    					codArea : {
    						required : true
    					},
    					semI : {
    						required : true
                            //lessOrEqualThan: ['#semF',parametros.semana1,parametros.semana2]
    					},
    					semF : {
    						required : true,
                            greaterOrEqualThan: ['#semI',parametros.semana2,parametros.semana1]
    					},
    					anioI : {
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
                        table1.fnClearTable();
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
                    else if ($('#codArea option:selected').val() == "AREAREP|UNI") {
                        //    title = title + '</br>'+parametros.unidad+' '+$('#codUnidadAtencion option:selected').text();

                        var ckeckd = $('#ckUS').is(':checked');

                        if (ckeckd) {
                            title = title + '</br>' + $('#areaL').val() + " " + $('#codUnidadAtencion option:selected').text();
                        } else {
                            title = title + '</br>' + parametros.unidad + " " + $('#codUnidadAtencion option:selected').text();

                        }

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|ZE"){
                        title = title + '</br>'+parametros.zona+' '+$('#codZona option:selected').text();
                    }
                    title = title + '</br>'+parametros.semana+' '+$('#semI option:selected').text() +' a la '+$('#semF option:selected').text() +', '+$('#anioI option:selected').text();

                    datasets = [];
                    datosC = [];
                    datasetsTasa = [];
                    datosTasa = [];
                    labels = [];
                    for (var row in data) {
                        table1.fnAddData([data[row][0],data[row][1], data[row][2]]);
                        encontrado = true;
        				datosTasa.push(data[row][2]);
                        datosC.push(data[row][1]);
                        labels.push('S'+data[row][0]);
                    }
                    var colorS = colors[0];
                    var colorT = colors[1];
                    var label = $('#anioI').find('option:selected').val();
                    datasets.push({
                        label: label,
                        fillColor: convertHex(colorS,20),
                        strokeColor: convertHex(colorS,80),
                        pointColor: convertHex(colorS,100),
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: convertHex(colorS,100),
                        data: datosC
                    });
                    datasetsTasa.push({
                        label: label,
                        fillColor: convertHex(colorT,20),
                        strokeColor: convertHex(colorT,800),
                        pointColor: convertHex(colorT,100),
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: convertHex(colorT,100),
                        data: datosTasa
                    });
                    lineChart(datasets,labels);
                    lineChart2(datasetsTasa,labels);
            		if(!encontrado){
	            		showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
	            		title='';
	            		$('#lineChart-title').html("<h5>"+title+"</h5>");
	            		$('#lineChart2-title').html("<h5>"+title+"</h5>");
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
            function lineChart(datasets,labels) {
                // LINE CHART
                // ref: http://www.chartjs.org/docs/#line-chart-introduction
                $('#lineChart-title').html("<h5>"+title+"</h5>");
                var lineData = { labels: labels,
                    datasets: datasets
                };
                // render chart
                if( window.myLine!==undefined)
                    window.myLine.destroy();
                var ctx = document.getElementById("lineChart").getContext("2d");
                window.myLine = new Chart(ctx).Line(lineData, lineOptions);
                legend(document.getElementById("lineLegend"), lineData);
                // END LINE CHART
            }

            function lineChart2(datasets2,labels2) {
                // LINE CHART
                // ref: http://www.chartjs.org/docs/#line-chart-introduction
                $('#lineChart2-title').html("<h5>"+title+"</h5>");
                var lineData2 = { labels: labels2,
                    datasets: datasets2
                };
                // render chart
                if( window.myLine2!==undefined)
                    window.myLine2.destroy();
                var ctx = document.getElementById("lineChart2").getContext("2d");
                window.myLine2 = new Chart(ctx).Line(lineData2, lineOptions);
                legend(document.getElementById("lineLegend2"), lineData2);
                // END LINE CHART
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