import java.util.ArrayList;
import java.util.Calendar;

public class Account {
    private Depositor depositor;
    private int accountNumber;
    private String accountType;
    private String accountStatus; // "Open", "Closed"
    protected double accountBalance;
    private ArrayList<TransactionReceipt> receipts; // transaction history

    final double BOUNCE_FEE = 2.50;

    // default constructor
    public Account() {
        depositor = new Depositor();
        accountNumber = 0;
        accountType = null;
        accountStatus = null;
        accountBalance = 0;
        receipts = new ArrayList<>();
    }
    // parametized constructor
    /**
     * USED FOR ACCOUNT W/O MATURITY DATE
     * @param dep
     * @param acctNum
     * @param type
     * @param balance
     */
    public Account(Depositor dep, int acctNum, String type, double balance) {
        depositor = dep;
        accountNumber = acctNum;
        accountType = type;
        accountStatus = "Open";
        accountBalance = balance;
        receipts = new ArrayList<>();
    }
    //copy constructor
    public Account(Account acct) {
        depositor = new Depositor(acct.depositor); // deep copy
        accountNumber = acct.accountNumber;
        accountType = acct.accountType;
        accountStatus = acct.accountStatus;
        accountBalance = acct.accountBalance;

        receipts = new ArrayList<>();
        for (TransactionReceipt receipt : acct.receipts) { //deep copy
            this.receipts.add(new TransactionReceipt(receipt));
        }

    }
    //getters
    public ArrayList<TransactionReceipt> getTransactionReceipts(TransactionTicket ticket) {
        ArrayList<TransactionReceipt> copy = new ArrayList<>();
        for (TransactionReceipt receipt : receipts) {
            copy.add(new TransactionReceipt(receipt));
        }
        return copy;
    }

    public Depositor getDepostior() {
        return new Depositor(depositor);
    }
    public int getAccountNumber() {
        return accountNumber;
    }
    public String getAccountType() {
        return accountType;
    }
    public String getAcctStatus() {
        return accountStatus;
    }
    public double getAccountBalance() {
        return accountBalance;
    }

    
    // transaction methods
    public TransactionReceipt getBalance(TransactionTicket ticket) {
        TransactionReceipt receipt = new TransactionReceipt(ticket, true, accountType, accountBalance, accountBalance);
        addTransaction(receipt);
        return receipt;
    }

