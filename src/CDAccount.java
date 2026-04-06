import java.util.Calendar;

public class CDAccount extends Account {
    protected Calendar maturityDate;

    // getter
    public Calendar getMaturityDate() {
        return maturityDate != null ? (Calendar)maturityDate.clone() : null;
    }
    /**
     * 
     * @param dep
     * @param acctNum
     * @param balance
     * @param date
     */
    public CDAccount(Depositor dep, int acctNum, double balance, Calendar date) {
        super(dep, acctNum, "CD", balance);
        maturityDate = date;
    }
    // copy constructor
    public CDAccount(CDAccount acct) {
        super(acct);
        this.maturityDate = (Calendar) acct.maturityDate.clone();
    }
    
    @Override
    /**
     * @param ticket
     */
    public TransactionReceipt makeDeposit(TransactionTicket ticket) {
        TransactionReceipt receipt;
        Calendar transDate = ticket.getDateOfTransaction();
        
        if (transDate.before(maturityDate)) {
            receipt = new TransactionReceipt(ticket, false,"Error - Maturity date not reached", "CD", accountBalance);
        } else {
            int term = ticket.getTermOfCD();
            Calendar newMaturityDate = Calendar.getInstance();
            newMaturityDate.add(Calendar.MONTH, term);
            maturityDate = newMaturityDate; 
            receipt = super.makeDeposit(ticket);
        }
        return receipt;
    }
    @Override
    /**
     * @param ticket
     */
    public TransactionReceipt makeWithdrawal(TransactionTicket ticket) {
        TransactionReceipt receipt;
        Calendar transDate = ticket.getDateOfTransaction();

        if (transDate.before(maturityDate)) {
            receipt = new TransactionReceipt(ticket, false, "Error - Maturity date not reached", "CD", accountBalance);
        } else {
            Calendar newMaturityDate = Calendar.getInstance();
            int term = ticket.getTermOfCD();
            newMaturityDate.add(Calendar.MONTH, term);
            maturityDate = newMaturityDate;
            receipt = super.makeWithdrawal(ticket);
        }
        return receipt;
    }
}
