package org.bpm.abcbook.service;

import org.bpm.abcbook.dto.request.StaffRequest;
import org.bpm.abcbook.repository.StaffRepo;

import org.bpm.abcbook.dto.response.StaffResponse;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.mapper.StaffMapper;
import org.bpm.abcbook.model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StaffService {
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private StaffMapper staffMapper;
    
    public Staff getUserById(Long id) {
        return staffRepo.findById(id).orElse(null);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public List<StaffResponse> getAllStaffs() {
        return staffRepo.findAll().stream().map(staffMapper::toStaffResponse).toList();
    }

    public void createStaff(StaffRequest staffData) throws AppException {
        if (staffData == null) {
            throw new AppException("CU00001", "create.user.data.empty");
        }

        if (staffRepo.existsByEmail(staffData.getEmail())) {
            throw new AppException("CU00002", "create.user.email.existed");
        }

        if (staffRepo.existsByPhoneNumber(staffData.getPhoneNumber())) {
            throw new AppException("CU00003", "create.user.phoneNumber.existed");
        }

        //Encode password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(staffData.getPassword());
        staffData.setPassword(encodedPassword);

        Staff staff = staffMapper.staffRequestToStaff(staffData);
        staff.setRegistrationDate(new Date());
        staffRepo.save(staff);
    }
}
