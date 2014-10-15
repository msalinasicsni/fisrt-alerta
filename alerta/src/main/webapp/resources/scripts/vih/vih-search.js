/**
 * Created by USER on 13/10/2014.
 */
var SearchVih = function () {

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
                    codSilais: {
                        required: true
                    },
                    codUnidadSalud: {
                        required: true
                    },
                    codUsuario: {
                        required: false,
                        minlength: 4,
                        maxlength: 13
                    }
                    /*anioEpi: {
                        required: true,
                        digits: true,
                        maxlength: 4
                    }*/
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
                var vihFiltros = {};
                //encuestaFiltros['codModeloEncu'] = $('#codModeloEncu option:selected').val();
                vihFiltros['codSilais'] = $('#codSilais option:selected').val();
                vihFiltros['codUnidadSalud'] = $('#codUnidadSalud option:selected').val();
                vihFiltros['codUsuario'] = $('#codUsuario').val();
                //vihFiltros['mesEpi'] = $('#mesEpi').val();

                $.getJSON(parametros.sSurveyUrl, {
                    vihFiltros: JSON.stringify(vihFiltros),
                    ajax : 'true'
                }, function(dataToLoad) {
                    var len = dataToLoad.length;
                    for ( var i = 0; i < len; i++) {
                        var surveyUrl = parametros.sSurveyEditUrl + '?idMaestro='+dataToLoad[i][0].cod_usuario;
                        table1.fnAddData(
                            [dataToLoad[i][0].silais, dataToLoad[i][0].unidadSalud, dataToLoad[i][0].cod_usuario, dataToLoad[i][0].fecha, '<a href='+ surveyUrl + ' class="btn btn-default btn-xs"><i class="fa fa-pencil fa-fw"></i></a>']);
                        //[data[i].identificacion, data[i].primerNombre, data[i].segundoNombre, data[i].primerApellido, data[i].segundoApellido, data[i].fechaNacimiento,data[i].municipioResidencia.nombre,'<a href='+ personUrl + ' class="btn btn-default btn-xs"><i class="fa fa-search"></i></a>']);
                    }
                })
                    .fail(function() {
                        alert( "error" );

                    });
            };

            <!-- al seleccionar SILAIS -->
            $('#codSilais').change(function(){
                $.getJSON(parametros.sUnidadesUrl, {
                    silaisId: $(this).val(),
                    ajax: 'true'
                }, function(data){
                    var html = null;
                    $('#codUnidadSalud').val("").change();
                    var len = data.length;
                    html += '<option value="">Seleccione...</option>';
                    for(var i = 0; i < len; i++){
                        html += '<option value="' + data[i].codigo + '">'
                            + data[i].nombre
                            + '</option>';
                        // html += '</option>';
                    }
                    $('#codUnidadSalud').html(html);
                })
            });
        }
    };

}();