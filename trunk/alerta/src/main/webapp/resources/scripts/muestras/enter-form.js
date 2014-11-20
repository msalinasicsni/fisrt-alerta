/**
 * Created by souyen-ics on 11-05-14.
 */

var EnterFormTomaMx = function () {

    return {
        init: function (parametros) {

            $('.datetimepicker').datetimepicker({
                language: 'es',
               format: 'DD/MM/YYYY h:m A'

            });

            $('#horaRefrigeracion').datetimepicker({
                pickDate: false
            });

        }
    }

}();