/**
 * Created by souyen-ics on 11-05-14.
 */

var EditFormTomaMx = function () {
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

            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet: 1024,
                phone: 480
            };
            var table = $('#solicitudes_list').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true,
                "paging": false,
                "ordering": false,
                "searching": false,
                "lengthChange": false,
                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#solicitudes_list'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
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

            <!-- formulario para anular solicitud -->
            $('#override-sol-form').validate({
                // Rules for form validation
                rules: {
                    causaAnulacion: {required: true}
                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());
                },
                submitHandler: function (form) {
                    anularSolicitud($("#idSolicitud").val());
                }
            });

            <!-- formulario para agregar solicitud -->
            $('#addDx-form').validate({
                // Rules for form validation
                rules: {
                    codDXNuevo: {required: true},
                    codEstudioNuevo: {required: true}
                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());
                },
                submitHandler: function (form) {
                    guardarSolicitud();
                }
            });


            $('#submit').click(function() {
                var $validarForm = $("#registroMx").valid();
                if (!$validarForm) {
                    $validator.focusInvalid();
                    return false;
                }else{
                    save();

                }

            });

            $('#back').click(function() {
                window.history.back();
            });

            function save() {
                bloquearUI(parametros.blockMess);
                $.ajax({
                    type: "GET",
                    url: parametros.saveTomaUrl,
                    data: {fechaHTomaMx: $("#fechaHTomaMx").val(), horaTomaMx: $("#horaTomaMx").val(), canTubos: $("#canTubos").val(),
                        volumen: $("#volumen").val(), horaRefrigeracion: $("#horaRefrigeracion").val(), idTomaMx: $("#idTomaMx").val(),
                        mxSeparada: $('input[name="mxSeparada"]:checked', '#registroMx').val(),
                        codSilaisAtencion: $('#codSilaisAtencion').find('option:selected').val(), codUnidadAtencion: $('#codUnidadAtencion').find('option:selected').val()
                    },
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function () {
                        $.smallBox({
                            title: $('#msjSuccessful').val() ,
                            content:  $('#smallBox_content').val(),
                            color: "#739E73",
                            iconSmall: "fa fa-success",
                            timeout: 4000
                        });
                        //clearFields();
                        desbloquearUI();
                        //window.location.href = parametros.searchUrl;

                    },
                    error: function () {
                        desbloquearUI();
                        $.smallBox({
                            title: $('#msjErrorSaving').val(),
                            content:  $('#smallBox_content').val(),
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 4000
                        });
                    }
                });

            }

            function guardarSolicitud() {
                var nuevaSolicitudObj = {};
                nuevaSolicitudObj['idTomaMx'] = $("#idTomaMx").val();
                nuevaSolicitudObj['idDiagnostico'] = $('#codDXNuevo').find('option:selected').val();
                nuevaSolicitudObj['idEstudio'] = $('#codEstudioNuevo').find('option:selected').val();
                nuevaSolicitudObj['esEstudio'] = $('#esEstudio').val();
                nuevaSolicitudObj['mensaje'] = '';
                bloquearUI();
                $.ajax(
                    {
                        url: parametros.sAgregarSolicitudUrl,
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(nuevaSolicitudObj),
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if (data.mensaje.length > 0) {
                                $.smallBox({
                                    title: unicodeEscape(data.mensaje),
                                    content: $("#smallBox_content").val(),
                                    color: "#C46A69",
                                    iconSmall: "fa fa-warning",
                                    timeout: 4000
                                });
                            } else {
                                getRequest();
                                $.smallBox({
                                    title: $("#msg_request_added").val(),
                                    content: $("#smallBox_content").val(),
                                    color: "#739E73",
                                    iconSmall: "fa fa-success",
                                    timeout: 4000
                                });
                            }
                            desbloquearUI();
                        },
                        error: function (jqXHR) {
                            desbloquearUI();
                            alert(" status: " + jqXHR.status + " - " + jqXHR.statusText);
                        }
                    });
            }

            function anularSolicitud(idSolicitud) {
                var anulacionObj = {};
                anulacionObj['idSolicitud'] = idSolicitud;
                anulacionObj['causaAnulacion'] = $("#causaAnulacion").val();
                anulacionObj['mensaje'] = '';
                bloquearUI();
                $.ajax(
                    {
                        url: parametros.sAnularSolicitudUrl,
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(anulacionObj),
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if (data.mensaje.length > 0) {
                                $.smallBox({
                                    title: unicodeEscape(data.mensaje),
                                    content: $("#smallBox_content").val(),
                                    color: "#C46A69",
                                    iconSmall: "fa fa-warning",
                                    timeout: 4000
                                });
                            } else {
                                getRequest();
                                hideModalOverrideRequest();
                                var msg = $("#msg_request_cancel").val();
                                $.smallBox({
                                    title: msg,
                                    content: $("#smallBox_content").val(),
                                    color: "#739E73",
                                    iconSmall: "fa fa-success",
                                    timeout: 4000
                                });
                            }
                            desbloquearUI();
                        },
                        error: function (jqXHR) {
                            desbloquearUI();
                            alert(" status: " + jqXHR.status + " - " + jqXHR.statusText);
                        }
                    });
            }

            function getRequest() {
                if (parametros.sGetSolicitudesUrl!=null) {
                    $.getJSON(parametros.sGetSolicitudesUrl, {
                        idTomaMx: $("#idTomaMx").val(),
                        contentType: "charset=ISO-8859-1",
                        ajax: 'true'
                    }, function (response) {
                        table.fnClearTable();
                        var len = Object.keys(response).length;
                        for (var i = 0; i < len; i++) {

                            table.fnAddData(
                                [response[i].tipo,response[i].nombre, response[i].fechaSolicitud, response[i].estado,
                                        '<a data-toggle="modal" class="btn btn-danger btn-xs anularSolicitud" data-id=' + response[i].idSolicitud +'><i class="fa fa-times"></i></a>']);

                        }
                        $(".anularSolicitud").on("click", function () {
                            if ($("#codEstadoMx").val()==='ESTDMX|PEND'){
                                $("#idSolicitud").val($(this).data('id'));
                                showModalOverrideRequest();
                            }else{
                                $.smallBox({
                                    title: $('#msg_request_cannotbe_edited').val(),
                                    content:  $('#smallBox_content').val(),
                                    color: "#AF801C",
                                    iconSmall: "fa fa-warning",
                                    timeout: 4000
                                });
                            }

                        });

                        //al paginar se define nuevamente la función de cargar el detalle
                        $(".dataTables_paginate").on('click', function () {
                            $(".anularSolicitud").on('click', function () {
                                if ($("#codEstadoMx").val()==='ESTDMX|PEND'){
                                $("#idSolicitud").val($(this).data('id'));
                                showModalOverrideRequest();
                                }else{
                                    $.smallBox({
                                        title: $('#msg_request_cannotbe_edited').val(),
                                        content:  $('#smallBox_content').val(),
                                        color: "#AF801C",
                                        iconSmall: "fa fa-warning",
                                        timeout: 4000
                                    });
                                }
                            });
                        });
                    }).fail(function (jqXHR) {
                        alert(" status: " + jqXHR.status + " - " + jqXHR.statusText);
                        setTimeout($.unblockUI, 10);
                    });
                }
            }

            function showModalOverrideRequest() {
                $("#causaAnulacion").val('');
                $("#modalOverrideSoli").modal({
                    show: true
                });
            }

            function hideModalOverrideRequest() {
                $('#modalOverrideSoli').modal('hide');
            }

            $("#btnAddDx").click(function () {
                if ($("#codEstadoMx").val()==='ESTDMX|PEND'){
                    if ($("#esEstudio").val() == 'true') {
                        getEstudios($("#idTipoMx").val(), $("#tipoNoti").val());
                    } else {
                        getDiagnosticos($("#idTipoMx").val(), $("#tipoNoti").val());
                    }
                    $("#modalSolicitudes").modal({
                        show: true
                    });
                }else {
                    $.smallBox({
                        title: $('#msg_request_cannotbe_edited').val(),
                        content:  $('#smallBox_content').val(),
                        color: "#AF801C",
                        iconSmall: "fa fa-warning",
                        timeout: 4000
                    });
                }
            });

            $("#back").click(function () {
                window.close();
            });

            <!-- cargar dx -->
            function getDiagnosticos(idTipoMx, codTipoNoti) {
                $.getJSON(parametros.dxUrl, {
                    codMx: idTipoMx, tipoNoti: codTipoNoti,
                    ajax: 'true'
                }, function (data) {
                    var html = null;
                    var len = data.length;
                    html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].diagnostico.idDiagnostico + '">'
                            + data[i].diagnostico.nombre
                            + '</option>';
                    }
                    $('#codDXNuevo').html(html);
                }).fail(function (jqXHR) {
                    setTimeout($.unblockUI, 10);
                    alert(" status: " + jqXHR.status + " - " + jqXHR.statusText);
                });
            }

            <!-- cargar estudios -->
            function getEstudios(idTipoMx, codTipoNoti) {
                if ($('#unidadId').val()!=""){
                    $.getJSON(parametros.sEstudiosURL, {
                        codMx: idTipoMx, tipoNoti: codTipoNoti, idUnidadSalud: $('#unidadId').val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].estudio.idEstudio + '">'
                                + data[i].estudio.nombre
                                + '</option>';
                        }
                        $('#codEstudioNuevo').html(html);
                    })
                        .fail(function (jqXHR) {
                            setTimeout($.unblockUI, 10);
                            alert(" status: " + jqXHR.status + " - " + jqXHR.statusText);
                        });
                }else{
                    $('#codEstudioNuevo').html(html).change("");
                    $.smallBox({
                        title: $('#msgSinUnidadSalud').val(),
                        content:  $('#smallBox_content').val(),
                        color: "#C79121",
                        iconSmall: "fa fa-warning",
                        timeout: 4000
                    });
                }
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
                    /*
                    $('#codSilaisAtencion').val('').change();
                    $('#codUnidadAtencion').val('').change();
                    $('#codMunicipio').val('').change();
                    */
                }else{
                    $('#dSilais').fadeOut('slow');
                }



            });
            getRequest();

            //utilidades
            function unicodeEscape(cadena) {
                var r = /\\u([\d\w]{4})/gi;
                cadena = cadena.replace(r, function (match, grp) {
                    return String.fromCharCode(parseInt(grp, 16));
                });
                return decodeURIComponent(cadena);
            }

        }
    }



}();
