package dit042;

import dit042.exceptions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MessageController controller class for Message object
 *
 * <p>
 * Maintains arraylist for all messages.
 * Used to build new messages with user inputs, read and send messages, display user inbox
 *
 * Users can send messages to either Employees or Customers.
 * Customer's membership upgrade or rent requests are automatically sent to All Employees.
 * User send message to himself/herself. Simpler for message testing.
 *
 * Customer membership upgrade and rental request is sent via Messages.
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public class MessageController {
    private final Helper getInput = new Helper();
    private UserController userController = new UserController();
    private static UserInterface userInterface = new UserInterface();
    private final String EOL = System.lineSeparator();
    private static ArrayList<Message> messages = new ArrayList<Message>();

    /***************************************************************************
     *                   MESSAGE SECTION METHODS START BELOW                   *
     ***************************************************************************/
    public String checkNewMsg(String activeId, String userType) {
        String newMsg = userInterface.getInterfaceLabels("messageNoMessages");
        int msgCount = 0;

        for (Message message : messages) {
            if (userType.equals("Customer")) {
                if (activeId.equals(message.getRecipientId()) && !message.getIsRead())
                    msgCount = msgCount + 1;
            } else if (userType.equals("Employee")) {
                if ((activeId.equals(message.getRecipientId()) && !message.getIsRead()) || (message.getRecipientId().equals("All Employees") && !message.getIsRead()))
                    msgCount = msgCount + 1;
            }
        }

        if(msgCount == 1) {
            newMsg = userInterface.getInterfaceLabels("messageOneMessage");
        } else if (msgCount > 1 && msgCount < 10) {
            newMsg = userInterface.getInterfaceLabels("messageSingleDigitMessage", String.valueOf(msgCount));
        } else if (msgCount > 9) {
            newMsg = userInterface.getInterfaceLabels("messageDoubleDigitMessage", String.valueOf(msgCount));
        }
        return newMsg;
    }

    void openInbox(String activeId, String userType) {
        try {
            // Getting ready to filter "isRead = false" messages for the activeId
            boolean isNewExist = true;
            Collections.sort(messages, Collections.reverseOrder());

            do {
                ArrayList<Message> userInbox = new ArrayList<Message>();

                if (userType.equals("Employee")) {
                    userInbox = messages.stream()
                            .filter(m -> (m.getRecipientId().equals(activeId) && !m.getIsRead()) || (m.getRecipientId().equals("All Employees") && !m.getIsRead()))
                            .collect(Collectors.toCollection(ArrayList::new));
                } else if (userType.equals("Customer")) {
                    userInbox = messages.stream()
                            .filter(m -> m.getRecipientId().equals(activeId) && !m.getIsRead())
                            .collect(Collectors.toCollection(ArrayList::new));
                }
                if (!userInbox.isEmpty()) {
                    String senderName = null;
                    String recipientName = null;
                    if (userInbox.size() == 1) {
                        userInterface.printlnInterfaceLabels("messageInboxYouHaveOneMsg");
                    } else {
                        userInterface.printlnInterfaceLabels("messageInboxYouHaveManyMsg", String.valueOf(userInbox.size()));
                    }
                    userInterface.printlnInterfaceLabels("messageInboxSeparator");
                    for (Message message : userInbox) {
                        LocalDateTime dateTime = message.getMsgDate();
                        String date = dateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                        String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("h:mm a"));
                            if (userController.ifEmployeeExist(message.getSenderId()))
                                senderName = userController.getEmployeeDetail(message.getSenderId(),"employeeName");
                            if (userController.ifEmployeeExist(message.getRecipientId()))
                                recipientName = userController.getEmployeeDetail(message.getRecipientId(),"employeeName");
                            else if (message.getRecipientId().equals("All Employees"))
                                recipientName = "DART Employee";
                            if (userController.ifCustomerExist(message.getSenderId()))
                                senderName = userController.getCustomerDetail(message.getSenderId(),"customerName");
                            if (userController.ifCustomerExist(message.getRecipientId()))
                                recipientName = userController.getCustomerDetail(message.getRecipientId(),"customerName");

                        System.out.println("|   | Sender: " + senderName + " (" + message.getSenderId() + ")" + EOL + "| " + (userInbox.indexOf(message) + 1) + " | Subject: " + message.getMsgSubject() + EOL + "|   | Date: " + date + " " + time);
                        userInterface.printlnInterfaceLabels("messageInboxSeparator");
                    }
                    String[] readAcceptSet = new String[]{"m", "M", "B", "b", "D", "d"};
                    String readOption = getInput.getMenuInput("messageInboxCommandsList", readAcceptSet);
                    if (readOption.toLowerCase().equals("m")) {
                        for (Message message : userInbox) {
                            readMessage(message, senderName, recipientName);
                            String senderId = message.getSenderId();
                            if (message.getMsgType().equals("upgrade")) {
                                userController.upgradeCustomerView(senderId);
                            } else {
                                try {
                                    userInterface.printInterfaceLabels("pressEnter");
                                    System.in.read();
                                } catch (Exception e) {
                                    userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
                                }
                            }
                        }
                        userInbox.clear();
                    } else if (readOption.toLowerCase().equals("d")) {
                        int deleteIndex = -1;
                        boolean notAccepted = true;
                        do {
                            deleteIndex = getInput.getIntRequired("messageInboxDeleteIdPrompt");
                            if (0 == deleteIndex || deleteIndex > userInbox.size()) {
                                userInterface.printlnInterfaceLabels("messageInboxDeleteIdMismatch");
                            } else {
                                String deleteId = userInbox.get(deleteIndex - 1).getMsgId();
                                for (Message message : messages) {
                                    if (message.getMsgId().equals(deleteId)) {
                                        deleteIndex = messages.indexOf(message);
                                    }
                                }
                                messages.remove(deleteIndex);
                                userInterface.printInterfaceLabels("messageInboxMsgDeleted");
                                notAccepted = false;
                            }
                        } while(notAccepted);
                    } else if (readOption.toLowerCase().equals("b")) {
                        isNewExist = false;
                    }
                } else {
                    System.out.println(EOL);
                    userInterface.printlnInterfaceLabels("messageInboxNoMessages");
                    isNewExist = false;
                }
            } while (isNewExist);
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
        }
        getInput.sleepMilliseconds();
    }

    public void readMessage(Message message, String senderName, String recipientName) {
        message.setIsRead(true);

        LocalDateTime dateTime = message.getMsgDate();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a", Locale.ENGLISH);
        String date = dateTime.toLocalDate().format(dateFormatter);
        String time = dateTime.toLocalTime().format(timeFormatter);
        String msgBody = message.getMsgBody();
        String[] tempBody = msgBody.split(" ");
        ArrayList<String> bodyText = new ArrayList<>();
        int length = 0;
        final int MESSAGEROWLENGTH = 60;
        StringBuilder row = new StringBuilder("  ");

        // Message body text wrapping
        for (String word : tempBody) {
            length = length + word.length() + 1;
            if (length <= MESSAGEROWLENGTH) {
                row.append(word).append(" ");
            }
            else {
                row.append(EOL);
                bodyText.add(row.toString());
                row = new StringBuilder("  ");
                length = 0;
            }
        }
        bodyText.add(row.toString());

        System.out.println(EOL);
        userInterface.printlnInterfaceLabels("messageInboxSeparator");
        userInterface.printlnInterfaceLabels("messageSingleMessageSender", senderName, message.getSenderId());
        userInterface.printlnInterfaceLabels("messageSingleMessageRecipient", recipientName, message.getRecipientId());
        userInterface.printlnInterfaceLabels("messageSingleMessageDate", date);
        userInterface.printlnInterfaceLabels("messageSingleMessageTime", time);
        userInterface.printlnInterfaceLabels("messageInboxSeparator");
        userInterface.printlnInterfaceLabels("messageSingleMessageSubject", message.getMsgSubject());
        userInterface.printlnInterfaceLabels("messageSingleMessageBodyHeader");
        for (String eachRow : bodyText) {
            System.out.print(eachRow);
        }
        System.out.println(EOL);
        userInterface.printlnInterfaceLabels("messageInboxSeparator");
        if (message.getMsgItemId() != null) {
            userInterface.printlnInterfaceLabels("messageSingleMessageItemId", message.getMsgItemId());
            userInterface.printlnInterfaceLabels("messageInboxSeparator");
        }
    }

    /***************************************************************************
     *                     SEND MESSAGE METHODS START BELOW                    *
     ***************************************************************************/
    void buildMessage(String activeId, String senderType, String recipientType, String msgItemId, String msgType, String rentedCustomerId) {
        try {
            boolean isFound = false;
            String recipientId = null;
            String recipientName = null;
            String idToCheck;

            if (recipientType.equals("Employee") && msgType.equals("upgrade")) {
                    isFound = true;
                    recipientId = "All Employees";
                    recipientName = "DART Employee";
            } else if (recipientType.equals("Employee") && msgType.equals("message")) {
                userController.listEmployees(true);
                idToCheck = getInput.getIdRequired("messageMessageRecipientIdPrompt", "Employee");

                if (userController.ifEmployeeExist(idToCheck)) {
                    isFound = true;
                    recipientId = idToCheck;
                    recipientName = userController.getEmployeeDetail(idToCheck, "employeeName");
                }
            } else if (recipientType.equals("Customer") && msgType.equals("rental")) {
                isFound = true;
                recipientId = rentedCustomerId;
                recipientName = userController.getCustomerDetail(rentedCustomerId, "customerName");
            } else if ((recipientType.equals("Customer")) && msgType.equals("message")) {
                userController.listCustomers(true);
                idToCheck = getInput.getIdRequired("messageMessageRecipientIdPrompt", "Customer");
                if (userController.ifCustomerExist(idToCheck)) {
                    isFound = true;
                    recipientId = idToCheck;
                    recipientName = userController.getCustomerDetail(idToCheck, "customerName");
                }
            }
            if (isFound && msgType.equals("rental")) {
                userInterface.printlnInterfaceLabels("messageRequestRentalProgress");
                sendMessage(recipientName, recipientId, activeId, msgType, msgItemId);
            } else if (isFound && msgType.equals("upgrade")) {
                userInterface.printlnInterfaceLabels("messageRequestUpgradeProgress");
                sendMessage(recipientName, recipientId, activeId, msgType, msgItemId);
            } else if (isFound && msgType.equals("message")) {
                sendMessage(recipientName, recipientId, activeId, msgType, null);
            } else {
                userInterface.printlnInterfaceLabels("messageRecipientNotFound", recipientType);
            }
        } catch (UUIDTypeException exception) {
            userInterface.printlnInterfaceLabels("UUIDInvalidFormat");
        } catch (StringTypeException ex) {
            userInterface.printlnInterfaceLabels("errorExceptionEmptyString", "Recipient id");
        } catch (Exception e) {
            userInterface.printlnInterfaceLabels("errorExceptionGeneric", String.valueOf(e));
        }
        getInput.sleepMilliseconds();
    }

    public void sendMessage(String recipientName, String recipientId, String activeId, String msgType, String msgItemId) {
        String msgSubject;
        String msgBody;
        LocalDateTime msgDate = LocalDateTime.of(LocalDate.now(), LocalTime.now());

        if (msgType.equals("rental")) {
            msgSubject = userInterface.getInterfaceLabels("messageRequestSubject");
            msgBody = userInterface.getInterfaceLabels("messageRequestBody");
            // Send the message and record it to Message array
            messages.add(new Message(activeId, recipientId, msgDate, msgSubject, msgBody, msgItemId, "rental"));
            userInterface.printlnInterfaceLabels("messageRequestSuccess");
        } else if (msgType.equals("upgrade")) {
            msgSubject = userInterface.getInterfaceLabels("messageUpgradeSubject");
            msgBody = userInterface.getInterfaceLabels("messageUpgradeBody");
            // Send the message and record it to Message array
            messages.add(new Message(activeId, recipientId, msgDate, msgSubject, msgBody, null, "upgrade"));
            userInterface.printlnInterfaceLabels("messageUpgradeSuccess");
        } else if (msgType.equals("message")) {
            userInterface.printlnInterfaceLabels("messageNewMsgHeader");
            userInterface.printlnInterfaceLabels("messageInboxSeparator");
            userInterface.printlnInterfaceLabels("messageSingleMessageRecipient", recipientName, recipientId);
            userInterface.printlnInterfaceLabels("messageInboxSeparator");
            msgSubject = getInput.getStringRequired("messageNewMsgSubject");

            userInterface.printlnInterfaceLabels("messageInboxSeparator");
            msgBody = getInput.getString("messageNewMsgBody");
            userInterface.printlnInterfaceLabels("messageInboxSeparator");
            String[] sendAcceptSet = {"S", "s", "C", "c"};
            String userInput = getInput.getMenuInput("messageNewMsgSendPrompt", sendAcceptSet);
            // Finally send message
            if (userInput.toLowerCase().equals("s")) {
                // Send the message and record it to Message array
                messages.add(new Message(activeId, recipientId, msgDate, msgSubject, msgBody, null, "message"));
                userInterface.printlnInterfaceLabels("messageMessageSendSuccess");
            } else {
                userInterface.printlnInterfaceLabels("messageMessageSendCancelled");
            }
        }
    }
}