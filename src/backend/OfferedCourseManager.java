package backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OfferedCourseManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<Course> allCourses; // All courses from the 4 degree files
	private ArrayList<OfferedCourse> offeredCourses; // Courses offered for the first semester
	private static final Path STORAGE_PATH = Path.of("offered_courses.txt");

	// Constructor
	public OfferedCourseManager(ArrayList<Course> allCourses, ArrayList<OfferedCourse> offeredCourses) {
		this.allCourses = allCourses;
		this.offeredCourses = offeredCourses;
	}

	// Getters
	public ArrayList<Course> getAllCourses() {
		return allCourses;
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
		if (!Files.exists(path)) return null; // File doesn't exist

		try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) { // Put in the try block so it automatically closes at the end
			OfferedCourseManager manager = (OfferedCourseManager) in.readObject(); // Read the serialized OfferedCourseManager object
			return manager; // Return the loaded object
		} catch (IOException | ClassNotFoundException e) {
			return null; // Return nothing if file is unreadable or corrupted
		}
	}

	// Helper method that saves only the course list to the file "offered_courses.txt"
	public static void saveData(ArrayList<Course> courses, ArrayList<OfferedCourse> offered) {
		OfferedCourseManager handler = new OfferedCourseManager(courses, offered);
		handler.save(STORAGE_PATH); // Save to file named offered_courses.txt
	}

	// Helper method that loads saved courses from file
	public static OfferedCourseManager loadData() {
		OfferedCourseManager handler = load(STORAGE_PATH);
		if (handler == null) {
			return new OfferedCourseManager(new ArrayList<>(), new ArrayList<>()); // Return empty list if loading fails
		}
		return handler; // Return list of courses if successful
	}

	// CSV Loading
	// Loading from the degree-related CSV file (ics_cmsc_courses, ics_mit_courses, ics_mscs_courses, ics_phd_courses)
	public static Map<String, Course> loadCourses() {
		Map<String, Course> courseMap = new LinkedHashMap<>(); // LinkedHashMap keeps original order of insertion so checking of courses will be easier
		String[] files = {"/dataset/ics_cmsc_courses.csv", "/dataset/ics_mit_courses.csv", "/dataset/ics_mscs_courses.csv", "/dataset/ics_phd_courses.csv"};

		for (String resourcePath : files) { // Loop through each CSV file
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(OfferedCourseManager.class.getResourceAsStream(resourcePath)))) {

				String line;
				while ((line = br.readLine()) != null) {
					if (line.isBlank() || line.toLowerCase().contains("code")) continue; // Skip the header of the CSV file

					String[] parts = line.split(","); // Split the lines by commas
					if (parts.length < 4) continue; // Must contain code, title, units, description, if one is missing, skip

					String code = parts[0].trim(); // Trims the course code
					
					// Trims the course name (some names have commas)
					StringBuilder titleBuilder = new StringBuilder();
					for (int i = 1; i < parts.length - 2; i++) {
						if (i > 1) titleBuilder.append(", "); // Restores the commas in the text
						titleBuilder.append(parts[i].trim());
				    }
					String title = titleBuilder.toString(); // Assigns the fixed title

					// For trimming the units (some units have range)
					String unitStr = parts[parts.length - 2].trim();
					int units = 1; // Default units if parsing fails
					try {
						units = Integer.parseInt(unitStr); // Parsing the units normally
					} catch (NumberFormatException e) { // Handle cases like "1–3" by taking the highest value
						if (unitStr.contains("-")) {
							String[] range = unitStr.split("-"); // Split the range into [1,3]
							try {
								units = Integer.parseInt(range[1].trim()); // Use the upper bound or max (3)
							} catch (NumberFormatException ex) {
								units = 1; // If parsing fails, use 1
							}
						} 
					}

					String description = parts[parts.length - 1].trim(); // For trimming the description (will be at the last index)

					Course c = new Course(code, title, units, description); // Creates the Course object from parsed values
					courseMap.put(code, c); // Inserts into the map (overwrites duplicates)
				}
			} catch (Exception e) {
				return null;
			}
		}
        return courseMap; // Return all parsed courses
	}

	// Loading the CSV file of the offered course for the first semester
	public static ArrayList<OfferedCourse> loadOfferedCoursesFromCSV() {
		ArrayList<OfferedCourse> list = new ArrayList<>();
		Map<String, Course> courseMap = loadCourses(); // Load all course info first

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

				// Try to use existing Course object; otherwise, create a minimal one
				Course baseCourse = courseMap.getOrDefault(code,new Course(code, parts[1].trim(), Integer.parseInt(parts[2].trim()), ""));

				OfferedCourse oc = new OfferedCourse(baseCourse, section, times, days, room); // Build OfferedCourse using course info + schedule info
				list.add(oc); // Add to offered list
			}
		} catch (Exception e) {
			return null;
		}
		return list; // Return all offered courses
	}
}
