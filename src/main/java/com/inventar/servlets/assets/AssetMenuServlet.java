package com.inventar.servlets.assets;

import com.inventar.DBUtils;
import com.inventar.domain.Asset;
import com.inventar.domain.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AssetMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("UTF-8");
        String idEmp = req.getParameter("id");
        long id = Long.parseLong(idEmp);
        response.getOutputStream().write(getPage(getTemplate(id)));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Парсим URL формы
        String idE = req.getParameter("assetEmpId");
        long empId = Long.parseLong(idE);
        String assetName = req.getParameter("assetName");
        String assetInvNumb = req.getParameter("assetInvNumb");
        String assetSerNumb = req.getParameter("assetSerNumb");
        String assetType = req.getParameter("assetType");
        String assetDescr = req.getParameter("assetDescr");

        //Определение вида операции
        String operType = req.getParameter("operType");

        String ownerInfo = "";
        switch (operType) {

            case "addAsset":
                DBUtils.getInstance().createAsset(assetName, assetInvNumb, assetSerNumb, assetType, assetDescr, empId);
                ownerInfo = DBUtils.getInstance().getEmployeeById(empId).getSurname() + " " + DBUtils.getInstance().getEmployeeById(empId).getName() + " " + DBUtils.getInstance().getEmployeeById(empId).getPatronymic() + " - " + DBUtils.getInstance().getEmployeeById(empId).getDepartment();
                DBUtils.getInstance().createHistoryRecord(DBUtils.getInstance().getLastAssetId(), empId, ownerInfo);
                break;
            case "editAsset":

                String newOwnerId = req.getParameter("newOwnerId");
                long newOwner = Long.parseLong(newOwnerId);

                if (newOwner != empId) {
                    empId = newOwner;
                }

                String idA = req.getParameter("assetId");
                long assetId = Long.parseLong(idA);
                ownerInfo = DBUtils.getInstance().getEmployeeById(empId).getSurname() + " " + DBUtils.getInstance().getEmployeeById(empId).getName() + " " + DBUtils.getInstance().getEmployeeById(empId).getPatronymic() + " - " + DBUtils.getInstance().getEmployeeById(empId).getDepartment();
                DBUtils.getInstance().editAsset(assetId, assetName, assetInvNumb, assetSerNumb, assetType, assetDescr, empId);
                DBUtils.getInstance().createHistoryRecord(DBUtils.getInstance().getLastAssetId(), empId, ownerInfo);
                break;
        }
        resp.sendRedirect("/menuAsset?id=" + empId);
    }

    private byte[] getPage(String text) {
        try {
            return text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getTemplate(long id) {
        String html = "<html>" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/tabs.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/table.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/modal.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/font-awesome.min.css\">" +
                "    <link rel=\"stylesheet\" href=\"css/dropdown.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/close.css\">\n" +
                "    <title>Inventory</title>\n" +
                "</head>" +
                "<body>" +
                "    <div class=\"content\"><span class=\"close\"></span>";


        ////////////////////////////////////////////////////////////
        ////            Модальное окно добавления              ////
        ////////////////////////////////////////////////////////////
        html += "            <div id=\"modalAddAsset\" class=\"modalAddAsset\">\n" +
                "                <form class=\"modal-content animate\" method=\"POST\">\n" +
                "                    <div class=\"imgcontainer\">\n" +
                "                        <span onclick=\"document.getElementById('modalAddAsset').style.display='none'\" class=\"close\"\n" +
                "                            title=\"Закрыть окно\">×</span>\n" +
                "                    </div>\n" +

                "                    <div class=\"containerModal\">\n" +
                "                        <h3>Добавление техники</h3>\n" +
                "                        <input class=\"operType\" type=\"text\" placeholder=\"type\" name=\"operType\" value=\"addAsset\" style=\"display:block;\">" +
                "                        <label for=\"assetName\"><b>Наименование</b></label>\n" +
                "                        <input class=\"inputName\" type=\"text\" placeholder=\"Наименование\" name=\"assetName\" required>\n" +

                "                        <label for=\"assetInvNumb\"><b>Инвентарный номер</b></label>\n" +
                "                        <input class=\"inputInvNumb\" type=\"text\" placeholder=\"Инв. номер\" name=\"assetInvNumb\">\n" +

                "                        <label for=\"assetSerNumb\"><b>Серийный номер</b></label>\n" +
                "                        <input class=\"inputSerNumb\" type=\"text\" placeholder=\"Сер. номер\" name=\"assetSerNumb\">\n" +

                "                        <label for=\"selectAssetType\"><b>Тип техники</b></label>\n" +
                "                        <select id=\"selectAssetType\" class=\"selectAssetType\" name=\"assetType\"><option value=\"no-type\">Не задан</option><option value=\"os\">ОС</option><option value=\"tmc\">ТМЦ</option></select>" +

                "                        <label for=\"assetDescr\"><b>Описание</b></label>\n" +
                "                        <textarea class=\"inputDescr\" type=\"text\" placeholder=\"Описание\" rows=\"5\" style=\"width: 100%; max-width: 100%;\" name=\"assetDescr\"></textarea>\n" +
                "                        <input class=\"inputHiddenEmp\" type=\"text\" name=\"assetEmpId\">\n" +
                "                        <button type=\"submit\">Добавить технику</button>\n" +
                "                    </div>\n" +

                "                    <div class=\"containerModal\" style=\"background-color:#f1f1f1\">\n" +
                "                        <button type=\"button\" onclick=\"document.getElementById('modalAddAsset').style.display='none'\"\n" +
                "                            class=\"cancelBtn\">Отмена</button>\n" +
                "                    </div>\n" +
                "                </form>\n" +
                "            </div>";


        ////////////////////////////////////////////////////////////
        ////            Модальное окно редактирования           ////
        ////////////////////////////////////////////////////////////
        html += "            <div id=\"modalEditAsset\" class=\"modalEditAsset\">\n" +
                "                <form class=\"modal-content animate\" method=\"POST\">\n" +
                "                    <div class=\"imgcontainer\">\n" +
                "                        <span onclick=\"document.getElementById('modalEditAsset').style.display='none'\" class=\"close\"\n" +
                "                            title=\"Закрыть окно\">×</span>\n" +
                "                    </div>\n" +

                "                    <div class=\"containerModal\">\n" +
                "                        <h3>Редактирование техники</h3>\n" +
                "                        <input class=\"operType\" type=\"text\" placeholder=\"type\" name=\"operType\" value=\"editAsset\" style=\"display:block;\">" +
                "                        <label for=\"assetName\"><b>Наименование</b></label>\n" +
                "                        <input class=\"inputName\" type=\"text\" placeholder=\"Наименование\" name=\"assetName\" required>\n" +

                "                        <label for=\"assetInvNumb\"><b>Инвентарный номер</b></label>\n" +
                "                        <input class=\"inputInvNumb\" type=\"text\" placeholder=\"Инв. номер\" name=\"assetInvNumb\">\n" +

                "                        <label for=\"assetSerNumb\"><b>Серийный номер</b></label>\n" +
                "                        <input class=\"inputSerNumb\" type=\"text\" placeholder=\"Сер. номер\" name=\"assetSerNumb\">\n" +

                "                        <label for=\"selectAssetType\"><b>Тип техники</b></label>\n" +
                "                        <select id=\"selectAssetType\" class=\"selectAssetType\" name=\"assetType\"><option value=\"none\"></option><option value=\"os\">ОС</option><option value=\"tmc\">ТМЦ</option></select>" +

                "                        <label for=\"assetDescr\"><b>Описание</b></label>\n" +
//                "                        <input class=\"inputAssetDescr\" type=\"text\" placeholder=\"Описание\" name=\"assetDescr\">\n" +
                "                        <textarea class=\"inputAssetDescr\" type=\"text\" placeholder=\"Описание\" rows=\"5\" style=\"width: 100%; max-width: 100%; min-width:50%;\" name=\"assetDescr\"></textarea>\n" +
                "                        <input class=\"inputHiddenAsset\" type=\"text\" name=\"assetId\">\n" +
                "                        <input class=\"inputHiddenEmp\" type=\"text\" name=\"assetEmpId\">\n" +

                "                        <label for=\"selectOwner\"><b>Передать технику</b></label>\n" +
                "                        <select class=\"selectOwner\" name=\"newOwnerId\">";

        List<Employee> emps = DBUtils.getInstance().getEmployeesList();
        emps = emps.stream().sorted(Comparator.comparing(Employee::getSurname)).collect(Collectors.toList());

        for (Employee e : emps) {
            html += "<option value=" + e.getId() + ">" + e.getSurname() + " " + e.getName() + " " + " " + e.getPatronymic() + " id: " + e.getId() + " " + "</option>";
        }

        html += "</select>" +


                "                        <button type=\"submit\">Отредактировать технику</button>\n" +
                "                    </div>\n" +

                "                    <div class=\"containerModal\" style=\"background-color:#f1f1f1\">\n" +
                "                        <button type=\"button\" onclick=\"document.getElementById('modalEditAsset').style.display='none'\"\n" +
                "                            class=\"cancelBtn\">Отмена</button>\n" +
                "                        <button type=\"button\" class=\"deleteEmpBtn\">Удалить технику</button>\n" +
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



        html += "       <div class=\"info\">" +
                "           <div class=\"info-tabcontent fade\">" +
                "               <div class=\"container-table10\">\n" +
                "<span class=\"closeButton\"></span>" +
                "                  <div class=\"wrap-table100\">" +
                "                   <div class=\"dropdown\">" +
                "                       <i data-title=\"Выгрузить в эксель\" style=\"padding:15px; font-size:30px;\" id=\"excelOutput\" class=\"fa fa-file-excel-o\"></i></div>" +
                "                            <table id= \"tableEmpAsset\" class=\"tableEmpAsset\">\n" +
                "                                <thead class=\"headerRow\">\n" +
                "                                    <tr class=\"table100-head\">\n" +
                "                                        <th class=\"column1\">#</th>\n" +
                "                                        <th class=\"column2\">Фамилия</th>\n" +
                "                                        <th class=\"column3\">Имя</th>\n" +
                "                                        <th class=\"column4\">Отчество</th>\n" +
                "                                        <th class=\"column5\">Отдел</th>\n" +
                "                                        <th class=\"column6\">Каб.</th>\n" +
                "                                        <th class=\"column7\">Должность</th>\n" +
                "                                    </tr>\n" +
                "                                </thead>\n" +
                "                             <tbody>";

        Employee e;
        e = DBUtils.getInstance().getEmployeeById(id);

        html += "<tr class=\"tableRow\">" +
                "<td class=\"column1\">" + e.getId() + "</td>" +
                "<td class=\"column2\">" + e.getSurname() + "</td>" +
                "<td class=\"column3\">" + e.getName() + "</td>" +
                "<td class=\"column4\">" + e.getPatronymic() + "</td>" +
                "<td class=\"column5\">" + e.getDepartment() + "</td>" +
                "<td class=\"column6\">" + e.getRoom() + "</td>" +
                "<td class=\"column7\">" + e.getJobposition() + "</td>";

        html += "                                </tbody>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            <div class=\"info-tabcontent fade\">\n" +
                "                <div class=\"container-table10\">\n" +
                "                    <div class=\"wrap-table100\">\n" +
                "                        <div class=\"table100\">\n" +
                "                            <table id=\"tableAsset\" class=\"tableAsset\">\n" +
                "                                <thead class=\"headerRow\">\n" +
                "                                    <tr class=\"table100-head\">\n" +
                "                                        <th class=\"column1\">#</th>" +
                "                                        <th class=\"column2\">Наименование</th>" +
                "                                        <th class=\"column3\">Инвентарный номер</th>" +
                "                                        <th class=\"column4\">Серийный номер</th>" +
                "                                        <th class=\"column5\">Тип техники</th>" +
                "                                        <th class=\"column6\">Владелец</th>" +
                "                                        <th class=\"column7\">Владелец Id</th>" +
                "                                        <th class=\"column8\">Id техники</th>" +
                "                                        <th class=\"column9\">Описание</th>" +
                "                                    </tr>\n" +
                "                                </thead>\n" +
                "                             <tbody>";

        int i = 0;

        for (Asset a : DBUtils.getInstance().getAssetListByEmployeeId(id)) {

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
                "<script src=\"js/script-menu.js\"></script>\n" +
                "<script src=\"js/jquery-3.5.1.min.js\"></script></body></html>";


        return html;
    }

}
