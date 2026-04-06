public class SavingsAccount extends Account {
    public SavingsAccount(Depositor dep, int acctNum, double balance) {
        super(dep, acctNum, "Savings", balance);
   }
   // copy constructor
    public SavingsAccount(SavingsAccount acct) {
        super(acct);
   }
}
