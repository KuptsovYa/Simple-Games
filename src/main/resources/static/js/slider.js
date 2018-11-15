/**
 * Intended to show images
 */
function theRotator() {
    // Устанавливаем прозрачность всех картинок в 0 
    $('div#rotator1 ul li').css({opacity: 0.0});
    
    // Берем первую картинку и показываем ее (по пути включаем полную видимость) 
    $('div#rotator1 ul li:first').css({opacity: 1.0});

    // Устанавливаем прозрачность всех картинок в 0
    $('div#rotator2 ul li').css({opacity: 0.0});

    // Берем первую картинку и показываем ее (по пути включаем полную видимость)
    $('div#rotator2 ul li:first').css({opacity: 1.0});

}

/**
 * Intended to rotate image in hangman game
 */
function rotate(num) {
    // Берем первую картинку 
    var current = ($("div#rotator" + num + " ul li.show")? $("div#rotator" + num + " ul li.show") : $("div#rotator" + num + " ul li:first"));
    
    // Берем следующую картинку, когда дойдем до последней начинаем с начала 
    var next = ((current.next().length) ? ((current.next().hasClass('show')) ? $("div#rotator" + num + " ul li:first") :current.next()) : $("div#rotator" + num + " ul li:first"));

    // Подключаем эффект растворения/затухания для показа картинок, css-класс show имеет больший z-index 
    next.css({opacity: 0.0}) 
        .addClass('show') 
        .animate({opacity: 1.0}, 1000); 
    
    // Прячем текущую картинку 
    current.animate({opacity: 0.0}, 1000) 
        .removeClass('show'); 
};