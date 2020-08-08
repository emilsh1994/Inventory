package com.inventar.servlets.employees;

import com.inventar.DBUtils;
import com.inventar.domain.Employee;
import com.inventar.enums.Department;
import com.inventar.enums.Jobposition;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

public class EmpSortServlet extends HttpServlet {

    List<Department> deps;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("UTF-8");
        String sort = req.getParameter("sortBy");
        resp.getOutputStream().write(getPage(getTemplate(sort)));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private List<Department> depsInOrder() {
        deps = new ArrayList<>();
        deps.add(Department.rukovodstvo);
        deps.add(Department.kkipr);
        deps.add(Department.pio);
        deps.add(Department.zio);
        deps.add(Department.lk);
        deps.add(Department.buon);
        deps.add(Department.cdippt);
        deps.add(Department.pto);
        deps.add(Department.so);
        deps.add(Department.oddisad);
        deps.add(Department.sitbadiis);
        deps.add(Department.uo);
        deps.add(Department.sokr);
        deps.add(Department.sahr);
        deps.add(Department.kis);
        deps.add(Department.vodi_i_ubor) ;
        deps.add(Department.none);
        return deps;
    }

    public List<Employee> getEmpsByPhone() {

        List<Department> deps = depsInOrder();
        List<Employee> emps = DBUtils.getInstance().getEmployeesList();

        List<Employee> empsPhone = new ArrayList<>();
        Deque<Employee> empsDeque = new LinkedList<>();

        for (Department d : deps) {
            for (Employee e : emps) {
                if (d == e.getDepartment()) {
                    if (e.getDepartment() == Department.rukovodstvo && e.getJobposition() == Jobposition.rukovoditel || e.getJobposition() == Jobposition.io_rukovoditel) {
                        empsDeque.addFirst(e);
                        continue;
                    }
                    empsPhone.add(e);
                    empsDeque.add(e);
                }
            }
        }

        System.out.println("size of phone: " + empsDeque.size());
        emps = new ArrayList<>(empsDeque);

        return emps;
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

        List<Employee> emps = DBUtils.getInstance().getEmployeesList();
        List<Employee> empsDep;

        switch (sort) {

            case "byEmp": // По алфавиту
                emps = emps.stream().sorted(Comparator.comparing(Employee::getSurname)).collect(Collectors.toList());
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "byPhone": // По тел справочнику

//                List<Department> deps = depsInOrder();
//
//                emps = DBUtils.getInstance().getEmployeesList();
//                List<Employee> empsPhone = new ArrayList<>();
//
//                for (Department d : deps) {
//                    for (Employee e : emps) {
//                        if (d == e.getDepartment()) {
//                            empsPhone.add(e);
//                        }
//                    }
//                }
//                System.out.println("size of phone: " + empsPhone.size());
//                emps = empsPhone;
                emps = getEmpsByPhone();
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "rukovodstvo": //Руководство
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'rukovodstvo'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "kkipr":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'kkipr'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "pio":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'pio'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "zio":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'zio'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "lk":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'lk'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "buon":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'buon'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "cdippt":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'cdippt'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "pto":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'pto'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "so":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'so'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "oddisad":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'oddisad'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "sitbadiis":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'sitbadiis'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "uo":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'uo'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "sokr":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'sokr'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "sahr":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'sahr'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "kis":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'kis'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "vodi_i_ubor":
                empsDep = DBUtils.getInstance().getEmployeeListByDepartment("'vodi_i_ubor'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;

            case "fired":
                empsDep = DBUtils.getInstance().getEmployeeListByJobposition("'fired'");
                emps = empsDep;
                DBUtils.getInstance().setEmpListSorted(emps);
                break;
        }

        String html = "<table id=\"tableEmp\" class=\"tableEmp\">\n" +
                "                                <thead class=\"headerRow\">\n" +
                "                                    <tr class=\"table100-head\">\n" +
                "                                        <th class=\"column1\">#</th>\n" +
                "                                        <th class=\"column2\">Фамилия</th>\n" +
                "                                        <th class=\"column3\">Имя</th>\n" +
                "                                        <th class=\"column4\">Отчество</th>\n" +
                "                                        <th class=\"column5\">Отдел</th>\n" +
                "                                        <th class=\"column6\">Каб.</th>\n" +
                "                                        <th class=\"column7\">Должность</th>\n" +
                "                                        <th class=\"column8\" style=\"display:none;\">id</th>\n" +
                "                                    </tr>\n" +
                "                                </thead>\n" +
                "                             <tbody>";

        for (Employee e : emps) {
            i++;
            html += "<tr class=\"tableRow\">" +
                    "<td class=\"column1\">" + i + "</td>" +
                    "<td class=\"column2\">" + e.getSurname() + "</td>" +
                    "<td class=\"column3\">" + e.getName() + "</td>" +
                    "<td class=\"column4\">" + e.getPatronymic() + "</td>" +
                    "<td class=\"column5\">" + e.getDepartment() + "</td>" +
                    "<td class=\"column6\">" + e.getRoom() + "</td>" +
                    "<td class=\"column7\">" + e.getJobposition() + "</td>" +
                    "<td class=\"column8\" style=\"display:none;\">" + e.getId() + "</td></tr>";
        }

        html += "</tbody></table>";
        return html;
    }
}
