// 1- Selector de año-mes formato calendario

const nextYearBtn = document.querySelector('[data-next-year]');
const prevYearBtn = document.querySelector('[data-prev-year]');

const monthInput = document.querySelector('[data-month]');
const yearInput = document.querySelector('[data-year]');
const minYear = yearInput.getAttribute('min');
const maxYear = yearInput.getAttribute('max');

// month cells in calendar
const months = document.querySelectorAll('#months-grid a'); 

// current(or selected) month and year display elements
const monthNameShown = document.getElementById('month-name');
const yearShown= document.getElementById('year-number');

const months_dictionary = [
  { id: 0, name: "Enero" },
  { id: 1, name: "Febrero" },
  { id: 2, name: "Marzo" },
  { id: 3, name: "Abril" },
  { id: 4, name: "Mayo" },
  { id: 5, name: "Junio" },
  { id: 6, name: "Julio" },
  { id: 7, name: "Agosto" },
  { id: 8, name: "Septiembre" },
  { id: 9, name: "Octubre" },
  { id: 10, name: "Noviembre" },
  { id: 11, name: "Diciembre" }
];

// const currentMonth = new Date().getMonth();
// const currentYear = new Date().getFullYear();

// seteo valores actuales en base a la vista
const currentMonth  = months_dictionary.find(mth => mth.name == monthNameShown.innerText).id;
const currentYear = yearShown.innerText;

const yearMonthInput = document.querySelector('[data-year-month]');

// default values
yearInput.value = currentYear;
monthInput.value = currentMonth + 1;

// the calendar is in a modal, focus current month when modal opens
$( "#year-month-modal" ).on('shown.bs.modal', function(){
  months[currentMonth].focus();
});

function incrementYear() {
  if (yearInput.value < maxYear) {
    yearInput.value++;
  }
}

function decrementYear() {
  if (yearInput.value > minYear) {
    yearInput.value--; 
  }
}

function passMonth(number){
  monthInput.value = number;
}

nextYearBtn.addEventListener('click', incrementYear);
prevYearBtn.addEventListener('click', decrementYear);


/* 2- 
  Selector de año-mes formato condensado ( botón estilo: < Jul 2021 > )
  Muestra el calendario año-mes anterior al clicker la fecha central,
  o envia fecha seleccionada al back automáticamente si se usan los controles de avance/retroceso.
*/

const dateSelectorBtn = document.getElementById('date-selector-btn');
const calendarForm = document.getElementById('year-month-Form');

const nextDateBtn = document.querySelector('[data-next-date]');
const prevDateBtn = document.querySelector('[data-prev-date]');

let tempMonth = currentMonth;
let tempYear = currentYear;

function updatePrevDateAndSubmit() {
  decrementDate();
  updateDateDisplay();
  updateFormInputValues();
  submitDate();
}

function updateNextDateAndSubmit() {
  incrementDate();
  updateDateDisplay();
  updateFormInputValues();
  submitDate();
}

function incrementDate() {
  tempMonth++;
  if (tempMonth > 11) {
    tempMonth = 0;
    tempYear++;
  }
}

function decrementDate() {
  tempMonth--;
  if (tempMonth < 0) {
    tempMonth = 11;
    tempYear--;
  }
}

function updateFormInputValues(){
  monthInput.value = tempMonth + 1;
  yearInput.value = tempYear;
} 

function updateDateDisplay(){
  yearShown.innerText = tempYear;
  monthNameShown.innerText = months_dictionary.find(mth => mth.id == tempMonth).name;
}

function submitDate(){
  calendarForm.submit();
}

// the click events!
prevDateBtn.onclick = updatePrevDateAndSubmit;
nextDateBtn.onclick = updateNextDateAndSubmit;

////

$('#date-selector-btn').mouseenter(function(){
  $('.gastoTotalMes').css('opacity', '0')
});
$('#date-selector-btn').mouseleave(function(){
  $('.gastoTotalMes').css('opacity', '1')
});