var EnterFormSindFeb = function () {
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

    var desbloquearUI = function() {
        setTimeout($.unblockUI, 500);
    };

	return {
		//main function to initiate the module
        init: function (parametros) {
        	
        	//Formulario
        	var form = $('#sind_feb_form');
        	
        	jQuery.validator.addMethod("noNingunoyOtro", function(value, select) { 
            	var isValid = true;
            	var number = $('option:selected', select).size();
                if (number > 1 && select.options[select.options.length-1].selected) {
                	isValid = false;
                }
                return isValid;
	      	}, "Invalido");
        	
        	//Validation
	    	form.validate({
				// Rules for form validation
					rules : {
						codSilaisAtencion: {
	                        required: true
	                    },
	                    codUnidadAtencion: {
	                        required: true
	                    },
	                    codMunicipio: {
	                        required: true
	                    },
	                    fechaFicha:{
	                    	required: true
	                    },
	                    codProcedencia:{
	                    	required: true
	                    },
	                    viaje:{
	                    	required: true
	                    },
	                    dondeViaje:{
	                    	required: true
	                    },
	                    embarazo:{
	                    	required: true
	                    },
	                    mesesEmbarazo:{
	                    	required: true
	                    },
	                    enfCronica:{
	                    	required: true,
	                    	noNingunoyOtro:true
	                    },
	                    enfAgudaAdicional:{
	                    	required: true,
	                    	noNingunoyOtro:true
	                    },
	                    otraAgudaAdicional:{
	                    	required: true
	                    },
	                    otraCronica:{
	                    	required: true
	                    },
	                    fuenteAgua:{
	                    	required: true
	                    },
	                    otraFuenteAgua:{
	                    	required: true
	                    },
	                    animales:{
	                    	required: true,
	                    	noNingunoyOtro:true
	                    },
	                    otrosAnimales:{
	                    	required: true
	                    },
	                    fechaInicioSintomas:{
	                    	required: true
	                    }
	                    ,
	                    temperatura:{
	                    	required: false,
	                    	min:33,
	                    	max:44
	                    }
	                    ,
	                    pas:{
	                    	required: false,
	                    	min:20,
	                    	max:200
	                    },
	                    pad:{
	                    	required: false,
	                    	min:10,
	                    	max:180
	                    },
	                    ssDSA:{
	                    	required: true
	                    },
	                    ssDCA:{
	                    	required: true,
	                    	noNingunoyOtro:true
	                    },
	                    ssDS:{
	                    	required: true,
	                    	noNingunoyOtro:true
	                    },
	                    ssLepto:{
	                    	required: true,
	                    	noNingunoyOtro:true
	                    },
	                    ssHV:{
	                    	required: true,
	                    	noNingunoyOtro:true
	                    },
	                    ssCK:{
	                    	required: true,
	                    	noNingunoyOtro:true
	                    },
	                    hosp:{
	                    	required: true
	                    },
	                    fechaIngreso:{
	                    	required: true
	                    },
	                    fallecido:{
	                    	required: true
	                    },
	                    fechaFallecido:{
	                    	required: true
	                    }
					},
					// Do not change code below
					errorPlacement : function(error, element) {
						error.insertAfter(element.parent());
					}
				});
        	
        	// fuelux wizard
	    	var wizard = $('.wizard').wizard();
	    	
	    	wizard.on('change', function (e, data) {
	    		if (form.valid() == false) {
                    return false;
                }
	    	});
	    	
	    	wizard.on('finished', function (e, data) {
	    		//form submit;
	    		guardarFicha();
		    	
	    	});
	    	
	    	function showMessage(title,content,color,icon,timeout){
	    		$.smallBox({
				    title: title,
				    content: content,
				    color: color,
				    iconSmall: icon,
				    timeout: timeout
				    });
	    	}
	    	
	    	function guardarFicha()
	    	{
                if ($("#autorizado").val()=='true') {
                    bloquearUI(parametros.blockMess);
                    $.post(parametros.sAddFebrilUrl
                        , form.serialize()
                        , function (data) {
                            if (data == "") {
                                desbloquearUI();
                                showMessage("Error", "Error desconocido", "#C46A69", "fa fa-warning", 4000);
                            }
                            else {
                                desbloquearUI();
                                ficha = JSON.parse(data);
                                $('#idNotificacion').val(ficha.idNotificacion.idNotificacion);
                                showMessage("Proceso completo!!", ficha.idNotificacion.persona.primerNombre + ' ' + ficha.idNotificacion.persona.primerApellido, "#5F895F", "fa fa-check-square-o bounce animated", 4000);
                                setTimeout(function(){window.location.href = parametros.sFebrilSearchUrl + '/' + ficha.idNotificacion.persona.personaId;},3000);

                            }
                        }
                        , 'text')
                        .fail(function (XMLHttpRequest, textStatus, errorThrown) {
                            desbloquearUI();
                            showMessage("FAIL", parametros.processError + " " + errorThrown, "#C46A69", "fa fa-warning", 8000);
                        });
                }else{
                    showMessage("Error", "No esta autorizado para actualizar la notificación", "#C46A69", "fa fa-warning", 4000);
                }
	    	}
	    	
	    	
	    	$("#fechaNacimiento").change(
	    			function() {
	    				if ($("#fechaNacimiento").val()!=null && $("#fechaNacimiento").val().length > 0) {
	    					$("#edad").val(getAge($("#fechaNacimiento").val()));
	    				}else{
	    					$("#edad").val('');
	    				}
	    			});
	    	
	    	$('#viaje').change(function () {
	    		if ($('#viaje').val() != "") {
	    			if ($('#viaje').val() == "RESP|S") {
	    				$('#sihayviaje').fadeIn('slow');
	    			} else {
	    				$('#sihayviaje').fadeOut('slow');
	    				$('#dondeViaje').val('');
	    			}
	    		}
	    	});
	    	
	    	$('#embarazo').change(function () {
	    		if ($('#embarazo').val() != "") {
	    			if ($('#embarazo').val() == "RESP|S") {
	    				$('#sihayemb').fadeIn('slow');
	    			} else {
	    				$('#sihayemb').fadeOut('slow');
	    				$('#mesesEmbarazo').val(0);
	    			}
	    		}
	    	});
	    	
	    	$('#enfCronica').change(function () {
	    		if ($('#enfCronica').val() != null) {
	    			var valor = $('#enfCronica').val().toString();
	    			if (valor.indexOf("CRONICAS|OTRA")!=-1) {
	    				$('#sihayotracronica').fadeIn('slow');
	    			} else {
	    				$('#sihayotracronica').fadeOut('slow');
	    				$('#otraCronica').val('');
	    			}
	    		}
	    	});
	    	
	    	$('#enfAgudaAdicional').change(function () {
	    		if ($('#enfAgudaAdicional').val() != null) {
	    			var valor = $('#enfAgudaAdicional').val().toString();
	    			if (valor.indexOf("AGUDAS|OTRA")!=-1) {
	    				$('#sihayotraaguda').fadeIn('slow');
	    			} else {
	    				$('#sihayotraaguda').fadeOut('slow');
	    				$('#otraAgudaAdicional').val('');
	    			}
	    		}
	    	});
	    	
	    	$('#fuenteAgua').change(function () {
	    		if ($('#fuenteAgua').val() != null) {
	    			var valor = $('#fuenteAgua').val().toString();
	    			if (valor.indexOf("AGUA|OTRA")!=-1) {
	    				$('#sihayotrafuente').fadeIn('slow');
	    			} else {
	    				$('#sihayotrafuente').fadeOut('slow');
	    				$('#otraFuenteAgua').val('');
	    			}
	    		}
	    	});
	    	
	    	$('#animales').change(function () {
	    		if ($('#animales').val() != null) {
	    			var valor = $('#animales').val().toString();
	    			if (valor.indexOf("ANIM|OTRA")!=-1) {
	    				$('#sihayotroanimal').fadeIn('slow');
	    			} else {
	    				$('#sihayotroanimal').fadeOut('slow');
	    				$('#otrosAnimales').val('');
	    			}
	    		}
	    	});
	    	
	    	$('#hosp').change(function () {
	    		if ($('#hosp').val() != "") {
	    			if ($('#hosp').val() == "RESP|S") {
	    				$('#sieshosp').fadeIn('slow');
	    			} else {
	    				$('#sieshosp').fadeOut('slow');
	    				$('#fechaIngreso').val('');
	    			}
	    		}
	    	});
	    	
	    	$('#fallecido').change(function () {
	    		if ($('#fallecido').val() != "") {
	    			if ($('#fallecido').val() == "RESP|S") {
	    				$('#siesfallecido').fadeIn('slow');
	    			} else {
	    				$('#siesfallecido').fadeOut('slow');
	    				$('#fechaFallecido').val('');
	    			}
	    		}
	    	});
        	
        }
	};
	
}();