$(function () {
    $('[data-toggle="tooltip"]').hover(function () {
        $(this).tooltip('show')
    });
});
$(function () {
    $('[data-toggle="tooltip"]').click(function () {
        $(this).tooltip('hide')
    });
});