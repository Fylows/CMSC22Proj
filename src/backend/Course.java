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
		if (prereq.isEmpty() && CourseManager.prereqMap.containsKey(courseCode)) {
			prereq.addAll(CourseManager.prereqMap.get(courseCode));
		}
		return prereq;
	}
	
//	// New method: prerequisites depending on the degree
//	public List<String> getPrerequisitesForDegree(String degreeCode) {
//	    List<String> prereqs = CourseManager.prereqMap.getOrDefault(courseCode, new ArrayList<>());
//
//	    // For MSCS or MSIT, assume all BSCS courses are completed
//	    if (degreeCode.equals("MSCS") || degreeCode.equals("MSIT")) {
//	        List<String> filtered = new ArrayList<>();
//	        for (String p : prereqs) {
//	            // Keep only non-BSCS courses
//	            if (!CourseManager.isBSCSCourse(p)) { // <- helper method
//	                filtered.add(p);
//	            }
//	        }
//	        return filtered;
//	    }
//
//	    // Otherwise, return normal prerequisites
//	    return prereqs;
//	}
	
	public String getType() { return this.type; }
	public String getCourseCode() { return courseCode; }
	public String getCourseTitle() { return courseTitle; }
	public int getUnits() { return units; }
	public String getDescription() { return description; }

}
