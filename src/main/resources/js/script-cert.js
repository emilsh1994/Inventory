window.addEventListener('DOMContentLoaded', function () {

    'esversion: 6';
    render();
    var selectedCert;

    function render() {

        $('#tableCert tr').each(function() {

            var col = $(this).find('.column6');

            var dateNow = moment().format('YYYY-MM-DD');
            var dateNow2 = moment(dateNow);

            var dateEnd = moment(new Date($(col).html())).format('YYYY-MM-DD');
            var dateEnd2 = moment(dateEnd);

            var differ = dateNow2.diff(dateEnd2, "days");

            // console.log('Now: ' + dateNow);
            // console.log('End: ' + dateEnd);
            // console.log('Diff: ' + differ);


            var daysMessage = '';
            var verdict = 'Закончится через ' + Math.abs(differ) + ' дня(дней)';

            //Закончился
            if (differ > 0) {
                $(this).css("background-color", "red");
                $(this).css("color", "black");
                verdict = 'Закончился';
            }

            //Осталось меньше месяца
            if (differ > -30 && differ < 0) {
                $(this).css("background-color", "orange");
                $(this).css("color", "black");

            }

            //От трех месяцев до одного
            if (differ > -90 && differ < -31) {
                $(this).css("background-color", "#F7D358");
                $(this).css("color", "black");
            }

            if (!$(this).parent().hasClass('headerRow')){
                $(this).find('.column8').html(verdict);
            }

        });


        //Скрываем ненужные поля
        $('.modalAddCert').css("display", "none");
        $('.modalAddCert .operType').css("display", "none");
        $('.tableCert').find('.column7').css("display", "none");


        //Эффект при ховере
        $('#addCertIcon, #excelCert, #sortCert').on({
            mouseenter: function () {
                $(this).addClass('fancy');
            },
            mouseleave: function () {
                $(this).removeClass('fancy');
            }
        });


        //Появления модального окна редактирования техники
        $('#addCertIcon').click(function () {
            $('#modalAddCert').css("display", "block");
        });

        $('#excelCert').click(function () {
            console.log('clicked excel');
            location.href = "/exportCert";
        });

        $('.tableCert tr').on({

            mouseenter: function () {

                selectedCert = $(this).find('.column7').html();
                console.log('id=' + selectedCert);

                if (!$(this).parent().hasClass('headerRow')) {  // Для оглавления таблицы не применять класс фэнси
                    $(this).find('.column6').append('<i data-title="Удалить" style="padding-left:15px;" id="trashcan" class="fa fa-trash"></i>');
                    // $(this).find('.column6').append('<i data-title="Редактировать" style="padding-left:15px;" id="pencil" class="fa fa-pencil"></i>');
                    $(this).addClass('fancy');
                }

                $('#trashcan').click(function () {
                    deleteCert();
                    console.log('delete clicked');
                });
            },
            mouseleave: function () {
                $(this).find('#trashcan, #pencil').remove();
                $(this).removeClass('fancy');
            }
        });

        //Функция удаления сертификата
        function deleteCert() {
            if (confirm('Удалить сертификат?')) {
                location.href = '/deleteCert?id=' + selectedCert;
                console.log('success');
            }
        }

        $('.dropdown-content a').click(function (event) {

            var aValue = $(this).html();

            switch (aValue) {
                case 'Убрать сортировку':
                    certsort('none');
                    break;
                case 'Сначала новые':
                    certSort('byDateUp');
                    break;
                case 'Сначала старые':
                    certSort('byDateDown');
                    break;
                case 'По алфавиту':
                    certSort('byAlphabet');
                    break;
                // default:
                //     certSort('none');
            }
        });

        //Сортировка сотрудников по фамилии с помощью XHR
        function certSort(sort) {
            var param = sort;
            var xhr = new XMLHttpRequest();
            xhr.open('GET', '/sortCert?sortBy=' + param, true);
            xhr.send();
            xhr.onreadystatechange = function () {
                if (xhr.readyState != 4) return;
                if (xhr.status != 200) {
                    alert(xhr.status + ': ' + xhr.statusText);
                } else {
                    var tableText = xhr.responseText;
                    var tableC = document.getElementById('tableCert');
                    tableC.innerHTML = tableText;
                    render();
                }
            }
        }
    }
});