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

            var colors = ["#0066FF","#FF0000","#009900","#FF6600","#FF3399","#008B8B","#663399","#FFD700","#0000FF","#DC143C","#32CD32","#FF8C00","#C71585","#20B2AA","#6A5ACD","#9ACD32"];

			var title = "";
			
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
		        "aoColumns" : [null, null,
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
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
		        "aoColumns" : [null, null, 
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
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
            		values1 = [], values2 = [];
            		values1P = [], values2P = [];
            		values1T = [], values2T = [];
            		series1 = $('#anioI option:selected').text(),series2 = $('#anioF option:selected').text();
            		labels1 = [], labels2 = [];
            		total1 = 0 , total2 =0;
            		pobMasc1 = 0, pobFem1 = 0, pobTotal1 = 0;
            		pobMasc2 = 0, pobFem2 = 0, pobTotal2 = 0;
            		factor = 0;
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
            		
            		for (var row in data) {
            			if(data[row][0]=='Pop' && data[row][1]==$('#anioI option:selected').text()){
            				pobMasc1=data[row][2],pobFem1=data[row][3],pobTotal1=data[row][4];
            			}
            			if(data[row][0]=='Pop' && data[row][1]==$('#anioF option:selected').text()){
            				pobMasc2=data[row][2],pobFem2=data[row][3],pobTotal2=data[row][4];
            			}
            		}
            		console.log(data);
            		for (var row in data) {
            			if(data[row][0]=='Pato'){
            				factor=data[row][1];
            				break;
            			}
            		}
            		
            		for (var row in data) {
        				if(data[row][0]==$('#anioI option:selected').text()){
        					total1=total1+data[row][29] + data[row][30];
        				}
        				if(data[row][0]==$('#anioF option:selected').text()){
        					total2=total2+data[row][29] + data[row][30];
        				}
        			}
            		
            		for (var row in data) {
        				if(data[row][0]==$('#anioI option:selected').text()){
        					table1.fnAddData([data[row][0],data[row][32],data[row][29], data[row][30], data[row][29] + data[row][30],
        					                 Math.round(data[row][29]/(total1) * 100 * 100) / 100,
        					                 Math.round(data[row][30]/(total1) * 100 * 100) / 100,
        					                 Math.round((data[row][29]+data[row][30])/(total1) * 100 * 100) / 100,
        					                 (pobMasc1 == 0) ? "NP":Math.round(data[row][29]/(pobMasc1) * factor * 100) / 100, 
        					                 (pobFem1 == 0) ? "NP":Math.round(data[row][30]/(pobFem1) * factor * 100) / 100, 
        					                 (pobTotal1 == 0) ? "NP":Math.round((data[row][29] + data[row][30])/(pobTotal1) * factor * 100) / 100]);
        					values1.push(data[row][29] + data[row][30]);
        					values1P.push(Math.round((data[row][29]+data[row][30])/(total1) * 100 * 100) / 100);
        					values1T.push((pobTotal1 == 0) ? 0:Math.round((data[row][29] + data[row][30])/(pobTotal1) * factor * 100) / 100);
        					labels1.push(data[row][32]);
        				}
        				if(data[row][0]==$('#anioF option:selected').text()){
        					table2.fnAddData([data[row][0],data[row][32],data[row][29], data[row][30], data[row][29] + data[row][30],
         					                 Math.round(data[row][29]/(total2) * 100 * 100) / 100,
         					                 Math.round(data[row][30]/(total2) * 100 * 100) / 100,
         					                 Math.round((data[row][29]+data[row][30])/(total2) * 100 * 100) / 100,
         					                (pobMasc2 == 0) ? "NP":Math.round(data[row][29]/(pobMasc2) * factor * 100) / 100, 
               					            (pobFem2 == 0) ? "NP":Math.round(data[row][30]/(pobFem2) * factor * 100) / 100, 
               					            (pobTotal2 == 0) ? "NP":Math.round((data[row][29] + data[row][30])/(pobTotal2) * factor * 100) / 100]);
        					values2.push(data[row][29] + data[row][30]);
        					values2P.push(Math.round((data[row][29]+data[row][30])/(total2) * 100 * 100) / 100);
        					values2T.push((pobTotal2 == 0) ? 0:Math.round((data[row][29] + data[row][30])/(pobTotal2) * factor * 100) / 100);
        					labels2.push(data[row][32]);
        				}
        			}
            		lineChart(values1,values2,series1,series2,labels1);
            		lineChartPorc(values1P,values2P,series1,series2,labels1);
            		lineChartTasa(values1T,values2T,series1,series2,labels1);
            		setTimeout($.unblockUI, 500);
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
	    	
	    	function lineChart(values1,values2,series1,series2,labels1) {
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
			        labels: labels1,
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
	    	
	    	function lineChartPorc(values1,values2,series1,series2,labels1) {
            	// BAR CHART
            	$('#lineChart2-title').html("<h5>"+title+"</h5>");
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
			        labels: labels1,
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
			    if( window.myBar2!==undefined)
			    	window.myBar2.destroy();
			    var ctx = document.getElementById("lineChart2").getContext("2d");
			    window.myBar2 = new Chart(ctx).Bar(barData, barOptions);
			    // END BAR CHART

                legend(document.getElementById("lineLegend2"), barData);
            }
	    	
	    	function lineChartTasa(values1,values2,series1,series2,labels1) {
            	// BAR CHART
            	$('#lineChart3-title').html("<h5>"+title+"</h5>");
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
			        labels: labels1,
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
			    if( window.myBar3!==undefined)
			    	window.myBar3.destroy();
			    var ctx = document.getElementById("lineChart3").getContext("2d");
			    window.myBar3 = new Chart(ctx).Bar(barData, barOptions);
			    // END BAR CHART

                legend(document.getElementById("lineLegend3"), barData);
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