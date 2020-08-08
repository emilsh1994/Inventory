window.addEventListener('DOMContentLoaded', function () {

    'esversion: 6';
    render();

    var id = 0;
    function render() {

        $('#tableHistory .column8').css("display", "none");
        if ($('.tableHistory tr .column5').html() == ''){
            $(this).find('.column5').val('-');
        }

        $('.tableHistory tr').on({
            mouseenter: function () {

                $(this).addClass('fancy');

                id = $(this).find('.column7').html();
                var name = $(this).find('.column2').html();
                var invNumb = $(this).find('.column3').html();
                var serNumb = $(this).find('.column4').html();
                var owner = $(this).find('.column5').html();
                var date = $(this).find('.column6').html();
                var ownerId = $(this).find('.column8').html();


                //Добавления ярлычков fonts awesome
                if (!$(this).parent().hasClass('headerRow')){  // Для оглавления таблицы не применять класс фэнси
                    if (ownerId != '0') {
                        $(this).find('.column7').append('<i data-title="Текущий пользователь" style="padding-left:10px;" id="user" class="fa fa-user"></i>');
                    }
                    $(this).find('.column7').append('<i data-title="Удалить" style="padding-left:10px;" id="trash" class="fa fa-trash"></i>');
                }

                $(this).find('#user').click(function () {
                    location.href = '/menuAsset?id=' + ownerId;
                });

                $(this).find('#trash').click(function () {
                    console.log('trash');
                    deleteHistoryRecord();
                    // location.href = '/main';
                });


            },
            mouseleave: function () {
                $(this).find('#trash, #user').remove();
                $(this).removeClass('fancy');
            }
        });

        $('.closeButton').click(function () {
            console.log('x');
            location.href = "/main";
        });

        function deleteHistoryRecord() {
            if (confirm('Удалить запись?')) {
                location.href = '/deleteHistoryRecord?id=' + id;
                console.log('success');
            }
        }


    }


});