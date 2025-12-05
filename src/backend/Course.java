package backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	private String courseCode; // Code for the course (ex: CMSC 22)
	private String courseTitle; // Name of the course (ex: Object-Oriented Programming)
	private String type; // In which degree does the course belong to (ex: BSCS, MSCS)
	private int units; // Total units for the course subject
	private String description;  // Course description from CSV
	private ArrayList<String> prereq = new ArrayList<String>();
	
	// Constructor
	public Course (String code, String title, int units, String description){
		this.courseCode = code;
		this.courseTitle = title;
		this.units = units;
		this.description = description;
		this.type = CourseManager.courseDegreeMap.get(code).name();
	}

	public void addPrereq(String course) { prereq.add(course); }
	
	// View State
	public void viewState() {
		System.out.printf("Course code: %s, title: %s, units: %d, description: %s\n", this.courseCode, this.courseTitle, this.units, this.description);
	}
	
	// Getters
	public ArrayList<String> getPrerequisites() {
		if (prereq.isEmpty() && CourseManager.prereqEnforcedMap.containsKey(courseCode)) {
			prereq.addAll(CourseManager.prereqEnforcedMap.get(courseCode));
		}
		return prereq;
	}
	
	public List<String> getEnforcedPrerequisites() {
		return CourseManager.prereqEnforcedMap.getOrDefault(courseCode, List.of());
	}
	
	public String getType() { return this.type; }
	public String getCourseCode() { return courseCode; }
	public String getCourseTitle() { return courseTitle; }
	public int getUnits() { return units; }
	public String getDescription() { return description; }

}
