/**
 * Created by FIRSTICT on 6/15/2016.
 */
var PatoGroup = function(){
   return {
      init : function(parametros){
          function blockUI(){
              var loc = window.location;
              var pathName = loc.pathname.substring(0,loc.pathname.indexOf('/', 1)+1);
              //var mess = $("#blockUI_message").val()+' <img src=' + pathName + 'resources/img/loading.gif>';
              var mess = '<img src=' + pathName + 'resources/img/ajax-loading.gif> ' + parametros.blockMess;
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
                  baseZ: 1051
              });
          }

          function unBlockUI() {
              setTimeout($.unblockUI, 500);
          }

          var responsiveHelper_dt_basic = undefined;
          var breakpointDefinition = {
              tablet : 1024,
              phone : 480
          };
          var table1 = $('#records').dataTable({
              "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                  "t"+
                  "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
              "autoWidth" : true,
              "columns": [
                  {
                      "className": 'groupHeader'
                  },
                  {
                      "className": 'patoHeader'
                  },
                  {
                      "className":      'edit',
                      "orderable":      false
                  },
                  {
                      "className":      'override',
                      "orderable":      false
                  }
              ],
              "preDrawCallback" : function() {
                  // Initialize the responsive datatables helper once.
                  if (!responsiveHelper_dt_basic) {
                      responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#records'), breakpointDefinition);
                  }
              },
              "rowCallback" : function(nRow) {
                  responsiveHelper_dt_basic.createExpandIcon(nRow);
              },
              "drawCallback" : function(oSettings) {
                  responsiveHelper_dt_basic.respond();
              },
              fnDrawCallback : function() {
                  $('.edit')
                      .off("click", editGroup)
                      .on("click", editGroup);
                  $('.override')
                      .off("click", overrideGroup)
                      .on("click", overrideGroup);
              }
          });

          var table2 = $('#recordsPato').dataTable({
              "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                  "t"+
                  "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
              "autoWidth" : true,
              "columns": [
                  {
                      "className": 'groupHeader'
                  },
                  {
                      "className": 'patoHeader'
                  },
                  {
                      "className":      'overridePatoGroup',
                      "orderable":      false
                  }
              ],
              "preDrawCallback" : function() {
                  // Initialize the responsive datatables helper once.
                  if (!responsiveHelper_dt_basic) {
                      responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#recordsPato'), breakpointDefinition);
                  }
              },
              "rowCallback" : function(nRow) {
                  responsiveHelper_dt_basic.createExpandIcon(nRow);
              },
              "drawCallback" : function(oSettings) {
                  responsiveHelper_dt_basic.respond();
              },
              fnDrawCallback : function() {
                  $('.overridePatoGroup')
                      .off("click", overridePathoGroup)
                      .on("click", overridePathoGroup);
              }
          });

          <!-- Validacion formulario -->
          var $validator1 =$("#group-form").validate({
              // Rules for form validation
              rules: {
                  nombreGrupo: {required : true}
              },
              // Do not change code below
              errorPlacement : function(error, element) {
                  error.insertAfter(element.parent());
              }
          });

          <!-- Validacion formulario -->
          var $validator2 =$("#patho-form").validate({
              // Rules for form validation
              rules: {
                  idPatologia: {required : true}
              },
              // Do not change code below
              errorPlacement : function(error, element) {
                  error.insertAfter(element.parent());
              }
          });

          getGroups();

          function getGroups() {
              $.getJSON(parametros.groupsUrl, {
                  ajax: 'true'
              }, function (data) {
                  table1.fnClearTable();
                  var len = Object.keys(data).length;
                  for (var i = 0; i < len; i++) {

                      var btnEdit = '<button type="button" title="Editar" class="btn btn-primary btn-xs" data-id="'+data[i].idGrupo+ "," + data[i].nombre +
                          '" > <i class="fa fa-edit"></i>' ;
                      var btnOverride = '<button type="button" title="Anular" class="btn btn-danger btn-xs" data-id="'+data[i].idGrupo+
                          '" > <i class="fa fa-times"></i>' ;

                      table1.fnAddData(
                          [data[i].nombre,data[i].patologias , btnEdit,btnOverride]);


                  }
              })
          }

          function getPathologies(idGroup) {
              $.getJSON(parametros.pathosUrl, {
                  idGroup: idGroup,
                  ajax: 'true'
              }, function (data) {
                  table2.fnClearTable();
                  var len = Object.keys(data).length;
                  for (var i = 0; i < len; i++) {

                      var btnOverride = '<button type="button" title="Anular" class="btn btn-danger btn-xs" data-id="'+data[i].idGrupoPatologia+'" > <i class="fa fa-times"></i>';

                      table2.fnAddData(
                          [data[i].patologia.codigo,data[i].patologia.nombre, btnOverride]);


                  }
              })
          }

          function getPathologiesAvailable(){
              blockUI();
              $.getJSON(parametros.pathosAvailableUrl, {
                  idGroup: $("#idGrupo").val(),
                  ajax: 'true'
              }, function (data) {
                  var html = null;
                  var len = data.length;
                  html += '<option value="">' + $("#text_opt_select").val() + '...</option>';
                  for (var i = 0; i < len; i++) {
                      html += '<option value="' + data[i].id + '">'
                          + data[i].codigo +' - '+ data[i].nombre
                          + '</option>';
                  }
                  $('#idPatologia').html(html);
              });

              $('#idPatologia').val('').change();
              unBlockUI();
          }

          function editGroup(){
              var data =  $(this.innerHTML).data('id');
              if(data != null){
                  var detalle = data.split(",");
                  var id= detalle[0];
                  var nombre = detalle[1];
                  //$('#lblHeader').html("<i class='fa-fw fa fa-link'></i>"+parametros.lblHeader + " - " + nombre);
                  $('#div1').hide();
                  $('#div2').fadeIn('slow');
                  $('#div3').fadeIn('slow');
                  $('#dBack').show();
                  getPathologies(id);
                  $('#idGrupo').val(id);
                  $('#nombreGrupo').val(nombre);
              }
          }

          function overrideGroup(){
              var data =  $(this.innerHTML).data('id');
              if(data != null){
                  var opcSi = $("#msg_yes").val();
                  var opcNo = $("#msg_no").val();

                  $.SmartMessageBox({
                      title: $("#msg_conf").val(),
                      content: $("#msg_override_confirm_c").val(),
                      buttons: '['+opcSi+']['+opcNo+']'
                  }, function (ButtonPressed) {
                      if (ButtonPressed === opcSi) {
                          deleteGroup(data);
                      }
                      if (ButtonPressed === opcNo) {
                          $.smallBox({
                              title: $("#msg_override_cancel").val(),
                              content: "<i class='fa fa-clock-o'></i> <i>" + $("#disappear").val() + "</i>",
                              color: "#C46A69",
                              iconSmall: "fa fa-times fa-2x fadeInRight animated",
                              timeout: 4000
                          });
                      }
                  });
              }
          }

          function overridePathoGroup(){
              var data =  $(this.innerHTML).data('id');
              if(data != null){
                  var opcSi = $("#msg_yes").val();
                  var opcNo = $("#msg_no").val();

                  $.SmartMessageBox({
                      title: $("#msg_conf").val(),
                      content: $("#msg_overridePatho_confirm_c").val(),
                      buttons: '['+opcSi+']['+opcNo+']'
                  }, function (ButtonPressed) {
                      if (ButtonPressed === opcSi) {
                          deletePathoGroup(data);
                      }
                      if (ButtonPressed === opcNo) {
                          $.smallBox({
                              title: $("#msg_override_cancel").val(),
                              content: "<i class='fa fa-clock-o'></i> <i>" + $("#disappear").val() + "</i>",
                              color: "#C46A69",
                              iconSmall: "fa fa-times fa-2x fadeInRight animated",
                              timeout: 4000
                          });
                      }
                  });
              }
          }

          function showModal(){
              $("#myModal").modal({
                  show: true
              });
          }

          function saveGroup() {
              var obj = {};
              obj['mensaje'] = '';
              obj['idGrupo'] = $('#idGrupo').val();
              obj['nombre'] = $('#nombreGrupo').val();

              blockUI();
              $.ajax(
                  {
                      url: parametros.saveGroupUrl,
                      type: 'POST',
                      dataType: 'json',
                      data: JSON.stringify(obj),
                      contentType: 'application/json',
                      mimeType: 'application/json',
                      success: function (data) {
                          unBlockUI();
                          if (data.mensaje.length > 0) {
                              $.smallBox({
                                  title: data.mensaje,
                                  content: $("#disappear").val(),
                                  color: "#C46A69",
                                  iconSmall: "fa fa-warning",
                                  timeout: 4000
                              });
                          } else {
                              $('#idGrupo').val(data.idGrupo);
                              getGroups();
                              var msg = $("#succGroup").val();
                              $.smallBox({
                                  title: msg,
                                  content: $("#disappear").val(),
                                  color: "#739E73",
                                  iconSmall: "fa fa-success",
                                  timeout: 4000
                              });

                          }
                      }

                  });
          }

          function savePathoGroup() {
              var obj = {};
              obj['mensaje'] = '';
              obj['idGrupo'] = $('#idGrupo').val();
              obj['nombreGrupo'] = $('#nombreGrupo').val();
              obj['idPatologia'] = $('#idPatologia').val();


              blockUI();
              $.ajax(
                  {
                      url: parametros.savePathoGroupUrl,
                      type: 'POST',
                      dataType: 'json',
                      data: JSON.stringify(obj),
                      contentType: 'application/json',
                      mimeType: 'application/json',
                      success: function (data) {
                          if (data.mensaje.length > 0) {
                              $.smallBox({
                                  title: data.mensaje,
                                  content: $("#disappear").val(),
                                  color: "#C46A69",
                                  iconSmall: "fa fa-warning",
                                  timeout: 4000
                              });
                          } else {
                              $('#idGrupo').val(data.idGrupo);
                              getPathologies(data.idGrupo);
                              getPathologiesAvailable();
                              var msg = $("#succPatho").val();
                              $.smallBox({
                                  title: msg,
                                  content: $("#disappear").val(),
                                  color: "#739E73",
                                  iconSmall: "fa fa-success",
                                  timeout: 4000
                              });

                          }
                          unBlockUI();
                      }
                  });
          }

          function deleteGroup(idGrupo) {
              var obj = {};
              obj['mensaje'] = '';
              obj['idGrupo'] = idGrupo;
              blockUI();
              $.ajax(
                  {
                      url: parametros.deleteGroupUrl,
                      type: 'POST',
                      dataType: 'json',
                      data: JSON.stringify(obj),
                      contentType: 'application/json',
                      mimeType: 'application/json',
                      success: function (data) {
                          unBlockUI();
                          if (data.mensaje.length > 0) {
                              $.smallBox({
                                  title: data.mensaje,
                                  content: $("#disappear").val(),
                                  color: "#C46A69",
                                  iconSmall: "fa fa-warning",
                                  timeout: 4000
                              });
                          } else {
                              getGroups();
                              var msg = $("#succDeleteGroup").val();
                              $.smallBox({
                                  title: msg,
                                  content: $("#disappear").val(),
                                  color: "#739E73",
                                  iconSmall: "fa fa-success",
                                  timeout: 4000
                              });

                          }
                      }

                  });
          }

          function deletePathoGroup(idPatoGrupo) {
              var obj = {};
              obj['mensaje'] = '';
              obj['idPatoGrupo'] = idPatoGrupo;
              blockUI();
              $.ajax(
                  {
                      url: parametros.deletePathoGroupUrl,
                      type: 'POST',
                      dataType: 'json',
                      data: JSON.stringify(obj),
                      contentType: 'application/json',
                      mimeType: 'application/json',
                      success: function (data) {
                          unBlockUI();
                          if (data.mensaje.length > 0) {
                              $.smallBox({
                                  title: data.mensaje,
                                  content: $("#disappear").val(),
                                  color: "#C46A69",
                                  iconSmall: "fa fa-warning",
                                  timeout: 4000
                              });
                          } else {
                              getPathologies($("#idGrupo").val());
                              getPathologiesAvailable();
                              var msg = $("#succDeletePatho").val();
                              $.smallBox({
                                  title: msg,
                                  content: $("#disappear").val(),
                                  color: "#739E73",
                                  iconSmall: "fa fa-success",
                                  timeout: 4000
                              });
                          }
                      }

                  });
          }

          $('#btnNew').click(function() {
              $('#dBack').show();
              //$('#lblHeader').html("<i class='fa-fw fa fa-link'></i>"+parametros.lblHeader);
              $('#div2').fadeIn('slow');
              $('#div3').fadeIn('slow');
              $('#div1').hide();
              $('#idGrupo').val('');
              table2.fnClearTable();
          });

          $('#btnSave').click(function() {
              var $validar = $("#group-form").valid();
              if (!$validar) {
                  $validator1.focusInvalid();
                  return false;
              } else {
                  saveGroup();
              }
          });

          $('#btnBack').click(function() {
              $('#dBack').hide();
              //$('#lblHeader').html("<i class='fa-fw fa fa-link'></i>"+parametros.lblHeader);
              $('#div2').hide();
              $('#div3').hide();
              $('#div1').fadeIn('slow');
              $('#idGrupo').val('');
              $('#nombreGrupo').val('');
              getGroups();

          });

          $('#btnAdd').click(function() {
              var $validar = $("#group-form").valid();
              if (!$validar && $('#idGrupo').val()=='') {
                  $validator1.focusInvalid();
                  return false;
              } else {
                  showModal();
                  getPathologiesAvailable();
              }
          });

          $('#btnSavePatho').click(function() {
              var $validarModal = $("#patho-form").valid();
              if (!$validarModal) {
                  $validator2.focusInvalid();
                  return false;
              } else {
                  savePathoGroup();
              }
          });
      }
   } ;
}();