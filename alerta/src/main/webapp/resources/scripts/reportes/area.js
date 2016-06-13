var areaReport = function () {

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

    return {
        //main function to initiate the module
        init: function (parametros) {
            var responsiveHelper_data_result = undefined;
            var breakpointDefinition = {
                tablet : 1024,
                phone : 480
            };
            var title = "";

            /* TABLETOOLS */

            var fecha = new Date();
            var fechaFormateada = (fecha.getDate()<10?'0'+fecha.getDate():fecha.getDate())
                +''+(fecha.getMonth()+1<10?'0'+fecha.getMonth()+1:fecha.getMonth()+1)
                +''+fecha.getFullYear();
            var table1 = $('#table1').dataTable({

                // Tabletools options:
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-6 hidden-xs'T>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-sm-6 col-xs-12'p>>",
                "oTableTools": {
                    "aButtons": [
                        {
                            "sExtends":    "collection",
                            "sButtonText": "Exportar",
                            "aButtons": [
                                {
                                    "sExtends": "csv",
                                    "sFileName": fechaFormateada + "-ReportePorArea.csv",
                                    "sTitle": "ddd",
                                    "oSelectorOpts": { filter: 'applied', order: 'current' }
                                },
                                {
                                    "sExtends": "pdf",
                                    "sFileName": fechaFormateada+"-ReportePorArea.pdf",
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
                "aoColumns" : [{sClass: "aw-left"},{sClass: "aw-right" },{sClass: "aw-right" }],
                "autoWidth" : true,
                "preDrawCallback" : function() {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_data_result) {
                        responsiveHelper_data_result = new ResponsiveDatatablesHelper($('#table1'), breakpointDefinition);
                    }
                },
                "rowCallback" : function(nRow) {
                    responsiveHelper_data_result.createExpandIcon(nRow);
                },
                "drawCallback" : function(oSettings) {
                    responsiveHelper_data_result.respond();
                }
            });


            /* END TABLETOOLS */

            var colors = ["#0066FF","#FF0000","#009900","#FF6600","#FF3399","#008B8B","#663399","#FFD700","#0000FF","#DC143C","#32CD32","#FF8C00","#C71585","#20B2AA","#6A5ACD","#9ACD32"];
            var barOptions = {
                //Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
                scaleBeginAtZero : true,
                //Boolean - Whether grid lines are shown across the chart
                scaleShowGridLines : true,
                //String - Colour of the grid lines
                scaleGridLineColor : "rgba(0,0,0,.05)",
                //Number - Width of the grid lines
                scaleGridLineWidth : 1,
                //Boolean - If there is a stroke on each bar
                barShowStroke : true,
                //Number - Pixel width of the bar stroke
                barStrokeWidth : 1,
                //Number - Spacing between each of the X value sets
                barValueSpacing : 5,
                //Number - Spacing between data sets within X values
                barDatasetSpacing : 1,
                //Boolean - Re-draw chart on page resize
                responsive: true
            };

            $('#area_form').validate({
                // Rules for form validation
                rules : {

                    codArea : {
                        required : true
                    },
                    codDepartamento : {
                        required : true
                    },
                    codMunicipio : {
                        required : true
                    },
                    codUnidadAtencion : {
                        required : true
                    },
                    codSilaisAtencion: {
                        required : true
                    },
                    codFactor:{
                        required:true
                    },
                    codTipoNoti:{
                        required:true
                    },
                    initDate:{
                        required:true
                    },
                    endDate:{
                        required:true
                    },
                    codZona: {
                        required : true
                    }
                },
                // Do not change code below
                errorPlacement : function(error, element) {
                    error.insertAfter(element.parent());
                },
                submitHandler: function (form) {
                    table1.fnClearTable();

                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    getData();
                }
            });


            $('#codArea').change(
                function() {
                    if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
                        $('#silais').hide();
                        $('#departamento').hide();
                        $('#municipio').hide();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#dNivelPais').show();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
                        $('#silais').show();
                        $('#departamento').hide();
                        $('#municipio').hide();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#dNivelPais').hide();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
                        $('#silais').hide();
                        $('#departamento').show();
                        $('#municipio').hide();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#dNivelPais').hide();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
                        $('#silais').show();
                        $('#departamento').hide();
                        $('#municipio').show();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#dNivelPais').hide();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
                        $('#silais').show();
                        $('#departamento').hide();
                        $('#municipio').show();
                        $('#unidad').show();
                        $('#dSubUnits').show();
                        $('#dNivelPais').hide();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|ZE"){
                        $('#silais').hide();
                        $('#departamento').hide();
                        $('#municipio').hide();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#dNivelPais').hide();
                        $('#zona').show();
                        $("#codZona").val("").change();
                    }
                });

            function getData() {
                var areaFiltro = {};
                areaFiltro['subunidades'] = $('#ckUS').is(':checked');
                areaFiltro['fechaInicio'] = $('#initDate').val();
                areaFiltro['fechaFin'] = $('#endDate').val();
                areaFiltro['codSilais'] = $('#codSilaisAtencion').find('option:selected').val();
                areaFiltro['codUnidadSalud'] = $('#codUnidadAtencion').find('option:selected').val();
                areaFiltro['codFactor'] = $('#codFactor').find('option:selected').val();
                areaFiltro['codDepartamento'] = $('#codDepartamento').find('option:selected').val();
                areaFiltro['codMunicipio'] = $('#codMunicipio').find('option:selected').val();
                areaFiltro['codArea'] = $('#codArea').find('option:selected').val();
                areaFiltro['tipoNotificacion'] = $('#codTipoNoti').find('option:selected').val();
                areaFiltro['porSilais'] = $('input[name="rbNivelPais"]:checked', '#area_form').val();
                areaFiltro['codZona'] = $('#codZona').find('option:selected').val();

                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.sActionUrl, {
                    filtro: JSON.stringify(areaFiltro),
                    ajax: 'true'
                }, function(data) {

                    title = $('#codTipoNoti option:selected').text();
                    var encontrado = false;
                    if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
                        title = title + '</br>' + $('#nicRepublic').val();
                        if (areaFiltro['porSilais'] == 'true') {
                            $('#firstTh').html($('#silaisT').val());
                        }else {
                            $('#firstTh').html($('#departaT').val());
                        }

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
                        title = title + '</br>'+$('#codSilaisAtencion option:selected').text() + " " + "-" + " " + $('#municps').val() ;
                        $('#firstTh').html( $('#municT').val() );

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
                        title = title + '</br>' + $('#dep').val() + " " +$('#codDepartamento option:selected').text();
                        $('#firstTh').html( $('#municT').val() );

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
                        title = title + '</br>'+ $('#munic').val() + " "  +$('#codMunicipio option:selected').text();
                        $('#firstTh').html( $('#usT').val() );

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|UNI") {
                        $('#firstTh').html($('#usT').val());
                        var ckeckd = $('#ckUS').is(':checked');

                        if (ckeckd) {
                            title = title + '</br>' + $('#areaL').val() + " " + $('#codUnidadAtencion option:selected').text();
                        } else {
                            title = title + '</br>' + $('#unit').val() + " " + $('#codUnidadAtencion option:selected').text();

                        }

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|ZE") {
                        title = title + '</br>'+ $('#lblZona').val() + " "  +$('#codZona option:selected').text();
                        $('#firstTh').html( $('#usT').val() );
                    }
                    title = title + '</br>' + $('#from').val() +" " +$('#initDate').val()  + " "+ "-" + " " + $('#to').val() + " " +$('#endDate').val();

                    datosC = [];
                    datosT = [];
                    labels = [];
                    datasetsC = [];
                    datasetsT = [];

                    for (var row in data) {

                        if($('#codArea option:selected').val() != "AREAREP|MUNI"){//municipio no porque se carga con demasiadas unidades los gráficos
                            labels.push([data[row][0]]);
                            datosC.push(data[row][1]);
                            if (data[row][2]==='NP'){
                                datosT.push(0);
                            }else {
                                datosT.push(data[row][2]);
                            }
                        }
                        table1.fnAddData([data[row][0],data[row][1], data[row][2]]);
                        encontrado = true;

                    }

                    var colorS = colors[0];
                    var colorT = colors[1];
                  //  var label = $('#anioI').find('option:selected').val();

                    if($('#codArea option:selected').val() != "AREAREP|MUNI"){

                        datasetsC.push({
                            label: "Casos",
                            fillColor: convertHex(colorS,100),
                            strokeColor: convertHex(colorS,100),
                            highlightFill: convertHex(colorS,100),
                            highlightStroke: convertHex(colorS,100),
                            data: datosC
                        });
                        datasetsT.push({
                            label: "Tasas",
                            fillColor: convertHex(colorT,100),
                            strokeColor: convertHex(colorT,100),
                            highlightFill: convertHex(colorT,100),
                            highlightStroke:convertHex(colorT,100),
                            data: datosT
                        });

                        barChart(datasetsC,labels);
                        barChartT(datasetsT,labels);

                    }else{
                        leg = $('#lineLegend');
                        leg1 = $('#lineLegendT');

                        $('#lineChart-title').html("");
                        $('#lineChart-titleT').html("");
                        leg1.removeClass("legend");
                        leg.removeClass("legend");
                        leg1.html("");
                        leg.html("");

                        if (window.myBar !== undefined)
                            window.myBar.destroy();

                        if (window.myBar2 !== undefined)
                            window.myBar2.destroy();

                        document.getElementById("lineChart").style.height = "120px";
                        document.getElementById("lineChartT").style.height = "120px";

                    }

                    if(!encontrado){
                        showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
                        title='';
                        $('#lineChart-title').html("<h5>"+title+"</h5>");
                        $('#lineChart-titleT').html("<h5>"+title+"</h5>");
                    }
                    setTimeout($.unblockUI, 500);
                })
                    .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " er:" + errorThrown);
                        setTimeout($.unblockUI, 5);
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


            function barChart(datasetsC,labels) {
                // LINE CHART
                // ref: http://www.chartjs.org/docs/#line-chart-introduction
                $('#lineChart-title').html("<h5>"+title+"</h5>");
                var barData = { labels: labels,
                    datasets: datasetsC
                };
                // render chart
                if( window.myBar!==undefined)
                    window.myBar.destroy();
                var ctx = document.getElementById("lineChart").getContext("2d");
                window.myBar = new Chart(ctx).Bar(barData, barOptions);
                // END BAR CHART

                legend(document.getElementById("lineLegend"), barData);
            }

            function barChartT(datasetsT,labels) {
                // LINE CHART
                // ref: http://www.chartjs.org/docs/#line-chart-introduction
                $('#lineChart-titleT').html("<h5>"+title+"</h5>");
                var barData = { labels: labels,
                    datasets: datasetsT
                };
                // render chart
                if( window.myBar2!==undefined)
                    window.myBar2.destroy();
                var ctx = document.getElementById("lineChartT").getContext("2d");
                window.myBar2 = new Chart(ctx).Bar(barData, barOptions);
                // END BAR CHART

                legend(document.getElementById("lineLegendT"), barData);
            }


            // Convert Hex color to RGB
            function convertHex(hex,opacity){
                hex = hex.replace('#','');
                r = parseInt(hex.substring(0,2), 16);
                g = parseInt(hex.substring(2,4), 16);
                b = parseInt(hex.substring(4,6), 16);

                // Add Opacity to RGB to obtain RGBA
                result = 'rgba('+r+','+g+','+b+','+opacity/100+')';
                return result;
            }


        }
    };

}();