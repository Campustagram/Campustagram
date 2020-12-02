
 //customjs ve treejs tek bir dosyada birleştirildi başlangıc ve bitis noktalari acıklamalar ile belirtildi


 //custom js start


$("li.dropdown ul").click(function(e) {
	e.stopPropagation();
})

// ===== Scroll to Top ==== 
$(window).scroll(function() {
    if ($(this).scrollTop() >= 50) {        // If page is scrolled more than 50px
        $('#return-to-top').fadeIn(200);    // Fade in the arrow
    } else {
        $('#return-to-top').fadeOut(200);   // Else fade out the arrow
    }
});
$('#return-to-top').click(function() {      // When arrow is clicked
    $('body,html').animate({
        scrollTop : 0                       // Scroll to top of body
    }, 500);
});

function refresh() {

    setTimeout(function () {
        location.reload()
    }, 0);
}


/**
 * Listener to trigger modal close, when clicked on dialog mask.
 */
$(document).ready(function(){
$("body").on("click",'.ui-dialog-mask',function () {
    idModal = this.id;
    idModal = idModal.replace("_modal","");
    getWidgetVarById(idModal).hide();
})
});

/**
 * Returns the PrimefacesWidget from ID
 * @param id
 * @returns {*}
 */
function getWidgetVarById(id) {
    for (var propertyName in PrimeFaces.widgets) {
        var widget = PrimeFaces.widgets[propertyName];
        if (widget && widget.id === id) {
            return widget;
        }
    }
}
//custom js end


//tree js start

var currentEvent;

$(document)
    .ready(
        function() {
            PrimeFaces.widget.ContextMenu.prototype.show = function(e) {
                // hide other contextmenus if any
                $(document.body).children('.ui-contextmenu:visible')
                    .hide();

                if (e) {
                    currentEvent = e;
                }

                var win = $(window),
                    left = e.pageX,
                    top = e.pageY,
                    width = this.jq
                    .outerWidth(),
                    height = this.jq.outerHeight();

                // collision detection for window boundaries
                if ((left + width) > (win.width()) + win.scrollLeft()) {
                    left = left - width;
                }
                if ((top + height) > (win.height() + win.scrollTop())) {
                    top = top - height;
                }

                if (this.cfg.beforeShow) {
                    this.cfg.beforeShow.call(this);
                }

                this.jq.css({
                    'left': left,
                    'top': top,
                    'z-index': ++PrimeFaces.zindex
                }).show();

                e.preventDefault();
            };
        });

//tree js end