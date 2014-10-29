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
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,
                "filter": false,
                "bPaginate": false,


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

            getVaccines();

            var tableCond = $('#lista_condiciones').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,
                "filter": false,
                "bLengthChange": false,


                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#lista_condiciones'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });

            getConditions();

            var tableMani = $('#lista_manifestaciones').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,
                "filter": false,
                "bLengthChange": false,
                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#lista_manifestaciones'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });
            getManifestations();

            $('#fechaConsulta').change(function() {
                var fecha = $('#fechaConsulta').datepicker("getDate");
                $('#fechaPrimeraConsulta').datepicker('setEndDate', fecha);
                $('#fechaInicioSintomas').datepicker('setEndDate', fecha);

            });






            //Filtro Unidad de Salud segun Silais seleccionado
            $('#codMunicipio').click(function () {
                $('#codUnidadAtencion option:first').prop("selected", true).change();
                $.getJSON(parametros.unidadesUrl, {
                    codMunicipio: $(this).val(),
                    codSilais: $('#codSilaisAtencion').val(),
                    ajax: 'true'
                }, function (data) {
                    var html = null;
                    var len = data.length;
                    html += '<option value="">Seleccione...</option>';
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigo + '">'
                            + data[i].nombre
                            + '</option>';

                    }
                    $('#codUnidadAtencion').html(html);

                })



            });

            <!-- Filtro de Municipio segun silais seleccionado  -->

                $('#codSilaisAtencion').change(function () {
                    $('#codMunicipio option:first').prop("selected", true).change();

                    $.getJSON(parametros.municipiosUrl, {
                        idSilais: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                         html += '<option value="">Seleccione...</option>';
                        var len = data.length;
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigoNacional + '">'
                                + data[i].nombre
                                + '</option>';
                        }
                        html += '</option>';
                        $('#codMunicipio').html(html);
                    });

                });




            //filtro de Municipio segun departamento
            $('#departamento').change(function(){

                $.getJSON(parametros.municipioByDepaUrl, {
                    departamentoId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = '<option value="">Seleccione...</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigoNacional + '">'
                            + data[i].nombre
                            + '</option>';
                    }
                    html += '</option>';
                    $('#municipioResidencia').html(html);
                });
                $("#municipioResidencia").val("${muniResidencia.codigoNacional}").change();

            });

            //filtro de comunidad segun municipio
            $('#municipioResidencia').change(function(){

                $.getJSON(parametros.comunidadUrl, {
                    municipioId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = '<option value="">Seleccione...</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigo + '">'
                            + data[i].nombre
                            + '</option>';
                    }
                    html += '</option>';
                    $('#comunidadResidencia').html(html);
                })
            });

            var wizard = $('#wizard').wizard();

            wizard.on('change', function (e, data) {
                var $valid = $("#wizard-1").valid();
                if (!$valid) {
                    $validator.focusInvalid();
                    return false;
                } else {

                    fnGuardarFicha(data.step);
                }

            });

            wizard.on('finished', function (e, data) {
                var $valid = $("#wizard-1").valid();
                if (!$valid) {
                    $validator.focusInvalid();
                    return false;
                } else {
                    fnGuardarFicha(6);
                }

            });

            function fnGuardarFicha(step) {

                if (step != 4) {
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

                    codClasFCaso:{
                        required:true
                    }

                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });

            //mostrar tipo de vacuna segun seleccion
            $('#codVacuna').change(function() {
                $('#tVacHib').attr("name", "");
                $('#tVacFlu').attr("name", "");
                $('#tVacNeumo').attr("name", "");
                $('#tVacMenin').attr("name", "");
                $('#tVacHib option:first').prop("selected", true).change();
                $('#tVacMenin option:first').prop("selected", true).change();
                $('#tVacNeumo option:first').prop("selected", true).change();
                $('#tVacFlu option:first').prop("selected", true).change();

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
                            [data[i].codVacuna.valor, data[i].codTipoVacuna.valor, data[i].dosis, data[i].fechaUltimaDosis]);

                    }
                })
                    .fail(function() {
                        alert( "error" );

                    });
            }

            //accion guardar condicion preexistente
            $('#btnSaveCondPre').click(function() {
                var $validarFormCondPre = $("#fCondPre").valid();
                if (!$validarFormCondPre) {
                    $validator3.focusInvalid();
                    return false;
                }else{
                    savePreexistingCondition();

                }

            });

            //validacion requeridos para condiciones preexistentes
            var $validator3 = $("#fCondPre").validate({
                rules: {
                    codCondicion: {
                        required: true

                    }
                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });

            $('#codCondicion').change(function(){
                $('#otraCondicion').val('').change();
                $('#semanasEmbarazo').val('').change();
                $("#dSemEmb").hide();
                $("#dOtraCondicion").hide();

                //mostrar u ocultar dic segun seleccion
                if ($('#codCondicion option:selected').text() === "Embarazo" ){
                    $("#dSemEmb").fadeIn('slow');
                }else{
                    $("#dSemEmb").fadeOut('slow');
                }

                if ($('#codCondicion option:selected').text() === "Otra" ){
                    $("#dOtraCondicion").fadeIn('slow');
                }else{
                    $("#dOtraCondicion").fadeOut('slow');
                }
            });


            //guardar condicion preexistente
            function savePreexistingCondition()
            {
                var datos_cond = $('#fCondPre').serialize();

                $.ajax({
                    type: "GET",
                    url: parametros.addCondition,
                    data: datos_cond,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function  (data) {
                        tableCond.fnClearTable();
                         getConditions();

                        $('#codCondicion option:first').prop("selected", true).change();
                        $("#semanasEmbarazo").val('').change();
                        $("#otraCondicion").val('').change();
                        $("#btnCancelAddCP").click();
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

            function getConditions() {

                $.getJSON(parametros.conditions, {
                    ajax : 'true'
                }, function(data) {

                    var len = data.length;
                    console.log(data);
                    for ( var i = 0; i < len; i++) {
                        var sem = "";
                        var otraC = "";

                        if (data[i].semanasEmbarazo != null){
                            sem = data[i].semanasEmbarazo + " " + $('#inPregnancy').val();
                        }

                        if (data[i].otraCondicion != null){
                            otraC = data[i].otraCondicion;
                        }
                            tableCond.fnAddData(
                                [data[i].codCondicion.valor + " " + sem + " " + otraC]);
                        }

                })
                    .fail(function() {
                        alert( "error" );

                    });
            }

            $('#btnSaveCM').click(function() {
                var $validarFormCM = $("#fCM").valid();
                if (!$validarFormCM) {
                    $validator4.focusInvalid();
                    return false;
                }else{
                    saveCM();

                }

            });

            var $validator4 = $("#fCM").validate({
                rules: {
                    codManifestacion: {
                        required: true

                    }

                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });

            $('#codManifestacion').change(function(){
                $('#otraManifestacion').val('').change();

                if ($('#codManifestacion option:selected').text() === "Otra" ){
                    $("#dMani").fadeIn('slow');
                }else{
                    $("#dMani").fadeOut('slow');
                }
            });

            function saveCM()
            {
                var datos_manif = $('#fCM').serialize();

                $.ajax({
                    type: "GET",
                    url: parametros.addManifestation,
                    data: datos_manif,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (response) {

                        tableMani.fnClearTable();
                        getManifestations();

                        $('#codManifestacion option:first').prop("selected", true).change();

                        $("#btnCancelCM").click();
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



            function getManifestations() {

                $.getJSON(parametros.manifestations, {
                    ajax : 'true'
                }, function(data) {
                    var len = data.length;
                    console.log(data);
                    for ( var i = 0; i < len; i++) {
                        var otraMani = "";

                        if(data[i].otraManifestacion != null){
                            otraMani = data[i].otraManifestacion;
                        }

                        tableMani.fnAddData(
                            [data[i].codManifestacion.valor + " " + otraMani]);

                    }
                })
                    .fail(function() {
                        alert( "error" );

                    });
            }


            $('#codResRadiologia').change(function(){
                if($('#codResRadiologia option:selected').text() === "Otros"){
                    $('#sOtroResRadiologia').fadeIn('slow');
                }else{
                    $('#sOtroResRadiologia').fadeOut('slow');
                }

            });

            $('#codClasFCaso').change(function(){
                if($('#codClasFCaso option:selected').val() != ""){
                    $('#fichaCompleta').val('1').change();
                }else{
                    $('#fichaCompleta').val('0').change();
                }

                if($('#codClasFCaso option:selected').text() === "Confirmado NB"){
                    $('#dConfNB').fadeIn('slow');
                }else if($('#codClasFCaso option:selected').text() === "Confirmado NV"){
                    $('#dViralEti').fadeIn('slow');
                }else if($('#codClasFCaso option:selected').text() === "Coinfección Bacteriana y Viral"){
                    $('#dEtiAgents').fadeIn('slow');
                }else{
                    $('#dConfNB').fadeOut('slow');
                    $('#dViralEti').fadeOut('slow');
                    $('#dEtiAgents').fadeOut('slow');
                }

            });

        }
    };

}();
