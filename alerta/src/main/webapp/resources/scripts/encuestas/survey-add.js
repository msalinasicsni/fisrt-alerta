var AddAedesSurvey = function () {

    return {
        //main function to initiate the module
        init: function (parametros) {
            $(document).on('keypress','form input',function(event)
            {
                event.stopImmediatePropagation();
                if( event.which == 13 )
                {
                    event.preventDefault();
                    var $input = $('form input');
                    if( $(this).is( $input.last() ) )
                    {
                        //Time to submit the form!!!!
                        //alert( 'Hooray .....' );
                    }
                    else
                    {
                        $input.eq( $input.index(this) + 1 ).focus();
                    }
                }
            });

            var responsiveHelper_dt_basic = undefined;
			var breakpointDefinition = {
				tablet : 1024,
				phone : 480
			};
			var table1 = $('#dtDetalle').dataTable({
				"sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
					"t"+
					"<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
				"autoWidth" : true,
                "aoColumns" : [
                    {sClass: "aw-center" },null,{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },null,null,{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" }
                ],
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_dt_basic) {
						responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#dtDetalle'), breakpointDefinition);
					}
				},
				"rowCallback" : function(nRow) {
					responsiveHelper_dt_basic.createExpandIcon(nRow);
				},
				"drawCallback" : function(oSettings) {
					responsiveHelper_dt_basic.respond();
				}
			});
            
            function saveSurvey(){
                var maestro = {
                    encuestaId: $("#idMaestroAgregado").val(), // se pasa el id del maestro que se esta trabajando, la primera vez es null
                    codSilais: $('#codSilais option:selected').val(),
                    codMunicipio: $('#codigoMunicipio option:selected').val(),
                    codDistrito: $('#codigoDistrito option:selected').val(),
                    codArea: $('#codigoArea option:selected').val(),
                    codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                    codProcedencia: $('#codProcedencia option:selected').val(),
                    feInicioEncuesta: $('#fecInicioEncuesta').val(),
                    feFinEncuesta: $('#fecFinEncuesta').val(),
                    codOrdinalEncu: $('#codOrdinal option:selected').val(),
                    codModeloEncu: 1,
                    semanaEpi: $('#semanaEpi').val(),
                    mesEpi: $('#mesEpi').val(),
                    anioEpi: $('#anioEpi').val(),
                    usuarioRegistroId: 1
                };
                var detalle = {
                    codLocalidad: $('#codigoLocalidad option:selected').val(),
                    viviendaInspeccionada: $('#viviendasInspec').val(),
                    viviendaPositiva: $('#viviendasPositivas').val(),
                    manzanaInspeccionada: $('#manzanasInspec').val(),
                    manzanaPositiva: $('#manzanasPositivas').val(),
                    depositoInspeccionado: $('#depositosInspec').val(),
                    depositoPositivo: $('#depositosPositovos').val(),
                    pupaPositiva: $('#pupasPositivas').val(),
                    noAbatizado: $('#noAbati').val(),
                    noEliminado: $('#noElimni').val(),
                    noNeutralizado: $('#noNeutr').val(),
                    feAbatizado: $('#fecAbat').val(),
                    feRepot: '',//$('#fecReport').val(),
                    feVEnt: $('#fecVent').val(),
                    usuarioRegistroId: 1
                };

                var encuestaObj = {};
                encuestaObj['idMaestro'] = '';
                encuestaObj['mensaje'] = '';
                encuestaObj['maestro'] = maestro;
                encuestaObj['detalle'] = detalle;
                var html = null;
                $.ajax(
                    {
                        url: parametros.sAddSurvey,
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(encuestaObj),
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if (data.mensaje.length > 0){
                                html = '<div class="alert alert-block alert-warning"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> '+data.mensaje+
                                    '</p> ' +
                                    '</div>';
                            }else{
                                getSurveyDetails(data.idMaestro);
                                $("#idMaestroAgregado").val(data.idMaestro);
                                limpiarCamposDetalle();
                                html = '<div class="alert alert-block alert-success"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> ' + $("#msg_location_added").val() +
                                    '</p> ' +
                                    '</div>';
                            }
                            $('#mensaje').html(html).show().focus();
                            //$("#mensaje").show("slow");
                            //$("#mensaje").focus();
                            //$("#mensaje").fadeOut(6000);
                            setTimeout(function(){$("#mensaje").hide("slow");}, 4000);
                        },
                        error: function (data, status, er) {
                            alert("error: " + data + " status: " + status + " er:" + er);
                        }
                    }
                )
            }
            
            function getSurveyDetails(idMaestro) {
                var nTotalViviendasInspec    = 0;
                var nTotalViviendasPosit     = 0;
                var nTotalManzanasInspec     = 0;
                var nTotalManzanasPosit      = 0;
                var nTotalDepositosInspec    = 0;
                var nTotalDepositosPosit     = 0;
                var nTotalPupas              = 0;
    			$.getJSON(parametros.sSurveyDetailsUrl, {
                    idMaestroEncuesta: idMaestro,
    				ajax : 'true'
    			}, function(data) {
                    table1.fnClearTable();

                    var len = data.length;
    				for ( var i = 0; i < len; i++) {
                        nTotalViviendasInspec = nTotalViviendasInspec + data[i][0].viviendasInspec;
                        nTotalViviendasPosit     = nTotalViviendasPosit + data[i][0].viviendasPosit;
                        nTotalManzanasInspec     = nTotalManzanasInspec + data[i][0].manzanasInspec;
                        nTotalManzanasPosit      = nTotalManzanasPosit + data[i][0].manzanasPosit;
                        nTotalDepositosInspec    = nTotalDepositosInspec + data[i][0].depositosInspec;
                        nTotalDepositosPosit     = nTotalDepositosPosit + data[i][0].depositosPosit;
                        nTotalPupas              = nTotalPupas + data[i][0].pupasPosit;

						//var surveyUrl = parametros.sSurveyEditUrl + '?idMaestro='+dataToLoad[i][0].encuestaId;
						table1.fnAddData(
                                [i+1, data[i][0].localidad, data[i][0].viviendasInspec, data[i][0].viviendasPosit, data[i][0].indiceViviendas, data[i][0].manzanasInspec, data[i][0].manzanasPosit, data[i][0].indiceManzanas, data[i][0].depositosInspec, data[i][0].depositosPosit, data[i][0].indiceDepositos, data[i][0].indiceBrete, data[i][0].pupasPosit, data[i][0].indicePupas, data[i][0].fechaAbat, data[i][0].fechaVEnt, data[i][0].noAbati, data[i][0].noElimin,  data[i][0].noNeutr]);
    							//[data[i].identificacion, data[i].primerNombre, data[i].segundoNombre, data[i].primerApellido, data[i].segundoApellido, data[i].fechaNacimiento,data[i].municipioResidencia.nombre,'<a href='+ personUrl + ' class="btn btn-default btn-xs"><i class="fa fa-search"></i></a>']);
                        var nTotalViviendasIndice    = parseFloat((nTotalViviendasPosit / nTotalViviendasInspec)*100).toFixed(1);
                        var nTotalManzanasIndice     = parseFloat((nTotalManzanasPosit / nTotalManzanasInspec)*100).toFixed(1);
                        var nTotalDepositosIndice    = parseFloat((nTotalDepositosPosit/nTotalDepositosInspec)*100).toFixed(1);
                        var nTotalIndiceBrete        = parseFloat((nTotalPupas/nTotalViviendasInspec)*100).toFixed(1);
                        var nTotalIndiceIPupa		 = parseFloat((nTotalDepositosPosit/nTotalViviendasInspec)*100).toFixed(1);
                    }

                    $("#totalViviendasInspec").text(nTotalViviendasInspec);
                    $("#totalViviendasPosit").text(nTotalViviendasPosit);
                    $("#totalViviendasIndice").text(nTotalViviendasIndice);
                    $("#totalManzanasInspec").text(nTotalManzanasInspec);
                    $("#totalManzanasPosit").text(nTotalManzanasPosit);
                    $("#totalManzanasIndice").text(nTotalManzanasIndice);
                    $("#totalDepositosInspec").text(nTotalDepositosInspec);
                    $("#totalDepositosPosit").text(nTotalDepositosPosit);
                    $("#totalDepositosIndice").text(nTotalDepositosIndice);
                    $("#totalPupas").text(nTotalPupas);
                    $("#totalIndiceBrete").text(nTotalIndiceBrete);
                    $("#totalIndiceIPupa").text(nTotalIndiceIPupa);
    			})
    			.fail(function() {
				    alert( "error" );
				    
				});
            }

            $("#mensaje").hide();

            //mostrar modal detalle encuesta
            $("#openModal").click(function(){
                var $valid = $("#frmPrincipal").valid();
                if (!$valid) {
                    $formPrincipal.focusInvalid();
                    return false;
                }else {
                    if ($("#idMaestroAgregado").val() == null || $("#idMaestroAgregado").val().trim().length<=0){
                        //Validar si existe maestro
                        var maestro = {
                            encuestaId: '' ,
                            codSilais: $('#codSilais option:selected').val(),
                            codMunicipio: $('#codigoMunicipio option:selected').val(),
                            codDistrito: $('#codigoDistrito option:selected').val(),
                            codArea: $('#codigoArea option:selected').val(),
                            codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                            codProcedencia: $('#codProcedencia option:selected').val(),
                            feInicioEncuesta: $('#fecInicioEncuesta').val(),
                            feFinEncuesta: $('#fecFinEncuesta').val(),
                            codOrdinalEncu: $('#codOrdinal option:selected').val(),
                            codModeloEncu: 1,
                            semanaEpi: $('#semanaEpi').val(),
                            mesEpi: $('#mesEpi').val(),
                            anioEpi: $('#anioEpi').val(),
                            usuarioRegistroId: 1
                        };
                        var encuestaObj = {};
                        encuestaObj['maestroEncuesta'] = maestro;
                        $.getJSON(parametros.sSurveyHeaderUrl, {
                            maestroEncuesta: JSON.stringify(encuestaObj),
                            ajax: 'true'
                        }, function (data) {
                            var html = '';
                            var len = data.length;
                            if (len > 0) {
                                html = '<div class="alert alert-block alert-warning"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> ' +$("#msg_header_exist").val()+
                                    '<a class="" href="'+parametros.sEditSurveyUrl + '?idMaestro=' + data[0].encuestaId + '">Editar</a>' +
                                    '</p> ' +
                                    '</div>';
                                $('#msjMaestro').html(html).show().focus();
                                //$("#msjMaestro").show("slow");
                                //$("#msjMaestro").trigger("focus");
                            } else {
                                $("#msjMaestro").hide("slow");
                                $("#myModal").modal({
                                    show: true
                                });
                            }
                        })
                    }else{
                        $("#myModal").modal({
                            show: true
                        });
                    }
                }
            });

            <!-- al seleccionar municipio -->
            $('#codigoMunicipio').change(function(){
                $('#codigoDistrito').val('').change();
                $('#codigoArea').val('').change();
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sComunidadesUrl, {
                        municipioId: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            //html += '</option>';
                        }
                        $('#codigoLocalidad').html(html);
                    });
                    $.getJSON(parametros.sDistritosUrl, {
                        codMunicipio: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].valor
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoDistrito').html(html);
                    });
                    $.getJSON(parametros.sAreasUrl, {
                        codMunicipio: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].valor
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoArea').html(html);
                    })
                }
            });

            <!-- al seleccionar SILAIS -->
            $('#codSilais').change(function(){
                $('#codigoMunicipio').val('').change();
                $('#codUnidadSalud').val('').change();
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sMunicipiosUrl, {
                        idSilais: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigoNacional + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoMunicipio').html(html);
                    });

                    $.getJSON(parametros.sUnidadesUrl, {
                        silaisId: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codUnidadSalud').html(html);
                    });
            }
            });

            <!-- al seleccionar localidad -->
            $('#codigoLocalidad').change(function() {
                if ($("#idMaestroAgregado").val().length > 0 && $(this).val().length > 0){
                    var maestro = {
                        encuestaId:'' ,
                        codSilais: $('#codSilais option:selected').val(),
                        codMunicipio: $('#codigoMunicipio option:selected').val(),
                        codDistrito: $('#codigoDistrito option:selected').val(),
                        codArea: $('#codigoArea option:selected').val(),
                        codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                        codProcedencia: $('#codProcedencia option:selected').val(),
                        feInicioEncuesta: $('#fecInicioEncuesta').val(),
                        feFinEncuesta: $('#fecFinEncuesta').val(),
                        codOrdinalEncu: $('#codOrdinal option:selected').val(),
                        codModeloEncu: 1,
                        semanaEpi: $('#semanaEpi').val(),
                        mesEpi: $('#mesEpi').val(),
                        anioEpi: $('#anioEpi').val(),
                        usuarioRegistroId: 1
                    };
                    var encuestaObj = {};
                    encuestaObj['idLocalidad'] = $(this).val();
                    encuestaObj['idMaestroEncuesta'] = $("#idMaestroAgregado").val();
                    //encuestaObj['maestroEncuesta'] = maestro;
                    $.getJSON(parametros.sValidarLocalidadUrl, {
                        datosEncuesta: JSON.stringify(encuestaObj),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        if (len > 0) {
                            html = '<div class="alert alert-block alert-warning"> ' +
                                '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                '<p> ' + $("#msg_location_exist").val() +
                                '</p> ' +
                                '</div>';
                            $('#mensaje').html(html).show().focus();
                            //$("#mensaje").show("slow");
                            //$("#mensaje").focus();
                            //$("#mensaje").fadeOut(5000);
                            setTimeout(function(){$("#mensaje").hide("slow");}, 4000);
                            $("#codigoLocalidad").val('').change();
                        } else {
                            $("#mensaje").hide("slow");
                        }
                    })
                }
            });

            var $formPrincipal = $("#frmPrincipal").validate({
                rules: {
                    codSilais: {
                        required: true
                    },
                    codigoMunicipio: {
                        required: true
                    },
                    codUnidadSalud: {
                        required: true
                    },
                    fecInicioEncuesta: {
                        required: true,
                        dpDate: true,
                        dpCompareDate: ['before', '#fecFinEncuesta', 'notAfter', parametros.dFechaHoy ]
                    },
                    fecFinEncuesta: {
                        required: true,
                        dpDate: true,
                        dpCompareDate: {after: '#fecInicioEncuesta', 'notAfter': parametros.dFechaHoy}

                    },
                    codOrdinal: {
                        required: true
                    },
                    codProcedencia: {
                        required: true
                    },
                    semanaEpi: {
                        digits: true,
                        maxlength: 2
                    },
                    mesEpi: {
                        digits: true,
                        maxlength: 2
                    },
                    anioEpi: {
                        digits: true,
                        maxlength: 4
                    }
                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });

            var $formDetalleEncuesta = $("#frmDetalleEncuesta").validate({
                rules: {
                    codigoLocalidad:{
                        required: true
                    },
                    viviendasInspec:{
                        required: true,
                        digits: true
                    },
                    viviendasPositivas:{
                        required: true,
                        digits: true
                    },
                    manzanasInspec:{
                        required: true,
                        digits: true
                    },
                    manzanasPositivas:{
                        required: true,
                        digits: true
                    },
                    depositosInspec:{
                        required: true,
                        digits: true
                    },
                    depositosPositovos:{
                        required: true,
                        digits: true
                    },
                    pupasPositivas:{
                        required: true,
                        digits: true
                    },
                    noAbati:{
                        required: true,
                        digits: true
                    },
                    noElimni:{
                        required: true,
                        digits: true
                    },
                    noNeutr: {
                        required: true,
                        digits: true
                    },
                    fecAbat: {
                        dpDate: true
                    },/*
                    fecReport: {
                        dpDate: true
                    },*/
                    fecVent: {
                        dpDate: true
                    }
                },    // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                },
                submitHandler: function (form) {
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    saveSurvey();
                }
            });

            $('#fecInicioEncuesta').change(function () {
                var fecha = $('#fecInicioEncuesta').val();
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

            function limpiarCamposMaestro(){
                $('#codSilais').val('').change();
                $('#codUnidadSalud').val('').change();
                $('#codigoMunicipio').val('').change();
                $('#codigoDistrito').val('').change();
                $('#codigoArea').val('').change();
                $('#codProcedencia').val('').change();
                $('#codOrdinal').val('').change();
                $('#mesEpi').val('');
                $("#anioEpi").val('');
                $("#semanaEpi").val('');
                $("#fecInicioEncuesta").val('');
                $("#fecFinEncuesta").val('');
                $("#idMaestroAgregado").val('');
            }

            function limpiarCamposDetalle(){
                $('#codigoLocalidad option:first').prop("selected", true).change();
                $("#viviendasInspec").val('');
                $("#viviendasPositivas").val('');
                $("#manzanasInspec").val('');
                $("#manzanasPositivas").val('');
                $("#depositosInspec").val('');
                $("#depositosPositovos").val('');
                $("#pupasPositivas").val('');
                $("#noAbati").val('');
                $("#noElimni").val('');
                $("#noNeutr").val('');
                //$("#fecReport").val('');
                $("#fecVent").val('');
                $("#fecAbat").val('');
            }

            function limpiarTablaDetalle(){
                table1.fnClearTable();
                    $("#totalViviendasInspec").text('0');
                    $("#totalViviendasPosit").text('0');
                    $("#totalViviendasIndice").text('0');
                    $("#totalManzanasInspec").text('0');
                    $("#totalManzanasPosit").text('0');
                    $("#totalManzanasIndice").text('0');
                    $("#totalDepositosInspec").text('0');
                    $("#totalDepositosPosit").text('0');
                    $("#totalDepositosIndice").text('0');
                    $("#totalPupas").text('0');
                    $("#totalIndiceBrete").text('0');
                    $("#totalIndiceIPupa").text('0');
            }

            $("#btnNuevoRegistro").click(function(){
                limpiarCamposMaestro();
                limpiarTablaDetalle();
                $("#msjMaestro").hide("slow");
            });
        }
    };

}();

