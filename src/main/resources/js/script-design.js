window.addEventListener('DOMContentLoaded', function () {

    'esversion: 6';
    //Вызов функции для работы с таблицей(Выделение строки и т.д.)
    render();


    //Спрятанные элементы
    $('.tableEmp .column8').css("display", "none");
    $('.modalAddEmp .operType').css("display", "none");
    $('.modalEditEmp .operType').css("display", "none");
    $('.modalEditEmp .inputHidden').css("display", "none");


    //Табы
    var tab = document.querySelectorAll('.info-header-tab'),
        info = document.querySelector('.info-header'),
        tabContent = document.querySelectorAll('.info-tabcontent'),
        selectedEmpId;

    //Таблица
    var tableEmp = document.querySelector('.tableEmp'),
        tableAsset = document.querySelector('.tableAsset'),
        tableCert = document.querySelector('.tableCert'),
        tableRows = document.querySelectorAll('.tableRow');

    function hideTabContent(a) {
        for (let i = a; i < tabContent.length; i++) {
            tabContent[i].classList.remove('show');
            tabContent[i].classList.add('hide');
        }
    }

    hideTabContent(1);

    function showTabContent(b) {
        if (tabContent[b].classList.contains('hide')) {
            tabContent[b].classList.remove('hide');
            tabContent[b].classList.add('show');
        }
    }

    info.addEventListener('click', function (event) {
        let target = event.target;
        if (target && target.classList.contains('info-header-tab')) {
            for (let i = 0; i < tab.length; i++) {
                if (target == tab[i]) {
                    hideTabContent(0);
                    showTabContent(i);
                    break;
                }
            }
        }
    });

    // hover, highlight with color+
    //Парсинг строки по клику
    //Обработка добавления, редактирования сотрудника
    function render() {

        $('#excelEmpIcon, #sort, #addEmpIcon').on({
            mouseenter: function () {
                $(this).addClass('fancy');

                // var dcHeight = $(this).find('.dropdown-content').height();
                // console.log('dcHeight : ' + dcHeight);
                //
                // if ($('.dropdown-content').height() > 400) {
                //     $('.dropdown-content').height(900);
                //     $('.dropdown-content').css("overflow", "scroll");
                // } else {
                //     $('.dropdown-content').css("height", 120);
                //     $('.dropdown-content').css("overflow", "auto");
                // }
            },
            mouseleave: function () {
                $(this).removeClass('fancy');
            }
        });

        $('#excelEmpIcon, #sort').on({
            mouseenter: function () {
                var dcHeight = $(document).find('.dropdown-content').height();
                console.log('dcHeight : ' + dcHeight);
            },
            mouseleave: function () {

            }
        });


        $('#addEmpIcon').click(function () {
            $('#modalAddEmp').css("display", "block");
        });

        $('.tableEmp tr').on({

            mouseenter: function () {

                //Выделение строки фэнси
                $(this).addClass('fancy');

                //Парсинг строки таблицы по столбцам
                var surname = $(this).find('.column2').html();
                var name = $(this).find('.column3').html();
                var patronymic = $(this).find('.column4').html();
                var department = $(this).find('.column5').html();
                var room = $(this).find('.column6').html();
                var position = $(this).find('.column7').html();
                var hidden = $(this).find('.column8').html();
                console.log('hidden = ' + hidden);

                switch (department) {
                    case 'Не указан':
                        department = 'none';
                        break;
                    case 'Руководство':
                        department = 'rukovodstvo';
                        break;
                    case 'ККиПР':
                        department = 'kkipr';
                        break;
                    case 'ПиО':
                        department = 'pio';
                        break;
                    case 'ЗИО':
                        department = 'zio';
                        break;
                    case 'ЛК':
                        department = 'lk';
                        break;
                    case 'БУОН':
                        department = 'buon';
                        break;
                    case 'ЦДиППТ':
                        department = 'cdippt';
                        break;
                    case 'ПТО':
                        department = 'pto';
                        break;
                    case 'СО':
                        department = 'so';
                        break;
                    case 'ОДДиСАД':
                        department = 'oddisad';
                        break;
                    case 'СиТБАДиИС':
                        department = 'sitbadiis';
                        break;
                    case 'ЮО':
                        department = 'uo';
                        break;
                    case 'СОКР':
                        department = 'sokr';
                        break;
                    case 'САХР':
                        department = 'sahr';
                        break;
                    case 'КиС':
                        department = 'kis';
                        break;
                    case 'Водители-и-уборщицы':
                        department = 'vodi_i_ubor';
                        break;
                    default:
                        department = 'none';
                        break;
                }

                switch (position) {
                    case 'Не указан':
                        position = 'none';
                        break;
                    case 'Руководитель':
                        position = 'rukovoditel';
                        break;
                    case 'И.о. руководителя':
                        position = 'rukovoditel';
                        break;
                    case 'Зам. руководителя':
                        position = 'zam_rukovoditel';
                        break;
                    case 'Начальник':
                        position = 'nachalnik';
                        break;
                    case 'Зам. начальника':
                        position = 'zam_nachalnik';
                        break;
                    case 'Главный-специалист':
                        position = 'glavspec';
                        break;
                    case 'Ведущий-специалист':
                        position = 'vedspec';
                        break;
                    case 'Спец-1-кат.':
                        position = 'spec1cat';
                        break;
                    case 'Спец-2-кат.':
                        position = 'spec2cat';
                        break;
                    case 'Практикант':
                        position = 'praktikant';
                        break;
                    case 'Стажёр':
                        position = 'stazher';
                        break;
                    case 'Уволен(а)':
                        position = 'fired';
                        break;

                    default:
                        department = 'none';
                }

                if (!$(this).parent().hasClass('headerRow')) {  // Для оглавления таблицы не применять класс фэнси
                    $(this).find('.column7').append('<i data-title="Редактировать" id="pencil" style="padding-left:10px;"class="fa fa-pencil"></i>');
                    $(this).find('.column7').append('<i data-title="Инвентарь пользователя" id="desktop" style="padding-left:10px;"class="fa fa-desktop"></i>');
                }

                $(this).find('#pencil').click(function () {

                    console.log('clicked edit');
                    $('.modalEditEmp').css("display", "block");
                    selectedEmpId = hidden;
                    console.log('selected emp: ' + selectedEmpId);

                    $('.modalEditEmp .inputSurname').val(surname);
                    $('.modalEditEmp .inputName').val(name);
                    $('.modalEditEmp .inputPatronymic').val(patronymic);
                    $('.modalEditEmp .inputDepartment').val(department);
                    $('.modalEditEmp .inputRoom').val(room);
                    $('.modalEditEmp .inputPosition').val(position);
                    $('.modalEditEmp .inputHidden').val(hidden);
                });

                $(this).find('#desktop').click(function () {
                    location.href = '/menuAsset?id=' + hidden;
                });
            },
            mouseleave: function () {
                //Убираем выделение от ховера
                $(this).removeClass('fancy');
                //Убираем карандаш
                $(this).find('#pencil, #desktop').remove();
            }
        });
    }

    //Вывод количества строк в таблице
    var tableEmpRowNumber = tableEmp.rows.length - 1;
    console.log(tableEmpRowNumber);//kl


    //Вывод параметра в первой ячейке
    $('.tableEmp tr').click(function () {
        var val = $(this).find('.column1').html();
        console.log('# : ' + val);
    });


    //Удаление сотрудника с подтверждением
    $('.modalEditEmp .deleteEmpBtn').click(function () {
        if (confirm('Удалить пользователя?')) {
            location.href = '/deleteEmp?id=' + selectedEmpId;
        }
    });

    //Нажатие на кнопку добавления техники в модальном окне редактирования
    $('.modalEditEmp .addAssetBtn').click(function () {
        location.href = '/menuAsset?id=' + selectedEmpId;
        var id = selectedEmpId;
    })


    //Сортировка сотрудников по фамилии с помощью XHR
    function empSortScript(sort) {
        var param = sort;
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/sortEmp?sortBy=' + param, true);
        xhr.send();
        xhr.onreadystatechange = function () {
            if (xhr.readyState != 4) return;
            if (xhr.status != 200) {
                alert(xhr.status + ': ' + xhr.statusText);
            } else {
                var tableText = xhr.responseText;
                var tableC = document.getElementById('tableEmp');
                tableC.innerHTML = tableText;
                render();
            }
        }
    }

    function exportExcel() {
        console.log('Export');
    }

    //Обработка нажатия на кнопку сортировки
    $('.dropdown a').click(function (event) {

        var anchorValue = $(this).html();

        //По какой опции выполнено нажатие

        switch (anchorValue) {
            //Выбор сортировки
            case 'По справочнику':
                empSortScript('byPhone');
                break;
            case 'В алфавитном порядке':
                empSortScript('byEmp');
                break;
            case 'Руководство':
                empSortScript('rukovodstvo');
                break;
            case 'ККиПР':
                empSortScript('kkipr');
                break;
            case 'ПиО':
                empSortScript('pio');
                break;
            case 'ЗИО':
                empSortScript('zio');
                break;
            case 'ЛК':
                empSortScript('lk');
                break;
            case 'БУОН':
                empSortScript('buon');
                break;
            case 'ЦДиППТ':
                empSortScript('cdippt');
                break;
            case 'ПТО':
                empSortScript('pto');
                break;
            case 'СО':
                empSortScript('so');
                break;
            case 'ОДДиСАД':
                empSortScript('oddisad');
                break;
            case 'СиТБАДиИС':
                empSortScript('sitbadiis');
                break;
            case 'ЮО':
                empSortScript('uo');
                break;
            case 'СОКР':
                empSortScript('sokr');
                break;
            case 'САХР':
                empSortScript('sahr');
                break;
            case 'КиС':
                empSortScript('kis');
                break;
            case 'Водители и уборщицы':
                empSortScript('vodi_i_ubor');
                break;
            case 'Уволен(а)':
                empSortScript('fired');
                break;

            case 'Импорт':
                console.log('Import');
                location.href = '/importEmp';
                break;
            case 'Экспорт':
                exportExcel();
                location.href = '/exportEmp';
                break;
        }
    });


    var btn = $('#buttonScroll');

    $(window).scroll(function () {
        if ($(window).scrollTop() > 200) {
            btn.addClass('show');
        } else {
            btn.removeClass('show');
        }
    });

    btn.on('click', function (e) {
        e.preventDefault();
        $('html, body').animate({scrollTop: 0}, '300');
    });
});


