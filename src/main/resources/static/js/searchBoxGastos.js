
      // dinamically filter entity cards, on typing in the search box!
      $('#searchBox').keyup(function () {
        // change search icon cursor style whether there is text typed or not
        ($(this).val() == "") ? $('#searchIcon').css('cursor','pointer') : $('#searchIcon').css('cursor','default');
        
        // compare texts and hide cards if input doesn't match categories
        let inputText = this.value.toLowerCase();
        
        $('.divGrupo').each(function () {
          let grupo = $(this);
          let gastos = grupo.find('.divGasto');
          let titulo = $(this).find('.group__title');
          let tituloText = titulo.text().toLowerCase();
          
          gastos.each(function(){
            let gasto = $(this);
            let gastoText  = gasto.text().toLowerCase();
            (!gastoText.includes(inputText)) ? gasto.addClass('d-none') : gasto.removeClass('d-none');
          })
          
          let counter = 0;
          gastos.each(function() {
            if ($(this).hasClass('d-none')){
              counter++;
            }
          });

          (counter == gastos.length) ?titulo.addClass('d-none') : titulo.removeClass('d-none');
          
          if (tituloText.includes(inputText)){
            titulo.removeClass('d-none');
            gastos.each(function(){
              $(this).removeClass('d-none');
            });
          } 
          
          // show 'no result found' msg
          let entidadesOcultas = 0;

          $('.entidad__container .card').each(function (){
            if ($(this).is(":hidden")) { entidadesOcultas++; }
          });
          
          let numEntidades = $('.entidad__container .card').length;
          (entidadesOcultas == numEntidades) ? $('#searchMsg').removeClass('d-none') : $('#searchMsg').addClass('d-none');

        });
      });