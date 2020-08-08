package com.inventar.servlets.certificates;

import com.inventar.DBUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CertAddFileServlet extends HttpServlet {

    final String regexFio = "SURNAME=([А-Яа-я]*), GIVENNAME=([А-Яа-я]*) ([А-Яа-я]*)";
    final String regexPublisher = "CN=([^,]*)";
    final String regexDate = "([A-z]*\\s)([0-9]*\\s)(...*\\s)([0-9]*\\w)";

    final Pattern patternFio = Pattern.compile(regexFio, Pattern.MULTILINE);
    final Pattern patternPublisher = Pattern.compile(regexPublisher, Pattern.MULTILINE);
    final Pattern patternDate = Pattern.compile(regexDate, Pattern.MULTILINE);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getOutputStream().write(getPage(getTemplate()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //Загрузка через файл
        String empSurname = "", empName = "", empPatron = "", certPublisher = "";
        String dateNotBeforeDay = "", dateNotBeforeMonth = "", dateNotBeforeYear = "";
        String dateNotAfterDay = "", dateNotAfterMonth = "", dateNotAfterYear = "";
        String certType = "";

        boolean isMultipart = ServletFileUpload.isMultipartContent(req);

        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List items = upload.parseRequest(req);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (!item.isFormField()) {
                        CertificateFactory cf = CertificateFactory.getInstance("X509");
                        X509Certificate c = (X509Certificate) cf.generateCertificate(item.getInputStream());
                        System.out.println("\tCertificate for: " + c.getSubjectDN());

                        //Парсинг ФИО из сертификата
                        String fio = c.getSubjectDN().toString();
                        final Matcher matcherFio = patternFio.matcher(fio);
                        if (matcherFio.find()) {
                            empSurname = matcherFio.group(1);
                            empName = matcherFio.group(2);
                            empPatron = matcherFio.group(3);
                            System.out.println(empSurname + " " + empName + " " + empPatron + " F I O");
                        }

                        //Парсинг издателя из сертификата
                        String certIssuer = c.getIssuerDN().toString();
                        final Matcher matcherPublisher = patternPublisher.matcher(certIssuer);
                        if (matcherPublisher.find()) {
                            certPublisher = matcherPublisher.group(1);
                        }

                        //Парсинг даты начала действия сертификата
                        String certDateNotBefore = c.getNotBefore().toString();
                        final Matcher matcherDateNotBefore = patternDate.matcher(certDateNotBefore);
                        if (matcherDateNotBefore.find()) {
                            dateNotBeforeMonth = matcherDateNotBefore.group(1).trim();
                            dateNotBeforeDay = matcherDateNotBefore.group(2).trim();
                            dateNotBeforeYear = matcherDateNotBefore.group(4).trim();
                        }

                        //Парсинг даты конца действия сертификата
                        String certDateNotAfter = c.getNotAfter().toString();
                        final Matcher matcherDateNotAfter = patternDate.matcher(certDateNotAfter);
                        if (matcherDateNotAfter.find()) {
                            dateNotAfterMonth = matcherDateNotAfter.group(1).trim();
                            dateNotAfterDay = matcherDateNotAfter.group(2).trim();
                            dateNotAfterYear = matcherDateNotAfter.group(4).trim();
                        }

                        System.out.println("\tCertificate issued by: " + c.getIssuerDN());
                        System.out.println("\tThe certificate is valid from " + c.getNotBefore() + " to " + c.getNotAfter());
                        System.out.println("\tCertificate SN# " + c.getSerialNumber());
                        System.out.println("\tGenerated with " + c.getSigAlgName());
                    } else {
                        certType = item.getString();
                    }
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
                long certNotBefore = 0;
                long certNotAfter = 0;
                certNotBefore = sdf.parse(dateNotBeforeDay + " " + monthConverter(dateNotBeforeMonth) + " " + dateNotBeforeYear).getTime();
                certNotAfter = sdf.parse(dateNotAfterDay + " " + monthConverter(dateNotAfterMonth) + " " + dateNotAfterYear).getTime();

                //Проверка на наличие владельца сертификата в БД
                if (DBUtils.getInstance().getEmployeeIdByFIO(empSurname, empName, empPatron) == 0 || DBUtils.getInstance().getEmployeeIdByFIO(empSurname, empName, empPatron) == -1) {
                    System.out.println("Такого сотрудника в базе нет.");
                    resp.sendRedirect("/certServlet");
                    return;
                } else {
//                    DBUtils.getInstance().createCertificateRecord(DBUtils.getInstance().getEmployeeIdByFIO(empSurname, empName, empPatron), certType, certPublisher, certNotBefore, certNotAfter);
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resp.sendRedirect("/certServlet");
    }


    private byte[] getPage(String text) {
        try {
            return text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String monthConverter(String month) {
        switch (month) {
            case "Jan": {
                month = "01";
                break;
            }
            case "Feb": {
                month = "02";
                break;
            }
            case "Mar": {
                month = "03";
                break;
            }
            case "Apr": {
                month = "04";
                break;
            }
            case "May": {
                month = "05";
                break;
            }
            case "Jun": {
                month = "06";
                break;
            }
            case "Jul": {
                month = "07";
                break;
            }
            case "Aug": {
                month = "08";
                break;
            }
            case "Sep": {
                month = "09";
                break;
            }
            case "Oct": {
                month = "10";
                break;
            }
            case "Nov": {
                month = "11";
                break;
            }
            case "Dec": {
                month = "12";
                break;
            }
        }
        return month;
    }


    //Добавление через файл
    private String getTemplate() {
        String html = "<html>" +
                "<form action=\"/addCertFile\" enctype=\"multipart/form-data\" method=\"post\">" +
                "<input type=\"file\" name=\"upload\">" +
                "<input type=\"text\" name=\"certType\">" +
                "<input type = \"submit\">" +
                "</form>" +
                "</html>";
        return html;
    }


}
