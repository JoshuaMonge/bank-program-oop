import java.util.Calendar;

public abstract class genTransactionReceipt {
    //getters
    public abstract TransactionTicket getTransactionTicket();
    public abstract boolean getSuccessIndicatorFlag();
    public abstract String getReasonForFailure();
    public abstract String getAccountType();
    public abstract double getPreTransactionBalance();
    public abstract double getPostTransactionBalance();
    public abstract Calendar getPostTransactionMaturityDate();
    //setters
    //setters
    public abstract void setTransactionTicket(TransactionTicket ticket);
    public abstract void setSuccessIndicatorFlag(boolean e);
    public abstract void setReasonForFailure(String reason);
    public abstract void setAccountType(String type);
    public abstract void setPreTransactionBalance(double balance);
    public abstract void setPostTransactionBalance(double balance);
    public abstract void setPostTransactionMaturityDate(Calendar date);
}
