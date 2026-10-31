package dev.kaldiroglu.dp.creational.ch01.employee.better;

public class ContactInfo {
    private String email;
    private String phoneNumber;
    private String emergencyContact;
    private String emergencyPhone;

    // Parameterized constructor - null-safe assignment
    public ContactInfo(String email, String phoneNumber, String emergencyContact, String emergencyPhone) {
        this.email = (email == null) ? "" : email;
        this.phoneNumber = (phoneNumber == null) ? "" : phoneNumber;
        this.emergencyContact = (emergencyContact == null) ? "" : emergencyContact;
        this.emergencyPhone = (emergencyPhone == null) ? "" : emergencyPhone;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
}