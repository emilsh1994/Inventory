package com.inventar.servlets;

import com.inventar.DBUtils;
import com.inventar.ExcelUtils;
import com.inventar.domain.Asset;
import com.inventar.domain.Certificate;
import com.inventar.domain.Employee;
import com.inventar.enums.Department;
import com.inventar.enums.Jobposition;
import com.inventar.servlets.employees.EmpSortServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


//Основной сервлет
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("UTF-8");


        EmpSortServlet ess = new EmpSortServlet();
        List<Employee> emps = ess.getEmpsByPhone();

        DBUtils.getInstance().setEmpListSorted(emps);
        DBUtils.getInstance().setAssetsListSorted(DBUtils.getInstance().getAssetsList());
        DBUtils.getInstance().setCertListSorted(DBUtils.getInstance().getCertificateList());

        response.getOutputStream().write(getPage(getTemplate()));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long empId = 0;
        String operType = req.getParameter("operType");

        String empSurname = req.getParameter("empSurname");
        String empName = req.getParameter("empName");
        String empPatronymic = req.getParameter("empPatronymic");
        String empDepartment = req.getParameter("empDepartment");
        String empRoom = req.getParameter("empRoom");
        String empPosition = req.getParameter("empPosition");

        String assetName = req.getParameter("assetName");
        String assetInvNumb = req.getParameter("assetInvNumb");
        String assetSerNumb = req.getParameter("assetSerNumb");
        String assetType = req.getParameter("assetType");
        String assetDescr = req.getParameter("assetDescr");


        switch (operType) {

            case "addEmployee": {
                DBUtils.getInstance().createEmployee(empSurname, empName, empPatronymic, Department.valueOf(empDepartment), empRoom, Jobposition.valueOf(empPosition));
                break;
            }

            case "editEmployee": {
                String id = req.getParameter("id");
                empId = Long.parseLong(id);
                DBUtils.getInstance().editEmployee(empId, empSurname, empName, empPatronymic, Department.valueOf(empDepartment), empRoom, Jobposition.valueOf(empPosition));
                break;
            }

            case "addCert": {
                String certOwnerStr = req.getParameter("certOwner");
                long certOwner = Long.parseLong(certOwnerStr);
                String certType = req.getParameter("certType");
                String certPublisher = req.getParameter("certPublisher");
                String date1 = req.getParameter("calendar1");
                String date2 = req.getParameter("calendar2");
                System.out.println(date1 + "  " + date2);
                try {
                    DBUtils.getInstance().createCertificateRecord(certOwner, certType, certPublisher, date1, date2);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }

            case "addAssetTab":

                //Парсим URL формы
                String ownerAddAsset = req.getParameter("ownerId");
                String ownerInfo = "";
                if (ownerAddAsset.equals("empNullAdd")) {
                    empId = 0;
                    ownerInfo = "Не задан";
                } else {
                    empId = Long.parseLong(ownerAddAsset);
                    ownerInfo = DBUtils.getInstance().getEmployeeById(empId).getSurname() + " " + DBUtils.getInstance().getEmployeeById(empId).getName() + " " + DBUtils.getInstance().getEmployeeById(empId).getPatronymic() + " - " + DBUtils.getInstance().getEmployeeById(empId).getDepartment();
                }

                DBUtils.getInstance().createAsset(assetName, assetInvNumb, assetSerNumb, assetType, assetDescr, empId);
                DBUtils.getInstance().createHistoryRecord(DBUtils.getInstance().getLastAssetId(), empId, ownerInfo);
                break;

            case "editAssetTab":
                String newOwnerId = req.getParameter("newOwnerId");
                long newOwner = Long.parseLong(newOwnerId);

                if (newOwner != empId) {
                    empId = newOwner;
                }

                ownerInfo = DBUtils.getInstance().getEmployeeById(empId).getSurname() + " " + DBUtils.getInstance().getEmployeeById(empId).getName() + " " + DBUtils.getInstance().getEmployeeById(empId).getPatronymic() + " - " + DBUtils.getInstance().getEmployeeById(empId).getDepartment();
                String idA = req.getParameter("assetId");
                long assetId = Long.parseLong(idA);
                DBUtils.getInstance().editAsset(assetId, assetName, assetInvNumb, assetSerNumb, assetType, assetDescr, empId);
                DBUtils.getInstance().createHistoryRecord(DBUtils.getInstance().getLastAssetId(), empId, ownerInfo);
                break;
        }

        resp.sendRedirect("/main");

    }

    //Преобразование текста в массив байт
    private byte[] getPage(String text) {
        try {
            return text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    //DESIGN-INV
    private String getTemplate() {
        int i = 0;
        String html = "<html>" +
                "<head>" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "    <link rel=\"stylesheet\" href=\"css/tabs.css\">" +
                "    <link rel=\"stylesheet\" href=\"css/table.css\">" +
                "    <link rel=\"stylesheet\" href=\"css/modal.css\">" +
                "    <link rel=\"stylesheet\" href=\"css/font-awesome.min.css\">" +
                "    <link rel=\"stylesheet\" href=\"css/dropdown.css\">" +
                "    <link rel=\"icon\" href=\"img/logo.png\">" +
                "    <title>Inventory</title>" +
                "</head>" +
                "<body>" +
                "    <div class=\"content\">" +
                "      <a id=\"buttonScroll\"></a>" +
                "        <div class=\"info\">" +
                "            <div class=\"info-header\">" +
                "                <div class=\"info-header-tab\">Сотрудники</div>" +
                "                <div class=\"info-header-tab\">Техника</div>" +
                "                <div class=\"info-header-tab\">Сертификаты</div>" +
                "            </div>" +


                ////////////////////////////////////////////////////////////
                ////       Модальное окно Добавления сотрудника         ////
                ////////////////////////////////////////////////////////////

                " <div id=\"modalAddEmp\" class=\"modalAddEmp\">\n" +
                "                <form class=\"modal-content animate\" method=\"POST\" action=\"/main\">\n" +
                "                    <div class=\"imgcontainer\">\n" +
                "                        <span onclick=\"document.getElementById('modalAddEmp').style.display='none'\" class=\"close\"\n" +
                "                            title=\"Закрыть окно\">×</span>\n" +
                "                    </div>\n" +

                "                    <div class=\"containerModal\">\n" +
                "                        <h3>Добавление сотрудника</h3>\n" +
                "                        <input class=\"operType\" type=\"text\" placeholder=\"type\" name=\"operType\" value=\"addEmployee\" style=\"display:block;\">" +
                "                        <label for=\"surname\"><b>Фамилия</b></label>\n" +
                "                        <input type=\"text\" placeholder=\"Фамилия\" name=\"empSurname\" required>\n" +

                "                        <label for=\"name\"><b>Имя</b></label>\n" +
                "                        <input type=\"text\" placeholder=\"Имя\" name=\"empName\" required>\n" +

                "                        <label for=\"patronymic\"><b>Отчество</b></label>\n" +
                "                        <input type=\"text\" placeholder=\"Отчество\" name=\"empPatronymic\" required>\n" +

                "                        <label for=\"department\"><b>Отдел</b></label>\n" +
                "                        <select class=\"selectDepartment\" name=\"empDepartment\">";

        for (Department dep : Department.values()) {
            html += "<option value=" + dep.name() + ">" + dep + "</option>";
        }

        html += "</select>" +
                "                        <label for=\"room\"><b>Помещение</b></label>\n" +
                "                        <input type=\"text\" placeholder=\"Помещение\" name=\"empRoom\" required>\n" +

                "                        <label for=\"empPosition\"><b>Должность</b></label>\n" +
                "                        <select class=\"selectPosition\" name=\"empPosition\" >" +
                "                        <label for=\"position\"><b>Должность</b></label>\n";

        for (Jobposition pos : Jobposition.values()) {
            html += "<option value=" + pos.name() + ">" + pos + "</option>";
        }

        html += "</select>" +
                "                        <button type=\"submit\">Добавить сотрудника</button>\n" +
                "                    </div>\n" +
                "                    <div class=\"containerModal\" style=\"background-color:#f1f1f1\">\n" +
                "                        <button type=\"button\" onclick=\"document.getElementById('modalAddEmp').style.display='none'\"\n" +
                "                            class=\"cancelBtn\">Отмена</button>\n" +
                "                    </div>\n" +
                "                </form>\n" +
                "            </div>\n" +


                ////////////////////////////////////////////////////////////
                ////       Модальное окно редактирования сотрудника     ////
                ////////////////////////////////////////////////////////////
                "            <div id=\"modalEditEmp\" class=\"modalEditEmp\">\n" +
                "                <form class=\"modal-content animate\" method=\"POST\" action=\"/main\">\n" +
                "                    <div class=\"imgcontainer\">\n" +
                "                        <span onclick=\"document.getElementById('modalEditEmp').style.display='none'\" class=\"close\"\n" +
                "                            title=\"Закрыть окно\">×</span>\n" +
                "                    </div>\n" +
                "\n" +
                "                    <div class=\"containerModal\">\n" +
                "                        <h3>Редактирование сотрудника</h3>\n" +
                "                        <input class=\"operType\" type=\"text\" placeholder=\"type\" name=\"operType\" value=\"editEmployee\" style=\"display:block;\">" +
                "                        <label for=\"surname\"><b>Фамилия</b></label>\n" +
                "                        <input class=\"inputSurname\" type=\"text\" placeholder=\"Фамилия\" name=\"empSurname\" required>\n" +
                "\n" +
                "                        <label for=\"name\"><b>Имя</b></label>\n" +
                "                        <input class=\"inputName\" type=\"text\" placeholder=\"Имя\" name=\"empName\" required>\n" +
                "\n" +
                "                        <label for=\"patronymic\"><b>Отчество</b></label>\n" +
                "                        <input class=\"inputPatronymic\" type=\"text\" placeholder=\"Отчество\" name=\"empPatronymic\" required>\n";

        html +=
                "                        <label for=\"department\"><b>Отдел</b></label>\n" +
                        "                        <select class=\"inputDepartment\" name=\"empDepartment\">";

        for (Department dep : Department.values()) {
            html += "<option value=" + dep.name() + ">" + dep + "</option>";
        }

        html += "</select>" +
                "                        <label for=\"room\"><b>Помещение</b></label>\n" +
                "                        <input class=\"inputRoom\" type=\"text\" placeholder=\"Помещение\" name=\"empRoom\" required>\n" +
                "                        <label for=\"empPosition\"><b>Должность</b></label>\n" +
                "                        <select class=\"inputPosition\" name=\"empPosition\" >";

        for (Jobposition pos : Jobposition.values()) {
            html += "<option value=" + pos.name() + ">" + pos + "</option>";
        }

        html += "                    <input class=\"inputHidden\" type=\"text\" name=\"id\">\n" +
                "                        <button type=\"submit\">Редактировать сотрудника</button>\n" +
                "                    </div>" +
                "                <button type=\"button\" class=\"addAssetBtn\">Добавить технику</button>\n" +
                "                    <div class=\"containerModal\" style=\"background-color:#f1f1f1\">\n" +
                "                        <button type=\"button\" onclick=\"document.getElementById('modalEditEmp').style.display='none'\"\n" +
                "                            class=\"cancelBtn\">Отмена</button>\n" +
                "                        <button type=\"button\" class=\"deleteEmpBtn\">Удалить сотрудника</button>\n" +
                "                    </div>\n" +
                "                </form>\n" +
                "            </div>" +


                ////////////////////////////////////////////////////////////
                ////       Модальное окно добавления сертификата        ////
                ////////////////////////////////////////////////////////////
                " <div id=\"modalAddCert\" class=\"modalAddCert\">" +
                "                <form class=\"modal-content animate\" method=\"POST\" action=\"/main\">" +
                "                    <div class=\"imgcontainer\">" +
                "                        <span onclick=\"document.getElementById('modalAddCert').style.display='none'\" class=\"close\"" +
                "                            title=\"Закрыть окно\">×</span>" +
                "                    </div>" +
                "                    <div class=\"containerModal\">" +
                "                        <h3>Добавление сертификата</h3>" +
                "                        <input class=\"operType\" type=\"text\" placeholder=\"type\" name=\"operType\" value=\"addCert\" style=\"display:block;\">" +
                "                        <label for=\"certOwner\"><b>Владелец</b></label>\n" +
                "<select name=\"certOwner\">";
        List<Employee> emps = DBUtils.getInstance().getEmployeesList();
        emps = emps.stream().sorted(Comparator.comparing(Employee::getSurname)).collect(Collectors.toList());
        for (Employee emp : emps) {
            html += "<option value=" + emp.getId() + ">" + emp.getSurname() + " " + emp.getName() + " " + emp.getPatronymic() + "</option>";
        }

        html += "</select>" +
                "<label for=\"certType\"><b>Тип сертификата</b></label>\n" +
                "                        <input type=\"text\" placeholder=\"Тип выданного сертификата\" name=\"certType\" required>\n" +

                "                        <label for=\"certPublisher\"><b>Издатель</b></label>\n" +
                "                        <input type=\"text\" placeholder=\"Кем выдан\" name=\"certPublisher\" required>\n" +

                "                        <label for=\"date\"><b>Действует с:</b></label>\n" +
                "                        <input type=\"date\" name=\"calendar1\">" +

                "                        <label for=\"date\"><b>Действует по:</b></label>\n" +
                "                        <input type=\"date\" name=\"calendar2\">" +
                "                        <button type=\"submit\">Добавить сертификат</button></div>\n" +

                "                    <div class=\"containerModal\" style=\"background-color:#f1f1f1\">\n" +
                "                        <button type=\"button\" onclick=\"document.getElementById('modalAddCert').style.display='none'\"\n" +
                "                            class=\"cancelBtn\">Отмена</button>\n" +
                "                    </div>\n" +
                "                </form>\n" +
                "            </div>\n";

        ////////////////////////////////////////////////////////////
        ////            Модальное окно добавления техники       ////
        ////////////////////////////////////////////////////////////
        html += "     <div id=\"modalAddAssetTab\" class=\"modalAddAssetTab\">\n" +
                "                <form class=\"modal-content animate\" method=\"POST\">\n" +
                "                    <div class=\"imgcontainer\">\n" +
                "                        <span onclick=\"document.getElementById('modalAddAssetTab').style.display='none'\" class=\"close\"\n" +
                "                            title=\"Закрыть окно\">×</span>\n" +
                "                    </div>\n" +
                "                    <div class=\"containerModal\">\n" +
                "                        <h3>Добавление техники</h3>\n" +
                "                        <input class=\"operType\" type=\"text\" placeholder=\"type\" name=\"operType\" value=\"addAssetTab\" style=\"display:block;\">" +
                "                        <label for=\"assetName\"><b>Наименование</b></label>\n" +
                "                        <input class=\"inputName\" type=\"text\" placeholder=\"Наименование\" name=\"assetName\" required>\n" +

                "                        <label for=\"assetInvNumb\"><b>Инвентарный номер</b></label>\n" +
                "                        <input class=\"inputInvNumb\" type=\"text\" placeholder=\"Инв. номер\" name=\"assetInvNumb\">\n" +

                "                        <label for=\"assetSerNumb\"><b>Серийный номер</b></label>\n" +
                "                        <input class=\"inputSerNumb\" type=\"text\" placeholder=\"Сер. номер\" name=\"assetSerNumb\">\n" +

                "                        <label for=\"selectAssetType\"><b>Тип техники</b></label>\n" +
                "                        <select id=\"selectAssetType\" class=\"selectAssetType\" name=\"assetType\"><option value=\"no-type\">Не задан</option><option value=\"os\">ОС</option><option value=\"tmc\">ТМЦ</option></select>" +

                "                        <label for=\"assetDescr\"><b>Описание</b></label>\n" +
                "                        <textarea class=\"inputAssetDescr\" type=\"text\" placeholder=\"Описание\" rows=\"5\" style=\"width: 100%; min-width:50%; max-width: 100%;\" name=\"assetDescr\"></textarea>\n" +

                "                        <label for=\"selectOwner\"><b>Назначить сотрудника</b></label>\n" +
                "                        <select class=\"selectOwner\" name=\"ownerId\">";

        html += "<option selected value=\"empNullAdd\">Выберите сотрудника</option>";
        for (Employee e : emps) {
            html += "<option value=" + e.getId() + ">" + e.getSurname() + " " + e.getName() + " " + " " + e.getPatronymic() + " id: " + e.getId() + " " + "</option>";
        }

        html += "</select>" +
                "                        <button type=\"submit\">Добавить технику</button>\n" +
                "                    </div>\n" +

                "                    <div class=\"containerModal\" style=\"background-color:#f1f1f1\">\n" +
                "                        <button type=\"button\" onclick=\"document.getElementById('modalAddAssetTab').style.display='none'\"\n" +
                "                            class=\"cancelBtn\">Отмена</button>\n" +
                "                    </div>\n" +
                "                </form>\n" +
                "            </div>";


        ////////////////////////////////////////////////////////////
        ////         Модальное окно редактирования техники      ////
        ////////////////////////////////////////////////////////////
        html += "      <div id=\"modalEditAssetTab\" class=\"modalEditAssetTab\">\n" +
                "                <form class=\"modal-content animate\" method=\"POST\">\n" +
                "                    <div class=\"imgcontainer\">\n" +
                "                        <span onclick=\"document.getElementById('modalEditAssetTab').style.display='none'\" class=\"close\"\n" +
                "                            title=\"Закрыть окно\">×</span>\n" +
                "                    </div>\n" +

                "                    <div class=\"containerModal\">\n" +
                "                        <h3>Редактирование техники</h3>\n" +
                "                        <input class=\"operType\" type=\"text\" placeholder=\"type\" name=\"operType\" value=\"editAssetTab\" style=\"display:block;\">" +
                "                        <label for=\"assetName\"><b>Наименование</b></label>\n" +
                "                        <input class=\"inputName\" type=\"text\" placeholder=\"Наименование\" name=\"assetName\" required>\n" +

                "                        <label for=\"assetInvNumb\"><b>Инвентарный номер</b></label>\n" +
                "                        <input class=\"inputInvNumb\" type=\"text\" placeholder=\"Инв. номер\" name=\"assetInvNumb\">\n" +

                "                        <label for=\"assetSerNumb\"><b>Серийный номер</b></label>\n" +
                "                        <input class=\"inputSerNumb\" type=\"text\" placeholder=\"Сер. номер\" name=\"assetSerNumb\">\n" +

                "                        <label for=\"AssetType\"><b>Тип техники</b></label>\n" +
                "                        <select id=\"selectAssetType\" class=\"selectAssetType\" name=\"assetType\"><option value=\"no-type\">Не задан</option><option value=\"os\">ОС</option><option value=\"tmc\">ТМЦ</option></select>" +

                "                        <label for=\"assetDescr\"><b>Описание</b></label>\n" +

                "                        <textarea class=\"inputAssetDescr\" type=\"text\" placeholder=\"Описание\" rows=\"5\" style=\"width: 100%; max-width: 100%; min-width:50%;\" name=\"assetDescr\"></textarea>\n" +

                "                        <input class=\"inputHiddenAssetTab\" type=\"text\" name=\"assetId\">\n" +

                "                        <input class=\"inputHiddenEmpAssetTab\" type=\"text\" name=\"assetEmpId\">\n" +

                "                        <label for=\"selectOwn\"><b>Передать технику</b></label>\n" +
                "                        <select class=\"selectOwner\" name=\"newOwnerId\">";

        html += "<option value=\"empNullEdit\">Выберите сотрудника</option>";
        for (Employee e : emps) {
            html += "<option value=" + e.getId() + ">" + e.getSurname() + " " + e.getName() + " " + " " + e.getPatronymic() + " id: " + e.getId() + " " + "</option>";
        }

        html += "</select>" +

                "                        <button type=\"submit\">Отредактировать технику</button>\n" +
                "                    </div>\n" +

                "                    <div class=\"containerModal\" style=\"background-color:#f1f1f1\">\n" +
                "                        <button type=\"button\" onclick=\"document.getElementById('modalEditAssetTab').style.display='none'\"\n" +
                "                            class=\"cancelBtn\">Отмена</button>\n" +
                "                        <button type=\"button\" class=\"deleteAssetBtn\">Удалить технику</button>\n" +
                "                    </div>\n" +
                "                </form>\n" +
                "            </div>";


        ////////////////////////////////////////////////////////////
        ////            Модальное окно описания техники         ////
        ////////////////////////////////////////////////////////////
        html += "     <div id=\"modalAssetDescrIcon\" class=\"modalAssetDescrIcon\">\n" +
                "                <form class=\"modal-content animate\">\n" +
                "                    <div class=\"imgcontainer\">\n" +
                "                        <span onclick=\"document.getElementById('modalAssetDescrIcon').style.display='none'\" class=\"close\"\n" +
                "                            title=\"Закрыть окно\">×</span>\n" +
                "                    </div>\n" +
                "                    <div class=\"containerModal\">\n" +
                "                        <h3>Описание техники</h3>\n" +
                "                        <textarea class=\"inputAssetDescrIcon\" type=\"text\" placeholder=\"Описание\" rows=\"5\" style=\"width: 100%; min-width:50%; max-width: 100%;\" name=\"assetDescr\"></textarea>\n" +
                "                    </div>\n" +
                "                </form>\n" +
                "            </div>";


        html += "<!-- Таб сотрудники -->\n" +
                "<div class=\"info-tabcontent fade\">\n" +
                "   <div class=\"container-table100\">\n" +
                "       <div class=\"wrap-table100\">" +
                "           <div class=\"dropdown\">" +
                "               <i data-title=\"Работа в экселе\" style=\"padding:15px; font-size:30px;\" id=\"excelEmpIcon\" class=\"fa fa-file-excel-o\"></i>" +
                "                <div class=\"dropdown-content\">" +
                "                   <a>Импорт</a>" +
                "                   <a>Экспорт</a>" +
                "                   </div>" +
                "               </div>" +

            "                   <div class=\"dropdown\">" +
            "                       <i data-title=\"Сортировка\" style=\"padding:15px; font-size:30px;\" id=\"sort\" class=\"fa fa-bar-chart\"></i>" +
            "                            <div class=\"dropdown-content-big\">" +
            "                               <a>По справочнику</a>" +
            "                               <a>В алфавитном порядке</a>" +
            "                               <a>Руководство</a>" +
            "                               <a>ККиПР</a>" +
            "                               <a>ПиО</a>" +
            "                               <a>ЗИО</a>" +
            "                               <a>ЛК</a>" +
            "                               <a>БУОН</a>" +
            "                               <a>ЦДиППТ</a>" +
            "                               <a>ПТО</a>" +
            "                               <a>СО</a>" +
            "                               <a>ОДДиСАД</a>" +
            "                               <a>СиТБАДиИС</a>" +
            "                               <a>ЮО</a>" +
            "                               <a>СОКР</a>" +
            "                               <a>САХР</a>" +
            "                               <a>КиС</a>" +
            "                               <a>Водители и уборщицы</a>" +
            "                               <a>Уволен(а)</a>" +
            "                               </div>" +
            "                          </div>" +

            "<div class=\"dropdown\"><i data-title=\"Добавить сотрудника\" style=\"padding:15px; font-size:30px;\" id=\"addEmpIcon\" class=\"fa fa-plus-circle\"></i></div>" +
            "                        <div class=\"table100\">\n" +
            "                            <table id= \"tableEmp\" class=\"tableEmp\">\n" +
            "                                <thead class=\"headerRow\">\n" +
            "                                    <tr class=\"table100-head\">\n" +
            "                                        <th class=\"column1\">#</th>\n" +
            "                                        <th class=\"column2\">Фамилия</th>\n" +
            "                                        <th class=\"column3\">Имя</th>\n" +
            "                                        <th class=\"column4\">Отчество</th>\n" +
            "                                        <th class=\"column5\">Отдел</th>\n" +
            "                                        <th class=\"column6\">Каб.</th>\n" +
            "                                        <th class=\"column7\">Должность</th>\n" +
            "                                        <th class=\"column8\">id сотрудика</th>\n" +
            "                                    </tr>\n" +
            "                                </thead>\n" +
            "                             <tbody>";

        i = 0;


        for (Employee e : DBUtils.getInstance().getEmpListSorted()) {
            i++;
            html += "<tr class=\"tableRow\">" +
                    "<td class=\"column1\">" + i + "</td>" +
                    "<td class=\"column2\">" + e.getSurname() + "</td>" +
                    "<td class=\"column3\">" + e.getName() + "</td>" +
                    "<td class=\"column4\">" + e.getPatronymic() + "</td>" +
                    "<td class=\"column5\">" + e.getDepartment() + "</td>" +
                    "<td class=\"column6\">" + e.getRoom() + "</td>" +
                    "<td class=\"column7\">" + e.getJobposition() + "</td>" +
                    "<td class=\"column8\">" + e.getId() + "</td></tr>";
        }

        html += "                                </tbody>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>" +
                "             </div> " +

                "         <!-- Таб техники -->\n" +
                "            <div class=\"info-tabcontent fade\">\n" +
                "                <div class=\"container-table100\">\n" +
                "                    <div class=\"wrap-table100\">\n" +

                "<div class=\"dropdown\" id=\"excelAssetsMenu\">" +
                "    <i data-title=\"Excel\" style=\"padding:15px; font-size:30px;\" class=\"fa fa-file-excel-o\"></i>" +
                "    <div class=\"dropdown-content\">" +
                "        <a>Импорт техники</a>" +
                "        <a>Экспорт техники</a>" +
                "    </div>" +
                "</div>" +


                "<div class=\"dropdown\" id=\"excelAssetsSort\">" +
                "   <i data-title=\"Сортировка\" style=\"padding:15px; font-size:30px;\" class=\"fa fa-bar-chart\"></i>" +
                "   <div class=\"dropdown-content\">" +
                "       <a>Вся техника</a>" +
                "       <a>С владельцами</a>" +
                "       <a>Без владельца</a>" +
                "       <a>ОС</a>" +
                "       <a>ТМЦ</a>" +
                "       <a>Без типа</a>" +
                "       <a>Без инв. номера</a>" +
                "       <a>Без сер. номера</a>" +
                "    </div>" +
                "</div>" +


                "<div class=\"dropdown\">" +
                "<i data-title=\"Добавить Технику\" style=\"padding:15px; font-size:30px;\" id=\"addAssetIcon\" class=\"fa fa-plus-circle\"></i>" +
                "</div>" +
                "                        <div class=\"table100\">\n" +
                "                            <table id=\"tableAssets\" class=\"tableAssets\">" +
                "                                <thead class=\"headerRow\">" +
                "                                    <tr class=\"table100-head\">" +
                "                                        <th class=\"column1\">#</th>" +
                "                                        <th class=\"column2\">Наименование</th>" +
                "                                        <th class=\"column3\">Инвентарный номер</th>" +
                "                                        <th class=\"column4\">Серийный номер</th>" +
                "                                        <th class=\"column5\">Тип техники</th>" +
                "                                        <th class=\"column6\">Владелец</th>" +
                "                                        <th class=\"column7\">Владелец Id</th>" +
                "                                        <th class=\"column8\">Техника Id</th>" +
                "                                        <th class=\"column9\">Описание</th>" +
                "                                    </tr>\n" +
                "                                </thead>\n" +
                "                             <tbody>";

        i = 0;

        for (Asset a : DBUtils.getInstance().getAssetsList()) {

            String owner = "";
            i++;
            html += "<tr>" +
                    "<td class=\"column1\">" + i + "</td>" +
                    "<td class=\"column2\">" + a.getName() + "</td>" +
                    "<td class=\"column3\">" + a.getInvNumb() + "</td>" +
                    "<td class=\"column4\">" + a.getSerNumb() + "</td>";
            switch (a.getAssetType()) {
                case "os" : {
                    html+= "<td class=\"column5\">ОС</td>";
                    break;
                }
                case "tmc" : {
                    html+= "<td class=\"column5\">ТМЦ</td>";
                    break;
                }
                case "no-type" : {
                    html+= "<td class=\"column5\">Не задан</td>";
                    break;
                }
            }


            if (a.getEmployeeId() == 0) {
                html += "<td class=\"column6\">Не задан</td>";
            } else {
                html += "<td class=\"column6\">" + DBUtils.getInstance().getEmployeeById(a.getEmployeeId()).getSurname() + " " + DBUtils.getInstance().getEmployeeById(a.getEmployeeId()).getName().substring(0, 1) + ". " + DBUtils.getInstance().getEmployeeById(a.getEmployeeId()).getPatronymic().substring(0, 1) + ". - " + DBUtils.getInstance().getEmployeeById(a.getEmployeeId()).getDepartment() + "</td>";
            }

            html +=
                    "<td class=\"column7\">" + a.getEmployeeId() + "</td>" +
                    "<td class=\"column8\">" + a.getId() + "</td>" +
                    "<td class=\"column9\">" + a.getAssetDescr() + "</td></tr>";
        }

        html += "                               </tbody>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +



                "            <!-- Таб сертификатов -->\n" +
                "            <div class=\"info-tabcontent fade\">\n" +
                "                <div class=\"container-table100\">\n" +
                "                    <div class=\"wrap-table100\">\n" +
                "                        <div class=\"exp-hint\">\n" +
                "                            <div class=\"yellow-hint\"> </div> \n" +
                "                            <div class=\"yellow-hint-text\">- от одного до трех месяцев до истечения</div>\n" +
                "                            <div class=\"orange-hint\"></div>\n" +
                "                            <div class=\"orange-hint-text\">- менее одного месяца до истечения</div>\n" +
                "                            <div class=\"red-hint\"></div>\n" +
                "                            <div class=\"orange-hint-text\">- Закончился</div>\n" +
                "                        </div>" +
                "<div class=\"dropdown\"><i data-title=\"Добавить сертификат\" style=\"padding:15px; font-size:30px;\" id=\"addCertIcon\" class=\"fa fa-plus-circle\"></i></div>" +
                "<div class=\"dropdown\"><i data-title=\"Экспорт в эксель\" style=\"padding:15px; font-size:30px;\" id=\"excelCert\" class=\"fa fa-file-excel-o\"></i></div>" +

                "                   <div class=\"dropdown\"><i data-title=\"Сортировка\" style=\"padding:15px; font-size:30px;\" id=\"sortCert\" class=\"fa fa-bar-chart\"></i> " +
                "                       <div class=\"dropdown-content\">" +
                "                           <a>По порядку добавления</a>" +
                "                           <a>Сначала новые</a>" +
                "                           <a>Сначала старые</a>" +
                "                           <a>По алфавиту</a>" +
                "                        </div></div>" +
                "                        <div class=\"table100\">\n" +
                "                            <table  id=\"tableCert\" class=\"tableCert\">\n" +
                "                                <thead class=\"headerRow\">\n" +
                "                                    <tr class=\"table100-head\">\n" +
                "                                        <th class=\"column1\">#</th>\n" +
                "                                        <th class=\"column2\">ФИО</th>\n" +
                "                                        <th class=\"column3\">Тип</th>\n" +
                "                                        <th class=\"column4\">Издатель</th>\n" +
                "                                        <th class=\"column5\">с:</th>\n" +
                "                                        <th class=\"column6\">По:</th>\n" +
                "                                        <th class=\"column7\">id сертификата:</th>\n" +
                "                                        <th class=\"column8\">До истечения (дн.):</th>\n" +
                "                                    </tr>\n" +
                "                                </thead>\n" +
                "                             <tbody>";

        i = 0;
        for (Certificate c : DBUtils.getInstance().getCertificateList()) {
            i++;
            html += "<tr>" +
                    "<td class=\"column1\">" + i + "</td>" +
                    "<td class=\"column2\">" + DBUtils.getInstance().getEmployeeById(c.getEmployeeId()).getSurname() + " " + DBUtils.getInstance().getEmployeeById(c.getEmployeeId()).getName().substring(0, 1) + ". " + DBUtils.getInstance().getEmployeeById(c.getEmployeeId()).getPatronymic().substring(0, 1) + ". - " + DBUtils.getInstance().getEmployeeById(c.getEmployeeId()).getDepartment() + "</td>" +
                    "<td class=\"column3\">" + c.getCertType() + "</td>" +
                    "<td class=\"column4\">" + c.getCertPublisher() + "</td>" +
                    "<td class=\"column5\">" + c.getNotBefore().substring(0, 10) + "</td>" +
                    "<td class=\"column6\">" + c.getNotAfter().substring(0, 10) + "</td>" +
                    "<td class=\"column7\">" + c.getId() + "</td>" +
                    "<td class=\"column8\"></td>";
        }

        html += "</tr>\n" +
                "                                </tbody>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "<script src=\"js/script-design.js\"></script>\n" +
                "<script src=\"js/script-tech.js\"></script>\n" +
                "<script src=\"js/script-cert.js\"></script>\n" +
                "<script src=\"js/moment.js\"></script>\n" +
                "<script src=\"js/jquery-3.5.1.min.js\"></script></body></html>";
        return html;
    }
}

