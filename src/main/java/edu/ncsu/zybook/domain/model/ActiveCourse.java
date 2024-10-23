package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ActiveCourse extends Course {

    private String courseToken;
    private int courseCapacity;
    private List<User> users;
//    private Course course;
}
