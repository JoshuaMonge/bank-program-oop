public class Name extends genName {
    private String firstName;
    private String lastName;

    // default constructor
    public Name() {
        firstName = null;
        lastName = null;
    }
    // parametized constructor
    public Name(String first, String last) {
        firstName = first;
        lastName = last;
    }
    // copy constructor
    public Name(Name name) {
        this.firstName = name.firstName;
        this.lastName = name.lastName;
    }
    // getters
    public String getFirst() {
        return firstName;
    }
    public String getLast() {
        return lastName;
    }

    // setters
    public void setFirst(String first) {
        firstName = first;
    }
    public void setLast(String last) {
        lastName = last;
    }
    
    //toString method
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    //equals method
    public boolean equals(Name name) {
        return firstName.equals(name.firstName) && lastName.equals(name.lastName);
    }
}
