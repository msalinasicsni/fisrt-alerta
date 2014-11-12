/**
 * Created by JMPS on 10-21-14.
 */
var CreateVIH = function () {

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
                    $('#codUnidadAtencion').val("").change();
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

            $('#codDepartamento').change(function () {
                $.getJSON(parametros.sMunicipiosUrl, {
                    departamentoId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = '<option value="">Seleccionar...</option>';
                    $('#codMunicipio').val("").change();
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigo + '">'
                            + data[i].nombre
                            + '</option>';
                    }
                    html += '</option>';
                    $('#codMunicipio').html(html);
                })
            });

            $('#bootstrap-wizard-1').bootstrapWizard({
                'tabClass': 'form-wizard',
                'onNext': function (tab, navigation, index) {
                    var $valid = $("#wizard-1").valid();
                    if (!$valid) {
                        $validator.focusInvalid();
                        return false;
                    } else {
                        $('#bootstrap-wizard-1').find('.form-wizard').children('li').eq(index - 1).addClass(
                            'complete');
                        $('#bootstrap-wizard-1').find('.form-wizard').children('li').eq(index - 1).find('.step')
                            .html('<i class="fa fa-check"></i>');
                    }
                }
            });
            // fuelux wizard
            var wizard = $('.wizard').wizard();
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

            $('input[name=embarazada]').click(function () {
                /*alert("La seleccionada es: " + $('input:radio[name=embarazada]:checked').val());
                alert("La seleccionada es: " + $(this).val());*/
                if($(this).val()==="1"){
                    $("#datosemb").fadeIn('slow');
                    $("#datosemb2").fadeIn('slow');
                 }else{
                    $("#datosemb").fadeOut('slow');
                    $('#fum').val('').change();
                    $('#semanasemb').val('').change();
                    $('#metodosemgest').val('').change();
                    $('#fpp').val('').change();
                    $("#datosemb2").fadeOut('slow');
                    $('#periodopruebavihemb').val('').change();
                    $('#controlprenatal').val('').change();
                    }
              });

            $('input[name=relacextranj]').click(function () {

                if($(this).val()==="0"){
                    $('input[name=usocondoextranj]').attr('disabled', 'disabled');
                    $('input[name=usocondoextranj]').removeAttr('checked');
                }else{
                    $('input[name=usocondoextranj]').removeAttr("disabled");
                }
            });

            $('#sexo').change(function(){

                if ($('#sexo option:selected').text() === "Mujer" ){
                    $('input[name=embarazada]').removeAttr("disabled");
                    $("#gesta").prop('disabled', false);
                    $("#para").prop('disabled', false);
                    $("#cesarea").prop('disabled', false);
                    $("#aborto").prop('disabled', false);
                }else{
                    $('input[name=embarazada]').attr('disabled', 'disabled');
                    $('input[name=embarazada]').removeAttr('checked');
                    $("#datosemb").fadeOut('slow');
                    $("#datosemb2").fadeOut('slow');
                    $('#fum').val('').change();
                    $('#semanasemb').val('').change();
                    $('#metodosemgest').val('').change();
                    $('#fpp').val('').change();
                    $('#periodopruebavihemb').val('').change();
                    $('#controlprenatal').val('').change();
                    $('#gesta').val('').change();
                    $('#para').val('').change();
                    $('#cesarea').val('').change();
                    $('#aborto').val('').change();
                    $("#gesta").prop('disabled', true);
                    $("#para").prop('disabled', true);
                    $("#cesarea").prop('disabled', true);
                    $("#aborto").prop('disabled', true);
                }
            });
/*
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
            });*/




        }
    };

}();
