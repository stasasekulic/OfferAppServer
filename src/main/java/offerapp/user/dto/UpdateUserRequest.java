package offerapp.user.dto;

import offerapp.user.enums.UserRole;

public class UpdateUserRequest {
    private String name;
    private String surname;
    private String phoneNumber;
    private UserRole role;

    public UpdateUserRequest(String name, String surname, String phoneNumber, UserRole role) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
