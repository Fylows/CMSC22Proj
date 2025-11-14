package backend;

public class OfferedCourse {
	private Course baseCourse;
	
	private String section, times, days, room;
	
	public OfferedCourse (String courseCode, String section, String times, String days, String room, String degree){
		this.baseCourse = CourseManager.getCourse(courseCode, baseCourse.getType());
		this.section = section;
		this.times = times;
		this.days = days;
		this.room = room;
	}
	
}
