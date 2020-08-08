package com.inventar;

import com.inventar.servlets.*;
import com.inventar.servlets.assets.*;
import com.inventar.servlets.certificates.*;
import com.inventar.servlets.employees.*;
import com.inventar.servlets.excel_related.*;
import com.inventar.servlets.history.AssetViewHistory;
import com.inventar.servlets.history.HistoryDeleteServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.sql.SQLException;

public class Launcher {

    public static void main(String[] args) throws SQLException {
        initServer();
    }

    public static void initServer() throws SQLException {
        createTables();
        int port = 8081;
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        DefaultServlet defaultServlet = new DefaultServlet();
        ServletHolder holderPwd = new ServletHolder("MainServlet", defaultServlet);
        holderPwd.setInitParameter("resourceBase", "./src/main/resources");

        context.addServlet(holderPwd, "/");

        //Сервлеты сотрудника
        context.addServlet(new ServletHolder(new MainServlet()), "/main");
        context.addServlet(new ServletHolder(new EmpAddServlet()), "/addEmp");
        context.addServlet(new ServletHolder(new EmpDeleteServlet()), "/deleteEmp");
        context.addServlet(new ServletHolder(new EmpExportServlet()), "/exportEmp");
        context.addServlet(new ServletHolder(new EmpExportAssetsServlet()), "/exportEmpAssets");
        context.addServlet(new ServletHolder(new EmpImportExcel()), "/importEmp");
        context.addServlet(new ServletHolder(new EmpSortServlet()), "/sortEmp");

        //Сервлеты техники
        context.addServlet(new ServletHolder(new AssetMenuServlet()), "/menuAsset");
        context.addServlet(new ServletHolder(new AssetDeleteServlet()), "/deleteAsset");
        context.addServlet(new ServletHolder(new AssetViewHistory()), "/viewAssetHistory");
        context.addServlet(new ServletHolder(new HistoryDeleteServlet()), "/deleteHistoryRecord");
        context.addServlet(new ServletHolder(new AssetSortServlet()), "/assetSort");
        context.addServlet(new ServletHolder(new AssetExportServlet()), "/assetExport");
        context.addServlet(new ServletHolder(new AssetImportServlet()), "/assetImport");

        //Сервлеты сертификатов
        context.addServlet(new ServletHolder(new CertAddFileServlet()), "/addCertFile");
        context.addServlet(new ServletHolder(new CertDeleteServlet()), "/deleteCert");
        context.addServlet(new ServletHolder(new CertExportServlet()), "/exportCert");
        context.addServlet(new ServletHolder(new CertSortServlet()), "/sortCert");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context});

        server.setHandler(handlers);
        try {
            server.start();
            System.out.println("Listening port : " + port);
            server.join();
        } catch (Exception e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }

    public static void createTables() throws SQLException {
        if (!DBUtils.getInstance().getConn().isClosed()) {
            DBUtils.getInstance().createEmployeesTable();
            DBUtils.getInstance().createAssetsTable();
            DBUtils.getInstance().createHistoryTable();
            DBUtils.getInstance().createCertificateTable();
            System.out.println("Connected successfully");
        }
    }
}
