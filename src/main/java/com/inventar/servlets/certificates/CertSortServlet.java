package com.inventar.servlets.certificates;

import com.inventar.DBUtils;
import com.inventar.ExcelUtils;
import com.inventar.domain.Certificate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CertSortServlet extends HttpServlet {

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
        int i = 0;

        List<Certificate> certs = DBUtils.getInstance().getCertificateList();

        switch (sort) {
            case "none": // Убрать сортировку
                certs = DBUtils.getInstance().getCertificateList();
                DBUtils.getInstance().setCertListSorted(certs);
                break;

            case "byDateUp": // По дате вверх
                certs = certs.stream().sorted(Comparator.comparing((Certificate o) -> o.getNotAfter()).reversed()).collect(Collectors.toList());
                DBUtils.getInstance().setCertListSorted(certs);
                break;

            case "byDateDown": // По вниз
                certs = certs.stream().sorted(Comparator.comparing((Certificate o) -> o.getNotAfter())).collect(Collectors.toList());
                DBUtils.getInstance().setCertListSorted(certs);
                break;

            case "byExpirationDate":
                break;

            case "byAlphabet": // По алфавиту
                certs = certs.stream().sorted(Comparator.comparing((Certificate o) -> DBUtils.getInstance().getEmployeeById(o.getEmployeeId()).getSurname())).collect(Collectors.toList());
                DBUtils.getInstance().setCertListSorted(certs);
                break;
        }

        String html = "<table id=\"tableCert\" class=\"tableCert\">\n" +
                "                    <thead class=\"headerRow\">\n" +
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
        for (Certificate c : certs) {
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

        html += "</tbody></table>";
        return html;
    }
}