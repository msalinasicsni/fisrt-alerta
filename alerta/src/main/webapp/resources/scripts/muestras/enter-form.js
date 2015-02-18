/**
 * Created by souyen-ics on 11-05-14.
 */

var EnterFormTomaMx = function () {
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
        init: function (parametros) {

            $('.datetimepicker').datetimepicker({
                language: 'es',
               format: 'DD/MM/YYYY h:m A'

            });

            $('#horaRefrigeracion').datetimepicker({
                pickDate: false
            });

            $('#codTipoMx').change(function() {
                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.dxUrl, {
                    codMx: $('#codTipoMx').val(),
                    tipoNoti: $('#tipoNoti').val(),
                    ajax: 'false'
                }, function (data) {
                    var len = data.length;
                    var html = null;
                    for (var i = 0; i < len; i++) {
                        console.log(data[i]);
                       html += '<option value="' + data[i].diagnostico.idDiagnostico + '">'
                            + data[i].diagnostico.nombre
                            + '</option>';
                    }
                    $('#dx').html(html);
                    desbloquearUI();
                });
            });


         var $validator = $("#registroMx").validate({
                rules: {
                    fechaHTomaMx: {
                        required: true

                    },

                    codTipoMx:{
                        required:true
                    },

                    dx:{
                        required:true
                    }

                },

                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());

                }

              /*  submitHandler: function (form) {
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                   save();
                }*/
            });


            $('#submit').click(function() {
                var $validarForm = $("#registroMx").valid();
                if (!$validarForm) {
                    $validator.focusInvalid();
                    return false;
                }else{
                   save();

                }

            });

            function save() {
                var datos_form = $('#registroMx').serialize();
                bloquearUI(parametros.blockMess);
                $.ajax({
                    type: "GET",
                    url: parametros.saveTomaUrl,
                    data: datos_form,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function () {
                        $.smallBox({
                            title: $('#msjSuccessful').val() ,
                            content:  $('#disappear').val(),
                            color: "#739E73",
                            iconSmall: "fa fa-check-circle",
                            timeout: 2000
                        });
                        desbloquearUI();
                        window.location.href = parametros.searchUrl;

                    },
                    error: function () {
                        desbloquearUI();
                        $.smallBox({
                            title: $('#msjErrorSaving').val(),
                            content:  $('#disappear').val(),
                            color: "#C46A69",
                            iconSmall: "fa fa-warning",
                            timeout: 2000
                        });
                    }
                });

            }




        }
    }



}();