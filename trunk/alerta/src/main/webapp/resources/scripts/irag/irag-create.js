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


            var idNotificacion = $('#idNotificacion').val();
            if (idNotificacion != null) {
                $('#inVacIdNoti').val(idNotificacion).change();
                getVaccines(idNotificacion);

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
                $('#municipioResidencia').find('option:first').prop("selected", true).change();
                $('#comunidadResidencia').find('option:first').prop("selected", true).change();
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
                $('#comunidadResidencia').find('option:first').prop("selected", true).change();
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
                if (step == 2) {
                    fnguardar();

                    fnguardarPersona();
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

                        window.location.href = parametros.searchIragUrl + '/'+ data.idNotificacion.persona.personaId;

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
                    },

                    semanasEmbarazo:{

                        required:function(){var emb = "CONDPRE|EMB"; var cond = $('#condiciones').val();  return  $.inArray(emb,cond) !==-1}

                    },

                    otraManifestacion:{
                        required:function(){var otra = "MANCLIN|OTRA"; var mani = $('#manifestaciones').val();  return  $.inArray(otra,mani) !==-1}

                    },

                    otraCondicion:{
                        required:function(){var otra = "CONDPRE|OTRA"; var cond = $('#condiciones').val();  return  $.inArray(otra,cond) !==-1}

                    },

                    otroResultadoRadiologia:{
                        required:function(){var otroR = "RESRAD|OTROS"; var res = $('#codResRadiologia').val();  return  $.inArray(otroR,res) !==-1}

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
                $('#tVacHib').find('option:first').prop("selected", true).change();
                $('#tVacMenin').find('option:first').prop("selected", true).change();
                $('#tVacNeumo').find('option:first').prop("selected", true).change();
                $('#tVacFlu').find('option:first').prop("selected", true).change();

                if ($('#codVacuna').find('option:selected').text() === "Anti Hib"){
                    $('#tVacHib').attr("name", "codTipoVacuna");
                    $('#dVacHib').show();
                    $('#dVacMenin').hide();
                    $('#dVacNeumo').hide();
                    $('#dVacFlu').hide();


                } else if ($('#codVacuna').find('option:selected').text() === "Anti meningocócica"){
                    $('#tVacMenin').attr("name", "codTipoVacuna");
                    $('#dVacMenin').show();
                    $('#dVacHib').hide();
                    $('#dVacNeumo').hide();
                    $('#dVacFlu').hide();
                } else if ($('#codVacuna').find('option:selected').text() === "Anti neumococica"){
                    $('#tVacNeumo').attr("name", "codTipoVacuna");
                    $('#dVacNeumo').show();
                    $('#dVacMenin').hide();
                    $('#dVacHib').hide();
                    $('#dVacFlu').hide();
                }else if ($('#codVacuna').find('option:selected').text() === "Anti influenza"){
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

                        $('#codVacuna').find('option:first').prop("selected", true).change();
                        $('#tVacHib').find('option:first').prop("selected", true).change();
                        $("#tVacMenin").find("option:first").prop("selected", true).change();
                        $('#tVacNeumo').find('option:first').prop("selected", true).change();
                        $('#tVacFlu').find('option:first').prop("selected", true).change();
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

            var sexo = $('#sexo').val();

            if (sexo == "Hombre") {
                $("#condiciones").find("option[value='CONDPRE|EMB']").remove();
            }



            $('#condiciones').change(function(){
                var otra = "CONDPRE|OTRA";
                var emb = "CONDPRE|EMB";
                var cond = $('#condiciones').val();

                if(cond != ""){
                    if($.inArray(otra,cond) !==-1){
                        $('#sOtraCond').fadeIn('slow');
                    }else{
                        $('#otraCondicion').val('');
                        $('#sOtraCond').fadeOut('slow');
                    }

                    if($.inArray(emb,cond) !==-1){
                        $('#sWeeksP').fadeIn('slow');
                    }else{
                        $('#semanasEmbarazo').val('');
                        $('#sWeeksP').fadeOut('slow');
                    }
                }


            });


            $('#manifestaciones').change(function(){

                var otra = "MANCLIN|OTRA";
                var mani = $('#manifestaciones').val();

                if(mani != ""){
                    if($.inArray(otra,mani) !==-1){
                        $('#sOtraMani').fadeIn('slow');
                    }else{
                        $('#otraManifestacion').val('');
                        $('#sOtraMani').fadeOut('slow');
                    }

                    if ($('#codManifestacion').find('option:selected').text() === "Otra" ){
                        $("#dMani").fadeIn('slow');
                    }else{
                        $("#dMani").fadeOut('slow');
                    }
                }

            });


            $('#codResRadiologia').change(function(){

                var res = $('#codResRadiologia').val();
                var otro = "RESRAD|OTROS";

                if(res != ""){
                    if($.inArray(otro,res) !==-1){
                        $('#sOtroResRadiologia').fadeIn('slow');
                    }else{
                        $('#sOtroResRadiologia').fadeOut('slow');
                    }

                }


            });

            $('#codClasFCaso').change(function(){
                var clasi = $('#codClasFCaso').val();
                var nb = "CLASFI|NB";
                var nv = "CLASFI|NV";
                var inad = "CLASFI|INAD";

                if(clasi != ""){
                    if($.inArray(nb,clasi) !==-1){
                        $('#dNB').fadeIn('slow');
                    }else{
                        $('#codClasFDetalleNB').find('option:first').prop("selected", true).change();
                        $('#serotipificacion').val('').change();
                        $('#agenteBacteriano').val('').change();
                        $('#dNB').fadeOut('slow');
                    }

                    if($.inArray(nv,clasi) !==-1){
                        $('#dNV').fadeIn('slow');
                    }else{
                        $('#dNV').fadeOut('slow');
                        $('#codClasFDetalleNV').find('option:first').prop("selected", true).change();
                        $('#agenteViral').val('').change();
                    }

                    if($.inArray(inad,clasi) !==-1){
                        $('#codClasFDetalleNB').find('option:first').prop("selected", true).change();
                        $('#codClasFDetalleNV').find('option:first').prop("selected", true).change();
                        $('#serotipificacion').val('').change();
                        $('#agenteBacteriano').val('').change();
                        $('#agenteViral').val('').change();
                        $("#codClasFCaso").find("option[value='CLASFI|NB']").attr('disabled','disabled');
                        $("#codClasFCaso").find("option[value='CLASFI|NV']").attr('disabled','disabled');

                    }else{
                        $("#codClasFCaso").find("option[value='CLASFI|NB']").removeAttr('disabled');
                        $("#codClasFCaso").find("option[value='CLASFI|NV']").removeAttr('disabled');

                    }
                }



            });




            $('#codClasFDetalleNB').change(function(){

                if($('#codClasFDetalleNB').val() != ""){
                    if($('#codClasFDetalleNB').find('option:selected').text() === "Confirmado NB"){
                        $('#dNBDet').fadeIn('slow');
                    }else{
                        $('#dNBDet').fadeOut('slow');
                        $('#serotipificacion').val('').change();
                        $('#agenteBacteriano').val('').change();
                    }
                }

            });

            $('#codClasFDetalleNV').change(function(){
                if($('#codClasFDetalleNV').val() != ""){
                    if($('#codClasFDetalleNV').find('option:selected').text() === "Confirmado NV"){
                        $('#sNV').fadeIn('slow');
                    }else{
                        $('#sNV').fadeOut('slow');
                        $('#agenteViral').val('').change();
                    }
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
