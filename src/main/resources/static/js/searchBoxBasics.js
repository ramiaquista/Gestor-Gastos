        // expand/hide search box
        let searchIcon = document.getElementById('searchIcon');
        let searchBox = document.getElementById('searchBox');
        
        function showInput(){
          if (searchBox.value === ''){
            searchBox.classList.toggle('expand');
          } 
        }
        
        function hideSearchBox(){
          (searchBox.value === '') ? searchBox.classList.remove('expand') : searchBox.focus();
        }
        // $('#searchBox').focusout( () => {
        //   (searchBox.value === '') ? searchBox.classList.remove('expand') : searchBox.focus();
        // });
    
        searchIcon.onclick = showInput;
    
        // if the page is reloaded, reset search input field
        if (performance.getEntriesByType("navigation")[0].type == "reload") {
          searchBox.value = ''
        } 