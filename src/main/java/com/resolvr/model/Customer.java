
package com.resolvr.model;

public class Customer {

    private int customerId;
    private String name;
    private String phone;
    private String email;
    private String licenseNo;

    public Customer(int customerId, String name, String phone,
                    String email, String licenseNo) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.licenseNo = licenseNo;
    }

    public Customer(String name, String phone,
                    String email, String licenseNo) {
        this(0, name, phone, email, licenseNo);
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
}
