var SeleccionUnidad = function () {
	
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
        	$('#codSilaisAtencion').change(
            		function() {
            			bloquearUI(parametros.blockMess);
            			$.getJSON(parametros.municipiosUrl, {
            				idSilais : $('#codSilaisAtencion').val(),
            				ajax : 'true'
            			}, function(data) {
            				$("#codMunicipio").select2('data',null);
            				$("#codMunicipio").empty();
            				$("#codUnidadAtencion").select2('data',null);
            				$("#codUnidadAtencion").empty();
            				var html='<option value=""></option>';
            				var len = data.length;
            				for ( var i = 0; i < len; i++) {
            					html += '<option value="' + data[i].codigoNacional + '">'
            							+ data[i].nombre + '</option>';
            				}
            				html += '</option>';
            				$('#codMunicipio').html(html);
            			});
            			setTimeout($.unblockUI, 500);
            			});
        	
        	$('#codMunicipio').change(
            		function() {
            			bloquearUI(parametros.blockMess);
            			$.getJSON(parametros.unidadesUrl, {
            				codMunicipio : $('#codMunicipio').val(),
            				codSilais: $('#codSilaisAtencion').val(),
            				ajax : 'true'
            			}, function(data) {
            				$("#codUnidadAtencion").select2('data',null);
            				$("#codUnidadAtencion").empty();
            				var html='<option value=""></option>';
            				var len = data.length;
            				for ( var i = 0; i < len; i++) {
            					html += '<option value="' + data[i].codigoNacional + '">'
            							+ data[i].nombre + '</option>';
            				}
            				html += '</option>';
            				$('#codUnidadAtencion').html(html);
            			});
            			setTimeout($.unblockUI, 500);
            			});
        	
        	//filtro de Municipio segun departamento
            $('#departamento').change(function(){
                $.getJSON(parametros.municipioByDepaUrl, {
                    departamentoId: $(departamento).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = '<option value="">Seleccione...</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigoNacional + '">'
                            + data[i].nombre
                            + '</option>';
                    }
                    html += '</option>';
                    $('#municipioResidencia').html(html);
                });
            });
            
          //filtro de comunidad segun municipio
            $('#municipioResidencia').change(function(){
                $.getJSON(parametros.comunidadUrl, {
                    municipioId: $(municipioResidencia).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = '<option value="">Seleccione...</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigo + '">'
                            + data[i].nombre
                            + '</option>';
                    }
                    html += '</option>';
                    $('#comunidadResidencia').html(html);
                });
                
            });
        }
	};
	
}();