/**
 * Created by souyen-ics on 10-14-14.
 */
var CreateIrag = function () {

    return {
        init: function (parametros) {

            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet: 1024,
                phone: 480
            };
            var tableVac = $('#lista_vacunas').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-5 col-xs-12 hidden'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true,
                "filter": false,
                "bPaginate": false,


                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#lista_vacunas'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }

            });

            $('#idNotificacion').change(function () {
                var idNotificacion = $('#idNotificacion').val();
                if (idNotificacion != null) {
                    $('#inVacIdNoti').val(idNotificacion).change();
                    getVaccines(idNotificacion);

                }

            });

            //rango de fecha segun fecha de consulta
            $('#fechaConsulta').change(function () {
                var fecha = $('#fechaConsulta').datepicker("getDate");

                if (fecha != null) {
                    $('#fechaPrimeraConsulta').datepicker('setEndDate', fecha);
                    $('#fechaInicioSintomas').datepicker('setEndDate', fecha);
                }


            });

            var wizard = $('#wizard').wizard();

            wizard.on('change', function (e, data) {
                var autorizado = $('#autorizado').val();
                var $valid = $("#wizard-1").valid();

                if (autorizado == "true") {
                    if (!$valid) {
                        $validator.focusInvalid();
                        return false;
                    } else {
                        fnGuardarFicha(data.step);
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
                        completeIrag();
                    }
                }

            });


            function fnGuardarFicha(step) {
                if (step == 2) {
                    fnguardar();

                    fnguardarPersona();
                }

                if (step != 4 && step != 2) {
                    fnguardar();
                }


            }

            function completeIrag() {
                var datos_form = $('#wizard-1').serialize();
                $.ajax({
                    type: "GET",
                    url: parametros.completeIragUrl,
                    data: datos_form,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {

                        $.smallBox({
                            title: $('#msjSuccessful').val(),
                            content: $('#disappear').val(),
                            color: "#739E73",
                            iconSmall: "fa fa-check-circle",
                            timeout: 2000
                        });

                        window.location.href = parametros.searchIragUrl + '/' + data.idNotificacion.persona.personaId;

                    },
                    error: function () {

                        $.smallBox({
                            title: $('#msjError').val(),
                            content: $('#disappear').val(),
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 2000
                        });
                    }
                });
            }

            function fnguardar() {
                var datos_form = $('#wizard-1').serialize();
                $.ajax({
                    type: "GET",
                    url: parametros.sAddIragUrl,
                    data: datos_form,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {
                        $('#idNotificacion').val(data.idNotificacion.idNotificacion).change();
                        $('#inVacIdNoti').val(data.idNotificacion.idNotificacion).change();
                        $.smallBox({
                            title: $('#msjSuccessful').val(),
                            content: $('#disappear').val(),
                            color: "#739E73",
                            iconSmall: "fa fa-check-circle",
                            timeout: 2000
                        });
                    },
                    error: function () {
//                alert("Error " + result.status + '' + result.statusText);
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

            function fnguardarPersona() {
                var datos_form = $('#wizard-1').serialize();

                $.ajax({
                    type: "GET",
                    url: parametros.updatePersonUrl,
                    data: datos_form,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    error: function () {
                        alert("Error " + result.status + '' + result.statusText);
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


            $('#fechaInicioSintomas').change(function () {
                var fecha = $('#fechaInicioSintomas').val();
                var arr = fecha.split('/');
                $('#mesEpi').val(arr[1]);
                $('#anioEpi').val(arr[2]);
                $.ajax({
                    type: "GET",
                    url: parametros.sSemanaEpiUrl,
                    data: {fechaValidar: fecha},
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (response) {
                        $('#semanaEpi').val(response.noSemana);
                    },
                    error: function (result) {
                        $('#semanaEpi').val("");
                    }
                });
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

                    fechaConsulta: {
                        required: true

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
                    },

                    codClasFCaso: {
                        required: true
                    },

                    semanasEmbarazo: {

                        required: function () {
                            var emb = "CONDPRE|EMB";
                            var cond = $('#condiciones').val();
                            return  $.inArray(emb, cond) !== -1
                        }

                    },

                    otraManifestacion: {
                        required: function () {
                            var otra = "MANCLIN|OTRA";
                            var mani = $('#manifestaciones').val();
                            return  $.inArray(otra, mani) !== -1
                        }

                    },

                    otraCondicion: {
                        required: function () {
                            var otra = "CONDPRE|OTRA";
                            var cond = $('#condiciones').val();
                            return  $.inArray(otra, cond) !== -1
                        }

                    },

                    otroResultadoRadiologia: {
                        required: function () {
                            var otroR = "RESRAD|OTROS";
                            var res = $('#codResRadiologia').val();
                            return  $.inArray(otroR, res) !== -1
                        }

                    },

                    agenteBacteriano:{
                        required:function(){
                            return $('#codClasFDetalleNB').find('option:selected').text() === "Confirmado NB";
                        }
                    },

                    serotipificacion:{
                        required:function(){
                            return $('#codClasFDetalleNB').find('option:selected').text() === "Confirmado NB";
                        }
                    },

                    agenteViral:{
                        required:function(){
                            return $('#codClasFDetalleNV').find('option:selected').text() === "Confirmado NV";
                        }
                    },

                    codClasFDetalleNB: {
                        required: function () {
                            var nb = "CLASFI|NB";
                            var clas = $('#codClasFCaso').val();
                            return  $.inArray(nb, clas) !== -1
                        }

                    },

                    codClasFDetalleNV: {
                        required: function () {
                            var nv = "CLASFI|NV";
                            var clas = $('#codClasFCaso').val();
                            return  $.inArray(nv, clas) !== -1
                        }

                    }


                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });

            var codVacuna = $('#codVacuna');
            var hib = $('#tVacHib');
            var flu = $('#tVacFlu');
            var neumo = $('#tVacNeumo');
            var menin = $('#tVacMenin');

            //mostrar tipo de vacuna segun seleccion
            codVacuna.change(function () {
                hib.attr("name", "");
                flu.attr("name", "");
                neumo.attr("name", "");
                menin.attr("name", "");
                hib.val('').change();
                menin.val('').change();
                neumo.val('').change();
                flu.val('').change();

                if (codVacuna.find('option:selected').text() === "Anti Hib") {
                    hib.attr("name", "codTipoVacuna");
                    $('#dVacHib').show();
                    $('#dVacMenin').hide();
                    $('#dVacNeumo').hide();
                    $('#dVacFlu').hide();


                } else if (codVacuna.find('option:selected').text() === "Anti meningocócica") {
                    menin.attr("name", "codTipoVacuna");
                    $('#dVacMenin').show();
                    $('#dVacHib').hide();
                    $('#dVacNeumo').hide();
                    $('#dVacFlu').hide();
                } else if (codVacuna.find('option:selected').text() === "Anti neumococica") {
                    neumo.attr("name", "codTipoVacuna");
                    $('#dVacNeumo').show();
                    $('#dVacMenin').hide();
                    $('#dVacHib').hide();
                    $('#dVacFlu').hide();
                } else if (codVacuna.find('option:selected').text() === "Anti influenza") {
                    flu.attr("name", "codTipoVacuna");
                    $('#dVacFlu').show();
                    $('#dVacNeumo').hide();
                    $('#dVacMenin').hide();
                    $('#dVacHib').hide();

                }
            });


            $('#btnSaveVaccine').click(function () {
                var $validarFormVac = $("#fVaccine").valid();
                if (!$validarFormVac) {
                    $validator2.focusInvalid();
                    return false;
                } else {
                    saveVaccine();

                }

            });


            //validacion modal vacuna
            var $validator2 = $("#fVaccine").validate({
                rules: {
                    codVacuna: {
                        required: true

                    },

                    codTipoVacuna: {
                        required: true

                    }
                },


                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });

            //funcion guardar vacuna
            function saveVaccine(idNotificacion) {
                var datos_vac = $('#fVaccine').serialize();
                $.ajax({
                    type: "GET",
                    url: parametros.sAddVaccineUrl,
                    data: datos_vac,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {

                        getVaccines(data.idNotificacion.idNotificacion.idNotificacion);

                        codVacuna.val('').change();
                        hib.val('').change();
                        menin.val('').change();
                        neumo.val('').change();
                        flu.val('').change();
                        neumo.attr("name", "");
                        hib.attr("name", "");
                        menin.attr("name", "");
                        flu.attr("name", "");
                        $("#dosis").val('');
                        $("#fechaUltimaDosis").val('');
                        $("#btnCancel").click();
                    },
                    error: function (result) {

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

            function getVaccines(idNotificacion) {
                var autorizado = $('#autorizado').val();
                $.getJSON(parametros.vaccines, {
                    idNotificacion: idNotificacion,
                    ajax: 'true'
                }, function (data) {
                    tableVac.fnClearTable();
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        var overrideUrl = parametros.overrideVaccineUrl + '/' + data[i].idVacuna;

                        if (autorizado == "true") {
                            tableVac.fnAddData(
                                [data[i].codVacuna.valor, data[i].codTipoVacuna, data[i].dosis, data[i].fechaUltimaDosis, '<a href=' + overrideUrl + ' class="btn btn-default btn-xs btn-danger"><i class="fa fa-times"></i></a>']);

                        } else {
                            tableVac.fnAddData(
                                [data[i].codVacuna.valor, data[i].codTipoVacuna, data[i].dosis, data[i].fechaUltimaDosis, '<a href=' + overrideUrl + ' class="btn btn-default btn-xs btn-danger disabled" ><i class="fa fa-times"></i></a>' ]);

                        }

                    }
                })

            }

            var sexo = $('#sexo').val();

            if (sexo == "Hombre") {
                $("#condiciones").find("option[value='CONDPRE|EMB']").remove();
            }


            $('#condiciones').change(function () {
                var otra = "CONDPRE|OTRA";
                var emb = "CONDPRE|EMB";
                var cond = $('#condiciones').val();

                if (cond != "") {
                    if ($.inArray(otra, cond) !== -1) {
                        $('#sOtraCond').fadeIn('slow');
                    } else {
                        $('#otraCondicion').val('');
                        $('#sOtraCond').fadeOut('slow');
                    }

                    if ($.inArray(emb, cond) !== -1) {
                        $('#sWeeksP').fadeIn('slow');
                    } else {
                        $('#semanasEmbarazo').val('');
                        $('#sWeeksP').fadeOut('slow');
                    }
                }


            });

            var antib = $('#codAntbUlSem');
            antib.change(function(){
                if (antib.find('option:selected').text() === "Si") {
                    $('#dAntib').fadeIn('slow');
                }else{
                    $('#dAntib').fadeOut('slow');
                    $('#cantidadAntib').val('');
                    $('#nombreAntibiotico').val('');
                    $('#fechaPrimDosisAntib').val('');
                    $('#fechaUltDosisAntib').val('');
                    $('#noDosisAntib').val('');
                    $('#codViaAntb').val('').change();
                }


            });

            var antiv = $('#usoAntivirales');
            antiv.change(function(){
                if (antiv.find('option:selected').text() === "Si") {
                $('#dAntiv').fadeIn('slow');
                }else{
                    $('#dAntiv').fadeOut('slow');
                    $('#nombreAntiviral').val('');
                    $('#fechaPrimDosisAntiviral').val('');
                    $('#fechaUltDosisAntiviral').val('');
                    $('#noDosisAntiviral').val('');

                }
            });


            $('#manifestaciones').change(function () {

                var otra = "MANCLIN|OTRA";
                var mani = $('#manifestaciones').val();

                if (mani != "") {
                    if ($.inArray(otra, mani) !== -1) {
                        $('#sOtraMani').fadeIn('slow');
                    } else {
                        $('#otraManifestacion').val('');
                        $('#sOtraMani').fadeOut('slow');
                    }

                    if ($('#codManifestacion').find('option:selected').text() === "Otra") {
                        $("#dMani").fadeIn('slow');
                    } else {
                        $("#dMani").fadeOut('slow');
                    }
                }

            });


            $('#codResRadiologia').change(function () {

                var res = $('#codResRadiologia').val();
                var otro = "RESRAD|OTROS";

                if (res != "") {
                    if ($.inArray(otro, res) !== -1) {
                        $('#sOtroResRadiologia').fadeIn('slow');
                    } else {
                        $('#sOtroResRadiologia').fadeOut('slow');
                    }
                }
            });

            $('#codClasFCaso').change(function () {
                var clasi = $('#codClasFCaso').val();
                var nb = "CLASFI|NB";
                var nv = "CLASFI|NV";
                var inad = "CLASFI|INAD";

                if (clasi != "") {
                    if ($.inArray(nb, clasi) !== -1) {
                        $('#dNB').fadeIn('slow');
                    } else {
                        $('#codClasFDetalleNB').find('option:first').prop("selected", true).change();
                        $('#serotipificacion').val('').change();
                        $('#agenteBacteriano').val('').change();
                        $('#dNB').fadeOut('slow');
                    }

                    if ($.inArray(nv, clasi) !== -1) {
                        $('#dNV').fadeIn('slow');
                    } else {
                        $('#dNV').fadeOut('slow');
                        $('#codClasFDetalleNV').find('option:first').prop("selected", true).change();
                        $('#agenteViral').val('').change();
                    }

                    if ($.inArray(inad, clasi) !== -1) {
                        $('#codClasFDetalleNB').find('option:first').prop("selected", true).change();
                        $('#codClasFDetalleNV').find('option:first').prop("selected", true).change();
                        $('#serotipificacion').val('').change();
                        $('#agenteBacteriano').val('').change();
                        $('#agenteViral').val('').change();
                        $("#codClasFCaso").find("option[value='CLASFI|NB']").attr('disabled', 'disabled');
                        $("#codClasFCaso").find("option[value='CLASFI|NV']").attr('disabled', 'disabled');

                    } else {
                        $("#codClasFCaso").find("option[value='CLASFI|NB']").removeAttr('disabled');
                        $("#codClasFCaso").find("option[value='CLASFI|NV']").removeAttr('disabled');

                    }
                }

            });


            $('#codClasFDetalleNB').change(function () {

                if ($('#codClasFDetalleNB').val() != "") {
                    if ($('#codClasFDetalleNB').find('option:selected').text() === "Confirmado NB") {
                        $('#dNBDet').fadeIn('slow');
                    } else {
                        $('#dNBDet').fadeOut('slow');
                        $('#serotipificacion').val('').change();
                        $('#agenteBacteriano').val('').change();
                    }
                }

            });

            $('#codClasFDetalleNV').change(function () {
                if ($('#codClasFDetalleNV').val() != "") {
                    if ($('#codClasFDetalleNV').find('option:selected').text() === "Confirmado NV") {
                        $('#sNV').fadeIn('slow');
                    } else {
                        $('#sNV').fadeOut('slow');
                        $('#agenteViral').val('').change();
                    }
                }


            });

            $('#codClasificacion').change(function () {
                if ($('#codClasificacion').find('option:selected').text() === "ETI") {
                    $('#step5').attr('disabled','disabled');

                }
            });

            var fechaNacimiento = $('#fechaNacimiento');

            fechaNacimiento.change(
                function () {
                    if (fechaNacimiento.val() != null && fechaNacimiento.val().length > 0) {
                        $("#edad").val(getAge(fechaNacimiento.val()));
                    } else {
                        $("#edad").val('');
                    }
                });

            var uciS = $('#uciS');
            var uciN = $('#uciN');


            uciS.change(function(){
               if(uciS.is(":checked")){
                   $('#dUci').fadeIn('slow');
               }

            });

            uciN.change(function(){
                if(uciN.is(":checked") ){
                    $('#dUci').fadeOut('slow');
                    $('#noDiasHospitalizado').val('');
                    $('#ventN').prop('checked', false);
                    $('#ventS').prop('checked', false);
                    $('#diagnostico1Egreso').val('').change();
                    $('#diagnostico2Egreso').val('').change();
                    $('#fechaEgreso').val('');
                    $('#codCondEgreso').val('').change();
                }

            });







        /*    $('#diagnostico2Egreso').select2({
                minimumInputLength: 3,
                id: function(enfermedad){ return enfermedad.codigoCie10; },
                ajax: {
                    url: parametros.enfermedadUrl,
                    dataType: 'json',
                    quietMillis: 100,
                    data: function(term, page) {
                        return {
                            filtro: term
                        };
                    },
                    results: function(data, page ) {
                        return {
                            results: data
                        };
                    }
                },
                formatResult: function(enfermedad) {
                    var markup = "<table'><tr>";
                    markup += "<td valign='top'><h5>" + enfermedad.codigoCie10 + "</h5>";
                    markup += "<div>" + enfermedad.nombreCie10 + "</div>";
                    markup += "</td></tr></table>";
                    return markup;
                },
                formatSelection: function(enfermedad) {
                    return enfermedad.nombreCie10;
                },
                dropdownCssClass: "bigdrop",
                initSelection: function (item, callback) {
                    return item;
                },
                escapeMarkup: function (m) { return m; }
            });

            $('#diagnostico1Egreso').select2({
                minimumInputLength: 3,
                id: function(enfermedad){ return enfermedad.codigoCie10; },
                ajax: {
                    url: parametros.enfermedadUrl,
                    dataType: 'json',
                    quietMillis: 100,
                    data: function(term, page) {
                        return {
                            filtro: term
                        };
                    },
                    results: function(data, page ) {
                        return {
                            results: data
                        };
                    }
                },
                formatResult: function(enfermedad) {
                    var markup = "<table'><tr>";
                    markup += "<td valign='top'><h5>" + enfermedad.codigoCie10 + "</h5>";
                    markup += "<div>" + enfermedad.nombreCie10 + "</div>";
                    markup += "</td></tr></table>";
                    return markup;
                },
                formatSelection: function(enfermedad) {
                    return enfermedad.nombreCie10;
                },
                dropdownCssClass: "bigdrop",
                initSelection: function (item, callback) {
                    return item;
                },
                escapeMarkup: function (m) { return m; }
            });*/
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
                console.log(json);
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

        }
    };

}();
