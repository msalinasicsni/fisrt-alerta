var ResultsRT = function () {
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
            var table1 = $('#noti_results').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true,
                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#noti_results'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }

            });

            getResults($('#idPerson').val());


            function getResults(idPerson) {
                $.getJSON(parametros.getResultsUrl, {
                    ajax: 'true',
                    idPerson: idPerson
                }, function (data) {
                    table1.fnClearTable();
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        console.log(data);

                        var editUrl = parametros.editUrl +  data[i].daNotificacion.idNotificacion;
                        var btnEdit = '<a href=' + editUrl + ' class="btn btn-xs btn-primary" ><i class="fa fa-edit"></i></a>';



                        var btnPdf = '<button type="button" class="btn btn-success btn-xs" data-id="' + data[i].daNotificacion.idNotificacion +
                            '" > <i class="fa fa-file-pdf-o"></i>';

                        var btnOverride = ' <button type="button" class="btn btn-xs btn-danger" data-id="' + data[i].daNotificacion.idNotificacion +
                            '"> <i class="fa fa-times"></i>';

                        var pasivo = '<span class="label label-success"><i class="fa fa-thumbs-up fa-lg"></i></span>';
                        if (data[i].daNotificacion.pasivo == true) {
                            pasivo = '<span class="label label-danger"><i class="fa fa-thumbs-down fa-lg"></i></span>';

                            btnOverride = ' <button type="button" disabled class="btn btn-xs btn-danger"> <i class="fa fa-times"></i>';

                            btnEdit = '<a href=' + editUrl + ' disabled class="btn btn-xs btn-primary" ><i class="fa fa-edit"></i></a>';
                        }else{
                            //se valida si el id de la notificación está autorizada para edición por parte del usuario
                            var path = new RegExp(data[i].daNotificacion.idNotificacion);
                            console.log(parametros.fichasAutorizadas);
                            console.log(path);
                            var esAutorizada = path.test(parametros.fichasAutorizadas);
                            if (!esAutorizada){
                                btnOverride = ' <button type="button" disabled class="btn btn-xs btn-danger"> <i class="fa fa-times"></i>';
                            }
                        }

                        table1.fnAddData(
                            [data[i].numExpediente,
                                data[i].fechaIngreso,
                                pasivo,
                                data[i].daNotificacion.codUnidadAtencion.nombre,
                                data[i].daNotificacion.persona.primerNombre,
                                data[i].daNotificacion.persona.segundoNombre,
                                data[i].daNotificacion.persona.primerApellido,
                                data[i].daNotificacion.persona.segundoApellido,
                                btnEdit,
                                btnOverride ]);


                    }

                });
            }


        }
    };

}();
