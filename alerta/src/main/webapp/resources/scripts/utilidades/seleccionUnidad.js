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
	            	},
            baseZ: 1051 // para que se muestre bien en los modales
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
            				$("#codUnidadAtencion").select2('data',null);
            				$("#codMunicipio").empty();
            				$("#codUnidadAtencion").empty();
            				var html='<option value=""></option>';
            				var len = data.length;
            				for ( var i = 0; i < len; i++) {
            					html += '<option value="' + data[i].codigoNacional + '">'
            							+ data[i].nombre + '</option>';
            				}
            				html += '</option>';
            				$('#codMunicipio').html(html);
                            $('#codMunicipio').focus();
                            $('#s2id_codMunicipio').addClass('select2-container-active');
                            //select2-container-active select2-dropdown-open select2-drop-above
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
            					html += '<option value="' + data[i].codigo + '">'
            							+ data[i].nombre + '</option>';
            				}
            				html += '</option>';
            				$('#codUnidadAtencion').html(html);
                            //console.log("unidades cargadas");
                            $('#codUnidadAtencion').focus();
                            $('#s2id_codUnidadAtencion').addClass('select2-container-active');
            			});
            			setTimeout($.unblockUI, 500);
            			});
        	
        	//filtro de Municipio segun departamento
            $('#departamento').change(function(){
                $.getJSON(parametros.municipioByDepaUrl, {
                    departamentoId: $(departamento).val(),
                    ajax: 'true'
                }, function (data) {
                	$("#municipioResidencia").select2('data',null);
                	$("#comunidadResidencia").select2('data',null);
                	$("#municipioResidencia").empty();
                	$("#comunidadResidencia").empty();
                    var html = '<option value=""></option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigoNacional + '">'
                            + data[i].nombre
                            + '</option>';
                    }
                    html += '</option>';
                    $('#municipioResidencia').html(html);
                    $('#municipioResidencia').focus();
                    $('#s2id_municipioResidencia').addClass('select2-container-active');
                });
            });
            
          //filtro de comunidad segun municipio
            $('#municipioResidencia').change(function(){
                $.getJSON(parametros.comunidadUrl, {
                    municipioId: $(municipioResidencia).val(),
                    ajax: 'true'
                }, function (data) {
                	$("#comunidadResidencia").select2('data',null);
                	$("#comunidadResidencia").empty();
                    var html = '<option value=""></option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        if(data[i].sector.unidad != null){
                            html += '<option value="' + data[i].codigo +  '">'
                                + data[i].nombre + "-"+ data[i].sector.unidad.nombre
                                + '</option>';
                        }else{
                            html += '<option value="' + data[i].codigo +  '">'
                                + data[i].nombre + "-"+ data[i].sector.nombre
                                + '</option>';
                        }

                    }
                    html += '</option>';
                    $('#comunidadResidencia').html(html);
                    $('#comunidadResidencia').focus();
                    $('#s2id_comunidadResidencia').addClass('select2-container-active');
                });
                
            });

            //ENTOMOLOGIA
            // al seleccionar municipio 
            $('#codMunicipioEncu').change(function(){
                bloquearUI(parametros.blockMess);
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sUnidadesUrl, {
                        codMunicipio: $(this).val(),
                        codSilais:$('#codSilais option:selected').val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codUnidadSalud').html(html);
                    });
                    $.getJSON(parametros.sDistritosUrl, {
                        codMunicipio: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].valor
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoDistrito').html(html);
                    });
                    $.getJSON(parametros.sAreasUrl, {
                        codMunicipio: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].valor
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoArea').html(html);
                    });
                }else{
                    var html = '<option value="">' + $("#text_opt_select").val() + '...</option>';
                    $('#codUnidadSalud').html(html);
                    $('#codigoDistrito').html(html);
                    $('#codigoArea').html(html);
                }
                $('#codUnidadSalud').val('').change();
                $('#codigoArea').val('').change();
                $('#codigoDistrito').val('').change();
                setTimeout($.unblockUI, 500);
            });

            <!-- Al seleccionar unidad de salud-->
            $('#codUnidadSalud').change(function () {
                if ($(this).val().length > 0){
                    $.getJSON(parametros.sSectoresUrl, {
                        codUnidad: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoSector').html(html);
                    });
                }else {
                    var html = '<option value="">' + $("#text_opt_select").val() + '...</option>';
                    $('#codigoSector').html(html);
                }
                $('#codigoSector').val('').change();

            });

            //<!-- al seleccionar sector-->
            $('#codigoSector').change(function(){
                bloquearUI(parametros.blockMess);
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sComunidadesUrl, {
                        codSector: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            //html += '</option>';
                        }
                        $('#codigoLocalidad').html(html);
                    });
                }else{
                    var html = '<option value="">' + $("#text_opt_select").val() + '...</option>';
                    $('#codigoLocalidad').html(html);
                }
                $('#codigoLocalidad').val('').change();
                setTimeout($.unblockUI, 500);
            });

            //<!-- al seleccionar SILAIS -->
            $('#codSilais').change(function(){
                bloquearUI(parametros.blockMess);
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sMunicipiosUrl, {
                        idSilais: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigoNacional + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codMunicipioEncu').html(html);
                    });
                }else{
                    var html = '<option value="">' + $("#text_opt_select").val() + '...</option>';
                    $('#codMunicipioEncu').html(html);
                }
                $('#codMunicipioEncu').val('').change();
                setTimeout($.unblockUI, 500);
            });
        }
	};
	
}();