/**
 * Created by souyen-ics on 10-14-14.
 */
var CreateIrag = function () {

    return {
        init: function (parametros) {
            console.log(parametros.dToday);

            //Filtro Unidad de Salud segun Silais seleecionado
            $('#codSilaisAtencion').change(function () {
                $.getJSON(parametros.sUnidadesUrl, {
                    silaisId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = '<option value="">Seleccionar...</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigo + '">'
                            + data[i].nombre
                            + '</option>';
                    }
                    html += '</option>';
                    $('#codUnidadAtencion').html(html);
                })
            });

            $('#bootstrap-wizard-1').bootstrapWizard({onNext: function (tab, navigation, index) {
                var $valid = $("#wizard-1").valid();
                if (!$valid) {
                    $validator.focusInvalid();
                    return false;
                } else {
                    fnGuardarFicha(index);
                }

            },

                onTabShow: function (tab, navigation, index) {
                    var $total = navigation.find('li').length;
                    var $current = index + 1;


                    if ($current >= $total) {
                        $('#bootstrap-wizard-1').find('.pager .next').hide();
                        $('#bootstrap-wizard-1').find('.pager .finish').show();
                        $('#bootstrap-wizard-1').find('.pager .finish').removeClass('disabled');
                    } else {
                        $('#bootstrap-wizard-1').find('.pager .next').show();
                        $('#bootstrap-wizard-1').find('.pager .finish').hide();
                    }
                }

            });

            function fnGuardarFicha(index) {

                if (index != 5) {
                    fnguardar();
                } else {

                }
            }

            function fnguardar() {
                var datos_form = $('#wizard-1').serialize();

                $.ajax({
                    type: "GET",
                    url: parametros.sAddIragUrl,
                    data: datos_form,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function () {
                        $.smallBox({
                            title: "Exito! Los datos se han guardado correctamente!",
                            content: "Este mensaje desaparecerá en 2 segundos!",
                            color: "#739E73",
                            iconSmall: "fa fa-check-circle",
                            timeout: 2000
                        });

                    },
                    error: function () {
//                alert("Error " + result.status + '' + result.statusText);
                        $.smallBox({
                            title: "Error! Los datos no se han guardado correctamente!",
                            content: "Este mensaje desaparecerá en 2 segundos!",
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 2000
                        });
                    }
                });

            }

            $('#bootstrap-wizard-1 .finish').click(function () {

                var $valid = $("#wizard-1").valid();
                if (!$valid) {
                    $validator.focusInvalid();
                    return false;
                } else {
                    fnguardar();

                }


            });

            var $validator = $("#wizard-1").validate({
                rules: {
                    codSilaisAtencion: {
                        required: true

                    },
                    codUnidadAtencion: {
                        required: true

                    },

                    fechaConsulta: {
                        required: true,
                        dpDate: true,
                        dpCompareDate: {after: '#fechaConsulta', 'notAfter': parametros.dToday}

                    },

                    fechaPrimeraConsulta:{
                        dpDate: true,
                        dpCompareDate: {after: '#fechaPrimeraConsulta', 'notAfter': parametros.dToday}

                    },

                    codProcedencia: {
                        required: true
                    },

                    codCaptacion: {
                        required: true
                    },

                    diagnostico: {
                        required: true
                    },

                    fechaInicioSintomas: {
                        required: true
                    }

                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });




        }
    };

}();
