
      // dinamically filter entity cards, on typing in the search box!
      $('#searchBox').keyup(function () {
        // change search icon cursor style whether there is text typed or not
        ($(this).val() == "") ? $('#searchIcon').css('cursor','pointer') : $('#searchIcon').css('cursor','default');
        
        // compare texts and hide cards if input doesn't match categories
        let inputText = this.value.toLowerCase();
        
        $('.entidad__container .card').each(function () {
          let card = $(this);
          let cardText  = card.text().toLowerCase();
          (cardText.includes(inputText)) ? card.show() : card.hide();
          
          if (card.hasClass('entidadGroup')){
            let grupo = $(this);
            let entidades = grupo.find('.entity');
            let entidadesText  = entidades.text().toLowerCase();

            if ((entidadesText).includes(inputText)) {
              entidades.each(function(){
                let entidad = $(this);
                let entidadText  = entidad.text().toLowerCase();
                (!entidadText.includes(inputText)) ? entidad.addClass('d-none') : entidad.removeClass('d-none');
              });
            }
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