package edu.ncsu.zybook.domain.model;

public class ActiveCourse {
    private String courseToken;
    private int courseCapacity;
    private Course course;

    public String getCourseToken() {
        return courseToken;
    }

    public void setCourseToken(String courseToken) {
        this.courseToken = courseToken;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getCourseCapacity() {
        return courseCapacity;
    }

    public void setCourseCapacity(int courseCapacity) {
        this.courseCapacity = courseCapacity;
    }
}
