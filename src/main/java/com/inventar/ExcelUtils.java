package com.inventar;

import com.inventar.domain.Asset;
import com.inventar.domain.Certificate;
import com.inventar.domain.Employee;
import com.inventar.enums.Department;
import com.inventar.enums.Jobposition;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ExcelUtils {


    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    String format = sdf.format(date);

    private static ExcelUtils excelUtils;
    private List<Certificate> alCert;
    private List<Employee> alEmp;
    private List<Asset> alAsset;

    private ExcelUtils() {
        alCert = new ArrayList<>();
        alEmp = new ArrayList<>();
        alAsset = new ArrayList<>();
    }

    public List<Certificate> getAlCert() {
        return alCert;
    }

    public List<Employee> getAlEmp() {
        return alEmp;
    }

    public List<Asset> getAlAsset() {
        return alAsset;
    }

    public static ExcelUtils getInstance() {
        if (excelUtils == null) {
            excelUtils = new ExcelUtils();
        }
        return excelUtils;
    }

    //Экспорт листа сертификатов в Эксель
    public void exportCertListToExcel() throws IOException {

        String fileName = "Сертификаты ГКУ УДХ РБ на " + format + ").xlsx";

        alCert = DBUtils.getInstance().getCertListSorted();

        XSSFWorkbook book = new XSSFWorkbook();

        Sheet sheet = book.createSheet("Сертификаты ГКУ УДХ РБ");
        Row row1 = sheet.createRow(0);
        sheet.setColumnWidth(0, 1300);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 8000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);

        XSSFCellStyle cellStyle = book.createCellStyle();
        XSSFCellStyle cellStyle2 = book.createCellStyle();

        XSSFFont font1 = book.createFont();
        XSSFFont font2 = book.createFont();

        font1.setBold(true);

        cellStyle.setFont(font1);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        font1.setBold(true);

        //Первая строка
        Cell cellId = row1.createCell(0);
        cellId.setCellStyle(cellStyle);

        cellId.setCellValue("Id");

        Cell cellFio = row1.createCell(1);
        cellFio.setCellStyle(cellStyle);
        cellFio.setCellValue("ФИО");

        Cell cellDep = row1.createCell(2);
        cellDep.setCellStyle(cellStyle);
        cellDep.setCellValue("Отдел");

        Cell cellCertType = row1.createCell(3);
        cellCertType.setCellStyle(cellStyle);
        cellCertType.setCellValue("Тип сертификата");

        Cell cellCertPub = row1.createCell(4);
        cellCertPub.setCellStyle(cellStyle);
        cellCertPub.setCellValue("Издатель сертификата");

        Cell cellCertNotBefore = row1.createCell(5);
        cellCertNotBefore.setCellStyle(cellStyle);
        cellCertNotBefore.setCellValue("Действует с:");

        Cell cellCertNotAfter = row1.createCell(6);
        cellCertNotAfter.setCellStyle(cellStyle);
        cellCertNotAfter.setCellValue("Действует до:");

        for (int i = 0; i < DBUtils.getInstance().getCertificateList().size(); i++) {
            Row row = sheet.createRow(i + 1);

            Cell cell = row.createCell(0);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(DBUtils.getInstance().getCertListSorted().get(i).getId());

            cell = row.createCell(1);
            cell.setCellValue(DBUtils.getInstance().getEmployeeById(DBUtils.getInstance().getCertListSorted().get(i).getEmployeeId()).getSurname() + " " + DBUtils.getInstance().getEmployeeById(DBUtils.getInstance().getCertificateList().get(i).getEmployeeId()).getName() + " " + DBUtils.getInstance().getEmployeeById(DBUtils.getInstance().getCertificateList().get(i).getEmployeeId()).getPatronymic());

            cell = row.createCell(2);
            cell.setCellValue(DBUtils.getInstance().getEmployeeById(DBUtils.getInstance().getCertListSorted().get(i).getEmployeeId()).getDepartment().toString());

            cell = row.createCell(3);
            cell.setCellValue(DBUtils.getInstance().getCertListSorted().get(i).getCertType());

            cell = row.createCell(4);
            cell.setCellValue(DBUtils.getInstance().getCertListSorted().get(i).getCertPublisher());

            cell = row.createCell(5);
            cell.setCellValue(DBUtils.getInstance().getCertListSorted().get(i).getNotBefore().substring(0,10));

            cell = row.createCell(6);
            cell.setCellValue(DBUtils.getInstance().getCertListSorted().get(i).getNotAfter().substring(0,10));
        }
        book.write(new FileOutputStream(fileName));
        book.close();

    }

    //Экспорт листа сотрудников в Эксель
    public void exportEmployeeListToExcel() throws IOException {

        String fileName = "Сотрудники ГКУ УДХ РБ на " + format + ").xlsx";
        alEmp = DBUtils.getInstance().getEmpListSorted();
        long size = alEmp.size();
        System.out.println("size - " + size);

        for(Employee e : alEmp) {
            System.out.println(e.getId() + " " + e.getSurname());
        }


        XSSFWorkbook book = new XSSFWorkbook();

        Sheet sheet = book.createSheet("Сотрудники ГКУ УДХ РБ");

        Row row1 = sheet.createRow(0);
        sheet.setColumnWidth(0, 1300); //id
        sheet.setColumnWidth(1, 7000); //f
        sheet.setColumnWidth(2, 7000); //i
        sheet.setColumnWidth(3, 7000); //o
        sheet.setColumnWidth(4, 4000); //dep
        sheet.setColumnWidth(5, 3500); //room
        sheet.setColumnWidth(6, 8000); //position

        XSSFCellStyle cellStyle = book.createCellStyle();
        XSSFCellStyle cellStyle2 = book.createCellStyle();

        XSSFFont font1 = book.createFont();

        font1.setBold(true);

        cellStyle.setFont(font1);
        font1.setBold(true);


        //Первая строка
        Cell cellId = row1.createCell(0);
        cellId.setCellStyle(cellStyle);

        cellId.setCellValue("Id");

        Cell cellSurname = row1.createCell(1);
        cellSurname.setCellStyle(cellStyle);
        cellSurname.setCellValue("Фамилия");

        Cell cellName = row1.createCell(2);
        cellName.setCellStyle(cellStyle);
        cellName.setCellValue("Имя");

        Cell cellPatr = row1.createCell(3);
        cellPatr.setCellStyle(cellStyle);
        cellPatr.setCellValue("Отчество");

        Cell cellDep = row1.createCell(4);
        cellDep.setCellStyle(cellStyle);
        cellDep.setCellValue("Отдел");

        Cell cellCertType = row1.createCell(5);
        cellCertType.setCellStyle(cellStyle);
        cellCertType.setCellValue("Помещение");

        Cell cellCertPub = row1.createCell(6);
        cellCertPub.setCellStyle(cellStyle);
        cellCertPub.setCellValue("Должность");

        for (int i = 0; i < alEmp.size(); i++) {
            Row row = sheet.createRow(i + 1);

            Cell cell = row.createCell(0);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(alEmp.get(i).getId());

            cell = row.createCell(1);
            cell.setCellValue(alEmp.get(i).getSurname());

            cell = row.createCell(2);
            cell.setCellValue(alEmp.get(i).getName());

            cell = row.createCell(3);
            cell.setCellValue(alEmp.get(i).getPatronymic());

            cell = row.createCell(4);
            cell.setCellValue(alEmp.get(i).getDepartment().toString());

            cell = row.createCell(5);
            cell.setCellValue(alEmp.get(i).getRoom());

            cell = row.createCell(6);
            cell.setCellValue(alEmp.get(i).getJobposition().toString());
        }

        book.write(new FileOutputStream(fileName));
        book.close();
    }


    //Экспорт листа сотрудников в Эксель
    public void exportAssetsListToExcel() throws IOException {

        String fileName = "Вся заведенная в базу техника ГКУ УДХ РБ на (" + format + ").xlsx";

        alAsset = DBUtils.getInstance().getAssetsListSorted();

        if (alAsset == null) {
            alAsset = DBUtils.getInstance().getAssetsList();
        }

        XSSFWorkbook book = new XSSFWorkbook();

        Sheet sheet = book.createSheet("Вся заведенная в базу техника ГКУ УДХ РБ");
        Row row1 = sheet.createRow(0);
        sheet.setColumnWidth(0, 1300); //id
        sheet.setColumnWidth(1, 7000); //Наименование
        sheet.setColumnWidth(2, 7000); //InvNumb
        sheet.setColumnWidth(3, 7000); //SerNumb
        sheet.setColumnWidth(4, 4000); //description
        sheet.setColumnWidth(5, 3500); //owner

        XSSFCellStyle cellStyle = book.createCellStyle();
        XSSFCellStyle cellStyle2 = book.createCellStyle();

        XSSFFont font1 = book.createFont();

        font1.setBold(true);

        cellStyle.setFont(font1);
        font1.setBold(true);


        //Первая строка
        Cell cellId = row1.createCell(0);
        cellId.setCellStyle(cellStyle);

        cellId.setCellValue("#");

        Cell cellSurname = row1.createCell(1);
        cellSurname.setCellStyle(cellStyle);
        cellSurname.setCellValue("Наименование");

        Cell cellName = row1.createCell(2);
        cellName.setCellStyle(cellStyle);
        cellName.setCellValue("Инв. номер");

        Cell cellPatr = row1.createCell(3);
        cellPatr.setCellStyle(cellStyle);
        cellPatr.setCellValue("Сер. номер");

        Cell cellDep = row1.createCell(4);
        cellDep.setCellStyle(cellStyle);
        cellDep.setCellValue("Описание");

        Cell cellCertType = row1.createCell(5);
        cellCertType.setCellStyle(cellStyle);
        cellCertType.setCellValue("Владелец");

        List<Employee> owners = DBUtils.getInstance().getEmployeesList();

        for (int i = 0; i < DBUtils.getInstance().getAssetsListSorted().size(); i++) {
            Row row = sheet.createRow(i + 1);

            Cell cell = row.createCell(0);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(DBUtils.getInstance().getAssetsListSorted().get(i).getId());

            cell = row.createCell(1);
            cell.setCellValue(DBUtils.getInstance().getAssetsListSorted().get(i).getName());

            cell = row.createCell(2);
            cell.setCellValue(DBUtils.getInstance().getAssetsListSorted().get(i).getInvNumb());

            cell = row.createCell(3);
            cell.setCellValue(DBUtils.getInstance().getAssetsListSorted().get(i).getSerNumb());

            cell = row.createCell(4);
            cell.setCellValue(DBUtils.getInstance().getAssetsListSorted().get(i).getAssetDescr());

            String cellOwner = "";
            Employee emp;
            long id = DBUtils.getInstance().getAssetsListSorted().get(i).getEmployeeId();
            cell = row.createCell(5);


            if (id != 0){
                emp = DBUtils.getInstance().getEmployeeById(id);
                cell.setCellValue(emp.getSurname());
            }
            else {
                id = 0;
                cell.setCellValue(0);
            }
        }

        book.write(new FileOutputStream(fileName));
        book.close();
    }

    //Выгрузка сотрудника и техники в Экселе
    public void exportAssetsOfEmployeeToExcel(long id) throws FileNotFoundException, IOException {

        Employee emp;
        emp = DBUtils.getInstance().getEmployeeById(id);

        String fileName = "Техника сотрудника " + emp.getSurname() + " " + emp.getName() + " " + emp.getPatronymic() + " ГКУ УДХ РБ.xls";


        XSSFWorkbook book = new XSSFWorkbook();

        Sheet sheet = book.createSheet("Техника сотрудника ГКУ УДХ РБ");

        Row row1 = sheet.createRow(0);
        sheet.setColumnWidth(0, 1300); //id
        sheet.setColumnWidth(1, 7000); //f
        sheet.setColumnWidth(2, 7000); //i
        sheet.setColumnWidth(3, 7000); //o
        sheet.setColumnWidth(4, 4000); //dep
        sheet.setColumnWidth(5, 3500); //room
        sheet.setColumnWidth(6, 8000); //position

        XSSFCellStyle cellStyle = book.createCellStyle();
        XSSFCellStyle cellStyle2 = book.createCellStyle();

        XSSFFont font1 = book.createFont();

        font1.setBold(true);


        cellStyle.setFont(font1);
        font1.setBold(true);


        //Первая строка
        Cell cellId = row1.createCell(0);
        cellId.setCellStyle(cellStyle);
        cellId.setCellValue("Id");

        Cell cellFio = row1.createCell(1);
        cellFio.setCellStyle(cellStyle);
        cellFio.setCellValue("Фамилия");

        Cell cellDep = row1.createCell(2);
        cellDep.setCellStyle(cellStyle);
        cellDep.setCellValue("Имя");

        Cell cellCertType = row1.createCell(3);
        cellCertType.setCellStyle(cellStyle);
        cellCertType.setCellValue("Отчество");

        Cell cellCertPub = row1.createCell(4);
        cellCertPub.setCellStyle(cellStyle);
        cellCertPub.setCellValue("Отдел");

        Cell cellCertNotBefore = row1.createCell(5);
        cellCertNotBefore.setCellStyle(cellStyle);
        cellCertNotBefore.setCellValue("Помещение");

        Cell cellCertNotAfter = row1.createCell(6);
        cellCertNotAfter.setCellStyle(cellStyle);
        cellCertNotAfter.setCellValue("Должность");


        //Создание нового ряда для вывода сотрудника
        Row row = sheet.createRow(1);

        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(DBUtils.getInstance().getEmployeeById(id).getId());

        cell = row.createCell(1);
        cell.setCellValue(DBUtils.getInstance().getEmployeeById(id).getSurname());

        cell = row.createCell(2);
        cell.setCellValue(DBUtils.getInstance().getEmployeeById(id).getName());

        cell = row.createCell(3);
        cell.setCellValue(DBUtils.getInstance().getEmployeeById(id).getPatronymic());

        cell = row.createCell(4);
        cell.setCellValue(DBUtils.getInstance().getEmployeeById(id).getDepartment().toString());

        cell = row.createCell(5);
        cell.setCellValue(DBUtils.getInstance().getEmployeeById(id).getRoom());

        cell = row.createCell(6);
        cell.setCellValue(DBUtils.getInstance().getEmployeeById(id).getJobposition().toString());

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        CellRangeAddress c;
        //Выгрузка техники
        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("Техника сотрудника");
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5));



        //Строка для заголовка таблицы
        row = sheet.createRow(4);



        //Первая строка
        cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Id");

        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Наименование");

        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Серийный номер");

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Инвентарный номер");

        for (int i = 0; i < DBUtils.getInstance().getAssetListByEmployeeId(id).size(); i++) {

            row = sheet.createRow(i + 5);

            cell = row.createCell(0);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(DBUtils.getInstance().getAssetListByEmployeeId(id).get(i).getId());

            cell = row.createCell(1);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(DBUtils.getInstance().getAssetListByEmployeeId(id).get(i).getName());

            cell = row.createCell(2);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(DBUtils.getInstance().getAssetListByEmployeeId(id).get(i).getInvNumb());

            cell = row.createCell(3);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(DBUtils.getInstance().getAssetListByEmployeeId(id).get(i).getSerNumb());

            cell = row.createCell(4);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(DBUtils.getInstance().getAssetListByEmployeeId(id).get(i).getAssetDescr());
        }

        //Выгрузка в эксель
        book.write(new FileOutputStream(fileName));
        book.close();
    }

    //Импорт сотрудников из Экселя
    public void importEmployeesFromExcel(XSSFWorkbook book) throws IOException {
        try {
            ArrayList<Employee> ale = new ArrayList<>();
            Employee emp;
//            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
//            Workbook workbook = new XSSFWorkbook(excelFile);

            Sheet sheet = book.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                emp = new Employee();
                while (cellIterator.hasNext()) {


                    Cell currentCell = cellIterator.next();

                    if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getColumnIndex() == 0) {
                        System.out.println(currentCell.getStringCellValue());
                        emp.setSurname(currentCell.getStringCellValue());
                    }

                    if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getColumnIndex() == 1) {
                        System.out.println(currentCell.getStringCellValue());
                        emp.setName(currentCell.getStringCellValue());
                    }

                    if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getColumnIndex() == 2) {
                        System.out.println(currentCell.getStringCellValue());
                        emp.setPatronymic(currentCell.getStringCellValue());
                    }


                    if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getColumnIndex() == 3) {
                        System.out.println(currentCell.getStringCellValue());
                        emp.setDepartment(Department.valueOf(currentCell.getStringCellValue()));
                    }

                    if (currentCell.getColumnIndex() == 4) {
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            String str = String.valueOf(currentCell.getStringCellValue());
                            emp.setRoom(str);
                            System.out.println(str);
                        } else {
                            String num = String.valueOf(currentCell.getNumericCellValue());
                            final String regex = "([0-9]*\\w)";
                            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            final Matcher matcher = pattern.matcher(num);
                            if (matcher.find()) {
                                String snum = matcher.group(1);
                                emp.setRoom(snum);
                                System.out.println(snum);
                            }
                        }
                    }
                    if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getColumnIndex() == 5) {
                        System.out.println(currentCell.getStringCellValue());
                        emp.setJobposition(Jobposition.valueOf(currentCell.getStringCellValue()));
                    }
                    System.out.println(emp.getSurname() + " " + emp.getName() + " " + emp.getPatronymic() + " " + emp.getDepartment() + emp.getRoom() + " " + emp.getJobposition());
                }
                ale.add(emp);
            }
            DBUtils.getInstance().importEmployeesFromExcel(ale);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Импорт Техники из Экселя
    public void importAssetsFromExcel(XSSFWorkbook book) throws IOException {
        try {
            ArrayList<Asset> ala = new ArrayList<>();
            Asset a;

            Sheet sheet = book.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                a = new Asset();
                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();

                    if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getColumnIndex() == 0) {
                        System.out.println(currentCell.getStringCellValue());
                        a.setName(currentCell.getStringCellValue());
                    }

                    if (currentCell.getColumnIndex() == 1) {
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            String str = String.valueOf(currentCell.getStringCellValue());
                            a.setInvNumb(str);
                        } else {
                            String str = String.valueOf(currentCell.getNumericCellValue()).split("\\.")[0];
                            a.setInvNumb(str);
                        }
                    }

                    if (currentCell.getColumnIndex() == 2) {
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            String str = String.valueOf(currentCell.getStringCellValue());
                            a.setSerNumb(str);
                        } else {
                            String str = String.valueOf(currentCell.getNumericCellValue()).split("\\.")[0];
                            a.setSerNumb(str);
                        }
                    }


                    if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getColumnIndex() == 3) {
                        System.out.println(currentCell.getStringCellValue());
                        a.setAssetType(currentCell.getStringCellValue());
                    }

                    if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getColumnIndex() == 4) {
                        System.out.println(currentCell.getStringCellValue());
                        a.setAssetDescr(currentCell.getStringCellValue());
                    }


                }
                ala.add(a);
            }
            DBUtils.getInstance().importAssetsFromExcel(ala);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
