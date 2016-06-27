var Boletin = function () {

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

    return {
        //main function to initiate the module
        init: function (parametros) {
            var responsiveHelper_data_result = undefined;
            var responsiveHelper_data_result_2 = undefined;
            var breakpointDefinition = {
                tablet: 1024,
                phone: 480
            };
            var title = "";
            var anio = $('#anio').val();
            var table = null;
            var secondTime = false;
            var arrayTable = [];


            /* TABLETOOLS */
            var summaryTable = $('#summaryTable').dataTable({

                // Tabletools options:
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-6 hidden-xs'T>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-sm-6 col-xs-12'p>>",
                "oTableTools": {
                    "aButtons": [
                        {
                            "sExtends": "collection",
                            "sButtonText": "Exportar",
                            "aButtons": [
                                {
                                    "sExtends": "csv",
                                    "sFileName": "ddd" + "-*.csv",
                                    "sTitle": "ddd",
                                    "oSelectorOpts": { filter: 'applied', order: 'current' }
                                },
                                {
                                    "sExtends": "pdf",
                                    "sFileName": "DD" + "-*.pdf",
                                    "sTitle": ":fff:",
                                    "sPdfMessage": "FF",
                                    "oSelectorOpts": { filter: 'applied', order: 'current' },
                                    "sPdfOrientation": "landscape"
                                }
                            ]
                        }
                    ],
                    "sSwfPath": parametros.dataTablesTTSWF
                },
                "aoColumns": [
                    {sClass: "aw-right" },
                    {sClass: "aw-right" },
                    {sClass: "aw-right" },
                    {sClass: "aw-right" },
                    {sClass: "aw-right" },
                    {sClass: "aw-right" },
                    {sClass: "aw-right" },
                    {sClass: "aw-right" },
                    {sClass: "aw-right" },
                    {sClass: "aw-right" }
                ],
                "autoWidth": true,
                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_data_result) {
                        responsiveHelper_data_result = new ResponsiveDatatablesHelper($('#summaryTable'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_data_result.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_data_result.respond();
                }
            });

            var form =$('#parameters_form');

            jQuery.validator.addMethod("maximo10", function(value, select) {
                var isValid = true;
                var number = $('option:selected', select).size();
                if (number > 10 ) {
                    isValid = false;
                }
                return isValid;
            }, "Se permite como máximo 10 patologías");


            form.validate({
                // Rules for form validation
                rules: {
                    codPato: {
                        required: true,
                        maximo10: true
                    },
                    codArea: {
                        required: true
                    },
                    semI: {
                        required: true
                    },
                    semF: {
                        required: true
                    },
                    anio: {
                        required: true
                    },
                    codDepartamento: {
                        required: true
                    }


                },
                // Do not change code below
                errorPlacement: function (error, element) {
                    error.insertAfter(element.parent());
                },
                submitHandler: function (form) {
                    summaryTable.fnClearTable();

                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax

                    if (secondTime) {
                        cleanTables(arrayTable);
                    }

                    getData();
                    secondTime = true;
                }
            });

            $('#codArea').change(
                function () {
                    if ($('#codArea option:selected').val() == "AREAREP|PAIS") {
                        $('#silais').hide();
                        $('#departamento').hide();

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|SILAIS") {
                        $('#silais').show();
                        $('#departamento').hide();

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|DEPTO") {
                        $('#silais').hide();
                        $('#departamento').show();

                    }


                });

            function getData() {
                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.sActionUrl, $('#parameters_form').serialize(), function (data) {

                    title = "Distribución por patologías";
                    if ($('#codArea option:selected').val() == "AREAREP|PAIS") {
                        title = title + '</br>' + $('#nicRepublic').val();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|SILAIS") {
                        title = title + '</br>' + $('#codSilaisAtencion option:selected').text();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|DEPTO") {
                        title = title + '</br>' + $('#dep').val() + " " + $('#codDepartamento option:selected').text();
                    }

                    title = title + '</br>' + $('#sem').val() + $('#semI option:selected').text() + $('#to').val() + $('#semF option:selected').text() + '</br>' + $('#lblAnios').val() + " " + anio - 1 + " " + "-" + " " + anio;


                    var codPato = $('#codPato').val();
                    var pato = [];

                    var coma = new RegExp(",");
                    if (coma.test(codPato)) {
                        pato = codPato;
                    } else {
                        pato.push(codPato);
                    }

                    var indicePato = 1;
                    arrayTable = [];


                    for (var pat in pato) {

                        var idTable = '#table' + indicePato;
                        table = $(idTable).dataTable({

                            // Tabletools options:
                            "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-6 hidden-xs'T>r>" +
                                "t" +
                                "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-sm-6 col-xs-12'p>>",
                            "oTableTools": {
                                "aButtons": [
                                    {
                                        "sExtends": "collection",
                                        "sButtonText": "Exportar",
                                        "aButtons": [
                                            {
                                                "sExtends": "csv",
                                                "sFileName": "ddd" + "-*.csv",
                                                "sTitle": "ddd",
                                                "oSelectorOpts": { filter: 'applied', order: 'current' }
                                            },
                                            {
                                                "sExtends": "pdf",
                                                "sFileName": "DD" + "-*.pdf",
                                                "sTitle": ":fff:",
                                                "sPdfMessage": "FF",
                                                "oSelectorOpts": { filter: 'applied', order: 'current' },
                                                "sPdfOrientation": "landscape"
                                            }
                                        ]
                                    }
                                ],
                                "sSwfPath": parametros.dataTablesTTSWF
                            },
                            "bDestroy": true,
                            "aoColumns": [
                                {sClass: "aw-right" },
                                {sClass: "aw-right" },
                                {sClass: "aw-right" },
                                {sClass: "aw-right" },
                                {sClass: "aw-right" },
                                {sClass: "aw-right" },
                                {sClass: "aw-right" },
                                {sClass: "aw-right" },
                                {sClass: "aw-right" },
                                {sClass: "aw-right" }
                            ],
                            "autoWidth": true,
                            "preDrawCallback": function () {
                                // Initialize the responsive datatables helper once.
                                if (!responsiveHelper_data_result_2) {
                                    responsiveHelper_data_result_2 = new ResponsiveDatatablesHelper($(idTable), breakpointDefinition);
                                }
                            },
                            "rowCallback": function (nRow) {
                                responsiveHelper_data_result_2.createExpandIcon(nRow);
                            },
                            "drawCallback": function (oSettings) {
                                responsiveHelper_data_result_2.respond();
                            }
                        });


                        var i = 0;
                        var idEntidad = 0;

                        dataset = [];
                        summDataset = [];
                        var firstYear = '#firstYear' + indicePato;
                        var secYear = '#secYear' + indicePato;
                        var anio = $('#anio').val();

                        //pathologies table
                        $(firstYear).html(anio - 1);
                        $(secYear).html(anio);

                        //summary table
                        $(firstY).html(anio - 1);
                        $(secY).html(anio);

                        var contRow = 0;

                        //var summary table
                        var sumCasosAct = 0;
                        var sumAcumAct = 0;
                        var sumCasosAnt = 0;
                        var sumAcumAnt = 0;
                        var contSumm = 0;
                        var sumPobAct= 0;
                        var sumPobAnt= 0;
                        var factor = null;
                        var nombrePatologia = null;

                        for (i = 0; i < (data.length); i++) {
                            var nombrePato = '#pat' + indicePato;

                            var entidad = '#entidad' + indicePato;
                            var dTable = '#dTable' + indicePato;

                            if (pato[pat] == data[i].anio.entidad.patologia.idPatologia) {
                                if ($('#codArea option:selected').val() == "AREAREP|PAIS") {
                                    $(entidad).html($('#silaisT').val());
                                }else{
                                    $(entidad).html($('#munic').val());
                                }
                                $(nombrePato).html(data[i].anio.entidad.patologia.nombre);

                                var caso = (data[i].valor == null) ? 0 :data[i].valor;
                                var acum = (data[i].valorAcum == null) ? 0 :data[i].valorAcum;
                                var pob  = (data[i].anio.entidad.totalPoblacion == null) ? 0 :data[i].anio.entidad.totalPoblacion;

                                 // Si es la misma entidad
                                if (idEntidad == data[i].anio.entidad.idEntidad) {

                                    //summary table

                                        //anio actual
                                        if (data[i].anio.anio == anio) {

                                            sumCasosAct = parseInt(sumCasosAct) +  parseInt(caso);
                                            sumAcumAct =   parseInt(sumAcumAct) +  parseInt(acum);
                                            sumPobAct = parseInt(sumPobAct) + parseInt(pob);
                                            contSumm++;
                                        } else {
                                            //anio anterior

                                            sumCasosAnt = parseInt(sumCasosAnt) +  parseInt(caso);
                                            sumAcumAnt = parseInt(sumAcumAnt) +  parseInt(acum);
                                            sumPobAnt = parseInt(sumPobAnt) + parseInt(pob);
                                            contSumm++;
                                        }


                                     // pathologies tables
                                    //año seleccionado
                                    if (data[i].anio.anio == anio) {
                                        dataset.push(data[i].valor);
                                        dataset.push(data[i].valorAcum);
                                        dataset.push(data[i].tasa);

                                        //Diferencia de Casos
                                        var difC = dataset[5] - dataset[2];
                                        // porcentaje relativo de tasas (tasa actual-tasa anterior)/tasa anterior *100
                                        var porc1 = "";
                                        if (dataset[7] != "" && dataset[4]!= "") {
                                            porc = (dataset[7] - dataset[4]) / dataset[4] * 100 ;
                                            porc1 = porc.toFixed(5);
                                        }

                                        dataset.push(difC);
                                        dataset.push(porc1);
                                        table.fnAddData(dataset);
                                        $(dTable).show();
                                        dataset = [];
                                        contRow = 0;
                                    }

                                } else {

                                   //summary table
                                    if (contSumm == 0) {
                                        factor = data[i].anio.entidad.patologia.factor;
                                        nombrePatologia = data[i].anio.entidad.patologia.nombre;


                                        //anio actual
                                        if (data[i].anio.anio == anio) {
                                            sumCasosAct = caso;
                                            sumAcumAct = acum;
                                            sumPobAct = pob;
                                            contSumm++;
                                        } else {
                                            //anio anterior
                                            sumCasosAnt = caso;
                                            sumAcumAnt = acum;
                                            sumPobAnt = pob;
                                            contSumm++;
                                        }
                                    } else {
                                        //anio actual
                                        if (data[i].anio.anio == anio) {
                                            sumCasosAct = parseInt(sumCasosAct) + parseInt(caso);
                                            sumAcumAct =  parseInt(sumAcumAct) +  parseInt(acum);
                                            sumPobAct = parseInt(sumPobAct) + parseInt(pob);
                                            contSumm++;
                                        } else {
                                            //anio anterior
                                            sumCasosAnt =  parseInt(sumCasosAnt) +  parseInt(caso);
                                            sumAcumAnt =  parseInt(sumAcumAnt) +  parseInt(acum);
                                            sumPobAnt = parseInt(sumPobAnt) + parseInt(pob);
                                            contSumm++;
                                        }
                                    }


                                     //pathologies tables
                                    //si es el primer registro se realiza push de los registros del año anterior
                                    if (contRow == 0) {
                                        //validar año anterior
                                        if (data[i].anio.anio == anio - 1) {
                                            //datos de año anterior
                                            dataset.push(data[i].anio.entidad.nombreEntidad);
                                            dataset.push(data[i].ultimaSemana);
                                            dataset.push(data[i].valor);
                                            dataset.push(data[i].valorAcum);
                                            dataset.push(data[i].tasa);
                                            contRow++;
                                        } else {
                                            //en caso q no hayan registros de año anterior y si se encuentren datos del año seleccionado
                                            dataset.push(data[i].anio.entidad.nombreEntidad);
                                            dataset.push(data[i].ultimaSemana);
                                            dataset.push("");
                                            dataset.push("");
                                            dataset.push("");
                                            dataset.push(data[i].valor);
                                            dataset.push(data[i].valorAcum);
                                            dataset.push(data[i].tasa);
                                            //Diferencia de Casos
                                            var difC = dataset[5] - dataset[2];
                                            // porcentaje relativo de tasas (tasa actual-tasa anterior)/tasa anterior *100
                                            var porc1 = "";
                                            if (dataset[7] != "" && dataset[4]!= "") {
                                                porc = (dataset[7] - dataset[4]) / dataset[4] * 100 ;
                                                porc1 = porc.toFixed(5);
                                            }

                                            dataset.push(difC);
                                            dataset.push(porc1);
                                            table.fnAddData(dataset);
                                            $(dTable).show();
                                            dataset = [];
                                            contRow = 0;
                                        }
                                    } else {
                                        //para los registros q solo tienen info del año anterior
                                        // si es diferente de cero se guardan los registros con el año seleccionado vacio

                                        //año actual sin datos
                                        dataset.push("");
                                        dataset.push("");
                                        dataset.push("");

                                        //Diferencia de Casos
                                        var difC = dataset[5] - dataset[2];
                                        // porcentaje relativo de tasas (tasa actual-tasa anterior)/tasa anterior *100

                                        var porc1 = "";
                                        if (dataset[7] != "" && dataset[4]!= "") {
                                            porc = (dataset[7] - dataset[4]) / dataset[4] * 100 ;
                                            porc1 = porc.toFixed(5);
                                        }

                                        dataset.push(difC);
                                        dataset.push(porc1);
                                        table.fnAddData(dataset);
                                        $(dTable).show();
                                        dataset = [];
                                        contRow = 0;

                                        //validar año anterior
                                        if (data[i].anio.anio == anio - 1) {
                                            //es el ultimo
                                            if (data[i] == data[data.length - 1]) {
                                                //datos de año anterior
                                                dataset.push(data[i].anio.entidad.nombreEntidad);
                                                dataset.push(data[i].ultimaSemana);
                                                dataset.push(data[i].valor);
                                                dataset.push(data[i].valorAcum);
                                                dataset.push(data[i].tasa);
                                                dataset.push("");
                                                dataset.push("");
                                                dataset.push("");
                                                //Diferencia de Casos
                                                var difC = dataset[5] - dataset[2];
                                                // porcentaje relativo de tasas (tasa actual-tasa anterior)/tasa anterior *100
                                                var porc1 = "";
                                                if (dataset[7] != "" && dataset[4]!= "") {
                                                    porc = (dataset[7] - dataset[4]) / dataset[4] * 100 ;
                                                    porc1 = porc.toFixed(5);
                                                }

                                                dataset.push(difC);
                                                dataset.push(porc1);
                                                table.fnAddData(dataset);
                                                $(dTable).show();
                                                dataset = [];
                                                contRow = 0;
                                            } else {
                                                //datos de año anterior
                                                dataset.push(data[i].anio.entidad.nombreEntidad);
                                                dataset.push(data[i].ultimaSemana);
                                                dataset.push(data[i].valor);
                                                dataset.push(data[i].valorAcum);
                                                dataset.push(data[i].tasa);
                                                contRow++;
                                            }

                                        }

                                    }

                                }

                                idEntidad = data[i].anio.entidad.idEntidad;
                            }


                        }

                        var difCasos = parseInt(sumCasosAct) - parseInt(sumCasosAnt);
                        var tasaAct = null;
                        var tasaAnt = null;
                        var porcR = null;

                        if(sumAcumAct!= 0 && sumPobAct!= 0 && factor!= null ){
                           var tasaAct1 = (sumCasosAct / sumPobAct)* factor;
                            tasaAct = tasaAct1.toFixed(2);
                        }

                        if(sumAcumAnt!= 0 && sumPobAnt!= 0 && factor!= null ){
                           var tasaAnt1 = (sumCasosAnt / sumPobAnt)* factor;
                            tasaAnt = tasaAnt1.toFixed(2);
                        }

                        if(tasaAct != null && tasaAnt != null){
                           var porcR1 = (tasaAct - tasaAnt) / tasaAnt * 100 ;
                            porcR = porcR1.toFixed(5);
                        }

                        summDataset.push(nombrePatologia);
                        summDataset.push(factor);
                        summDataset.push(sumCasosAnt);
                        summDataset.push(sumAcumAnt);
                        summDataset.push(tasaAnt);
                        summDataset.push(sumCasosAct);
                        summDataset.push(sumAcumAct);
                        summDataset.push(tasaAct);
                        summDataset.push(difCasos);
                        summDataset.push(porcR);
                        summaryTable.fnAddData(summDataset);

                        summDataset = [];
                        nombrePatologia = null;
                        factor = null;
                        contSumm = 0;

                        indicePato++;
                        arrayTable.push(table);


                    }

                    setTimeout($.unblockUI, 500);
                })
                    .fail(function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " error:" + errorThrown);
                        setTimeout($.unblockUI, 5);
                    });
            }


            function cleanTables(arrayTable) {
                var cont=0;
                for (var row in arrayTable) {
                    cont++;
                    var nombrePato = '#pat' + cont;
                    var divName = '#dTable' + cont;
                    $(nombrePato).html("");
                    $(divName).hide();

                    var table = arrayTable[row];
                    if (!(table == null)) {
                        table.fnClearTable();
                        table.fnDestroy();
                        table = null;
                    }
                }
            }


            function showMessage(title, content, color, icon, timeout) {
                $.smallBox({
                    title: title,
                    content: content,
                    color: color,
                    iconSmall: icon,
                    timeout: timeout
                });
            }




        }
    };

}();