package backend;

import java.util.ArrayList;

public class RegSystem {
    private StudentManager studentManager;
    private OfferedCourseManager courseManager;

    public RegSystem() {
        this.studentManager = new StudentManager(StudentManager.loadStudents());
        this.courseManager = OfferedCourseManager.loadData();
    }

    public void addStudent(Student s) {
        studentManager.getStudents().add(s);
        StudentManager.saveStudents(studentManager.getStudents());
    }

    public void deleteStudent(String studentEmail) {
        Student s = getStudent(studentEmail);
        if (s != null) {
            for (OfferedCourse oc : courseManager.getOfferedCourses()) {
                oc.getEnrolledStudents().remove(s);
            }
            studentManager.getStudents().remove(s);

            StudentManager.saveStudents(studentManager.getStudents());
            OfferedCourseManager.saveData(courseManager.getAllCourses(), courseManager.getOfferedCourses());
        }
    }

    public Student getStudent(String studentEmail) {
        return studentManager.getStudentByEmail(studentEmail);
    }

    public ArrayList<Student> getAllStudents() {
        return studentManager.getStudents();
    }

    public void addCourse(Course c) {
        courseManager.getAllCourses().add(c);
        OfferedCourseManager.saveData(courseManager.getAllCourses(), courseManager.getOfferedCourses());
    }

    public void deleteCourse(String courseCode) {
        Course c = getCourse(courseCode);
        if (c != null) {
            courseManager.getOfferedCourses().removeIf(oc -> oc.getCourse().equals(c));
            courseManager.getAllCourses().remove(c);

            OfferedCourseManager.saveData(courseManager.getAllCourses(), courseManager.getOfferedCourses());
        }
    }

    public Course getCourse(String courseCode) {
        for (Course c : courseManager.getAllCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(courseCode)) return c;
        }
        return null;
    }

    public ArrayList<Course> getAllCourses() {
        return courseManager.getAllCourses();
    }

    public ArrayList<OfferedCourse> getAllOfferedCourses() {
        return courseManager.getOfferedCourses();
    }

    public OfferedCourse getOfferedCourse(String courseCode, String term) {
        for (OfferedCourse oc : courseManager.getOfferedCourses()) {
            if (oc.getCourse().getCourseCode().equalsIgnoreCase(courseCode) &&
                oc.getTerm().equalsIgnoreCase(term)) {
                return oc;
            }
        }
        return null;
    }

    public boolean enrollStudentInOfferedCourse(String studentEmail, String courseCode, String term) {
        Student s = getStudent(studentEmail);
        OfferedCourse oc = getOfferedCourse(courseCode, term);

        if (s == null || oc == null) return false;

        // PREREQ CHECK
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

            StudentManager.saveStudents(studentManager.getStudents());
            OfferedCourseManager.saveData(courseManager.getAllCourses(), courseManager.getOfferedCourses());

            System.out.println("Successfully enrolled in " + courseCode + " (" + term + ")");
            return true;
        }
        return false;
    }

    public boolean dropStudentFromOfferedCourse(String studentEmail, String courseCode, String term) {
        Student s = getStudent(studentEmail);
        OfferedCourse oc = getOfferedCourse(courseCode, term);

        if (s == null || oc == null) return false;

        if (oc.getEnrolledStudents().contains(s)) {
            oc.getEnrolledStudents().remove(s);

            s.getEnrolledCourses().remove(courseCode);

            StudentManager.saveStudents(studentManager.getStudents());
            OfferedCourseManager.saveData(courseManager.getAllCourses(), courseManager.getOfferedCourses());
            return true;
        }
        return false;
    }

    public ArrayList<Student> getStudentsInOfferedCourse(String courseCode, String term) {
        OfferedCourse oc = getOfferedCourse(courseCode, term);
        if (oc != null) return oc.getEnrolledStudents();
        return new ArrayList<>();
    }

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

    private void saveStudentData() {
        StudentManager.saveStudents(studentManager.getStudents());
        System.out.println("Student data saved.");
    }
}
