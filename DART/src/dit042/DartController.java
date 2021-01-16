package dit042;

import dit042.exceptions.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toList;

/**
 * DartController controller class for Product objects and Rental objects
 *
 * <p>
 *     Maintains arraylist for all products and rental transactions, provide methods for behaviours.
 *     Loads and saves products and rentals arraylist from/to DartData.txt and RentalHistory.txt files.
 *
 * @param   "fileName"
 *          in Helper class expects the above files in ...src/dit042/resources folder.
 *          Please check if files exist physically. Helper class searches few possible
 *          options, but it's not smart enough yet. Thanks.
 *
 *      Uses UserInterface class for onscreen printouts.
 *      Uses Streams API to consolidate Rentals arraylist to calculate total rental income and rental
 *      frequency for each unique products, and accumulated rental income per customer.
 *
 *      Rental days are calculated automatically via LocalDateTime as VG feature and does not require user
 *      input. The same day rental is counted as 1 day. Negative rental days throws a customer exception.
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public class DartController {

    private UserController userController = new UserController();
    private MessageController messageController = new MessageController();
    private static UserInterface userInterface = new UserInterface();
    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<Rental> rentals = new ArrayList<>();
    protected static Helper getInput = new Helper();

    public void loadProductData() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(getInput.pathFinder("DartData")))) {
            final String DELIMITER = ";";
            String line = "";

            while ((line = fileReader.readLine()) != null) {
                String[] columns = line.split(DELIMITER);
                if (columns[0].equals("Game")) {
                    String tempId = null;
                    String tempName = null;
                    String tempGenre = null;
                    double tempDailyRent = 0;
                    boolean tempIsRented = false;

                    for (int i = 0; i < columns.length; i++) {
                        switch (i) {
                            case 1 -> tempId = columns[i];
                            case 2 -> tempName = columns[i];
                            case 3 -> tempGenre = columns[i];
                            case 4 -> tempDailyRent = Double.parseDouble(columns[i]);
                            case 5 -> tempIsRented = Boolean.parseBoolean(columns[i]);
                        }
                    }
                    if ((products != null && !ifProductExist(tempId)) || products == null) {
                        products.add(new Game(tempId, tempName, tempGenre, tempDailyRent, tempIsRented));
                    }
                } else if (columns[0].equals("Album")) {
                    String tempId = null;
                    String tempName = null;
                    String tempArtist = null;
                    int tempYear = 0;
                    double tempDailyRent = 0;
                    boolean tempIsRented = false;

                    for (int i = 0; i < columns.length; i++) {
                        switch (i) {
                            case 1 -> tempId = columns[i];
                            case 2 -> tempName = columns[i];
                            case 3 -> tempArtist = columns[i];
                            case 4 -> tempYear = Integer.parseInt(columns[i]);
                            case 5 -> tempDailyRent = Double.parseDouble(columns[i]);
                            case 6 -> tempIsRented = Boolean.parseBoolean(columns[i]);
                        }
                    }
                    if ((products != null && !ifProductExist(tempId)) || products == null) {
                        products.add(new Album(tempId, tempName, tempArtist, tempYear, tempDailyRent, tempIsRented));
                    }
                }
            }
            displayGames(true);
            userInterface.printInterfaceLabels("userManagerImportSuccess", "Game");
            userInterface.printInterfaceLabels("EOL");
            displayAlbums(true);
            userInterface.printInterfaceLabels("userManagerImportSuccess", "Album");
            getInput.sleepMilliseconds();
            userInterface.printInterfaceLabels("EOL");
            userController.loadUserData("DartData");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRentalData() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(getInput.pathFinder("RentalHistory")))) {
            final String DELIMITER = ";";
            String line = null;
            while ( (line = fileReader.readLine()) != null) {
                String tempId = null;
                String tempItemId = null;
                String tempCustomerId = null;
                LocalDate tempRentalDate = null;
                LocalDate tempReturnDate = null;
                double tempRentalIncome = 0.0;
                double tempRating = 0.0;
                String tempReview = null;
                boolean tempIsReturned = false;

                String[] columns = line.split(DELIMITER);
                for (int i = 0; i < columns.length; i++) {
                    switch (i) {
                        case 0 -> tempId = columns[i];
                        case 1 -> tempItemId = columns[i];
                        case 2 -> tempCustomerId = columns[i];
                        case 3 -> tempRentalDate = LocalDate.parse(columns[i]);
                        case 4 -> tempReturnDate = LocalDate.parse(columns[i]);
                        case 5 -> tempRentalIncome = Double.parseDouble(columns[i]);
                        case 6 -> tempRating = Double.parseDouble(columns[i]);
                        case 7 -> tempReview = columns[i];
                        case 8 -> tempIsReturned = Boolean.parseBoolean(columns[i]);
                    }
                }
                if ((!ifRentalExist(tempId)) || rentals == null || rentals.isEmpty()) {
                    rentals.add(new Rental(tempId, tempItemId, tempCustomerId, tempRentalDate, tempReturnDate, tempRentalIncome, tempRating, tempReview, tempIsReturned));
                }
            }
            displayRentals(true);
            userInterface.printInterfaceLabels("userManagerImportSuccess", "Rental history");
            getInput.sleepMilliseconds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProductData() {
        try {
            if (products.isEmpty()) {
                System.out.println("No product or user list to save. Please load existing data first or add new products.");
                getInput.sleepMilliseconds();
            } else {
                final String DELIMITER = ";";
                String line = "";
                File origFile = new File(getInput.pathFinder("DartData"));
                File tempFile = new File(getInput.pathFinder("tempData"));
                BufferedWriter csvWriter = new BufferedWriter(new FileWriter(tempFile));
                for (Product product : products) {
                    String row = null;
                    if (product instanceof Game) {
                        row = "Game" + DELIMITER +
                                product.getId() + DELIMITER +
                                product.getTitle() + DELIMITER +
                                ((Game) product).getGenre() + DELIMITER +
                                product.getDailyRent() + DELIMITER +
                                product.getIsRented();
                    } else if (product instanceof Album) {
                        row = "Album" + DELIMITER +
                                product.getId() + DELIMITER +
                                product.getTitle() + DELIMITER +
                                ((Album) product).getArtist() + DELIMITER +
                                ((Album) product).getAlbumYear() + DELIMITER +
                                product.getDailyRent() + DELIMITER +
                                product.getIsRented();
                    }
                    csvWriter.write(row);
                    csvWriter.newLine();
                }
                csvWriter.close();
                userController.saveUserData("DartData");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRentalData() {
        try {
            if (rentals.isEmpty()) {
                System.out.println("No rental history to save. Please load existing history or save after some transactions.");
                getInput.sleepMilliseconds();
            } else {
                final String DELIMITER = ";";
                String line = "";
                File origFile = new File(getInput.pathFinder("RentalHistory"));
                File tempFile = new File(getInput.pathFinder("tempRental"));
                BufferedWriter csvWriter = new BufferedWriter(new FileWriter(tempFile, true));
                int newLine = 1;
                for (Rental rental : rentals) {
                    String row = rental.getRentalId() + DELIMITER +
                            rental.getRentedItemId() + DELIMITER +
                            rental.getRentedCustomerId() + DELIMITER +
                            rental.getRentalDate() + DELIMITER +
                            rental.getReturnDate() + DELIMITER +
                            rental.getRentalIncome() + DELIMITER +
                            rental.getRating() + DELIMITER +
                            rental.getReview() + DELIMITER +
                            rental.getIsReturned();

                    if (newLine != 1)
                        csvWriter.newLine();
                    csvWriter.write(row);
                    newLine++;
                }

                csvWriter.close();
                String origFileLocation = origFile.getAbsolutePath();
                origFile.delete();
                tempFile.renameTo(new File(origFileLocation));
                userInterface.printInterfaceLabels("userManagerExportSuccess", "Rental history");
                getInput.sleepMilliseconds();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     *            PRODUCT GENERIC METHODS AND GETTERS & SETTERS                *
     ***************************************************************************/
    void productViews(String objectType, String sortBy) {
        try {
            if (!products.isEmpty()) {
                switch (sortBy) {
                    case "title" -> products.sort(Comparator.comparing(Product::getTitle)
                                            .thenComparing(Product::getId));

                    case "rating" -> products.sort(Comparator.comparing(Product::getRating).reversed()
                                             .thenComparing(Product::getTitle)
                                             .thenComparing(Product::getId));
                }
                if (objectType.equals("album") && sortBy.equals("year")) {
                    ArrayList<Album> artistList = new ArrayList<>();
                    for (Product album : products) {
                        if (album instanceof Album)
                            artistList.add((Album) album);
                    }
                    artistList.sort(Comparator.comparing(Album::getAlbumYear));
                    for (Album album : artistList) {
                        System.out.println(album.toString());
                    }
                } else {
                    if (objectType.equals("game")) {
                        for (Product game : products) {
                            if (game instanceof Game)
                                System.out.println(game.toString());
                        }
                    } else if (objectType.equals("album")) {
                        for (Product album : products) {
                            if (album instanceof Album)
                                System.out.println(album.toString());
                        }
                    }
                }
            } else {
                userInterface.printlnInterfaceLabels("userNoListNotFound", "products");
            }
            userInterface.printInterfaceLabels("pressEnter");
            System.in.read();
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userListNotFoundException", String.valueOf(e));
        }
    }

    public String getProductDetail(String productId, String attributeName) {
        String productDetail = null;
        for (Product product : products) {
            if (productId.equals(product.getId())) {
                switch (attributeName) {
                    case "productId" -> productDetail = product.getId();
                    case "productTitle" -> productDetail = product.getTitle();
                }
            }
        }
        return productDetail;
    }

    public String getProductClass(String productId) {
        String productClass = null;
        for (Product product : products) {
            if (productId.equals(product.getId())) {
                if (product instanceof Game) {
                    productClass = "Game";
                }
                else if (product instanceof Album) {
                    productClass = "Album";
                }
            }
        }
        return productClass;
    }

    void deleteProduct(String productType) {
        try {
            boolean deleteProcess = false;
            int deleteIndex = -1;
            if (productType.equals("Game"))
                displayGames(true);
            else if (productType.equals("Album"))
                displayAlbums(true);

            String idToDelete = getInput.getIdRequired("dartProductDeleteId", productType);

            if (getInput.isUUID(idToDelete) && ifProductExist(idToDelete) && ifProductAvailable(idToDelete)) {
                // Proceed to delete if Product is found and not Rented
                for (Product product : products) {
                    if (product instanceof Game && idToDelete.equals(product.getId()) && productType.equals("Game")) {
                        deleteIndex = products.indexOf(product);
                    } else if (product instanceof Album && idToDelete.equals(product.getId()) && productType.equals("Album")) {
                        deleteIndex = products.indexOf(product);
                    }
                }
                System.out.println(products.get(deleteIndex).toString());
                String[] deleteAcceptSet = {"Y", "y", "N", "n"};
                String userInput = getInput.getMenuInput("dartProductDeleteConfirmPrompt", productType.toLowerCase(), deleteAcceptSet);

                if (userInput.toLowerCase().equals("y")) {
                    products.remove(deleteIndex);
                    userInterface.printlnInterfaceLabels("successDeleted");
                } else {
                    userInterface.printlnInterfaceLabels("successDeleteCancelled");
                }
            } else if (getInput.isUUID(idToDelete) && ifProductExist(idToDelete) && !ifProductAvailable(idToDelete)) {
                userInterface.printlnInterfaceLabels("dartProductDeleteRented");
            } else if (getInput.isUUID(idToDelete) && !ifProductExist(idToDelete)) {
                userInterface.printlnInterfaceLabels("dartProductDeleteIdNotFound", productType, idToDelete);
            } else if (!getInput.isUUID(idToDelete)) {
                userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
            }
        } catch (UUIDTypeException exception) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (StringTypeException ex) {
            userInterface.printlnInterfaceLabels("errorExceptionEmptyString", productType + " id");
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorIdInputInvalid");
        }
        getInput.sleepMilliseconds();
    }

    public void searchProduct(String itemType) throws IOException {
        List<Game> searchGames = null;
        List<Album> searchAlbums = null;
        if (itemType.equals("Game")) {
            String searchString = getInput.getStringRequired("dartProductSearch", "game genre", "Game genre");
            searchGames = products.stream()
                    .filter(Game.class::isInstance)
                    .map(Game.class::cast)
                    .filter(game -> game.getGenre().toLowerCase().contains(searchString.toLowerCase()))
                    .collect(toList());
        } else {
            String searchString = getInput.getStringRequired("dartProductSearch", "album year", "Album year");
            searchAlbums = products.stream()
                    .filter(Album.class::isInstance)
                    .map(Album.class::cast)
                    .filter(album -> String.valueOf(album.getAlbumYear()).contains(searchString))
                    .collect(toList());
        }

        if ((itemType.equals("Game") && searchGames.isEmpty()) || (itemType.equals("Album") && searchAlbums.isEmpty())) {
            userInterface.printlnInterfaceLabels("dartProductSearchNoResult");
            getInput.sleepMilliseconds();
        } else if (itemType.equals("Game")) {
            for (Game game : searchGames) {
                System.out.println(game.toString());
            }
            userInterface.printInterfaceLabels("pressEnter");
            System.in.read();
        } else if (itemType.equals("Album")) {
            for (Album album : searchAlbums) {
                System.out.println(album.toString());
            }
            userInterface.printInterfaceLabels("pressEnter");
            System.in.read();
        }
    }

    private boolean ifProductExist(String productId) {
        boolean ifExists = false;
        for (Product product : products)
            if (productId.equals(product.getId()))
                ifExists = true;
        return ifExists;
    }

    private boolean ifProductAvailable(String productId) {
        boolean ifNotRented = false;
        for (Product product : products) {
            if (productId.equals(product.getId()) && !product.getIsRented())
                ifNotRented = true;
        }
        return ifNotRented;
    }

    private boolean ifProductExist(String productId, String productType) {
        boolean ifExists = false;
        for (Product product : products)
            if (productId.equals(product.getId()) && (product instanceof Game) && (productType.equalsIgnoreCase("games") || productType.equalsIgnoreCase("game")))
                ifExists = true;
            else if (productId.equals(product.getId()) && (product instanceof Album) && (productType.equalsIgnoreCase("albums") || productType.equalsIgnoreCase("album")))
                ifExists = true;
        return ifExists;
    }

    private boolean ifRentalExist(String rentalId) {
        boolean ifExists = false;
        for (Rental rental : rentals)
            if (rentalId.equals(rental.getRentalId()))
                ifExists = true;
        return ifExists;
    }

    /***************************************************************************
     *                     GAME OBJECTS METHODS START BELOW                     *
     ***************************************************************************/
    public void displayGames(boolean skipEnter) {
        try {
            userInterface.printlnInterfaceLabels("dartGameListHeader");
            products.sort(Comparator.comparing(Product::getRating).reversed()
                    .thenComparing(Product::getTitle)
                    .thenComparing(Product::getId));

            for (Product game : products) {
                if (game instanceof Game)
                    System.out.println(game.toString());
            }
            if (!skipEnter) {
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userListNotFoundException", String.valueOf(e));
        }
    }

    public void addGame() {
        userInterface.printlnInterfaceLabels("dartProductNewEntryGame");
        String newGameID = UUID.randomUUID().toString();
        userInterface.printlnInterfaceLabels("dartProductNewID", "game", newGameID);
        String newGameTitle = getInput.getStringRequired("dartProductNewTitle", "Game", "Game title");
        String newGameGenre = getInput.getStringRequired("dartProductNewGenre");
        double newGameRentalRate = getInput.getDoubleRequired("dartProductNewDailyRent", "Game daily rent fee");

        products.add(new Game(newGameID, newGameTitle, newGameGenre, newGameRentalRate, false));
        userInterface.printlnInterfaceLabels("dartProductNewSuccess", "Game");
        getInput.sleepMilliseconds();
    }

    /***************************************************************************
     *                    ALBUM SECTION METHODS START BELOW                    *
     ***************************************************************************/
    public void displayAlbums(boolean skipEnter) {
        try {
            userInterface.printlnInterfaceLabels("dartAlbumListHeader");
            products.sort(Comparator.comparing(Product::getRating).reversed()
                    .thenComparing(Product::getTitle)
                    .thenComparing(Product::getId));

            for (Product album : products) {
                if (album instanceof Album)
                    System.out.println(album.toString());
            }
            if (!skipEnter) {
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userListNotFoundException", String.valueOf(e));
        }
    }

    public void addAlbum() {
        userInterface.printlnInterfaceLabels("dartProductNewEntryAlbum");
        String newAlbumID = UUID.randomUUID().toString();
        userInterface.printlnInterfaceLabels("dartProductNewID", "album", newAlbumID);

        String newAlbumTitle = getInput.getStringRequired("dartProductNewTitle", "Album", "Album title");
        String newAlbumArtist = getInput.getStringRequired("dartProductNewAlbumArtist", "Album artist");
        int newAlbumYear = getInput.getIntRequired("dartProductNewYear", "Album");
        double newAlbumRentalRate = getInput.getDoubleRequired("dartProductNewDailyRent", "Album daily rent fee");

        products.add(new Album(newAlbumID, newAlbumTitle, newAlbumArtist, newAlbumYear, newAlbumRentalRate, false));
        userInterface.printlnInterfaceLabels("dartProductNewSuccess", "Album");
        getInput.sleepMilliseconds();
    }

    /***************************************************************************
     *                   RENTAL SECTION METHODS START BELOW                    *
     ***************************************************************************/

    public void displayRentals(boolean skipEnter) {
        try {
            for (Rental rental : rentals) {
                System.out.println(rental.toString());
            }
            if (!skipEnter) {
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userListNotFoundException", String.valueOf(e));
        }
    }

    public void viewCurrentRentals() {
        try {
            for (Rental item : rentals) {
                System.out.println(item.toString());
            }
            userInterface.printInterfaceLabels("pressEnter");
            System.in.read();
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("userListNotFoundException", String.valueOf(e));
        }
    }

    public void checkRental(String activeId, String itemType) {
        try {
            String activeIdMembership = userController.getCustomerDetail(activeId,"membershipType");
            int maxRentals = userController.getCustomerDetail(activeId,"concurrentRentals", 1);
            // Get current rental count for customer
            long rentCount = rentals.stream().filter(rental -> (rental.getRentedCustomerId().equals(activeId)) && !rental.getIsReturned()).count();

            if (rentCount < maxRentals) {
                    addRental(activeId, itemType);
            } else if (activeIdMembership.equals("Platinum") && rentCount == maxRentals) {
                userInterface.printlnInterfaceLabels("dartRentalMemberLimit", activeIdMembership);
            } else {
                userInterface.printlnInterfaceLabels("dartRentalMemberLimit", activeIdMembership);
                String[] upgradeAcceptSet = {"Y", "y", "N", "n"};
                String userInput = getInput.getMenuInput("dartRentalUpgradePrompt", upgradeAcceptSet);

                if (userInput.toLowerCase().equals("y")) {
                    messageController.buildMessage(activeId,"Customer", "Employee", null, "upgrade", null);
                } else {
                    userInterface.printlnInterfaceLabels("dartRentalCancelled");
                }
            }
        } catch (UUIDTypeException ex) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
        }
        getInput.sleepMilliseconds();
    }

    public void addRental(String activeId, String itemType) throws UUIDTypeException {
        try {
            if (itemType.equals("Game")) {
                displayGames(true);
            } else {
                displayAlbums(true);
            }
            String idToRent = getInput.getIdRequired("dartRentalItemID", itemType);

            int idMatchFound = -1;
            String rentItemId = "";
            LocalDate currentDate = LocalDate.now();
            String itemToRent = itemType.substring(0, 1).toUpperCase() + itemType.substring(1);

            if (ifProductExist(idToRent, itemType) && ifProductAvailable(idToRent)) {
                for (Product product : products) {
                    if (idToRent.equals(product.getId())) {
                        rentItemId = product.getId();
                        idMatchFound = products.indexOf(product);
                        userInterface.printlnInterfaceLabels("dartRentalProductFound", itemType);
                        System.out.println(products.get(idMatchFound).toString());
                    }
                }

                String[] rentAcceptSet = {"Y", "y", "N", "n"};
                String userInput = getInput.getMenuInput("dartRentalConfirmPrompt", rentAcceptSet);

                if (userInput.toLowerCase().equals("y")) {
                    rentals.add(new Rental(rentItemId, activeId, currentDate));
                    products.get(idMatchFound).setRentalStatus(true);
                    userInterface.printlnInterfaceLabels("dartRentalSuccess", itemToRent);
                } else {
                    userInterface.printlnInterfaceLabels("dartRentalCancelled");
                }
            } else if (ifProductExist(idToRent, itemType) && !ifProductAvailable(idToRent)) {
                userInterface.printlnInterfaceLabels("dartRentalAlreadyRented", itemToRent, idToRent);
                String[] requestAcceptSet = {"Y", "y", "N", "n"};
                String userInput = getInput.getMenuInput("dartRentalRequestPrompt", requestAcceptSet);

                if (userInput.toLowerCase().equals("y")) {
                    String rentedCustomer = null;
                    for (Rental rental : rentals) {
                        if (idToRent.equals(rental.getRentedItemId()))
                            rentedCustomer = rental.getRentedCustomerId();
                    }
                    messageController.buildMessage(activeId, "Customer", "Customer", idToRent, "rental", rentedCustomer);
                }
            } else if (!ifProductExist(idToRent, itemType) && !ifProductAvailable(idToRent)) {
                userInterface.printlnInterfaceLabels("dartProductDeleteIdNotFound", itemToRent, idToRent);
            }
        } catch (UUIDTypeException exception) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (StringTypeException ex) {
            userInterface.printlnInterfaceLabels("errorExceptionEmptyString", itemType + " id");
        } catch (Exception ex) {
            userInterface.printlnInterfaceLabels("userAuthError");
            ex.printStackTrace();
        }
    }

    public boolean ifCustomerHasRentals(String customerId) {
        boolean customerHasRentals = false;
        for (Rental rental : rentals) {
            if (customerId.equals(rental.getRentedCustomerId()) && !rental.getIsReturned()) {
                customerHasRentals = true;
            }
        }
        return customerHasRentals;
    }

    public void removeCustomerRentals(String customerId) {
        rentals.removeIf(rental -> rental.getRentedCustomerId().equals(customerId));
    }

    public void returnRental() {
        try {
            userController.displayCustomers(true);
            String customerId = getInput.getIdRequired("userIdPrompt", "Customer");

            boolean customerExist = userController.ifCustomerExist(customerId);
            if (customerExist && ifCustomerHasRentals(customerId)) {
                // If Customer exists and has any active rentals
                userInterface.printlnInterfaceLabels("dartRentalCurrentItems");
                for (Rental rental : rentals) {
                    String itemId;
                    if (customerId.equals(rental.getRentedCustomerId()) && !rental.getIsReturned()) {
                        itemId = rental.getRentedItemId();
                        for (Product item : products) {
                            if (itemId.equals(item.getId())) {
                                if (item instanceof Game)
                                    userInterface.printlnInterfaceLabels("dartRentalCurrentItemsGameList", itemId, item.getTitle());
                                else if (item instanceof Album)
                                    userInterface.printlnInterfaceLabels("dartRentalCurrentItemsAlbumList", itemId, item.getTitle());
                            }
                        }
                    }
                }

                String productIdToReturn = getInput.getIdRequired("dartRentalReturnItemPrompt", "Product Id");

                if (ifProductExist(productIdToReturn)) {
                    userInterface.printlnInterfaceLabels("dartRentalReturnItemId", productIdToReturn);
                    boolean isFound = false;
                    boolean isAlreadyReturned = false;
                    String itemType = null;
                    LocalDate rentalDate = null;
                    String rentId = "";
                    for (Rental rental : rentals) {
                        if (productIdToReturn.equals(rental.getRentedItemId()) && customerId.equals(rental.getRentedCustomerId()) && !rental.getIsReturned()) {
                            isFound = true;
                            rentId = rental.getRentalId();
                            rentalDate = rental.getRentalDate();
                        } else if (productIdToReturn.equals(rental.getRentedItemId()) && customerId.equals(rental.getRentedCustomerId()) && rental.getIsReturned()) {
                            isFound = true;
                            isAlreadyReturned = true;
                        }
                    }

                    if (isFound && !isAlreadyReturned) {
                        double dailyRent = 0;
                        long daysRented = 0;
                        double totalRent;
                        double discountRate = 0;
                        double discountAmount;
                        Double discountedTotal;
                        int customerCredit = 0;
                        boolean isRentFree = false;

                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                        LocalDate returnDate = LocalDate.now();
                        daysRented = calculateRentalDays(rentalDate);
                        String rentDate = rentalDate.format(dateFormatter);
                        String returnedDate = returnDate.format(dateFormatter);
                        String rentedTitle = "";

                        // Look for a game with that ID
                        for (Product product : products) {
                            if (productIdToReturn.equals(product.getId())) {
                                product.setRentalStatus(false);
                                dailyRent = product.getDailyRent();
                                rentedTitle = product.getTitle();

                                if (product instanceof Game)
                                    itemType = "game";
                                else if (product instanceof Album)
                                    itemType = "album";
                            }
                        }
                        // Check customer list for the customer's credit and get membership type for discount
                        customerCredit = userController.getCustomerDetail(customerId, "customerCredit", 1);
                        discountRate = userController.getCustomerDetail(customerId, "discountRate", 1.0);
                        if (customerCredit == 5) {
                            userController.setCustomerDetail(customerId, "customerCredit", 0);
                            isRentFree = true;
                        } else {
                            customerCredit = customerCredit + 1;
                            userController.setCustomerDetail(customerId, "customerCredit", customerCredit);
                        }

                        String daysRentedString;
                        if (daysRented < 2) {
                            daysRentedString = String.valueOf(daysRented) + " day";
                        } else {
                            daysRentedString = String.valueOf(daysRented) + " days";
                        }
                        // Calculate rent payment total
                        assert itemType != null;
                        String itemTypeText = itemType.substring(0, 1).toUpperCase() + itemType.substring(1);

                        if (isRentFree) {
                            discountedTotal = 0.0;
                            userInterface.printlnInterfaceLabels("dartSeparatorLine");
                            userInterface.printlnInterfaceLabels("dartRentalReturnTypeTitle", itemTypeText, rentedTitle);
                            userInterface.printlnInterfaceLabels("dartRentalReturnDates", rentDate, returnedDate);
                            userInterface.printlnInterfaceLabels("dartRentalReturnDaysRate", daysRentedString, String.valueOf(dailyRent));
                            userInterface.printlnInterfaceLabels("dartSeparatorLine");
                            userInterface.printlnInterfaceLabels("dartRentalReturnFreeMessage");
                        } else {
                            totalRent = dailyRent * daysRented;
                            totalRent = Math.round(totalRent * 100.0) / 100.0;
                            discountAmount = Math.round(totalRent * discountRate * 100.0) / 100.0;
                            discountedTotal = Math.round((totalRent - discountAmount) * 100.0) / 100.0;
                            userInterface.printlnInterfaceLabels("dartSeparatorLine");
                            userInterface.printlnInterfaceLabels("dartRentalReturnTypeTitle", itemTypeText, rentedTitle);
                            userInterface.printlnInterfaceLabels("dartRentalReturnDates", rentDate, returnedDate);
                            userInterface.printlnInterfaceLabels("dartRentalReturnDaysRate", daysRentedString, String.valueOf(dailyRent));
                            userInterface.printlnInterfaceLabels("dartSeparatorLine");
                            userInterface.printlnInterfaceLabels("dartRentalTotalBeforeDiscount", String.valueOf(totalRent));
                            userInterface.printlnInterfaceLabels("dartRentalReturnDiscount", String.valueOf(discountAmount));
                            userInterface.printlnInterfaceLabels("dartRentalReturnTotal", String.valueOf(discountedTotal));

                            String creditPlural = "credits. ";
                            if (customerCredit < 2) {
                                creditPlural = "credit. ";
                            }
                            userInterface.printInterfaceLabels("dartRentalReturnCreditsDisplay", String.valueOf(customerCredit), creditPlural);
                            if (customerCredit == 5)
                                userInterface.printlnInterfaceLabels("dartRentalReturnHooray");
                        }
                        String[] rateAcceptSet = {"Y", "y", "N", "n"};
                        String userInput = getInput.getMenuInput("dartRentalRatingPrompt", itemType, rateAcceptSet);
                        double customerRating = 0;
                        String customerReview = "";
                        if (userInput.toLowerCase().equals("y")) {
                            String[] ratingAcceptSet = {"0", "1", "2", "3", "4", "5"};
                            customerRating = Double.parseDouble(getInput.getMenuInput("dartRentalRatingInput", ratingAcceptSet));
                            customerReview = getInput.getString("dartRentalReviewInput");
                        }
                        // Finally write everything to Rental arraylist and be done.
                        for (Rental rentItem : rentals) {
                            if (rentId.equals(rentItem.getRentalId())) {
                                rentItem.setRentalIncome(discountedTotal);
                                rentItem.setReturnDate(returnDate);
                                rentItem.setRating(customerRating);
                                rentItem.setReview(customerReview);
                                rentItem.setIsReturned(true);
                            }
                        }
                        userInterface.printlnInterfaceLabels("dartRentalComplete");
                        // Update item rating while at it.
                        updateRating();

                    } else if (isFound && isAlreadyReturned) {
                        userInterface.printlnInterfaceLabels("dartRentalItemAlreadyReturned");
                    } else {
                        userInterface.printlnInterfaceLabels("dartRentalCustomerNoItemFound", itemType);
                    }
                } else{
                    userInterface.printlnInterfaceLabels("dartRentalNoItemFound");
                }
            } else if (customerExist && !ifCustomerHasRentals(customerId)) {
                userInterface.printlnInterfaceLabels("dartRentalNoActiveRental");
            } else if (!customerExist) {
                userInterface.printlnInterfaceLabels("userCustomerIdNotFound");
            }
        } catch (IntegerTypeException ex) {
            userInterface.printlnInterfaceLabels("errorExceptionRentalNegativeNumber");
        } catch (UUIDTypeException exception) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (StringTypeException ex) {
            userInterface.printlnInterfaceLabels("errorExceptionEmptyString", "Id");
        } catch (Exception ex) {
            userInterface.printlnInterfaceLabels("userAuthError");
            ex.printStackTrace();
        }
        getInput.sleepMilliseconds();
    }

    public void viewRentalTotalProfit() {
        try {
            if (rentals.isEmpty() || rentals == null) {
                userInterface.printlnInterfaceLabels("dartRentalNoTransactions");
                getInput.sleepMilliseconds();
            } else {
                double rentTotalProfit = 0.0;
                for (Rental rental: rentals) {
                    rentTotalProfit = rentTotalProfit + rental.getRentalIncome();
                }

                userInterface.printlnInterfaceLabels("dartRentalShopTotalIncome", String.valueOf(rentTotalProfit));
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
        }
    }

    public void viewRentalProfitable() {
        try {
            Rental profitItem = rentalHighestIncome();
            if (profitItem == null) {
                userInterface.printlnInterfaceLabels("dartRentalNoTransactions");
                getInput.sleepMilliseconds();
            } else {
                userInterface.printlnInterfaceLabels("dartRentalMostItem", "profitable");
                String itemId = profitItem.getRentedItemId();
                userInterface.printInterfaceLabels("dartRentalMostItemIdTitle", getProductClass(itemId), getProductDetail(itemId, "productTitle"), itemId);
                userInterface.printlnInterfaceLabels("dartRentalMostTotalIncome", String.valueOf(profitItem.getRentalIncome()));
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
        }
    }

    public void viewRentalFrequency() {
        try {
            Map<String, Long> frequentItem = rentalFrequentItem();
            if (frequentItem == null || frequentItem.isEmpty()) {
                userInterface.printlnInterfaceLabels("dartRentalNoTransactionsFrequency");
                getInput.sleepMilliseconds();
            } else {
                userInterface.printlnInterfaceLabels("dartRentalMostItem", "frequent");
                Map.Entry<String, Long> entry = frequentItem.entrySet().iterator().next();
                String itemId = entry.getKey();
                Long frequencyNumber = entry.getValue();

                String frequencyText = null;
                if (frequencyNumber == 1) {
                    frequencyText = frequencyNumber + " time.";
                } else if (frequencyNumber > 1) {
                    frequencyText = frequencyNumber + " times.";
                }
                userInterface.printInterfaceLabels("dartRentalMostItemIdTitle", getProductClass(itemId), getProductDetail(itemId, "productTitle"), itemId);
                userInterface.printlnInterfaceLabels("dartRentalMostFrequencyNumber", frequencyText);
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
        }
    }

    public void viewRentalBestCustomer() {
        try {
            Rental bestCustomer = rentalBestCustomer();
            if (bestCustomer == null) {
                userInterface.printlnInterfaceLabels("dartRentalNoTransactions");
                getInput.sleepMilliseconds();
            } else {
                userInterface.printlnInterfaceLabels("dartRentalBestCustomer");
                String customerId = bestCustomer.getRentedCustomerId();
                userInterface.printlnInterfaceLabels("dartRentalBestCustomerDisplay", userController.getCustomerDetail(customerId, "customerName"), customerId, String.valueOf(bestCustomer.getRentalIncome()));
                userInterface.printInterfaceLabels("pressEnter");
                System.in.read();
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
        }
    }

    private void updateRating() {
        ArrayList<String> itemIds = (ArrayList<String>) products.stream().map(Product::getId).collect(toList());
        for (String item : itemIds) {
            Double itemRating = 0.0;
            int numberOfItems = 1;
            boolean isRatingFound = false;

            for (Rental rental : rentals) {
                if (!rental.getRating().equals(-1.0) && rental.getRentedItemId().equals(item) && rental.getIsReturned()) {
                    itemRating = (itemRating + rental.getRating())/numberOfItems;
                    numberOfItems = numberOfItems + 1;
                    isRatingFound = true;
                }
            }
            if (isRatingFound) {
                for (Product product : products) {
                    if (item.equals(product.getId()))
                        product.setRating(itemRating);
                }
            }
        }
    }

    private long calculateRentalDays(LocalDate rentalDate) throws IntegerTypeException {
        long daysRented;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        LocalDate returnDate = LocalDate.now();
        daysRented = DAYS.between(rentalDate, returnDate);
        // Same day rental return is counted as 1 day.
        if (daysRented == 0) {
            daysRented = 1;
        } else if (daysRented < 0) {
            throw new IntegerTypeException("errorExceptionRentalNegativeNumber", "returnRental", "number of days rented");
        }
        return daysRented;
    }

    private Rental rentalHighestIncome() {
        ArrayList<Rental> rentalIncome = new ArrayList<>();
        Rental profitItem;
        for (Rental item : rentals) {
            if (item.getIsReturned()) {
                Double itemIncome = rentals.stream()
                        .filter(p -> p.getRentedItemId().equals(item.getRentedItemId()))
                        .mapToDouble(Rental::getRentalIncome).sum();

                rentalIncome.add(new Rental(item.getRentedItemId(), itemIncome, item.getRentedCustomerId()));
            }
        }
        if (!rentalIncome.isEmpty()) {
            rentalIncome.sort(Comparator.comparing(Rental::getRentalIncome).reversed().thenComparing(Rental::getRentedItemId));
             profitItem = rentalIncome.get(0);
        } else
            profitItem = null;
        return profitItem;
    }

    private Rental rentalBestCustomer() {
        ArrayList<Rental> rentalCustomer = new ArrayList<>();
        Rental bestCustomer;
        for (Rental item : rentals) {
            if (item.getIsReturned()) {
                Double itemIncome = rentals.stream()
                        .filter(p -> p.getRentedCustomerId().equals(item.getRentedCustomerId()))
                        .mapToDouble(Rental::getRentalIncome).sum();

                rentalCustomer.add(new Rental(item.getRentedCustomerId(), itemIncome, item.getRentedCustomerId()));
            }
        }
        if (!rentalCustomer.isEmpty()) {
            rentalCustomer.sort(Comparator.comparing(Rental::getRentalIncome).reversed().thenComparing(Rental::getRentedCustomerId));
            bestCustomer = rentalCustomer.get(0);
        } else
            bestCustomer = null;
        return bestCustomer;
    }

    private Map rentalFrequentItem() {
        List<String> itemList = rentals
                .stream()
                .filter(Rental::getIsReturned)
                .map(Rental::getRentedItemId)
                .collect(toList());
        Map<String, Long> itemFrequencyMap = itemList
                .stream()
                .collect(Collectors.groupingBy(f -> f, Collectors.counting()));
        Map<String, Long> frequentItem = itemFrequencyMap.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Long>comparingByValue().reversed()))
                .limit(1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (f1, f2) -> f1, LinkedHashMap::new));
        return frequentItem;
    }
}