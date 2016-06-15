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
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },
		                       {sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" }
		                   ],
               "createdRow": function ( row, data, index ) {
            	   if ( data[10] * 1 > 1.24 ) {
                       $('td', row).eq(10).addClass('highlight');
                   }
            	   if ( data[11] * 1 > 1.24 ) {
                       $('td', row).eq(11).addClass('highlight');
                   }
            	   if ( data[12] * 1 > 1.24 ) {
                       $('td', row).eq(12).addClass('highlight');
                   }
               },
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
    					table1.fnClearTable();
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
            		for (var row in data) {
            			table1.fnAddData(data[row]);
            		}
            		aact = $('#anioI option:selected').val();
            		aant = aact - 1;
            		sact = $('#semI option:selected').val();
            		sant = sact - 3;
            		$(".e1").each(function(){$(this).html('Sem ' + sact);});
            		$(".e2").each(function(){$(this).html('Sem ' + sant + '-' + sact);});
            		$(".a1").each(function(){$(this).html(aant);});
            		$(".a2").each(function(){$(this).html(aact);});
            		$(".sem").each(function(){$(this).html('Sem ' + sact);});
            		$(".sema").each(function(){$(this).html('Sem ' + sant + '-' + sact);});
            		$(".dift").each(function(){$(this).html(aact + '-' + aant);});
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

        }
    };

}();