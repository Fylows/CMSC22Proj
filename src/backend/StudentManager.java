package backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class StudentManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Student> students;

	// Constructor
	public StudentManager(ArrayList<Student> students) {
		this.students = students;
	}

	public ArrayList<Student> getStudents() {
		return students;
	}

	// Save this manager to file
	public void save(Path path) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			return;
		}
	}

	// Load manager from file
	public static StudentManager load(Path path) {
		try {
			ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path));
			StudentManager handler = (StudentManager) in.readObject();
			in.close();
			return handler;
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
	}

	// Helper: saves ONLY the student list
	public static void saveStudents(ArrayList<Student> students) {
		StudentManager handler = new StudentManager(students);
		handler.save(Path.of("students.txt"));
	}

	// Helper: loads ONLY the student list
	public static ArrayList<Student> loadStudents() {
		StudentManager handler = load(Path.of("students.txt"));
		if (handler == null) return new ArrayList<>();
		return handler.getStudents();
	}
}
