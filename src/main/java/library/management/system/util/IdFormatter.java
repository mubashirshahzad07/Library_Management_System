package library.management.system.util;

public class IdFormatter {
    
    // display integer User ID as USR-001
    public static String formatUserId(int userId, String role) {
        if (role.equalsIgnoreCase("ADMIN")) {
            return String.format("ADM-%03d", userId);
        } else if (role.equalsIgnoreCase("LIBRARIAN")) {
            return String.format("LIB-%03d", userId);
        } else if (role.equalsIgnoreCase("STUDENT")) {
            return String.format("STU-%03d", userId);
        } else {
            return String.format("USR-%03d", userId);
        }
    }
    
    // display integer Book ID as BK-001
    public static String formatBookId(int bookId) {
        return String.format("BK-%03d", bookId);
    }
    
    // display integer Transaction ID as TXN-001
    public static String formatTransactionId(int transactionId) {
        return String.format("TXN-%03d", transactionId);
    }
    
    // extract integer ID from formatted IDs
    public static int extractId(String formattedId) {

    // removing characters and leading zeros
    String digits = formattedId.replaceAll("[^0-9]", "").replaceFirst("^0+", "");

    // convert String to int
    return Integer.parseInt(digits);
    }
}