var AddLarvariaSurvey = function () {

    return {
        //Se comenta todo lo referente a recipientes infestados
        //main function to initiate the module
        init: function (parametros) {
            $(document).on('keypress','form input',function(event)
            {
                event.stopImmediatePropagation();
                if( event.which == 13 )
                {
                    event.preventDefault();
                    var $input = $('form input');
                    if( $(this).is( $input.last() ) )
                    {
                        //Time to submit the form!!!!
                        //alert( 'Hooray .....' );
                    }
                    else
                    {
                        $input.eq( $input.index(this) + 1 ).focus();
                    }
                }
            });

            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet : 1024,
                phone : 480
            };
            var table2 = $('#dtDetalleDistribucion').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,
                "aoColumns" : [
                    {sClass: "aw-center" },null,{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" }
                ],
                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#dtDetalleDistribucion'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });
            function saveSurvey(){
                console.log("entra savesurvey");
                var maestro = {
                    encuestaId: $("#idMaestroAgregado").val(), // se pasa el id del maestro que se esta trabajando, la primera vez es null
                    codSilais: $('#codSilais option:selected').val(),
                    codMunicipio: $('#codigoMunicipio option:selected').val(),
                    codDistrito: $('#codigoDistrito option:selected').val(),
                    codArea: $('#codigoArea option:selected').val(),
                    codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                    codProcedencia: $('#codProcedencia option:selected').val(),
                    feInicioEncuesta: $('#fecInicioEncuesta').val(),
                    feFinEncuesta: $('#fecFinEncuesta').val(),
                    codOrdinalEncu: $('#codOrdinal option:selected').val(),
                    codModeloEncu: 1,
                    semanaEpi: $('#semanaEpi').val(),
                    mesEpi: $('#mesEpi').val(),
                    anioEpi: $('#anioEpi').val(),
                    usuarioRegistroId: 1
                };
                var detalle = {
                    codLocalidad: $('#codigoLocalidad option:selected').val(),
                    especieAegypti: $('#txtAedesAegyti').val(),
                    especieAlbopic: $('#txtAedesAlbopic').val(),
                    especieCulexQuinque: $('#txtCulexQuinque').val(),
                    especieCulexNigrip: $('#txtCulexNigrip').val(),
                    especieCulexCoronat: $('#txtCulexCoronat').val(),
                    especieCulexErratico: $('#txtCulexErratico').val(),
                    especieCulexTarsalis: $('#txtCulexTarsalis').val(),
                    especieCulexFatigans: $('#txtCulexFatigans').val(),
                    especieCulexAlbim: $('#txtAnophAlbim').val(),
                    usuarioRegistroId: 1
                };
                var encuestaObj = {};
                encuestaObj['idMaestro'] = '';
                encuestaObj['mensaje'] = '';
                encuestaObj['maestro'] = maestro;
                encuestaObj['detalle'] = detalle;
                var html = null;
                $.ajax(
                    {
                        url: parametros.sAddSurvey,
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(encuestaObj),
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if (data.mensaje.length > 0){
                                html = '<div class="alert alert-block alert-warning"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> '+data.mensaje+
                                    '</p> ' +
                                    '</div>';
                            }else{
                                getSurveyDetails(data.idMaestro);
                                $("#idMaestroAgregado").val(data.idMaestro);
                                limpiarCamposDetalle();
                                html = '<div class="alert alert-block alert-success"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> ' + $("#msg_location_added").val() +
                                    '</p> ' +
                                    '</div>';
                            }
                            $('#mensaje').html(html).show().focus();
                            //$("#mensaje").show("slow");
                            //$("#mensaje").focus();
                            //$("#mensaje").fadeOut(6000);
                            setTimeout(function(){$("#mensaje").hide("slow");}, 4000);
                        },
                        error: function (data, status, er) {
                            alert("error: " + data + " status: " + status + " er:" + er);
                        }
                    }
                )
            }

            function getSurveyDetails(idMaestro) {
                console.log("entra get surveydetails");
                var nIndiceAegypti  = 0, nIndiceAlbopic  = 0, nIndiceQuinque  = 0, nIndiceNigrip   = 0, nIndiceCoronat  = 0, nIndiceErratico = 0, nIndiceTarsalis = 0, nIndiceFatigans = 0, nIndiceAlbim    = 0;
                var nTotalAegypti  = 0, nTotalAlbopic  = 0, nTotalQuinque  = 0, nTotalNigrip   = 0, nTotalCoronat  = 0, nTotalErratico = 0, nTotalTarsalis = 0, nTotalFatigans = 0, nTotalAlbim    = 0, nTotalTotalDist=0;

                $.getJSON(parametros.sSurveyDetailsUrl, {
                    idMaestroEncuesta: idMaestro,
                    ajax : 'true'
                }, function(response) {
                    table2.fnClearTable();
                    var len = response.length;
                    for ( var i = 0; i < len; i++) {

                        nTotalAegypti  =    nTotalAegypti  + response[i][0].especieAegypti;
                        nTotalAlbopic  =    nTotalAlbopic  + response[i][0].especieAlbopic;
                        nTotalQuinque  =    nTotalQuinque  + response[i][0].especieCulexQuinque;
                        nTotalNigrip   =    nTotalNigrip   + response[i][0].especieCulexNigrip  ;
                        nTotalCoronat  =    nTotalCoronat  + response[i][0].especieCulexCoronat ;
                        nTotalErratico =    nTotalErratico + response[i][0].especieCulexErratico;
                        nTotalTarsalis =    nTotalTarsalis + response[i][0].especieCulexTarsalis;
                        nTotalFatigans =    nTotalFatigans + response[i][0].especieCulexFatigans;
                        nTotalAlbim    =    nTotalAlbim    + response[i][0].especieCulexAlbim;
                        var nTotalDistribucion = response[i][0].especieAegypti  +
                            response[i][0].especieAlbopic +
                            response[i][0].especieCulexQuinque +
                            response[i][0].especieCulexNigrip  +
                            response[i][0].especieCulexCoronat +
                            response[i][0].especieCulexErratico+
                            response[i][0].especieCulexTarsalis+
                            response[i][0].especieCulexFatigans+
                            response[i][0].especieCulexAlbim;
                        nTotalTotalDist = nTotalTotalDist + nTotalDistribucion;

                        table2.fnAddData(
                            [i+1, response[i][0].localidad, response[i][0].especieAegypti , response[i][0].especieAlbopic, response[i][0].especieCulexQuinque, response[i][0].especieCulexNigrip, response[i][0].especieCulexCoronat, response[i][0].especieCulexErratico, response[i][0].especieCulexTarsalis, response[i][0].especieCulexFatigans, response[i][0].especieCulexAlbim, nTotalDistribucion]);
                    }

                    if (nTotalTotalDist > 0){
                        nIndiceAegypti = parseFloat((nTotalAegypti / nTotalTotalDist) * 100).toFixed(1);
                        nIndiceAlbopic = parseFloat((nTotalAlbopic / nTotalTotalDist) * 100).toFixed(1);
                        nIndiceQuinque = parseFloat((nTotalQuinque / nTotalTotalDist) * 100).toFixed(1);
                        nIndiceNigrip = parseFloat((nTotalNigrip / nTotalTotalDist) * 100).toFixed(1);
                        nIndiceCoronat = parseFloat((nTotalCoronat / nTotalTotalDist) * 100).toFixed(1);
                        nIndiceErratico = parseFloat((nTotalErratico / nTotalTotalDist) * 100).toFixed(1);
                        nIndiceTarsalis = parseFloat((nTotalTarsalis / nTotalTotalDist) * 100).toFixed(1);
                        nIndiceFatigans = parseFloat((nTotalFatigans / nTotalTotalDist) * 100).toFixed(1);
                        nIndiceAlbim = parseFloat((nTotalAlbim / nTotalTotalDist) * 100).toFixed(1);
                    }else{
                        nIndiceAegypti = 0;
                        nIndiceAlbopic = 0;
                        nIndiceQuinque = 0;
                        nIndiceNigrip = 0;
                        nIndiceCoronat = 0;
                        nIndiceErratico = 0;
                        nIndiceTarsalis = 0;
                        nIndiceFatigans = 0;
                        nIndiceAlbim = 0;
                    }

                    $("#indiceAegypti").text(nIndiceAegypti  );
                    $("#indiceAlbopic").text(nIndiceAlbopic  );
                    $("#indiceQuinque").text(nIndiceQuinque  );
                    $("#indiceNigrip").text( nIndiceNigrip  );
                    $("#indiceCoronat").text(nIndiceCoronat  );
                    $("#indiceErratico").text(nIndiceErratico );
                    $("#indiceTarsalis").text(nIndiceTarsalis );
                    $("#indiceFatigans").text(nIndiceFatigans );
                    $("#indiceAnophAlbim").text(nIndiceAlbim    );

                    $("#totalAegypti").text(nTotalAegypti  );
                    $("#totalAlbopic").text(nTotalAlbopic  );
                    $("#totalQuinque").text(nTotalQuinque  );
                    $("#totalNigrip").text( nTotalNigrip  );
                    $("#totalCoronat").text(nTotalCoronat  );
                    $("#totalErratico").text(nTotalErratico );
                    $("#totalTarsalis").text(nTotalTarsalis );
                    $("#totalFatigans").text(nTotalFatigans );
                    $("#totalAnophAlbim").text(nTotalAlbim    );
                    $("#totalTotalD").text(nTotalTotalDist);
                })
                    .fail(function() {
                        alert( "error" );

                    });
            }

            $("#mensaje").hide();

            //mostrar modal detalle encuesta
            $("#openModal").click(function(){
                var $valid = $("#frmPrincipal").valid();
                if (!$valid) {
                    $formPrincipal.focusInvalid();
                    return false;
                }else {
                    if ($("#idMaestroAgregado").val() == null || $("#idMaestroAgregado").val().trim().length<=0){
                        //Validar si existe maestro
                        var maestro = {
                            encuestaId: '' ,
                            codSilais: $('#codSilais option:selected').val(),
                            codMunicipio: $('#codigoMunicipio option:selected').val(),
                            codDistrito: $('#codigoDistrito option:selected').val(),
                            codArea: $('#codigoArea option:selected').val(),
                            codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                            codProcedencia: $('#codProcedencia option:selected').val(),
                            feInicioEncuesta: $('#fecInicioEncuesta').val(),
                            feFinEncuesta: $('#fecFinEncuesta').val(),
                            codOrdinalEncu: $('#codOrdinal option:selected').val(),
                            codModeloEncu: 1,
                            semanaEpi: $('#semanaEpi').val(),
                            mesEpi: $('#mesEpi').val(),
                            anioEpi: $('#anioEpi').val(),
                            usuarioRegistroId: 1
                        };
                        var encuestaObj = {};
                        encuestaObj['maestroEncuesta'] = maestro;
                        $.getJSON(parametros.sSurveyHeaderUrl, {
                            maestroEncuesta: JSON.stringify(encuestaObj),
                            ajax: 'true'
                        }, function (data) {
                            var html = '';
                            var len = data.length;
                            if (len > 0) {
                                html = '<div class="alert alert-block alert-warning"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> ' + $("#msg_header_exist").val() +
                                    '<a class="" href="'+parametros.sEditSurveyUrl + '?idMaestro=' + data[0].encuestaId + '">Editar</a>' +
                                    '</p> ' +
                                    '</div>';
                                $('#msjMaestro').html(html).show().focus();
                                //$("#msjMaestro").show("slow");
                                //$("#msjMaestro").trigger("focus");
                            } else {
                                $("#msjMaestro").hide("slow");
                                $("#myModal").modal({
                                    show: true
                                });
                            }
                        })
                    }else{
                        $("#myModal").modal({
                            show: true
                        });
                    }
                }
            });

            <!-- al seleccionar municipio -->
            $('#codigoMunicipio').change(function(){
                $('#codigoDistrito').val('').change();
                $('#codigoArea').val('').change();
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sComunidadesUrl, {
                        municipioId: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            //html += '</option>';
                        }
                        $('#codigoLocalidad').html(html);
                    });
                    $.getJSON(parametros.sDistritosUrl, {
                        codMunicipio: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].valor
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoDistrito').html(html);
                    });
                    $.getJSON(parametros.sAreasUrl, {
                        codMunicipio: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].valor
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoArea').html(html);
                    })
                }
            });

            <!-- al seleccionar SILAIS -->
            $('#codSilais').change(function(){
                $('#codigoMunicipio').val('').change();
                $('#codUnidadSalud').val('').change();
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sMunicipiosUrl, {
                        idSilais: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigoNacional + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoMunicipio').html(html);
                    });
                    $.getJSON(parametros.sUnidadesUrl, {
                        silaisId: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codUnidadSalud').html(html);
                    });
                }
            });

            <!-- al seleccionar localidad -->
            $('#codigoLocalidad').change(function() {
                if ($("#idMaestroAgregado").val().length > 0 && $(this).val().length > 0){
                    var maestro = {
                        encuestaId:'' ,
                        codSilais: $('#codSilais option:selected').val(),
                        codMunicipio: $('#codigoMunicipio option:selected').val(),
                        codDistrito: $('#codigoDistrito option:selected').val(),
                        codArea: $('#codigoArea option:selected').val(),
                        codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                        codProcedencia: $('#codProcedencia option:selected').val(),
                        feInicioEncuesta: $('#fecInicioEncuesta').val(),
                        feFinEncuesta: $('#fecFinEncuesta').val(),
                        codOrdinalEncu: $('#codOrdinal option:selected').val(),
                        codModeloEncu: 1,
                        semanaEpi: $('#semanaEpi').val(),
                        mesEpi: $('#mesEpi').val(),
                        anioEpi: $('#anioEpi').val(),
                        usuarioRegistroId: 1
                    };
                    var encuestaObj = {};
                    encuestaObj['idLocalidad'] = $(this).val();
                    encuestaObj['idMaestroEncuesta'] = $("#idMaestroAgregado").val();
                    //encuestaObj['maestroEncuesta'] = maestro;
                    $.getJSON(parametros.sValidarLocalidadUrl, {
                        datosEncuesta: JSON.stringify(encuestaObj),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        if (len > 0) {
                            html = '<div class="alert alert-block alert-warning"> ' +
                                '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                '<p> ' + $("#msg_location_exist").val() +
                                '</p> ' +
                                '</div>';
                            $('#mensaje').html(html).show().focus();
                            //$("#mensaje").show("slow");
                            //$("#mensaje").focus();
                            //$("#mensaje").fadeOut(5000);
                            setTimeout(function(){$("#mensaje").hide("slow");}, 4000);
                            $("#codigoLocalidad").val('').change();
                        } else {
                            $("#mensaje").hide("slow");
                        }
                    })
                }
            });

            var $formPrincipal = $("#frmPrincipal").validate({
                rules: {
                    codSilais: {
                        required: true
                    },
                    codigoMunicipio: {
                        required: true
                    },
                    codUnidadSalud: {
                        required: true
                    },
                    fecInicioEncuesta: {
                        required: true,
                        dpDate: true,
                        dpCompareDate: ['before', '#fecFinEncuesta', 'notAfter', parametros.dFechahoy ]
                    },
                    fecFinEncuesta: {
                        required: true,
                        dpDate: true,
                        dpCompareDate: {after: '#fecInicioEncuesta', 'notAfter': parametros.dFechahoy}

                    },
                    codOrdinal: {
                        required: true
                    },
                    codProcedencia: {
                        required: true
                    },
                    semanaEpi: {
                        digits: true,
                        maxlength: 2
                    },
                    mesEpi: {
                        digits: true,
                        maxlength: 2
                    },
                    anioEpi: {
                        digits: true,
                        maxlength: 4
                    }
                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });

            var $formDetalleEncuesta = $("#frmDetalleEncuesta").validate({
                rules: {
                    codigoLocalidad:{
                        required: true
                    },
                    txtAedesAegyti: {
                        required: true,
                        digits: true
                    },
                    txtAedesAlbopic: {
                        required: true,
                        digits: true
                    },
                    txtCulexQuinque: {
                        required: true,
                        digits: true
                    },
                    txtCulexNigrip: {
                        required: true,
                        digits: true
                    },
                    txtCulexCoronat: {
                        required: true,
                        digits: true
                    },
                    txtCulexErratico: {
                        required: true,
                        digits: true
                    },
                    txtCulexTarsalis: {
                        required: true,
                        digits: true
                    },
                    txtCulexFatigans: {
                        required: true,
                        digits: true
                    },
                    txtAnophAlbim: {
                        required: true,
                        digits: true
                    }
                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                },
                submitHandler: function (form) {
                    console.log("entra summithandler");
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    saveSurvey();
                }
            });

            $('#fecInicioEncuesta').change(function () {
                var fecha = $('#fecInicioEncuesta').val();
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

            function limpiarCamposMaestro(){
                $('#codSilais').val('').change();
                $('#codUnidadSalud').val('').change();
                $('#codigoMunicipio').val('').change();
                $('#codigoDistrito').val('').change();
                $('#codigoArea').val('').change();
                $('#codProcedencia').val('').change();
                $('#codOrdinal').val('').change();
                $('#mesEpi').val('');
                $("#anioEpi").val('');
                $("#semanaEpi").val('');
                $("#fecInicioEncuesta").val('');
                $("#fecFinEncuesta").val('');
                $("#idMaestroAgregado").val('');
            }

            function limpiarCamposDetalle(){
                $('#codigoLocalidad option:first').prop("selected", true).change();
                $("#txtAedesAegyti").val('');
                $("#txtAedesAlbopic").val('');
                $("#txtCulexQuinque").val('');
                $("#txtCulexNigrip").val('');
                $("#txtCulexCoronat").val('');
                $("#txtCulexErratico").val('');
                $("#txtCulexTarsalis").val('');
                $("#txtCulexFatigans").val('');
                $("#txtAnophAlbim").val('');
            }

            function limpiarTablasDetalle(){
                table2.fnClearTable();

                $("#indiceAegypti").text('0');
                $("#indiceAlbopic").text('0');
                $("#indiceQuinque").text('0');
                $("#indiceNigrip").text('0');
                $("#indiceCoronat").text('0');
                $("#indiceErratico").text('0');
                $("#indiceTarsalis").text('0');
                $("#indiceFatigans").text('0');
                $("#indiceAnophAlbim").text('0'    );

                $("#totalAegypti").text('0' );
                $("#totalAlbopic").text('0');
                $("#totalQuinque").text('0');
                $("#totalNigrip").text('0');
                $("#totalCoronat").text('0');
                $("#totalErratico").text('0');
                $("#totalTarsalis").text('0');
                $("#totalFatigans").text('0');
                $("#totalAnophAlbim").text('0');
                $("#totalTotalD").text('0');

            }

            $("#btnNuevoRegistro").click(function(){
                limpiarCamposMaestro();
                limpiarTablasDetalle();
                $("#msjMaestro").hide("slow");
            });
        }
    };

}();

