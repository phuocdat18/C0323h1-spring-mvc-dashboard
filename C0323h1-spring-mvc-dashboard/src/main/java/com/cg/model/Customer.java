package com.cg.model;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "customers")
public class Customer extends BasseEntity implements Validator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String address;

    @Column(precision = 10, scale = 0, nullable = false)
//    , updatable = false
    private BigDecimal balance;

    public Customer() {
    }

    public Customer(Long id, String fullName, String email, String phone, String address, BigDecimal balance) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;

        String fullName = customer.fullName;

        if (fullName.length() == 0) {
            errors.rejectValue("fullName", "fullName.empty" );
        }
        else {
            if (!fullName.matches("^[a-zA-Z\\s]{7,30}$")) {
                errors.rejectValue("fullName", "fullName.matches");
            }
        }

        String email = customer.email;
        if (email.length() == 0) {
            errors.rejectValue("email","email.empty");
        }
        else {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                errors.rejectValue("email", "email.matches");
            }
        }

        String address = customer.address;

        if (address.length() == 0) {
            errors.rejectValue("address", "address.empty" );
        }

        String phone = customer.phone;

        if (phone.length() == 0) {
            errors.rejectValue("phone", "phone.empty" );
        }
        else {
            if (!phone.matches("^\\+\\d{1,2}\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}$")) {
                errors.rejectValue("phone", "phone.matches");
            }
        }
    }

}