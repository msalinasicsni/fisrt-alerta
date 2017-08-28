var ReportGeneralNoti = function () {
    var bloquearUI = function(mensaje){
        var loc = window.location;
        var pathName = loc.pathname.substring(0,loc.pathname.indexOf('/', 1)+1);
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

    var desbloquearUI = function() {
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
            var text_selected_all = $("#text_selected_all").val();
            var text_selected_none = $("#text_selected_none").val();
            var table1 = $('#notices_result').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+ //"sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'T>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,
                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#notices_result'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                },
                "oTableTools": {
                    "aButtons": [
                        {
                            "sExtends": "pdf",
                            "sTitle": "Notificaciones registradas",
                            "sPdfSize": "A4",
                            "sPdfOrientation": "landscape"
                        }
                    ],
                    "sSwfPath": parametros.sTableToolsPath
                }
            });

            $('#searchOrders-form').validate({
                // Rules for form validation
                rules: {
                    fechaFin:{required:function(){return $('#fechaInicio').val().length>0;}},
                    fechaInicio:{required:function(){return $('#fechaFin').val().length>0;}}
                },
                // Do not change code below
                errorPlacement : function(error, element) {
                    error.insertAfter(element.parent());
                },
                submitHandler: function (form) {
                    table1.fnClearTable();
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    getNotifications(false)
                }
            });

            function blockUI(){
                var loc = window.location;
                var pathName = loc.pathname.substring(0,loc.pathname.indexOf('/', 1)+1);
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

            function getNotifications(showAll) {
                var notificacionesFiltro = {};
                if (showAll){
                    //notificacionesFiltro['nombreApellido'] = '';
                    notificacionesFiltro['fechaInicio'] = '';
                    notificacionesFiltro['fechaFin'] = '';
                    notificacionesFiltro['codSilais'] = '';
                    notificacionesFiltro['codUnidadSalud'] = '';
                    notificacionesFiltro['codTipoMx'] = '';
                    notificacionesFiltro['codTipoSolicitud']= '';
                    notificacionesFiltro['nombreSolicitud']= '';
                }else {
                    //notificacionesFiltro['nombreApellido'] = $('#txtfiltroNombre').val();
                    notificacionesFiltro['fechaInicio'] = $('#fechaInicio').val();
                    notificacionesFiltro['fechaFin'] = $('#fechaFin').val();
                    notificacionesFiltro['codSilais'] = $('#codSilais').find('option:selected').val();
                    notificacionesFiltro['codUnidadSalud'] = $('#codUnidadSalud').find('option:selected').val();
                    notificacionesFiltro['tipoNotificacion'] = $('#tipoNotificacion').find('option:selected').val();
                    //notificacionesFiltro['codTipoSolicitud'] = $('#tipo option:selected').val();
                    //notificacionesFiltro['nombreSolicitud'] = $('#nombreSoli').val();
                }
                blockUI();
                console.log(JSON.stringify(notificacionesFiltro));
                $.getJSON(parametros.notificacionesUrl, {
                    strFilter: JSON.stringify(notificacionesFiltro),
                    ajax : 'true'
                }, function(dataToLoad) {
                    table1.fnClearTable();
                    var len = Object.keys(dataToLoad).length;
                    if (len > 0) {
                        for (var i = 0; i < len; i++) {
                            var actionUrl;
                            switch (dataToLoad[i].codtipoNoti) {
                                case 'TPNOTI|SINFEB':
                                    actionUrl = parametros.febrilesUrl+dataToLoad[i].idNotificacion;
                                    break;
                                case 'TPNOTI|IRAG':
                                    actionUrl = parametros.iragUrl+dataToLoad[i].idNotificacion;
                                    break;
                                case 'TPNOTI|PCNT':
                                    actionUrl = parametros.pacienteUrl+dataToLoad[i].idNotificacion;
                                    break;
                                default:
                                    actionUrl = '#';
                                    break;
                            }


                            table1.fnAddData(
                                [dataToLoad[i].persona, dataToLoad[i].edad, dataToLoad[i].sexo,dataToLoad[i].embarazada,dataToLoad[i].municipio, dataToLoad[i].tipoNoti, dataToLoad[i].silais, dataToLoad[i].unidad,
                                    dataToLoad[i].fechaRegistro, dataToLoad[i].fechaInicioSintomas,dataToLoad[i].conResultado,
                                        '<a target="_blank" title="Ver" href=' + actionUrl + ' class="btn btn-primary btn-xs"><i class="fa fa-mail-forward"></i></a>']);
                        }
                    }else{
                        $.smallBox({
                            title: $("#msg_no_results_found").val() ,
                            content: $("#smallBox_content").val(),
                            color: "#C79121",
                            iconSmall: "fa fa-warning",
                            timeout: 4000
                        });
                    }
                    unBlockUI();
                })
                    .fail(function (XMLHttpRequest, textStatus, errorThrown) {
                        unBlockUI();
                        showMessage("FAIL", errorThrown, "#C46A69", "fa fa-warning", 8000);
                    });
            }

            function showMessage(title,content,color,icon,timeout){
                $.smallBox({
                    title: title,
                    content: content,
                    color: color,
                    iconSmall: icon,
                    timeout: timeout
                });
            }

            $("#all-orders").click(function() {
                getNotifications(true);
            });

            <!-- al seleccionar SILAIS -->
            $('#codSilais').change(function(){
                blockUI();
                if ($(this).val().length > 0) {
                    $.getJSON(parametros.sUnidadesUrl, {
                        codSilais: $(this).val(),
                        ajax: 'true'
                    }, function (data) {
                        var html = null;
                        var len = data.length;
                        html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                        for (var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].codigo + '">'
                                + data[i].nombre
                                + '</option>';
                            // html += '</option>';
                        }
                        $('#codUnidadSalud').html(html);
                    })
                }else{
                    var html = '<option value="">' + $("#text_opt_select").val() + '...</option>';
                    $('#codUnidadSalud').html(html);
                }
                $('#codUnidadSalud').val('').change();
                unBlockUI();
            });
        }
    };

}();

