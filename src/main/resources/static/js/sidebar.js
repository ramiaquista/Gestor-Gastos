(function() {
    'use strict';
    
    // GLOBAL VARIABLES
    const header_ = document.getElementById('header'),
          body_ = document.getElementById('body'),
          sidebar = document.getElementById('sidebar'),
          sidebar_links = document.querySelectorAll('.nav__link'),
          backdrop = document.getElementById('backdropId'),
          appName = document.getElementById('app__name'),
          newEntityBtn = document.getElementById('newEntity'),
          newEntityBtnCondensed = document.getElementById('newEntity-condensed');
          ;
    
    // by default, side navbar is expanded on big screens
    if (window.innerWidth >= 768 ) {
        sidebar.classList.add('expanded');
    }

    /*===== show NAVBAR =====*/ 
    const showNavbar = (toggleId) => {
        const toggle = document.getElementById(toggleId);

        // Validate that all variables exist
        if (toggle && sidebar && body_ && header_) {
    
            toggle.addEventListener('click', () => {
                if (window.innerWidth >= 768){
                    if (newEntityBtn != null) {
                        newEntityBtn.classList.toggle('d-none')
                    }
                    
                    // swap sidebar formats
                    sidebar.classList.toggle('condensed');
                    sidebar.classList.toggle('expanded');
                    
                    // reduce body and header padding when sidebar is condensed 
                    body_.classList.toggle('body-left-displacement');
                    header_.classList.toggle('header-left-displacement');
                    
                    appName.classList.toggle('invisible');
                    
                    // when sidebar is condensed, show condensed new entity btn
                    if (newEntityBtnCondensed != null) {
                        newEntityBtnCondensed.classList.toggle('d-none')
                        setTimeout(() => {
                            newEntityBtnCondensed.classList.toggle('show')
                        }, 50);
                    }
                    
                } else {
                    // On mobile, the side navbar is completely hidden, so we show it!
                    sidebar.classList.toggle('show');
                    if (newEntityBtn != null) {
                        newEntityBtnCondensed.classList.add('d-none')
                        newEntityBtn.classList.remove('d-none')
                    }
                    // block screen when the sidebar is open on small screens
                    toggleBackdrop();
                }
            });
        }
    }
    
    showNavbar('header-toggle');
    
    /*===== LINK ACTIVE =====*/ 
    const linkColor = document.querySelectorAll('.nav__link')
    
    function colorLink() {
        if (linkColor) {
            linkColor.forEach(link => link.classList.remove('active'));
            this.classList.add('active');
        }
    }
    
    linkColor.forEach(link => link.addEventListener('click', colorLink));
    
    // show sidebar links on hover when sidebar is condensed (removing sidebar overflow)
    $('.nav__list').hover(
        function() {
            if (sidebar.classList.contains('condensed')){
                sidebar.classList.toggle('overflow-y-auto');
            }
        }
      );

    // block screen when the sidebar is open on small screens
    const toggleBackdrop = function(){
        if (sidebar.classList.contains('show')) {
            setTimeout(() => {
                body_.classList.add('blockScroll');
                backdrop.classList.add('visible');
            }, 50);
        } else {
            removeBackdrop();
        }
    }
    
    const removeBackdrop = function(){
        body_.classList.remove('blockScroll');
        backdrop.classList.remove('visible');
    }

    // remove blocking backdrop when a link in the sidebar is clicked
    for (const link of sidebar_links) {
        link.onclick = removeBackdrop;
    }

    /* === Manage window resizing === */
    const revertChanges = function revertToDefaultSettings(){
        body_.classList.remove('blockScroll')
        backdrop.classList.remove('visible');
        
        if (window.innerWidth >= 768) {
            if (!sidebar.classList.contains('condensed')) {
                sidebar.classList.add('expanded');
            }
        } else {
            body_.classList.remove('body-left-displacement');
            header_.classList.remove('header-left-displacement');
            sidebar.classList.remove('expanded', 'condensed', 'show');
            appName.classList.remove('invisible');
        }
    }

    window.onresize = revertChanges;  


}());