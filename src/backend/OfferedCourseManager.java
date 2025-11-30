package backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class OfferedCourseManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<OfferedCourse> offeredCourses; // Courses offered
	private static final Path STORAGE_PATH = Path.of("src/storage/offered_courses.txt");

	// Constructor
	public OfferedCourseManager(ArrayList<OfferedCourse> offeredCourses) {
		this.offeredCourses = offeredCourses;
	}

	public ArrayList<OfferedCourse> getOfferedCourses() {
		return offeredCourses;
	}

	// Persistence Functions (saving/loading)
	// Saves the entire OfferedCourseManager object to a file
	public void save(Path path) {
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) { // Put in the try block so it automatically closes at the end
			out.writeObject(this); // Writes all the courses
		} catch (IOException e) {
			e.printStackTrace(); // Prints a detailed error report to the console
		}
	}

	public static OfferedCourseManager load(Path path) {
		if (!Files.exists(path)) return new OfferedCourseManager(OfferedCourseManager.loadOfferedCoursesFromCSV()); // File doesn't exist fall back to CSV loading

		try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) { // Put in the try block so it automatically closes at the end
			OfferedCourseManager manager = (OfferedCourseManager) in.readObject(); // Read the serialized OfferedCourseManager object
			return manager; // Return the loaded object
		} catch (IOException | ClassNotFoundException e) {
			return null; // Return nothing if file is unreadable or corrupted
		}
	}

	// Helper method that saves only the course list to the file "offered_courses.txt"
	public static void saveData(ArrayList<OfferedCourse> offered) {
		OfferedCourseManager handler = new OfferedCourseManager(offered);
		handler.save(STORAGE_PATH); // Save to file named offered_courses.txt
	}

	// Helper method that loads saved courses from file
	public static OfferedCourseManager loadData() {
		OfferedCourseManager handler = load(STORAGE_PATH);
		if (handler == null) {
			return new OfferedCourseManager(new ArrayList<>()); // Return empty list if loading fails
		}
		return handler; // Return list of courses if successful
	}

	// Loading the CSV file of the offered course for the first semester
	public static ArrayList<OfferedCourse> loadOfferedCoursesFromCSV() {
		ArrayList<OfferedCourse> list = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(OfferedCourseManager.class.getResourceAsStream("/dataset/course_offerings.csv")))) {

			String line;
			while ((line = br.readLine()) != null) {
				if (line.isBlank() || line.toLowerCase().contains("code")) continue; // Skip the header of the CSV file

				String[] parts = line.split(","); // Split the lines by commas
				if (parts.length < 7) continue; // Must contain code, title, units, section, time, days, room, if one is missing, skip

				String code = parts[0].trim(); // Trims the course code
				String section = parts[3].trim(); // Trims the section (e.g., "U-1L")
				String times = parts[4].trim(); // Trims the time range
				String days = parts[5].trim(); // Trims the days
				String room = parts[6].trim(); // Trims the room

				Course baseCourse = CourseManager.getCourse(code, CourseManager.courseDegreeMap.get(code).name());
				if (baseCourse == null) {
					System.out.printf("Couldn't find %s skipping...\n", code);
					continue;
				}
				OfferedCourse oc = new OfferedCourse(baseCourse, section, times, days, room); // Build OfferedCourse using course info + schedule info
				list.add(oc); // Add to offered list
			}
		} catch (Exception e) {
			return null;
		}
		return list; // Return all offered courses
	}
}
