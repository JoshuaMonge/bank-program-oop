import java.util.Calendar;

public class CheckingAccount extends Account {
    public CheckingAccount(Depositor dep, int acctNum, double balance) {
        super(dep, acctNum, "Checking", balance);
    }
    // copy constructor
    public CheckingAccount(CheckingAccount acct) {
        super(acct);
    }

    private void applyLowBalanceFee(double oldBalance) {
        if (oldBalance < 2500) {
            accountBalance -= 1.50;
        }
    }
    @Override
    public TransactionReceipt makeWithdrawal(TransactionTicket ticket) {
        double oldBalance = accountBalance;
        TransactionReceipt receipt;
        receipt = super.makeWithdrawal(ticket);
        if (receipt.getSuccessIndicatorFlag()) {
            applyLowBalanceFee(oldBalance);
            receipt.setPostTransactionBalance(accountBalance);
        }
        return receipt;
    }
    public TransactionReceipt clearCheck(Check check) {
        double oldBalance = accountBalance;
        int acctNum = check.getAccountNumber();
        Calendar todaysDate = Calendar.getInstance();
        Calendar dateOfCheck = check.getDateOfCheck();
        double amount = check.getCheckAmount();
        TransactionReceipt receipt;
        TransactionTicket ticket = new TransactionTicket(acctNum, todaysDate, "Clear check", amount);
        
        if (accountStatus.equals("Closed")) {
            receipt = new TransactionReceipt(ticket, false, "Account is closed - No transaction allowed", "Checking", accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        if (dateOfCheck.after(todaysDate)) {
            String checkDateString = formatDate(dateOfCheck);
            receipt = new TransactionReceipt(ticket, false, String.format("Check not cleared - Post-dated check: %s", checkDateString), "Checking", accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        Calendar sixMonthsAgo = Calendar.getInstance();
        sixMonthsAgo.add(Calendar.MONTH, -6);
        if (dateOfCheck.before(sixMonthsAgo)) {
            String checkDateString = formatDate(dateOfCheck);
            receipt = new TransactionReceipt(ticket, false, String.format("Check not cleared - Check is too old (%s)", checkDateString), "Checking", accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        if (amount > accountBalance) {
            accountBalance -= BOUNCE_FEE;
            receipt = new TransactionReceipt(ticket, false, "Check not cleared - Insufficient Funds ($2.50 Bounce Fee Incurred)", "Checking", oldBalance, accountBalance);
            addTransaction(receipt);
            return receipt;
        }
        accountBalance -= amount;
        applyLowBalanceFee(oldBalance);
        receipt = new TransactionReceipt(ticket, true, "Checking", oldBalance, accountBalance);
        addTransaction(receipt);
        return receipt;
    }
}

