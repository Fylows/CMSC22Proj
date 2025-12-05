package backend;

import javafx.scene.control.Label;
import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RegSystem {
    private static StudentManager studentManager;
    private static OfferedCourseManager courseManager;

    private final static Map<LocalTime, Integer> timeMap = new LinkedHashMap<>();
   
    private final static Map<String, List<Integer>> dayMap = Map.ofEntries(
        	Map.entry("mon", List.of(1)), Map.entry("tues", List.of(2)),
        	Map.entry("wed", List.of(3)), Map.entry("thurs", List.of(4)),
        	Map.entry("fri", List.of(5)), Map.entry("mw", List.of(1,3)),
        	Map.entry("tth", List.of(2,4)), Map.entry("wf", List.of(3,5))
	);
    
    //Constructor
    public RegSystem() {
    	RegSystem.studentManager = StudentManager.load();
    	RegSystem.courseManager = OfferedCourseManager.load();
        OfferedCourseManager.save(courseManager);
        
        // initialize timeMap
        int row = 1;
        for (int hour = 7; hour <= 19; hour++) {
            timeMap.put(LocalTime.of(hour, 0), row++);
            timeMap.put(LocalTime.of(hour, 30), row++);
        }
        
    }

    //Adds new students to system
    //Saves the updated student list
    public void addStudent(Student s) {
        studentManager.getStudents().add(s);
        studentManager.save();
    }

    // Deletes students from every course they enrolled in
    // Deletes students from the entire system
    // Saves the updated list
    public void deleteStudent(String studentEmail) {
        Student s = getStudent(studentEmail);
        if (s != null) {
            for (OfferedCourse oc : OfferedCourseManager.getOfferedCourses()) {
                oc.getEnrolledStudents().remove(s);
            }
            studentManager.getStudents().remove(s);
            studentManager.save();
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
    /** enrollStudentInOfferedCourse
     * 
     * enrolls a student in their chosen course by going through multiple checks, namely:
     * is student enrolled, is the course in their degree, do they have the prerequisites, is there a time conflict
     * 
     * @param student, the student to enroll in the course
     * @param course, the course to be enrolled in
     * @param off, observable list of student's courses
     * @return integers corresponding to the type of error
     * -1 = null error
     * 0 = successful 
     * 1 = already have the course
     * 2 = not their degree
     * 3 = prerequisites not met
     * 4 = has a time conflict
     * 5 = course has already been completed
     */
    public static int enrollStudentInOfferedCourse(Student student, OfferedCourse course, ObservableList<OfferedCourse> off) { 
        if (student == null || course == null) return -1; 

        boolean alreadyCompleted = student.getCompletedCourses().stream()
                .anyMatch(c -> c.getCourseCode().equalsIgnoreCase(course.getCourseCode()));
        if (alreadyCompleted) {
            return 5;
        }

        boolean alreadyEnrolled = student.getEnrolledCourses().stream()
                .anyMatch(c -> c.getCourseCode().equals(course.getCourseCode())
                           && !c.getSection().equals(course.getSection()));

        if (alreadyEnrolled) {
            return 1;
        }

        else if (!student.getDegree().equalsIgnoreCase(course.getCourse().getType())) {
            return 2; 
        }

        else if (!hasPrerequisites(student, course)) { 
            return 3; 
        } 

        else if (hasTimeConflict(student, course)) { 
            return 4; 
        } 

        off.add(course);

        if (!isLecture(course) && !student.getEnrolledCourses().contains(course.getLec())) {
            off.add(course.getLec());
        }

        studentManager.updateStudent(student);
        studentManager.save(); 
        OfferedCourseManager.save(courseManager); 
        return 0;
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
    		studentManager.updateStudent(student);
    		studentManager.save(); 
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

        boolean meetsPrereqs = course.getCourse().getPrerequisites().stream()
        	    .allMatch(req ->
        	        student.getCompletedCourses().stream()
        	                 .anyMatch(c -> c.getCourseCode().toLowerCase().contains(req.toLowerCase()))
        	    );
        return meetsPrereqs; 
    }
    
    // Compares two courses and checks if they have overlapping time frames
    private static boolean hasTimeConflict(Student student, OfferedCourse courseToEnroll) {
    	for (OfferedCourse enrolledCourse : student.getEnrolledCourses()) {
    	if (!enrolledCourse.getTerm().equalsIgnoreCase(courseToEnroll.getTerm())) continue;

    	    if (enrolledCourse.getDay() == null || courseToEnroll.getDay() == null) continue;

    	    String[] days1 = enrolledCourse.getDay().split(",");
    	    String[] days2 = courseToEnroll.getDay().split(",");

    	    boolean sharesDay = false;
    	    for (String d1 : days1) {
    	        for (String d2 : days2) {
    	            String a = d1.trim().toLowerCase();
    	            String b = d2.trim().toLowerCase();

    	            if (a.equals(b)) {
    	                sharesDay = true;
    	                break;
    	            }

    	            if ((a.length() > 1 && a.contains(b)) || (b.length() > 1 && b.contains(a))) {
    	                sharesDay = true;
    	                break;
    	            }
    	        }
    	        if (sharesDay) break;
    	    }

    	    if (!sharesDay) continue;

    	    LocalTime sTime1 = courseToEnroll.getStartTime();
    	    LocalTime eTime1 = courseToEnroll.getEndTime();
    	    LocalTime sTime2  = enrolledCourse.getStartTime();
    	    LocalTime eTime2  = enrolledCourse.getEndTime();

    	    if (sTime1 == null || eTime1 == null || sTime2 == null || eTime2 == null) continue;

    	    if (sTime1.isBefore(eTime2) && sTime2.isBefore(eTime1)) {
    	        return true;
    	    }
    	}
    	return false;

    	}
    
    @SuppressWarnings("exports")
	public static void fillTime(GridPane timeTable, OfferedCourse courseToEnroll) {
        int startRow = timeMap.get(courseToEnroll.getStartTime());
        int endRow = timeMap.get(courseToEnroll.getEndTime());

        List<Integer> targetCols = dayMap.get(courseToEnroll.getDay().toLowerCase());
        if (targetCols == null) return;

        for (int col : targetCols) {
            int middleRow = startRow + (endRow - startRow) / 2;

            for (int row = startRow; row < endRow; row++) {
                Node node = getNodeFromGridPane(timeTable, col, row);
                if (node instanceof StackPane stack) {
                    Rectangle rect = (Rectangle) stack.getChildren().get(0);
                    rect.setFill(Color.PINK);
                    rect.setStroke(Color.PINK);

                    // Place course title at middleRow - 1
                    if (row == middleRow - 1) {
                        boolean hasLabel = stack.getChildren().stream().anyMatch(n -> n instanceof Label && ((Label)n).getText().equals(courseToEnroll.getCourse().getCourseCode()));
                        if (!hasLabel) {
                            Label courseLabel = new Label(courseToEnroll.getCourse().getCourseCode());
                            courseLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
                            stack.getChildren().add(courseLabel);
                            StackPane.setAlignment(courseLabel, Pos.BOTTOM_CENTER);
                        }
                    }

                    // Place section + room at middleRow + 1
                    if (row == middleRow) {
                        boolean hasLabel = stack.getChildren().stream().anyMatch(n -> n instanceof Label && ((Label)n).getText().equals(courseToEnroll.getSection() + " - " + courseToEnroll.getRoom()));
                        if (!hasLabel) {
                        	VBox detailsLabel = new VBox();
                        	Label sectionDetails = new Label(courseToEnroll.getSection());
                        	Label roomDetails = new Label(courseToEnroll.getRoom());

                        	detailsLabel.getChildren().addAll(roomDetails, sectionDetails);
                        	detailsLabel.setStyle("-fx-font-size: 11;");
                        	detailsLabel.setAlignment(Pos.CENTER); // center children inside VBox
                        	detailsLabel.setSpacing(2);

                        	stack.getChildren().add(detailsLabel);
                        	StackPane.setAlignment(detailsLabel, Pos.CENTER); // center the VBox in the cell
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("exports")
	public static void resetTime(GridPane timeTable, OfferedCourse courseToEnroll) {
        int startRow = timeMap.get(courseToEnroll.getStartTime());
        int endRow = timeMap.get(courseToEnroll.getEndTime());
        List<Integer> targetCols = dayMap.get(courseToEnroll.getDay().toLowerCase());
        if (targetCols == null) return;

        for (int col : targetCols) {
            for (int row = startRow; row < endRow; row++) {
                Node node = getNodeFromGridPane(timeTable, col, row);
                if (node instanceof StackPane stack) {
                    // Reset the rectangle
                    Rectangle rect = (Rectangle) stack.getChildren().get(0);
                    rect.setFill(Color.WHITE);
                    rect.setStroke(Color.BLACK);

                    // Remove all other nodes (Labels, VBoxes, etc.)
                    stack.getChildren().removeIf(n -> n != rect);
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
    
    public static StudentManager getStudentManager() { return studentManager; }
    public static OfferedCourseManager getCourseManager() { return courseManager; }

}
