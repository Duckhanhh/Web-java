package org.bpm.abcbook.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Data;
import org.bpm.abcbook.dto.response.AuthenticationResponse;
import org.bpm.abcbook.dto.response.StaffResponse;
import org.bpm.abcbook.model.Staff;

import java.io.Serializable;

@Named
@SessionScoped
@Data
public class UserSessionBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private StaffResponse currentStaff;
    private String token;
    private AuthenticationResponse authResponse;
    private boolean authenticated = false;

    public void setUserSession(StaffResponse staff, String token, AuthenticationResponse authResponse) {
        this.currentStaff = staff;
        this.token = token;
        this.authResponse = authResponse;
        this.authenticated = true;
    }

    public void clearSession() {
        this.currentStaff = null;
        this.token = null;
        this.authResponse = null;
        this.authenticated = false;
    }

    public boolean isAuthenticated() {
        return authenticated && currentStaff != null;
    }

    public String getcurrentStaffEmail() {
        return currentStaff != null ? currentStaff.getEmail() : null;
    }

    public String getcurrentStaffName() {
        return currentStaff != null ? currentStaff.getFirstName() : null;
    }
}