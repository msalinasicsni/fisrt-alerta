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
		        "aoColumns" : [{sClass: "aw-center"},{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" }
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
                        pieChart();
                        pieChart2();
                        pieChart3();
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
            			}else if ($('#codArea option:selected').val() == "AREAREP|ZE"){
                            $('#silais').hide();
                            $('#departamento').hide();
                            $('#municipio').hide();
                            $('#unidad').hide();
                            $('#dSubUnits').hide();
                            $('#zona').show();
                            $("#codZona").val("").change();
                        }
                        console.log($('#codArea option:selected').val());
                    });
            
            function getData() {
            	bloquearUI(parametros.blockMess); 
            	$.getJSON(parametros.sActionUrl, $('#parameters_form').serialize(), function(data) {
            		title = $('#codPato option:selected').text();
            		var encontrado = false;
            		var pobMasc = 0;
            		var pobFem = 0;
            		var pobTotal = 0;
            		var factor = 0;
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
            		title = title + '</br>Semana '+$('#semI option:selected').text() +' a la '+$('#semF option:selected').text() +', '+$('#anioI option:selected').text();
            		
            		for (var row in data) {
            			if(data[row][0]=='Pop'){
            				pobMasc=data[row][1],pobFem=data[row][2],pobTotal=data[row][3];
            				break;
            			}
            		}
            		
            		for (var row in data) {
            			if(data[row][0]=='Pato'){
            				factor=data[row][1];
            				break;
            			}
            		}
            		
            		for (var row in data) {
        				if(data[row][0]==$('#anioI option:selected').text()){
        					var tasaM = (pobMasc == null) ? "NP":Math.round(data[0][29]/(pobMasc) * factor * 100) / 100;
        					var tasaF = (pobFem == null) ? "NP":Math.round(data[0][30]/(pobMasc) * factor * 100) / 100;
        					var tasa = (pobTotal == null) ? "NP":Math.round((data[0][29] + data[0][30])/(pobTotal) * factor * 100) / 100;
        					table1.fnAddData([data[0][0],data[0][29], data[0][30], data[0][29] + data[0][30],
        					                 Math.round(data[0][29]/(data[0][29] + data[0][30]) * 100 * 100) / 100,
     	 	    							 Math.round(data[0][30]/(data[0][29] + data[0][30]) * 100 * 100) / 100,
     	 	    							 tasaM, tasaF, tasa]);
        					pieChart(data[0][29], data[0][30]);
        					pieChart2(Math.round(data[0][29]/(data[0][29] + data[0][30]) * 100 * 100) / 100,
 	    							 Math.round(data[0][30]/(data[0][29] + data[0][30]) * 100 * 100) / 100);
        					pieChart3(tasaM,tasaF, factor);
        					encontrado = true;
        				}
        			}
            		if(!encontrado){
	            		showMessage("Sin datos", "No se encontraron datos como resultado de esta consulta", "#AF801C", "fa fa-warning", 3000);
	            		title='';
	            		$('#pieChart-title').html("<h5>"+title+"</h5>");
	            		$('#pieChart2-title').html("<h5>"+title+"</h5>");
	            		$('#pieChart3-title').html("<h5>"+title+"</h5>");
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
            
            function pieChart(porcM1,porcF1) {
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
				        highlight: convertHex(colors[0],70),// "rgba(151,187,205,0.8)",
				        label: "Masculino"
				    },
				    {
				        value: porcF1,
				        color: convertHex(colors[1],100), //"rgba(169, 3, 41, 0.7)",
				        highlight: convertHex(colors[1],70),// "rgba(169, 3, 41, 0.7)",
				        label: "Femenino"
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
            
            function pieChart2(porcM2,porcF2) {
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
				        highlight: convertHex(colors[0],70),// "rgba(151,187,205,0.8)",
				        label: "Masculino"
				    },
				    {
				        value: porcF2,
				        color: convertHex(colors[1],100),// "rgba(169, 3, 41, 0.7)",
				        highlight: convertHex(colors[1],70),// "rgba(169, 3, 41, 0.7)",
				        label: "Femenino"
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
            
            function pieChart3(porcM3,porcF3,factor) {
            	// PIE CHART
            	$('#pieChart3-title').html("<h5>"+title+", Tasa por "+factor+" hab.</h5>");
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
				        value: porcM3,
				        color: convertHex(colors[0],100),// "rgba(151,187,205,1)",
				        highlight: convertHex(colors[0],70), //"rgba(151,187,205,0.8)",
				        label: "Masculino"
				    },
				    {
				        value: porcF3,
				        color: convertHex(colors[1],100),// "rgba(169, 3, 41, 0.7)",
				        highlight: convertHex(colors[1],70),// "rgba(169, 3, 41, 0.7)",
				        label: "Femenino"
				    }
			    ];

			    // render chart
			    if( window.myPie3!==undefined)
			    	window.myPie3.destroy();
			    var ctx = document.getElementById("pieChart3").getContext("2d");
			    window.myPie3 = new Chart(ctx).Pie(pieData, pieOptions);
			    
			    // END PIE CHART

                legend(document.getElementById("pieLegend3"), pieData);
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