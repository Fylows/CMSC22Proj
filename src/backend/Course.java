package backend;

import java.io.Serializable;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	private String courseCode; // Code for the course (ex: CMSC 22)
	private String courseTitle; // Name of the course (ex: Object-Oriented Programming)
	private String type; // 
	private int units; // Total units for the course subject
	
	// NOTE: I think missing pa sha ng description?
	
	// Constructor
	public Course (String code, String title, int units){
		this.courseCode = code;
		this.courseTitle = title;
		this.units = units;
		this.type = CourseManager.getCourseDegree(code);
	}
	
	// View State
	public void viewState() {
		System.out.printf("Course code: %s, Course title: %s, units: %d\n", this.courseCode, this.courseTitle, this.units);
	}
	
	// Getters
	public String getType() { return this.type; }
	public String getCourseCode() { return courseCode; }
	public String getCourseTitle() { return courseTitle; }
	public int getUnits() { return units; }

}
