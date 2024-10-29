package edu.ncsu.zybook.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@NoArgsConstructor
public class UserRegistersCourseDTO {
    Timestamp enrollmentDate;
    String approvalStatus;
    Integer userId;
    String courseId;
}
