package dit042;

import dit042.exceptions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Helper class
 *
 * <p>
 * Provides static scanner input for all other classes to get inputs and display prompt strings.
 * Uses pathFinder method to verify given fileName exists, and find absolute path.
 * Close Scanner before system exit
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public class Helper {
    private static UserInterface userInterface = new UserInterface();
    private static final Scanner input = new Scanner(System.in); // static scanner
    private String userInput;
    private final int sleepTime = 1650;

    /***************************************************************************
     *                      MENU INPUT METHODS START BELOW                     *
     ***************************************************************************/
    public String getMenuInput(String message, String[] acceptSet) {
        boolean accept = true;
        do {
            userInterface.printInterfaceLabels(message);
            userInput = input.nextLine();
            for (String s : acceptSet) {
                if (userInput.equals(s)) {
                    accept = false;
                }
            }
            if (accept) {
                userInterface.printlnInterfaceLabels("helperValidationInvalidInput");
            }
        } while (accept);
        return userInput;
    }

    public String getMenuInput(String message, String labelSupplement, String[] acceptSet) {
        boolean accept = true;
        do {
            userInterface.printInterfaceLabels(message, labelSupplement);
            userInput = input.nextLine();
            for (String s : acceptSet) {
                if (userInput.equals(s)) {
                    accept = false;
                }
            }
            if (accept) {
                userInterface.printlnInterfaceLabels("helperValidationInvalidInput");
            }
        } while (accept);
        return userInput;
    }

    /***************************************************************************
     *                     STRING INPUT METHODS START BELOW                    *
     ***************************************************************************/
    // Get String - Loose scanner
    public String getString(String message) {
        userInterface.printInterfaceLabels(message);
        String userInput = input.nextLine();
        return userInput;
    }

    public String getString(String message, String labelSupplement) {
        userInterface.printInterfaceLabels(message, labelSupplement);
        String userInput = input.nextLine();
        return userInput;
    }

    public String getStringRequired(String message) {
        boolean accept = true;
        String callerMethod = getMethodName();
        do {
            try {
                userInterface.printInterfaceLabels(message);
                userInput = input.nextLine();
                if (validateString(userInput)) {
                    accept = false;
                }
            } catch (StringTypeException ex) {
                userInterface.printlnInterfaceLabels("errorExceptionEmptyString", "This field");
            }
        } while (accept);
        return userInput;
    }

    public String getStringRequired(String message, String attributeName) {
        boolean accept = true;
        do {
            try {
                userInterface.printInterfaceLabels(message);
                userInput = input.nextLine();
                if (validateString(userInput)) {
                    accept = false;
                }
            } catch (StringTypeException ex) {
                userInterface.printlnInterfaceLabels("errorExceptionEmptyString", attributeName);
            }
        } while (accept);

        return userInput;
    }

    public String getStringRequired(String message, String labelSupplement, String attributeName) {
        boolean accept = true;
        do {
            try {
                userInterface.printInterfaceLabels(message, labelSupplement);
                userInput = input.nextLine();

                if (validateString(userInput)) {
                    accept = false;
                }
            } catch (StringTypeException ex) {
                userInterface.printlnInterfaceLabels("errorExceptionEmptyString", attributeName);
            }
        } while (accept);
        return userInput;
    }

    public String getIdRequired(String message, String labelSupplement) throws UUIDTypeException, StringTypeException {
        boolean accept = true;
        do {
            userInterface.printInterfaceLabels(message, labelSupplement);
            userInput = input.nextLine();
            if (isUUID(userInput)) {
               accept = false;
            }
        } while (accept);
        return userInput;
    }

    /***************************************************************************
     *                    INTEGER INPUT METHODS START BELOW                    *
     ***************************************************************************/
    public int getIntRequired(String message) {
        int userInput = 0;
        boolean accept = true;
        do {
            userInterface.printInterfaceLabels(message);
            String userInputString = input.nextLine();
            if (!userInputString.isEmpty() && !userInputString.equals("") && isStringInt(userInputString) && (Integer.parseInt(userInputString) > 0)) {
                userInput = Integer.parseInt(userInputString);
                accept = false;
            } else {
                userInterface.printlnInterfaceLabels("helperValidationErrorEmptyText");
                System.out.println();
            }
        } while (accept);
        return userInput;
    }

    public int getIntRequired(String message, String labelSupplement) {
        int userInput = 0;
        boolean accept = true;
        do {
            userInterface.printInterfaceLabels(message, labelSupplement);
            String userInputString = input.nextLine();
            if (!userInputString.isEmpty() && !userInputString.equals("") && isStringInt(userInputString) && (Integer.parseInt(userInputString) > 0)) {
                userInput = Integer.parseInt(userInputString);
                accept = false;
            } else {
                userInterface.printlnInterfaceLabels("helperValidationErrorEmptyText");
                System.out.println();
            }
        } while (accept);
        return userInput;
    }

    /***************************************************************************
     *                     DOUBLE INPUT METHODS START BELOW                    *
     ***************************************************************************/
    public double getDoubleRequired(String message, String attributeName) {
        double userInput = 0;
        boolean accept = true;
        do {
            try {
                userInterface.printInterfaceLabels(message);
                String userInputString = input.nextLine();
                if (validateDouble(userInputString)) {
                    accept = false;
                    userInput = Double.parseDouble(userInputString);
                }
            } catch (DoubleTypeException ex) {
                userInterface.printlnInterfaceLabels("errorExceptionNegativeNumber", attributeName);
            }
        } while (accept);
        return userInput;
    }

    /***************************************************************************
     *                      VALIDATOR METHODS START BELOW                      *
     ***************************************************************************/
    private boolean validateString(String userInput) throws StringTypeException {
        boolean result = false;
        if (userInput.equals(null) || userInput.equals("") || userInput.isEmpty()) {
            throw new StringTypeException("errorExceptionEmptyString");
        } else
            result = true;
        return result;
    }

    private boolean validateDouble(String userInput) throws DoubleTypeException {
        boolean result = false;
        if ((userInput.isEmpty() && userInput.equals("") && !isStringDoubleInt(userInput)) || (Double.parseDouble(userInput) < 0)) {
            throw new DoubleTypeException("errorExceptionNegativeNumber");
        } else
            result = true;
        return result;
    }

    public boolean isStringInt(String userInput) {
        try {
            Integer.parseInt(userInput);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public boolean isStringDoubleInt(String userInput) {
        boolean result = false;
        try {
            double d = Double.valueOf(userInput);
            if (d == Integer.valueOf((int) d) || d == Double.valueOf(d)) {
                result = true;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean isUUID(String userInput) throws UUIDTypeException, StringTypeException {
        boolean result = false;

        Pattern uuidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        if (userInput.equals(null) || userInput.equals("") || userInput.isEmpty()) {
            throw new StringTypeException("errorExceptionEmptyString");
        } else if (!uuidPattern.matcher(userInput).matches()) {
            throw new UUIDTypeException();
        } else {
            result = true;
        }
        return result;
    }

    /***************************************************************************
     *                  OTHER HELPER INPUT METHODS START BELOW                 *
     ***************************************************************************/
    // Sleep thread for given milliseconds - Update constant on top
    public void sleepMilliseconds() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            userInterface.printlnInterfaceLabels("helperSleeperError");
        }
    }

    public String getMethodName() {
        String methodName=null;
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stacktrace.length; i++) {
            if(stacktrace[i].getMethodName().equals("getMethodName")) {
                methodName = stacktrace[i+2].getMethodName();
                break;
            }
        }
        return methodName;
    }

    /***************************************************************************
     *                   FILE OPERATIONS  METHODS START BELOW                  *
     ***************************************************************************/

    public String pathFinder(String fileName) {
        String absPath = null;
        String tempPath = null;
        String root = null;
        String checkName = null;
        if (fileName.equals("tempData")) {
            checkName = "DartData";
        } else if (fileName.equals("tempRental")) {
            checkName = "RentalHistory";
        }
        else {
            checkName = fileName;
        }
        try {
            root = System.getProperty("user.dir");
            if (Files.exists(Path.of(root + "/src/dit042/resources/" + checkName + ".txt"))) {
                absPath = root + "/src/dit042/resources/";
            } else if (Files.exists(Path.of(root + "/dit042/resources/" + checkName + ".txt"))) {
                absPath = root + "/dit042/resources/";
            } else if (Files.exists(Path.of(root + "dit042/resources/" + checkName + ".txt"))) {
                absPath = root + "dit042/resources/";
            } else {
                userInterface.printlnInterfaceLabels("errorFileNotFound");
                absPath = null;
            }
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorFileIOError");
        }
        return absPath = absPath + fileName + ".txt";
    }

    public void closeScanner() {
        input.close();
    }
}