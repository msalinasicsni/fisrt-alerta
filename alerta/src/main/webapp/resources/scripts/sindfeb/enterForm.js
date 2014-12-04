var EnterFormSindFeb = function () {
	
	return {
		//main function to initiate the module
        init: function (parametros) {
        	
        	//Formulario
        	var form = $('#sind_feb_form');
        	
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
	    		$.post( parametros.sAddFebrilUrl
	    				, form.serialize()
	    				, function(data)
	    				{
	    			if (data == ""){
	    				showMessage("Error","Error desconocido","#C46A69","fa fa-warning",4000);
	    			}
	    			else{
	    				ficha = JSON.parse(data);
	    				$('#idNotificacion').val(ficha.idNotificacion.idNotificacion);
	    				showMessage("Proceso completo!!",ficha.idNotificacion.persona.primerNombre + ' ' +ficha.idNotificacion.persona.primerApellido,"#5F895F","fa fa-check-square-o bounce animated",4000);
	    			}
	    				}
	    		, 'text' )
	    		.fail(function(XMLHttpRequest, textStatus, errorThrown) {
	    			showMessage("FAIL",parametros.processError+" "+errorThrown,"#C46A69","fa fa-warning",8000);	    			
	    		});
	    	}
	    	
	    	
	    	$("#fechaNacimiento").change(
	                function() {
	                    if ($("#fechaNacimiento").val()!=null && $("#fechaNacimiento").val().length > 0) {
	                        $("#edad").val(getAge($("#fechaNacimiento").val()));
	                    }else{
	                        $("#edad").val('');
	                    }
	                });
        	
        }
	};
	
}();