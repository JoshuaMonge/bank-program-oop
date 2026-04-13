import java.util.ArrayList;
import java.util.Calendar;

public class Bank {
    private ArrayList<Account> accounts; // active or closed
    private static double totalAmountInSavingsAccts = 0.0;
    private static double totalAmountInCheckingAccts = 0.0;
    private static double totalAmountInCDAccts = 0.0;
    private static double totalAmountInAllAccts = 0.0;

    // default constructor
    public Bank() {
        accounts = new ArrayList<>();
    }
    // getters for static variables
    public static double getTotalInSavings() {
    return totalAmountInSavingsAccts;
    }

    public static double getTotalInChecking() {
        return totalAmountInCheckingAccts;
    }

    public static double getTotalInCD() {
        return totalAmountInCDAccts;
    }

    public static double getTotalInAll() {
        return totalAmountInAllAccts;
    }
    // update total methods
    public static void addToSavingsTotal(double amount) {
        totalAmountInSavingsAccts += amount;
        totalAmountInAllAccts += amount;
    }
    public static void subtractFromSavingsTotal(double amount) {
        totalAmountInSavingsAccts -= amount;
        totalAmountInAllAccts -= amount;
    }
    public static void addToCheckingTotal(double amount) {
        totalAmountInCheckingAccts += amount;
        totalAmountInAllAccts += amount;
    }
    public static void subtractFromCheckingTotal(double amount) {
        totalAmountInCheckingAccts -= amount;
        totalAmountInAllAccts -= amount;
    }
    public static void addToCDTotal(double amount) {
        totalAmountInCDAccts += amount;
        totalAmountInAllAccts += amount;
    }
    public static void subtractFromCDTotal(double amount) {
        totalAmountInCDAccts -= amount;
        totalAmountInAllAccts -= amount;
    }
    // helpful methods
    /**
     * ALLOWS US TO GET ACCOUNT ONLY BY ACCTNUM
     * @param acctNum
     * @return
     */
    public Account getAcctByNum(int acctNum) {
        int index = findAcct(acctNum);
        if (index == -1) {
            return null;
        }
        return getAcct(index);
    }
    /**
     * ALLOWS TO GET ACCT BY SSN
     * @param ssn
     * @return
     */
    public ArrayList<Account> getAcctBySSN(String ssn) {
        ArrayList<Account> matchingAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getDepositor().getSSN().equals(ssn)) {
                if (account instanceof CDAccount CDAcct) {
                    matchingAccounts.add(new CDAccount(CDAcct));
                } else if (account instanceof CheckingAccount CKAcct) {
                    matchingAccounts.add(new CheckingAccount(CKAcct));
                } else if (account instanceof SavingsAccount SVAcct) {
                    matchingAccounts.add(new SavingsAccount(SVAcct));
                }
            }
        }  
        return matchingAccounts;
    }
    /**
     * 
     * @param index
     * @return
     */
    public Account getAcct(int index) {
        if (index >= 0 && index < accounts.size()) {
            if (accounts.get(index) instanceof CDAccount CDAcct) {
                return new CDAccount(CDAcct);
            }
            else if (accounts.get(index) instanceof CheckingAccount CKAcct) {
                return new CheckingAccount(CKAcct);
            } else {
                SavingsAccount SVAcct = (SavingsAccount) accounts.get(index);
                return new SavingsAccount(SVAcct);
            }
        }
        return null;
    }
    /**
     * returns size of accounts array for TOTAL num of accounts (both open and closed)
     * @return
     */
    public int getNumTotalAccts() {
        return accounts.size();
    }
    /**
     * retuns num of ACTIVE accounts
     * @return
     */
    public int getNumActiveAccts() {
        int count = 0;
        for (Account acct : accounts) {
            if (acct.getAcctStatus().equals("Open")) {
                count++;
            }
        } 
        return count;
    }
    /**
     * allows us to verify if account is found in accounts arraylist
     * @param acctNum
     * @return
     */
    private int findAcct(int acctNum) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber() == acctNum) {
                return i;
            }
        }
        return -1;
    }
    // transaction methods
    /**
     * 
     * @param newAcct
     * @return
     */
    public TransactionReceipt openNewAcct (Account newAcct) {
        int acctNum = newAcct.getAccountNumber();
        int index = findAcct(acctNum);
        Calendar today = Calendar.getInstance();
        TransactionTicket ticket = new TransactionTicket(acctNum, today, "Open Account");
        // account number already exists
        if (index != -1) {
            return new TransactionReceipt(ticket, false, "Account number already exists", newAcct.getAccountType(), 0.0); // failed constructor
        }

        accounts.add(newAcct);

        double balance = newAcct.getAccountBalance();
        String type = newAcct.getAccountType();
        if (type.equals("Savings")) {
            addToSavingsTotal(balance);
        } else if (type.equals("Checking")) {
            addToCheckingTotal(balance);
        } else if (type.equals("CD")) {
            addToCDTotal(balance);
        }
        TransactionReceipt receipt;
        if (newAcct instanceof CDAccount) {
            CDAccount cdAcct = (CDAccount) newAcct;
            Calendar maturityDate = cdAcct.getMaturityDate();
            receipt =  new TransactionReceipt(ticket, true, type, 0.0, balance, maturityDate);
        } else {
            receipt = new TransactionReceipt(ticket, true, type, 0.0, balance);
        }
        int accountIndex = findAcct(acctNum);
        accounts.get(accountIndex).addTransaction(receipt);
        
        return receipt;
    }
    /**
     * 
     * @param ticket
     * @return
     */
    public TransactionReceipt getBalance(TransactionTicket ticket){
        int acctNum = ticket.getAccountNumber();
        int index = findAcct(acctNum);
        if (index == -1) {
            return new TransactionReceipt(ticket, false, ("Account number " + acctNum + " not exist"), "", 0.0);
        }
        return accounts.get(index).getBalance(ticket);
    }
    public TransactionReceipt makeDeposit(TransactionTicket ticket) {
        int acctNum = ticket.getAccountNumber();
        int index = findAcct(acctNum);
        
        if (index == -1) {
            return new TransactionReceipt(ticket, false, ("Account number " + acctNum + " not exist"), "", 0.0);
        }
        double oldBalance = accounts.get(index).getAccountBalance();
        String type = accounts.get(index).getAccountType();

        TransactionReceipt receipt = accounts.get(index).makeDeposit(ticket);
        if (receipt.getSuccessIndicatorFlag()) {
            double newBalance = accounts.get(index).getAccountBalance();
            double difference = newBalance - oldBalance;

            if (type.equals("Savings")) {
                addToSavingsTotal(difference);
            } else if (type.equals("Checking")) {
                addToCheckingTotal(difference);
            } else if (type.equals("CD")) {
                addToCDTotal(difference);
            }
        }
        return receipt;
    }
    
    /**
     * 
     * @param ticket
     * @return
     */
    public TransactionReceipt makeWithdrawal(TransactionTicket ticket) {
        int acctNum = ticket.getAccountNumber();
        int index = findAcct(acctNum);
        if (index == -1) {
            return new TransactionReceipt(ticket, false, ("Account number " + acctNum + " not exist"), "", 0.0);
        }
        double oldBalance = accounts.get(index).getAccountBalance();
        String type = accounts.get(index).getAccountType();

        TransactionReceipt receipt = accounts.get(index).makeWithdrawal(ticket);
        if (receipt.getSuccessIndicatorFlag()) {
            double newBalance = accounts.get(index).getAccountBalance();
            double difference = oldBalance - newBalance;

            if (type.equals("Savings")) {
                subtractFromSavingsTotal(difference);
            } else if (type.equals("Checking")) {
                subtractFromCheckingTotal(difference);
            } else if (type.equals("CD")) {
                subtractFromCDTotal(difference);
            }
        }
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
        int index = findAcct(acctNum);
        TransactionTicket ticket = new TransactionTicket(acctNum, todaysDate, "Clear Check");
        TransactionReceipt receipt;
        if (index == -1) {
            return new TransactionReceipt(ticket, false, ("Account number " + acctNum + " not exist"), "", 0.0);
        }
        double oldBalance = accounts.get(index).getAccountBalance();
        if (accounts.get(index) instanceof CheckingAccount CKAcct){
            receipt = CKAcct.clearCheck(check);
            double newBalance = accounts.get(index).getAccountBalance();
            double difference  = oldBalance - newBalance;
            if (difference > 0)
                subtractFromCheckingTotal(difference);
            } else {
            receipt = new TransactionReceipt(ticket, false, "Account is not a Checking account", "", 0.0);
        }
        return receipt;
    }
    /**
     * 
     * @param ticket
     * @return
     */
    public TransactionReceipt closeAcct(TransactionTicket ticket) {
        int acctNum = ticket.getAccountNumber();
        int index = findAcct(acctNum);
        if (index == -1) {
            return new TransactionReceipt(ticket, false, ("Account number " + acctNum + " not exist"), "", 0.0);
        }
        double balance = accounts.get(index).getAccountBalance();
        String type = accounts.get(index).getAccountType();

        TransactionReceipt receipt = accounts.get(index).closeAcct(ticket);

        if (receipt.getSuccessIndicatorFlag()) {
            if (type.equals("Savings")) {
                subtractFromSavingsTotal(balance);
            } else if (type.equals("Checking")) {
                subtractFromCheckingTotal(balance);
            } else if (type.equals("CD")) {
                subtractFromCDTotal(balance);
            }
        }
        return receipt;
    }
    /**
     * 
     * @param ticket
     * @return
     */
    public TransactionReceipt reopenAcct(TransactionTicket ticket) {
        int acctNum = ticket.getAccountNumber();
        int index = findAcct(acctNum);
        if (index == -1) {
            return new TransactionReceipt(ticket, false, ("Account number " + acctNum + " not exist"), "", 0.0);
        }
        double balance = accounts.get(index).getAccountBalance();
        String type = accounts.get(index).getAccountType();

        TransactionReceipt receipt = accounts.get(index).reopenAcct(ticket);
        
        if (receipt.getSuccessIndicatorFlag()) {
            if (type.equals("Savings")) {
                addToSavingsTotal(balance);
            } else if (type.equals("Checking")) {
                addToCheckingTotal(balance);
            } else if (type.equals("CD")) {
                addToCDTotal(balance);
            }
        }
        return receipt;
    }
    public TransactionReceipt deleteAcct(TransactionTicket ticket) {
        int acctNum = ticket.getAccountNumber();
        int index = findAcct(acctNum);
        if (index == -1) {
            return new TransactionReceipt(ticket, false, ("Account number " + acctNum + " not exist"), "", 0.0);
        }
        Account account = accounts.get(index);
        if (account.getAccountBalance() != 0.0) {
            return new TransactionReceipt(ticket, false, String.format("Account not deleted - Account has a non-zero balance ($%.2f)", account.getAccountBalance()), account.getAccountType(), account.getAccountBalance());
        }
        accounts.remove(index);
        return new TransactionReceipt(ticket, true, "", 0.0, 0.0);
    }

}