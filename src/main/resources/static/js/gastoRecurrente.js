let vecesARepetir = document.querySelector('[data-repeticion-gasto]');
const minVeces = vecesARepetir.getAttribute('min');
const maxVeces = vecesARepetir.getAttribute('max');

const disminuirVecesBtn = document.querySelector('[data-disminuir-veces]');
const aumentarVecesBtn = document.querySelector('[data-aumentar-veces]');

const guardadRecurrenciaBtn = document.getElementById('guardad-recurrencia-btn');

// botones para definir la periodicidad
let allButtonsPeriodicidad = document.querySelectorAll('#modal-gasto-recurrente .form-group button')
const btn7Dias = document.getElementById('periodo-7dias');
const btn15Dias = document.getElementById('periodo-15dias');
const btnMensual = document.getElementById('periodo-mensual');
const btnPersonalizado = document.getElementById('periodo-personalizado');
let periodicidadTemp;

// para el periodo personalizado:
const selectorPeriodoPersonalizado = document.getElementById('selector-periodo-personalizado');
const periodoPersonalizado = document.querySelector('[data-periodo-personalizado]');
const disminuirDiasBtn = document.querySelector('[data-disminuir-dias]');
const aumentarDiasBtn = document.querySelector('[data-aumentar-dias]');

// inputs del formulario de crear gasto
const vecesARepetirInput = document.getElementById('vecesARepetir');
const periodicidadInput = document.getElementById('periodicidad');

$('#modal-gasto-recurrente').on('shown.bs.modal', function(){
    // inicializo el contador
    vecesARepetir.value = 1;
    periodoPersonalizado.value = 1;
    periodicidadTemp = 1;
});

function incrementNumber() {
    if (vecesARepetir.value < Number(maxVeces)) {
        vecesARepetir.value++;
    }
  }
  
function decrementNumber() {
    if (vecesARepetir.value > Number(minVeces)) {
        vecesARepetir.value--; 
    }
}

function incrementDiasPeriodo() {
    periodoPersonalizado.value++;
    periodicidadTemp = periodoPersonalizado.value;
  }

// para periodicidad personalizada
function decrementDiasPeriodo() {
    periodoPersonalizado.value--;
    periodicidadTemp = periodoPersonalizado.value;
}

function updateInputsInGastoForm(){
    vecesARepetirInput.value = vecesARepetir.value;
    periodicidadInput.value = Number(periodicidadTemp);
    $('.d-none.recurrente').removeClass('d-none')
}

// al abrir el modal para crear gasto, seteamos inputs de recurrencia a valores por defecto
$('[data-bs-target="#modal-añadir-gasto"]').on('click', function(){
    vecesARepetirInput.value = "";
    periodicidadInput.value = "";
    $('.recurrente').addClass('d-none');
});

$('#modal-gasto-recurrente').on('hidden.bs.modal', function(){
    selectorPeriodoPersonalizado.classList.add('hidden');
    allButtonsPeriodicidad.forEach(btn => btn.classList.remove('selected'));
    allButtonsPeriodicidad.forEach(btn => btn.classList.remove('non-selected'));
    $('#modal-añadir-gasto').modal('show');
});

/* == click events == */

// para definir cuántas veces se repetirá el gasto:
disminuirVecesBtn.addEventListener('click', decrementNumber);
aumentarVecesBtn.addEventListener('click', incrementNumber);

// botones para definir la periodicidad:

btn7Dias.onclick = set7DiasAndHighlightBtn;
btn15Dias.onclick = set15DiasAndHighlightBtn;
btnMensual.onclick = set30DiasAndHighlightBtn;
btnPersonalizado.onclick = showOptionsAndHighlightBtn;

function set7DiasAndHighlightBtn(){
    periodicidadTemp = 7;
    highlightBtn(this);
    selectorPeriodoPersonalizado.classList.add('hidden');
}

function set15DiasAndHighlightBtn(){
    periodicidadTemp = 15;
    highlightBtn(this);
    selectorPeriodoPersonalizado.classList.add('hidden');
}

function set30DiasAndHighlightBtn(){
    periodicidadTemp = 30;
    highlightBtn(this);
    selectorPeriodoPersonalizado.classList.add('hidden');
}

function showOptionsAndHighlightBtn(){
    selectorPeriodoPersonalizado.classList.remove('hidden');
    highlightBtn(this);
}

function highlightBtn(button){
    allButtonsPeriodicidad.forEach(btn => btn.classList.remove('selected'));
    allButtonsPeriodicidad.forEach(btn => btn.classList.add('non-selected'));
    button.classList.remove('non-selected');
    button.classList.add('selected');
}

// botones para definir periodocidad personalizada:
disminuirDiasBtn.addEventListener('click', decrementDiasPeriodo);
aumentarDiasBtn.addEventListener('click', incrementDiasPeriodo);

// para guardar los valores elegidos en el forulario 'crear gasto'
guardadRecurrenciaBtn.onclick = updateInputsInGastoForm;