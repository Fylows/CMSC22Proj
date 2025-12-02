package backend;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import backend.CourseManager.Degree;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class RegSystem {
    private StudentManager studentManager;
    private OfferedCourseManager courseManager;

    private final static Map<LocalTime, Integer> timeMap = new LinkedHashMap<>();
   
    private final static Map<String, Integer> dayMap = Map.ofEntries(
        	Map.entry("monday", 0), Map.entry("tuesday", 1),
        	Map.entry("wednesday", 2), Map.entry("thursday", 3),
        	Map.entry("friday", 4)
	);
    
    //Constructor
    public RegSystem() {
        this.studentManager = StudentManager.load();
        this.courseManager = OfferedCourseManager.load();
        OfferedCourseManager.save(courseManager);

        // initialize timeMap
        int row = 0;
        for (int hour = 7; hour <= 18; hour++) {
            timeMap.put(LocalTime.of(hour, 0), row++);
            timeMap.put(LocalTime.of(hour, 30), row++);
        }
        
    }

    //Adds new students to system
    //Saves the updated student list
    public void addStudent(Student s) {
        studentManager.getStudents().add(s);
        StudentManager.save(this.studentManager);
    }

    //Deletes students from every course they enrolled in
    //Deletes students from the entire system
    //Saves the updated list
    public void deleteStudent(String studentEmail) {
        Student s = getStudent(studentEmail);
        if (s != null) {
            for (OfferedCourse oc : courseManager.getOfferedCourses()) {
                oc.getEnrolledStudents().remove(s);
            }
            studentManager.getStudents().remove(s);

            StudentManager.save(this.studentManager);
            // OfferedCourseManager.saveData(courseManager.getAllCourses(), courseManager.getOfferedCourses());
        }
    }

    //Gets student by email address
    public Student getStudent(String studentEmail) {
        return studentManager.getStudentByEmail(studentEmail);
    }

    //Gets a list of all students in the system
    public ArrayList<Student> getAllStudents() {
        return studentManager.getStudents();
    }

//    public void addCourse(Course c) {
//        courseManager.getAllCourses().add(c);
//        OfferedCourseManager.saveData(courseManager.getAllCourses(), courseManager.getOfferedCourses());
//    }
//
//    public void deleteCourse(String courseCode) {
//        Course c = getCourse(courseCode);
//        if (c != null) {
//            courseManager.getOfferedCourses().removeIf(oc -> oc.getCourse().equals(c));
//            courseManager.getAllCourses().remove(c);
//
//            OfferedCourseManager.saveData(courseManager.getAllCourses(), courseManager.getOfferedCourses());
//        }
//    }

//    public Course getCourse(String courseCode) {
//        for (Course c : courseManager.getAllCourses()) {
//            if (c.getCourseCode().equalsIgnoreCase(courseCode)) return c;
//        }
//        return null;
//    }
    
    //Get all currently offered courses in the system
    public static ArrayList<OfferedCourse> getAllCourses() {
        return OfferedCourseManager.getAllCourses();
    }

    //Gets a specific course from the system using course codes
    public OfferedCourse getOfferedCourse(String courseCode, String term) {
        for (OfferedCourse oc : courseManager.getOfferedCourses()) {
            if (oc.getCourse().getCourseCode().equalsIgnoreCase(courseCode) &&
                oc.getTerm().equalsIgnoreCase(term)) {
                return oc;
            }
        }
        return null;
    }

    //Enrolls students into offered courses
    //Checks if students meet the prerequisites before enrolling in the course
    //Saves the updated list
    public boolean enrollStudentInOfferedCourse(String studentEmail, String courseCode, String term) {
        Student s = getStudent(studentEmail);
        OfferedCourse oc = getOfferedCourse(courseCode, term);

        if (s == null || oc == null) return false;

        if (!hasPrerequisites(s, oc.getCourse())) {
            System.out.println("Enrollment failed: prerequisites not met.");
            return false;
        }

        if (hasTimeConflict(s, oc)) {
            System.out.println("Enrollment failed: time schedule conflict.");
            return false;
        }

        if (!oc.getEnrolledStudents().contains(s)) {
            oc.getEnrolledStudents().add(s);

            if (!s.getEnrolledCourses().contains(courseCode)) {
                s.getEnrolledCourses().add(courseCode);
            }

            StudentManager.save(this.studentManager);
            OfferedCourseManager.save(this.courseManager);

            System.out.println("Successfully enrolled in " + courseCode + " (" + term + ")");
            return true;
        }
        return false;
    }

    //Removes student from enrolled courses
    //Saves the updated list
    public boolean dropStudentFromOfferedCourse(String studentEmail, String courseCode, String term) {
        Student s = getStudent(studentEmail);
        OfferedCourse oc = getOfferedCourse(courseCode, term);

        if (s == null || oc == null) return false;

        if (oc.getEnrolledStudents().contains(s)) {
            oc.getEnrolledStudents().remove(s);

            s.getEnrolledCourses().remove(courseCode);

            StudentManager.save(this.studentManager);
            OfferedCourseManager.save(this.courseManager);
            return true;
        }
        return false;
    }

    //Gets list of all students currently enrolled in a specific course
    public ArrayList<Student> getStudentsInOfferedCourse(String courseCode, String term) {
        OfferedCourse oc = getOfferedCourse(courseCode, term);
        if (oc != null) return oc.getEnrolledStudents();
        return new ArrayList<>();
    }

    //Checks if a specific course has prerequisites
    private boolean hasPrerequisites(Student student, Course course) {
        if (course.getPrerequisites() == null || course.getPrerequisites().isEmpty()) return true;

        for (String req : course.getPrerequisites()) {
            if (!student.getCompletedCourses().contains(req)) {
                System.out.println("Missing prerequisite: " + req);
                return false;
            }
        }
        return true;
    }

    //Compares two courses and checks if they have overlapping time frames
    private boolean hasTimeConflict(Student student, OfferedCourse courseToEnroll) {
        for (String code : student.getEnrolledCourses()) {

            OfferedCourse c = getOfferedCourse(code, courseToEnroll.getTerm());
            if (c == null) continue;

            if (c.getDay() != null && courseToEnroll.getDay() != null) {
                String[] cDays = c.getDay().split(",");
                String[] newDays = courseToEnroll.getDay().split(",");
                boolean conflict = false;

                for (String d1 : cDays) {
                    for (String d2 : newDays) {
                        if (d1.trim().equalsIgnoreCase(d2.trim())) {
                            conflict = true;
                            break;
                        }
                    }
                    if (conflict) break;
                }

                if (conflict) {
                    boolean overlap = !(courseToEnroll.getEndTime().isBefore(c.getStartTime()) ||
                                        courseToEnroll.getStartTime().isAfter(c.getEndTime()));
                    if (overlap) {
                        System.out.println("Time conflict with " + c.getCourse().getCourseCode());
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static void fillTime(GridPane timeTable, OfferedCourse courseToEnroll) {
    	int[] timeArray = new int[24];

    	timeArray[timeMap.get(courseToEnroll.getStartTime())] = 1;
    	timeArray[timeMap.get(courseToEnroll.getEndTime())] = 1;
    	int targetCol = dayMap.get(courseToEnroll.getDay().toLowerCase());
    	
		boolean fill = false;
		for (int row = 0; row < 24; row++) {
		     if (timeArray[row] == 1) {fill = !fill; }
		     if (fill) {
		     	StackPane cell = (StackPane) getNodeFromGridPane(timeTable, targetCol, row);
		         if (cell != null) {
		             cell.setStyle("-fx-background-color: pink; -fx-border-color: black;");
		         }
		     }
		     
		 }
    }
    

    // Helper to get a cell from a GridPane
    private static Node getNodeFromGridPane(GridPane grid, int col, int row) {
        for (Node node : grid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (colIndex == null) colIndex = 0;
            if (rowIndex == null) rowIndex = 0;

            if (colIndex == col && rowIndex == row) {
                return node;
            }
        }
        return null;
    }
    

}
