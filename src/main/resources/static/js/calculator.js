// (function() {
//     "use strict";
  
    let getElements = function(element) {
      if (element.charAt(0) === '#') { // If passed an ID...
        return document.querySelector(element); // ... returns single element
      }
  
      return document.querySelectorAll(element); // Otherwise, returns a nodelist
    };
  
    // Variables
    let viewer = getElements('#viewer'), // Calculator screen where result is displayed
      equals = getElements('#equals'), // Equal button
      submitBtn = getElements('#submitMonto__btn'), // form submit monto button
      ans = getElements('#Ans'), // Equal button
      del = getElements('#backspace'),
      numbers = getElements('.num'), // List of numbers
      ops = getElements('.ops'), // List of operators
      theNum = '0', // Current number
      oldNum = '0', // First number
      montoInput = getElements('#montoCreate'), // the monto form input
      montoInputEdit = getElements('#montoEditarGasto'), // the monto form input
      // a new calculation is started when a new number is clicked right after clicking equal button
      calculationIsFinished = false,
      
      resultNum, // Result
      operator, // Batman
      operatorBtnWasPressed,

      calcOpenedFromCreateGastoModal = true;
    
    // default value of this button
    submitBtn.disabled = true;

    // When: Number is clicked. Get the current number selected
    let setNum = function() {
        if(!operatorBtnWasPressed){
            highligthSubmitBtn();
        }
        
        //theNum = viewer.innerText;
        let clickedDigit = this.getAttribute('data-num');
        
        if (clickedDigit === '.' && lastEntry() === '.') return;  
        
        if (calculationIsFinished) { 
            // reset display
            resultNum = ''; 
            
            // and show new number
            theNum = clickedDigit;
            viewer.innerText = theNum; 
            setSubmitValue(theNum);

            // starts new calculation
            calculationIsFinished = false; 
        } else { 
            // add clicked digit to previous number (this is a string!)
            if (theNum == '0') {
                theNum = '';
            }  
            theNum += clickedDigit;
            setSubmitValue(theNum);
            // if a math operator wasn't clicked yet, show current number (already updated);
            // otherwise: concatenate the display (which includes the operation symbol) with the new clicked digit.
            if (!operatorBtnWasPressed) { viewer.innerText = theNum; } 
            else                        { viewer.innerText += clickedDigit; }
        }
        //console.log(resultNum)
    };
    
    // function processInput(){
    //     console.log(theNum)
    //     let higherOrderOps = theNum.split(/\+|\-/);
    //     //higherOrderOps.addAll(higherOrderOps.split("-"));
    //     console.log(higherOrderOps)
    //     //return higherOrderOps;
    // }

    let backspace = function() {
        let content = viewer.innerText;
        let updatedContent = content.substring(0, content.length-1);
     
        if (updatedContent.length === 0) {
            clearAll();
        } else {
            viewer.innerText = updatedContent;
            theNum = viewer.innerText;
        }
    }

    // When: Operator is clicked. Pass number to oldNum and save operator
    let moveNum = function() {
        let mathOps = ['+','-','x','/'];
        // do nothing if a math operation is clicked 2 or more times in a row 
        if (!mathOps.includes(lastEntry())) { 
            highligthEqualsBtn();
            operatorBtnWasPressed = true;
            calculationIsFinished = false;
            
            oldNum = theNum;
            theNum = '';
            operator = this.getAttribute('data-ops');
            
            viewer.innerText = oldNum + operator;
            equals.setAttribute('data-result', ''); // Reset result in attr
        };
    };
    

    // function toSymbol(operator){
    //     switch (operator) {
    //         case "más": return "+";
    //         case "menos": return "-";
    //         case "por": return "*";
    //         case "dividido por": return "/";
    //     }
    // }
    
    let lastEntry = function() {
        return viewer.innerText.charAt(viewer.innerText.length-1);
    }

    let displayPreviousResult = function() {
        viewer.innerText += ans;
        theNum = ans;
    }

    // const switchHighligth = function switchHighligthBetweenEqualsAndSubmitButtons() {
    //     if ((resultNum != '0')) {// si es negativo enviar un mensaje (los gastos son positivos)
    //         equals.classList.toggle('highligth');
    //         submitBtn.classList.toggle('highligth');
    //         submitBtn.disabled = false;
    //     }
    // }
    
    const highligthSubmitBtn = function(){
        if ((resultNum != '0') && !submitBtn.classList.contains('highligth')){
            submitBtn.classList.add('highligth');
            submitBtn.disabled = false;
            equals.classList.remove('highligth');
        }
    }
    
    const disableSubmitBtn = function(){
        submitBtn.classList.remove('highligth');
        submitBtn.disabled = true;
    }

    const highligthEqualsBtn = function(){
        if (!equals.classList.contains('highligth')){
            equals.classList.add('highligth');
            disableSubmitBtn();
        }
    }

    // When: submit btn is clicked. Set result
    let setSubmitValue = function(number) {
        resultNum = number;
        // let input = getElements('#inputMonto');
        // input.setAttribute('value', resultNum);

        // alternative: pass value to another form:
        if (calcOpenedFromCreateGastoModal) {
            montoInput.blur();
            montoInput.value = resultNum;
            montoInput.innerHTML = resultNum;
            montoInput.classList.remove('invalid');
            montoInput.classList.add('valid');
        } else {
            montoInputEdit.blur();
            montoInputEdit.value = resultNum;
            montoInputEdit.innerHTML = resultNum;
            montoInputEdit.classList.remove('invalid');
            montoInputEdit.classList.add('valid');
        }

    }

    // When: Equals is clicked. Calculate result
    let displayNum = function() {
        calculationIsFinished = true; 
        operatorBtnWasPressed = false;

        // Convert string input to numbers
        oldNum = parseFloat(oldNum);
        theNum = parseFloat(theNum);
        
        // Perform operation
        switch (operator) {
            case '+': resultNum = oldNum + theNum; break;
            case '-': resultNum = oldNum - theNum; break;
            case 'x': resultNum = oldNum * theNum; break;
            case '/': resultNum = oldNum / theNum; break;
            // If equal is pressed without an operator, keep number and continue
            default: resultNum = theNum; 
        }

        let validResult = true;
        // If NaN or Infinity returned
        if (!isFinite(resultNum)) {
            validResult = false;
            if (isNaN(resultNum)) { 
                // resultNum = 'La rompiste! :(';
                resultNum = 'puff.. la rompiste! :(';
            } else { 
                // If result is infinity, set off by dividing by zero
                resultNum = 'Ohh no';
                setTimeout(function(){ 
                    getElements('#calculator').classList.add('broken'); // Break calculator
                    getElements('#reset').classList.add('show'); // And show reset button
                    getElements('#reset-msg').classList.add('show'); // And show reset button
                }, 200);
            }
        }
        
        // Display result, finally!
        if (validResult) {
            highligthSubmitBtn();
            //switchHighligth();
            let roundedResult = Math.round(resultNum*100000)/100000; // redondeo a 5 dígitos después de la coma
            viewer.innerText =  roundedResult;
            equals.setAttribute('data-result', roundedResult);
            setSubmitValue(roundedResult);
            ans = roundedResult;
            theNum = roundedResult;
        } else {
            viewer.innerText =  resultNum;
            equals.setAttribute('data-result', resultNum);
            theNum = resultNum;
        }

        // reset oldNum 
        oldNum = 0;
    };

    // When: Clear button is pressed. Clear everything
    let clearAll = function() {
        oldNum = '';
        theNum = '';
        viewer.innerText = '0';
        equals.setAttribute('data-result', resultNum);
        setSubmitValue('');
        disableSubmitBtn();
    };


    let openModalCreateSpending = function() {
        $('#modal-añadir-gasto').modal('show');
    }

    let openModalEditSpending = function() {
        $('#modal-editar-gasto').modal('show');
    }

    function switchFlag () {
        calcOpenedFromCreateGastoModal = false;
    }


    /* The click events */

    // Add click event to numbers
    for (let i = 0; i < numbers.length; i++) {
        numbers[i].onclick = setNum;
    }

    // Add click event to operators
    for (let i = 0; i < ops.length; i++) {
        ops[i].onclick = moveNum;
    }

    // Add click event to equal sign
    equals.onclick = displayNum;
    
    // Add click event to equal sign
    //submitBtn.onclick = setSubmitValue;

    function openModalGasto(){
        (calcOpenedFromCreateGastoModal) ? openModalCreateSpending() : openModalEditSpending();
    }
    
    submitBtn.addEventListener('click', openModalGasto);

    // Add click event to Ans button
    ans.onclick = displayPreviousResult;

    // Add click event to backspace button
    del.onclick = backspace; 
    
    // Add click event to close calculator button
    getElements('#close-calculator-btn').addEventListener('click', openModalGasto);

    //getElements('#close-calculator-btn').onclick = openModalCreateSpending;
//        getElements('#close-calculator-btn').onclick = openModalEditSpending;

    // Add click event to clear button
    getElements('#clear').onclick = clearAll;

    // Add click event to reset button
    getElements('#reset').onclick = function() {
        getElements('#reset').classList.remove('show'); // And show reset button
        getElements('#reset-msg').classList.remove('show'); // And show reset button
        getElements('#calculator').classList.remove('broken'); // Break calculator
        clearAll();
        // window.location = window.location;
    };

    $("#modal-calculator").on('shown.bs.modal', function(){
        oldNum = '';
        theNum = '';
        viewer.innerText = '0';
        disableSubmitBtn();
    });

//}());