package com.service.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommService {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private RelativeService relativeService;

    public void sendMessageToUser(String userId, String message, String userType) {
        switch (userType.toLowerCase()) {
            case "doctor":
                doctorService.sendMessage(userId, message);
                break;
            case "nurse":
                nurseService.sendMessage(userId, message);
                break;
            case "patient":
                patientService.sendMessage(userId, message);
                break;
            case "relative":
                relativeService.sendMessage(userId, message);
                break;
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }
}
