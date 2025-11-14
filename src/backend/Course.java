package backend;

public class Course {
	/**
	 * An Object to store the 
	 */
	private String courseCode, courseTitle, type;
	private int units;
	
	/**
	 * 
	 */
	public Course (String code, String title, int units){
		this.courseCode = code;
		this.courseTitle = title;
		this.units = units;
		this.type = CourseManager.getCourseDegree(code);
	}
	
	/**
	 * 
	 */
	public void viewState() {
		System.out.printf("Course code: %s, Course title: %s, units: %d\n", this.courseCode, this.courseTitle, this.units);
	}
	
	/**
	 * 
	 */
	public String getType() { return this.type; }
}
