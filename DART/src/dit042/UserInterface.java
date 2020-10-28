package dit042;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * User Interface class
 *
 * <p>
 *     Provides menu for each type of the users besides main menu for all
 *     Provides Hashmap dictionary style definition of through getInterfaceLabels method
 *     Facilitates all other classes with overloaded "printInterfaceLabels" method for
 *     any System.out.print() onscreen printouts including variables and end-of-line.
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public class UserInterface {
    private final Helper getInput = new Helper();
    private DartController dartController = new DartController();
    private UserController userController = new UserController();
    private MessageController messageController = new MessageController();
    private static final String EOL = System.lineSeparator();
    private boolean session = true;
    static HashMap<String, String> interfaceDictionary = new HashMap<>();

    /***************************************************************************
     *                          MAIN MENU START BELOW                          *
     ***************************************************************************/

    public void startApp() {
        try {
            System.out.println(" ");
            Thread.sleep(200);
            System.out.println("           ▓█████▄  ▄▄▄       ██▀███  ▄▄▄█████▓");
            Thread.sleep(220);
            System.out.println("           ▒██▀ ██▌▒████▄    ▓██ ▒ ██▒▓  ██▒ ▓▒");
            Thread.sleep(210);
            System.out.println("           ░██   █▌▒██  ▀█▄  ▓██ ░▄█ ▒▒ ▓██░ ▒░");
            Thread.sleep(200);
            System.out.println("           ░▓█▄   ▌░██▄▄▄▄██ ▒██▀▀█▄  ░ ▓██▓ ░");
            Thread.sleep(190);
            System.out.println("           ░▒████▓  ▓█   ▓██▒░██▓ ▒██▒  ▒██▒ ░");
            Thread.sleep(180);
            System.out.println("           ▒▒▓  ▒  ▒▒   ▓▒█░░ ▒▓ ░▒▓░  ▒ ░░");
            Thread.sleep(170);
            System.out.println("           ░ ▒  ▒   ▒   ▒▒ ░  ░▒ ░ ▒░    ░");
            Thread.sleep(160);
            System.out.println("           ░ ░  ░   ░   ▒     ░░   ░   ░");
            Thread.sleep(150);
            System.out.println("             ░          ░  ░   ░");
            Thread.sleep(150);
            System.out.println("           ░");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainMenu();
        }
        mainMenu();
    }

    public void mainMenu() {
        try {
            do {
                System.out.println(EOL + " ------------------------------------------------------- ");
                System.out.println("|   Welcome to DART, your good old game rental system.  |" + EOL +
                                   "|        The competition has no steam to keep up!       |");
                System.out.println("|-------------------------------------------------------|");
                System.out.println("| << Enter \"M\" for Manager  >>                          |");
                System.out.println("| << Enter \"E\" for Employee  >>                         |");
                System.out.println("| << Enter \"C\" for Customer  >>                         |");
                System.out.println("|-------------------------------------------------------|");
                System.out.println("| << Enter \"X\" to exit system  >>                       |");
                System.out.println(" -------------------------------------------------------");

                String[] menuAcceptSet = new String[]{"M", "m", "E", "e", "C", "c", "X", "x"}; // Accepted responses for menu options
                String userInput = getInput.getMenuInput("menuOptionPrompt", menuAcceptSet); // Calling Helper method

                switch (userInput.toLowerCase()) {
                    case "m" -> userController.authenticateManager();
                    case "e" -> userController.authenticateUser("Employee");
                    case "c" -> userController.authenticateUser("Customer");
                    case "x" -> {
                        printlnInterfaceLabels("dartSystemExit");
                        getInput.closeScanner();
                        session = false;
                    }
                    default -> printlnInterfaceLabels("menuOptionNoMatch");
                }
            } while (session);

            System.exit(0);

        } catch (Exception e) {
            printlnInterfaceLabels("errorExceptionMenu", String.valueOf(e));
        }
    }

    /***************************************************************************
     *                        MANAGER MENU START BELOW                         *
     ***************************************************************************/
    public void managerMenu() {
        try {
            do {
                System.out.println(EOL + " ---------------------------------------------------");
                System.out.println("| Manager Screen - Type one of the options below:   |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 1. Add an employee                                |");
                System.out.println("| 2. Remove an employee                             |");
                System.out.println("| 3. View all employees                             |");
                System.out.println("| 4. View employees' total salary cost              |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 5. View total rent profit                         |");
                System.out.println("| 6. View most profitable item                      |");
                System.out.println("| 7. View most frequent rented item                 |");
                System.out.println("| 8. View best customer                             |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 9. Load user, product data file                   |");
                System.out.println("| 10. Load rental history file                      |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 11. Save DART data file                           |");
                System.out.println("| 12. Save Rental history file                      |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 13. Return to Main Menu                           |");
                System.out.println(" ---------------------------------------------------");
                String[] menuAcceptSet = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"}; // Accepted responses for menu options
                String userInput = getInput.getMenuInput("menuOptionPrompt", menuAcceptSet); // Calling Helper method

                switch (userInput.toLowerCase()) {
                    case "1" -> userController.addEmployee();
                    case "2" -> userController.deleteEmployee();
                    case "3" -> userController.displayEmployees(false);
                    case "4" -> userController.calculateSalary();
                    case "5" -> dartController.viewRentalTotalProfit();
                    case "6" -> dartController.viewRentalProfitable();
                    case "7" -> dartController.viewRentalFrequency();
                    case "8" -> dartController.viewRentalBestCustomer();
                    case "9" -> dartController.loadProductData();
                    case "10" -> dartController.loadRentalData();
                    case "11" -> dartController.saveProductData();
                    case "12" -> dartController.saveRentalData();
                    case "13" -> mainMenu();

                    default -> printlnInterfaceLabels("menuOptionNoMatch");
                }
            } while (session);
        } catch (Exception e) {
            printlnInterfaceLabels("errorExceptionMenu", String.valueOf(e));
        }
    }

    /***************************************************************************
     *                        EMPLOYEE MENU START BELOW                        *
     ***************************************************************************/
    public void employeeMenu(String activeId) {
        try {
            do {
                System.out.println(EOL + " ---------------------------------------------------");
                System.out.println("| Employee Screen - Type one of the options below:  |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 1. Register a game                                |");
                System.out.println("| 2. Remove a game                                  |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 3. Register an album                              |");
                System.out.println("| 4. Remove an album                                |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 5. Register a customer                            |");
                System.out.println("| 6. Upgrade a customer                             |");
                System.out.println("| 7. Remove a customer                              |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 8. Return rented item                             |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 9. View all games and albums (sorted)             |");
                System.out.println("| 10. View all customers                            |");
                System.out.println("| 11. View all rentals and history                  |");
                System.out.println("| 12. View total rent profit                        |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 13. Send message to customer                      |");
                System.out.println("| 14. Send message to employee                      |");
                System.out.println("| 15. Read my messages " + messageController.checkNewMsg(activeId, "Employee") + "            |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 16. Return to Main Menu                           |");
                System.out.println(" ---------------------------------------------------");
                String[] menuAcceptSet = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"}; // Accepted responses for menu options
                String userInput = getInput.getMenuInput("menuOptionPrompt", menuAcceptSet); // Calling Helper method
                switch (userInput.toLowerCase()) {
                    case "1" -> dartController.addGame();
                    case "2" -> dartController.deleteProduct("Game");
                    case "3" -> dartController.addAlbum();
                    case "4" -> dartController.deleteProduct("Album");
                    case "5" -> userController.addCustomer();
                    case "6" -> userController.upgradeCustomerView(null);
                    case "7" -> userController.deleteCustomer();
                    case "8" -> dartController.returnRental();
                    case "9" -> productSortView(activeId);
                    case "10" -> userController.displayCustomers(false);
                    case "11" -> dartController.viewCurrentRentals();
                    case "12" -> dartController.viewRentalTotalProfit();
                    case "13" -> messageController.buildMessage(activeId, "Employee", "Customer", null, "message", null);
                    case "14" -> messageController.buildMessage(activeId, "Employee", "Employee", null, "message", null);
                    case "15" -> messageController.openInbox(activeId, "Employee");
                    case "16" -> mainMenu();
                    default -> printlnInterfaceLabels("menuOptionNoMatch");
                }
            } while (session);
        } catch (Exception e) {
            printlnInterfaceLabels("errorExceptionMenu", String.valueOf(e));
        }
    }

    /***************************************************************************
     *                  CUSTOMER MENU AND VIEWS START BELOW                    *
     ***************************************************************************/
    public void customerMenu(String activeId) {
        try {
            do {
                System.out.println(EOL + " ---------------------------------------------------");
                System.out.println("| Customer Screen - Type one of the options below:  |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 1. View games and albums                          |");
                System.out.println("|     - Sorted by Ratings (Best to worst)           |");
                System.out.println("|     - Sorted by Album Year (Most recent)          |");
                System.out.println("|     - Sorted by Title (A-Z)                       |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 2. Search game by Genre                           |");
                System.out.println("| 3. Search album by Year                           |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 4. Rent a game                                    |");
                System.out.println("| 5. Rent an album                                  |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 6. Send message to customer                       |");
                System.out.println("| 7. Send message to employee                       |");
                System.out.println("| 8. Read my messages " + messageController.checkNewMsg(activeId, "Customer") + "             |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 9. Request membership upgrade                     |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 10. Return to Main Menu                           |");
                System.out.println(" ---------------------------------------------------");

                String[] menuAcceptSet = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}; // Accepted responses for menu options
                String userInput = getInput.getMenuInput("menuOptionPrompt", menuAcceptSet); // Calling Helper method
                switch (userInput.toLowerCase()) {
                    case "1" -> productSortView(activeId);
                    case "2" -> dartController.searchProduct("Game");
                    case "3" -> dartController.searchProduct("Album");
                    case "4" -> dartController.checkRental(activeId, "Game");
                    case "5" -> dartController.checkRental(activeId, "Album");
                    case "6" -> messageController.buildMessage(activeId, "Customer", "Customer", null, "message", null);
                    case "7" -> messageController.buildMessage(activeId, "Customer", "Employee", null, "message", null);
                    case "8" -> messageController.openInbox(activeId, "Customer");
                    case "9" -> userController.customerUpgradeRequest(activeId);
                    case "10" -> mainMenu();
                    default -> printlnInterfaceLabels("menuOptionNoMatch");
                }
            } while (session);
        } catch (Exception e) {
            printlnInterfaceLabels("errorExceptionMenu", String.valueOf(e));
        }
    }

    /***************************************************************************
     *                   PRODUCT SORTED MENU START BELOW                       *
     ***************************************************************************/
    public void productSortView(String activeId) {
        try {
            do {
                System.out.println(EOL + " ---------------------------------------------------");
                System.out.println("| Product lists - Type one of the options below:    |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 1. View games - Sorted by Title (A-Z)             |");
                System.out.println("| 2. View games - Sorted by Rating (5-0)            |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 3. View albums - Sorted by Title (A-Z)            |");
                System.out.println("| 4. View albums - Sorted by Rating (5-0)           |");
                System.out.println("| 5. View albums - Sorted by Year (Latest first)    |");
                System.out.println("|---------------------------------------------------|");
                System.out.println("| 6. Return to Previous Menu                        |");
                System.out.println(" ---------------------------------------------------");

                String[]menuAcceptSet = new String[]{"1", "2", "3", "4", "5", "6", "7",}; // Accepted responses for menu options
                String userInput = getInput.getMenuInput("menuOptionPrompt", menuAcceptSet); // Calling Helper method
                switch (userInput.toLowerCase()) {
                    case "1" -> dartController.productViews("game", "title");
                    case "2" -> dartController.productViews("game", "rating");
                    case "3" -> dartController.productViews("album", "title");
                    case "4" -> dartController.productViews("album", "rating");
                    case "5" -> dartController.productViews("album", "year");
                    case "6" -> {
                        System.out.println(EOL);
                        String userType = userController.getUserType(activeId);
                        if (userType.equals("Employee"))
                            employeeMenu(activeId);
                        else if (userType.equals("Customer"))
                            customerMenu(activeId);
                        else {
                            mainMenu();
                        }
                    }
                    default -> printlnInterfaceLabels("menuOptionNoMatch");
                }
            } while (session);
        } catch (Exception e) {
            printlnInterfaceLabels("errorExceptionMenu", String.valueOf(e));
        }
    }

    /***************************************************************************
     *                USER INTERFACE PRINT METHODS START BELOW                 *
     ***************************************************************************/
    public String getInterfaceLabels(String textLabel) {
        String labelString = null;
        try {
            labelString = interfaceDictionary.get(textLabel);
        } catch (Exception ex) {
            System.out.println("Error retrieving customer label - HashMap");
        }
        return labelString;
    }

    public String getInterfaceLabels(String textLabel, String labelSupplement) {
        String labelString = null;
        try {
            labelString = interfaceDictionary.get(textLabel);
            labelString = labelString.replaceAll(Pattern.quote("%EOL%"), EOL);
            labelString = labelString.replaceAll(Pattern.quote("%S1%"), labelSupplement);
        } catch (Exception ex) {
            printlnInterfaceLabels("errorExceptionHashmapUI", String.valueOf(ex));
        }
        return labelString;
    }

    public void printInterfaceLabels(String textLabel) {
        try {
            String printString;
            printString = getInterfaceLabels(textLabel);
            printString = printString.replaceAll(Pattern.quote("%EOL%"), EOL);
            System.out.print(printString);
        } catch (Exception ex) {
            printlnInterfaceLabels("errorExceptionUIPrint", String.valueOf(ex));
        }
    }

    public void printlnInterfaceLabels(String textLabel) {
        try {
            String printString;
            printString = getInterfaceLabels(textLabel);
            printString = printString.replaceAll(Pattern.quote("%EOL%"), EOL);
            System.out.println(printString);
        } catch (Exception ex) {
            printlnInterfaceLabels("errorExceptionUIPrint", String.valueOf(ex));
        }
    }

    public void printInterfaceLabels(String textLabel, String ...variablesAsString) {
        try {
            String printString;
            printString = getInterfaceLabels(textLabel);
            printString = printString.replaceAll(Pattern.quote("%EOL%"), EOL);
            int s = 1;
            for (String var : variablesAsString) {
                String pattern = "%S" + s + "%";
                printString = printString.replaceAll(Pattern.quote(pattern), var);
                s++;
            }
            System.out.print(printString);
        } catch (Exception ex) {
            printlnInterfaceLabels("errorExceptionUIPrint", String.valueOf(ex));
        }
    }

    public void printlnInterfaceLabels(String textLabel, String ...variablesAsString) {
        try {
            String printString;
            printString = getInterfaceLabels(textLabel);
            printString = printString.replaceAll(Pattern.quote("%EOL%"), EOL);
            int s = 1;
            for (String var : variablesAsString) {
                String pattern = "%S" + s + "%";
                printString = printString.replaceAll(Pattern.quote(pattern), var);
                s++;
            }
            System.out.println(printString);
        } catch (Exception ex) {
            printlnInterfaceLabels("errorExceptionUIPrint", String.valueOf(ex));
        }
    }

    /***************************************************************************
     *              USER INTERFACE DICTIONARY LIST START BELOW                 *
     ***************************************************************************/
    static {
        // Generic messages and prompts
        interfaceDictionary.put("pressEnter", "Press Enter to continue ...");
        interfaceDictionary.put("confirmDeleteEmployee", "%EOL%Are you sure to delete this employee (Y/N): ");
        interfaceDictionary.put("successDeleted", "Successfully deleted.");
        interfaceDictionary.put("successDeleteCancelled", "Deletion cancelled.");
        interfaceDictionary.put("dartSeparatorLine", "-----------------------------------------------");
        interfaceDictionary.put("dartSystemExit", "Exiting system ...");
        interfaceDictionary.put("EOL", "%EOL%");

        // Manager section text
        interfaceDictionary.put("userDartWelcomeManager", "%EOL%Welcome back to DART, Manager");
        interfaceDictionary.put("userManagerPasswordPrompt", "%EOL%Please enter your manager password: ");
        interfaceDictionary.put("userManagerImportSuccess", "%S1% list has been successfully loaded. Duplicates skipped if any. %EOL%");
        interfaceDictionary.put("userManagerExportSuccess", "%S1% list has been successfully saved. %EOL%");

        // User Controller text - Authentication
        interfaceDictionary.put("userAuthIdPrompt", "%EOL%Please enter your %S1% ID: ");
        interfaceDictionary.put("userAuthPasswordPrompt", "Please enter your password: ");
        interfaceDictionary.put("userDartWelcomeMessage", "%EOL%Welcome back to DART, %S1%");
        interfaceDictionary.put("userAuthWrongPassword", " *** Invalid password *** ");
        interfaceDictionary.put("userAuthError", "Error in User authentication.");
        interfaceDictionary.put("userAuthUserNotFound", "Could not find %S1% ID. Please check Id and try again.%EOL%");

        // Menu prompts and text
        interfaceDictionary.put("menuOptionPrompt", "Please enter your choice: ");
        interfaceDictionary.put("menuOptionNoMatch", "No match in Menu selection - Please check acceptSet.");

        // User Controller text - Employees
        interfaceDictionary.put("userEmployeeListHeader", "All employees:");
        interfaceDictionary.put("userIdPrompt", "%EOL%Please enter %S1% ID: ");
        interfaceDictionary.put("userEmployeeNewEntry", "Creating an employee. Please type the Employee’s:");
        interfaceDictionary.put("userEmployeeNewId", "New employee ID: %S1%");
        interfaceDictionary.put("userEmployeeNewName", "Employee name (required): ");
        interfaceDictionary.put("userNewUserPassword", "New %S1% password: ");
        interfaceDictionary.put("userEmployeeNewBirthYear", "Birth year (required): ");
        interfaceDictionary.put("userEmployeeNewAddress", "Address: ");
        interfaceDictionary.put("userEmployeeNewSalary", "Monthly gross salary (required): ");
        interfaceDictionary.put("userEmployeeNewSuccess", "New employee added successfully.");
        interfaceDictionary.put("userEmployeeDeleteId", "%EOL%Please enter Employee Id to delete: ");
        interfaceDictionary.put("userEmployeeDeleteIdNotFound", "Employee with id %S1% not found.");
        interfaceDictionary.put("userEmployeeMonthlyGrossSalary", "Total monthly gross salary: %S1% SEK");
        interfaceDictionary.put("userEmployeeMonthlyNetSalary", "Total monthly net salary: %S1% SEK");
        interfaceDictionary.put("userEmployeeMonthlyTotalBonus", "Total monthly bonus: %S1% SEK");
        interfaceDictionary.put("userEmployeeMonthlySalaryCost", "Total monthly salary cost: %S1% SEK");
        interfaceDictionary.put("userEmployeeNothingCalculate", "No employee found in the list to calculate salary.");

        // User Controller text - Customer
        interfaceDictionary.put("userCustomerListHeader", "All customers:");
        interfaceDictionary.put("UserCurrentMembershipType", "Your current membership type: %S1%");
        interfaceDictionary.put("userCustomerNewEntry", "Creating a customer. Please type the customer’s:");
        interfaceDictionary.put("userCustomerNewId", "New customer ID: %S1%");
        interfaceDictionary.put("userCustomerNewName", "Customer name (required): ");
        interfaceDictionary.put("userCustomerNewPassword", "Customer password (required): ");
        interfaceDictionary.put("userCustomerNewSuccess", "New customer added successfully.");
        interfaceDictionary.put("userCustomerUpgradeSuccess", "Customer membership successfully upgraded.");
        interfaceDictionary.put("userCustomerDeleteId", "Please enter Customer Id to delete: ");
        interfaceDictionary.put("userCustomerToDelete", "%S1% : %S2% (%S3%)");
        interfaceDictionary.put("userCustomerDeleteConfirm", "%EOL%All customer information and related rental history will be deleted.%EOL%Are you sure to delete this customer (Y/N): ");
        interfaceDictionary.put("userCustomerHasRentals", "%EOL%Customer has active rentals.%EOL%Please return items before removing the customer.");
        interfaceDictionary.put("userCustomerIdNotFound", "Customer Id is not found. Please try again.");
        interfaceDictionary.put("userCustomerUpgradeId", "Please enter Customer Id to upgrade: ");
        interfaceDictionary.put("userCustomerUpgradeCurrentType", "Current membership: %S1%");
        interfaceDictionary.put("userCustomerUpgradeNewType", "Upgraded membership: %S1%");
        interfaceDictionary.put("userCustomerUpgradeConfirm", "Press Y to approve, N to reject (Y/N): ");
        interfaceDictionary.put("userCustomerPlatinumNoUpgrade", "%EOL%%S1% the highest membership level that we offer.");
        interfaceDictionary.put("userCustomerPlatinumNoUpgradeCompliment", "Thank you for being our valuable customer.%EOL%");
        interfaceDictionary.put("userCustomerUpgradeTo", "You can upgrade to: %S1%");
        interfaceDictionary.put("userCustomerUpgradeRequestPrompt", "Would you like to apply for upgrade (Y/N): ");
        interfaceDictionary.put("userCustomerUpgradeCancelled", "Upgrade cancelled.");
        interfaceDictionary.put("userCustomerUpgradeRejected", "Upgrade rejected.");

        // Dart Controller, products
        interfaceDictionary.put("dartGameListHeader", "All games:");
        interfaceDictionary.put("dartAlbumListHeader", "All albums:");
        interfaceDictionary.put("dartProductDeleteId", "%EOL%Please enter %S1% Id to delete: ");
        interfaceDictionary.put("dartProductDeleteConfirmPrompt", "Are you sure to delete this %S1%? (Y/N): ");
        interfaceDictionary.put("dartProductDeleteIdNotFound", "%S1% with id %S2% not found.");
        interfaceDictionary.put("dartProductNewEntryGame", "Creating a Game. Please type the game’s:");
        interfaceDictionary.put("dartProductNewID", "New %S1% Id: %S2%");
        interfaceDictionary.put("dartProductNewTitle", "%S1% title (required): ");
        interfaceDictionary.put("dartProductNewYear", "%S1% year (required): ");
        interfaceDictionary.put("dartProductNewGenre", "Game genre (required): ");
        interfaceDictionary.put("dartProductNewDailyRent", "Daily Rent Fee (required): ");
        interfaceDictionary.put("dartProductNewSuccess", "%S1% added successfully.");
        interfaceDictionary.put("dartProductNewEntryAlbum", "Creating an Album. Please type the album’s:");
        interfaceDictionary.put("dartProductNewAlbumArtist", "Artist (required): ");
        interfaceDictionary.put("dartProductSearch", "%EOL%Please enter %S1% to search for: ");
        interfaceDictionary.put("dartProductSearchNoResult", "No results found.");
        interfaceDictionary.put("dartProductDeleteRented", "Product is already rented. %EOL%We cannot delete a rented product. Please return before deleting from system.");

        // Rental section
        interfaceDictionary.put("dartRentalMemberLimit", "Your membership type \"%S1%\" doesn't allow any more rentals. %EOL%Please return some of current rentals. Thank you.");
        interfaceDictionary.put("dartRentalUpgradePrompt", "Would you like to upgrade your membership? (Y/N): ");
        interfaceDictionary.put("dartRentalCancelled", "Rental cancelled.");
        interfaceDictionary.put("dartRentalItemID", "%EOL%Please enter %S1% ID to check for rent: ");
        interfaceDictionary.put("dartRentalProductFound", "%S1% found: ");
        interfaceDictionary.put("dartRentalConfirmPrompt", "Would you like to rent it? (Y/N): ");
        interfaceDictionary.put("dartRentalSuccess", "%S1% is rented now and added to your account. Thank you.");
        interfaceDictionary.put("dartRentalAlreadyRented", "%S1% with id %S2% is already rented");
        interfaceDictionary.put("dartRentalRequestPrompt", "Would you to like to request this item? (Y/N): ");
        interfaceDictionary.put("dartRentalCurrentItems", "%EOL%Current items rented under customer account:");
        interfaceDictionary.put("dartRentalCurrentItemsGameList", "  Game Id:  %S1%  |  Game title: %S2%");
        interfaceDictionary.put("dartRentalCurrentItemsAlbumList", "  Album Id:  %S1%  |  Album title: %S2%");
        interfaceDictionary.put("dartRentalReturnItemPrompt", "Please enter product ID to return: ");
        interfaceDictionary.put("dartRentalReturnItemId", "%EOL%Rented product id: %S1%");
        interfaceDictionary.put("dartRentalNoActiveRental", "This customer do not have any active rentals.");
        interfaceDictionary.put("dartRentalItemAlreadyReturned", "This item is already returned.");
        interfaceDictionary.put("dartRentalReturnTypeTitle", "%S1%: %S2%");
        interfaceDictionary.put("dartRentalReturnDates", "Date rented:  %S1% %EOL%Date returned:  %S2%");
        interfaceDictionary.put("dartRentalReturnDaysRate", "Total days rented: %S1% %EOL%Daily rate: %S2% SEK");
        interfaceDictionary.put("dartRentalReturnFreeMessage", "You have used 5 credits from your account.%EOL%This rental is free of charge.");
        interfaceDictionary.put("dartRentalTotalBeforeDiscount", "Total before discount is: %S1% SEK.");
        interfaceDictionary.put("dartRentalReturnDiscount", "Your discount is: %S1% SEK.");
        interfaceDictionary.put("dartRentalReturnTotal", "Rent total to pay is: %S1% SEK.%EOL%");
        interfaceDictionary.put("dartRentalReturnCreditsDisplay", "You now have %S1% %S2%");
        interfaceDictionary.put("dartRentalReturnHooray", "Hooray, your next rental is FREE!");
        interfaceDictionary.put("dartRentalRatingPrompt", "Would you like to rate the %S1% you returned? (Y/N): ");
        interfaceDictionary.put("dartRentalRatingInput", "Please enter your rating 0-5 (Worst 0 - Best 5): ");
        interfaceDictionary.put("dartRentalReviewInput", "Please enter your review (up to 100 words): ");
        interfaceDictionary.put("dartRentalComplete", "Rental return is complete. Thank you.");
        interfaceDictionary.put("dartRentalCustomerNoItemFound", "Sorry. We could not find a rented %S1% under this Customer ID.%EOL%Please check the item ID and try again.");
        interfaceDictionary.put("dartRentalNoItemFound", "Sorry. We could not the product with this Id. Please try again.");
        interfaceDictionary.put("dartRentalNoTransactions", "No returned items found to calculate rental income.");
        interfaceDictionary.put("dartRentalNoTransactionsFrequency", "No returned items found to calculate rental frequency.");
        interfaceDictionary.put("dartRentalMostItem", "The most %S1% item is: %EOL%");
        interfaceDictionary.put("dartRentalBestCustomer", "The best customer is: %EOL%");
        interfaceDictionary.put("dartRentalMostItemIdTitle", "%S1%: %S2% (%S3%)");
        interfaceDictionary.put("dartRentalShopTotalIncome", "Total rental profit: %S1% SEK. %EOL%");
        interfaceDictionary.put("dartRentalMostTotalIncome", " | Total income: %S1% SEK.");
        interfaceDictionary.put("dartRentalMostFrequencyNumber", " | Number of rental: %S1%");
        interfaceDictionary.put("dartRentalBestCustomerDisplay", "%S1% (%S2%) | Total rental paid: %S3% SEK.");

        // Message controller texts
        interfaceDictionary.put("messageNoMessages", "(No messages)    ");
        interfaceDictionary.put("messageInboxNoMessages", "You have no new messages.");
        interfaceDictionary.put("messageInboxYouHaveOneMsg", "You have 1 new message.");
        interfaceDictionary.put("messageInboxYouHaveManyMsg", "You have %S1% new messages.");
        interfaceDictionary.put("messageInboxCommandsList", "\"M\" - Read messages, \"D\" - Delete a message or \"B\" - Back to menu: ");
        interfaceDictionary.put("messageInboxDeleteIdPrompt", "Enter message Id to delete: ");
        interfaceDictionary.put("messageInboxDeleteIdMismatch", "Invalid message Id. Please try again.%EOL%");
        interfaceDictionary.put("messageInboxMsgDeleted", "Message deleted. ");
        interfaceDictionary.put("messageOneMessage", "(1 new message)  ");
        interfaceDictionary.put("messageSingleDigitMessage", "(%S1% new messages) ");
        interfaceDictionary.put("messageDoubleDigitMessage", "(%S1% new messages)");
        interfaceDictionary.put("messageInboxSeparator", "---------------------------------------------------------------");
        interfaceDictionary.put("messageMessageRecipientIdPrompt", "%EOL%Please enter %S1% ID to send to: ");
        interfaceDictionary.put("messageManyMessages", "Rental return is complete. Thank you.");
        interfaceDictionary.put("messageMessageSendSuccess", "Message sent successfully.");
        interfaceDictionary.put("messageMessageSendCancelled", "Message cancelled.");
        interfaceDictionary.put("messageSingleMessageSender", "Sender: %S1% (%S2%)");
        interfaceDictionary.put("messageSingleMessageRecipient", "Recipient: %S1% (%S2%)");
        interfaceDictionary.put("messageSingleMessageDate", "Date: %S1%");
        interfaceDictionary.put("messageSingleMessageTime", "Time: %S1%");
        interfaceDictionary.put("messageSingleMessageSubject", "  Subject: %S1% %EOL%");
        interfaceDictionary.put("messageSingleMessageBodyHeader", "  Message: ");
        interfaceDictionary.put("messageSingleMessageItemId", "Item ID requested: %S1%");
        interfaceDictionary.put("messageRequestRentalProgress", "Sending rental request...");
        interfaceDictionary.put("messageRequestUpgradeProgress", "Sending upgrade request...");
        interfaceDictionary.put("messageRecipientNotFound", "Sorry. Could not find %S1%.%EOL%Please check the ID number and try again.");
        interfaceDictionary.put("messageRequestSubject", "Rental request");
        interfaceDictionary.put("messageRequestBody", "I would like to rent this item when you return. Thank you.");
        interfaceDictionary.put("messageRequestSuccess", "Request sent to customer who rented the product.");
        interfaceDictionary.put("messageUpgradeSubject", "Membership upgrade request");
        interfaceDictionary.put("messageUpgradeBody", "I would like to upgrade my membership. Thank you.");
        interfaceDictionary.put("messageUpgradeSuccess", "Upgrade request sent successfully.");
        interfaceDictionary.put("messageNewMsgHeader", "Please type your new message below:");
        interfaceDictionary.put("messageNewMsgSubject", "Subject: ");
        interfaceDictionary.put("messageNewMsgBody", "Please type your message: %EOL%");
        interfaceDictionary.put("messageNewMsgSendPrompt", "Press S to send your message, press C to cancel the message (S/C): ");

        // Helper Class, generic error messages
        interfaceDictionary.put("helperValidationInvalidInput", "Invalid input. Please try again.");
        interfaceDictionary.put("helperValidationIntegerOnly", "Please enter Integer number only.");
        interfaceDictionary.put("helperValidationErrorEmpty", "This field cannot be empty. Please try again.");
        interfaceDictionary.put("helperValidationErrorEmptyText", "This field cannot be empty, text or negative number. Please try again.");
        interfaceDictionary.put("helperSleeperError", "Could not sleep. Please check the error.");
        interfaceDictionary.put("userNoListNotFound", "%EOL%No %S1% to list");
        interfaceDictionary.put("dartRentalItemTypeSwitchError", "Error in itemType - Rental");
        interfaceDictionary.put("UUIDInvalidFormat", "%EOL%The Id number is not a valid UUID format (e.g. 123e4567-e89b-12d3-a456-426614174000)");

        // Exception Errors
        interfaceDictionary.put("errorExceptionGeneric", "Exception error: %S1%");
        interfaceDictionary.put("errorExceptionMenu", "Exception error in menu: %S1%");
        interfaceDictionary.put("errorIdInputInvalid", "Id input is invalid. Please try again.");
        interfaceDictionary.put("errorFileNotFound", "File not found.");
        interfaceDictionary.put("errorFileIOError", "FileIO error. Please check.");
        interfaceDictionary.put("errorCustomerDeleteException", "Error in customer delete method. Please try again.");
        interfaceDictionary.put("userListNotFoundException", "Not printing list. Error: %S1%");
        interfaceDictionary.put("errorExceptionHashmapUI", "Error retrieving customer label - HashMap");
        interfaceDictionary.put("errorExceptionUIPrint", "Error UI printing label - check the map");
        interfaceDictionary.put("exceptionProductDetailException", "Create a new product: ");
        interfaceDictionary.put("exceptionInputTypeException", "Invalid data. %S1% ");
        interfaceDictionary.put("exceptionRentalNegativeException", "Invalid operation. Upon returning the item, the %S1%");
        interfaceDictionary.put("errorExceptionGenericThisField", "Invalid data. ");
        interfaceDictionary.put("errorExceptionEmptyString", "Invalid data. %S1% cannot be empty.");
        interfaceDictionary.put("errorExceptionNegativeNumber", "Invalid data. %S1% cannot be negative.");
        interfaceDictionary.put("errorExceptionRentalNegativeNumber", " must be positive. %EOL%Please check the system date configuration and try again.");
    }
}