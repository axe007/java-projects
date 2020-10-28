package dit042;

import dit042.exceptions.*;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * UserController controller class for Employee and Customer object
 *
 * <p>
 *     Maintains arraylist for all Employees and Customers, provide methods for behaviours.
 *     Loads and saves employees and customers arraylist from/to DartData.txt file.
 *     When adding new users, empty password is allowed since strict requirement is not
 *     mentioned.
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public class UserController {

    private final Helper getInput = new Helper();
    private static final UserInterface userInterface = new UserInterface();
    private final String EOL = System.lineSeparator();
    private final static String MANAGERPASSWORD = "admin1234";
    private static final ArrayList<Employee> employees = new ArrayList<>();
    private static final ArrayList<Customer> customers = new ArrayList<>();

    /***************************************************************************
     *                       USER AUTHENTICATION METHODS                       *
     ***************************************************************************/

    public void authenticateManager() {
        String pwdEntered = getInput.getStringRequired("userManagerPasswordPrompt", "Manager password");

        if (pwdEntered.equals(MANAGERPASSWORD)) {
            userInterface.printlnInterfaceLabels("userDartWelcomeManager");
            UserInterface menuSystem = new UserInterface();
            menuSystem.managerMenu();
        } else {
            userInterface.printlnInterfaceLabels("userAuthWrongPassword");
            getInput.sleepMilliseconds();
        }
    }

    public void authenticateUser(String objectType) {
        try {
            UserInterface menuSystem = new UserInterface();
            boolean idFound = false;
            String pwdToAuth = null;
            String activeId = null;
            String activeName = null;

            String idToAuth = getInput.getIdRequired("userAuthIdPrompt", objectType);

            if (objectType.equals("Employee")) {
                for (Employee employee : employees) {
                    if (idToAuth.equals(employee.getId())) {
                        idFound = true;
                        activeId = employee.getId();
                        pwdToAuth = employee.getEmployeePassword();
                        activeName = employee.getName();
                    }
                }
            } else if (objectType.equals("Customer")) {
                for (Customer customer : customers) {
                    if (idToAuth.equals(customer.getId())) {
                        idFound = true;
                        activeId = customer.getId();
                        pwdToAuth = customer.getCustomerPassword();
                        activeName = customer.getCustomerName();
                    }
                }
            }

            if (idFound) {
                String pwdEntered = getInput.getString("userAuthPasswordPrompt");
                if (pwdEntered.equals(String.valueOf(pwdToAuth))) {
                    userInterface.printlnInterfaceLabels("userDartWelcomeMessage",activeName);
                    if (objectType.equals("Employee")) {
                        menuSystem.employeeMenu(activeId);
                    } else {
                        menuSystem.customerMenu(activeId);
                    }
                } else {
                    userInterface.printInterfaceLabels("userAuthWrongPassword");
                    getInput.sleepMilliseconds();
                    menuSystem.mainMenu();
                }
            } else {
                userInterface.printlnInterfaceLabels("userAuthUserNotFound", objectType);
                getInput.sleepMilliseconds();
                menuSystem.mainMenu();
            }
        } catch (UUIDTypeException exception) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (StringTypeException ex) {
            userInterface.printlnInterfaceLabels("errorExceptionEmptyString", objectType + " user id");
        } catch (Exception ex) {
            userInterface.printlnInterfaceLabels("userAuthError");
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     *                   FILE OPERATIONS  METHODS START BELOW                  *
     ***************************************************************************/
    public void loadUserData(String fileName) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(getInput.pathFinder(fileName)));
            final String DELIMITER = ";";
            String line = "";

            while ((line = fileReader.readLine()) != null) {
                String[] columns = line.split(DELIMITER);
                if (columns[0].equals("Employee")) {
                    String tempId = null;
                    String tempName = null;
                    String tempPassword = null;
                    int tempBirthYear = 0;
                    String tempAddress = null;
                    double tempSalary = 0.0;

                    for (int i = 0; i < columns.length; i++) {
                        switch (i) {
                            case 1 -> tempId = columns[i];
                            case 2 -> tempName = columns[i];
                            case 3 -> tempPassword = columns[i];
                            case 4 -> tempBirthYear = Integer.parseInt(columns[i]);
                            case 5 -> tempAddress = columns[i];
                            case 6 -> tempSalary = Double.parseDouble(columns[i]);
                        }
                    }
                    if (employees.isEmpty() || !ifEmployeeExist(tempId)) {
                        employees.add(new Employee(tempId, tempName, tempPassword, tempBirthYear, tempAddress, tempSalary));
                    }
                } else if (columns[0].equals("Customer")) {
                    String tempId = null;
                    String tempName = null;
                    String tempPassword = null;
                    String tempMembership = null;
                    int tempCredit = 0;

                    for (int i = 0; i < columns.length; i++) {
                        switch (i) {
                            case 1 -> tempId = columns[i];
                            case 2 -> tempName = columns[i];
                            case 3 -> tempPassword = columns[i];
                            case 4 -> tempMembership = columns[i];
                            case 5 -> tempCredit = Integer.parseInt(columns[i]);
                        }
                    }
                    if (customers.isEmpty() || !ifCustomerExist(tempId)) {
                        switch (tempMembership) {
                            case "Regular" -> customers.add(new Regular(tempId, tempName, tempPassword, tempCredit));
                            case "Silver" -> customers.add(new Silver(tempId, tempName, tempPassword, tempCredit));
                            case "Gold" -> customers.add(new Gold(tempId, tempName, tempPassword, tempCredit));
                            case "Platinum" -> customers.add(new Platinum(tempId, tempName, tempPassword, tempCredit));
                        }
                    }
                }
            }
            displayEmployees(true);
            userInterface.printInterfaceLabels("userManagerImportSuccess", "Employee");
            userInterface.printInterfaceLabels("EOL");
            displayCustomers(true);
            userInterface.printInterfaceLabels("userManagerImportSuccess", "Customer");

            fileReader.close();
            getInput.sleepMilliseconds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUserData(String fileName) {
        try {
            final String DELIMITER = ";";
            File origFile = new File(getInput.pathFinder(fileName));
            File tempFile = new File(getInput.pathFinder("tempData"));
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(tempFile,true));

            if (fileName.equals("DartData")) {
                for (Employee employee : employees){
                    String row;
                    row = "Employee" + DELIMITER +
                            employee.getId() + DELIMITER +
                            employee.getName() + DELIMITER +
                            employee.getEmployeePassword() + DELIMITER +
                            employee.getBirthYear() + DELIMITER +
                            employee.getAddress() + DELIMITER +
                            employee.getSalary();
                    csvWriter.append(row);
                    csvWriter.newLine();
                }
                int newLine = 1;
                for (Customer customer : customers){
                    String row;
                    row = "Customer" + DELIMITER +
                            customer.getId() + DELIMITER +
                            customer.getCustomerName() + DELIMITER +
                            customer.getCustomerPassword() + DELIMITER +
                            customer.getMembershipType() + DELIMITER +
                            customer.getCustomerCredit();
                    if (newLine !=1)
                        csvWriter.newLine();

                    csvWriter.append(row);
                    newLine++;
                }
            }
            csvWriter.close();
            String origFileLocation = origFile.getAbsolutePath();
            origFile.delete();
            tempFile.renameTo(new File(origFileLocation));
            userInterface.printInterfaceLabels("userManagerExportSuccess", "Product and user");
            getInput.sleepMilliseconds();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     *                  EMPLOYEE OBJECTS METHODS START BELOW                   *
     ***************************************************************************/

    void displayEmployees(boolean skipEnter) {
        try {
            if (!employees.isEmpty()) {
                userInterface.printlnInterfaceLabels("userEmployeeListHeader");
                for (Employee worker : employees) {
                    System.out.println(worker.toString());
                }
            } else {
                userInterface.printlnInterfaceLabels("userNoListNotFound", "employees");
            }
            if (!skipEnter) {
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userEmployeeListNotFoundException");
        }
    }

    void listEmployees(boolean skipEnter) {
        try {
            for (Employee worker : employees) {
                System.out.println(worker.getId() + " : " + worker.getName());
            }
            if (!skipEnter) {
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userNoListNotFound", "employees");
        }
    }

    void addEmployee() {
        userInterface.printlnInterfaceLabels("userEmployeeNewEntry");
        String newEmployeeID = UUID.randomUUID().toString();
        userInterface.printlnInterfaceLabels("userEmployeeNewId", newEmployeeID);
        String newEmployeeName = getInput.getStringRequired("userEmployeeNewName", "Employee name");
        String newEmployeePassword = getInput.getString("userNewUserPassword", "employee");
        int newEmployeeBirthYear = getInput.getIntRequired("userEmployeeNewBirthYear");
        String nameEmployeeAddress = getInput.getString("userEmployeeNewAddress");
        double newEmployeeSalary = getInput.getDoubleRequired("userEmployeeNewSalary", "Employee salary");

        employees.add(new Employee(newEmployeeID, newEmployeeName, newEmployeePassword, newEmployeeBirthYear, nameEmployeeAddress, newEmployeeSalary));
        userInterface.printlnInterfaceLabels("userEmployeeNewSuccess");
        getInput.sleepMilliseconds();
    }

    void deleteEmployee() {
        try {
            boolean deleteProcess = false;
            int deleteIndex = -1;
            displayEmployees(true);
            String idToDelete = getInput.getStringRequired("userEmployeeDeleteId");
            if (!getInput.isUUID(idToDelete))
                throw new UUIDTypeException();

            for (int i = 0; i < employees.size(); i++) {
                if (idToDelete.equals(employees.get(i).getId())) {
                    deleteProcess = true;
                    deleteIndex = i;
                }
            }
            if (deleteProcess) {
                // Confirm to delete
                System.out.println(employees.get(deleteIndex).toString());
                String[] deleteAcceptSet = {"Y", "y", "N", "n"};
                String userInput = getInput.getMenuInput("confirmDeleteEmployee", deleteAcceptSet);

                if (userInput.toLowerCase().equals("y")) {
                    employees.remove(deleteIndex);
                    userInterface.printlnInterfaceLabels("successDeleted");
                } else {
                    userInterface.printlnInterfaceLabels("successDeleteCancelled");
                }
            } else {
                userInterface.printlnInterfaceLabels("userEmployeeDeleteIdNotFound", idToDelete);
            }
        } catch (UUIDTypeException ex) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (Exception ex) {
            userInterface.printlnInterfaceLabels("errorIdInputInvalid");
        }
        getInput.sleepMilliseconds();
    }

    public void calculateSalary() {
        try {
            if (employees.isEmpty()) {
                userInterface.printlnInterfaceLabels("userEmployeeNothingCalculate");
                getInput.sleepMilliseconds();
            } else {
                double employeeBonus;
                double employeeNetSalary = 0;
                double totalGrossSalary = 0;
                double totalNetSalary = 0;
                double totalBonus = 0;
                double totalTax = 0;

                for (Employee employee : employees) {
                    double employeeGrossSalary = employee.getSalary();
                    int employeeAge = employee.getAge();
                    totalGrossSalary = totalGrossSalary + employeeGrossSalary;
                    if ((employeeGrossSalary * 12) >= 100000) {
                        double employeeTax = employeeGrossSalary * 0.3;
                        employeeNetSalary = employeeGrossSalary - employeeTax;
                        totalTax = totalTax + employeeTax;
                    } else {
                        employeeNetSalary = employeeGrossSalary;
                    }

                    totalNetSalary = totalNetSalary + employeeNetSalary;

                    if (employeeAge < 22) {
                        employeeBonus = 4000;
                    } else if (employeeAge >= 22 && employeeAge <= 30) {
                        employeeBonus = 6000;
                    } else {
                        employeeBonus = 7500;
                    }
                    totalBonus = totalBonus + employeeBonus;
                }
                userInterface.printlnInterfaceLabels("userEmployeeMonthlyGrossSalary", String.valueOf(totalGrossSalary));
                userInterface.printlnInterfaceLabels("userEmployeeMonthlyNetSalary", String.valueOf(totalNetSalary));
                userInterface.printlnInterfaceLabels("userEmployeeMonthlyTotalBonus", String.valueOf(totalBonus));
                userInterface.printlnInterfaceLabels("userEmployeeMonthlySalaryCost", String.valueOf(totalNetSalary + totalBonus));
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception ex) {
            userInterface.printInterfaceLabels("errorExceptionGeneric", String.valueOf(ex));
        }
    }

    /***************************************************************************
     *                  CUSTOMER OBJECTS METHODS START BELOW                   *
     ***************************************************************************/

    void displayCustomers(boolean skipEnter) {
        try {
            if (!customers.isEmpty()) {
                userInterface.printlnInterfaceLabels("userCustomerListHeader");
                for (Customer customer : customers) {
                    System.out.println(customer.toString());
                }
            } else {
                userInterface.printlnInterfaceLabels("userNoListNotFound", "customers");
            }
            if (!skipEnter) {
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userListNotFoundException");
        }
    }

    void listCustomers(boolean skipEnter) {
        try {
            for (Customer customer : customers) {
                System.out.println(customer.getId() + " : " + customer.getCustomerName());
            }
            if (!skipEnter) {
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userListNotFoundException");
        }
    }

    void addCustomer() {
        userInterface.printlnInterfaceLabels("userCustomerNewEntry");
        String newCustomerID = UUID.randomUUID().toString();
        userInterface.printlnInterfaceLabels("userCustomerNewId", newCustomerID);
        String newCustomerName = getInput.getStringRequired("userCustomerNewName", "Customer name");
        String newCustomerPassword = getInput.getString("userNewUserPassword", "customer");
        customers.add(new Regular(newCustomerID, newCustomerName, newCustomerPassword));
        userInterface.printlnInterfaceLabels("userCustomerNewSuccess");
        getInput.sleepMilliseconds();
    }

    void deleteCustomer() {
        try {
            String customerId = null;
            DartController dart = new DartController();
            if (customerId == null || customerId.isEmpty()) {
                System.out.print(EOL);
                displayCustomers(true);
                customerId = getInput.getIdRequired("userCustomerDeleteId", "Customer Id");
            }

            if (!ifCustomerExist(customerId)) {
                userInterface.printlnInterfaceLabels("userCustomerIdNotFound");
            } else if (ifCustomerExist(customerId) && dart.ifCustomerHasRentals(customerId)) {
                userInterface.printlnInterfaceLabels("userCustomerHasRentals");
            } else if (ifCustomerExist(customerId) && !dart.ifCustomerHasRentals(customerId)) {
                String customerName = getCustomerDetail(customerId, "customerName");
                String customerMembership = getCustomerDetail(customerId, "membershipType");
                userInterface.printlnInterfaceLabels("userCustomerToDelete", customerId, customerName, customerMembership);
                String userInput = "";
                String[] deleteAcceptSet = {"Y", "y", "N", "n"};
                userInput = getInput.getMenuInput("userCustomerDeleteConfirm", deleteAcceptSet);

                if (userInput.toLowerCase().equals("y")) {
                    final String customerIdDelete = customerId;
                    customers.removeIf(customer -> customer.getId().equals(customerIdDelete));
                    dart.removeCustomerRentals(customerId);
                    userInterface.printlnInterfaceLabels("successDeleted");
                } else if (userInput.toLowerCase().equals("n")) {
                    userInterface.printlnInterfaceLabels("successDeleteCancelled");
                }
            }
        } catch (UUIDTypeException ex) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (StringTypeException ex) {
            userInterface.printlnInterfaceLabels("errorExceptionEmptyString", "Customer id");
        } catch(Exception ex) {
            userInterface.printInterfaceLabels("errorExceptionGeneric", String.valueOf(ex));
        }
        getInput.sleepMilliseconds();
    }
    public void upgradeCustomerView(String customerId) {
        try {
            String customerIdUpgrade = customerId;
            if (customerId == null || customerId.isEmpty()) {
                System.out.print(EOL);
                displayCustomers(true);
                customerIdUpgrade = getInput.getString("userCustomerUpgradeId");
                if (!getInput.isUUID(customerIdUpgrade))
                    throw new UUIDTypeException();
            }

            String currentMembershipType = getCustomerDetail(customerIdUpgrade, "membershipType");
            if (ifCustomerExist(customerIdUpgrade) && currentMembershipType.equals("Platinum")) {
                userInterface.printlnInterfaceLabels("userCustomerPlatinumNoUpgrade", "Customer already has");
                userInterface.printInterfaceLabels("pressEnter");
                int enter = System.in.read();
            } else if (ifCustomerExist(customerIdUpgrade) && !currentMembershipType.equals("Platinum")) {
                String newMembershipType = null;
                switch (currentMembershipType) {
                    case "Regular" -> newMembershipType = "Silver";
                    case "Silver" -> newMembershipType = "Gold";
                    case "Gold" -> newMembershipType = "Platinum";
                }
                userInterface.printlnInterfaceLabels("userCustomerUpgradeCurrentType", currentMembershipType);
                userInterface.printlnInterfaceLabels("userCustomerUpgradeNewType", newMembershipType);
                String[] upgradeAcceptSet = new String[]{"y", "Y", "n", "N"};
                String upgradeResponse = getInput.getMenuInput("userCustomerUpgradeConfirm", upgradeAcceptSet);
                if (upgradeResponse.toLowerCase().equals("y")) {
                    upgradeCustomer(customerIdUpgrade, newMembershipType);
                } else {
                    userInterface.printlnInterfaceLabels("userCustomerUpgradeRejected");
                }
            } else {
                userInterface.printlnInterfaceLabels("userCustomerIdNotFound");
            }
        } catch (UUIDTypeException ex) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (StringTypeException ex) {
            userInterface.printlnInterfaceLabels("errorExceptionEmptyString", "Customer id");
        } catch (Exception ex) {
            userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(ex));
        }
        getInput.sleepMilliseconds();
    }

    public void upgradeCustomer(String customerId, String newMembershipType) {
        String upCustomerName = null;
        String upCustomerPassword  = null;
        int upCustomerCredit = 0;
        int deleteIndex = -1;

        for (Customer customer : customers) {
            if (customerId.equals(customer.getId())) {
                deleteIndex = customers.indexOf(customer);
                upCustomerName = customer.getCustomerName();
                upCustomerPassword = customer.getCustomerPassword();
                upCustomerCredit = customer.getCustomerCredit();
            }
        }

        customers.remove(deleteIndex);
        switch (newMembershipType) {
            case "Silver" -> customers.add(new Silver(customerId, upCustomerName, upCustomerPassword, upCustomerCredit));
            case "Gold" -> customers.add(new Gold(customerId, upCustomerName, upCustomerPassword, upCustomerCredit));
            case "Platinum" -> customers.add(new Platinum(customerId, upCustomerName, upCustomerPassword, upCustomerCredit));
        }
        userInterface.printlnInterfaceLabels("userCustomerUpgradeSuccess");
    }

    public void customerUpgradeRequest(String activeId) {
        try {
            MessageController messageController = new MessageController();
            String currentMembershipType = getCustomerDetail(activeId, "membershipType");
            String nextMembershipType = null;
            userInterface.printlnInterfaceLabels("UserCurrentMembershipType", currentMembershipType);
            switch (currentMembershipType) {
                case "Regular" -> nextMembershipType = "Silver";
                case "Silver" -> nextMembershipType = "Gold";
                case "Gold" -> nextMembershipType = "Platinum";
            }

            if (currentMembershipType.equals("Platinum")) {
                userInterface.printlnInterfaceLabels("userCustomerPlatinumNoUpgrade", "You already have");
                userInterface.printlnInterfaceLabels("userCustomerPlatinumNoUpgradeCompliment");
                userInterface.printInterfaceLabels("pressEnter");
                int enter = System.in.read();
            } else {
                System.out.println("You can upgrade to: " + nextMembershipType);
                String[] requestAcceptSet = {"Y", "y", "N", "n"};
                String userInput = getInput.getMenuInput("userCustomerUpgradeRequestPrompt", requestAcceptSet);

                if (userInput.toLowerCase().equals("y")) {
                    messageController.buildMessage(activeId, "Customer", "Employee", null, "upgrade", null);
                } else {
                    userInterface.printInterfaceLabels("userCustomerUpgradeCancelled");
                }
                getInput.sleepMilliseconds();
            }
        } catch (Exception e) {
            userInterface.printInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
        }
    }

    /***************************************************************************
     *         USER VALIDATORS AND ARRAYLIST GETTER METHODS START BELOW        *
     ***************************************************************************/
    boolean ifCustomerExist(String customerId) {
        boolean ifExists = false;
        for (Customer customer : customers) {
            if (customerId.equals(customer.getId()))
                ifExists = true;
        }
        return ifExists;
    }

    boolean ifEmployeeExist(String employeeId) {
        boolean ifExists = false;
        for (Employee employee : employees) {
            if (employeeId.equals(employee.getId()))
                ifExists = true;
        }
        return ifExists;
    }

    public String getCustomerDetail(String customerId, String attributeName) {
        String customerDetail = null;
        for (Customer customer : customers) {
            if (customerId.equals(customer.getId())) {
                switch (attributeName) {
                    case "customerPassword" -> customerDetail = customer.getCustomerPassword();
                    case "customerName" -> customerDetail = customer.getCustomerName();
                    case "membershipType" -> customerDetail = customer.getMembershipType();
                }
            }
        }
        return customerDetail;
    }
    public double getCustomerDetail(String customerId, String attributeName, double somethingDouble) {
        double customerDetail = -1.0;
        for (Customer customer : customers) {
            if (customerId.equals(customer.getId())) {
                if (attributeName.equals("discountRate"))
                    customerDetail = customer.getDiscountRate();
            }
        }
        return customerDetail;
    }

    public int getCustomerDetail(String customerId, String attributeName, int somethingInt) {
        int customerDetail = 0;
        for (Customer customer : customers) {
            if (customerId.equals(customer.getId())) {
                switch (attributeName) {
                    case "customerCredit" -> customerDetail = customer.getCustomerCredit();
                    case "concurrentRentals" -> customerDetail = customer.getConcurrentRentals();
                }
            }
        }
        return customerDetail;
    }

    public void setCustomerDetail(String customerId, String attributeName, int attributeValue) {
        for (Customer customer : customers) {
            if (customerId.equals(customer.getId())) {
                if (attributeName.equals("customerCredit"))
                    customer.setCustomerCredit(attributeValue);
            }
        }
    }

    public String getEmployeeDetail(String employeeId, String attributeName) {
        String employeeDetail = null;
        for (Employee employee : employees) {
            if (employeeId.equals(employee.getId())) {
                switch (attributeName) {
                    case "employeePassword" -> employeeDetail = employee.getEmployeePassword();
                    case "employeeName" -> employeeDetail = employee.getName();
                    case "employeeAddress" -> employeeDetail = employee.getAddress();
                }
            }
        }
        return employeeDetail;
    }

    public double getEmployeeDetail(String employeeId, String attributeName, double somethingDouble) {
        double employeeDetail = -1.0;
        for (Employee employee : employees) {
            if (employeeId.equals(employee.getId())) {
                if (attributeName.equals("employeeSalary"))
                    employeeDetail = employee.getSalary();
            }
        }
        return employeeDetail;
    }

    public int getEmployeeDetail(String employeeId, String attributeName, int somethingInt) {
        int employeeDetail = 0;
        for (Employee employee : employees) {
            if (employeeId.equals(employee.getId())) {
                switch (attributeName) {
                    case "employeeBirthYear" -> employeeDetail = employee.getBirthYear();
                    case "employeeAge" -> employeeDetail = employee.getAge();
                }
            }
        }
        return employeeDetail;
    }

    public String getUserType(String userId) {
        String userType = null;
        for (Employee employee : employees) {
            if (userId.equals(employee.getId())) {
                userType = "Employee";
                break;
            }
        }
        for (Customer customer : customers) {
            if (userId.equals(customer.getId())) {
                userType = "Customer";
                break;
            }
        }
        return userType;
    }
}