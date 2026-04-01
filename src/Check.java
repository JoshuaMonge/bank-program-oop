
import java.util.Calendar;

public class Check {
    private int accountNumber;
    private double checkAmount;
    private Calendar dateOfCheck;

    // default constructor
    public Check() {
        accountNumber = 0;
        checkAmount = 0.0;
        dateOfCheck = null;
    }
    // paramaterized constructors
    public Check(int accountNumber, double checkAmount, Calendar dateOfCheck) {
        this.accountNumber = accountNumber;
        this.checkAmount =checkAmount;
        this.dateOfCheck = dateOfCheck;
    }
    // copy constructor
    public Check (Check check) {
        accountNumber = check.accountNumber;
        checkAmount = check.checkAmount;
        dateOfCheck = (Calendar) check.dateOfCheck.clone();
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
    // getters
    public int getAccountNumber() {
        return accountNumber;
    }    
    public double getCheckAmount() {
        return checkAmount;
    }
    public Calendar getDateOfCheck() {
        return (Calendar) dateOfCheck.clone();
    }
    //toString method
    @Override
    public String toString() {
        return String.format("Check[Account: %d, Amount: %f, Date: %s]", accountNumber, checkAmount, formatDate(dateOfCheck));
    }
}
