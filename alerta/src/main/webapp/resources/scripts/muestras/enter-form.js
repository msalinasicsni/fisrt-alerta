/**
 * Created by souyen-ics on 11-05-14.
 */

var EnterFormTomaMx = function () {
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
        init: function (parametros) {

            $('#horaRefrigeracion').datetimepicker({
                format: 'LT'
            });

            $('#horaTomaMx').datetimepicker({
                format: 'LT'
            });

            $('#codTipoMx').change(function() {
                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.dxUrl, {
                    codMx: $('#codTipoMx').val(),
                    tipoNoti: $('#tipoNoti').val(),
                    ajax: 'false'
                }, function (data) {
                    var len = data.length;
                    var html = null;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].diagnostico.idDiagnostico + '">'
                            + data[i].diagnostico.nombre
                            + '</option>';
                    }
                    $('#dx').html(html);
                    desbloquearUI();
                });
            });

            jQuery.validator.addMethod("greaterOrEqualThan",
                function(value, element, params) {
                    var fecha1 = value.split("/");
                    if (!/Invalid|NaN/.test(new Date(fecha1[2], fecha1[1]-1, fecha1[0]))) {
                        if ($(params).val().length > 0) {
                            var fecha2 = $(params).val().split("/");
                            return new Date(fecha1[2], fecha1[1] - 1, fecha1[0]) >= new Date(fecha2[2], fecha2[1] - 1, fecha2[0]);
                        }else { //si el otro campo de fecha esta vacío y no es requerido
                            return true;
                        }
                    }

                    return isNaN(value) && isNaN($(params).val())
                        || (Number(value) >= Number($(params).val()));
                },'Fecha debe ser mayor o igual a {0}.');

            var $validator = $("#registroMx").validate({
                rules: {
                    fechaHTomaMx: {
                        required: true,
                        greaterOrEqualThan: "#fechaInicioSintomas"
                    },

                    codTipoMx:{
                        required:true
                    },

                    dx:{
                        required:true
                    },
                    codUnidadAtencion:{
                        required:true
                    },

                    codSilaisAtencion:{
                        required:true
                    },

                    codMunicipio:{
                        required:true
                    }

                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }

                /*  submitHandler: function (form) {
                 //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                 save();
                 }*/
            });


            $('#submit').click(function() {
                var $validarForm = $("#registroMx").valid();
                if (!$validarForm) {
                    $validator.focusInvalid();
                    return false;
                }else{
                    validateFechaToma();
                }

            });

            $('#back').click(function() {
                window.history.back();
            });


            function validateFechaToma() {
                bloquearUI(parametros.blockMess);
                var valores = $('#dx').val();
                var strValores = '';
                for (var i = 0; i < valores.length; i++) {
                    if (i == 0)
                        strValores = +valores[i];
                    else
                        strValores = strValores + ',' + valores[i];
                }
                $.getJSON(parametros.validateUrl, {
                    idNotificacion : $('#idNotificacion').val(),
                    fechaHTomaMx: $('#fechaHTomaMx').val(),
                    dxs: strValores,
                    ajax : 'true'
                }, function(data) {
                    if (data.respuesta==="OK"){
                        save();
                    }else{
                        $.smallBox({
                            title: data.respuesta ,
                            content:  $('#disappear').val(),
                            color: "#C79121",
                            iconSmall: "fa fa-check-circle",
                            timeout: 4000
                        });
                    }
                });
               desbloquearUI();
            }

            function save() {
                var datos_form = $('#registroMx').serialize();
                bloquearUI(parametros.blockMess);
                $.ajax({
                    type: "GET",
                    url: parametros.saveTomaUrl,
                    data: datos_form,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function () {
                        $.smallBox({
                            title: $('#msjSuccessful').val() ,
                            content:  $('#disappear').val(),
                            color: "#739E73",
                            iconSmall: "fa fa-check-circle",
                            timeout: 2000
                        });
                        clearFields();
                        desbloquearUI();
                        setTimeout(function () {
                            window.history.back();//window.location.href = parametros.searchUrl;
                        }, 3000);

                    },
                    error: function (response) {
                        desbloquearUI();
                        $.smallBox({
                            title: response.responseJSON.error,
                            content:  $('#disappear').val(),
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 4000
                        });
                    }
                });

            }

            function clearFields(){
                //$('#ckChangeUS').prop("checked", false).change();
                $("#fechaHTomaMx").val('').change();
                $("#horaTomaMx").val('').change();
                $('#codTipoMx option:first').prop("selected", true).change();
                $('#dx').select2('data', null);
                $("#canTubos").val('');
                $("#volumen").val('');
                $("#horaRefrigeracion").val('');
                $("#mxSeparada").prop("checked", true).change();
                $('#registroMx input[type="radio"]:checked').each(function(){
                    $(this).prop("checked", false).change();
                });
            }

            $("input[name$='ckChangeUS']").change(function () {
                var valor = ($('#ckChangeUS').is(':checked'));
                if (valor){
                    $('#dSilais').fadeIn('slow');
                    $('#codSilaisAtencion').val('').change();
                    $('#codUnidadAtencion').val('').change();
                    $('#codMunicipio').val('').change();
                }else{
                    $('#dSilais').fadeOut('slow');
                }



            });





        }
    }



}();

