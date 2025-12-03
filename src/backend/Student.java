package backend;

import java.util.ArrayList;
import java.io.Serializable;

public class Student implements Serializable {
	private static final long serialVersionUID = 1L;
    
	private String firstName, middleName, lastName, suffix; // Student's name
	private String email; // Student's email
	private String birthday; // Student's birthday
	private String sex; // Student's sex assigned at birth
	private String password; // Student's password
	private String degree; // BSCS, MSCS, MIT, PHD
	private ArrayList<Course> completedCourses = new ArrayList<>(); // List of the student's completed courses
	private ArrayList<OfferedCourse> enrolledCourses = new ArrayList<>();

    // Constructor
	public Student(String firstName, String middleName, String lastName, String suffix,
				String email, String birthday, String sex, String password, String degree) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.suffix = suffix;
		this.email = email;
		this.birthday = birthday;
		this.sex = sex;
		this.password = password;
		this.degree = degree;
	}

	public void addCompletedCourse(Course c) {
		if (!completedCourses.contains(c)) {
            completedCourses.add(c);
		}
	}

	// Getters
	public ArrayList<Course> getCompletedCourses() { 
		return completedCourses; 
	}

	public ArrayList<OfferedCourse> getEnrolledCourses() { 
		return enrolledCourses; 
	}
	
	public String getName() { return firstName + " " + lastName; }
	public String getFirstName() { return firstName; }
	public String getMiddleName() { return middleName; }
	public String getLastName() { return lastName; }
	public String getSuffix() { return suffix; }
	public String getSex() { return sex; }
	public String getDegree() { return degree; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }
}
