public class CheckingAccount extends Account {
    /**
     * 
     * @param dep
     * @param acctNum
     * @param balance
     */
    public CheckingAccount(Depositor dep, int acctNum, double balance) {
        super(dep, acctNum, "Checking", balance);
    }
    // copy constructor
    public CheckingAccount(CheckingAccount acct) {
        super(acct);
    }
}
