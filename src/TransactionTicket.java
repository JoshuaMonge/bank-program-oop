
import java.util.Calendar;

public class TransactionTicket {
    private int accountNumber;
    private Calendar dateOfTransaction;
    private String typeOfTransaction;
    private double amountOfTransaction;
    private int termOfCD;

    // default constructor
    public TransactionTicket() {
        accountNumber = 0;
        dateOfTransaction = null;
        typeOfTransaction = null;
        amountOfTransaction = 0;
        termOfCD = 0;
    }
    // paramterized constructor per each possible outcome
    /**
     * USED FOR REQUESTS WITH AN AMOUNT
     * @param accountNumber
     * @param dateOfTransaction
     * @param typeOfTransaction
     * @param amountOfTransaction
     */
    public TransactionTicket(
                            int accountNumber,
                            Calendar dateOfTransaction,
                            String typeOfTransaction,
                            double amountOfTransaction
                            ) {
        this.accountNumber = accountNumber;
        this.dateOfTransaction = dateOfTransaction;
        this.typeOfTransaction = typeOfTransaction;
        this.amountOfTransaction = amountOfTransaction;
    }
    /**
     * USED FOR REQUESTS WITH NO AMOUNT
     * @param accountNumber
     * @param dateOfTransaction
     * @param typeOfTransaction
     */
    public TransactionTicket(
                            int accountNumber,
                            Calendar dateOfTransaction,
                            String typeOfTransaction
                            ) {
        this.accountNumber = accountNumber;
        this.dateOfTransaction = dateOfTransaction;
        this.typeOfTransaction = typeOfTransaction;
    }
    /**
     * USED FOR CD REQUESTS THAT HAVE TERM
     * @param accountNumber
     * @param dateOfTransaction
     * @param typeOfTransaction
     * @param amountOfTransaction
     * @param termOfCD
     */
    public TransactionTicket(
                            int accountNumber,
                            Calendar dateOfTransaction,
                            String typeOfTransaction,
                            double amountOfTransaction,
                            int termOfCD
                            ) {
        this.accountNumber = accountNumber;
        this.dateOfTransaction = dateOfTransaction;
        this.typeOfTransaction = typeOfTransaction;
        this.amountOfTransaction = amountOfTransaction;
        this.termOfCD = termOfCD;
    }
    // copy constructor
    public TransactionTicket(TransactionTicket ticket) {
        accountNumber = ticket.accountNumber;
        dateOfTransaction = (Calendar) ticket.dateOfTransaction.clone();
        typeOfTransaction = ticket.typeOfTransaction;
        amountOfTransaction = ticket.amountOfTransaction;
        termOfCD = ticket.termOfCD;
    }
    // getters
    public int getAccountNumber() {
        return accountNumber;
    }
    public Calendar getDateOfTransaction() {
        return dateOfTransaction;
    }
    public String getTypeOfTransaction() {
        return typeOfTransaction;
    }
    public double getAmountOfTransaction() {
        return amountOfTransaction;
    }
    public int getTermOfCD() {
        return termOfCD;
    }
    @Override
    public String toString() {
        if (termOfCD > 0) {
            return String.format("TransactionTicket[Account: %d, Date: %s, Type: %s, Amount: %.2f, CD Term: %d months]", 
            accountNumber, formatDate(dateOfTransaction), typeOfTransaction, amountOfTransaction, termOfCD);
        } else if (amountOfTransaction > 0) {
            return String.format("TransactionTicket[Account: %d, Date: %s, Type: %s, Amount: %.2f]", 
            accountNumber, formatDate(dateOfTransaction), typeOfTransaction, amountOfTransaction);
        } else {
            return String.format("TransactionTicket[Account: %d, Date: %s, Type: %s]", 
            accountNumber, formatDate(dateOfTransaction), typeOfTransaction);
        }
    }
    // format date
    private String formatDate(Calendar date) {
        String dateString = String.format("%02d/%02d/%d", 
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.YEAR)
        );
        return dateString;
    }
}
