
// GLOBAL VARIABLES
// For all the 4 color pickers (in Modals: GN, GE, CN, CE)
let colorPickers = $('.colorPicker'); 
let colorInputs = colorPickers.find('input');
let colorLabels = colorPickers.find('label');

// agrego un nbsp a azul marino para que se comporte como una sola palabra
for (const label of colorLabels) {
  if (label.htmlFor == 'color_azul marino' || label.htmlFor == 'color_azul marino_grupo' || label.htmlFor == 'color_azul marino_editarGrupo' || label.htmlFor == 'color_azul marino_editarCategoria'){
      label.innerHTML = 'Azul&nbsp;Marino';
  }
}

function highligthColorLabel(colorInputId) {
  for (const label of colorLabels) {
    (label.htmlFor == colorInputId) ? label.classList.add('selected') : label.classList.remove('selected')
  }
}

let inputClick = function(){
  let inputId = $(this).prop('id');
  highligthColorLabel(inputId);
}

// color inputs click events
for (const input of colorInputs) {
  input.onclick = inputClick;
}


// poner aclaración de esta sección
function blockColorPicker(cp){
  cp.addClass('blocked');
}

function resetColorPicker(cp){
  cp.removeClass('blocked');
  let color_Labels = cp.find('label');
  color_Labels.each( function() {
    $(this).removeClass('selected');
  });
}

function selectColorInput(colorInputs, colorId){
  colorInputs.each(function(){
    if ($(this).prop('value') == colorId) {
      console.log($(this).prop('id') + " color id: " + colorId)
      $(this).prop('checked', 'true');
      highligthColorLabel($(this).prop('id'));
    } 
    // else {
    //   $(this).prop('checked', 'false');
    // }
  });
}

// get elements in categories modals (Nueva y Editar) 
let formSelect_CN = $('#groups_ModalCN');
let formSelect_CE = $('#groups_ModalCE');

let options_CN = formSelect_CN.find('option');
let options_CE = formSelect_CE.find('option');
  
let colorPicker_CN = $('#colorPickerCN'); 
let colorPicker_CE = $('#colorPickerCE');

let colorInputs_CN = colorPicker_CN.find('input');
let colorLabels_CN = colorPicker_CN.find('label');
let colorInputs_CE = colorPicker_CE.find('input');
let colorLabels_CE = colorPicker_CE.find('label');
  
let groupColorId = 0; // id del color del grupo 'sin agrupar'
let selectedInputId;

let colorPickerLabel_CN = document.getElementById('pickColorLabel_CN');
let colorPickerLabel_CE = document.getElementById('pickColorLabel_CE');

// check changes in form nueva categoria and select color accordingly
formSelect_CN.change(function(){
  let selectedGroup;
  // see which option is selected
  options_CN.each(function(){
    if ($(this).prop('selected') == true && $(this).prop('value') != 2) { // 2 es el valor del grupo 'sin agrupar' 
      groupColorId = $(this).attr('data-group-color');
      selectedGroup = $(this).text();
    }
  });

  // busco y selecciono el color input asociado al grupo recién seleccionado
  selectColorInput(colorInputs_CN, groupColorId);

  // Luego de resaltar el color asociado al grupo, bloqueo y cambio mensaje
  blockColorPicker(colorPicker_CN);
  //colorPickerLabel_CN.innerText = 'Color del grupo al que pertenece:';
  colorPickerLabel_CN.innerText = "Color del grupo '" + selectedGroup + "':";


  // si se vuelve a elegir la opción de no agrupar, restablezco el color Picker a estado inicial
  options_CN.each(function(){
    if ($(this).prop('selected') == true && $(this).prop('value') == 2) { // 2 es el valor del grupo 'sin agrupar' 
      colorPickerLabel_CN.innerText = 'Elige un color distintivo';
      resetColorPicker(colorPicker_CN);
    }
  });

});


// check changes in form editar categoria and select color accordingly
formSelect_CE.change(function(){
  let selectedGroup;
  // see which option is selected
  options_CE.each(function(){
    if ($(this).prop('selected') == true && $(this).prop('value') != 2) { // 2 es el valor del grupo 'sin agrupar' 
      groupColorId = $(this).attr('data-group-color');
      selectedGroup = $(this).text();
    }
  });

  // busco y selecciono el input color asociado al grupo recién seleccionado
  selectColorInput(colorInputs_CE, groupColorId);

  // Luego de resaltar el color asociado al grupo, bloqueo y cambio mensaje
  blockColorPicker(colorPicker_CE);
  colorPickerLabel_CE.innerText = "Color del grupo '" + selectedGroup + "':";
//  colorPickerLabel_CE.innerText = 'Color del grupo al que pertenece:';

  // si se vuelve a elegir la opción de no agrupar, restablezco el color Picker a estado inicial
  options_CE.each(function(){
    if ($(this).prop('selected') == true && $(this).prop('value') == 2) { // 2 es el valor del grupo 'sin agrupar' 
      colorPickerLabel_CE.innerText = 'Elige un color distintivo';
      resetColorPicker(colorPicker_CE);
    }
  });

});

/* == Populates edit modals == */

function fillModalCE(categoriaId, nombre, grupoId, colorId, groupColor_Id) {
  $('#formEditarCategoria').attr('action','/categorias/modificar/' + categoriaId ); 

  $('#nombreEditCategoria').val(nombre);

  let selectedGroup;
  // selecciono grupo del select de grupos
  options_CE.each(function(){
    if ($(this).prop('value') == grupoId) {
      $(this).prop('selected', 'true');
      selectedGroup = $(this).text();
    }
  });

  // busco y selecciono el input color asociado al grupo recién seleccionado
  selectColorInput(colorInputs_CE, colorId);

  // si el grupo de la categoria no es el 'sin agrupar' (id=2)
  if (grupoId != 2) {
    blockColorPicker(colorPicker_CE);
    colorPickerLabel_CN.innerText = "Color del grupo '" + selectedGroup + "':";
    //colorPickerLabel_CE.innerText = 'Color del grupo al que pertenece:';
  }

}

 function passDataEditGroupModal(groupId, nombre, groupColorId){
  $('#formEditGrupo').attr('action','/grupos/modificar/' + groupId );
  $('#nombreEditGrupo').val(nombre);
    
  let colorPickerGE = $('#colorPickerGE');
  let colorInputs_GE = colorPickerGE.find('input');
  selectColorInput(colorInputs_GE, groupColorId);
}

function selectGroupColorInColorPicker(groupId, groupColorId){
  options_CN.each(function(){
    if ($(this).prop('value') == groupId) {
      $(this).prop('selected','true');
      selectedGroup = $(this).text();
    }
  });

  // busco y selecciono el input color asociado al grupo recién seleccionado
  selectColorInput(colorInputs_CN, groupColorId);
// formSelect_CN.toggle();
// document.getElementById('groupListLabel_NuevaCategoria').innerText = 'Grupo al que pertenece: ' + selectedGroup;
  // si el grupo de la categoria no es el 'sin agrupar' (id=2)
  blockColorPicker(colorPicker_CN);
  colorPickerLabel_CN.innerText = "Color del grupo '" + selectedGroup + "':";
}

// reset form nueva categoria when modal closes

$("#modal-añadir-categoria").on('hidden.bs.modal', function(){
  formSelect_CN.trigger("reset");
  colorPickerLabel_CN.innerText = 'Elige un color distintivo';

  let opcionPorDefecto = $('#groups_ModalCN option').filter(function () { 
    return $(this).html() == "Ninguno";
  });

  opcionPorDefecto.prop('selected','true');

  resetColorPicker(colorPicker_CN);
});