    // transaction methods functionality
    /**
     * DEPOSIT FUNCTIONALITY
     * @param ticket
     * @return
     */
    public TransactionReceipt makeDeposit(TransactionTicket ticket) {
        double depositAmount = ticket.getAmountOfTransaction();
        double oldBalance = accountBalance;
        TransactionReceipt receipt;

        if (accountStatus.equals("Closed")) {
            receipt = new TransactionReceipt(ticket, false, "Account is closed - No transaction allowed", accountType, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        if (depositAmount < 0) {
            receipt = new TransactionReceipt(ticket, false, String.format("Deposit amount $%.2f must be greater than 0", depositAmount), accountType, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        accountBalance += depositAmount;
        receipt = new TransactionReceipt(ticket, true, accountType, oldBalance, accountBalance);
        addTransaction(receipt);
        return receipt;
    }
    /**
     * 
     * @param ticket
     * @return
     */
    public TransactionReceipt makeWithdrawal(TransactionTicket ticket) {
        double withdrawalAmount = ticket.getAmountOfTransaction();
        double oldBalance = accountBalance;
        TransactionReceipt receipt;

        if (accountStatus.equals("Closed")) {
            receipt = new TransactionReceipt(ticket, false, "Account is closed - No transaction allowed", accountType, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        if (withdrawalAmount < 0) {
            receipt = new TransactionReceipt(ticket, false, String.format("Withdrawal amount $%.2f must be greater than 0", withdrawalAmount), accountType, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        if (withdrawalAmount > accountBalance) {
            receipt = new TransactionReceipt(ticket, false, "Insufficient funds available", accountType, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        accountBalance -= withdrawalAmount;
        receipt = new TransactionReceipt(ticket, true, accountType, oldBalance, accountBalance);
        addTransaction(receipt);
        return receipt;
    }  
    /**
     * 
     * @param check
     * @return
     */
    public TransactionReceipt clearCheck(Check check) {
        int acctNum = check.getAccountNumber();
        Calendar todaysDate = Calendar.getInstance();
        Calendar dateOfCheck = check.getDateOfCheck();
        double amount = check.getCheckAmount();
        TransactionReceipt receipt;
        double oldBalance = accountBalance;
        TransactionTicket ticket = new TransactionTicket(acctNum, todaysDate, "Clear check", amount);
        
        if (accountStatus.equals("Closed")) {
            receipt = new TransactionReceipt(ticket, false, "Account is closed - No transaction allowed", accountType, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        if (dateOfCheck.after(todaysDate)) {
            String checkDateString = formatDate(dateOfCheck);
            receipt = new TransactionReceipt(ticket, false, String.format("Check not cleared - Post-dated check: %s", checkDateString), accountType, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        Calendar sixMonthsAgo = Calendar.getInstance();
        sixMonthsAgo.add(Calendar.MONTH, -6);
        if (dateOfCheck.before(sixMonthsAgo)) {
            String checkDateString = formatDate(dateOfCheck);
            receipt = new TransactionReceipt(ticket, false, String.format("Check not cleared - Check is too old (%s)", checkDateString), accountType, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        if (amount > accountBalance) {
            oldBalance = accountBalance;
            accountBalance -= BOUNCE_FEE;
            receipt = new TransactionReceipt(ticket, false, "Check not cleared - Insufficient Funds ($2.50 Bounce Fee Incurred)", accountType, oldBalance, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        accountBalance -= amount;
        receipt = new TransactionReceipt(ticket, true, accountType, oldBalance, accountBalance);
        addTransaction(receipt);
        return receipt;
    }
    /**
     * 
     * @param ticket
     * @return
     */
    public TransactionReceipt closeAcct(TransactionTicket ticket) {
        TransactionReceipt receipt;
        if (accountStatus.equals("Closed")) {
            receipt = new TransactionReceipt(ticket, false, "Account is already closed", accountBalance, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        accountStatus = "Closed";
        receipt = new TransactionReceipt(ticket, true, accountType, accountBalance, accountBalance);
        addTransaction(receipt);
        return receipt;
    }
    /**
     * 
     * @param ticket
     * @return
     */
    public TransactionReceipt reopenAcct(TransactionTicket ticket) {
        TransactionReceipt receipt;
        if (accountStatus.equals("Open")) {
            receipt = new TransactionReceipt(ticket, false, "Account is already open", accountBalance, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        accountStatus = "Open";
        receipt = new TransactionReceipt(ticket, true, accountType, accountBalance, accountBalance);
        addTransaction(receipt);
        return receipt;
    }

    // utility methods
    // stores history of receipts 
    /**
     * 
     * @param receipt
     */
    public void addTransaction(TransactionReceipt receipt) {
        receipts.add(receipt);    
    }
    /**
     * RETURNS DATE AS A STRING -> "MM/DD/YYYY"
     * @param date
     * @return
     */
    public String formatDate(Calendar date) {
        if (date == null) return "N/A";
        return String.format("%02d/%02d/%d",
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.DAY_OF_MONTH), 
            date.get(Calendar.YEAR) 
        );
    }
    // toString method
    public String toString() {
        return String.format("Account[Account Num: %d, Name: %s %s, Type: %s, Status: %s, Balance: $%.2f\n",
        accountNumber, 
        depositor.getFirst(), 
        depositor.getLast(), 
        accountType, 
        accountStatus, 
        accountBalance);
    }
    public boolean equals(Account acct) {
        return depositor.equals(acct.depositor) &&
        accountNumber == acct.accountNumber &&
        accountType.equals(acct.accountType) &&
        accountStatus.equals(acct.accountStatus) &&
        accountBalance == acct.accountBalance &&
        receipts.equals(acct.receipts);
    }
}