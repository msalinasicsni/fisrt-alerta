/**
 * Created by souyen-ics on 10-14-14.
 */
var CreateIrag = function () {

    return {
        init: function (parametros) {

            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet : 1024,
                phone : 480
            };
            var tableVac = $('#lista_vacunas').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,
                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#lista_vacunas'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });

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

            //mostrar u ocultar div de vacunas
            $("input[name$='tarjetaVacuna']").click(function () {
                if($(this).val() == 1){
                    $("#sAddVaccine").fadeIn('slow');
                    $("#dVaccinesTable").fadeIn('slow');

                }else{
                    $("#sAddVaccine").fadeOut('slow');
                    $("#dVaccinesTable").fadeOut('slow');
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

            //mostrar tipo de vacuna segun seleccion
            $('#codVacuna').change(function() {
            $('#codTipoVacuna option:first').prop("selected", true).change();

                if ($('#codVacuna option:selected').text() === "Anti Hib"){
                    $('#tVacHib').attr("name", "codTipoVacuna");
                    $('#dVacHib').show();
                    $('#dVacMenin').hide();
                    $('#dVacNeumo').hide();
                    $('#dVacFlu').hide();
                } else if ($('#codVacuna option:selected').text() === "Anti meningocócica"){
                    $('#tVacMenin').attr("name", "codTipoVacuna");
                    $('#dVacMenin').show();
                    $('#dVacHib').hide();
                    $('#dVacNeumo').hide();
                    $('#dVacFlu').hide();
                } else if ($('#codVacuna option:selected').text() === "Anti neumococica"){
                    $('#tVacNeumo').attr("name", "codTipoVacuna");
                    $('#dVacNeumo').show();
                    $('#dVacMenin').hide();
                    $('#dVacHib').hide();
                    $('#dVacFlu').hide();
                }else if ($('#codVacuna option:selected').text() === "Anti influenza"){
                    $('#tVacFlu').attr("name", "codTipoVacuna");
                    $('#dVacFlu').show();
                    $('#dVacNeumo').hide();
                    $('#dVacMenin').hide();
                    $('#dVacHib').hide();

                }
            });



            $('#btnSaveVaccine').click(function() {
                var $validarFormVac = $("#fVaccine").valid();
                if (!$validarFormVac) {
                    $validator2.focusInvalid();
                    return false;
                }else{
                    saveVaccine();

                }

            });


            //validacion modal vacuna
            var $validator2 = $("#fVaccine").validate({
                rules: {
                    codVacuna: {
                        required: true

                    },
                    codAplicada: {
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
            function saveVaccine()
            {
                var datos_vac = $('#fVaccine').serialize();


                $.ajax({
                    type: "GET",
                    url: parametros.sAddVaccineUrl,
                    data: datos_vac,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {
                        tableVac.fnClearTable();

                        getVaccines();

                        $('#codVacuna option:first').prop("selected", true).change();
                        $('#codAplicada option:first').prop("selected", true).change();
                        $('#tVacHib option:first').prop("selected", true).change();
                        $('#tVacMenin option:first').prop("selected", true).change();
                        $('#tVacNeumo option:first').prop("selected", true).change();
                        $('#tVacFlu option:first').prop("selected", true).change();
                        $('#tVacNeumo').attr("name", "");
                        $('#tVacHib').attr("name", "");
                        $('#tVacMenin').attr("name", "");
                        $('#tVacFlu').attr("name", "");
                        $("#dosis").val('');
                        $("#fechaUltimaDosis").val('');
                        $("#btnCancel").click();
                    },
                    error: function (result) {
                      $.smallBox({
                            title : "Error! Los datos no se han guardado correctamente!",
                            content : "Este mensaje desaparecerá en 2 segundos!",
                            color : "#C46A69",
                            iconSmall : "fa fa-warning",
                            timeout : 2000
                        });
                    }
                });
            }

            function getVaccines() {

                $.getJSON(parametros.vaccines, {
                    ajax : 'true'
                }, function(data) {
                    var len = data.length;
                    console.log(data);
                    for ( var i = 0; i < len; i++) {
                        tableVac.fnAddData(
                            [data[i].codVacuna.valor, data[i].codAplicada.valor, data[i].codTipoVacuna.valor, data[i].dosis, data[i].fechaUltimaDosis]);

                    }
                })
                    .fail(function() {
                        alert( "error" );

                    });
            }



        }
    };

}();
