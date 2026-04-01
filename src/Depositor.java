public class Depositor {
    private Name name;
    private String socialSecutiryNumber;

    // default constructor
    public Depositor() {
        name = new Name();
        socialSecutiryNumber = null;
    }

    // parametized constructor
    public Depositor(String first, String last, String ssn) {
        name = new Name(first, last);
        socialSecutiryNumber = ssn;
    }
    // copy constructors
    public Depositor(Depositor dep) {
        name = new Name(dep.name); // deep copy
        socialSecutiryNumber = dep.socialSecutiryNumber;
    }
    //getters
    public String getFirst() {
        return name.getFirst();
    }
    public String getLast() {
        return name.getLast();
    }
    public String getSSN() {
        return socialSecutiryNumber;
    }
    // toString method
    @Override
    public String toString() {
        return name.toString() + "(SSN: " + socialSecutiryNumber + ")";
    }
    // equals method
    public boolean equals(Depositor dep) {
        return name.equals(dep.name) && socialSecutiryNumber.equals(dep.socialSecutiryNumber);
    }
}
