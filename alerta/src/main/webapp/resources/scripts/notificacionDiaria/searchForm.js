var SearchNotificationD = function () {
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
                    null, null, null, null, null,

                    {
                        "className": 'fEdit',
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
                "drawCallback" : function(oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });


            //Validacion del formulario irag
            var $validator = $("#search-form").validate({
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
                    }

                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                },
                submitHandler: function (form) {
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    getNotiDetails($('#codSilaisAtencion').val(), $('#codMunicipio').val(), $('#codUnidadAtencion').val(), $('#notidate').val());
                }
            });



            function getNotiDetails(silais, municipio, unidad, fecha) {
                bloquearUI(parametros.blockMess);

                $.getJSON(parametros.detailsUrl, {
                    silais: silais,
                    municipio: municipio,
                    unidad: unidad,
                    fecha: fecha,
                    ajax : 'true'
                }, function(response) {
                    table1.fnClearTable();
                    var len = response.length;

                    if(len > 0){
                        var estado;
                        for ( var i = 0; i < len; i++) {

                            var editUrl = parametros.loadNotiDFormUrl + response[i][5] + "," + response[i][6] + "," + response[i][7] + "," + response[i][0] ;

                            var btnEdit = '<a target="_blank" title="Ver" href=' + editUrl + ' class="btn btn-xs btn-primary" ><i class="fa fa-mail-forward"></i></a>';

                            var bloqueado = response[i][4];
                            if(bloqueado == 0){
                                estado = $('#open').val();
                            }else{
                                estado = $('#close').val();
                            }

                            table1.fnAddData(
                                [response[i][0], response[i][1],response[i][2], response[i][3], estado,  btnEdit]);
                        }
                    }else{
                        $.smallBox({
                            title: $("#msg_no_results_found").val() ,
                            content: $("#disappear").val(),
                            color: "#C79121",
                            iconSmall: "fa fa-warning",
                            timeout: 4000
                        });

                    }
                    desbloquearUI();

                })
                    .fail(function() {
                        desbloquearUI();
                        alert( "error" );

                    });
            }

        }
    };

}();
