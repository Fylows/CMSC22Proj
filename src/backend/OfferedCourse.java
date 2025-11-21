package backend;

import java.io.Serializable;

public class OfferedCourse implements Serializable {
    private static final long serialVersionUID = 1L;
    
	private Course baseCourse; // The course or subject to be offered
	private String section, times, days, room; // Information about the offered course

	// Constructor
	public OfferedCourse(Course baseCourse, String section, String times, String days, String room) {
		this.baseCourse = baseCourse;
		this.section = section;
		this.times = times;
		this.days = days;
		this.room = room;
	}
	
	// Getters
	public Course getBaseCourse() { return baseCourse; }
	public String getSection() { return section; }
	public String getTimes() { return times; }
	public String getDays() { return days; }
	public String getRoom() { return room; }

	// ViewState
	public void viewState() {
		System.out.printf("%s | Section: %s | Days: %s | Time: %s | Room: %s%n", baseCourse.getCourseCode(), section, days, times, room);
	}
}
