import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner inFile = new Scanner(new File("initAccts.txt"));
        PrintWriter outFile = new PrintWriter("pgmOutput.txt");
        Bank bank = new Bank();
        
        readAccts(bank, inFile);

        Scanner scnr = new Scanner(System.in);

        outFile.println("===== INITIAL ACCOUNT DATABASE =====");
        printAccts(bank, outFile);
        outFile.println();

        char choice = ' ';
        do {
            menu();
            System.out.print("Enter your choice: ");
            String input = scnr.nextLine().trim();
            
            if (input.length() == 0) {
                System.out.println("\nPlease try again.");
                continue;
            }
            choice = input.toUpperCase().charAt(0);

            switch (choice) {
                case 'W':
                    withdrawal(bank, scnr, outFile);
                    break;
                case 'D':
                    deposit(bank, scnr, outFile);
                    break;
                case 'C':
                    clearCheck(bank, scnr, outFile);
                    break;
                case 'N':
                    newAcct(bank, scnr, outFile);
                    break;
                case 'B':
                    balance(bank, scnr, outFile);
                    break;
                case 'I':
                    acctInfo(bank, scnr, outFile);
                    break;
                case 'H':
                    acctInfoHistory(bank, scnr, outFile);
                    break;
                case 'S':
                    closeAcct(bank, scnr, outFile);
                    break;
                case 'R':
                    reopenAcct(bank, scnr, outFile);
                    break;
                case 'X':
                    deleteAcct(bank, scnr, outFile);
                    break;
                case 'Q':
                    System.out.println("Quitting program...");
                    break;
                default:
                    System.out.println("Error: Invalid selection. Please try again.");
            }
            
            System.out.println();
        } while (choice != 'Q');

        outFile.println();
        outFile.println("===== FINAL ACCOUNT DATABASE =====");
        printAccts(bank, outFile);

        outFile.close();
        System.out.println("Program terminated. Output saved to pgmOutput.txt");
    }
    /**
     * READS INITACCTS.TXT AND PLACES EACH RESPECTIVE PART INTO ACCOUNT OBJECT, THEN OPENS ACCOUNT WITH BANK
     * @param bank
     * @param inFile
     */
    public static void readAccts(Bank bank, Scanner inFile) {
        String[] parts;
        while (inFile.hasNextLine()) {
            String line = inFile.nextLine().trim();
            parts = line.split(" ");

            String firstName = parts[0];
            String lastName = parts[1];
            String ssn = parts[2];
            int acctNum = Integer.parseInt(parts[3]);
            String acctType = parts[4];
            double balance = Double.parseDouble(parts[5]);

            Depositor depositor = new Depositor(firstName, lastName, ssn);
            Calendar maturityDate = null;
            
            if (parts.length > 6) {
                String[] dateParts = parts[6].split("/");
                int month = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                maturityDate = Calendar.getInstance();
                maturityDate.set(year, month - 1, day);
            }
            Account account;
            if (acctType.equals("Savings")) {
                account = new SavingsAccount(depositor, acctNum, balance);
            } else if (acctType.equals("Checking")) {
                account = new CheckingAccount(depositor, acctNum, balance);
            } else {
                account = new CDAccount(depositor, acctNum, balance, maturityDate);
            }
            bank.openNewAcct(account);
        }
    }
    /**
     * DISPLAYS MENU
     */
    public static void menu() {
        System.out.println("========================================");
        System.out.println("         BANKING SYSTEM MENU");
        System.out.println("========================================");
        System.out.println("Select one of the following:");
        System.out.println("  W - Withdrawal");
        System.out.println("  D - Deposit");
        System.out.println("  C - Clear Check");
        System.out.println("  N - New account");
        System.out.println("  B - Balance");
        System.out.println("  I - Account Info");
        System.out.println("  H - Account Info with History");
        System.out.println("  S - Close Account");
        System.out.println("  R - Reopen Account");
        System.out.println("  X - Delete Account");
        System.out.println("  Q - Quit");
        System.out.println("========================================");
    }
    /**
     * 
     * @param bank
     * @param outFile
     */
    public static void printAccts(Bank bank, PrintWriter outFile) {   
        outFile.printf("%-15s %-15s %-12s %-10s %-12s %-8s %12s %15s%n",
            "LastName", "FirstName", "SSN", "Acct Num", "Acct Type", "Status", "Balance", "Maturity Date");
        outFile.println("====================================================================================================");
        
        for (int i = 0; i < bank.getNumTotalAccts(); i++) {
            Account account = bank.getAcct(i);
            Depositor depositor = account.getDepositor();

            String maturityString = "N/A";
            if (account instanceof CDAccount) {
                CDAccount cdAcct = (CDAccount) account; 
                maturityString = formatDate(cdAcct.getMaturityDate());
            }
            outFile.printf("%-15s %-15s %-12s %-10d %-12s %-8s $%10.2f %15s%n",
                depositor.getLast(), depositor.getFirst(), depositor.getSSN(),
                account.getAccountNumber(), account.getAccountType(), account.getAcctStatus(),  
                account.getAccountBalance(), maturityString);
        }

        outFile.println("====================================================================================================");
        outFile.printf("Total Accounts: %d (Open: %d, Closed: %d)%n", 
            bank.getNumTotalAccts(), bank.getNumActiveAccts(), 
            (bank.getNumTotalAccts() - bank.getNumActiveAccts()));

        // print static totals
        outFile.println();
        outFile.println("========== ACCOUNT TOTALS BY TYPE ==========");
        outFile.printf("Total in Savings Accounts:  $%,12.2f%n", Bank.getTotalInSavings());
        outFile.printf("Total in Checking Accounts: $%,12.2f%n", Bank.getTotalInChecking());
        outFile.printf("Total in CD Accounts:       $%,12.2f%n", Bank.getTotalInCD());
        outFile.println("                            ---------------");
        outFile.printf("GRAND TOTAL IN BANK:        $%,12.2f%n", Bank.getTotalInAll());
        outFile.println("============================================");
    }
    /**
     * 
     * @param date
     * @return
     */
    public static String formatDate(Calendar date) {
        if (date == null) return "N/A";
        return String.format("%02d/%02d/%d",
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.DAY_OF_MONTH), 
            date.get(Calendar.YEAR));
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void balance(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter account number: ");
        int acctNum = scnr.nextInt();
        scnr.nextLine();

        Calendar today = Calendar.getInstance();

        TransactionTicket ticket = new TransactionTicket(acctNum, today, "Balance Inquiry");
        TransactionReceipt receipt = bank.getBalance(ticket);

        outFile.println("Transaction Requested: Balance Inquiry");
        outFile.println("Account number: " + acctNum);

        outFile.println(receipt.toString());
        System.out.println(receipt.toString());
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void deposit(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter account number: ");
        int acctNum = scnr.nextInt();
        scnr.nextLine();

        Calendar today = Calendar.getInstance();
        
        outFile.println("Transaction Requested: Deposit");
        outFile.println("Accout number: " + acctNum);
        
        Account account = bank.getAcctByNum(acctNum);
        if (account == null) {
            outFile.println("Error: Account does not exist");
            System.out.println("Error: Account does not exist");
            outFile.println();
            return;
        }
        System.out.print("Enter deposit amount: $");
        double depositAmount = scnr.nextDouble();
        scnr.nextLine();
        
        TransactionTicket ticket;

        if(account instanceof CDAccount CDacct) {
            Calendar maturityDate = CDacct.getMaturityDate(); // used instanceof pattern 
            outFile.println("Account type: CD");
        
            if (today.before(maturityDate)) {
                String maturityDateString = formatDate(maturityDate);
                outFile.println("Error: CD maturity date " + maturityDateString + " not reached");
                System.out.println("Error: CD maturity date " + maturityDateString + " not reached");
                outFile.println();
                return;
            }
            System.out.println("Select CD term length: 6, 12, 18, or 24 months");
            System.out.print("Enter term: ");
            int termLength = scnr.nextInt();
            scnr.nextLine();
            ticket = new TransactionTicket(acctNum, today, "Deposit", depositAmount, termLength);
        } else {
            ticket = new TransactionTicket(acctNum, today, "Deposit", depositAmount);
        }
        TransactionReceipt receipt = bank.makeDeposit(ticket);
        outFile.println("Account type: " + account.getAccountType());

        outFile.println("\n"+ receipt.toString());
        System.out.println("\n" + receipt.toString());
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void withdrawal(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter account number: ");
        int acctNum = scnr.nextInt();
        scnr.nextLine();

        Calendar today = Calendar.getInstance();
        Account account = bank.getAcctByNum(acctNum);

        outFile.println("Transaction Requested: Withdrawal");
        outFile.println("Accout number: " + acctNum);

        if (account == null) {
            outFile.println("Error: Account does not exist");
            System.out.println("Error: Account does not exist");
            outFile.println();
            return;
        }

        System.out.print("Enter withdrawal amount: $");
        double withdrawalAmount = scnr.nextDouble();
        scnr.nextLine();

        TransactionTicket ticket;
        if (account instanceof CDAccount CDAcct) {
            Calendar maturityDate = CDAcct.getMaturityDate();
            outFile.println("Account type: CD");
            outFile.printf("Current balance: $%.2f\n", account.getAccountBalance());
            
            if (today.before(maturityDate)) {
                String maturityDateString = formatDate(maturityDate);
                outFile.println("Error: CD maturity date " + maturityDateString + " not reached");
                System.out.println("Error: CD maturity date " + maturityDateString + " not reached");
                outFile.println();
                return;
            }
            System.out.println("Select CD term: 6, 12, 18, or 24 months");
            System.out.print("Enter term: ");
            int termLength = scnr.nextInt();
            scnr.nextLine();

            ticket = new TransactionTicket(acctNum, today, "Withdrawal", withdrawalAmount, termLength);
        } else {
            ticket = new TransactionTicket(acctNum, today, "Withdrawal", withdrawalAmount);
        }
        TransactionReceipt receipt = bank.makeWithdrawal(ticket);
        outFile.println("Account type: " + account.getAccountType());

        outFile.println("\n"+ receipt.toString());
        System.out.println("\n" + receipt.toString());
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void clearCheck(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter your account number: ");
        int acctNum = scnr.nextInt();
        scnr.nextLine();
        Calendar checkDate = Calendar.getInstance();

        System.out.print("Enter check amount: ");
        double checkAmount = scnr.nextDouble();
        scnr.nextLine();

        System.out.print("Enter check date (MM/DD/YYYY): ");
        String checkDateString = scnr.nextLine();
        String[] dateParts = checkDateString.split("/");
        checkDate.set(Integer.parseInt(dateParts[2]), 
                     Integer.parseInt(dateParts[0]) - 1, 
                     Integer.parseInt(dateParts[1]));

        Check check = new Check(acctNum,checkAmount, checkDate);
        TransactionReceipt receipt = bank.clearCheck(check);

        outFile.println("Transaction Requested: Clear Check");
        outFile.println("Account Number: " + acctNum);
                     
        outFile.println("\n"+ receipt.toString());
        System.out.println("\n" + receipt.toString());
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void newAcct(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter new account number: ");
        int acctNum = scnr.nextInt();
        scnr.nextLine();

        outFile.println("Transaction Requested: New Account");
        outFile.println("Account number: " + acctNum);

        if (acctNum < 100000 || acctNum > 999999) {
            outFile.println("Transaction Requested: Open New Account");
            outFile.println("Error: Invalid account number format");
            System.out.println("Error: Account number must be 6 digits");
            outFile.println();
        }

        System.out.print("Enter your first name: ");
        String firstName = scnr.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scnr.nextLine();

        System.out.print("Enter your social security number: ");
        String ssn = scnr.nextLine();

        System.out.print("Enter account type: ");
        String accountType = scnr.nextLine();

        System.out.print("Enter opening balance: $");
        double balance = scnr.nextDouble();
        scnr.nextLine();


        Depositor dep = new Depositor(firstName, lastName, ssn);
        Account account;
        
        if (accountType.equals("CD")) {
            System.out.println("Select CD term: 6, 12, 18, 24");
            System.out.print("Enter term: ");
            int termLength = scnr.nextInt();
            scnr.nextLine();

            Calendar maturityDate = Calendar.getInstance();
            maturityDate.add(Calendar.MONTH, termLength);

            account = new CDAccount(dep, acctNum, balance, maturityDate);
        } else if (accountType.equals("Checking")) {
            account = new CheckingAccount(dep, acctNum, balance);
        } else {
            account = new SavingsAccount(dep, acctNum, balance);
        }
        TransactionReceipt receipt = bank.openNewAcct(account);

        outFile.println("\n"+ receipt.toString());
        System.out.println("\n" + receipt.toString());
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void closeAcct(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter your account number: ");
        int acctNum = scnr.nextInt();
        scnr.nextLine();

        outFile.println("Transaction requested: Close Account");
        outFile.println("Account number: " + acctNum);

        Calendar today = Calendar.getInstance();
        TransactionTicket ticket = new TransactionTicket(acctNum, today, "Close account");
        TransactionReceipt receipt = bank.closeAcct(ticket);

        outFile.println("\n"+ receipt.toString());
        System.out.println("\n" + receipt.toString());
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void reopenAcct(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter account number to reopen: ");
        int acctNum = scnr.nextInt();
        scnr.nextLine();
        Calendar today = Calendar.getInstance();
        TransactionTicket ticket = new TransactionTicket(acctNum, today, "Reopen Account");
        TransactionReceipt receipt = bank.reopenAcct(ticket);

        outFile.println("Transaction requested: Reopen Account");
        outFile.println("Account number: " + acctNum);

        outFile.println("\n"+ receipt.toString());
        System.out.println("\n" + receipt.toString());
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void deleteAcct(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter account number to delete: ");
        int acctNum = scnr.nextInt();
        scnr.nextLine();

        Calendar today = Calendar.getInstance();
        TransactionTicket ticket = new TransactionTicket(acctNum, today, "Delete Account");
        TransactionReceipt receipt = bank.deleteAcct(ticket);

        outFile.println("Transaction requested: Delete Account");
        outFile.println("Account number: " + acctNum);

        outFile.println("\n"+ receipt.toString());
        System.out.println("\n" + receipt.toString());
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void acctInfo(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter SSN: ");
        String ssn = scnr.nextLine();

        ArrayList<Account> matchingAccounts = bank.getAcctBySSN(ssn);
        outFile.println("Transaction Requested: Account Info");
        outFile.println("SSN: " + ssn);

        if (matchingAccounts.size() == 0) {
            outFile.println("Error: No accounts found for SSN");
            System.out.println("Error: No accounts found for SSN");
        } else {
            outFile.printf("%-15s %-15s %-12s %-10s %-12s %-8s %12s %15s%n",
                "Last Name", "First Name", "SSN", "Acct Num", "Acct Type", "Status", "Balance", "Maturity Date");
            outFile.println("====================================================================================================");
            
            System.out.printf("%-15s %-15s %-12s %-10s %-12s %-8s %12s %15s%n",
                "Last Name", "First Name", "SSN", "Acct Num", "Acct Type", "Status", "Balance", "Maturity Date");
            System.out.println("====================================================================================================");
            
            for (Account account : matchingAccounts) {
                Depositor dep = account.getDepositor();
                String maturityString = "N/A";

                if (account instanceof CDAccount CDAcct) {
                    maturityString = formatDate(CDAcct.getMaturityDate());
                }
                outFile.printf("%-15s %-15s %-12s %-10d %-12s %-8s $%10.2f %15s%n",
                    dep.getLast(), dep.getFirst(), dep.getSSN(),
                    account.getAccountNumber(), account.getAccountType(), account.getAcctStatus(),
                    account.getAccountBalance(), maturityString);

                System.out.printf("%-15s %-15s %-12s %-10d %-12s %-8s $%10.2f %15s%n",
                    dep.getLast(), dep.getFirst(), dep.getSSN(),
                    account.getAccountNumber(), account.getAccountType(), account.getAcctStatus(),
                    account.getAccountBalance(), maturityString);
            }
            outFile.println(matchingAccounts.size() + " account(s) were found");
            System.out.println(matchingAccounts.size() + " account(s) were found");
        }
        outFile.println();
    }
    /**
     * 
     * @param bank
     * @param scnr
     * @param outFile
     */
    public static void acctInfoHistory(Bank bank, Scanner scnr, PrintWriter outFile) {
        System.out.print("Enter SSN: ");
        String ssn = scnr.nextLine();
        
        outFile.println("Transaction Requested: Account Info History");
        outFile.println("SSN: " + ssn);
        
        ArrayList<Account> matchingAccounts = bank.getAcctBySSN(ssn);

        if (matchingAccounts.size() == 0) {
            outFile.println("Error: No accounts found for SSN");
            System.out.println("Error: No accounts found for SSN");
        }

        for (Account account : matchingAccounts) {
            Depositor dep = account.getDepositor();
            String maturityDateString = "N/A";
            if (account instanceof CDAccount CDAcct) {
                maturityDateString = formatDate(CDAcct.getMaturityDate());
            }
            outFile.printf("%-15s %-15s %-12s %-10s %-12s %-8s %12s %15s%n",
                "Last Name", "First Name", "SSN", "Acct Num", "Acct Type", "Status", "Balance", "Maturity Date");
            outFile.println("====================================================================================================");
            
            outFile.printf("%-15s %-15s %-12s %-10d %-12s %-8s $%10.2f %15s%n",
                dep.getLast(), dep.getFirst(), dep.getSSN(),
                account.getAccountNumber(), account.getAccountType(), account.getAcctStatus(),
                account.getAccountBalance(), maturityDateString);
            
            Calendar today = Calendar.getInstance();
            TransactionTicket historyTicket = new TransactionTicket(account.getAccountNumber(), today, "Get History");
            ArrayList<TransactionReceipt> history = account.getTransactionReceipts(historyTicket);

            if (history.size() > 0) {
                outFile.println("***** Account Transactions *****");
                outFile.printf("%-12s %-25s %10s %-8s %10s %-50s%n",
                    "Date", "Transaction", "Amount", "Status", "Balance", "Reason For Failure");
                
                for (TransactionReceipt receipt : history) {
                    TransactionTicket ticket = receipt.getTransactionTicket();
                    
                    String date = formatDate(ticket.getDateOfTransaction());
                    String transType = ticket.getTypeOfTransaction();
                    double amount = ticket.getAmountOfTransaction();
                    String status = receipt.getSuccessIndicatorFlag() ? "Done" : "Failed";
                    double balance = receipt.getPostTransactionBalance();
                    String reason = receipt.getSuccessIndicatorFlag() ? "" : receipt.getReasonForFailure();
                    
                    outFile.printf("%-12s %-25s $%9.2f %-8s $%9.2f %-50s%n",
                        date, transType, amount, status, balance, reason);
                }
            }
            outFile.println();
        }
        outFile.println(matchingAccounts.size() + " account(s) were found");
        System.out.println(matchingAccounts.size() + " account(s) with history displayed");
        outFile.println();
            
        }
    }

