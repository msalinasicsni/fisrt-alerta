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
                        tipoIndicador: {
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
                            $('#dNivelPais').show();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').hide();
            				$('#unidad').hide();
                            $('#dNivelPais').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
            				$('#silais').hide();
            				$('#departamento').show();
            				$('#municipio').hide();
            				$('#unidad').hide();
                            $('#dNivelPais').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').show();
            				$('#unidad').hide();
                            $('#dNivelPais').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').show();
            				$('#unidad').show();
                            $('#dNivelPais').hide();
            			}
                    });
            
            function getData() {
            	
            	bloquearUI(parametros.blockMess); 
            	$.getJSON(parametros.sActionUrl, $('#parameters_form').serialize(), function(data) {
            		countryData = []; 
            		for(var row in data){
                        countryData[data[row][0]] = data[row][1];
        			}
                    var nombreMapa = '', tituloMapa = '';
                    if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
                        console.log($('input[name="nivelPais"]:checked', '#parameters_form').val());
                        if ( $('input[name="nivelPais"]:checked', '#parameters_form').val()=='true') {
                            nombreMapa = 'nicaragua_mill_en';
                        }else{
                            nombreMapa = 'nicaragua_mun_mill_en';
                        }
                    }else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
                        nombreMapa = 'nicaragua_mun_mill_en';
                    }

                    tituloMapa = $('#tipoIndicador option:selected').text();

            		$('#vector-map').html('');
        			$('#vector-map').vectorMap({
        				map : nombreMapa,
        				backgroundColor : '#fff',
        				regionStyle : {
        					initial : {
        						fill : '#c4c4c4'
        					},
        					hover : {
        						"fill-opacity" : 1
        					}
        				},
        				series : {
        					regions : [{
        						values : countryData,
        						scale : ['#008141','#ffd700','#800000'],
        						normalizeFunction: 'polynomial',
        				        attribute: 'fill',
        				        legend: {
        			                horizontal: true,
        			                cssClass: 'jvectormap-legend-icons',
        			                title: tituloMapa
        			              }
        					}]
        				},
        				onRegionTipShow: function(event, label, code){
        			        label.html(
        			          '<b>'+label.html()+'</b></br>'+
        			          '<b>'+tituloMapa+': </b>'+(countryData[code] === undefined?'-':countryData[code])
        			        );
        			      }
        			});
            		setTimeout($.unblockUI, 500);
    			})
    			.fail(function(XMLHttpRequest, textStatus, errorThrown) {
    				alert(" status: " + textStatus + " error:" + errorThrown);
				    setTimeout($.unblockUI, 5);
				});
            }
        }
    };

}();