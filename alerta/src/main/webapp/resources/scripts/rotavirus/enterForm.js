var EnterRotavirus = function () {
    var bloquearUI = function (mensaje) {
        var loc = window.location;
        var pathName = loc.pathname.substring(0, loc.pathname.indexOf('/', 1) + 1);
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

    var desbloquearUI = function () {
        setTimeout($.unblockUI, 500);
    };

    return {
        //main function to initiate the module
        init: function (parametros) {

            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet: 1024,
                phone: 480
            };

            var wizard = $('#wizard').wizard();

            wizard.on('change', function (e, data) {
                var autorizado = $('#autorizado').val();
                var $valid = $("#wizard-1").valid();

                if (autorizado == "true") {
                    if (!$valid) {
                        $validator.focusInvalid();
                        return false;
                    } else {
                        save(false);
                        if(data.step == 2){
                            actualizarPersona();
                        }
                    }
                }
            });

            wizard.on('finished', function (e, data) {
                var autorizado = $('#autorizado').val();
                var $valid = $("#wizard-1").valid();
                if (!$valid) {
                    $validator.focusInvalid();
                    return false;
                } else {
                    if (autorizado == "true") {
                        save(true);
                    }else{
                        $.smallBox({
                            title: "Error",
                            content: "No esta autorizado para actualizar la notificación",
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 5000
                        });
                    }
                }
            });

            //Validacion del formulario irag
            var $validator = $("#wizard-1").validate({
                rules: {
                    codSilaisAtencion: {
                        required: true
                    },
                    codUnidadAtencion: {
                        required: true
                    },
                    codMunicipio: {
                        required: true
                    },
                    codExpediente: {
                        required: true
                    }
                    ,
                    codSala: {
                        required: true
                    },
                    fechaIngreso: {
                        required: true
                    },
                    departamento: {
                        required: true
                    },
                    municipioResidencia: {
                        required: true
                    },
                    comunidadResidencia: {
                        required: true
                    },
                    direccionResidencia: {
                        required: true
                    },
                    nombreMadreTutor: {
                        required: true
                    },
                    rbtnCare: {
                        required: true
                    },
                    nombreGuarderia: {
                        required: true
                    },
                    fechaInicioDiarrea: {
                        required: true
                    },
                    fiebre: {
                        required: true
                    },
                    vomito: {
                        required: true
                    },
                    fechaInicioVomito: {
                        required: true
                    },
                    heces: {
                        required: true
                    },
                    otraCaracHeces: {
                        required: true
                    },
                    deshidratacion: {
                        required: true
                    },
                    antibioticoPrevio: {
                        required: true
                    },
                    planinline: {
                        required: true
                    },
                    antHosp: {
                        required: true
                    },
                    nombreAntibiotico: {
                        required: true
                    },
                    uciinline: {
                        required: true
                    },
                    noevacuaciones: {
                        required: true
                    },
                    noDiasUCI: {
                        required: true
                    },
                    uciDiarrea: {
                        required: true
                    },
                    fechaFinDiarrea: {
                        required: function () {
                            return  !($('#checkboxignorado').is(':checked'));
                        }
                    },
                    vacunado: {
                        required: true
                    },
                    registrovacunado: {
                        required: true
                    },
                    muestraHeces: {
                        required: true
                    },
                    codClasFCaso: {
                        required: true
                    },
                    codCondEgreso: {
                        required: true
                    },
                    nombreLlenoFicha : {
                        required: true
                    },
                    nombreTomoMx: {
                        required:function(){
                            var opcionTomoMx = $('#muestraHeces').find('option:selected').val();
                            return opcionTomoMx === "RESP|S";
                        }
                    }
                },
                errorPlacement: function (error, element) {
                    if (element.attr("name") === "uciinline") {
                        $("#dErrorUCI").fadeIn('slow');
                    }else if (element.attr("name") === "planinline") {
                        $("#dErrorPlain").fadeIn('slow');
                    }else if (element.attr("name") === "rbtnCare") {
                        $("#dErrorCare").fadeIn('slow');
                    }else if (element.attr("name") === "uciDiarrea") {
                        $("#dErrorAltaUCI").fadeIn('slow');
                    }else {
                        error.insertAfter(element.parent());
                    }
                }
            });

            /*PARA MOSTRAR TABLA DETALLE RESULTADO*/
            var table1 = $('#lista_resultados').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true, //"T<'clear'>"+
                "columns": [
                    null,null,null,null,null,null,
                    {
                        "className":      'details-control',
                        "orderable":      false,
                        "data":           null,
                        "defaultContent": ''
                    }
                ],
                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#lista_resultados'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });

            function format (d,indice) {
                // `d` is the original data object for the row
                var texto = d[indice]; //indice donde esta el input hidden
                var resultado = $(texto).val();
                var json =JSON.parse(resultado);
                var len = Object.keys(json).length;
                var childTable = '<table style="padding-left:20px;border-collapse: separate;border-spacing:  10px 3px;">'+
                    '<tr><td style="font-weight: bold">'+$('#text_response').val()+'</td><td style="font-weight: bold">'+$('#text_value').val()+'</td><td style="font-weight: bold">'+$('#text_date').val()+'</td></tr>';
                for (var i = 0; i < len; i++) {
                    childTable =childTable +
                        '<tr></tr><tr><td>'+json[i].respuesta+'</td><td>'+json[i].valor+'</td><td>'+json[i].fechaResultado+'</td></tr>';
                }
                childTable = childTable + '</table>';
                return childTable;
            }

            $('#lista_resultados tbody').on('click', 'td.details-control', function () {
                var tr = $(this).closest('tr');
                var row = table1.api().row(tr);
                if ( row.child.isShown() ) {
                    // This row is already open - close it
                    row.child.hide();
                    tr.removeClass('shown');
                }
                else {
                    // Open this row
                    row.child( format(row.data(),6)).show();
                    tr.addClass('shown');
                }
            } );

            function getResultadosAprovados(){
                $.getJSON(parametros.sResultsUrl, {
                    strIdNotificacion: $('#idNotificacion').val(),
                    ajax : 'true'
                }, function(dataToLoad) {
                    table1.fnClearTable();
                    var len = Object.keys(dataToLoad).length;
                    if (len > 0) {
                        for (var i = 0; i < len; i++) {
                            table1.fnAddData(
                                [dataToLoad[i].tipoSolicitud,dataToLoad[i].nombreSolicitud,dataToLoad[i].fechaSolicitud,dataToLoad[i].fechaAprobacion,dataToLoad[i].codigoUnicoMx,
                                    dataToLoad[i].tipoMx, " <input type='hidden' value='"+dataToLoad[i].resultado+"'/>"]);
                        }
                    }
                })
                    .fail(function() {
                        desbloquearUI();
                        alert( "error" );
                    });
            }

            getResultadosAprovados();
            /// FIN MOSTRAR TABLA DETALLE RESULTADO
            //TRATAMIENTO CAMPOS CON DEPENDENCIAS

            $("#fechaNacimiento").change(
                function() {
                    if ($("#fechaNacimiento").val()!=null && $("#fechaNacimiento").val().length > 0) {
                        $("#edad").val(getAge($("#fechaNacimiento").val()));
                    }else{
                        $("#edad").val('');
                    }
                });

            $('#vacunado').change(function () {
               var si = "RESP|S";

                if ($(this).val() != "") {
                    if ($(this).val() != si) {
                        $('#dRegistroV').fadeOut('slow');
                        $('#dTipoVacuna').fadeOut('slow');
                    } else {
                        $('#dRegistroV').fadeIn('slow');
                        $('#dTipoVacuna').fadeIn('slow');
                    }
                }
            });

            $('#vomito').change(function () {
                var si = "RESP|S";

                if ($(this).val() != "") {
                    if ($(this).val() != si) {
                        $('#dNoVomito').fadeOut('slow');
                        $('#dFechaVomito').fadeOut('slow');
                    } else {
                        $('#dNoVomito').fadeIn('slow');
                        $('#dFechaVomito').fadeIn('slow');
                    }
                }
            });

            $('#antHosp').change(function () {
                var si = "RESP|S";

                if ($(this).val() != "") {
                    if ($(this).val() != si) {
                        $('#dAntName').fadeOut('slow');
                    } else {
                        $('#dAntName').fadeIn('slow');
                    }
                }
            });

            $('#heces').change(function () {
                var otra = "CARHECESRT|OTRA";

                if ($(this).val() != "") {
                    if ($(this).val() != otra) {
                        $('#dOtraCaracHeces').fadeOut('slow');
                    } else {
                        $('#dOtraCaracHeces').fadeIn('slow');
                    }
                }
            });

            $("input[name$='checkbox-d1']").change(function () {
                var valor = ($('#checkbox-d1').is(':checked'));
                if (valor)
                    $('#dFechaD1').fadeIn('slow');
                else
                    $('#dFechaD1').fadeOut('slow');
            });

            $("input[name$='checkbox-d2']").change(function () {
                var valor = ($('#checkbox-d2').is(':checked'));
                if (valor)
                    $('#dFechaD2').fadeIn('slow');
                else
                    $('#dFechaD2').fadeOut('slow');
            });

            $("input[name$='checkbox-d3']").change(function () {
                var valor = ($('#checkbox-d3').is(':checked'));
                if (valor)
                    $('#dFechaD3').fadeIn('slow');
                else
                    $('#dFechaD3').fadeOut('slow');
            });

            $("input[name$='rbtnCare']").change(function () {
                var valor = $(this).val();
                if (valor=='true')
                    $('#dCareName').fadeIn('slow');
                else
                    $('#dCareName').fadeOut('slow');
            });

            $("input[name$='uciinline']").change(function () {
                var valor = $(this).val();
                if (valor=='true') {
                    $('#dNoDaysUci').fadeIn('slow');
                    $('#dUciDiarrea').fadeIn('slow');
                }else {
                    $('#dNoDaysUci').fadeOut('slow');
                    $('#dUciDiarrea').fadeOut('slow');
                }
            });

            //guardar
            function save(redirigir) {
                var objetoRT = {};
                objetoRT['idNotificacion'] = $("#idNotificacion").val();

                objetoRT['personaId'] = $("#personaId").val();
                objetoRT['codSilaisAtencion'] = $('#codSilaisAtencion').find('option:selected').val();
                objetoRT['codUnidadAtencion'] = $('#codUnidadAtencion').find('option:selected').val();
                objetoRT['urgente'] = $('#urgente').find('option:selected').val();

                objetoRT['numExpediente'] = $("#codExpediente").val();
                objetoRT['codigo'] = $("#codigo").val();
                objetoRT['nombreTutorAcompana'] = $("#nombreMadreTutor").val();
                objetoRT['enGuarderia'] = $('input[name="rbtnCare"]:checked', '#wizard-1').val();
                objetoRT['nombreGuarderia'] = $("#nombreGuarderia").val();
                objetoRT['fechaInicioDiarrea'] = $("#fechaInicioDiarrea").val();
                objetoRT['noEvacuaciones24Hrs'] = $("#noevacuaciones").val();
                objetoRT['fiebre'] = $('#fiebre').find('option:selected').val();
                objetoRT['vomito'] = $('#vomito').find('option:selected').val();
                objetoRT['noVomito24Hrs'] = $("#novomito").val();
                objetoRT['fechaInicioVomito'] = $("#fechaInicioVomito").val();
                objetoRT['caracteristaHeces'] = $('#heces').find('option:selected').val();
                objetoRT['otraCaracteristicaHeces'] = $("#otraCaracHeces").val();
                objetoRT['gradoDeshidratacion'] = $('#deshidratacion').find('option:selected').val();
                objetoRT['diasHospitalizacion'] = $("#diasHosp").val();
                objetoRT['fechaAlta'] = $("#fechaAlta").val();
                objetoRT['usoAntibioticoPrevio'] = $('#antibioticoPrevio').find('option:selected').val();
                objetoRT['plan'] = $('input[name="planinline"]:checked', '#wizard-1').val();
                objetoRT['antibioticoHospital'] = $('#antHosp').find('option:selected').val();
                objetoRT['cualAntibiotico'] = $("#nombreAntibiotico").val();
                objetoRT['UCI'] = $('input[name="uciinline"]:checked', '#wizard-1').val();
                objetoRT['diasUCI'] = $("#noDiasUCI").val();
                objetoRT['altaUCIDiarrea'] = $('input[name="uciDiarrea"]:checked', '#wizard-1').val();
                objetoRT['fechaTerminoDiarrea'] = $("#fechaFinDiarrea").val();
                objetoRT['ignoradoFTD'] = ($('#checkboxignorado').is(':checked'));
                objetoRT['vacunado'] = $('#vacunado').find('option:selected').val();
                objetoRT['registroVacuna'] = $('#registrovacunado').find('option:selected').val();
                objetoRT['tipoVacunaRotavirus'] = $('#tipoVacuna').find('option:selected').val();
                objetoRT['dosi1'] = ($('#checkbox-d1').is(':checked'));
                objetoRT['fechaAplicacionDosis1'] = $("#fechaD1").val();
                objetoRT['dosi2'] = ($('#checkbox-d2').is(':checked'));
                objetoRT['fechaAplicacionDosis2'] = $("#fechaD2").val();
                objetoRT['dosi3'] = ($('#checkbox-d3').is(':checked'));
                objetoRT['fechaAplicacionDosis3'] = $("#fechaD3").val();
                objetoRT['tomoMuestraHeces'] = $('#muestraHeces').find('option:selected').val();
                objetoRT['clasificacionFinal'] = $('#codClasFCaso').find('option:selected').val();
                objetoRT['condicionEgreso'] = $('#codCondEgreso').find('option:selected').val();
                objetoRT['nombreLlenaFicha'] = $("#nombreLlenoFicha").val();
                objetoRT['epidemiologo'] = $("#epidemiologo").val();
                objetoRT['sala'] = $('#codSala').find('option:selected').val();
                objetoRT['fechaIngreso'] = $("#fechaIngreso").val();
                objetoRT['nombreTomoMx'] = $("#nombreTomoMx").val();
                objetoRT['telefonoTutor'] = $("#telefonoTutor").val();

                objetoRT['mensaje'] = '';
                
                bloquearUI(parametros.blockMess);
                $.ajax({
                    url: parametros.saveUrl,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(objetoRT),
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
                            $('#idNotificacion').val(data.idNotificacion);
                            $.smallBox({
                                title: $('#msjSuccessful').val(),
                                content: $('#disappear').val(),
                                color: "#739E73",
                                iconSmall: "fa fa-check-circle",
                                timeout: 4000
                            });
                            if (redirigir) {
                                setTimeout(function () {
                                 window.location.href = parametros.searchUrl+$("#personaId").val();
                                 }, 4000);
                            }
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

            function actualizarPersona() {
                $.ajax({
                    type: "GET",
                    url: parametros.updatePersonURL,
                    data: {idNotificacion: $("#idNotificacion").val(),
                        personaId: $("#personaId").val(),
                        municipioResidencia: $('#municipioResidencia').find('option:selected').val(),
                        comunidadResidencia: $('#comunidadResidencia').find('option:selected').val(),
                        direccionResidencia:$('#direccionResidencia').val()
                    },
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    error: function () {
                        //alert("Error " + result.status + '' + result.statusText);
                        $.smallBox({
                            title: $('#msjErrorSaving').val(),
                            content: $('#disappear').val(),
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 2000
                        });
                    }
                });
            }

        }
    };

}();
