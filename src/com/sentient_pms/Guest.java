package com.sentient_pms;

public class Guest {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String notes;
    private String paymentInfo;

    public Guest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = "";
        this.email = "";
        this.notes = "";
        this.paymentInfo = "";
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setPaymentInfo(String paymentInfo) { this.paymentInfo = paymentInfo; }

    public String getDetails() {
        return "Name: " + getFullName() +
                "\nPhone: " + phone +
                "\nEmail: " + email +
                "\nPayment: " + paymentInfo +
                "\nNotes: " + notes;
    }
}