var EnterFormTomaMxStudies = function () {
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
        init: function (parametros) {

            $('#horaRefrigeracion').datetimepicker({
                format: 'LT'
            });

            $('#horaTomaMx').datetimepicker({
                format: 'LT'
            });

            $('#codTipoMx').change(function() {
                bloquearUI(parametros.blockMess);
                var html = '<option value="">' + $("#text_opt_select").val() + '...</option>';
                if ($('#unidadId').val()!=""){
                    if ($(this).val()!="") {
                        $.getJSON(parametros.sStudiesUrl, {
                            codMx: $(this).val(),
                            tipoNoti: $('#tipoNoti').val(),
                            idUnidadSalud: $('#unidadId').val(),
                            ajax: 'false'
                        }, function (data) {
                            var len = data.length;
                            for (var i = 0; i < len; i++) {
                                html += '<option value="' + data[i].estudio.idEstudio + '">'
                                    + data[i].estudio.nombre
                                    + '</option>';
                            }
                            $('#idEstudio').html(html);
                            desbloquearUI();
                            if (len==0){
                                $.smallBox({
                                    title: $('#msgSinEstudios').val(),
                                    content:  $('#disappear').val(),
                                    color: "#C79121",
                                    iconSmall: "fa fa-warning",
                                    timeout: 4000
                                });
                            }
                        });
                    }else{
                        desbloquearUI();
                        $('#idEstudio').html(html).change("");
                    }
                }else{
                    desbloquearUI();
                    $('#idEstudio').html(html).change("");
                    $.smallBox({
                        title: $('#msgSinUnidadSalud').val(),
                        content:  $('#disappear').val(),
                        color: "#C79121",
                        iconSmall: "fa fa-warning",
                        timeout: 4000
                    });
                }

            });

            function validarFormatoCodigoUnicoMx (estudio) {
                var pattern;
                var valid = true ;
                var msgErrorValid;
                if (estudio == "1"){ //cohorte dengue
                    pattern = /^\d*[\.]\d*[\.][A-Z][\.][1-2]$/;
                    valid = pattern.test($("#codigoUnicoMx").val());
                    msgErrorValid = $("#msjInvalidCodMxChd").val();
                }
                  if(estudio == "2"){ //clínico dengue
                    pattern = /^\d*[\.][1-9]$/;
                    valid = pattern.test($("#codigoUnicoMx").val());
                    msgErrorValid = $("#msjInvalidCodMxCnd").val();
                }
                if(!valid){
                    $.smallBox({
                        title: msgErrorValid,
                        content:  $('#disappear').val(),
                        color: "#C79121",
                        iconSmall: "fa fa-warning",
                        timeout: 4000
                    });
                }
                return valid;
            }

            $('#idEstudio').change(function () {

                var estudio = $(this).val();
                var cd = "1"; //Se asume que el id del estudio cohorte dengue es el 1
                if (estudio != "") {
                    validarFormatoCodigoUnicoMx(estudio);
                        if (estudio == cd) {
                            $("#divCategoriaMx").fadeIn('slow');
                        } else {
                            $("#divCategoriaMx").fadeOut('slow');
                        }

                }else{
                    $("#divCategoriaMx").fadeOut('slow');
                }
            });

            var $validator = $("#registroMx").validate({
                rules: {
                    fechaHTomaMx : { required: true },
                    codTipoMx :{ required:true },
                    idEstudio : { required:true },
                    codigoUnicoMx : { required:true },
                    codCategoriaMx :{ required:true}

                },
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }

                /*  submitHandler: function (form) {
                 //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                 save();
                 }*/
            });


            $('#submit').click(function() {
                var $validarForm = $("#registroMx").valid();
                if (!$validarForm) {
                    $validator.focusInvalid();
                    return false;
                }else{
                    if (validarFormatoCodigoUnicoMx($('#idEstudio').val())) {
                        save();
                    }
                }

            });

            $('#back').click(function() {
                window.history.back();
            });

            function save() {
                var objetoTomaMx = {};
                objetoTomaMx['idNotificacion'] = $("#idNotificacion").val();
                objetoTomaMx['fechaHTomaMx'] = $("#fechaHTomaMx").val();
                objetoTomaMx['horaTomaMx'] = $("#horaTomaMx").val();
                objetoTomaMx['canTubos'] = $("#canTubos").val();
                objetoTomaMx['volumen'] = $("#volumen").val();
                objetoTomaMx['horaRefrigeracion'] = $("#horaRefrigeracion").val();
                objetoTomaMx['codTipoMx'] = $('#codTipoMx option:selected').val();
                objetoTomaMx['codigoUnicoMx'] = $("#codigoUnicoMx").val();
                objetoTomaMx['mxSeparada'] = $('input[name="mxSeparada"]:checked', '#registroMx').val();
                objetoTomaMx['estudio'] = $('#idEstudio').val();
                objetoTomaMx['categoriaMx'] = $('#codCategoriaMx option:selected').val();
                objetoTomaMx['mensaje'] = '';
                bloquearUI(parametros.blockMess);
                $.ajax({
                    url: parametros.saveTomaMxStudy,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(objetoTomaMx),
                    contentType: 'application/json',
                    mimeType: 'application/json',
                    success: function (data) {
                        desbloquearUI();
                        if (data.mensaje.length > 0) {
                            $.smallBox({
                                title: data.mensaje,
                                content: $("#disappear").val(),
                                color: "#C46A69",
                                iconSmall: "fa fa-warning",
                                timeout: 4000
                            });
                        } else {
                            $.smallBox({
                                title: $('#msjSuccessful').val(),
                                content: $('#disappear').val(),
                                color: "#739E73",
                                iconSmall: "fa fa-check-circle",
                                timeout: 4000
                            });
                            /*setTimeout(function () {
                                window.location.href = parametros.searchUrl;
                            }, 4000);*/
                            clearFields();
                            desbloquearUI();
                        }

                    },
                    error: function (data, status, er) {
                        desbloquearUI();
                        $.smallBox({
                            title: $('#msjErrorSaving').val() + " error: " + data + " status: " + status + " er:" + er,
                            content: $('#disappear').val(),
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 5000
                        });
                    }
                });
            }

            function clearFields(){
                $("#codigoUnicoMx").val('');
                $("#fechaHTomaMx").val('').change();
                $("#horaTomaMx").val('').change();
                $('#codTipoMx option:first').prop("selected", true).change();
                $('#idEstudio').select2('data', null);
                $('#idEstudio option:first').prop("selected", true).change();
                $("#canTubos").val('');
                $("#volumen").val('');
                $("#horaRefrigeracion").val('');
                $('#registroMx input[type="radio"]:checked').each(function(){
                    $(this).prop("checked", false).change();
                });
            }


        }
    }



}();