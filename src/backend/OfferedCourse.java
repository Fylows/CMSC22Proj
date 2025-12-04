package backend;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OfferedCourse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Course baseCourse;
    private String section, times, days, room;
    private String term;
    private ArrayList<Student> enrolledStudents = new ArrayList<>();
    private OfferedCourse parentLecture = null; // instance field
    private static OfferedCourse lastLec = null; // static for global access

    public OfferedCourse(Course baseCourse, String section, String times, String days, String room, String term) {
        this.baseCourse = baseCourse;
        this.section = section;
        this.times = times;
        this.days = days;
        this.room = room;
        this.term = term;
        this.enrolledStudents = new ArrayList<>();
    }

    public Course getCourse() { return baseCourse; }
    public String getCourseCode() {return baseCourse != null ? baseCourse.getCourseCode() : null; }
    public String getSection() { return section; }
    public String getTimes() { return times; }
    public String getDays() { return days; }
    public String getRoom() { return room; }
    public String getTerm() { return term; }
    public String getDay() { return days; }
    public OfferedCourse getLec() { return parentLecture; }
    public static OfferedCourse getLastLec() { return lastLec; }
    public ArrayList<Student> getEnrolledStudents() { return enrolledStudents; }


	
    public LocalTime getStartTime() {
        try {
            String start = times.split("-")[0].trim();
            return parseFlexibleTime(start, false);
        } catch (Exception e) {
            return null;
        }
    }

    public LocalTime getEndTime() {
        try {
            String end = times.split("-")[1].trim();
            return parseFlexibleTime(end, true);
        } catch (Exception e) {
            return null;
        }
    }

	public void setLastLec() { lastLec = this;	}
    public void setLec(OfferedCourse lec) { this.parentLecture = lec; }

    
    public boolean isStudentEnrolled(Student student) { 
    	return enrolledStudents.contains(student);
    }

    
    private LocalTime parseFlexibleTime(String t, boolean isEnd) {
        t = t.trim();

        // If AM/PM is already included → parse immediately
        if (t.toLowerCase().contains("am") || t.toLowerCase().contains("pm")) {
            return LocalTime.parse(t.toUpperCase(), DateTimeFormatter.ofPattern("h:mm a"));
        }

        // Otherwise infer AM/PM based on rules
        String[] parts = t.split(":");
        int hour = Integer.parseInt(parts[0]);

        String suffix;

        if (hour >= 7 && hour <= 11) {
            suffix = " AM";
        } else if (hour == 12) {
            suffix = " PM";  // 12 is ALWAYS PM in your schedule
        } else {
            suffix = " PM";  // hours 1–6 must be PM
        }

        // Special case: 7 PM is ONLY allowed as end time
        if (hour == 7 && isEnd) {
            suffix = " PM";
        }

        return LocalTime.parse(t + suffix, DateTimeFormatter.ofPattern("h:mm a"));
    }

    
    public boolean conflictsWith(OfferedCourse other) {
    	if (other == null) return false;
    	
    	if (!this.term.equalsIgnoreCase(other.term))
    		return false;

    	if (!this.days.equalsIgnoreCase(other.days))
    		return false;
    	
    	LocalTime s1 = this.getStartTime();
    	LocalTime e1 = this.getEndTime();
    	LocalTime s2 = other.getStartTime();
    	LocalTime e2 = other.getEndTime();
    	
    	if (s1 == null || e1 == null || s2 == null || e2 == null) return false;
    	return s1.isBefore(e2) && s2.isBefore(e1);
    }

    
    // View State
    public void viewState() {
        System.out.printf("%s | Section: %s | Days: %s | Time: %s | Room: %s%n",
                baseCourse.getCourseCode(), section, days, times, room);
    }

	
}
