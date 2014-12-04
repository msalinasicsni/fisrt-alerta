var EnterFormSindFeb = function () {
	
	return {
		//main function to initiate the module
        init: function (parametros) {
        	
        	// fuelux wizard
	    	var wizard = $('.wizard').wizard();
	    	
	    	wizard.on('change', function (e, data) {
	    		//$("#fuelux-wizard").submit();
	    		//console.log("submitted!");
		    	//alert('validate');
	    	});
	    	
	    	wizard.on('finished', function (e, data) {
	    		//$("#fuelux-wizard").submit();
	    		//console.log("submitted!");
		    	$.smallBox({
			    title: "Congratulations! Your form was submitted",
			    content: "<i class='fa fa-clock-o'></i> <i>1 seconds ago...</i>",
			    color: "#5F895F",
			    iconSmall: "fa fa-check bounce animated",
			    timeout: 4000
			    });
	    	});
	    	
	    	
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