package com.inventar;

import com.inventar.domain.Asset;
import com.inventar.domain.Certificate;
import com.inventar.domain.Employee;
import com.inventar.domain.History;
import com.inventar.enums.Department;
import com.inventar.enums.Jobposition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {


    private static DBUtils dbUtils;

    private ArrayList<Employee> employees;
    private ArrayList<Asset> assets;

    private List<Certificate> certListSorted;
    private List<Employee> empListSorted;
    private List<Asset> assetsListSorted;

    //Создание подключения
    private Connection conn;
    private ResultSet resultSetEmployee;
    private ResultSet resultSetAsset;
    //Таблица сотрудников
    private String table1;
    //Таблица техники
    private String table2;

    private DBUtils() {
        employees = new ArrayList<>();
        assets = new ArrayList<>();
        certListSorted = new ArrayList<>();
        empListSorted = new ArrayList<>();
        assetsListSorted = new ArrayList<>();
    }

    public static DBUtils getInstance() {
        if (dbUtils == null) {
            dbUtils = new DBUtils();
        }
        try {
            if (dbUtils.getConn() == null || dbUtils.getConn().isClosed()) {
                dbUtils.dbConnect();
                System.out.println("Соединение восстановлено.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbUtils;
    }

    public List<Certificate> getCertListSorted() {
        return certListSorted;
    }

    public void setCertListSorted(List<Certificate> certListSorted) {
        this.certListSorted = certListSorted;
    }

    public List<Employee> getEmpListSorted() {
        return empListSorted;
    }

    public void setEmpListSorted(List<Employee> empListSorted) {
        this.empListSorted = empListSorted;
    }

    public List<Asset> getAssetsListSorted() {
        return assetsListSorted;
    }

    public void setAssetsListSorted(List<Asset> assetsListSorted) {
        this.assetsListSorted = assetsListSorted;
    }

    /*
    Подключение к базе данных
     */
    public boolean dbConnect() {
        boolean bool = false;
        String url = "jdbc:mysql://localhost/inventory?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&characterEncoding=utf8";
        String user = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected");
            bool = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            bool = false;
        }
        return bool;
    }


    /*
    Создание таблицы сотрудников
     */
    public void createEmployeesTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS employees(Id INT PRIMARY KEY AUTO_INCREMENT, Surname VARCHAR(30), Name VARCHAR(30), Patronymic VARCHAR (30), Department VARCHAR(30), Room VARCHAR(30), Post VARCHAR(30))");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Создание таблицы техники
     */
    public void createAssetsTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS assets(Id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(999), invNumb VARCHAR(100), serNumb VARCHAR(100), assetType VARCHAR(10), assetDescr VARCHAR(999), employeeId INT(30) NOT NULL)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Создание таблицы истории
     */
    public void createHistoryTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS history(Id INT PRIMARY KEY AUTO_INCREMENT, assetId INT(30), employeeId INT(30), ownerInfo VARCHAR(512), recordDate VARCHAR (30))");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    Создание таблицы сертификатов
    */
    public void createCertificateTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS certificate(Id INT PRIMARY KEY AUTO_INCREMENT, employeeId INT(30), certType VARCHAR(30), certPublisher VARCHAR(30), dateNotBefore TIMESTAMP, dateNotAfter TIMESTAMP)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Удаление таблицы сотрудников
     */
    public void dropEmployeesTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS employees");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Удаление таблицы техники
     */
    public void dropAssetsTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS assets");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Удаление таблицы истории
     */
    public void dropHistoryTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS history");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Удаление таблицы истории
     */
    public void dropCertificateTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS certificate");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Редактирование  сотрудника
     */
    public void editEmployee(long id, String surname, String name, String patronymic, Department department, String room, Jobposition jobposition) {
        try (Statement statement = conn.createStatement()) {
            PreparedStatement pSt = conn.prepareStatement("UPDATE employees Set Surname = ?, Name = ?,  Patronymic = ?, Department = ?, Room = ?, Post = ? WHERE ID = ?");
            pSt.setString(1, surname);
            pSt.setString(2, name);
            pSt.setString(3, patronymic);
            pSt.setString(4, department.name());
            pSt.setString(5, room);
            pSt.setString(6, jobposition.name());
            pSt.setLong(7, id);
            pSt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Рекдактирование  техники
     */
    public void editAsset(long id, String assetName, String assetInvNumb, String assetSerNumb, String assetType, String assetDescr, long assetEmpId) {
        try (Statement statement = conn.createStatement()) {
            PreparedStatement pSt = conn.prepareStatement("UPDATE assets Set Name = ? , InvNumb = ?,  SerNumb = ?, AssetType = ?, AssetDescr = ?, employeeId = ? WHERE ID = ?");
            pSt.setString(1, assetName);
            pSt.setString(2, assetInvNumb);
            pSt.setString(3, assetSerNumb);
            pSt.setString(4, assetType);
            pSt.setString(5, assetDescr);
            pSt.setLong(6, assetEmpId);
            pSt.setLong(7, id);
            pSt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Получение сотрудника по id
     */
    public Employee getEmployeeById(long id) {
        Employee employee = new Employee();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM employees WHERE Id = " + id);
            if (rs.next()) {
                employee.setId(rs.getInt(1));
                employee.setSurname(rs.getString(2));
                employee.setName(rs.getString(3));
                employee.setPatronymic(rs.getString(4));
                employee.setDepartment(Department.valueOf(rs.getString(5)));
                employee.setRoom(rs.getString(6));
                employee.setJobposition(Jobposition.valueOf(rs.getString(7)));
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return employee;
    }


    /*
    Получение списка техники по Id сотрудника
     */
    public ArrayList<Asset> getAssetListByEmployeeId(long employeeId) {
        ArrayList<Asset> al = new ArrayList<Asset>();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM assets WHERE employeeId=" + employeeId);
            while (rs.next()) {
                Asset a = new Asset();
                a.setId(rs.getInt(1));
                a.setName(rs.getString(2));
                a.setInvNumb(rs.getString(3));
                a.setSerNumb(rs.getString(4));
                a.setAssetType(rs.getString(5));
                a.setAssetDescr(rs.getString(6));
                a.setEmployeeId(rs.getLong(7));
                al.add(a);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return al;
    }

    /*
    Получение списка техники по типу
     */
    public ArrayList<Asset> getAssetListByType(String assetTypeStr) {
        ArrayList<Asset> al = new ArrayList<Asset>();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM assets WHERE assetType=" + assetTypeStr);
            while (rs.next()) {
                Asset a = new Asset();
                a.setId(rs.getInt(1));
                a.setName(rs.getString(2));
                a.setInvNumb(rs.getString(3));
                a.setSerNumb(rs.getString(4));
                a.setAssetType(rs.getString(5));
                a.setAssetDescr(rs.getString(6));
                a.setEmployeeId(rs.getLong(7));
                al.add(a);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return al;
    }

    /*
  Получение списка техники по нвентарному номеру
   */
    public ArrayList<Asset> getAssetListByInvNumb(String invNumbStr) {
        ArrayList<Asset> al = new ArrayList<Asset>();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM assets WHERE InvNumb=" + invNumbStr);
            while (rs.next()) {
                Asset a = new Asset();
                a.setId(rs.getInt(1));
                a.setName(rs.getString(2));
                a.setInvNumb(rs.getString(3));
                a.setSerNumb(rs.getString(4));
                a.setAssetType(rs.getString(5));
                a.setAssetDescr(rs.getString(6));
                a.setEmployeeId(rs.getLong(7));
                al.add(a);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return al;
    }

    /*
Получение списка техники по серийному номеру
*/
    public ArrayList<Asset> getAssetListBySerNumb(String serNumbStr) {
        ArrayList<Asset> al = new ArrayList<Asset>();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM assets WHERE serNumb=" + serNumbStr);
            while (rs.next()) {
                Asset a = new Asset();
                a.setId(rs.getInt(1));
                a.setName(rs.getString(2));
                a.setInvNumb(rs.getString(3));
                a.setSerNumb(rs.getString(4));
                a.setAssetType(rs.getString(5));
                a.setAssetDescr(rs.getString(6));
                a.setEmployeeId(rs.getLong(7));
                al.add(a);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return al;
    }




    /*
     Получение списка присвоенной техники
      */
    public ArrayList<Asset> getAssetListWithOwner() {
        ArrayList<Asset> al = new ArrayList<Asset>();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM assets WHERE employeeId != 0");
            while (rs.next()) {
                Asset a = new Asset();
                a.setId(rs.getInt(1));
                a.setName(rs.getString(2));
                a.setInvNumb(rs.getString(3));
                a.setSerNumb(rs.getString(4));
                a.setAssetType(rs.getString(5));
                a.setAssetDescr(rs.getString(6));
                a.setEmployeeId(rs.getLong(7));
                al.add(a);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return al;
    }


    /*
    Получение техники по id
     */
    public Asset getAssetById(long id) {
        Asset asset = new Asset();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM assets WHERE Id = " + id);
            if (rs.next()) {
                asset.setId(rs.getLong(1));
                asset.setName(rs.getString(2));
                asset.setInvNumb(rs.getString(3));
                asset.setSerNumb(rs.getString(4));
                asset.setAssetType(rs.getString(5));
                asset.setAssetDescr(rs.getString(6));
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return asset;
    }


    /*
    Создание сотрудника
     */
    public boolean createEmployee(String empSurname, String empName, String empPatronymic, Department empDepartment, String empRoom, Jobposition empJobposition) {
        try (PreparedStatement pSt = conn.prepareStatement("INSERT employees(Surname, Name, Patronymic, Department, Room, Post) VALUES (?, ?, ?, ?, ?, ?)")) {
            pSt.setString(1, empSurname);
            pSt.setString(2, empName);
            pSt.setString(3, empPatronymic);
            pSt.setString(4, empDepartment.name());
            pSt.setString(5, empRoom);
            pSt.setString(6, empJobposition.name());
            return pSt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /*
    Создание техники
     */
    public boolean createAsset(String assetName, String assetInvNumb, String assetSerNumb, String assetType, String assetDescr, long employeeId) {
        try (PreparedStatement pSt = conn.prepareStatement("INSERT assets(Name, InvNumb, SerNumb, AssetType, AssetDescr, employeeId) VALUES (?, ?, ?, ?, ?, ?)")) {
            pSt.setString(1, assetName);
            pSt.setString(2, assetInvNumb);
            pSt.setString(3, assetSerNumb);
            pSt.setString(4, assetType);
            pSt.setString(5, assetDescr);
            pSt.setLong(6, employeeId);
            return pSt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /*
    Создание записи в истории
     */
    public boolean createHistoryRecord(long assetId, long employeeId, String ownerInfo) {
        try (PreparedStatement pSt = conn.prepareStatement("INSERT history(assetId, employeeId, ownerInfo, recordDate) VALUES(?, ?, ?, NOW())")) {
            pSt.setLong(1, assetId);
            pSt.setLong(2, employeeId);
            pSt.setString(3, ownerInfo);
            return pSt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /*
    Создание записи в истории
     */
    public boolean createCertificateRecord(long employeeId, String certType, String certPublisher, String dateNotBefore, String dateNotAfter) {
        try (PreparedStatement pSt = conn.prepareStatement("INSERT certificate(employeeId, certType, certPublisher, dateNotBefore, dateNotAfter) VALUES(?, ?, ?, ?, ?)")) {
            pSt.setLong(1, employeeId);
            pSt.setString(2, certType);
            pSt.setString(3, certPublisher);
            pSt.setString(4, dateNotBefore);
            pSt.setString(5, dateNotAfter);
            return pSt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    //Удаление сотрудника
    public boolean deleteEmployee(long id) {
        boolean bool = false;
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM employees WHERE Id=" + id);
            bool = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bool;
    }

    //Удаление техники
    public boolean deleteAsset(long id) {
        boolean bool = false;
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM assets WHERE Id=" + id);
            bool = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bool;
    }

    //Удаление истории - не сделан
    public boolean deleteHistoryRecord(long id) {
        boolean bool = false;
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM history WHERE Id=" + id);
            bool = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bool;
    }

    //Удаление записи о сертификате
    public boolean deleteCertificateRecord(long id) {
        boolean bool = false;
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM certificate WHERE Id=" + id);
            bool = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bool;
    }


    //Вывод сотрудников в idea - не нужен
    public void outputEmployees(ArrayList<Employee> list) {
        ArrayList<Employee> em = list;
        for (int i = 0; i < em.size(); i++) {
            System.out.println(em.get(i).getId() + " " + em.get(i).getSurname() + " " + em.get(i).getName() + " " + em.get(i).getPatronymic());
        }
    }


    //Получение списка всех сотрудников
    public ArrayList<Employee> getEmployeesList() {
        ArrayList<Employee> em = new ArrayList<>();
        Employee emp;
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM employees");
            while (rs.next()) {
                emp = new Employee();
                emp.setId(rs.getInt(1));
                emp.setSurname(rs.getString(2));
                emp.setName(rs.getString(3));
                emp.setPatronymic(rs.getString(4));
                emp.setDepartment(Department.valueOf(rs.getString(5)));
                emp.setRoom(rs.getString(6));
                emp.setJobposition(Jobposition.valueOf(rs.getString(7)));
                em.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return em;
    }

    //Вывод списка всей техники
    public ArrayList<Asset> getAssetsList() {
        ArrayList<Asset> al = new ArrayList<>();
        Asset a;
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM assets");
            while (rs.next()) {
                a = new Asset();
                a.setId(rs.getLong(1));
                a.setName(rs.getString(2));
                a.setInvNumb(rs.getString(3));
                a.setSerNumb(rs.getString(4));
                a.setAssetType(rs.getString(5));
                a.setAssetDescr(rs.getString(6));
                a.setEmployeeId(rs.getLong(7));
                al.add(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    /*
    Получение списка всех сертификатов
     */
    public ArrayList<Certificate> getCertificateList() {
        ArrayList<Certificate> al = new ArrayList<>();
        Certificate c;
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM certificate");
            while (rs.next()) {
                c = new Certificate();
                c.setId(rs.getLong(1));
                c.setEmployeeId(rs.getLong(2));
                c.setCertType(rs.getString(3));
                c.setCertPublisher(rs.getString(4));
                c.setNotBefore(rs.getString(5));
                c.setNotAfter(rs.getString(6));
                al.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ;
        }
        return al;
    }

    //Получение Id последней записи в таблице техники
    public long getLastAssetId() {
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT MAX(Id) FROM assets");
            if (rs.next()) {
                return rs.getLong(1);
            } else return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //Получение списка истории по Id техники
    public ArrayList<History> getAssetHistoryByAssetId(long id) {
        ResultSet rs;
        ArrayList<History> hl = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            rs = statement.executeQuery("SELECT * FROM history WHERE assetId=" + id);
            while (rs.next()) {
                History h = new History();
                h.setId(rs.getLong(1));
                h.setAssetId(rs.getLong(2));
                h.setEmployeeId(rs.getLong(3));
                h.setOwnerInfo(rs.getString(4));
                h.setDate(rs.getString(5));
                hl.add(h);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hl;
    }


    //Получение сотрудников в отделе
    public ArrayList<Employee> getEmployeeListByDepartment(String dep) {
        ResultSet rs;
        ArrayList<Employee> em = new ArrayList<>();
        Employee emp;
        try (Statement statement = conn.createStatement()) {
            rs = statement.executeQuery("SELECT * FROM employees WHERE department=" + dep);
            while (rs.next()) {
                emp = new Employee();
                emp.setId(rs.getInt(1));
                emp.setSurname(rs.getString(2));
                emp.setName(rs.getString(3));
                emp.setPatronymic(rs.getString(4));
                emp.setDepartment(Department.valueOf(rs.getString(5)));
                emp.setRoom(rs.getString(6));
                emp.setJobposition(Jobposition.valueOf(rs.getString(7)));
                em.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return em;
    }

    //Получение сотрудников по должности
    public ArrayList<Employee> getEmployeeListByJobposition(String jp) {
        ResultSet rs;
        ArrayList<Employee> em = new ArrayList<>();
        Employee emp;
        try (Statement statement = conn.createStatement()) {
            rs = statement.executeQuery("SELECT * FROM employees WHERE Post=" + jp);
            while (rs.next()) {
                emp = new Employee();
                emp.setId(rs.getInt(1));
                emp.setSurname(rs.getString(2));
                emp.setName(rs.getString(3));
                emp.setPatronymic(rs.getString(4));
                emp.setDepartment(Department.valueOf(rs.getString(5)));
                emp.setRoom(rs.getString(6));
                emp.setJobposition(Jobposition.valueOf(rs.getString(7)));
                em.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return em;
    }

    public long getEmployeeIdByFIO(String empSurname, String empName, String empPatron) {
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT Id FROM employees WHERE Surname LIKE '" + empSurname + "' AND Name LIKE '" + empName + "' AND Patronymic LIKE '" + empPatron + "'");
            if (rs.next()) {
                return rs.getLong(1);
            } else return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }


    public void editCertificateRecord(long id, long employeeId, String certType, String certPublisher, long dateNotBefore, long dateNotAfter) {
        try (PreparedStatement pSt = conn.prepareStatement("UPDATE certificate Set employeeId = ?, certType = ?, certPublisher = ?, dateNotBefore = ?, dateNotAfter = ? WHERE ID = ?")) {
            pSt.setLong(1, employeeId);
            pSt.setString(2, certType);
            pSt.setString(3, certPublisher);
            pSt.setLong(4, dateNotBefore);
            pSt.setLong(5, dateNotAfter);
            pSt.setLong(6, id);
            pSt.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Certificate getCertificateRecordById(long id) {
        Certificate c = new Certificate();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM certificate WHERE Id = " + id);
            getCertificate(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return c;
    }

    public Certificate getCertificateByEmployeeId(long id) {
        Certificate c = new Certificate();
        ResultSet rs;
        try (Statement statement = conn.createStatement()) {
            rs = statement.executeQuery("SELECT * FROM certificate WHERE employeeId=" + id);
            getCertificate(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return c;
    }


    public Certificate getCertificate(ResultSet rs) {
        Certificate cert = new Certificate();
        try {
            if (rs.next()) {
                cert.setId(rs.getLong(1));
                cert.setEmployeeId(rs.getLong(2));
                cert.setCertType(rs.getString(3));
                cert.setCertPublisher(rs.getString(4));
                cert.setNotBefore(rs.getString(5));
                cert.setNotAfter(rs.getString(6));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cert;
    }


    public boolean importEmployeesFromExcel(ArrayList<Employee> ale) {
        for (Employee e : ale) {
            try (PreparedStatement pSt = conn.prepareStatement("INSERT employees(Surname, Name, Patronymic, Department, Room, Post) VALUES (?, ?, ?, ?, ?, ?)")) {
                System.out.println(e.getSurname() + " " + e.getName() + " " + e.getPatronymic() + " " + e.getDepartment() + e.getRoom() + " " + e.getJobposition());
                pSt.setString(1, e.getSurname());
                pSt.setString(2, e.getName());
                pSt.setString(3, e.getPatronymic());
                pSt.setString(4, e.getDepartment().name());
                pSt.setString(5, e.getRoom());
                pSt.setString(6, e.getJobposition().name());
                pSt.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }


    public boolean importAssetsFromExcel(ArrayList<Asset> ala) {
        for (Asset a : ala) {
            try (PreparedStatement pSt = conn.prepareStatement("INSERT assets(Name, InvNumb, SerNumb, AssetType, AssetDescr, employeeId) VALUES (?, ?, ?, ?, ?, ?)")) {

                pSt.setString(1, a.getName());
                pSt.setString(2, a.getInvNumb());
                pSt.setString(3, a.getSerNumb());
                pSt.setString(4, a.getAssetType());
                pSt.setString(5, a.getAssetDescr());
                pSt.setString(6, "0");
                pSt.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }


    public Connection getConn() {
        return conn;
    }
}
