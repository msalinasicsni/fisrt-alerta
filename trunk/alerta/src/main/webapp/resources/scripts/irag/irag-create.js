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

            var tableCond = $('#lista_condiciones').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-10 hidden-xs'i><'col-xs-6 col-sm-6'p>>",
                "autoWidth": true,
                "filter": false,
                "bLengthChange": false,


                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#lista_condiciones'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }

            });


            var tableMani = $('#lista_manifestaciones').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true,
                "filter": false,
                "bLengthChange": false,
                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#lista_manifestaciones'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });



            var idNotificacion = $('#idNotificacion').val();
            if (idNotificacion != null) {

                $('#inCondIdNoti').val(idNotificacion).change();
                $('#inVacIdNoti').val(idNotificacion).change();
                $('#inManifIdNoti').val(idNotificacion).change();

                getVaccines(idNotificacion);
                getConditions(idNotificacion);
                getManifestations(idNotificacion);
            }

            //rango de fecha segun fecha de consulta
            $('#fechaConsulta').change(function() {
                var fecha = $('#fechaConsulta').datepicker("getDate");

                if(fecha != null){
                    $('#fechaPrimeraConsulta').datepicker('setEndDate', fecha);
                    $('#fechaInicioSintomas').datepicker('setEndDate', fecha);
                }


            });


            //filtro de Municipio segun departamento
            $('#departamento').click(function(){
                $('#municipioResidencia option:first').prop("selected", true).change();
                $('#comunidadResidencia option:first').prop("selected", true).change();
                $.getJSON(parametros.municipioByDepaUrl, {
                    departamentoId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = null;
                    html += '<option value="">' + $('#msjSelect').val() + '</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigoNacional + '">'
                            + data[i].nombre
                            + '</option>';
                    }
                    html += '</option>';
                    $('#municipioResidencia').html(html);
                });

            });

            //filtro de comunidad segun municipio
            $('#municipioResidencia').click(function(){
                $('#comunidadResidencia option:first').prop("selected", true).change();
                $.getJSON(parametros.comunidadUrl, {
                    municipioId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = null;
                    html += '<option value="">' + $('#msjSelect').val() + '</option>';
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
                var autorizado = $('#autorizado').val();
                var $valid = $("#wizard-1").valid();

                if(autorizado == "true"){
                    if (!$valid) {
                        $validator.focusInvalid();
                        return false;
                    }else{
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
                    if(autorizado == "true"){
                        completeIrag();
                    }
                }

            });



            function fnGuardarFicha(step) {
                if(step == 2){
                        fnguardar();
//                        fnguardarPersona();

                }

                if (step != 4 && step != 2) {
                        fnguardar();


                }


            }

            function completeIrag(){
                var datos_form = $('#wizard-1').serialize();
                $.ajax({
                    type: "GET",
                    url: parametros.completeIragUrl,
                    data: datos_form,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {

                            $.smallBox({
                            title: "Exito! Se ha completado el Formulario IRAG/ETI!",
                            content:  $('#disappear').val(),
                            color: "#739E73",
                            iconSmall: "fa fa-check-circle",
                            timeout: 2000
                        });

                        window.location.href = parametros.searchIragUrl + '/'+ data.persona.personaId;

                    },
                    error: function () {

                        $.smallBox({
                            title: $('#msjError').val(),
                            content:  $('#disappear').val(),
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
                        $('#inCondIdNoti').val(data.idNotificacion.idNotificacion).change();
                        $('#inManifIdNoti').val(data.idNotificacion.idNotificacion).change();
                        $.smallBox({
                            title: $('#msjSuccessful').val() ,
                            content:  $('#disappear').val(),
                            color: "#739E73",
                            iconSmall: "fa fa-check-circle",
                            timeout: 2000
                        });

                    },
                    error: function () {
//                alert("Error " + result.status + '' + result.statusText);
                        $.smallBox({
                            title: $('#msjErrorSaving').val(),
                            content:  $('#disappear').val(),
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
                            content:  $('#disappear').val(),
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
            function saveVaccine(idNotificacion)
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
                        getVaccines(data.idNotificacion.idNotificacion);

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
                            title : $('#msjErrorSaving').val() ,
                            content :  $('#disappear').val(),
                            color : "#C46A69",
                            iconSmall : "fa fa-warning",
                            timeout : 2000
                        });
                    }
                });
            }

            function getVaccines(idNotificacion) {
                var autorizado = $('#autorizado').val();
                $.getJSON(parametros.vaccines, {
                    idNotificacion: idNotificacion,
                    ajax : 'true'
                }, function(data) {

                    var len = data.length;
                    for ( var i = 0; i < len; i++) {
                        var overrideUrl = parametros.overrideVaccineUrl + '/'+data[i].idVacuna;

                        if(autorizado == "true"){
                            tableVac.fnAddData(
                                [data[i].codVacuna.valor, data[i].codTipoVacuna.valor, data[i].dosis, data[i].fechaUltimaDosis, '<a href='+ overrideUrl+ ' class="btn btn-default btn-xs btn-danger"><i class="fa fa-times"></i></a>']);

                        }else{
                            tableVac.fnAddData(
                                [data[i].codVacuna.valor, data[i].codTipoVacuna.valor, data[i].dosis, data[i].fechaUltimaDosis, '<a href='+ overrideUrl+ ' class="btn btn-default btn-xs btn-danger disabled" ><i class="fa fa-times"></i></a>' ]);

                        }

                    }
                })

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
                         getConditions(data.idNotificacion.idNotificacion);

                        $('#codCondicion option:first').prop("selected", true).change();
                        $("#semanasEmbarazo").val('').change();
                        $("#otraCondicion").val('').change();
                        $("#btnCancelAddCP").click();
                        },

                    error: function () {
                        $.smallBox({
                            title : $('#msjErrorSaving').val(),
                            content :  $('#disappear').val(),
                            color : "#C46A69",
                            iconSmall : "fa fa-warning",
                            timeout : 2000
                        });
                    }
                });
            }

            function getConditions(idNotificacion) {

                var datos_form = $('#wizard-1').serialize();
                console.log(idNotificacion);
                $.getJSON(parametros.conditions, {

                    idNotificacion: idNotificacion,
                    ajax : 'true',
                    data:datos_form
                }, function(data) {
                    var autorizado = $('#autorizado').val();
                    var len = data.length;
                    for ( var i = 0; i < len; i++) {
                        var sem = "";
                        var otraC = "";
                        var overrideUrl = parametros.overrideConditionUrl + '/'+ data[i].idCondicion;

                        if (data[i].semanasEmbarazo != null){
                            sem = data[i].semanasEmbarazo + " " + $('#inPregnancy').val();
                        }

                        if (data[i].otraCondicion != null){
                            otraC = data[i].otraCondicion;
                        }

                        if(autorizado == "true"){
                            tableCond.fnAddData(
                                [data[i].codCondicion.valor + " " + sem + " " + otraC, '<a href='+ overrideUrl+ ' class="btn btn-default btn-xs btn-danger"><i class="fa fa-times"></i></a>']);
                        }else{
                            tableCond.fnAddData(
                                [data[i].codCondicion.valor + " " + sem + " " + otraC, '<a href='+ overrideUrl+ ' class="btn btn-default btn-xs btn-danger disabled"><i class="fa fa-times"></i></a>']);
                        }

                        }

                })

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
                var idNotificacion = $('#inManifIdNoti').val();
                $.ajax({
                    type: "GET",
                    url: parametros.addManifestation,
                    data: datos_manif,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {

                        tableMani.fnClearTable();
                        getManifestations(idNotificacion);

                        $('#codManifestacion option:first').prop("selected", true).change();

                        $("#btnCancelCM").click();
                    },
                    error: function () {
                        $.smallBox({
                            title : $('#msjErrorSaving').val(),
                            content :  $('#disappear').val(),
                            color : "#C46A69",
                            iconSmall : "fa fa-warning",
                            timeout : 2000
                        });
                    }
                });
            }

            function getManifestations(idNotificacion) {
                var datos_form = $('#wizard-1').serialize();
                $.getJSON(parametros.manifestations, {
                    idNotificacion: idNotificacion,
                    ajax : 'true',
                    data: datos_form
                }, function(data) {
                    var autorizado = $('#autorizado').val();
                    var len = data.length;
                    for ( var i = 0; i < len; i++) {
                        var overrideUrl = parametros.overrideManifestationUrl + '/'+ data[i].idManifestacion;
                        var otraMani = "";

                        if(data[i].otraManifestacion != null){
                            otraMani = data[i].otraManifestacion;
                        }

                        if(autorizado == "true"){
                            tableMani.fnAddData(
                                [data[i].codManifestacion.valor + " " + otraMani, '<a href='+ overrideUrl+ ' class="btn btn-default btn-xs btn-danger"><i class="fa fa-times"></i></a>']);

                        }else{
                            tableMani.fnAddData(
                                [data[i].codManifestacion.valor + " " + otraMani, '<a href='+ overrideUrl+ ' class="btn btn-default btn-xs btn-danger disabled"><i class="fa fa-times"></i></a>']);

                        }


                    }
                })
            }


            $('#codResRadiologia').change(function(){
                if($('#codResRadiologia option:selected').text() === "Otros"){
                    $('#sOtroResRadiologia').fadeIn('slow');
                }else{
                    $('#sOtroResRadiologia').fadeOut('slow');
                }

            });

            $('#codClasFCaso').change(function(){
                $('#dNB').fadeOut('slow');
                $('#sNV').fadeOut('slow');


                //limpiar inputs

                var clasi = $('#codClasFCaso').val();
                var nb = "CLASFI|NB";
                var nv = "CLASFI|NV";
                var inad = "CLASFI|INAD";

                if($.inArray(nb,clasi) !==-1){
                    $('#dNB').fadeIn('slow');
                }else{
                    $('#codClasFDetalleNB option:first').prop("selected", true).change();
                    $('#serotipificacion').val('').change();
                    $('#agenteBacteriano').val('').change();
                    $('#dNB').fadeOut('slow');
                }

                if($.inArray(nv,clasi) !==-1){
                    $('#dNV').fadeIn('slow');
                }else{
                    $('#dNV').fadeOut('slow');
                    $('#codClasFDetalleNV option:first').prop("selected", true).change();
                    $('#agenteViral').val('').change();
                }

                if($.inArray(inad,clasi) !==-1){

                    $("#codClasFCaso").find("option[value='CLASFI|NB']").remove();
                    $('#codClasFDetalleNB option:first').prop("selected", true).change();
                    $('#codClasFDetalleNV option:first').prop("selected", true).change();
                    $('#serotipificacion').val('').change();
                    $('#agenteBacteriano').val('').change();
                    $('#agenteViral').val('').change();
                    $("#codClasFCaso option[value='CLASFI|NB']").attr('disabled','disabled');
                    $("#codClasFCaso option[value='CLASFI|NV']").attr('disabled','disabled');
                    $("#codClasFCaso option[value='CLASFI|INAD']").attr('disabled','disabled');
                }else{
                    $("#codClasFCaso option[value='CLASFI|NB']").removeAttr('disabled');
                    $("#codClasFCaso option[value='CLASFI|NV']").removeAttr('disabled');
                    $("#codClasFCaso option[value='CLASFI|INAD']").removeAttr('disabled');
                }

            });




            $('#codClasFDetalleNB').change(function(){

                if($('#codClasFDetalleNB option:selected').text() === "Confirmado NB"){
                    $('#dNBDet').fadeIn('slow');
                }else{
                    $('#dNBDet').fadeOut('slow');
                    $('#serotipificacion').val('').change();
                    $('#agenteBacteriano').val('').change();
                }

            });

            $('#codClasFDetalleNV').change(function(){
                if($('#codClasFDetalleNV option:selected').text() === "Confirmado NV"){
                    $('#sNV').fadeIn('slow');
                }else{
                    $('#sNV').fadeOut('slow');
                    $('#agenteViral').val('').change();
                }


            });

            $('#codClasificacion').change(function(){
                if($('#codClasificacion option:selected').text() === "ETI"){
                    $('#step5').readOnly(true);
                    $('#liStep5').readOnly(true);
                }
            });


            $("#fechaNacimiento").change(
                function() {
                    if ($("#fechaNacimiento").val()!=null && $("#fechaNacimiento").val().length > 0) {
                        $("#edad").val(getAge($("#fechaNacimiento").val()));
                    }else{
                        $("#edad").val('');
                    }
                });

        }
    };

}();
