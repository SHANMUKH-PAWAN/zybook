package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class Course {
    protected String courseId;
    protected String title;
    protected Date startDate;
    protected Date endDate;
    protected String courseType;
    protected Textbook textbook; // Reference to Textbook entity
    protected User user;



}
