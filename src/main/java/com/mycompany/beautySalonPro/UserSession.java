package com.mycompany.beautySalonPro;

public class UserSession {
    private static String username;
    private static String firstName;
    private static String lastName;
    private static String contact;

    
    public static void setUsername(String user) { username = user; }
    public static void setFirstName(String fname) { firstName = fname; }
    public static void setLastName(String lname) { lastName = lname; }
    public static void setContact(String cont) { contact = cont; }

    
    public static String getUsername() { return username; }
    public static String getFirstName() { return firstName; }
    public static String getLastName() { return lastName; }
    public static String getContact() { return contact; }

    
    public static void clearSession() {
        username = null;
        firstName = null;
        lastName = null;
        contact = null;
    }
}
