
import java.util.Calendar;

public class TransactionReceipt {
    private TransactionTicket ticket;
    private boolean successIndicatorFlag;
    private String reasonForFailure;
    private String accountType;
    private double preTransactionBalance;
    private double postTransactionBalance;
    private Calendar postTransactionMaturityDate; // only for cd
    
    // default constructor
    public TransactionReceipt() {
        ticket = new TransactionTicket();
        successIndicatorFlag = false;
        reasonForFailure = null;
        accountType = null;
        preTransactionBalance = 0;
        postTransactionBalance = 0;
        postTransactionMaturityDate = null;
    }
    // paramaterized constructors per possible outcome (successful, successful cd, failure)
    /**
     * USED FOR SUCCESSFUL TRANSACTIONS (NON CD)
     * @param ticket
     * @param successIndicatorFlag
     * @param accountType
     * @param preTransactionBalance
     * @param postTransactionBalance
     */
    public TransactionReceipt(
                                TransactionTicket ticket, 
                                boolean successIndicatorFlag,
                                String accountType,
                                double preTransactionBalance,
                                double postTransactionBalance
                            ) {
        this.ticket = ticket;
        this.successIndicatorFlag = successIndicatorFlag;
        this.accountType = accountType;
        this.preTransactionBalance = preTransactionBalance;
        this.postTransactionBalance = postTransactionBalance;
    }
    /**
     * USED FOR SUCCESSFULL TRANSACTION (CD SPECIFIC)
     * @param ticket
     * @param successIndicatorFlag
     * @param accountType
     * @param preTransactionBalance
     * @param postTransactionBalance
     * @param postTransactionMaturityDate
     */
    public TransactionReceipt(
                                TransactionTicket ticket, 
                                boolean successIndicatorFlag,
                                String accountType,
                                double preTransactionBalance,
                                double postTransactionBalance,
                                Calendar postTransactionMaturityDate
                            ) {
        this.ticket = ticket;
        this.successIndicatorFlag = successIndicatorFlag;
        this.accountType = accountType;
        this.preTransactionBalance = preTransactionBalance;
        this.postTransactionBalance = postTransactionBalance;
        this.postTransactionMaturityDate = postTransactionMaturityDate;
    }
    /**
     * USED FOR FAILED TRANSACTIONS
     * @param ticket
     * @param successIndicatorFlag
     * @param reasonForFailure
     * @param accountType
     * @param preTransactionBalance
     * @param postTransactionBalance
     */
    public TransactionReceipt(
                                TransactionTicket ticket, 
                                boolean successIndicatorFlag,
                                String reasonForFailure,
                                String accountType,
                                double preTransactionBalance,
                                double postTransactionBalance
                            ) {
        this.ticket = ticket;
        this.successIndicatorFlag = successIndicatorFlag;
        this.reasonForFailure = reasonForFailure;
        this.accountType = accountType;
        this.preTransactionBalance = preTransactionBalance;
        this.postTransactionBalance = postTransactionBalance;
    }
    /**
     * USED FOR FAILED TRANSACTIONS (with single balance)
     * @param ticket
     * @param successIndicatorFlag
     * @param reasonForFailure
     * @param accountType
     * @param balance
     */
    public TransactionReceipt(
                                TransactionTicket ticket, 
                                boolean successIndicatorFlag,
                                String reasonForFailure,
                                String accountType,
                                double balance
                            ) {
        this.ticket = ticket;
        this.successIndicatorFlag = successIndicatorFlag;
        this.reasonForFailure = reasonForFailure;
        this.accountType = accountType;
        this.preTransactionBalance = balance;
        this.postTransactionBalance = balance;
    }
    // copy constructor
    public TransactionReceipt(TransactionReceipt receipt) {
        ticket = new TransactionTicket(receipt.ticket); // deep copy
        successIndicatorFlag = receipt.successIndicatorFlag;
        reasonForFailure = receipt.reasonForFailure;
        accountType = receipt.accountType;
        preTransactionBalance = receipt.preTransactionBalance;
        postTransactionBalance = receipt.postTransactionBalance;
        postTransactionMaturityDate = (receipt.postTransactionMaturityDate != null) ? (Calendar) 
            receipt.postTransactionMaturityDate.clone() : null; // checking if there is maturity date to begin with
    }
    // getters
    public TransactionTicket getTransactionTicket() {
        return ticket;
    }
    public boolean getSuccessIndicatorFlag() {
        return successIndicatorFlag;
    }
    public String getReasonForFailure() {
        return reasonForFailure;
    }
    public String getAccountType() {
        return accountType;
    }
    public double getPreTransactionBalance() {
        return preTransactionBalance;
    }
    public double getPostTransactionBalance() {
        return postTransactionBalance;
    }
    public Calendar getPostTransactionMaturityDate() {
        return (Calendar) postTransactionMaturityDate.clone();
    }
    // to string method
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== TRANSACTION RECEIPT ==========\n");
        sb.append("Transaction: ").append(ticket.getTypeOfTransaction()).append("\n");
        sb.append("Account Number: ").append(ticket.getAccountNumber()).append("\n");
    
        if (accountType != null) {
            sb.append("Account Type: ").append(accountType).append("\n");
        }
        if (successIndicatorFlag) {
            sb.append("Status: SUCCESS\n");

            if (ticket.getAmountOfTransaction() > 0) {
                sb.append(String.format("Transaction Amount: $%.2f\n", ticket.getAmountOfTransaction()));
            }

            sb.append(String.format("Old Balance: $%.2f\n", preTransactionBalance));
            sb.append(String.format("New Balance: $%.2f\n", postTransactionBalance));

            if (postTransactionMaturityDate != null) {
                sb.append("New Maturity Date: ").append(formatDate(postTransactionMaturityDate)).append("\n");
            }
        } else {
            if (ticket.getTypeOfTransaction().equals("Clear check") 
                && reasonForFailure.equals("Check not cleared - Insufficient Funds ($2.50 Bounce Fee Incurred)")) {
                    sb.append("Status: FAILED\n");
                    sb.append("Reason: ").append(reasonForFailure).append("\n");
                    sb.append(String.format("Current Balance: $%.2f\n", postTransactionBalance));

            } else {
                sb.append("Status: FAILED\n");
                sb.append("Reason: ").append(reasonForFailure).append("\n");
                sb.append(String.format("Current Balance: $%.2f\n", preTransactionBalance));
            }
        }
        sb.append("=========================================");
        return sb.toString();
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
