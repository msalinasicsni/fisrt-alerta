/**
 * Created by FIRSTICT on 3/11/2016.
 */
var PaginaInicio = function () {
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

    function showMessage(title,content,color,icon,timeout){
        $.smallBox({
            title: title,
            content: content,
            color: color,
            iconSmall: icon,
            timeout: timeout
        });
    }

    return {
        init: function (parametros) {

            var backColors = ["#0066FF","#FF0000","#009900","#FF6600","#FF3399","#008B8B","#663399","#FFD700","#0000FF","#DC143C","#32CD32","#FF8C00","#C71585","#20B2AA","#6A5ACD","#9ACD32"];

            var title;
            var title2;

            //dengue confirmado
            datasetsDCCasos = [];
            datasetsDCTasas = [];
            labelsDC = [];
            //denque sospechoso
            datasetsDSCasos = [];
            datasetsDSTasas = [];
            labelsDS = [];

            function getDataDengueConfiramdo() {

                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.sActionUrl, { codPato: "0612,0613",
                    semI : $("#semanaI").val(),
                    semF : $("#semanaF").val(),
                    anioI : $("#anioI").val(),
                    anioF : $("#anioF").val(),
                    nivel : $("#nivelUsuario").val(),
                    ajax: 'true'
                }, function(data) {
                    var long = data.length;
                    var hay = (long < 2)?false:true;
                    var nivel = $("#nivelUsuario").val();
                    title = $("#dengueConfirmadoMAY").val()+"</br>";
                    if ( nivel == "PAIS"){
                        title += $("#nivel").val()+": "+$("#nivelNac").val()+"</br>";
                    }else if (nivel == "SILAIS"){
                        title += $("#nivel").val()+": "+$("#nivelSilais").val()+"</br>";
                    }else if (nivel == "UNIDAD"){
                        title += $("#nivel").val()+": "+$("#nivelUS").val()+"</br>";
                    }else{
                        title += "Sin Nivel </br>";
                    }
                    if (hay){
                        var i = 0, j = 0;
                        title += $("#semana").val()+" "+$("#semanaI").val()+" "+$("#semanaHasta").val()+" "+$("#semanaF").val();
                        for (i = 0; i < (data.length-1) ;i++) {
                            datos = [];
                            datosC = [];
                            datosT = [];
                            label = ""; //$("#dengueConfirmado").val();
                            for (j = 0 ; j < data[i].length; j+=2) {
                                if (j == 0) label = label + data[i][j];
                                if(j>1) {
                                    datosC.push(data[i][j]);
                                    datosT.push(data[i][j+1]);
                                }
                            }
                            var colorS = (i < 12)?backColors[i]:getRandomColor();
                            datasetsDCCasos.push({
                                label: label,
                                fillColor: convertHex(colorS,0),
                                strokeColor: convertHex(colorS,100),
                                pointColor: convertHex(colorS,100),
                                pointStrokeColor: "#fff",
                                pointHighlightFill: "#fff",
                                pointHighlightStroke: convertHex(colorS,100),
                                data: datosC
                            });
                            datasetsDCTasas.push({
                                label: label,
                                fillColor: convertHex(colorS,0),
                                strokeColor: convertHex(colorS,100),
                                pointColor: convertHex(colorS,100),
                                pointStrokeColor: "#fff",
                                pointHighlightFill: "#fff",
                                pointHighlightStroke: convertHex(colorS,100),
                                data: datosT
                            });
                        }
                        labelsDC = data[(data.length)-1];
                    }
                    else{
                        //showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
                        //lineChart([],[]);
                        //lineChart2([],[]);
                        datasetsDCCasos = [];
                        datasetsDCTasas = [];
                        labelsDC = [];
                    }
                    lineChart(datasetsDCCasos,labelsDC);
                    setTimeout($.unblockUI, 100);
                })
                    .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " error:" + errorThrown);
                        setTimeout($.unblockUI, 5);
                    });
            }

            function getDataDengueSospechoso() {

                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.sActionUrl, { codPato: "0610,0654",
                    semI : $("#semanaI").val(),
                    semF : $("#semanaF").val(),
                    anioI : $("#anioI").val(),
                    anioF : $("#anioF").val(),
                    nivel : $("#nivelUsuario").val(),
                    ajax: 'true'
                }, function(data) {
                    var long = data.length;
                    var hay = (long < 2)?false:true;
                    title2 = $("#dengueSospechosoMAY").val()+"</br>";
                    var nivel = $("#nivelUsuario").val();
                    if ( nivel == "PAIS"){
                        title2 += $("#nivel").val()+": "+$("#nivelNac").val()+"</br>";
                    }else if (nivel == "SILAIS"){
                        title2 += $("#nivel").val()+": "+$("#nivelSilais").val()+"</br>";
                    }else if (nivel == "UNIDAD"){
                        title2 += $("#nivel").val()+": "+$("#nivelUS").val()+"</br>";
                    }else{
                        title2 += "Sin Nivel </br>";
                    }
                    if (hay){
                        var i = 0, j = 0;
                        title2 += $("#semana").val()+" "+$("#semanaI").val()+" "+$("#semanaHasta").val()+" "+$("#semanaF").val();
                        for (i = 0; i < (data.length-1) ;i++) {
                            datos = [];
                            datosC = [];
                            datosT = [];
                            label = ""; //$("#dengueSospechoso").val();
                            for (j = 0 ; j < data[i].length; j+=2) {
                                if (j == 0) label = label + data[i][j];
                                if(j>1) {
                                    datosC.push(data[i][j]);
                                    datosT.push(data[i][j+1]);
                                }
                            }
                            var colorS = (i < 12)?backColors[i]:getRandomColor();
                            datasetsDSCasos.push({
                                label: label,
                                fillColor: convertHex(colorS,0),
                                strokeColor: convertHex(colorS,100),
                                pointColor: convertHex(colorS,100),
                                pointStrokeColor: "#fff",
                                pointHighlightFill: "#fff",
                                pointHighlightStroke: convertHex(colorS,100),
                                data: datosC
                            });
                            datasetsDSTasas.push({
                                label: label,
                                fillColor: convertHex(colorS,0),
                                strokeColor: convertHex(colorS,100),
                                pointColor: convertHex(colorS,100),
                                pointStrokeColor: "#fff",
                                pointHighlightFill: "#fff",
                                pointHighlightStroke: convertHex(colorS,100),
                                data: datosT
                            });

                        }
                        labelsDS = data[(data.length)-1];
                    }
                    else{
                        //showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
                        //lineChart3([],[]);
                        //lineChart4([],[]);
                        datasetsDSCasos = [];
                        datasetsDSTasas = [];
                        labelsDS = [];
                    }
                    lineChart3(datasetsDSCasos,labelsDS);
                    setTimeout($.unblockUI, 100);
                })
                    .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " error:" + errorThrown);
                        setTimeout($.unblockUI, 5);
                    });
            }

            var lineOptions = {
                ///Boolean - Whether grid lines are shown across the chart
                scaleShowGridLines : true,
                //String - Colour of the grid lines
                scaleGridLineColor : "rgba(0,0,0,0.04)",
                //Number - Width of the grid lines
                scaleGridLineWidth : 1,
                //Boolean - Whether the line is curved between points
                bezierCurve : false,
                //Number - Tension of the bezier curve between points
                bezierCurveTension : 0.4,
                //Boolean - Whether to show a dot for each point
                pointDot : true,
                //Number - Radius of each point dot in pixels
                pointDotRadius : 4,
                //Number - Pixel width of point dot stroke
                pointDotStrokeWidth : 1,
                //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
                pointHitDetectionRadius : 20,
                //Boolean - Whether to show a stroke for datasets
                datasetStroke : true,
                //Number - Pixel width of dataset stroke
                datasetStrokeWidth : 2,
                //Boolean - Whether to fill the dataset with a colour
                datasetFill : true,
                //Boolean - Re-draw chart on page resize
                responsive: true,
                //String - A legend template
                legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].lineColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"
            };
            function lineChart(datasets,labels) {
                // LINE CHART
                // ref: http://www.chartjs.org/docs/#line-chart-introduction
                $('#lineChart1-title').html("<h5>"+title+"</h5>");
                var lineData = { labels: labels,
                    datasets: datasets
                };
                // render chart
                if( window.myLine!==undefined)
                    window.myLine.destroy();
                $('.nav-tabs a[href="#s1"]').tab('show');
                var ctx = document.getElementById("lineChart1").getContext("2d");
                window.myLine = new Chart(ctx).Line(lineData, lineOptions);
                legend(document.getElementById("lineLegend1"), lineData);
                // END LINE CHART
            }

            function lineChart2(datasets2,labels2) {
                // LINE CHART
                // ref: http://www.chartjs.org/docs/#line-chart-introduction
                $('#lineChart2-title').html("<h5>"+title+"</h5>");
                var lineData2 = { labels: labels2,
                    datasets: datasets2
                };
                // render chart
                if( window.myLine2!==undefined)
                    window.myLine2.destroy();
                $('.nav-tabs a[href="#s2"]').tab('show');
                var ctx = document.getElementById("lineChart2").getContext("2d");
                window.myLine2 = new Chart(ctx).Line(lineData2, lineOptions);
                legend(document.getElementById("lineLegend2"), lineData2);
                // END LINE CHART
            }

            function lineChart3(datasets3,labels3) {
                // LINE CHART
                // ref: http://www.chartjs.org/docs/#line-chart-introduction
                $('#lineChart3-title').html("<h5>"+title2+"</h5>");
                var lineData3 = { labels: labels3,
                    datasets: datasets3
                };
                // render chart
                if( window.myLine3!==undefined)
                    window.myLine3.destroy();
                //$('.nav-tabs a[href="#ss1"]').tab('show');
                var ctx = document.getElementById("lineChart3").getContext("2d");
                window.myLine3 = new Chart(ctx).Line(lineData3, lineOptions);
                legend(document.getElementById("lineLegend3"), lineData3);
                // END LINE CHART
            }

            function lineChart4(datasets4,labels4) {
                // LINE CHART
                // ref: http://www.chartjs.org/docs/#line-chart-introduction
                $('#lineChart4-title').html("<h5>"+title2+"</h5>");
                var lineData4 = { labels: labels4,
                    datasets: datasets4
                };
                // render chart
                if( window.myLine4!==undefined)
                    window.myLine4.destroy();
                //$('.nav-tabs a[href="#ss2"]').tab('show');
                var ctx = document.getElementById("lineChart4").getContext("2d");
                window.myLine4 = new Chart(ctx).Line(lineData4, lineOptions);
                legend(document.getElementById("lineLegend4"), lineData4);
                // END LINE CHART
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

            function getRandomColor() {
                var color = '';
                color = '#'+Math.floor(Math.random()*16777215).toString(16);
                return color;
            }

            //getDataDengueConfiramdo();
            //getDataDengueSospechoso();

            /*$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                var x = $(e.target).attr("href");         // active tab
                if (x=='#s1'){
                    lineChart(datasetsDCCasos,labelsDC);
                }
                if (x == '#s2'){
                    lineChart2(datasetsDCTasas,labelsDC);
                }
                if (x=='#ss1'){
                    lineChart3(datasetsDSCasos,labelsDS);
                }
                if (x == '#ss2'){
                    lineChart4(datasetsDSTasas,labelsDS);
                }
            });*/

            //MAPA DENGUE SOSPECHOSO
            function getDataMapaDengueSospechoso() {

                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.sMapasUrl, {
                    codPato: "0610,0654",
                    semI : $("#semanaI").val(),
                    semF : $("#semanaF").val(),
                    anio : $("#anioF").val(),
                    nivel : $("#nivelUsuario").val(),
                    nivelPais : $('input[name="nivelPais"]:checked', '#parameters_form').val(),
                    ajax: 'true'
                }, function(data) {
                    countryData = [];
                    for(var row in data){
                        countryData[data[row][0]] = data[row][1];
                    }
                    var nombreMapa = '', tituloMapa = "Casos";
                    var nivel = $("#nivelUsuario").val();
                    if ( nivel == "PAIS"){
                        if ( $('input[name="nivelPais"]:checked', '#parameters_form').val()=='true') {
                            nombreMapa = 'nicaragua_mill_en';
                        }else{
                            nombreMapa = 'nicaragua_mun_mill_en';
                        }
                    }else if (nivel == "SILAIS"){
                        nombreMapa = 'nicaragua_mun_mill_en';
                    }

                    $('#vector-map').html('');
                    $('#vector-map').vectorMap({
                        map : nombreMapa,
                        backgroundColor : '#fff',
                        regionStyle : {
                            initial : {
                                fill : '#c4c4c4'
                            },
                            hover : {
                                "fill-opacity" : 1
                            }
                        },
                        series : {
                            regions : [{
                                values : countryData,
                                scale : ['#008141','#ffd700','#800000'],
                                normalizeFunction: 'polynomial',
                                attribute: 'fill',
                                legend: {
                                    horizontal: true,
                                    cssClass: 'jvectormap-legend-icons',
                                    title: tituloMapa
                                }
                            }]
                        },
                        onRegionTipShow: function(event, label, code){
                            label.html(
                                    '<b>'+label.html()+'</b></br>'+
                                    '<b>'+tituloMapa+': </b>'+(countryData[code] === undefined?'-':countryData[code])
                            );
                        }
                    });
                    setTimeout($.unblockUI, 500);
                })
                    .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " error:" + errorThrown);
                        setTimeout($.unblockUI, 5);
                    });
            }

            //MAPA DENGUE CONFIRMADO
            function getDataMapaDengueConfirmado() {

                bloquearUI(parametros.blockMess);
                $.getJSON(parametros.sMapasUrl, {
                    codPato: "0612,0613",
                    semI : $("#semanaI").val(),
                    semF : $("#semanaF").val(),
                    anio : $("#anioF").val(),
                    nivel : $("#nivelUsuario").val(),
                    nivelPais : $('input[name="nivelPais2"]:checked', '#parameters_form2').val(),
                    ajax: 'true'
                }, function(data) {
                    countryData2 = [];
                    for(var row in data){
                        countryData2[data[row][0]] = data[row][1];
                    }
                    var nombreMapa = '', tituloMapa = "Casos";
                    var nivel = $("#nivelUsuario").val();
                    if ( nivel == "PAIS"){
                        if ( $('input[name="nivelPais2"]:checked', '#parameters_form2').val()=='true') {
                            nombreMapa = 'nicaragua_mill_en';
                        }else{
                            nombreMapa = 'nicaragua_mun_mill_en';
                        }
                    }else if (nivel == "SILAIS"){
                        nombreMapa = 'nicaragua_mun_mill_en';
                    }

                    $('#vector-map2').html('');
                    $('#vector-map2').vectorMap({
                        map : nombreMapa,
                        backgroundColor : '#fff',
                        regionStyle : {
                            initial : {
                                fill : '#c4c4c4'
                            },
                            hover : {
                                "fill-opacity" : 1
                            }
                        },
                        series : {
                            regions : [{
                                values : countryData2,
                                scale : ['#008141','#ffd700','#800000'],
                                normalizeFunction: 'polynomial',
                                attribute: 'fill',
                                legend: {
                                    horizontal: true,
                                    cssClass: 'jvectormap-legend-icons',
                                    title: tituloMapa
                                }
                            }]
                        },
                        onRegionTipShow: function(event, label, code){
                            label.html(
                                    '<b>'+label.html()+'</b></br>'+
                                    '<b>'+tituloMapa+': </b>'+(countryData2[code] === undefined?'-':countryData2[code])
                            );
                        }
                    });
                    setTimeout($.unblockUI, 500);
                })
                    .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " error:" + errorThrown);
                        setTimeout($.unblockUI, 5);
                    });
            }

            var nivel = $("#nivelUsuario").val();
            /*if ( nivel == "PAIS"){
                $("#divMapas").show();
                $("#rev-toggles").show();
                $("#rev-toggles2").show();
                getDataMapaDengueSospechoso();
                getDataMapaDengueConfirmado();
            }else if (nivel == "SILAIS") {
                $("#divMapas").show();
                $("#rev-toggles").hide();
                $("#rev-toggles2").hide();
                getDataMapaDengueSospechoso();
                getDataMapaDengueConfirmado();

            }else
            {
                $("#divMapas").hide();
            }
            $('input[type=radio][name=nivelPais2]').change(function() {
                getDataMapaDengueConfirmado();
            });

            $('input[type=radio][name=nivelPais]').change(function() {
                getDataMapaDengueSospechoso();
            });*/

            /*TABLAS */
            var responsiveHelper_dt_basic = undefined;
            var breakpointDefinition = {
                tablet: 1024,
                phone: 480
            };
            var table1 = $('#noti_sinresultado').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true,
                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#noti_sinresultado'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });
            var table2 = $('#noti_embarazadas').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true,
                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#noti_embarazadas'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });
            var table3 = $('#noti_hospitalizados').dataTable({
                "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>" +
                    "t" +
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                "autoWidth": true,
                "preDrawCallback": function () {
                    // Initialize the responsive datatables helper once.
                    if (!responsiveHelper_dt_basic) {
                        responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#noti_hospitalizados'), breakpointDefinition);
                    }
                },
                "rowCallback": function (nRow) {
                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                },
                "drawCallback": function (oSettings) {
                    responsiveHelper_dt_basic.respond();
                }
            });

            function getNotiSinResultado() {
                bloquearUI(parametros.blockMess);
                table1.fnClearTable();
                $.getJSON(parametros.sSinResultadoUrl, {
                    nivel : $("#nivelUsuario").val(),
                    conSubUnidades : true,
                    ajax: 'true'
                }, function(data) {
                    var encontrado;
                    var mostrar=true;
                    for(var row in data){
                        var actionUrl;
                        switch (data[row].codtipoNoti) {
                            case 'TPNOTI|SINFEB':
                                actionUrl = parametros.febrilesUrl+data[row].idNotificacion;
                                break;
                            case 'TPNOTI|IRAG':
                                actionUrl = parametros.iragUrl+data[row].idNotificacion;
                                break;
                            case 'TPNOTI|PCNT':
                                actionUrl = parametros.pacienteUrl+data[row].idNotificacion;
                                break;
                            default:
                                actionUrl = '#';
                                mostrar = false; //si es otro tipo de notificación no mostrar en la tabla
                                break;
                        }
                        if (mostrar) {
                            table1.fnAddData([data[row].persona, data[row].edad, data[row].sexo, data[row].embarazada, data[row].municipio, data[row].tipoNoti, data[row].fechaRegistro, data[row].fechaInicioSintomas, data[row].SILAIS, data[row].unidad, '<a target="_blank" title="Ver" href=' + actionUrl + ' class="btn btn-primary btn-xs"><i class="fa fa-mail-forward"></i></a>']);
                            encontrado = true;
                        }
                        mostrar=true; //se reinicia valor bandera
                    }
                    if(!encontrado){
                        //showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
                        title='';
                    }
                    setTimeout($.unblockUI, 500);
                })
                    .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " er:" + errorThrown);
                        setTimeout($.unblockUI, 5);
                    });
            }

            function getNotiEmbarazadas() {
                bloquearUI(parametros.blockMess);
                table2.fnClearTable();
                $.getJSON(parametros.sEmbarazadasUrl, {
                    nivel : $("#nivelUsuario").val(),
                    conSubUnidades : true,
                    ajax: 'true'
                }, function(data) {
                    var encontrado;
                    var mostrar=true;
                    for(var row in data){
                        var actionUrl;
                        switch (data[row].codtipoNoti) {
                            case 'TPNOTI|SINFEB':
                                actionUrl = parametros.febrilesUrl+data[row].idNotificacion;
                                break;
                            case 'TPNOTI|IRAG':
                                actionUrl = parametros.iragUrl+data[row].idNotificacion;
                                break;
                            case 'TPNOTI|PCNT':
                                actionUrl = parametros.pacienteUrl+data[row].idNotificacion;
                                break;
                            default:
                                actionUrl = '#';
                                mostrar = false; //si es otro tipo de notificación no mostrar en la tabla
                                break;
                        }
                        if (mostrar) {
                            table2.fnAddData([data[row].persona, data[row].edad, data[row].municipio, data[row].tipoNoti, data[row].fechaRegistro, data[row].fechaInicioSintomas, data[row].SILAIS, data[row].unidad, '<a target="_blank" title="Ver" href=' + actionUrl + ' class="btn btn-primary btn-xs"><i class="fa fa-mail-forward"></i></a>']);
                            encontrado = true;
                        }
                        mostrar=true; //se reinicia valor bandera
                    }
                    if(!encontrado){
                        //showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
                        title='';
                    }
                    setTimeout($.unblockUI, 500);
                })
                    .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " er:" + errorThrown);
                        setTimeout($.unblockUI, 5);
                    });
            }

            function getNotiHospitalizados() {
                bloquearUI(parametros.blockMess);
                table3.fnClearTable();
                $.getJSON(parametros.sHospitalizadosUrl, {
                    nivel : $("#nivelUsuario").val(),
                    conSubUnidades : true,
                    ajax: 'true'
                }, function(data) {
                    var encontrado;
                    var mostrar=true;
                    for(var row in data){
                        var actionUrl;
                        switch (data[row].codtipoNoti) {
                            case 'TPNOTI|SINFEB':
                                actionUrl = parametros.febrilesUrl+data[row].idNotificacion;
                                break;
                            case 'TPNOTI|IRAG':
                                actionUrl = parametros.iragUrl+data[row].idNotificacion;
                                break;
                            case 'TPNOTI|PCNT':
                                actionUrl = parametros.pacienteUrl+data[row].idNotificacion;
                                break;
                            default:
                                actionUrl = '#';
                                mostrar = false; //si es otro tipo de notificación no mostrar en la tabla
                                break;
                        }
                        if (mostrar) {
                            table3.fnAddData([data[row].persona, data[row].edad, data[row].sexo, data[row].embarazada, data[row].municipio, data[row].tipoNoti, data[row].fechaRegistro, data[row].fechaInicioSintomas, data[row].SILAIS, data[row].unidad, '<a target="_blank" title="Ver" href=' + actionUrl + ' class="btn btn-primary btn-xs"><i class="fa fa-mail-forward"></i></a>']);
                            encontrado = true;
                        }
                        mostrar=true; //se reinicia valor bandera
                    }
                    if(!encontrado){
                        //showMessage(parametros.noData, parametros.msgNoData, "#AF801C", "fa fa-warning", 3000);
                        title='';
                    }
                    setTimeout($.unblockUI, 500);
                })
                    .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(" status: " + textStatus + " er:" + errorThrown);
                        setTimeout($.unblockUI, 5);
                    });
            }

            getNotiSinResultado();
            getNotiEmbarazadas();
            getNotiHospitalizados();
        }
    }
}();