window.addEventListener('DOMContentLoaded', function () {

    'esversion: 6';
    //Вызов функции для работы с таблицей(Выделение строки и т.д.)
    render();
    var selectedAsset, idEmp;

    //Спрятанные элементы модального окна редактирования техники
    $('.modalEditAsset .operType').css("display", "none");
    $('.modalEditAsset .inputHiddenAsset').css("display", "none");
    $('.modalEditAsset .inputHiddenEmp').css("display", "none");

    //Спрятанные элементы модального окна добавления техники
    $('.modalAddAsset .operType').css("display", "none");
    $('.modalAddAsset .inputHiddenEmp').css("display", "none");

    idEmp = $('.tableEmpAsset tr').eq(1).find('.column1').html();
    console.log(idEmp);



    function render() {

        $('.tableAsset').find('.column6, .column7, .column9').css("display", "none");

        $('#excelOutput').on({
            mouseenter: function () {
                $(this).addClass('fancy');

                $(this).click(function () {
                    console.log('excel output');
                    location.href = '/exportEmpAssets?id=' + idEmp;
                });
            },
            mouseleave: function () {
                $(this).removeClass('fancy');
            }
        });


        $('.tableEmpAsset tr').on({
            mouseenter: function () {

                console.log('id=' + idEmp);

                if (!$(this).parent().hasClass('headerRow')) {  // Для оглавления таблицы не применять класс фэнси
                    $(this).find('.column7').append('<i data-title="Добавить технику" style="padding-left:15px;" id="plus" class="fa fa-plus"></i>');
                    $(this).addClass('fancy');
                }

                $('#plus').click(function () {
                    $('.modalAddAsset').css("display", "block");
                    $('.modalAddAsset .inputHiddenEmp').val(idEmp);
                });
            },
            mouseleave: function () {
                $(this).find('#plus').remove();
                $(this).removeClass('fancy');
            }
        });

        //Работа с таблицей техники
        $('.tableAsset tr').on({

            //При наведении курсора на таблицу с техникой
            mouseenter: function () {

                //Выделение строки фэнси
                $(this).addClass('fancy');

                //Парсинг строки таблицы по столбцам
                var name = $(this).find('.column2').html();
                var invNumb = $(this).find('.column3').html();
                var serNumb = $(this).find('.column4').html();
                var assetType = $(this).find('.column5').html();
                selectedAsset = $(this).find('.column8').html();
                var assetDescr = $(this).find('.column9').html();

                //Добавления ярлычков fonts awesome
                if (!$(this).parent().hasClass('headerRow')) {  // Для оглавления таблицы не применять класс фэнси
                    $(this).find('.column4').append('<i data-title="Добавить технику" style="padding-left:10px;" id="plus-sign" class="fa fa-plus"></i>');
                    $(this).find('.column4').append('<i data-title="Редактировать" style="padding-left:10px;" id="pencil" class="fa fa-pencil"></i>');
                    $(this).find('.column4').append('<i data-title="История" style="padding-left:10px;" id="history" class="fa fa-book"></i>');
                    $(this).find('.column4').append('<i data-title="Описание" style="padding-left:10px;" id="description" class="fa fa-info-circle"></i>');
                    $(this).find('.column4').append('<i data-title="Удалить" style="padding-left:10px;" id="trashcan" class="fa fa-trash"></i>');
                }

                //Обработка нажатия на карандаш
                $(this).find('#pencil').click(function () {
                    var assetTypeNew;
                    switch (assetType) {
                        case "ОС":
                            assetTypeNew = "os";
                            break;
                        case "ТМЦ":
                            assetTypeNew = "tmc";
                            break;
                        case "Не задан":
                            assetTypeNew = "no-type";
                            break;
                    }

                    console.log('clicked edit');
                    $('.modalEditAsset').css("display", "block");
                    $('.modalEditAsset .inputName').val(name);
                    $('.modalEditAsset .inputInvNumb').val(invNumb);
                    $('.modalEditAsset .inputSerNumb').val(serNumb);
                    $('.modalEditAsset .selectAssetType').val(assetTypeNew);
                    $('.modalEditAsset .inputAssetDescr').val(assetDescr);
                    $('.modalEditAsset .inputHiddenAsset').val(selectedAsset);
                    $('.modalEditAsset .inputHiddenEmp').val(idEmp);
                    $('.modalEditAsset .selectOwner').val(idEmp);
                });

                //Обработка нажатия на корзину
                $(this).find('#trashcan').click(function () {
                    deleteAsset();
                });

                //Обработка нажатия на значок плюса
                $(this).find('#plus-sign').click(function () {
                    $('.modalAddAsset').css("display", "block");
                    $('.modalAddAsset .inputHiddenAsset').val(selectedAsset);
                    $('.modalAddAsset .inputHiddenEmp').val(idEmp);
                });

                $(this).find('#history').click(function () {
                    console.log('calendar');
                    location.href = '/viewAssetHistory?assetId=' + selectedAsset;
                });

                $(this).find('#description').click(function () {
                    $('.modalAssetDescrIcon').css("display", "block");
                    $('.inputAssetDescrIcon').val(assetDescr);
                    console.log('description');
                });
            },
            //При убирании курсора
            mouseleave: function () {
                //Убираем выделение от ховера
                $(this).removeClass('fancy');
                //Убираем карандаш
                $('#trashcan, #pencil, #history, #plus-sign, #description').remove();
            }
        });


        //Удаление техники при редактировании
        $('.modalEditAsset .deleteEmpBtn').click(function () {
            deleteAsset();
        });

        //Функция удаления техники
        function deleteAsset() {
            if (confirm('Удалить технику?')) {
                location.href = '/deleteAsset?idAsset=' + selectedAsset + '&idEmp=' + idEmp;
            }
        }
    }

    $('.closeButton').click(function () {
        console.log('x');
        location.href = "/main";
    });


});