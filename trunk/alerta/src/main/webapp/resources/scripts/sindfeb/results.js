/**
 * Created by souyen-ics on 07-29-15.
 */
var Results = function () {
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
            getResults($('#idPerson').val());

            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet: 1024,
                phone: 480
            };
            var table1 = $('#fichas_result').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true,
                "columns": [
                    null, null, null, null, null, null, null, null, null,

                    {
                        "className": 'fPdf',
                        "orderable": false
                    },
                    {
                        "className": 'fOverride',
                        "orderable": false
                    }


                ],

                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#fichas_result'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                },

                fnDrawCallback : function() {
                    $('.fPdf')
                        .off("click", pdfHandler)
                        .on("click", pdfHandler);

                }


            });

            function pdfHandler(){
                var id = $(this.innerHTML).data('id');
                if (id != null) {
                   exportPDF(id);
                }
            }


            function getResults(idPerson) {
                $.getJSON(parametros.getResultsUrl, {
                    ajax: 'true',
                    idPerson: idPerson
                }, function (data) {
                    table1.fnClearTable();
                    var len = data.length;
                    for (var i = 0; i < len; i++) {


                        var editUrl = parametros.editUrl +  data[i].idNotificacion.idNotificacion;
                        var btnEdit = '<a href=' + editUrl + ' class="btn btn-xs btn-primary" ><i class="fa fa-edit"></i></a>';



                        var btnPdf = '<button type="button" class="btn btn-success btn-xs" data-id="' + data[i].idNotificacion.idNotificacion +
                            '" > <i class="fa fa-file-pdf-o"></i>';

                        var btnOverride = ' <button type="button" class="btn btn-xs btn-danger" data-id="' + data[i].idNotificacion.idNotificacion +
                            '"> <i class="fa fa-times"></i>';

                        var pasivo = '<span class="label label-success"><i class="fa fa-thumbs-up fa-lg"></i></span>';
                        if (data[i].idNotificacion.pasivo == true) {
                            pasivo = '<span class="label label-danger"><i class="fa fa-thumbs-down fa-lg"></i></span>';

                            btnOverride = ' <button type="button" disabled class="btn btn-xs btn-danger"> <i class="fa fa-times"></i>';

                            btnEdit = '<a href=' + editUrl + ' disabled class="btn btn-xs btn-primary" ><i class="fa fa-edit"></i></a>';                        }

                        table1.fnAddData(
                            [data[i].numFicha, data[i].fechaFicha, pasivo, data[i].codExpediente, data[i].idNotificacion.codUnidadAtencion.nombre, data[i].idNotificacion.persona.primerNombre, data[i].idNotificacion.persona.primerApellido, data[i].idNotificacion.persona.segundoApellido, btnEdit, btnPdf, btnOverride ]);


                    }

                });
            }

            function exportPDF(id) {
                $.ajax(
                    {
                        url: parametros.pdfUrl,
                        type: 'GET',
                        dataType: 'text',
                        data: {idNotificacion: id},
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if(data.length != 0){
                                var blob =   blobData(data, 'application/pdf');
                                var blobUrl =  showBlob(blob);


                            }

                           desbloquearUI();
                        },
                        error: function (data, status, er) {
                            desbloquearUI();
                            alert("error: " + data + " status: " + status + " er:" + er);
                        }
                    });


            }


        }
    };

}();