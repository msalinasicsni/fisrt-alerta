/**
 * Created by souyen-ics on 08-13-15.
 */
var IragResults = function () {

    return {
        //main function to initiate the module
        init: function (parametros) {

            function blockUI() {
                var loc = window.location;
                var pathName = loc.pathname.substring(0, loc.pathname.indexOf('/', 1) + 1);
                //var mess = $("#blockUI_message").val()+' <img src=' + pathName + 'resources/img/loading.gif>';
                var mess = '<img src=' + pathName + 'resources/img/ajax-loading.gif> ' + parametros.blockMess;
                $.blockUI({ message: mess,
                    css: {
                        border: 'none',
                        padding: '15px',
                        backgroundColor: '#000',
                        '-webkit-border-radius': '10px',
                        '-moz-border-radius': '10px',
                        opacity: .5,
                        color: '#fff'
                    }});
            }

            function unBlockUI() {
                setTimeout($.unblockUI, 500);
            }

            getResults($('#idPerson').val());

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

                "columns": [
                    null, null, null, null, null, null, null, null,null,

                    {
                        "className": 'fPdf',
                        "orderable": false
                    },
                    {
                        "className": 'tomaMx',
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
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#noti_results'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                },

                fnDrawCallback: function () {
                    $('.fPdf')
                        .off("click", pdfHandler)
                        .on("click", pdfHandler);
                    $('.tomaMx')
                        .off("click", tomaMxHandler)
                        .on("click", tomaMxHandler);
                    $('.fOverride')
                        .off("click", overrideHandler)
                        .on("click", overrideHandler);

                }


            });

            function tomaMxHandler() {
                var id = $(this.innerHTML).data('id');
                console.log(id);
                if (id != null) {
                    validarTomaMx(id);
                }
            }

            function pdfHandler() {
                var id = $(this.innerHTML).data('id');
                if (id != null) {
                    exportPDF(id);
                }
            }

            function overrideHandler() {
                var id = $(this.innerHTML).data('id');
                if (id != null) {
                    //$('#overrideUrl').val(id);
                    //$('#d_confirmacion').modal('show');
                    validarAnulacionTomaMx(id);
                }
            }

            $('#btnOverride').click(function () {
                window.location.href = parametros.overrideUrl + $('#overrideUrl').val();
            });

            $('#agregarNoti').click(function () {
                if ($("#incompleta").val()==='true'){
                    $.smallBox({
                        title: $('#msgIncompleta').val(),
                        content:  $('#smallBox_content').val(),
                        color: "#C79121",
                        iconSmall: "fa fa-warning",
                        timeout: 4000
                    });
                }else{
                    window.location.href = parametros.newUrl;
                }
            });

            function getResults(idPerson) {
                $.getJSON(parametros.getResultsUrl, {
                    ajax: 'true',
                    idPerson: idPerson
                }, function (data) {
                    table1.fnClearTable();
                    var len = data.length;
                    for (var i = 0; i < len; i++) {


                        var editUrl = parametros.editUrl + data[i].idNotificacion.idNotificacion;
                        //var tomaMxUrl = parametros.createMxUrl + data[i].idNotificacion.idNotificacion;

                        var btnEdit = '<a title="Editar" href=' + editUrl + ' class="btn btn-xs btn-primary" ><i class="fa fa-edit"></i></a>';

                        var btnPdf = '<button type="button" title="Ficha en PDF" class="btn btn-success btn-xs" data-id="' + data[i].idNotificacion.idNotificacion +
                            '" > <i class="fa fa-file-pdf-o"></i>';

                        //var btnTomaMx = '<a target="_blank" title="Tomar Mx" href=' + tomaMxUrl + ' class="btn btn-xs btn-primary" ><i class="fa fa-eyedropper"></i></a>';
                        var btnTomaMx = '<a title="Tomar Mx" class="btn btn-primary btn-xs tomaMx" data-id='+data[i].idNotificacion.idNotificacion+'><i class="fa fa-eyedropper fa-fw"></i></a>';

                        var btnOverride = '<button title="Anular" type="button" class="btn btn-danger btn-xs" data-id="' + data[i].idNotificacion.idNotificacion +
                            '" > <i class="fa fa-times"></i>';

                        var pasivo = '<span class="label label-success"><i class="fa fa-thumbs-up fa-lg"></i></span>';
                        var completa = '<span class="label label-success"><i class="fa fa-thumbs-up fa-lg"></i></span>';
                        if (data[i].idNotificacion.completa == false) {
                            completa = '<span class="label label-danger"><i class="fa fa-thumbs-down fa-lg"></i></span>';
                        }
                        if (data[i].idNotificacion.pasivo == true) {
                            pasivo = '<span class="label label-danger"><i class="fa fa-thumbs-down fa-lg"></i></span>';

                            btnEdit = '<a  title="Editar" href=' + editUrl + ' disabled class="btn btn-xs btn-primary" ><i class="fa fa-edit"></i></a>';
                            btnTomaMx = '<a  title="Tomar Mx" href="#" disabled class="btn btn-xs btn-primary" ><i class="fa fa-eyedropper"></i></a>';
                            btnOverride = ' <button type="button" disabled class="btn btn-xs btn-danger"> <i class="fa fa-times"></i>';

                        } else {
                            //se valida si el id de la notificación está autorizada para edición por parte del usuario
                            var path = new RegExp(data[i].idNotificacion.idNotificacion);
                            var esAutorizada = path.test(parametros.iragAutorizadas);
                            if (!esAutorizada) {
                                btnOverride = ' <button type="button" disabled class="btn btn-xs btn-danger"> <i class="fa fa-times"></i>';
                            }
                        }

                        //se formate la fecha de reporte de la ficha a un formato legible
                        var fechaFormateada = '';
                        if (data[i].fechaConsulta!=null) {
                            var fechaFicha = new Date(data[i].fechaConsulta);
                            fechaFormateada = (fechaFicha.getDate() < 10 ? '0' + fechaFicha.getDate() : fechaFicha.getDate())
                                + '/' + (fechaFicha.getMonth() + 1 < 10 ? '0' + (fechaFicha.getMonth() + 1) : fechaFicha.getMonth() + 1)
                                + '/' + fechaFicha.getFullYear();
                        }
                        //se valida si hay unidad de atención
                        var nombreUnidad='';
                        if (data[i].idNotificacion.codUnidadAtencion!=null){
                            nombreUnidad = data[i].idNotificacion.codUnidadAtencion.nombre;
                        }
                        table1.fnAddData(
                            [fechaFormateada, pasivo, completa, data[i].codExpediente, nombreUnidad, data[i].idNotificacion.persona.primerNombre, data[i].idNotificacion.persona.primerApellido, data[i].idNotificacion.persona.segundoApellido, btnEdit, btnPdf, btnTomaMx, btnOverride  ]);

                    }

                });
            }

            function validarAnulacionTomaMx (idNotificacion){
                blockUI();
                $.getJSON(parametros.tomaMxUrl, {
                    idNotificacion: idNotificacion,
                    ajax: 'true'
                }, function (data) {
                    //var actionUrl = parametros.createMxUrl + idNotificacion;
                    unBlockUI();
                    var len = data.length;
                    if(len > 0) {
                        var permitir = true;
                        for (var i = 0; i < len; i++) {
                            //estados ya dentro del sistema de laboratorio
                            if (data[i].estadoMx.codigo=='ESTDMX|RCP' ||
                                data[i].estadoMx.codigo=='ESTDMX|TRAS' ||
                                data[i].estadoMx.codigo=='ESTDMX|EPLAB' ||
                                data[i].estadoMx.codigo=='ESTDMX|RCLAB'){
                                $.smallBox({
                                    title: $('#msgNoOverride').val(),
                                    content: "<i class='fa fa-clock-o'></i> <i>" + $("#smallBox_content").val() + "</i>",
                                    color: "#C79121",
                                    iconSmall: "fa fa-times fa-2x fadeInRight animated",
                                    timeout: 4000
                                });
                                permitir = false;
                                break;
                            }
                        }
                        if (permitir){
                            $('#overrideUrl').val(idNotificacion);
                            $('#d_confirmacion').modal('show');
                        }
                    }else{
                        $('#overrideUrl').val(idNotificacion);
                        $('#d_confirmacion').modal('show');
                    }

                });
            }

            function validarTomaMx (idNotificacion){
                blockUI();
                $.getJSON(parametros.tomaMxUrl, {
                    idNotificacion: idNotificacion,
                    ajax: 'true'
                }, function (data) {
                    var actionUrl = parametros.createMxUrl + idNotificacion;
                    unBlockUI();
                    if(data.length > 0){
                        var opcSi = $("#inYes").val();
                        var opcNo = $("#inNo").val();
                        $.SmartMessageBox({

                            title: $('#titleC').val(),
                            content: $('#contentC').val(),
                            buttons: '['+opcSi+']['+opcNo+']'
                        }, function (ButtonPressed) {
                            if (ButtonPressed === opcSi) {
                                window.location.href = actionUrl;

                            }
                            if (ButtonPressed === opcNo) {
                                $.smallBox({
                                    title: $('#titleCancel').val(),
                                    content: "<i class='fa fa-clock-o'></i> <i>"+$("#smallBox_content").val()+"</i>",
                                    color: "#C46A69",
                                    iconSmall: "fa fa-times fa-2x fadeInRight animated",
                                    timeout: 4000
                                });
                            }

                        });
                    }else{
                        window.location.href =  actionUrl;
                    }

                });
            }

            function exportPDF(id) {
                blockUI();
                $.ajax(
                    {
                        url: parametros.pdfUrl,
                        type: 'GET',
                        dataType: 'text',
                        data: {idNotificacion: id},
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if (data.length != 0) {
                                var blob = blobData(data, 'application/pdf');
                                var blobUrl = showBlob(blob);
                            }

                            unBlockUI();
                        },
                        error: function (data, status, er) {
                            unBlockUI();
                            alert("error: " + data + " status: " + status + " er:" + er);
                        }
                    });


            }


        }
    };

}();