package com.inventar.servlets.history;

import com.inventar.DBUtils;
import com.inventar.domain.History;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AssetViewHistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long assetId = Long.parseLong(req.getParameter("assetId"));
        response.getOutputStream().write(getPage(getTemplate(assetId)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    private String getTemplate(long assetId) {
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

        html += "       <div class=\"info\">" +
                "           <div class=\"info-tabcontent fade\">" +
                "               <div class=\"container-table10\">\n" +
                "                   <span class=\"closeButton\"></span>" +
                "                    <div class=\"wrap-table100\">" +
                "                            <table id= \"tableHistory\" class=\"tableHistory\">\n" +
                "                                <thead class=\"headerRow\">\n" +
                "                                    <tr class=\"table100-head\">\n" +
                "                                        <th class=\"column1\">#</th>\n" +
                "                                        <th class=\"column2\">Наименование</th>\n" +
                "                                        <th class=\"column3\">Инв. номер</th>\n" +
                "                                        <th class=\"column4\">Серийный номер</th>\n" +
                "                                        <th class=\"column5\">ФИО</th>\n" +
                "                                        <th class=\"column6\">Дата</th>\n" +
                "                                        <th class=\"column7\">Номер в базе</th>\n" +
                "                                        <th class=\"column8\">id сотрудника</th>\n" +
                "                                    </tr>\n" +
                "                                </thead>\n" +
                "                             <tbody>";


        for (History h : DBUtils.getInstance().getAssetHistoryByAssetId(assetId)) {
            html += "<tr class=\"tableRow\">" +
                    "<td class=\"column1\">" + h.getId() + "</td>" +
                    "<td class=\"column2\">" + DBUtils.getInstance().getAssetById(assetId).getName() + "</td>" + /*Наименование*/
                    "<td class=\"column3\">" + DBUtils.getInstance().getAssetById(assetId).getInvNumb() + "</td>" + /*Инвентарный номер*/
                    "<td class=\"column4\">" + DBUtils.getInstance().getAssetById(assetId).getSerNumb() + "</td>" + /*Серийный номер*/
//                    "<td class=\"column5\"><a id=\"historyOwner\">" + DBUtils.getInstance().getEmployeeById(h.getEmployeeId()).getDepartment() + " " + DBUtils.getInstance().getEmployeeById(h.getEmployeeId()).getSurname() + " " + DBUtils.getInstance().getEmployeeById(h.getEmployeeId()).getName() + " " + DBUtils.getInstance().getEmployeeById(h.getEmployeeId()).getPatronymic() + " id = " + h.getEmployeeId() + "</a></td>" + /*Владелец*/

                    "<td class=\"column5\">" + h.getOwnerInfo() + "</td>" +
                    "<td class=\"column6\">" + h.getDate() + "</td>" +
                    "<td class=\"column7\">" + h.getAssetId() + "</td>" +
                    "<td class=\"column8\">" + h.getEmployeeId() + "</td>" +
                    "</tr>";
        }

        html += "                               </tbody>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "<script src=\"js/script-history.js\"></script>\n" +
                "<script src=\"js/jquery-3.5.1.min.js\"></script></body></html>";
        return html;
    }

    private byte[] getPage(String text) {
        try {
            return text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
