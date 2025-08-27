package utility;

/**
 * Utility class for displaying messages
 * 
 * @author hugol
 */
public class MessageUI {
    
    public static void displaySuccessMessage(String message) {
        System.out.println("Success " + message);
    }
    
    public static void displayErrorMessage(String message) {
        System.out.println("Error: " + message);
    }
    
    public static void displayInvalidChoiceMessage(String message) {
        System.out.println("Invalid: " + message);
    }
    
    public static void displayExitMessage() {
        System.out.println("Exiting... Thank you!");
    }
    
    public static void displayInvalidChoiceMessage() {
        System.out.println("Invalid choice! Please try again.");
    }
}