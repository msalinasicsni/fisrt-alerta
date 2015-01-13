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
			var responsiveHelper_data_result = undefined;
			var breakpointDefinition = {
				tablet : 1024,
				phone : 480
			};
			
			/* TABLETOOLS */
			var table2 = $('#data_result_2').dataTable({});
			var table1 = $('#data_result').dataTable({
				
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
             	                                     "sFileName": "ddd"+"-*.csv",
             	                                     "sTitle": "ddd",
             	                                     "oSelectorOpts": { filter: 'applied', order: 'current' }
             	                                 },
             	                                 {
             	                                     "sExtends": "pdf",
             	                                     "sFileName": "DD"+"-*.pdf",
	            	                                     "sTitle": ":fff:",
	            	                                     "sPdfMessage": "FF",
             	                                     "oSelectorOpts": { filter: 'applied', order: 'current' },
             	                                     "sPdfOrientation": "landscape",
             	                                 }
             	                                 ]
             	                }
             	            ],
		            "sSwfPath": parametros.dataTablesTTSWF
		        },
				"autoWidth" : true,
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_data_result) {
						responsiveHelper_data_result = new ResponsiveDatatablesHelper($('#data_result'), breakpointDefinition);
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
		
		

            $('#parameters_form').validate({
    			// Rules for form validation
    				rules : {
    					codArea : {
    						required : true
    					}
    				},
    				// Do not change code below
    				errorPlacement : function(error, element) {
    					error.insertAfter(element.parent());
    				},
    				submitHandler: function (form) {
                        table1.fnClearTable();
                        table2.fnClearTable();
                        lineChart();
                        //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                        getData();
                    }
            });

            
            
            function getData() {
            	bloquearUI(parametros.blockMess); 
            	$.getJSON(parametros.sActionUrl, $('#parameters_form').serialize(), function(data) {
            		for (var row in data) {
    					table1.fnAddData(
    							[data[row][0], data[row][1], data[row][2], data[row][3], data[row][4], data[row][5], data[row][6], data[row][7],
    							 data[row][8], data[row][9], data[row][10], data[row][11], data[row][12], data[row][13], data[row][14],
    							 data[row][15], data[row][16], data[row][17], data[row][18], data[row][19], data[row][20], data[row][21],
    							 data[row][22], data[row][23], data[row][24], data[row][25], data[row][26], data[row][27], data[row][28]]);
    					if (row==0){
    						values1 = [data[row][1]+ data[row][2], data[row][3]+ data[row][4], data[row][5]+ data[row][6], data[row][7]+data[row][8], data[row][9]+ data[row][10], data[row][11]+ data[row][12], data[row][13]+ data[row][14],data[row][15]+ data[row][16], data[row][17]+ data[row][18], data[row][19]+ data[row][20], data[row][21]+data[row][22], data[row][23]+ data[row][24], data[row][25]+ data[row][26], data[row][27]+ data[row][28]];
    						series1 = data[row][0];
    					}
    					if (row==1){
    						values2 = [data[row][1]+ data[row][2], data[row][3]+ data[row][4], data[row][5]+ data[row][6], data[row][7]+data[row][8], data[row][9]+ data[row][10], data[row][11]+ data[row][12], data[row][13]+ data[row][14],data[row][15]+ data[row][16], data[row][17]+ data[row][18], data[row][19]+ data[row][20], data[row][21]+data[row][22], data[row][23]+ data[row][24], data[row][25]+ data[row][26], data[row][27]+ data[row][28]];
    						series2 = data[row][0];
    					}
            		}
            		pieChart(25,75);
            		lineChart(values1,values2,series1,series2);
                    setTimeout($.unblockUI, 500);
    			})
    			.fail(function(XMLHttpRequest, textStatus, errorThrown) {
    				alert(" status: " + textStatus + " er:" + errorThrown);
				    setTimeout($.unblockUI, 5);
				});
            }
            
            function pieChart(value1,value2) {
            	// PIE CHART

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
			        responsive: true,
			    };

			    pieData = [
				    {
				        value: 57,
				        color: "rgba(151,187,205,1)",
				        highlight: "rgba(151,187,205,0.8)",
				        label: "Masculino"
				    },
				    {
				        value: 43,
				        color: "rgba(169, 3, 41, 0.7)",
				        highlight: "rgba(169, 3, 41, 0.7)",
				        label: "Femenino"
				    }
			    ];

			    // render chart
			    var ctx = document.getElementById("pieChart").getContext("2d");
			    new Chart(ctx).Pie(pieData, pieOptions);

			    // END PIE CHART

                legend(document.getElementById("pieLegend"), pieData);
            }
            
            function lineChart(values1,values2,series1,series2) {
            	// BAR CHART

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
			        responsive: true,
			    };

			    var barData = {
			        labels: ["0-6 días", "7-27 días", "28d-11 meses", "1 año", "2-4 años", "5-9 años", "10-14 años","15-19 años","20-34 años","35-49 años","50-59 años","60-64 años","65 y + años","Desc"],
			         datasets: [
				        {
				            label: series1,
				            fillColor: "rgba(220,220,220,0.5)",
				            strokeColor: "rgba(220,220,220,0.8)",
				            highlightFill: "rgba(220,220,220,0.75)",
				            highlightStroke: "rgba(220,220,220,1)",
				            data: values1
				        },
				        {
				            label: series2,
				            fillColor: "rgba(151,187,205,0.5)",
				            strokeColor: "rgba(151,187,205,0.8)",
				            highlightFill: "rgba(151,187,205,0.75)",
				            highlightStroke: "rgba(151,187,205,1)",
				            data: values2
				        }
				    ]
			    };

			    // render chart
			    var ctx = document.getElementById("lineChart").getContext("2d");
			    new Chart(ctx).Bar(barData, barOptions);

			    // END BAR CHART

                legend(document.getElementById("lineLegend"), barData);
            }
            	
        }
    };

}();