var EnterNotificationD = function () {
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
                tablet : 1024,
                phone : 480
            };
            var table1 = $('#dtNotiD').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,
                "columns": [
                    null, null, null,

                    {
                        "className": 'edit',
                        "orderable": false
                    },

                    {
                        "className": 'delete',
                        "orderable": false
                    }

                ],


                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#dtNotiD'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                },

                fnDrawCallback: function () {
                    $('.edit')
                        .off("click", editNoti)
                        .on("click", editNoti);

                    $('.delete')
                        .off("click", deleteEvent)
                        .on("click", deleteEvent);

                    responsiveHelper_dt_basic.respond();
                }
            });

            loadEvents();

            var anterior;
            var actual;



                $('#notidate').change(function () {
                    var fecha = $('#notidate').val();
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



            //suma de totales masculino
            function totalMale(anterior, actual){
               var total = $('#totalMasc');

                var suma = total.val();

                suma =  (parseInt(suma)  + parseInt(actual))  - parseInt(anterior) ;

                total.val(suma);

            }


            //suma de totales femenino
            function totalFem(anterior, actual){
                var total = $('#totalFem');

                var suma = total.val();

                suma =  (parseInt(suma)  + parseInt(actual))  - parseInt(anterior) ;

                total.val(suma);

            }

            $('.masculino').focus(function() {
               anterior = $(this).val();
                if(anterior == ""){
                    anterior = 0
                }
            }).change(function() {
               actual = $(this).val();
                if (actual == ""){
                    actual = 0;
                }
                totalMale(anterior, actual);
            });


            $('.femenino').focus(function() {
                anterior = $(this).val();
                if(anterior == ""){
                    anterior = 0
                }
            }).change(function() {
                actual = $(this).val();
                if (actual == ""){
                    actual = 0;
                }
                totalFem(anterior, actual);
            });


            //Validacion del formulario irag
            var $validator = $("#create-form").validate({
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

                    notidate: {
                        required: true
                    },
                    codPato: {
                        required: true
                    }

                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                },
                submitHandler: function (form) {
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    save();
                }
            });


            function save() {
                var obj = {};

                obj['codSilaisAtencion'] = $('#codSilaisAtencion option:selected').val();
                obj['codMunicipio'] = $('#codMunicipio option:selected').val();
                obj['codUnidadAtencion'] = $('#codUnidadAtencion option:selected').val();
                obj['notidate'] = $("#notidate").val();
                obj['semanaEpi'] = $("#semanaEpi").val();
                obj['codPato'] = $('#codPato option:selected').val();
                obj['masc0'] = $("#masc0").val();
                obj['fem0'] = $("#fem0").val();
                obj['masc1'] = $("#masc1").val();
                obj['fem1'] = $("#fem1").val();
                obj['masc2'] = $("#masc2").val();
                obj['fem2'] = $("#fem2").val();
                obj['masc3'] = $("#masc3").val();
                obj['fem3'] = $("#fem3").val();
                obj['masc4'] = $("#masc4").val();
                obj['fem4'] = $("#fem4").val();
                obj['masc5'] = $("#masc5").val();
                obj['fem5'] = $("#fem5").val();
                obj['totalMasc'] = $("#totalMasc").val();
                obj['masc6'] = $("#masc6").val();
                obj['fem6'] = $("#fem6").val();
                obj['masc7'] = $("#masc7").val();
                obj['fem7'] = $("#fem7").val();
                obj['totalFem'] = $("#totalFem").val();
                obj['masc8'] = $("#masc8").val();
                obj['fem8'] = $("#fem8").val();
                obj['masc9'] = $("#masc9").val();
                obj['fem9'] = $("#fem9").val();
                obj['masc10'] = $("#masc10").val();
                obj['fem10'] = $("#fem10").val();
                obj['masc11'] = $("#masc11").val();
                obj['fem11'] = $("#fem11").val();
                obj['masc12'] = $("#masc12").val();
                obj['fem12'] = $("#fem12").val();
                obj['masc13'] = $("#masc13").val();
                obj['fem13'] = $("#fem13").val();
                obj['mensaje'] = '';

                bloquearUI(parametros.blockMess);
                $.ajax({
                    url: parametros.saveUrl,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(obj),
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

                            getNotiDetails(data.silais, data.municipio, data.unidad, data.fecha);

                            clean1();
                            $.smallBox({
                                title: $('#msjSuccessful').val(),
                                content: $('#disappear').val(),
                                color: "#739E73",
                                iconSmall: "fa fa-check-circle",
                                timeout: 4000
                            });


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

            function getNotiDetails(silais, municipio, unidad, fecha) {

                $.getJSON(parametros.detailsUrl, {
                    silais: silais,
                    municipio: municipio,
                    unidad: unidad,
                    fecha: fecha,
                    ajax : 'true'
                }, function(response) {
                    table1.fnClearTable();
                    var len = response.length;
                    for ( var i = 0; i < len; i++) {

                        var btnEdit = '<button title="Editar" type="button" class="btn btn-primary btn-xs" data-id="' + response[i].silais + "," + response[i].municipio   + "," + response[i].unidad.codigo  + "," + response[i].fechaNotificacion  + "," + response[i].patologia.codigo +
                            '" > <i class="fa fa-edit"></i>';

                        var btnDelete = '<button title="Anular" type="button" class="btn btn-danger btn-xs" data-id="' + response[i].silais + "," + response[i].municipio   + "," + response[i].unidad.codigo  + "," + response[i].fechaNotificacion  + "," + response[i].patologia.codigo +
                            '" > <i class="fa fa-times"></i>';

                        table1.fnAddData(
                            [response[i].patologia.codigo + " - " + response[i].patologia.nombre, response[i].totalm,response[i].totalf, btnEdit, btnDelete]);
                    }

                })
                    .fail(function() {
                        alert( "error" );

                    });
            }



            $('#btnCancelar').click(function(){
                clean1();
                $('#dBtnSave').show();
                $('#dBtnEdit').hide();
                $('#codPato').removeAttr('disabled');
                $('#action').val('');
            });


            function clean() {
                $('#notidate').val(null);
                $('.masculino').val('');
                $('.femenino').val('');
                $('.sel').val(null).change();
                $('#totalMasc').val(0);
                $('#totalFem').val(0);
                $('#semanaEpi').val('');


            }

            function clean1() {
                $('.masculino').val('');
                $('.femenino').val('');
                $('#totalMasc').val(0);
                $('#totalFem').val(0);
                $('#codPato').val('').change();


            }

            $("#btnNueva").click(function(){
                window.location.href = parametros.createUrl;
            });

            $('#codPato').change(function(){
                var pato = $(this).val();
                var silais = $('#codSilaisAtencion').val();
                var municipio = $('#codMunicipio').val();
                var unidad = $('#codUnidadAtencion').val();
                var fecha = $('#notidate').val();

                if (pato != ''){

                    //Obtener grupo de edades y sexo de patologia seleccionada
                    $.getJSON(parametros.patoUrl, {
                        pato: pato,
                        ajax : 'true'
                    }, function(response) {
                        if (!response.empty) {

                            //obtener grupos de edades
                            var grupos = response.gruposEdades;
                            var sexo = response.sexo;
                            var masc = $('.masculino');
                            var fem = $('.femenino');
                            var grupo = 'g_';
                            var id = 0;


                            //deshabilitar input segun sexo
                            if (sexo != null) {
                                var sexo1 = sexo.toUpperCase();
                                if (sexo1 == 'F') {
                                    masc.val('');
                                    masc.attr('disabled', 'disabled');

                                    //deshabilitar input segun grupo de edad
                                    if (grupos != null) {

                                        for (var i = 1; i <= 14; i++) {
                                            var idFe = '#' + 'fem' + id;

                                            if (i == 1) {
                                                if (grupos.indexOf(grupo + i + ',') == -1) {
                                                    $(idFe).val('');
                                                    $(idFe).attr('disabled', 'disabled');

                                                } else {
                                                    $(idFe).removeAttr('disabled');
                                                }
                                            } else {
                                                if (grupos.indexOf(grupo + i) == -1) {
                                                    $(idFe).val('');
                                                    $(idFe).attr('disabled', 'disabled');

                                                } else {
                                                    $(idFe).removeAttr('disabled');
                                                }
                                            }
                                            id++
                                        }

                                    }

                                } else if (sexo1 == 'M') {
                                    fem.val('');
                                    fem.attr('disabled', 'disabled');

                                    if(grupos != null){

                                        for (var j = 1; j <=14; j++){
                                            var idMa =  '#' + 'masc' + id;


                                            if (j == 1) {
                                                if (grupos.indexOf(grupo + j + ',') == -1) {
                                                    $(idMa).val('');
                                                    $(idMa).attr('disabled', 'disabled');

                                                } else {
                                                    $(idMa).removeAttr('disabled');
                                                }
                                            }else{
                                                if(grupos.indexOf(grupo +j) == -1){
                                                    $(idMa).val('');
                                                    $(idMa).attr('disabled', 'disabled');

                                                }else{
                                                    $(idMa).removeAttr('disabled');
                                                }
                                            }

                                          id++
                                        }

                                    }
                                }
                            } else {
                                masc.removeAttr('disabled');
                                fem.removeAttr('disabled');

                                //deshabilitar input segun grupo de edad
                                if (grupos != null) {

                                    for (var k = 1; k <= 14; k++) {
                                        var idF = '#' + 'masc' + id;
                                        var idM = '#' + 'fem' + id;

                                        if (k == 1) {
                                            if (grupos.indexOf(grupo + k + ',') == -1) {
                                                $(idM).val('');
                                                $(idM).attr('disabled', 'disabled');
                                                $(idF).val('');
                                                $(idF).attr('disabled', 'disabled');

                                            } else {
                                                $(idM).removeAttr('disabled');
                                                $(idF).removeAttr('disabled');
                                            }
                                        }else{
                                            if (grupos.indexOf(grupo + k) == -1) {
                                                $(idF).val('');
                                                $(idF).attr('disabled', 'disabled');
                                                $(idM).val('');
                                                $(idM).attr('disabled', 'disabled');
                                            } else {
                                                $(idF).removeAttr('disabled');
                                                $(idM).removeAttr('disabled');
                                            }
                                        }

                                        id++
                                    }

                                }


                            }

                        }

                    });


                    if (silais != '' && municipio != '' && unidad != '' && fecha != ''){
                        //Buscar registros del mismo evento
                        var action = $('#action').val();
                        if(action != "edit"){
                            $.getJSON(parametros.duplicateUrl, {
                                silais: silais,
                                municipio: municipio,
                                unidad: unidad,
                                fecha: fecha,
                                pato: pato,
                                ajax : 'true'
                            }, function(response) {
                                var len = response.length;

                                if (len > 0){
                                    $.smallBox({
                                        title: $("#msjduplicate").val(),
                                        content: $("#disappear").val(),
                                        color: "#C46A69",
                                        iconSmall: "fa fa-warning",
                                        timeout: 5000
                                    });

                                    $('#btnSave').attr('disabled', 'disabled');
                                }else{
                                    $('#btnSave').removeAttr('disabled');
                                }

                            })

                        }



                    }

                }

            });

            function editNoti() {
                var data = $(this.innerHTML).data('id');

                if(data != null){
                    var detalle = data.split(",");
                    var silais= detalle[0];
                    var municipio = detalle[1];
                    var unidad = detalle[2];
                    var fecha = detalle[3];
                    var pato = detalle[4];

                    getNotiToEdit(silais,municipio,unidad,fecha,pato)
                }
            }

            $("#btnSearch").click(function(){
                clean();
            });

            function loadEvents(){

                var silais = $('#silais').val();
                var munici = $('#munici').val();
                var unidad = $('#uni').val();
                var fecha = $('#fechaNoti').val();


                if (silais != '' && munici != '' && unidad != '' && fecha != ''){
                   getNotiDetails(silais, munici, unidad, fecha);

                    $('#codSilaisAtencion').attr('disabled', 'disabled');
                    $('#codMunicipio').attr('disabled', 'disabled');
                    $('#codUnidadAtencion').attr('disabled', 'disabled');
                    $('#notidate').attr('disabled', 'disabled');

                }

            }

            function getNotiToEdit(silais, municipio, unidad, fecha, pato) {
                $.ajax({
                    type: "GET",
                    url: parametros.notiDUrl,
                    data: {silais: silais, municipio: municipio, unidad: unidad, fecha: fecha, pato: pato},
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (response) {
                        bloquearUI(parametros.blockMess);
                        var patol = $('#codPato');
                        $('#action').val("edit");
                        patol.attr('disabled', 'disabled' );
                        patol.val(response.patologia.codigo).change();
                        $("#masc0").val(response.g01m);
                        $("#fem0").val(response.g01f);
                        $("#masc1").val(response.g02m);
                        $("#fem1").val(response.g02f);
                        $("#masc2").val(response.g03m);
                        $("#fem2").val(response.g03f);
                        $("#masc3").val(response.g04m);
                        $("#fem3").val(response.g04f);
                        $("#masc4").val(response.g05m);
                        $("#fem4").val(response.g05f);
                        $("#masc5").val(response.g06f);
                        $("#fem5").val(response.g06m);
                        $("#totalMasc").val(response.totalm);
                        $("#masc6").val(response.g07m);
                        $("#fem6").val(response.g07f);
                        $("#masc7").val(response.g08m);
                        $("#fem7").val(response.g08f);
                        $("#totalFem").val(response.totalf);
                        $("#masc8").val(response.g09m);
                        $("#fem8").val(response.g09f);
                        $("#masc9").val(response.g10m);
                        $("#fem9").val(response.g10f);
                        $("#masc10").val(response.g11m);
                        $("#fem10").val(response.g11f);
                        $("#masc11").val(response.g12m);
                        $("#fem11").val(response.g12f);
                        $("#masc12").val(response.g13m);
                        $("#fem12").val(response.g13f);
                        $("#masc13").val(response.descm);
                        $("#fem13").val(response.descf);

                        $('#dBtnEdit').show();
                        $('#dBtnSave').hide();
                    setTimeout(function(){
                            desbloquearUI() },500);
                    },
                    error: function (result) {

                    }
                });


            }

            function edit() {
                var obj = {};

                obj['codSilaisAtencion'] = $('#codSilaisAtencion option:selected').val();
                obj['codMunicipio'] = $('#codMunicipio option:selected').val();
                obj['codUnidadAtencion'] = $('#codUnidadAtencion option:selected').val();
                obj['notidate'] = $("#notidate").val();
                obj['semanaEpi'] = $("#semanaEpi").val();
                obj['codPato'] = $('#codPato option:selected').val();
                obj['masc0'] = $("#masc0").val();
                obj['fem0'] = $("#fem0").val();
                obj['masc1'] = $("#masc1").val();
                obj['fem1'] = $("#fem1").val();
                obj['masc2'] = $("#masc2").val();
                obj['fem2'] = $("#fem2").val();
                obj['masc3'] = $("#masc3").val();
                obj['fem3'] = $("#fem3").val();
                obj['masc4'] = $("#masc4").val();
                obj['fem4'] = $("#fem4").val();
                obj['masc5'] = $("#masc5").val();
                obj['fem5'] = $("#fem5").val();
                obj['totalMasc'] = $("#totalMasc").val();
                obj['masc6'] = $("#masc6").val();
                obj['fem6'] = $("#fem6").val();
                obj['masc7'] = $("#masc7").val();
                obj['fem7'] = $("#fem7").val();
                obj['totalFem'] = $("#totalFem").val();
                obj['masc8'] = $("#masc8").val();
                obj['fem8'] = $("#fem8").val();
                obj['masc9'] = $("#masc9").val();
                obj['fem9'] = $("#fem9").val();
                obj['masc10'] = $("#masc10").val();
                obj['fem10'] = $("#fem10").val();
                obj['masc11'] = $("#masc11").val();
                obj['fem11'] = $("#fem11").val();
                obj['masc12'] = $("#masc12").val();
                obj['fem12'] = $("#fem12").val();
                obj['masc13'] = $("#masc13").val();
                obj['fem13'] = $("#fem13").val();
                obj['mensaje'] = '';

                bloquearUI(parametros.blockMess);
                $.ajax({
                    url: parametros.saveUrl,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(obj),
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

                            $('#codPato').removeAttr('disabled');
                            $('#action').val('');
                            $('#dBtnSave').show();
                            $('#dBtnEdit').hide();

                            getNotiDetails(data.silais, data.municipio, data.unidad, data.fecha);

                            clean1();
                          /*  $.smallBox({
                                title: $('#msjSuccessfulE').val(),
                                content: $('#disappear').val(),
                                color: "#739E73",
                                iconSmall: "fa fa-check-circle",
                                timeout: 4000
                            });*/


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


            $("#btnEdit").click(function(){
                edit();
            });


            function deleteEvent(){
                var dat = $(this.innerHTML).data('id');

                var opcSi = $("#yes").val();
                var opcNo = $("#no").val();
                $.SmartMessageBox({

                    title: $('#titleC').val(),
                    content: $('#contentC').val(),
                    buttons: '['+opcSi+']['+opcNo+']'
                }, function (ButtonPressed) {
                    if (ButtonPressed === opcSi) {

                        if(dat != null) {
                            var detalle = dat.split(",");
                            var silais = detalle[0];
                            var municipio = detalle[1];
                            var unidad = detalle[2];
                            var fecha = detalle[3];
                            var pato = detalle[4];

                            if(fecha!= null){
                                var fe = fecha.split("-");
                                var anio = fe[0];
                                var mes = fe[1];
                                var dia = fe[2];
                            }

                            var obj = {};

                            obj['codSilaisAtencion'] = silais;
                            obj['codMunicipio'] = municipio;
                            obj['codUnidadAtencion'] = unidad;
                            obj['notidate'] = dia + "/" + mes + "/"+ anio;
                            obj['codPato'] = pato;

                            bloquearUI(parametros.blockMess);
                            $.ajax({
                                url: parametros.overrideUrl,
                                type: 'POST',
                                dataType: 'json',
                                data: JSON.stringify(obj),
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
                                    }else{

                                       getNotiDetails(data.silais, data.municipio, data.unidad, data.fecha);
                                        $.smallBox({
                                            title: $('#msjSuccDelete').val(),
                                            content: $('#disappear').val(),
                                            color: "#739E73",
                                            iconSmall: "fa fa-check-circle",
                                            timeout: 4000
                                        });
                                    }
                                },
                                error: function (data, status, er) {
                                    desbloquearUI();
                                    $.smallBox({
                                        title: $('#msjErrorOverride').val() + " error: " + data + " status: " + status + " er:" + er,
                                        content: $('#disappear').val(),
                                        color: "#C46A69",
                                        iconSmall: "fa fa-warning",
                                        timeout: 5000
                                    });
                                }
                            });


                        }else{
                            desbloquearUI();
                            $.smallBox({
                                title: $('#msjErrorOverride').val(),
                                content: $('#disappear').val(),
                                color: "#C46A69",
                                iconSmall: "fa fa-warning",
                                timeout: 5000
                            });
                        }

                }

                    if (ButtonPressed === opcNo) {
                        $.smallBox({
                            title: $('#titleCancel').val(),
                            content: "<i class='fa fa-clock-o'></i> <i>"+$("#disappear").val()+"</i>",
                            color: "#C79121",
                            iconSmall: "fa fa-warning",
                            timeout: 4000
                        });
                    }
                });

            }



        }
    };

}();
