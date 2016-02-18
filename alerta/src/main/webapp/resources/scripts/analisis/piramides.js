var ViewReport = function () {
	
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
        	var title ="";
        	$('#parameters_form').validate({
    			// Rules for form validation
    				rules : {
    					codArea : {
    						required : true
    					},
    					anioI : {
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
                        }
    				},
    				// Do not change code below
    				errorPlacement : function(error, element) {
    					error.insertAfter(element.parent());
    				},
    				submitHandler: function (form) {
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
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').hide();
            				$('#unidad').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
            				$('#silais').hide();
            				$('#departamento').show();
            				$('#municipio').hide();
            				$('#unidad').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').show();
            				$('#unidad').hide();
            			}
            			else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
            				$('#silais').show();
            				$('#departamento').hide();
            				$('#municipio').show();
            				$('#unidad').show();
            			}
                    });
            
            function getData() {
            	
            	bloquearUI(parametros.blockMess); 
            	$.getJSON(parametros.sActionUrl, $('#parameters_form').serialize(), function(data) {
            		var y = $('#anioI option:selected').val();
            		hombres=[];
            		mujeres=[];
            		title = 'Piramides Poblacionales ' + y;
            		if ($('#codArea option:selected').val() == "AREAREP|PAIS"){
    					title = title + '</br>República de Nicaragua';
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|SILAIS"){
    					title = title + '</br>'+$('#codSilaisAtencion option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|DEPTO"){
    					title = title + '</br>Departamento de '+$('#codDepartamento option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|MUNI"){
    					title = title + '</br>Municipio: '+$('#codMunicipio option:selected').text();
    				}
    				else if ($('#codArea option:selected').val() == "AREAREP|UNI"){
    					title = title + '</br>Unidad de Salud: '+$('#codUnidadAtencion option:selected').text();
    				}
            		for(var row in data){
            			hombres.push([data[row][0],data[row][1]]);
            			mujeres.push([data[row][0],data[row][2]]);
            		}
            		drawPyramid(hombres, mujeres);
            		setTimeout($.unblockUI, 500);
    			})
    			.fail(function(XMLHttpRequest, textStatus, errorThrown) {
    				alert(" status: " + textStatus + " error:" + errorThrown);
				    setTimeout($.unblockUI, 5);
				});
            }
            
            function drawPyramid(hombres, mujeres) {
            	$('#lineChart-title').html("<h5>"+title+"</h5>");
                pseries = [
                  {
                      label: 'Hombres',
                      color: '#0066FF',
                      data: hombres
                  },
                  {
                      label: 'Mujeres',
                      color: '#FF0000',
                      data: mujeres,
                      pyramid: {
                          direction: 'L'
                      }
                  }
                ];

                $.plot($('#pyramid'), pseries, {
                    series: {
                      pyramid: {
                        show: true
                      },
                        bars: { fillColor: { colors: [ { opacity: 1 }, { opacity: 1 } ] } }
                    },
                    xaxis: {
                      tickFormatter: function(v) {
                        return v/1000 + " K";
                      }
                    },
                    grid: {
                        hoverable: true
                    }
                });

                function showTooltip(x, y, contents) {
                    $('<div id="tooltip">' + contents + "</div>").css({
                      position: "absolute",
                      display: "none",
                      top: y + 5,
                      left: x + 5,
                      border: "1px solid #fdd",
                      padding: "2px",
                      "background-color": "#FACC2E",
                      opacity: .9
                    }).appendTo("body").fadeIn(200);
                }

                var previousPoint = null,
                    previousSeries = null;

                $('#pyramid').bind('plothover', function(event, pos, item) {
                    $("#x").text(pos.x.toFixed(2));
                    $("#y").text(pos.y.toFixed(2));

                    if (item) {
                        if (previousPoint != item.dataIndex || previousSeries != item.series) {
                            previousPoint = item.dataIndex;
                            previousSeries = item.series;
                            $("#tooltip").remove();
                            var data = item.series.data[item.dataIndex];
                            var x = data[1];
                            var group = item.series.yaxis.ticks[data[0]].label;
                            showTooltip(item.pageX, item.pageY, item.series.label + " con edad en  " + group + " = " + x);
                        }
                    } else {
                        $("#tooltip").remove();
                        previousPoint = null;
                    }
                });
            };
        }
    };

}();