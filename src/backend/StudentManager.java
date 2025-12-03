package backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class StudentManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Student> students; // List of registered students
	private static final Path STORAGE_PATH = Path.of("src/storage/students.txt");

	// Constructor
	public StudentManager(ArrayList<Student> students) {
		this.students = students;
	}

	// Getters
	public ArrayList<Student> getStudents() {
		return students;
	}

    // Persistence Functions (saving/loading)
	// Saves the entire StudentManager object to a file
	public static void save(StudentManager manager) {
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(STORAGE_PATH))) { // Put in the try block so it automatically closes at the end
			out.writeObject(manager); // Writes all the students
		} catch (IOException e) {
			e.printStackTrace(); // Prints a detailed error report to the console
		}
	}

	// Load a StudentManager from a file (returns null if something fails)
	public static StudentManager load() {
		StudentManager fallback = new StudentManager(new ArrayList<Student>());
		
		if (!Files.exists(STORAGE_PATH)) return fallback; // If file does not exist, return a default value
		
		try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(STORAGE_PATH))) { // Put in the try block so it automatically closes at the end
			StudentManager manager = (StudentManager) in.readObject(); // Read the serialized StudentManager object
			return manager; // Return the loaded object
		} catch (IOException | ClassNotFoundException e) {
			return fallback; // Return nothing if file is unreadable or corrupted
		}
	}

	// Update the information for the student (used after a successful sign-up, and student is asked for completed courses)
	public void updateStudent(Student updatedStudent) {
		for (int i = 0; i < students.size(); i++) { // Loop through the students to look for the email
			if (students.get(i).getEmail().equalsIgnoreCase(updatedStudent.getEmail())) { // Find matching email
				students.set(i, updatedStudent); // Update record
				break;
			}
		}
		StudentManager.save(this); // Persist all students
	}

	// Logic for Sign-Up Screen
	public String signUp(String firstName, String middleName, String lastName, String suffix,
						String email, String birthday, String sex, String password, String degree) {

		// Checks if the required fields (those with *) are not empty
		if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || birthday.isBlank()
				|| sex.isBlank() || password.isBlank() || degree.isBlank()) {
			return "Please fill in all required fields.";
		}

		// Checks if email is in a valid up.edu.ph format
		if (!email.matches("^[\\w.-]+@up\\.edu\\.ph$")) { // ^ start while $ end of string; accepts letters and numbers before @up.edu.ph
			return "Invalid email format.";
		}

		// Password must have a minimum length of 6 characters
		if (password.length() < 6) {
			return "Password must be at least 6 characters.";
		}

		// Checks if email already exists
		if (findByEmail(email) != null) {
			return "Email is already registered. Try adding numbers";
		}

		// Creates the new student
		Student newStudent = new Student(firstName, middleName, lastName, suffix, email, birthday, sex, password, degree);
		students.add(newStudent);
		StudentManager.save(this); // Saves the student after signing-up

		return "SUCCESS";
	}

	// Logic for Log-In Screen
	public String login(String email, String password) {
		if (email.isBlank() || password.isBlank()) { // Both email and password fields must be not empty
			return "Please enter both email and password.";
		}

		Student s = findByEmail(email); // Looks if student account is valid
		if (s == null) { // If email is not found, return null
			return "Email not found."; 
		}

		if (!s.getPassword().equals(password)) { // If email exists but password is incorrect
			return "Incorrect password.";
		}

		return "SUCCESS";
	}

    // Finds the student using their UP mail
	private Student findByEmail(String email) {
		for (Student s : students) { // Loops through each student
			if (s.getEmail().equalsIgnoreCase(email)) {
				return s;
			}
		}
		return null; // Return null if not found
	}

	// For proper encapsulation
	public Student getStudentByEmail(String email) {
		return findByEmail(email);
	}
}
