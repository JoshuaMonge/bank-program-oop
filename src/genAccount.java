public abstract class genAccount {
    // getters
    public abstract Depositor getDepositor();
    public abstract int getAccountNumber();
    public abstract String getAccountType();
    public abstract String getAcctStatus();
    public abstract double getAccountBalance();
    //setters
    public abstract void setDepositor(Depositor dep);
    public abstract void setAccountNumber(int acctNum);
    public abstract void setAccountType(String type);
    public abstract void setAcctStatus(String status);
    public abstract void setAccountBalance(double bal);
}
