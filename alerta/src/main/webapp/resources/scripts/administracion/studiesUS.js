/**
 * Created by souyen-ics on 09-17-15.
 */

/**
 * Created by souyen-ics on 07-02-15.
 */

var StudiesUS  = function () {
    return {
        init: function (parametros) {
            getStudies();

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
                    },
                    baseZ: 1051
                });
            }

            function unBlockUI() {
                setTimeout($.unblockUI, 500);
            }

            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet : 1024,
                phone : 480
            };
            var catalogueTable = $('#records').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,

                "columns": [
                    null, null,
                    {
                        "className":      'detail',
                        "orderable":      false
                    }


                ],

                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#records'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                },


                fnDrawCallback : function() {
                    $('.detail')
                        .off("click", detailUs)
                        .on("click", detailUs);


                }

            });

            function detailUs(){
                var data =  $(this.innerHTML).data('id');
                if(data != null){
                    var detalle = data.split(",");
                    var id= detalle[0];
                    var nombre = detalle[1];
                    $('#estudio').html(nombre);
                    $('#div1').hide();
                    $('#div2').fadeIn('slow');
                    $('#dBack').show();
                    getUS(id);
                    $('#idEst').val(id);

                }

            }

            $('#btnBack').click(function() {
                $('#dBack').hide();
                $('#estudio').html("");
                $('#div2').hide();
                $('#div1').fadeIn('slow');
                $('#idEst').val('');

            });

            var tableUs = $('#table-us').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth" : true,

                "columns": [
                    null, null, null,
                    {
                        "className":      'overrideUS',
                        "orderable":      false
                    }

                ],

                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#table-us'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                },


               fnDrawCallback : function() {
                    $('.overrideUS')
                        .off("click", override)
                        .on("click", override);

                }

            });


            function getStudies() {
                $.getJSON(parametros.catalogueUrl, {
                    ajax: 'true'
                }, function (data) {
                    catalogueTable.fnClearTable();
                    var len = Object.keys(data).length;
                    for (var i = 0; i < len; i++) {

                        var btnDetail = '<button type="button" title="Ver unidades asociadas" class="btn btn-primary btn-xs" data-id="'+data[i].idEstudio+ "," + data[i].nombre +
                            '" > <i class="fa fa-list"></i>' ;

                        catalogueTable.fnAddData(
                            [data[i].nombre, data[i].area, btnDetail]);


                    }
                })
            }

            function getUS(id) {
                $.getJSON(parametros.usUrl, {
                    ajax: 'true',
                    idEstudio: id
                }, function (data) {
                    tableUs.fnClearTable();
                    var len = Object.keys(data).length;
                    for (var i = 0; i < len; i++) {
                        var btnOverride = '<button type="button" title="Anular" class="btn btn-danger btn-xs" data-id="'+data[i].idEstudioUnidad +
                            '" > <i class="fa fa-times"></i>' ;

                        tableUs.fnAddData(
                            [data[i].silais, data[i].municipio, data[i].nombreUS, btnOverride]);
                    }
                })
            }

            function showModal(){
                $("#myModal").modal({
                    show: true
                });
            }

            $('#btnAddUs').click(function() {
                showModal();
            });

            <!-- Validacion formulario -->
            var $validator = $("#form").validate({
                // Rules for form validation
                rules: {
                    codUnidadAtencion: {required : true},
                    codMunicipio: {required: true},
                    codSilaisAtencion:{required:true}
                },
                // Do not change code below
                errorPlacement : function(error, element) {
                    error.insertAfter(element.parent());
                }
            });

            $('#btnAddTest').click(function() {
                var $validarModal = $("#form").valid();
                if (!$validarModal) {
                    $validator.focusInvalid();
                    return false;
                } else {
                    addUs();
                }
            });

            function override() {
                var data = $(this.innerHTML).data('id');
                if (data != null) {
                    overrideUs(data);
                }
            }



            function addUs() {
                var obj = {};
                obj['mensaje'] = '';
                obj['idEstudio'] = $('#idEst').val();
                obj['us'] = $('#codUnidadAtencion').val();
                obj['idRecord'] = '';


                blockUI(parametros.blockMess);
                $.ajax(
                    {
                        url: parametros.saveEstUsUrl,
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(obj),
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if (data.mensaje.length > 0) {
                                $.smallBox({
                                    title: data.mensaje,
                                    content: $("#disappear").val(),
                                    color: "#C46A69",
                                    iconSmall: "fa fa-warning",
                                    timeout: 4000
                                });
                            } else {
                                getUS(data.idEstudio);
                                $('#codUnidadAtencion').val('').change();
                                $('#codSilaisAtencion').val('').change();
                                $('#codMunicipio').val('').change();
                                var msg = $("#succ").val();
                                $.smallBox({
                                    title: msg,
                                    content: $("#disappear").val(),
                                    color: "#739E73",
                                    iconSmall: "fa fa-success",
                                    timeout: 4000
                                });

                            }
                            unBlockUI();
                        }

                    });
            }

            function overrideUs(idRecord) {
                var obj = {};
                obj['mensaje'] = '';
                obj['idEstudio'] = '';
                obj['us'] = '';

                obj['idRecord'] = idRecord;


                var opcSi = $("#msg_yes").val();
                var opcNo = $("#msg_no").val();

                $.SmartMessageBox({
                    title: $("#msg_conf").val(),
                    content: $("#msg_overrideUs_confirm_c").val(),
                    buttons: '['+opcSi+']['+opcNo+']'
                }, function (ButtonPressed) {
                    if (ButtonPressed === opcSi) {

                        blockUI(parametros.blockMess);
                        $.ajax(
                            {
                                url: parametros.saveEstUsUrl,
                                type: 'POST',
                                dataType: 'json',
                                data: JSON.stringify(obj),
                                contentType: 'application/json',
                                mimeType: 'application/json',
                                success: function (data) {
                                    if (data.mensaje.length > 0){
                                        $.smallBox({
                                            title: data.mensaje ,
                                            content: $("#disappear").val(),
                                            color: "#C46A69",
                                            iconSmall: "fa fa-warning",
                                            timeout: 4000
                                        });
                                    }else{
                                        getUS($('#idEst').val());
                                        var msg = $("#msg_succOverrideUs").val();
                                        $.smallBox({
                                            title: msg ,
                                            content: $("#disappear").val(),
                                            color: "#739E73",
                                            iconSmall: "fa fa-success",
                                            timeout: 4000
                                        });
                                    }
                                    unBlockUI();
                                }
                            });

                    }
                    if (ButtonPressed === opcNo) {
                        $.smallBox({
                            title: $("#msg_overrideUs_cancel").val(),
                            content: "<i class='fa fa-clock-o'></i> <i>"+$("#disappear").val()+"</i>",
                            color: "#C46A69",
                            iconSmall: "fa fa-times fa-2x fadeInRight animated",
                            timeout: 4000
                        });
                    }
                });
            }


        }

    };

}();