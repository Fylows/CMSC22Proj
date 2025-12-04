package backend;

import javafx.scene.control.Label;
import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
//import backend.CourseManager.Degree;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RegSystem {
    private static StudentManager studentManager;
    private static OfferedCourseManager courseManager;

    private final static Map<LocalTime, Integer> timeMap = new LinkedHashMap<>();
   
    private final static Map<String, List<Integer>> dayMap = Map.ofEntries(
        	Map.entry("mon", List.of(0)), Map.entry("tues", List.of(1)),
        	Map.entry("wed", List.of(2)), Map.entry("thurs", List.of(3)),
        	Map.entry("fri", List.of(4)), Map.entry("mw", List.of(0,2)),
        	Map.entry("tth", List.of(1,3)), Map.entry("wf", List.of(2,4))
	);
    
    //Constructor
    public RegSystem() {
    	RegSystem.studentManager = StudentManager.load();
    	RegSystem.courseManager = OfferedCourseManager.load();
        OfferedCourseManager.save(courseManager);

        // initialize timeMap
        int row = 0;
        for (int hour = 7; hour <= 19; hour++) {
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

    // Deletes students from every course they enrolled in
    // Deletes students from the entire system
    // Saves the updated list
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

    // Gets student by email address
    public static Student getStudent(String studentEmail) {
        return studentManager.getStudentByEmail(studentEmail);
    }

    // Gets a list of all students in the system
    public static ArrayList<Student> getAllStudents() {
        return studentManager.getStudents();
    }

    
    // Get all currently offered courses in the system
    public static ArrayList<OfferedCourse> getAllCourses() {
        return OfferedCourseManager.getOfferedCourses();
    }

    // Gets a specific course from the system using course codes
    public static OfferedCourse getOfferedCourse(String courseCode, String section, String term) {
        ArrayList<OfferedCourse> courses = OfferedCourseManager.getOfferedCourses();
        for (OfferedCourse oc : courses) {
            if (oc.getCourse().getCourseCode().equalsIgnoreCase(courseCode) &&
                oc.getSection().equalsIgnoreCase(section) &&
                oc.getTerm().equalsIgnoreCase(term)) {
                return oc;
            }
        }
        return null;
    }
    // Enrolls students into offered courses
    // Checks if students meet the prerequisites before enrolling in the course
    // Saves the updated list
    public static boolean enrollStudentInOfferedCourse(Student student, OfferedCourse course, ObservableList<OfferedCourse> off) { 
    	if (student == null || course == null) return false; 
    	for (OfferedCourse oc : courseManager.getOfferedCourses()) { 
    		if (oc.getCourseCode().equalsIgnoreCase(course.getCourseCode()) && 
    				oc.getTerm().equalsIgnoreCase(course.getTerm()) && 
    				oc.getEnrolledStudents().contains(student)) { 
    			System.out.println("Enrollment failed: already enrolled in another section of " + course.getCourseCode()); 
    			return false; 
    			} 
    		} 

    	boolean alreadyEnrolled = student.getEnrolledCourses().stream()
    	        .anyMatch(c -> c.getCourseCode().equals(course.getCourseCode())
    	                   && !c.getSection().equals(course.getSection()));
    	
    	if (!student.getDegree().equalsIgnoreCase(course.getCourse().getType())) {
    		System.out.println("Enrollment failed: not your degree"); 
    		return false; 
    	}
    	
    	if (!hasPrerequisites(student, course)) { 
    		System.out.println("Enrollment failed: prerequisites not met."); 
    		return false; 
    		} 

    	if (hasTimeConflict(student, course)) { 
    		System.out.println("Enrollment failed: schedule conflict."); 
    		return false; 
    		} 
    	
    	
    	if (!course.getEnrolledStudents().contains(student)) { 
    		course.getEnrolledStudents().add(student); 
    		
			if (!alreadyEnrolled) {
				// student.getEnrolledCourses().add(course);
				off.add(course);
			    // auto-enroll lecture for labs
				if (!isLecture(course) && !student.getEnrolledCourses().contains(course.getLec())) {
			        //student.getEnrolledCourses().add(course.getLec());
			        off.add(course.getLec());
				}
			    
	    		return true;
			}
		} 
    	return false; 
    }
    
    private static boolean isLecture(OfferedCourse course) {
        return course.getLec() == null;
    }
    // Removes student from enrolled courses
    // Saves the updated list
    public static boolean dropStudentFromOfferedCourse(Student student, OfferedCourse course) { 
    	if (student == null || course == null) return false;
    	
    	if (course.getEnrolledStudents().contains(student)) { 
    		course.getEnrolledStudents().remove(student); 
    		student.getEnrolledCourses().remove(course); 
    		if (course.getLec() != null) {
        		course.getLec().getEnrolledStudents().remove(student); 
        		student.getEnrolledCourses().remove(course.getLec()); 
        	}
    		
    		StudentManager.save(studentManager); 
    		OfferedCourseManager.save(courseManager); 
    		return true; 
    		} 
    	return false; 
    }
    
    // Gets list of all students currently enrolled in a specific course
    public ArrayList<Student> getStudentsInOfferedCourse(OfferedCourse course) { 
    	if (course != null) { 
    		return course.getEnrolledStudents(); 
    		} 
    	return new ArrayList<Student>(); 
    	}

    // Checks if a specific course has prerequisites
    private static boolean hasPrerequisites(Student student, OfferedCourse course) { 
        if (course.getCourse() == null || course.getCourse().getPrerequisites() == null || course.getCourse().getPrerequisites().isEmpty())
            return true; 

        for (String req : course.getCourse().getPrerequisites()) { 
            if (!student.getCompletedCourses().contains(req)) { 
                System.out.println("Missing prerequisite: " + req); 
                return false; 
            } 
        } 
        return true; 
    }
    
    

    // Compares two courses and checks if they have overlapping time frames
    private static boolean hasTimeConflict(Student student, OfferedCourse courseToEnroll) {
        for (OfferedCourse enrolledCourse : student.getEnrolledCourses()) {
            if (!enrolledCourse.getTerm().equalsIgnoreCase(courseToEnroll.getTerm())) continue;

            if (enrolledCourse.getDay() != null && courseToEnroll.getDay() != null) {
                String[] days1 = enrolledCourse.getDay().split(",");
                String[] days2 = courseToEnroll.getDay().split(",");

                boolean sharesDay = false;
                for (String d1 : days1) {
                    for (String d2 : days2) {
                        if (d1.trim().equalsIgnoreCase(d2.trim())) {
                            sharesDay = true;
                            break;
                        }
                    }
                    if (sharesDay) break;
                }

                if (sharesDay) {
                    boolean overlap =
                        !(courseToEnroll.getEndTime().isBefore(enrolledCourse.getStartTime()) ||
                          courseToEnroll.getStartTime().isAfter(enrolledCourse.getEndTime()));

                    if (overlap) {
                        System.out.println("Time conflict with " +
                                           enrolledCourse.getCourseCode() +
                                           " (" + enrolledCourse.getSection() + ")");
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static void fillTime(GridPane timeTable, OfferedCourse courseToEnroll) {
        int startRow = timeMap.get(courseToEnroll.getStartTime());
        int endRow = timeMap.get(courseToEnroll.getEndTime());

        List<Integer> targetCols = dayMap.get(courseToEnroll.getDay().toLowerCase());
        if (targetCols == null) return;

        for (int col : targetCols) {
            for (int row = startRow; row < endRow; row++) {
                Node node = getNodeFromGridPane(timeTable, col, row);
                if (node instanceof StackPane stack) {  // safe cast
                	Rectangle rect = (Rectangle) stack.getChildren().get(0);
                    rect.setFill(Color.PINK);
                    rect.setStroke(Color.PINK);
                    

                    
                    boolean hasLabel = stack.getChildren().stream().anyMatch(n -> n instanceof Label);
                    if (!hasLabel) {
                        Label code = new Label(courseToEnroll.getCourse().getCourseCode());
                        code.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
                        stack.getChildren().add(code);
                    }
                }
            }
        }
    }

    public static void resetTime(GridPane timeTable, OfferedCourse courseToEnroll) {
        int startRow = timeMap.get(courseToEnroll.getStartTime());
        int endRow = timeMap.get(courseToEnroll.getEndTime());
        List<Integer> targetCols = dayMap.get(courseToEnroll.getDay().toLowerCase());

        for (int col : targetCols) {
            for (int row = startRow; row < endRow; row++) {
                Node node = getNodeFromGridPane(timeTable, col, row);
                if (node instanceof StackPane stack) {
                    Rectangle rect = (Rectangle) stack.getChildren().get(0);
                    rect.setFill(Color.WHITE);
                    rect.setStroke(Color.BLACK);

                    // Remove any label inside
                    stack.getChildren().removeIf(n -> n instanceof Label);
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
