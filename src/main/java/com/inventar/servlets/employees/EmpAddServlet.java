package com.inventar.servlets.employees;

import com.inventar.DBUtils;
import com.inventar.enums.Department;
import com.inventar.enums.Jobposition;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


//Сервлет для добавления сотрудников
public class EmpAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("UTF-8");
        response.getOutputStream().write(getPage(getTemplate()));

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empSurname = req.getParameter("empSurname");
        String empName = req.getParameter("empName");
        String empPatronymic = req.getParameter("empPatronymic");
        String empDepartment = req.getParameter("empDepartment");
        String empRoom = req.getParameter("empRoom");
        String empPosition = req.getParameter("empPosition");
        DBUtils.getInstance().createEmployee(empSurname, empName, empPatronymic, Department.valueOf(empDepartment), empRoom, Jobposition.valueOf(empPosition));
        resp.sendRedirect("/main");
    }


    private byte[] getPage(String text) {
        try {
            return text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTemplate() {
        String html = "<html>" +
                "<head><title>Инвентарь</title>" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><link rel=\"stylesheet\" type=\"text/css\" href=\"styles/style.css\"></head>" +
                "<body>"
                + "<form> <input value = \"На главную страницу\" type=\"button\" onclick=\"location.href='/main'\"/>"
                + "<form method=\"POST\" action=\"/addEmp\">"
                + "<table class=\"tg\">"
                + "<th class=\"tg-baqh\" colspan=\"7\">Добавление сотрудника</th>"
                + "<tr>"
                + "<td class=\"tg-hmp3\">"
                + "Фамилия"
                + "</td>"
                + "<td class=\"tg-hmp3\">"
                + "Имя"
                + "</td>"
                + "<td class=\"tg-hmp3\">"
                + "Отчество</td>"
                + "<td class=\"tg-hmp3\">"
                + "Отдел</td>"
                + "<td class=\"tg-hmp3\">"
                + "Помещение</td>"
                + "<td class=\"tg-hmp3\">"
                + "Должность</td>"
                + "<td class=\"tg-hmp3\">"
                + "</td>"
                + "</tr>";


        html += "<tr>" +
                "<td><input type=\"text\" name = \"empSurname\"></td>" +
                "<td><input type=\"text\" name = \"empName\"></td>" +
                "<td><input type=\"text\" name = \"empPatronymic\"></td>" +
                "<td><select name=\"empDepartment\" >";
        for (Department dep : Department.values()) {
            html += "<option value=" + dep.name() + ">" + dep + "</option>";
        }
        html += "</select>" +

                "<td><input type=\"text\" name = \"empRoom\"></td>" +
                "<td><select name=\"empPosition\" >";
        for (Jobposition pos : Jobposition.values()) {
            html += "<option value=" + pos.name() + ">" + pos + "</option>";
        }
        html += "</select>" +
                "<td>" + "<input type=\"submit\" value=\"Добавить\"/>" + "</td>" +
                "</tr>" +
                "</html>";
        return html;
    }


}
