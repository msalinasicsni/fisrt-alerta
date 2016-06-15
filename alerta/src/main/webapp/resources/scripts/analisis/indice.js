var ViewReport = function () {
	
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
        	var table1 = null;
        	var title ="";
        	var responsiveHelper_data_result = undefined;
			
			var breakpointDefinition = {
				tablet : 1024,
				phone : 480
			};
			
            $('#parameters_form').validate({
    			// Rules for form validation
    				rules : {
    					codPato : {
    						required : true
    					},
    					codArea : {
    						required : true
    					},
    					semI : {
    						required : true
    					},
    					cantAnio : {
    						required : true
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
                        codZona: {
                            required : true
                        }
    				},
    				// Do not change code below
    				errorPlacement : function(error, element) {
    					error.insertAfter(element.parent());
    				},
    				submitHandler: function (form) {
                        //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                        getData();
                    }
            });

            $('#codArea').change(
            		function() {
            			if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
            				$('#silais').hide();
            				$('#departamento').hide();
            				$('#municipio').hide();
            				$('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').hide();
            				$('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
            				$('#silais').hide();
            				$('#departamento').show();
            				$('#municipio').hide();
            				$('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').show();
            				$('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').show();
            				$('#unidad').show();
                            $('#dSubUnits').show();
                            $('#zona').hide();
            			}
                        else if ($('#codArea option:selected').val() == "AREAREP|ZE"){
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
            		
            		columnas = [];
            		aoColumns = [];
            		anios=[];
            		labels=[],menor=[],dentro=[],indice=[];
            		var cant=parseInt($('#cantAnio option:selected').val());
            		var y = $('#anioI option:selected').val();
            		
            		for (var i=cant+1;i>0;i--){
            			anios.push(y-i+1);
            		}
            		columnas.push({"title": "Semanas" , "targets": 0});
            		aoColumns.push({sClass: "aw-right" });
            		for (var i=0;i<anios.length;i++){
            			columnas.push({"title": "C"+anios[i].toString() , "targets": i+1});
            			aoColumns.push({sClass: "aw-right" });
            		}
            		columnas.push({"title": "Mediana" , "targets": (anios.length)+1});
            		aoColumns.push({sClass: "aw-right" });
            		columnas.push({"title": "Indice" , "targets": (anios.length)+2});
            		aoColumns.push({sClass: "aw-right" });
            		title = 'Indice Epidémico Semanal ' + y;
            		if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
    					title = title + '</br>'+ $('#codPato option:selected').text() +'. República de Nicaragua';
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
    					title = title + '</br>'+ $('#codPato option:selected').text() +'. ' +$('#codSilaisAtencion option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
    					title = title + '</br>' + $('#codPato option:selected').text() +'. ' + 'Departamento de '+$('#codDepartamento option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
    					title = title + '</br>' + $('#codPato option:selected').text() +'. ' + 'Municipio: '+$('#codMunicipio option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
                        var ckeckd = $('#ckUS').is(':checked');
                        title = title + '</br>' + $('#codPato option:selected').text() +'. ' + (ckeckd?'Area de Salud':'Unidad de Salud') + ": "+$('#codUnidadAtencion option:selected').text();
    				}
                    else if ($('#codArea option:selected').val() == "AREAREP|ZE"){
                        title = title + '</br>' + $('#codPato option:selected').text() +'. ' + 'Zona Especial: '+$('#codZona option:selected').text();
                    }
            		title = title + '</br>Histórico de '+$('#cantAnio option:selected').text() +' años';
            		
            		/* TABLETOOLS */
            		if (!(table1 == null)) {
            			table1.fnClearTable();
            			table1.fnDestroy();
            			table1=null;
            			$('#data_result').html('');
            		}
            		table1 = $('#data_result').dataTable({
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
        				"autoWidth" : true,
        				"bDestroy":true,
        				"aoColumns" : aoColumns,
        		        "columnDefs": columnas,
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
            		for (var row in data) {
            			table1.fnAddData(data[row]);
            			for (var row2 in data[row]) {
            				labels.push(data[row][row2][0]);
            				indice.push(data[row][row2][data[row][row2].length-1]);
            				menor.push(0.75);
            				dentro.push(1.25);
            			}
            		}
            		lineChart(labels,dentro,menor,indice);
            		setTimeout($.unblockUI, 500);
    			})
    			.fail(function(XMLHttpRequest, textStatus, errorThrown) {
    				alert(" status: " + textStatus + " error:" + errorThrown);
				    setTimeout($.unblockUI, 5);
				});
            }
            
            function lineChart(labels,dentro,menor,indice){
            	// LINE CHART
				// ref: http://www.chartjs.org/docs/#line-chart-introduction
            	$('#lineChart-title').html("<h5>"+title+"</h5>");
			    var lineOptions = {
				    ///Boolean - Whether grid lines are shown across the chart
				    scaleShowGridLines : true,
				    //String - Colour of the grid lines
				    scaleGridLineColor : "rgba(0,0,0,.05)",
				    //Number - Width of the grid lines
				    scaleGridLineWidth : 1,
				    //Boolean - Whether the line is curved between points
				    bezierCurve : false,
				    //Number - Tension of the bezier curve between points
				    bezierCurveTension : 0.1,
				    //Boolean - Whether to show a dot for each point
				    pointDot : true,
				    //Number - Radius of each point dot in pixels
				    pointDotRadius : 2,
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

			    var lineData = { labels: labels,
			        datasets: [
				        {
				            label: "Dentro de lo esperado",
				            fillColor: "rgba(145,112,241,0.8)",
				            strokeColor: "rgba(145,112,241,1)",
				            pointColor: "rgba(145,112,241,1)",
				            pointStrokeColor: "#fff",
				            pointHighlightFill: "#fff",
				            pointHighlightStroke: "rgba(145,112,241,1)",
				            data: dentro
				        },
				        {
				            label: "Menor a lo esperado",
				            fillColor: "rgba(8,138,75,0.8)",
				            strokeColor: "rgba(8,138,75,1)",
				            pointColor: "rgba(8,138,75,1)",
				            pointStrokeColor: "#fff",
				            pointHighlightFill: "#fff",
				            pointHighlightStroke: "rgba(8,138,75,1)",
				            data: menor
				        },
				        {
				            label: "Indice epidémico",
				            fillColor: "rgba(0,0,0,0.0)",
				            strokeColor: "rgba(0,0,0,1)",
				            pointColor: "rgba(0,0,0,1)",
				            pointStrokeColor: "#000",
				            pointHighlightFill: "#000",
				            pointHighlightStroke: "rgba(0,0,0,1)",
				            data: indice
				        }
				    ]
			    };
			    
			    // render chart
			    if( window.myLine!==undefined)
			    	window.myLine.destroy();
			    var ctx = document.getElementById("lineChart").getContext("2d");


			    $('#lineChart').css('background-color', 'rgba(212, 77, 77, 0.5)');
			    window.myLine = new Chart(ctx).Line(lineData, lineOptions);
			    legend(document.getElementById("lineLegend"), lineData);
			    // END LINE CHART
            }
       
            
        }
    };

}();