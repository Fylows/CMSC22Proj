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
    public String getSection() { return section; }
    public String getTimes() { return times; }
    public String getDays() { return days; }
    public String getRoom() { return room; }
    public String getTerm() { return term; }
    
    public ArrayList<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    
    public LocalTime getStartTime() {
        try {
            String start = times.split("-")[0].trim();
            return LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return null;
        }
    }

    public LocalTime getEndTime() {
        try {
            String end = times.split("-")[1].trim();
            return LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return null;
        }
    }

    public String getDay() {
        return days;
    }

    // View State
    public void viewState() {
        System.out.printf("%s | Section: %s | Days: %s | Time: %s | Room: %s%n",
                baseCourse.getCourseCode(), section, days, times, room);
    }
}
