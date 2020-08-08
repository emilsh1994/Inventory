package com.inventar.servlets.assets;

import com.inventar.DBUtils;
import com.inventar.domain.Asset;
import com.inventar.domain.Certificate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AssetSortServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("UTF-8");
        String sort = req.getParameter("sortBy");
        resp.getOutputStream().write(getPage(getTemplate(sort)));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private byte[] getPage(String text) {
        try {
            return text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private String getTemplate(String sort) {

        List<Asset> assets = DBUtils.getInstance().getAssetsList();

        switch (sort) {
            case "none": // Убрать сортировку
                assets = DBUtils.getInstance().getAssetsList();
                DBUtils.getInstance().setAssetsListSorted(assets);
                break;

            case "withEmp": // С сотрудниками
                assets = DBUtils.getInstance().getAssetListWithOwner();
                DBUtils.getInstance().setAssetsListSorted(assets);
                break;

            case "withNoEmp": // Без сотрудников
                assets = DBUtils.getInstance().getAssetListByEmployeeId(0);
                DBUtils.getInstance().setAssetsListSorted(assets);
                break;

            case "os": // ОС
                assets = DBUtils.getInstance().getAssetListByType("'os'");
                DBUtils.getInstance().setAssetsListSorted(assets);
                break;

            case "tmc": // ТМЦ
                assets = DBUtils.getInstance().getAssetListByType("'tmc'");
                DBUtils.getInstance().setAssetsListSorted(assets);
                break;

            case "no-type": // Без типа
                assets = DBUtils.getInstance().getAssetListByType("'no-type'");
                DBUtils.getInstance().setAssetsListSorted(assets);
                break;

            case "noInvNumb": // Без типа
                assets = DBUtils.getInstance().getAssetListByInvNumb("''");
                DBUtils.getInstance().setAssetsListSorted(assets);
                break;

            case "noSerNumb": // Без типа
                assets = DBUtils.getInstance().getAssetListBySerNumb("''");
                DBUtils.getInstance().setAssetsListSorted(assets);
                break;
        }

        String html="                        <table id=\"tableAssets\" class=\"tableAssets\">\n" +
                "                                <thead class=\"headerRow\">\n" +
                "                                    <tr class=\"table100-head\">\n" +
                "                                        <th class=\"column1\">#</th>\n" +
                "                                        <th class=\"column2\">Наименование</th>\n" +
                "                                        <th class=\"column3\">Инвентарный номер</th>\n" +
                "                                        <th class=\"column4\">Серийный номер</th>\n" +
                "                                        <th class=\"column5\">Тип техники</th>\n" +
                "                                        <th class=\"column6\">Владелец</th>\n" +
                "                                        <th class=\"column7\">Владелец Id</th>\n" +
                "                                        <th class=\"column8\">Техника Id</th>\n" +
                "                                        <th class=\"column9\">Описание</th>\n" +
                "                                    </tr>\n" +
                "                                </thead>\n" +
                "                             <tbody>";


        int i = 0;

        for (Asset a : assets) {

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

        html += "</tbody></table>";
        return html;
    }
}
