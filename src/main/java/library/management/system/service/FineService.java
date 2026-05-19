package library.management.system.service;

import library.management.system.model.Fine;
import library.management.system.model.Transaction;
import library.management.system.dao.FineDAO;

public class FineService {
    
    FineDAO fineDAO = new FineDAO();
    
    public void createFine(Transaction transaction) {

        // no transaction found
        if (transaction.getReturnDate() == null) {
            return;
        }

        // don't store fine if book returned on time
        if (!transaction.getReturnDate().after(transaction.getDueDate())) {
            return;
        }

        long diff = transaction.getReturnDate().getTime() - transaction.getDueDate().getTime();
        long daysLate = diff / (1000 * 60 * 60 * 24);

        double fineAmount = daysLate * 1.0;

        Fine fine = new Fine(0, transaction.getTransactionId(), transaction.getUserId(), fineAmount, "UNPAID");

        fineDAO.insertFine(fine);
    }
}
