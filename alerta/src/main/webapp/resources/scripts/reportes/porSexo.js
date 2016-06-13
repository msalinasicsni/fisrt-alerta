/**
 * Created by souyen-ics on 10-05-15.
 */
var sexReport = function () {

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
                                    "sFileName": fechaFormateada + "-ReportePorSexo.csv",
                                    "sTitle": "ddd",
                                    "oSelectorOpts": { filter: 'applied', order: 'current' }
                                },
                                {
                                    "sExtends": "pdf",
                                    "sFileName": fechaFormateada + "-ReportePorSexo.pdf",
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
                "aoColumns" : [{sClass: "aw-left"},{sClass: "aw-right" },{sClass: "aw-left"},{sClass: "aw-right" }],
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

            var pieOptions = {
                //Boolean - Whether we should show a stroke on each segment
                segmentShowStroke: true,
                //String - The colour of each segment stroke
                segmentStrokeColor: "#fff",
                //Number - The width of each segment stroke
                segmentStrokeWidth: 2,
                //Number - Amount of animation steps
                animationSteps: 100,
                //String - types of animation
                animationEasing: "easeOutBounce",
                //Boolean - Whether we animate the rotation of the Doughnut
                animateRotate: true,
                //Boolean - Whether we animate scaling the Doughnut from the centre
                animateScale: false,
                //Boolean - Re-draw chart on page resize
                responsive: true
            };

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
                barValueSpacing : 30,
                //Number - Spacing between data sets within X values
                barDatasetSpacing : 35,
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
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
                        $('#silais').show();
                        $('#departamento').hide();
                        $('#municipio').hide();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
                        $('#silais').hide();
                        $('#departamento').show();
                        $('#municipio').hide();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
                        $('#silais').show();
                        $('#departamento').hide();
                        $('#municipio').show();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
                        $('#silais').show();
                        $('#departamento').hide();
                        $('#municipio').show();
                        $('#unidad').show();
                        $('#dSubUnits').show();
                        $('#zona').hide();
                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|ZE"){
                        $('#silais').hide();
                        $('#departamento').hide();
                        $('#municipio').hide();
                        $('#unidad').hide();
                        $('#dSubUnits').hide();
                        $('#zona').show();
                        $("#codZona").val("").change();
                    }
                });

            function getData() {
                var filtro = {};
                filtro['subunidades'] = $('#ckUS').is(':checked');
                filtro['fechaInicio'] = $('#initDate').val();
                filtro['fechaFin'] = $('#endDate').val();
                filtro['codSilais'] = $('#codSilaisAtencion').find('option:selected').val();
                filtro['codUnidadSalud'] = $('#codUnidadAtencion').find('option:selected').val();
                filtro['codFactor'] = $('#codFactor').find('option:selected').val();
                filtro['codDepartamento'] = $('#codDepartamento').find('option:selected').val();
                filtro['codMunicipio'] = $('#codMunicipio').find('option:selected').val();
                filtro['codArea'] = $('#codArea').find('option:selected').val();
                filtro['tipoNotificacion'] = $('#codTipoNoti').find('option:selected').val();
                filtro['codZona'] = $('#codZona').find('option:selected').val();

                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.sActionUrl, {
                    filtro: JSON.stringify(filtro),
                    ajax: 'true'
                }, function(data) {

                    title = $('#codTipoNoti option:selected').text();
                    var encontrado = false;
                    if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
                        title = title + '</br>' + $('#nicRepublic').val();

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
                        title = title + '</br>'+$('#codSilaisAtencion option:selected').text();

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
                        title = title + '</br>' + $('#dep').val()+ " " +$('#codDepartamento option:selected').text();

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
                        title = title + '</br>'+ $('#munic').val() + " " +$('#codMunicipio option:selected').text();

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
                       // title = title + '</br>'+ $('#unit').val() + " " +$('#codUnidadAtencion option:selected').text();

                        var ckeckd = $('#ckUS').is(':checked');

                        if (ckeckd) {
                            title = title + '</br>' + $('#areaL').val() + " " + $('#codUnidadAtencion option:selected').text();
                        } else {
                            title = title + '</br>' + $('#unit').val() + " " + $('#codUnidadAtencion option:selected').text();

                        }

                    }
                    else if ($('#codArea option:selected').val() == "AREAREP|ZE") {
                        title = title + '</br>'+ $('#lblZona').val() + " "  +$('#codZona option:selected').text();
                    }
                    title = title + '</br>' + $('#from').val() +" " +$('#initDate').val()  + " "+ "-" + " " + $('#to').val() + " " +$('#endDate').val();

                    datosP = [];
                    datosT = [];
                    labels = [];
                    datasetsP = [];
                    datasetsT = [];


                    labels.push(["Sexo"]);
                    for (var row in data) {
                        table1.fnAddData([data[row][0],data[row][1], data[row][2],data[row][3]]);
                        encontrado = true;

                        datosT.push(data[row][3]);

                        datasetsP[row] = {
                            value: data[row][2],
                            color: colors[row],
                            highlight: convertHex(colors[row],80),
                            label: data[row][0]
                        };

                        datasetsT[row]= {
                            label: data[row][0],
                            fillColor: convertHex( colors[row],100),
                            strokeColor: convertHex( colors[row],100),
                            highlightFill: convertHex( colors[row],100),
                            highlightStroke:convertHex( colors[row],100),
                            data: [data[row][3]]
                        };


                    }

                    pieChart(datasetsP);
                    barChartT(datasetsT,labels);

                    if(!encontrado){
                        showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
                        title='';
                        $('#lineChart-titleT').html("<h5>"+title+"</h5>");
                        $('#pieChart-titleP').html("<h5>"+title+"</h5>");
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


            function pieChart(datasetsP) {
                // render chart
                $('#pieChart-titleP').html("<h5>"+title+"</h5>");

                if( window.myPie!==undefined)
                    window.myPie.destroy();
                var ctx = document.getElementById("pieChart").getContext("2d");
                window.myPie = new Chart(ctx).Pie(datasetsP, pieOptions);

                // END PIE CHART

                legend(document.getElementById("pieLegend"), datasetsP);
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