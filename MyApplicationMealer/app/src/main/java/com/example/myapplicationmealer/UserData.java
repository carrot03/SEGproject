package com.example.myapplicationmealer;

public class UserData {

    public enum UserRole {
        COOKER
    }

    public static String roleToString(UserRole role) {
        return "Cooker";
    }

    protected String name;
    protected String email;
    private boolean active;
    protected UserRole role;
    protected String id;

    // Constructor

    public UserData(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserData(String name, UserRole role, String id, String email){
        this.name = name;
        this.role = role;
        this.id = id;
        this.email = email;
        active = true;
    }

    public String getName() {
        return name;
    }
    public UserRole getRole() {
        return role;
    }

    public String getId() {
        return id;
    }
    public String getEmail() {
        return this.email;
    }

    public void invertActive(){
        active = !active;
    }

    public boolean isActive() {
        return active;
    }


    public String toString() {
        return name+ (active ? "": " (DISABLED)") +"\n"+roleToString(role)+"\n"+email;
    }


}