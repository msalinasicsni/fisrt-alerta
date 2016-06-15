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
			var title = "";
			var table1 = null;
			var responsiveHelper_data_result = undefined;
			
			var breakpointDefinition = {
				tablet : 1024,
				phone : 480
			};
			var backColors = ["#0066FF","#FF0000","#009900","#FF6600","#FF3399","#008B8B","#663399","#FFD700","#0000FF","#DC143C","#32CD32","#FF8C00","#C71585","#20B2AA","#6A5ACD","#9ACD32"];
			
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
    					codPato : {
    						required : true
    					},
    					codArea : {
    						required : true
    					},
    					semI : {
    						required : true
    					},
    					semF : {
    						required : true
    					},
    					anioI : {
    						required : true
    					},
    					anioF : {
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
            		var long = data.length;
            		var hay = (long < 2)?false:true;
            		if (hay){
	            		columnas = [{title:'Patología'},{title:'Año'}];
	            		for (var row in data[(data.length)-1]) {
	            			c = 'C'+data[(data.length)-1][row].toString();
	            			t = 'T'+data[(data.length)-1][row].toString();
	            			columnas.push({title:c});
	            			columnas.push({title:t}); 
	            		} 
	            		title = "Distribución por patologías";
	            		if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
	    					title = title + '</br>República de Nicaragua';
	    				}
	    				else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
	    					title = title + '</br>'+$('#codSilaisAtencion option:selected').text();
	    				}
	    				else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
	    					title = title + '</br>Departamento de '+$('#codDepartamento option:selected').text();
	    				}
	    				else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
	    					title = title + '</br>Municipio: '+$('#codMunicipio option:selected').text();
	    				}
	    				else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
                            var ckeckd = $('#ckUS').is(':checked');
                            title = title + '</br>' + (ckeckd?'Area de Salud':'Unidad de Salud') + ": " + $('#codUnidadAtencion option:selected').text();
	    				}
                        else if ($('#codArea option:selected').val() == "AREAREP|ZE"){
                            title = title + '</br>Zona Especial: '+$('#codZona option:selected').text();
                        }
	            		title = title + '</br>Semana '+$('#semI option:selected').text() +' a la '+$('#semF option:selected').text();
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
	        		        "columns":columnas,
	        				"autoWidth" : true,
	        				"bDestroy":true,
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
	        			var i = 0, j = 0;
	        			datasets = [];
	        			datasets2 = [];
	        			for (i = 0; i < (data.length-1) ;i++) {
	        				datos = [];
	        				datosC = [];
	        				datosT = [];
	        				label = "";
	        				for (j = 1 ; j < data[i].length; j++) {
	        					datos.push(data[i][j]);
	        					if (j == 1 || j == 2) label = label + data[i][j];
	        					if(j>1 && j%2) {
	        						datosC.push(data[i][j]);
	        						datosT.push(data[i][j+1]);
	        					}
	        				}
	        				table1.fnAddData(datos);
	        				var colorS = (i < 12)?backColors[i]:getRandomColor();
	        				datasets.push({
    				            label: label,
    				            fillColor: convertHex(colorS,0),
    				            strokeColor: convertHex(colorS,100),
    				            pointColor: convertHex(colorS,100),
    				            pointStrokeColor: "#fff",
    				            pointHighlightFill: "#fff",
    				            pointHighlightStroke: convertHex(colorS,100),
    				            data: datosC
    				        });
	        				datasets2.push({
    				            label: label,
    				            fillColor: convertHex(colorS,0),
    				            strokeColor: convertHex(colorS,100),
    				            pointColor: convertHex(colorS,100),
    				            pointStrokeColor: "#fff",
    				            pointHighlightFill: "#fff",
    				            pointHighlightStroke: convertHex(colorS,100),
    				            data: datosT
    				        });
	        			}
	        			lineChart(datasets,data[(data.length)-1]);
	        			lineChart2(datasets2,data[(data.length)-1]);
            		}
            		else{
            			showMessage("Sin datos", "No se encontraron datos como resultado de esta consulta", "#AF801C", "fa fa-warning", 3000);
	        			title = '';
                        lineChart([],[]);
	        			lineChart2([],[]);
                        /* TABLETOOLS */
                        if (!(table1 == null)) {
                            table1.fnClearTable();
                            table1.fnDestroy();
                            table1=null;
                            $('#data_result').html('');
                        }
            		}
            		setTimeout($.unblockUI, 100);
    			})
    			.fail(function(XMLHttpRequest, textStatus, errorThrown) {
    				alert(" status: " + textStatus + " error:" + errorThrown);
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
	    	
			function getRandomColor() {
			    var color = '';
			    color = '#'+Math.floor(Math.random()*16777215).toString(16);
			    return color;
			}

        }
    };

}();