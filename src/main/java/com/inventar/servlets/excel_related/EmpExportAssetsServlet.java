package com.inventar.servlets.excel_related;

import com.inventar.ExcelUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EmpExportAssetsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("UTF-8");
        long id = Long.parseLong(req.getParameter("id"));
        ExcelUtils.getInstance().exportAssetsOfEmployeeToExcel(id);
        resp.sendRedirect("/menuAsset?id=" + id);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
