package library.management.system.util;

public class IdFormatter {
    
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
    
    public static String formatBookId(int bookId) {
        return String.format("BK-%03d", bookId);
    }
    
    public static String formatTransactionId(int transactionId) {
        return String.format("TXN-%03d", transactionId);
    }
}