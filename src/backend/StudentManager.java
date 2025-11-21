package backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class StudentManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Student> students;

    // Degrees for validation
    private static final String[] VALID_DEGREES = {"BSCS", "MSCS", "MSIT", "PHD"};

    // Constructor
    public StudentManager(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    // ---------------- PERSISTENCE ----------------
    public void save(Path path) {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StudentManager load(Path path) {
        if (!Files.exists(path)) return null;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            return (StudentManager) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    // Helper: save only students list
    public static void saveStudents(ArrayList<Student> students) {
        StudentManager handler = new StudentManager(students);
        handler.save(Path.of("students.txt"));
    }

    // Helper: load only students list
    public static ArrayList<Student> loadStudents() {
        StudentManager handler = load(Path.of("students.txt"));
        if (handler == null) return new ArrayList<>();
        return handler.getStudents();
    }
    
 // Update an existing student and save to file
    public void updateStudent(Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getEmail().equalsIgnoreCase(updatedStudent.getEmail())) {
                students.set(i, updatedStudent); // replace old student
                break;
            }
        }
        saveStudents(students); // persist all students
    }


    // ---------------- SIGN-UP ----------------
    public String signUp(String firstName, String middleName, String lastName, String suffix,
                         String email, String birthday, String sex, String password, String degree) {

        // 1. Check required fields
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || birthday.isBlank()
                || sex.isBlank() || password.isBlank() || degree.isBlank()) {
            return "Please fill in all required fields.";
        }

        // 2. Email format check
        if (!email.matches("^[\\w.-]+@up\\.edu\\.ph$")) {
        	return "Invalid email format.";
        }
        
        // 3. Password length
        if (password.length() < 6) return "Password must be at least 6 characters.";

        // 4. Degree validation
        boolean validDegree = false;
        for (String d : VALID_DEGREES) {
            if (degree.equalsIgnoreCase(d)) {
                validDegree = true;
                degree = d; // normalize
                break;
            }
        }
        if (!validDegree) return "Invalid degree selected.";

        // 5. Check if email already exists
        if (findByEmail(email) != null) return "Email is already registered.";

        // 6. Add new student
        Student newStudent = new Student(firstName, middleName, lastName, suffix, email, birthday, sex, password, degree);
        students.add(newStudent);
        saveStudents(students);

        return "SUCCESS";
    }

    // ---------------- LOGIN ----------------
    public String login(String email, String password) {
        if (email.isBlank() || password.isBlank()) return "Please enter both email and password.";

        Student s = findByEmail(email);
        if (s == null) return "Email not found.";
        if (!s.getPassword().equals(password)) return "Incorrect password.";

        return "SUCCESS";
    }

    // ---------------- HELPER ----------------
    private Student findByEmail(String email) {
        for (Student s : students) {
            if (s.getEmail().equalsIgnoreCase(email)) return s;
        }
        return null;
    }

    public Student getStudentByEmail(String email) {
        return findByEmail(email);
    }
}
