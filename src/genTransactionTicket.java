import java.util.Calendar;

public abstract class genTransactionTicket {
    // getters
    public abstract int getAccountNumber();
    public abstract Calendar getDateOfTransaction();
    public abstract String getTypeOfTransaction();
    public abstract double getAmountOfTransaction();
    public abstract int getTermOfCD();
    // setters
    public abstract void setAccountNumber(int acctNum);
    public abstract void setDateOfTransaction(Calendar date);
    public abstract void setTypeOfTransaction(String type);
    public abstract void setAmountOfTransaction(double amount);
    public abstract void setTermOfCD(int term);
}   