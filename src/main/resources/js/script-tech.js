window.addEventListener('DOMContentLoaded', function () {

    'esversion: 6';
    render();

    function render() {

        var selectedAsset;

        $('.tableAssets').find('.column7, .column8, .column9').css("display", "none");
        $('#modalAddAssetTab').css("display", "none");
        $('.modalAddAssetTab, .modalEditAssetTab, .operType, .inputHiddenEmp .inputHiddenAsset').css("display", "none");
        $('.modalEditAssetTab, .inputHiddenEmpAssetTab, .inputHiddenAssetTab').css("display", "none");

        $('#excelAssetsMenu, #excelAssetsSort, #addAssetIcon').on({
            mouseenter: function () {
                $(this).addClass('fancy');
            },
            mouseleave: function () {
                $(this).removeClass('fancy');
            }
        });

        $('#addAssetIcon').click(function () {
            console.log('click add');
            $('.modalAddAssetTab').css("display", "block");
        });

        $('.tableAssets tr').on({

            mouseenter: function () {

                //Парсим строку таблицы  при ховере
                selectedAsset = $(this).find('.column8').html();
                var name = $(this).find('.column2').html();
                var invNumb = $(this).find('.column3').html();
                var serNumb = $(this).find('.column4').html();
                var assetType = $(this).find('.column5').html();
                var ownerId = $(this).find('.column7').html();
                var description = $(this).find('.column9').html();


                console.log('owner ' + ownerId);
                console.log('asset ' + selectedAsset);
                console.log('type ' + assetType);

                // Для оглавления таблицы не применять класс фэнси
                if (!$(this).parent().hasClass('headerRow')) {

                    if (ownerId != '0') {
                        $(this).find('.column5').append('<i data-title="Пользователь" style="padding-left:10px;" id="user" class="fa fa-user"></i>');
                    }

                    console.log('html= ' + $(this).find('.column5').html());
                    $(this).find('.column5').append('<i data-title="История" style="padding-left:10px;" id="history" class="fa fa-book"></i>');
                    $(this).find('.column5').append('<i data-title="Описание" style="padding-left:10px;" id="description" class="fa fa-info-circle"></i>');
                    $(this).find('.column5').append('<i data-title="Редактировать" style="padding-left:10px;" id="pencil" class="fa fa-pencil"></i>');
                    $(this).addClass('fancy');
                }

                $(this).find('#user').click(function () {
                    location.href = '/menuAsset?id=' + ownerId;
                });

                $(this).find('#history').click(function () {
                    location.href = '/viewAssetHistory?assetId=' + selectedAsset;
                });

                $(this).find('#description').click(function () {
                    $('.modalAssetDescrIcon').css("display", "block");
                    $('.inputAssetDescrIcon').val(description);
                    console.log('description');
                });

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

                    console.log('pencil');
                    $('.modalEditAssetTab').css("display", "block");
                    $('.modalEditAssetTab .inputName').val(name);
                    $('.modalEditAssetTab .inputInvNumb').val(invNumb);
                    $('.modalEditAssetTab .inputSerNumb').val(serNumb);
                    $('.modalEditAssetTab .selectAssetType').val(assetTypeNew);
                    $('.modalEditAssetTab .selectOwner').val(ownerId);
                    $('.modalEditAssetTab .inputHiddenAssetTab').val(selectedAsset);
                    $('.modalEditAssetTab .inputHiddenEmpAssetTab').val(ownerId);
                    $('.modalEditAssetTab .inputAssetDescr').val(description);
                });

            },
            mouseleave: function () {
                $(this).find('#user, #history, #description, #pencil').remove();
                $(this).removeClass('fancy');
            }
        });

        $('.modalEditAssetTab .deleteAssetBtn').click(function() {
           deleteAssetFromEdit();
        });

        function deleteAssetFromEdit() {
            if (confirm('Удалить технику?')) {
                location.href = '/deleteAsset?id=' + selectedAsset;
                console.log('success');
            }
        }
    }

    //Сортировка сотрудников по фамилии с помощью XHR
    function assetSortScript(sort) {
        var param = sort;
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/assetSort?sortBy=' + param, true);
        xhr.send();
        xhr.onreadystatechange = function () {
            if (xhr.readyState != 4) return;
            if (xhr.status != 200) {
                alert(xhr.status + ': ' + xhr.statusText);
            } else {
                var tableText = xhr.responseText;
                var tableC = document.getElementById('tableAssets');
                tableC.outerHTML = tableText;
                render();
            }
        }
    };

    //Обработка нажатия на кнопку сортировки
    $('.dropdown a').click(function (event) {

        var anchorValue = $(this).html();

        //По какой опции выполнено нажатие
        switch (anchorValue) {

            case 'Вся техника':
                assetSortScript('none');
                break;

            case 'С владельцами':
                assetSortScript('withEmp');
                break;

            case 'Без владельца':
                assetSortScript('withNoEmp');
                break;

            case 'ОС':
                assetSortScript('os');
                break;

            case 'ТМЦ':
                assetSortScript('tmc');
                break;

            case 'Без типа':
                assetSortScript('no-type');
                break;
            case 'Без инв. номера':
                assetSortScript('noInvNumb');
                break;
            case 'Без сер. номера':
                assetSortScript('noSerNumb');
                break;

            case 'Импорт техники':
                console.log('import');
                location.href = '/assetImport';
                break;

            case 'Экспорт техники':
                console.log('export');
                location.href = '/assetExport';
                break;

        }
    });

});