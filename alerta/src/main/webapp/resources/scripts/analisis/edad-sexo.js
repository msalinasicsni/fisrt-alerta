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
			var responsiveHelper_data_result = undefined;
			var responsiveHelper_data_result_2 = undefined;
			var breakpointDefinition = {
				tablet : 1024,
				phone : 480
			};
			var title = "";

            var colors = ["#0066FF","#FF0000","#009900","#FF6600","#FF3399","#008B8B","#663399","#FFD700","#0000FF","#DC143C","#32CD32","#FF8C00","#C71585","#20B2AA","#6A5ACD","#9ACD32"];
			
			/* TABLETOOLS */
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
		        "aoColumns" : [null,
		                       {sClass: "aw-center"},{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" }
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
			
			var table2 = $('#data_result_2').dataTable({
				
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
		        "aoColumns" : [null,
		                       {sClass: "aw-center"},{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" }
		                   ],
				"autoWidth" : true,
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_data_result_2) {
						responsiveHelper_data_result_2 = new ResponsiveDatatablesHelper($('#data_result_2'), breakpointDefinition);
					}
				},
				"rowCallback" : function(nRow) {
					responsiveHelper_data_result_2.createExpandIcon(nRow);
				},
				"drawCallback" : function(oSettings) {
					responsiveHelper_data_result_2.respond();
				}
			});
			
			/* END TABLETOOLS */
		
		

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
                        table1.fnClearTable();
                        table2.fnClearTable();
                        pieChart();
                        pieChart2();
                        lineChart();
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
            		values1 = [];
            		values2 = [];
            		series1 = [];
            		series2 = [];
            		porcM1 = [];
            		porcF1 = [];
            		porcM2 = [];
            		porcF2 = [];
            		title = $('#codPato option:selected').text();
            		if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
    					title = title + '</br>Rep�blica de Nicaragua';
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
            		if (data == '' || data == null){
            			showMessage("Sin datos", "No se encontraron datos como resultado de esta consulta", "#AF801C", "fa fa-warning", 3000);
            			title='';
            			$('#pieChart-title').html("<h5>"+title+"</h5>");
            			$('#pieChart2-title').html("<h5>"+title+"</h5>");
            			$('#lineChart-title').html("<h5>"+title+"</h5>");
            		}
            		else {
	            		for (var row in data) {
	    					table1.fnAddData(
	    							[data[row][0], data[row][1], data[row][2], data[row][3], data[row][4], data[row][5], data[row][6], data[row][7],
	    							 data[row][8], data[row][9], data[row][10], data[row][11], data[row][12], data[row][13], data[row][14],
	    							 data[row][15], data[row][16], data[row][17], data[row][18], data[row][19], data[row][20], data[row][21],
	    							 data[row][22], data[row][23], data[row][24], data[row][25], data[row][26], data[row][27], data[row][28],data[row][29], data[row][30]]);
	    					table2.fnAddData(
	    							[data[row][0], 
	    							 Math.round(data[row][1]/(data[row][29] + data[row][30]) * 100 * 100) / 100, 
	    							 Math.round(data[row][2]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][3]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][4]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][5]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][6]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][7]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][8]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][9]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][10]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][11]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][12]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][13]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][14]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][15]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][16]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][17]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][18]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][19]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][20]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][21]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][22]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][23]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][24]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][25]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][26]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][27]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][28]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][29]/(data[row][29] + data[row][30]) * 100 * 100) / 100,
	    							 Math.round(data[row][30]/(data[row][29] + data[row][30]) * 100 * 100) / 100]);

	    					if(data[row][0]==$('#anioI option:selected').text()){
	    						values1 = [data[row][1]+ data[row][2], data[row][3]+ data[row][4], data[row][5]+ data[row][6], data[row][7]+data[row][8], data[row][9]+ data[row][10], data[row][11]+ data[row][12], data[row][13]+ data[row][14],data[row][15]+ data[row][16], data[row][17]+ data[row][18], data[row][19]+ data[row][20], data[row][21]+data[row][22], data[row][23]+ data[row][24], data[row][25]+ data[row][26], data[row][27]+ data[row][28]];
	    						series1 = data[row][0];
	    						porcM1 = Math.round(data[row][29] / (data[row][29] + data[row][30]) * 100 * 100) / 100;
	    						porcF1 = Math.round(data[row][30] / (data[row][29] + data[row][30]) * 100 * 100) / 100;
	    					}
	    					if(data[row][0]==$('#anioF option:selected').text()){
	    						values2 = [data[row][1]+ data[row][2], data[row][3]+ data[row][4], data[row][5]+ data[row][6], data[row][7]+data[row][8], data[row][9]+ data[row][10], data[row][11]+ data[row][12], data[row][13]+ data[row][14],data[row][15]+ data[row][16], data[row][17]+ data[row][18], data[row][19]+ data[row][20], data[row][21]+data[row][22], data[row][23]+ data[row][24], data[row][25]+ data[row][26], data[row][27]+ data[row][28]];
	    						series2 = data[row][0];
	    						porcM2 = Math.round(data[row][29] / (data[row][29] + data[row][30]) * 100 * 100) / 100;
	    						porcF2 = Math.round(data[row][30] / (data[row][29] + data[row][30]) * 100 * 100) / 100;
	    					}
	            		}
	            		pieChart(porcM1,porcF1,series1);
	            		pieChart2(porcM2,porcF2,series2);
	            		lineChart(values1,values2,series1,series2);
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
            
            function pieChart(porcM1,porcF1,series1) {
            	// PIE CHART
            	$('#pieChart-title').html("<h5>"+title+"</h5>");
			    var pieOptions = {
			    	//Boolean - Whether we should show a stroke on each segment
			        segmentShowStroke: true,
			        //String - The colour of each segment stroke
			        segmentStrokeColor: "#fff",
			        //Number - The width of each segment stroke
			        segmentStrokeWidth: 2,
			        //Number - Amount of animation steps
			        animationSteps: 100,
			        //String - types of animation
			        animationEasing: "easeOutBounce",
			        //Boolean - Whether we animate the rotation of the Doughnut
			        animateRotate: true,
			        //Boolean - Whether we animate scaling the Doughnut from the centre
			        animateScale: false,
			        //Boolean - Re-draw chart on page resize
			        responsive: true
			    };

			    pieData = [
				    {
				        value: porcM1,
                        color: convertHex(colors[0],100),// "rgba(151,187,205,1)",
                        highlight: convertHex(colors[0],70), //"rgba(151,187,205,0.8)",
				        label: "Masculino - " + series1
				    },
				    {
				        value: porcF1,
                        color: convertHex(colors[1],100),// "rgba(169, 3, 41, 0.7)",
                        highlight: convertHex(colors[1],70),// "rgba(169, 3, 41, 0.7)",
				        label: "Femenino - " + series1
				    }
			    ];

			    // render chart
			    if( window.myPie!==undefined)
			    	window.myPie.destroy();
			    var ctx = document.getElementById("pieChart").getContext("2d");
			    window.myPie = new Chart(ctx).Pie(pieData, pieOptions);
			    
			    // END PIE CHART

                legend(document.getElementById("pieLegend"), pieData);
            }
            
            function pieChart2(porcM2,porcF2,series2) {
            	// PIE CHART
            	$('#pieChart2-title').html("<h5>"+title+"</h5>");
			    var pieOptions = {
			    	//Boolean - Whether we should show a stroke on each segment
			        segmentShowStroke: true,
			        //String - The colour of each segment stroke
			        segmentStrokeColor: "#fff",
			        //Number - The width of each segment stroke
			        segmentStrokeWidth: 2,
			        //Number - Amount of animation steps
			        animationSteps: 100,
			        //String - types of animation
			        animationEasing: "easeOutBounce",
			        //Boolean - Whether we animate the rotation of the Doughnut
			        animateRotate: true,
			        //Boolean - Whether we animate scaling the Doughnut from the centre
			        animateScale: false,
			        //Boolean - Re-draw chart on page resize
			        responsive: true
			    };

			    pieData = [
				    {
				        value: porcM2,
                        color: convertHex(colors[0],100),// "rgba(151,187,205,1)",
                        highlight: convertHex(colors[0],70), //"rgba(151,187,205,0.8)",
				        label: "Masculino - " + series2
				    },
				    {
				        value: porcF2,
                        color: convertHex(colors[1],100),// "rgba(169, 3, 41, 0.7)",
                        highlight: convertHex(colors[1],70),// "rgba(169, 3, 41, 0.7)",
				        label: "Femenino - " + series2
				    }
			    ];

			    // render chart
			    if( window.myPie2!==undefined)
			    	window.myPie2.destroy();
			    var ctx = document.getElementById("pieChart2").getContext("2d");
			    window.myPie2 = new Chart(ctx).Pie(pieData, pieOptions);
			    
			    // END PIE CHART

                legend(document.getElementById("pieLegend2"), pieData);
            }
            
            function lineChart(values1,values2,series1,series2) {
            	// BAR CHART
            	$('#lineChart-title').html("<h5>"+title+"</h5>");
			    var barOptions = {
				    //Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
				    scaleBeginAtZero : true,
				    //Boolean - Whether grid lines are shown across the chart
				    scaleShowGridLines : true,
				    //String - Colour of the grid lines
				    scaleGridLineColor : "rgba(0,0,0,.05)",
				    //Number - Width of the grid lines
				    scaleGridLineWidth : 1,
				    //Boolean - If there is a stroke on each bar
				    barShowStroke : true,
				    //Number - Pixel width of the bar stroke
				    barStrokeWidth : 1,
				    //Number - Spacing between each of the X value sets
				    barValueSpacing : 5,
				    //Number - Spacing between data sets within X values
				    barDatasetSpacing : 1,
				    //Boolean - Re-draw chart on page resize
			        responsive: true
			    };

			    var barData = {
			        labels: ["0-6 d�as", "7-27 d�as", "28d-11 meses", "1 a�o", "2-4 a�os", "5-9 a�os", "10-14 a�os","15-19 a�os","20-34 a�os","35-49 a�os","50-59 a�os","60-64 a�os","65 y + a�os","Desc"],
			         datasets: [
				        {
				            label: series1,
                            fillColor: convertHex(colors[0],70), //"rgba(220,220,220,0.5)",
                            strokeColor: convertHex(colors[0],95), //"rgba(220,220,220,0.8)",
                            highlightFill: convertHex(colors[0],90), //"rgba(220,220,220,0.75)",
                            highlightStroke: convertHex(colors[0],100), //"rgba(220,220,220,1)",
				            data: values1
				        },
				        {
				            label: series2,
                            fillColor: convertHex(colors[1],70), //"rgba(151,187,205,0.5)",
                            strokeColor: convertHex(colors[1],95), //"rgba(151,187,205,0.8)",
                            highlightFill: convertHex(colors[1],90), //"rgba(151,187,205,0.75)",
                            highlightStroke: convertHex(colors[1],100), //"rgba(151,187,205,1)",
				            data: values2
				        }
				    ]
			    };

			    // render chart
			    if( window.myBar!==undefined)
			    	window.myBar.destroy();
			    var ctx = document.getElementById("lineChart").getContext("2d");
			    window.myBar = new Chart(ctx).Bar(barData, barOptions);
			    // END BAR CHART

                legend(document.getElementById("lineLegend"), barData);
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