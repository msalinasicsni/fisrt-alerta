/**
 * Created by FIRSTICT on 8/24/2015.
 */
var PatientDetail = function () {
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
            /*PARA MOSTRAR TABLA DETALLE RESULTADO*/
            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet: 1024,
                phone: 480
            };
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
                $.getJSON(parametros.sPacienteResultsUrl, {
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

            /// PARA MOSTRAR TABLA DATOS SOLICITUDES
            var table2 = $('#lista_datos').dataTable({
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
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#lista_datos'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });

            function formatDato (d,indice) {
                // `d` is the original data object for the row
                var texto = d[indice]; //indice donde esta el input hidden
                var resultado = $(texto).val();
                var json =JSON.parse(resultado);
                var len = Object.keys(json).length;
                var childTable = '<table style="padding-left:20px;border-collapse: separate;border-spacing:  10px 3px;">'+
                    '<tr><td style="font-weight: bold">'+$('#data_response').val()+'</td><td style="font-weight: bold">'+$('#data_value').val()+'</td><td style="font-weight: bold">'+$('#data_date').val()+'</td></tr>';
                for (var i = 0; i < len; i++) {
                    childTable =childTable +
                        '<tr></tr><tr><td>'+json[i].respuesta+'</td><td>'+json[i].valor+'</td><td>'+json[i].fechaResultado+'</td></tr>';
                }
                childTable = childTable + '</table>';
                return childTable;
            }

            $('#lista_datos tbody').on('click', 'td.details-control', function () {
                var tr = $(this).closest('tr');
                var row = table2.api().row(tr);
                if ( row.child.isShown() ) {
                    // This row is already open - close it
                    row.child.hide();
                    tr.removeClass('shown');
                }
                else {
                    // Open this row
                    row.child( formatDato(row.data(),6)).show();
                    tr.addClass('shown');
                }
            } );

            function getDatosSolicitudesNoti(){
                $.getJSON(parametros.sDatosSolicitudUrl, {
                    strIdNotificacion: $('#idNotificacion').val(),
                    ajax : 'true'
                }, function(dataToLoad) {
                    table2.fnClearTable();
                    var len = Object.keys(dataToLoad).length;
                    if (len > 0) {
                        for (var i = 0; i < len; i++) {
                            table2.fnAddData(
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

            getDatosSolicitudesNoti();
            /// FIN MOSTRAR TABLA DATOS SOLICITUDES

            var wizard = $('#wizard').wizard();

            wizard.on('finished', function (e, data) {
                window.location.href = parametros.sPacienteSearchUrl + $('#personaId').val();
            });
        }
    };

}();