var SearchSurvey = function () {

    return {
        //main function to initiate the module
        init: function (parametros) {
			var responsiveHelper_dt_basic = undefined;
			var breakpointDefinition = {
				tablet : 1024,
				phone : 480
			};
			var table1 = $('#dtBusqueda').dataTable({
				"sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
					"t"+
					"<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
				"autoWidth" : true,
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_dt_basic) {
						responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#dtBusqueda'), breakpointDefinition);
					}
				},
				"rowCallback" : function(nRow) {
					responsiveHelper_dt_basic.createExpandIcon(nRow);
				},
				"drawCallback" : function(oSettings) {
					responsiveHelper_dt_basic.respond();
				}
			});
            
            $('#frmPrincipal').validate({
    			// Rules for form validation
                rules: {
                    codModeloEncu: {
                        required: true
                    },
                    codSilais: {
                        required: true
                    },
                    codUnidadSalud: {
                        required: true
                    },
                    mesEpi: {
                        required: true,
                        digits: true,
                        maxlength: 2,
                        range: [1, 12]
                    },
                    anioEpi: {
                        required: true,
                        digits: true,
                        maxlength: 4
                    }
                },
    				// Do not change code below
    				errorPlacement : function(error, element) {
    					error.insertAfter(element.parent());
    				},
    				submitHandler: function (form) {
                        table1.fnClearTable();
                        //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                        getSurveys();
                    }
            });

            
            function getSurveys() {
                var encuestaFiltros = {};
                encuestaFiltros['codModeloEncu'] = $('#codModeloEncu option:selected').val();
                encuestaFiltros['codSilais'] = $('#codSilais option:selected').val();
                encuestaFiltros['codUnidadSalud'] = $('#codUnidadSalud option:selected').val();
                encuestaFiltros['anioEpi'] = $('#anioEpi').val();
                encuestaFiltros['mesEpi'] = $('#mesEpi').val();

    			$.getJSON(parametros.sSurveyUrl, {
                    filtrosEncuesta: JSON.stringify(encuestaFiltros),
    				ajax : 'true'
    			}, function(dataToLoad) {
                    console.log(dataToLoad);
                    var len = Object.keys(dataToLoad).length;
                    //var d = dataToLoad['encu0'];
                    if (len > 0) {
                        ocultarMensaje();
                        for (var i = 0; i < len; i++) {
                            var surveyUrl = parametros.sSurveyEditUrl + '?idMaestro=' + dataToLoad['encu'+i].encuestaId;
                            table1.fnAddData(
                                [dataToLoad['encu'+i].silais, dataToLoad['encu'+i].unidadSalud, dataToLoad['encu'+i].mesEpi, dataToLoad['encu'+i].anioEpi, dataToLoad['encu'+i].departamento, dataToLoad['encu'+i].municipio, dataToLoad['encu'+i].distrito, dataToLoad['encu'+i].area, dataToLoad['encu'+i].ordinalEncu, dataToLoad['encu'+i].procedencia, dataToLoad['encu'+i].feInicioEncuesta, dataToLoad['encu'+i].feFinEncuesta, dataToLoad['encu'+i].modeloEncu, '<a href=' + surveyUrl + ' class="btn btn-default btn-xs"><i class="fa fa-edit fa-fw"></i></a>']);
                                //[dataToLoad[i][0].silais, dataToLoad[i][0].unidadSalud, dataToLoad[i][0].mesEpi, dataToLoad[i][0].anioEpi, dataToLoad[i][0].departamento, dataToLoad[i][0].municipio, dataToLoad[i][0].distrito, dataToLoad[i][0].area, dataToLoad[i][0].ordinalEncu, dataToLoad[i][0].procedencia, dataToLoad[i][0].feInicioEncuesta, dataToLoad[i][0].feFinEncuesta, dataToLoad[i][0].modeloEncu, '<a href=' + surveyUrl + ' class="btn btn-default btn-xs"><i class="fa fa-pencil fa-fw"></i></a>']);
                            //[data[i].identificacion, data[i].primerNombre, data[i].segundoNombre, data[i].primerApellido, data[i].segundoApellido, data[i].fechaNacimiento,data[i].municipioResidencia.nombre,'<a href='+ personUrl + ' class="btn btn-default btn-xs"><i class="fa fa-search"></i></a>']);
                        }
                    }else{
                        $.smallBox({
                            title: $("#msg_no_results_found").val() ,
                            content: $("#smallBox_content").val(),
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 4000
                        });
                    }
    			})
    			.fail(function() {
				    alert( "error" );
				    
				});
            }

            function mostrarMensaje(msgHtml){
                $("#mensaje").html(msgHtml).show().focus();
                //$("#mensaje").show("slow");
                //$("#mensaje").focus();
            }

            function ocultarMensaje() {
                $("#mensaje").hide("slow");
            }

            <!-- al seleccionar SILAIS -->
            $('#codSilais').change(function(){
                $('#codUnidadSalud').val('').change();
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sUnidadesUrl, {
                        codSilais: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            // html += '</option>';
                        }
                        $('#codUnidadSalud').html(html);
                    })
                }
            });
        }
    };

}();