var AddDepositoSurvey = function(){

    return {
        //main function to initiate the module
        init: function (parametros){
            $(document).on('keypress','form input',function(event)
            {
                event.stopImmediatePropagation();
                if( event.which == 13 )
                {
                    event.preventDefault();
                    var $input = $('form input');
                    if( $(this).is( $input.last() ) )
                    {
                        //Time to submit the form!!!!
                        //alert( 'Hooray .....' );
                    }
                    else
                    {
                        $input.eq( $input.index(this) + 1 ).focus();
                    }
                }
            });

            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet : 1024,
                phone : 480
            };
            var table1 = $('#dtDetalleDepositos').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,
                "aoColumns" : [
                    {sClass: "aw-center" },null,{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },{sClass: "aw-right" },null,null,null
                ],
                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#dtDetalleDepositos'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });

            function saveSurvey(){
                var maestro = {
                    encuestaId: $("#idMaestroAgregado").val(), // se pasa el id del maestro que se esta trabajando, la primera vez es null
                    codSilais: $('#codSilais option:selected').val(),
                    codMunicipio: $('#codigoMunicipio option:selected').val(),
                    codDistrito: $('#codigoDistrito option:selected').val(),
                    codArea: $('#codigoArea option:selected').val(),
                    codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                    codProcedencia: $('#codProcedencia option:selected').val(),
                    feInicioEncuesta: $('#fecInicioEncuesta').val(),
                    feFinEncuesta: $('#fecFinEncuesta').val(),
                    codOrdinalEncu: $('#codOrdinal option:selected').val(),
                    codModeloEncu: 1,
                    semanaEpi: $('#semanaEpi').val(),
                    mesEpi: $('#mesEpi').val(),
                    anioEpi: $('#anioEpi').val(),
                    usuarioRegistroId: 1
                };
                var detalle = {
                    codLocalidad: $('#codigoLocalidad option:selected').val(),
                    pilaInfestado: $('#txtPila').val(),
                    llantaInfestado: $('#txtLlanta').val(),
                    barrilInfestado: $('#txtBarril').val(),
                    floreroInfestado: $('#txtFlorero').val(),
                    bebederoInfestado: $('#txtBebedero').val(),
                    artEspecialInfes: $('#txtArt_Esp').val(),
                    otrosDepositosInfes: $('#txtO_Dep').val(),
                    cisterInfestado: $('#txtCister').val(),
                    inodoroInfestado: $('#txtInodoro').val(),
                    barroInfestado: $('#txtBarro').val(),
                    plantaInfestado: $('#txtPlanta').val(),
                    arbolInfestado: $('#txtArbol').val(),
                    pozoInfestado: $('#txtPozo').val(),
                    manzana: $('#txtManzanas').val(),
                    vivienda: $('#txtViviendas').val(),
                    nombre: $('#txtNombre').val(),
                    decripOtroDeposito: $('#txtDescOdep').val(),
                    decripcionCister: $('#txtDescCister').val(),
                    usuarioRegistroId: 1
                };

                var encuestaObj = {};
                encuestaObj['idMaestro'] = '';
                encuestaObj['mensaje'] = '';
                encuestaObj['maestro'] = maestro;
                encuestaObj['detalle'] = detalle;
                var html = null;
                $.ajax(
                    {
                        url: parametros.sAddSurvey,
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(encuestaObj),
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if (data.mensaje.length > 0){
                                html = '<div class="alert alert-block alert-warning"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> '+data.mensaje+
                                    '</p> ' +
                                    '</div>';
                            }else {
                                getSurveyDetails(data.idMaestro);
                                $("#idMaestroAgregado").val(data.idMaestro);
                                limpiarCamposDetalle();
                                html = '<div class="alert alert-block alert-success"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> ' + $("#msg_location_added").val() +
                                '</p> ' +
                                '</div>';
                            }
                            $('#mensaje').html(html).show().focus();
                            //$("#mensaje").show("slow");
                            //$("#mensaje").focus();
                            //$("#mensaje").fadeOut(5000);
                            setTimeout(function(){$("#mensaje").hide("slow");}, 4000);
                        },
                        error: function (data, status, er) {
                            alert("error: " + data + " status: " + status + " er:" + er);
                        }
                    }
                )
            }

            function getSurveyDetails(idMaestro){
                $.getJSON(parametros.sSurveyDetailsUrl, {
                    idMaestroEncuesta: idMaestro,
                    ajax : 'true'
                }, function(response) {
                    table1.fnClearTable();

                    var nIndicePilas, nIndiceLlanta, nIndiceBarril, nIndiceFlorero,nIndiceBebedero, nIndiceArtEspec, nIndiceODep, nIndiceCister, nIndiceInodo;
                    var nIndicePlanta   = 0, nIndiceBarro    = 0, nIndiceArbol    = 0,nIndicePozo     = 0, nTotalPilas    = 0, nTotalLlanta   = 0, nTotalBarril   = 0, nTotalFlorero  = 0, nTotalBebedero = 0;
                    var nTotalArtEspec = 0, nTotalODep     = 0, nTotalCister   = 0, nTotalInodo    = 0, nTotalPlanta   = 0, nTotalBarro    = 0, nTotalArbol    = 0, nTotalPozo     = 0, nTotalTotalIndice = 0;

                    var len = response.length;
                    for (var i = 0; i < len; i++) {
                        var nTotalIndice = response[i][0].pilaInfestado + response[i][0].llantaInfestado + response[i][0].barrilInfestado + response[i][0].floreroInfestado + response[i][0].bebederoInfestado + response[i][0].artEspecialInfes +
                            response[i][0].otrosDepositosInfes + response[i][0].cisterInfestado + response[i][0].inodoroInfestado + response[i][0].barroInfestado  +response[i][0].plantaInfestado + response[i][0].arbolInfestado +response[i][0].pozoInfestado;
                        nTotalTotalIndice = nTotalTotalIndice + nTotalIndice;
                        nTotalPilas    =    nTotalPilas    + response[i][0].pilaInfestado;
                        nTotalLlanta   =    nTotalLlanta   + response[i][0].llantaInfestado ;
                        nTotalBarril   =    nTotalBarril   + response[i][0].barrilInfestado ;
                        nTotalFlorero  =    nTotalFlorero  + response[i][0].floreroInfestado ;
                        nTotalBebedero =    nTotalBebedero + response[i][0].bebederoInfestado ;
                        nTotalArtEspec =    nTotalArtEspec + response[i][0].artEspecialInfes;
                        nTotalODep     =    nTotalODep     + response[i][0].otrosDepositosInfes;
                        nTotalCister   =    nTotalCister   + response[i][0].cisterInfestado ;
                        nTotalInodo    =    nTotalInodo    + response[i][0].inodoroInfestado;
                        nTotalBarro    =    nTotalBarro    + response[i][0].barroInfestado ;
                        nTotalPlanta   =    nTotalPlanta   + response[i][0].plantaInfestado;
                        nTotalArbol    =    nTotalArbol    + response[i][0].arbolInfestado;
                        nTotalPozo     =    nTotalPozo     + response[i][0].pozoInfestado;

                        table1.fnAddData(
                            [i+1, response[i][0].localidad, response[i][0].manzana, response[i][0].vivienda, response[i][0].pilaInfestado, response[i][0].llantaInfestado, response[i][0].barrilInfestado, response[i][0].floreroInfestado, response[i][0].bebederoInfestado, response[i][0].artEspecialInfes, response[i][0].otrosDepositosInfes, response[i][0].cisterInfestado, response[i][0].inodoroInfestado, response[i][0].barroInfestado, response[i][0].plantaInfestado, response[i][0].arbolInfestado, response[i][0].pozoInfestado, nTotalIndice, response[i][0].nombre , response[i][0].decripOtroDeposito, response[i][0].decripcionCister]);

                    }
                    if (nTotalTotalIndice > 0) {
                        nIndicePilas = parseFloat((nTotalPilas / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceLlanta = parseFloat((nTotalLlanta / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceBarril = parseFloat((nTotalBarril / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceFlorero = parseFloat((nTotalFlorero / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceBebedero = parseFloat((nTotalBebedero / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceArtEspec = parseFloat((nTotalArtEspec / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceODep = parseFloat((nTotalODep / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceCister = parseFloat((nTotalCister / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceInodo = parseFloat((nTotalInodo / nTotalTotalIndice) * 100).toFixed(1);
                        nIndicePlanta = parseFloat((nTotalPlanta / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceBarro = parseFloat((nTotalBarro / nTotalTotalIndice) * 100).toFixed(1);
                        nIndiceArbol = parseFloat((nTotalArbol / nTotalTotalIndice) * 100).toFixed(1);
                        nIndicePozo = parseFloat((nTotalPozo / nTotalTotalIndice) * 100).toFixed(1);
                    }else
                    {
                        nIndicePilas = 0;
                        nIndiceLlanta = 0;
                        nIndiceBarril = 0;
                        nIndiceFlorero = 0;
                        nIndiceBebedero = 0;
                        nIndiceArtEspec = 0;
                        nIndiceODep = 0;
                        nIndiceCister = 0;
                        nIndiceInodo = 0;
                        nIndicePlanta = 0;
                        nIndiceBarro = 0;
                        nIndiceArbol = 0;
                        nIndicePozo = 0;
                    }
                    $("#indicePila").text(nIndicePilas);
                    $("#indiceLlanta").text(nIndiceLlanta);
                    $("#indiceBarril").text(nIndiceBarril);
                    $("#indiceFloreros").text(nIndiceFlorero);
                    $("#indiceBeberedos").text(nIndiceBebedero);
                    $("#indiceArtEspec").text(nIndiceArtEspec);
                    $("#indiceODep").text(nIndiceODep);
                    $("#indiceCister").text(nIndiceCister);
                    $("#indiceInodo").text(nIndiceInodo);
                    $("#indiceBarro").text(nIndiceBarro);
                    $("#indicePlanta").text(nIndicePlanta);
                    $("#indiceArbol").text(nIndiceArbol);
                    $("#indicePozo").text(nIndicePozo);

                    $("#totalPila").text(nTotalPilas);
                    $("#totalLlanta").text(nTotalLlanta);
                    $("#totalBarril").text(nTotalBarril);
                    $("#totalFloreros").text(nTotalFlorero);
                    $("#totalBeberedos").text(nTotalBebedero);
                    $("#totalArtEspec").text(nTotalArtEspec);
                    $("#totalODep").text(nTotalODep);
                    $("#totalCister").text(nTotalCister);
                    $("#totalInodo").text(nTotalInodo);
                    $("#totalBarro").text(nTotalBarro);
                    $("#totalPlanta").text(nTotalPlanta);
                    $("#totalArbol").text(nTotalArbol);
                    $("#totalPozo").text(nTotalPozo);
                    $("#totalTotalI").text(nTotalTotalIndice);
                });
            }

            function limpiarCamposDetalle(){
                $('#codigoLocalidad option:first').prop("selected", true).change();
                $("#txtPila").val('');
                $("#txtLlanta").val('');
                $("#txtBarril").val('');
                $("#txtFlorero").val('');
                $("#txtBebedero").val('');
                $("#txtArt_Esp").val('');
                $("#txtO_Dep").val('');
                $("#txtCister").val('');
                $("#txtInodoro").val('');
                $("#txtBarro").val('');
                $("#txtPlanta").val('');
                $("#txtArbol").val('');
                $("#txtPozo").val('');
                $("#txtManzanas").val('');
                $("#txtViviendas").val('');
                $("#txtNombre").val('');
                $("#txtDescOdep").val('');
                $("#txtDescCister").val('');
            }

            function limpiarCamposMaestro(){
                $('#codSilais').val('').change();
                $('#codUnidadSalud').val('').change();
                $('#codigoMunicipio').val('').change();
                $('#codigoDistrito').val('').change();
                $('#codigoArea').val('').change();
                $('#codProcedencia').val('').change();
                $('#codOrdinal').val('').change();
                $('#mesEpi').val('');
                $("#anioEpi").val('');
                $("#semanaEpi").val('');
                $("#fecInicioEncuesta").val('');
                $("#fecFinEncuesta").val('');
                $("#idMaestroAgregado").val('');
            }

            function limpiarTablasDetalle(){
                table1.fnClearTable();

                $("#indicePila").text('0');
                $("#indiceLlanta").text('0');
                $("#indiceBarril").text('0');
                $("#indiceFloreros").text('0');
                $("#indiceBeberedos").text('0');
                $("#indiceArtEspec").text('0');
                $("#indiceODep").text('0');
                $("#indiceCister").text('0');
                $("#indiceInodo").text('0');
                $("#indiceBarro").text('0');
                $("#indicePlanta").text('0');
                $("#indiceArbol").text('0');
                $("#indicePozo").text('0');

                $("#totalPila").text('0');
                $("#totalLlanta").text('0');
                $("#totalBarril").text('0');
                $("#totalFloreros").text('0');
                $("#totalBeberedos").text('0');
                $("#totalArtEspec").text('0');
                $("#totalODep").text('0');
                $("#totalCister").text('0');
                $("#totalInodo").text('0');
                $("#totalBarro").text('0');
                $("#totalPlanta").text('0');
                $("#totalArbol").text('0');
                $("#totalPozo").text('0');
                $("#totalTotalI").text('0');

            }

            var $formPrincipal = $("#frmPrincipal").validate({
                rules: {
                    codSilais: {
                        required: true
                    },
                    codigoMunicipio: {
                        required: true
                    },
                    codUnidadSalud: {
                        required: true
                    },
                    fecInicioEncuesta: {
                        required: true,
                        dpDate: true,
                        dpCompareDate: ['before', '#fecFinEncuesta', 'notAfter', parametros.dFechaHoy ]
                    },
                    fecFinEncuesta: {
                        required: true,
                        dpDate: true,
                        dpCompareDate: {after: '#fecInicioEncuesta', 'notAfter': parametros.dFechaHoy}

                    },
                    codOrdinal: {
                        required: true
                    },
                    codProcedencia: {
                        required: true
                    },
                    semanaEpi: {
                        digits: true,
                        maxlength: 2
                    },
                    mesEpi: {
                        digits: true,
                        maxlength: 2
                    },
                    anioEpi: {
                        digits: true,
                        maxlength: 4
                    }
                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }
            });

            var $formDetalleEncuesta = $("#frmDetalleEncuesta").validate({
                rules: {
                    codigoLocalidad:{
                        required: true
                    },
                    txtPila:{
                        required: true,
                        digits: true
                    },
                    txtLlanta:{
                        required: true,
                        digits: true
                    },
                    txtBarril:{
                        required: true,
                        digits: true
                    },
                    txtFlorero:{
                        required: true,
                        digits: true
                    },
                    txtBebedero:{
                        required: true,
                        digits: true
                    },
                    txtArt_Esp:{
                        required: true,
                        digits: true
                    },
                    txtO_Dep:{
                        required: true,
                        digits: true
                    },
                    txtCister:{
                        required: true,
                        digits: true
                    },
                    txtInodoro:{
                        required: true,
                        digits: true
                    },
                    txtBarro: {
                        required: true,
                        digits: true
                    },
                    txtPlanta: {
                        required: true,
                        digits: true
                    },
                    txtArbol: {
                        required: true,
                        digits: true
                    },
                    txtPozo: {
                        required: true,
                        digits: true
                    },
                    txtManzanas: {
                        required: true,
                        digits: true
                    },
                    txtViviendas: {
                        required: true,
                        digits: true
                    }
                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                },
                submitHandler: function (form) {
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    saveSurvey();
                }
            });

            $("#btnNuevoRegistro").click(function(){
                limpiarCamposMaestro();
                limpiarTablasDetalle();
                $("#msjMaestro").hide("slow");
            });

            $("#mensaje").hide();

            //mostrar modal detalle encuesta
            $("#openModal").click(function(){
                var $valid = $("#frmPrincipal").valid();
                if (!$valid) {
                    $formPrincipal.focusInvalid();
                    return false;
                }else {
                    if ($("#idMaestroAgregado").val() == null || $("#idMaestroAgregado").val().trim().length<=0){
                        //Validar si existe maestro
                        var maestro = {
                            encuestaId: '' ,
                            codSilais: $('#codSilais option:selected').val(),
                            codMunicipio: $('#codigoMunicipio option:selected').val(),
                            codDistrito: $('#codigoDistrito option:selected').val(),
                            codArea: $('#codigoArea option:selected').val(),
                            codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                            codProcedencia: $('#codProcedencia option:selected').val(),
                            feInicioEncuesta: $('#fecInicioEncuesta').val(),
                            feFinEncuesta: $('#fecFinEncuesta').val(),
                            codOrdinalEncu: $('#codOrdinal option:selected').val(),
                            codModeloEncu: 1,
                            semanaEpi: $('#semanaEpi').val(),
                            mesEpi: $('#mesEpi').val(),
                            anioEpi: $('#anioEpi').val(),
                            usuarioRegistroId: 1
                        };
                        var encuestaObj = {};
                        encuestaObj['maestroEncuesta'] = maestro;
                        $.getJSON(parametros.sSurveyHeaderUrl, {
                            maestroEncuesta: JSON.stringify(encuestaObj),
                            ajax: 'true'
                        }, function (data) {
                            var html = '';
                            var len = data.length;
                            if (len > 0) {
                                html = '<div class="alert alert-block alert-warning"> ' +
                                    '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                    '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                    '<p> ' + $("#msg_header_exist").val() +
                                    '<a class="" href="'+parametros.sEditSurveyUrl + '?idMaestro=' + data[0].encuestaId + '">Editar</a>' +
                                    '</p> ' +
                                    '</div>';
                                $('#msjMaestro').html(html).show().focus();
                                //$("#msjMaestro").show("slow");
                                //$("#msjMaestro").trigger("focus");
                            } else {
                                $("#msjMaestro").hide("slow");
                                $("#myModal").modal({
                                    show: true
                                });
                            }
                        })
                    }else{
                        $("#myModal").modal({
                            show: true
                        });
                    }
                }
            });

            <!-- al seleccionar municipio -->
            $('#codigoMunicipio').change(function(){
                $('#codigoDistrito').val('').change();
                $('#codigoArea').val('').change();
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sComunidadesUrl, {
                        municipioId: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            //html += '</option>';
                        }
                        $('#codigoLocalidad').html(html);
                    });
                    $.getJSON(parametros.sDistritosUrl, {
                        codMunicipio: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].valor
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoDistrito').html(html);
                    });
                    $.getJSON(parametros.sAreasUrl, {
                        codMunicipio: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].valor
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoArea').html(html);
                    })
                }
            });

            <!-- al seleccionar SILAIS -->
            $('#codSilais').change(function(){
                $('#codigoMunicipio').val('').change();
                $('#codUnidadSalud').val('').change();
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sMunicipiosUrl, {
                        idSilais: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigoNacional + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codigoMunicipio').html(html);
                    });
                    $.getJSON(parametros.sUnidadesUrl, {
                        silaisId: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            html += '</option>';
                        }
                        $('#codUnidadSalud').html(html);
                    });
                }
            });

            <!-- al seleccionar localidad -->
            $('#codigoLocalidad').change(function() {
                /*if ($("#idMaestroAgregado").val().length > 0 && $(this).val().length > 0){
                    var maestro = {
                        encuestaId:'' ,
                        codSilais: $('#codSilais option:selected').val(),
                        codMunicipio: $('#codigoMunicipio option:selected').val(),
                        codDistrito: $('#codigoDistrito option:selected').val(),
                        codArea: $('#codigoArea option:selected').val(),
                        codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                        codProcedencia: $('#codProcedencia option:selected').val(),
                        feInicioEncuesta: $('#fecInicioEncuesta').val(),
                        feFinEncuesta: $('#fecFinEncuesta').val(),
                        codOrdinalEncu: $('#codOrdinal option:selected').val(),
                        codModeloEncu: 1,
                        semanaEpi: $('#semanaEpi').val(),
                        mesEpi: $('#mesEpi').val(),
                        anioEpi: $('#anioEpi').val(),
                        usuarioRegistroId: 1
                    };
                    var encuestaObj = {};
                    encuestaObj['idLocalidad'] = $(this).val();
                    encuestaObj['idMaestroEncuesta'] = $("#idMaestroAgregado").val();
                    //encuestaObj['maestroEncuesta'] = maestro;
                    $.getJSON(parametros.sValidarLocalidadUrl, {
                        datosEncuesta: JSON.stringify(encuestaObj),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        if (len > 0) {
                            html = '<div class="alert alert-block alert-warning"> ' +
                                '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                '<p> ' + $("#msg_location_exist").val() +
                                '</p> ' +
                                '</div>';
                            $('#mensaje').html(html).show().focus();
                            //$("#mensaje").show("slow");
                            //$("#mensaje").focus();
                            //$("#mensaje").fadeOut(5000);
                            setTimeout(function(){$("#mensaje").hide("slow");}, 4000);
                            $("#codigoLocalidad").val('').change();
                        } else {
                            $("#mensaje").hide("slow");
                        }
                    })
                }
                */
            });

            <!-- al ingresar fecha de inicio de encuesta-->
            $('#fecInicioEncuesta').change(function () {
                var fecha = $('#fecInicioEncuesta').val();
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

        }
    };
}();