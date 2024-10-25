package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
@Data
@NoArgsConstructor
public class UserRegistersCourse {
    private Timestamp enrollmentDate;
    private String approvalStatus;
    private int userId;
    private String courseId;
}
