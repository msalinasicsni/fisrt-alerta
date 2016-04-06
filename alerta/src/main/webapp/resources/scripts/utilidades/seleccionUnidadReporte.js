
var SeleccionUnidadReporte = function () {

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
            },
            baseZ: 1051 // para que se muestre bien en los modales
        });
    };

    return {
        //main function to initiate the module
        init: function (parametros) {
            $('#codSilaisAtencion').change(
                function() {
                    bloquearUI(parametros.blockMess);
                    $.getJSON(parametros.municipiosUrl, {
                        idSilais : $('#codSilaisAtencion').val(),
                        ajax : 'true'
                    }, function(data) {
                        $("#codMunicipio").select2('data',null);
                        $("#codUnidadAtencion").select2('data',null);
                        $("#codMunicipio").empty();
                        $("#codUnidadAtencion").empty();
                        var html='<option value=""></option>';
                        var len = data.length;
                        for ( var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].divisionpoliticaId + '">'
                                + data[i].nombre + '</option>';
                        }
                        html += '</option>';
                        $('#codMunicipio').html(html);

                        $('#codMunicipio').focus();
                        $('#s2id_codMunicipio').addClass('select2-container-active');
                    });
                    setTimeout($.unblockUI, 500);
                });

            $('#codMunicipio').change(
                function() {
                    bloquearUI(parametros.blockMess);
                    $.getJSON(parametros.unidadesUrl, {
                        idMunicipio : $('#codMunicipio').val(),
                        idSilais: $('#codSilaisAtencion').val(),
                        ajax : 'true'
                    }, function(data) {
                        $("#codUnidadAtencion").select2('data',null);
                        $("#codUnidadAtencion").empty();
                        var html='<option value=""></option>';
                        var len = data.length;
                        for ( var i = 0; i < len; i++) {
                            html += '<option value="' + data[i].unidadId + '">'
                                + data[i].nombre + '</option>';
                        }
                        html += '</option>';
                        $('#codUnidadAtencion').html(html);
                        $('#codUnidadAtencion').focus();
                        $('#s2id_codUnidadAtencion').addClass('select2-container-active');
                        //console.log("unidades cargadas");
                    });
                    setTimeout($.unblockUI, 500);
                });
        }
    };

}();