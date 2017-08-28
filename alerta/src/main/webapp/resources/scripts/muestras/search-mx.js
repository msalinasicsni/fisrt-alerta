/**
 * Created by FIRSTICT on 7/22/2017.
 */

var SearchMx = function () {
    return {

        //main function to initiate the module
        init: function (parametros) {
            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet: 1024,
                phone: 480
            };

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

            <!-- al seleccionar SILAIS -->
            $('#codSilais').change(function () {
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
                    }).fail(function (jqXHR) {
                        setTimeout($.unblockUI, 10);
                        validateLogin(jqXHR);
                    });
                } else {
                    var html = '<option value="">' + $("#text_opt_select").val() + '...</option>';
                    $('#codUnidadSalud').html(html);
                }
                $('#codUnidadSalud').val('').change();
                unBlockUI();
            });

            var table = $('#mx_list').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-6 hidden-xs'T>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-sm-6 col-xs-12'p>>",
                "aaSorting": [],

                "oTableTools": {
                    "aButtons": [
                        {
                            "sExtends": "xls",
                            "sTitle": "estado_muestras"
                        }

                    ],
                    "sSwfPath": parametros.sTableToolsPath
                },

                "autoWidth": true,

                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#mx_list'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }

            });

            <!-- filtro Solicitudes -->
            $('#searchmx_form').validate({
                // Rules for form validation
                rules: {
                    inicioToma: {required: function () {
                        return $('#finToma').val().length > 0;
                    }},
                    finToma: {required: function () {
                        return $('#inicioToma').val().length > 0;
                    }}
                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());
                },
                submitHandler: function (form) {
                    table.fnClearTable();
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    codigos = "";
                    getMxs(false)
                }
            });

            function getMxs(showAll) {
                var mxFiltros = {};
                if (showAll) {

                    mxFiltros['fechaInicioToma'] = '';
                    mxFiltros['fechaFinToma'] = '';
                    mxFiltros['codSilais'] = '';
                    mxFiltros['codUnidadSalud'] = '';
                    mxFiltros['nombreSolicitud'] = '';
                    mxFiltros['nombrePersona'] = '';
                } else {
                    mxFiltros['fechaInicioToma'] = $('#inicioToma').val();
                    mxFiltros['fechaFinToma'] = $('#finToma').val();
                    mxFiltros['codSilais'] = $('#codSilais').find('option:selected').val();
                    mxFiltros['codUnidadSalud'] = $('#codUnidadSalud').find('option:selected').val();
                    mxFiltros['nombreSolicitud'] = encodeURI($('#nombreSol').val());
                    mxFiltros['nombrePersona'] = $('#txtfiltroNombre').val();
                }

                blockUI();
                $.getJSON(parametros.searchUrl, {
                    strFilter: JSON.stringify(mxFiltros),
                    ajax: 'true'
                }, function (dataToLoad) {
                    table.fnClearTable();
                    var len = Object.keys(dataToLoad).length;

                    if (len > 0) {
                        for (var i = 0; i < len; i++) {
                            var actionUrl = parametros.editUrl+dataToLoad[i].idTomaMx;
                            table.fnAddData(
                                [dataToLoad[i].codigoUnicoMx , dataToLoad[i].fechaTomaMx, dataToLoad[i].tipoNoti, dataToLoad[i].codSilais, dataToLoad[i].codUnidadSalud,
                                    dataToLoad[i].persona, dataToLoad[i].laboratorio, dataToLoad[i].estadoMx, dataToLoad[i].solicitudes,
                                        '<a target="_blank" title="Editar" href=' + actionUrl + ' class="btn btn-primary btn-xs"><i class="fa fa-edit"></i></a>']);
                        }

                    } else {
                        $.smallBox({
                            title: $("#msg_no_results_found").val(),
                            content: $("#smallBox_content").val(),
                            color: "#C79121",
                            iconSmall: "fa fa-warning",
                            timeout: 4000
                        });
                    }
                    unBlockUI();
                })
                    .fail(function (jqXHR) {
                        setTimeout($.unblockUI, 10);
                    });
            }

        }
    };

}();