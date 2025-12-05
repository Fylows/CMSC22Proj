package backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OfferedCourseManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private static ArrayList<OfferedCourse> offeredCourses; // Courses offered
	private static final Path STORAGE_PATH = Path.of("src/storage/offered_courses.txt");

	// Constructor
	public OfferedCourseManager(ArrayList<OfferedCourse> offeredCourses) {
		OfferedCourseManager.offeredCourses = offeredCourses;
	}

	// Getters
	public static ArrayList<OfferedCourse> getOfferedCourses() {
		return offeredCourses;
	}

	// Savers and Loaders
	// Loading the CSV file of the offered course for the first semester
	public static ArrayList<OfferedCourse> loadOfferedCoursesFromCSV() {
	    ArrayList<OfferedCourse> list = new ArrayList<>();
	    Map<String, OfferedCourse> lectureMap = new HashMap<>();
	    Set<String> lecturesWithLabs = new HashSet<>();

	    try (BufferedReader br = new BufferedReader(
	            new InputStreamReader(OfferedCourseManager.class.getResourceAsStream("/dataset/course_offerings.csv")))) {

	        String line;
	        while ((line = br.readLine()) != null) {
	        	// Skip header
	            if (line.isBlank() || line.toLowerCase().contains("course code"))
	                continue;

	            // get parts
	            String[] parts = line.split(",");
	            if (parts.length < 7)
	                continue;

	            String code = parts[0].trim();
	            String section = parts[3].trim();
	            String times = parts[4].trim();
	            String days = parts[5].trim();
	            String room = parts[6].trim();

	            // Load base course
	            Course baseCourse = CourseManager.getCourse(code,
	                    CourseManager.courseDegreeMap.get(code).name());
	            if (baseCourse == null)
	                continue;

	            // Create offered course
	            OfferedCourse oc = new OfferedCourse(baseCourse, section, times, days, room, "1st Semester");

	            // Create unique key: courseCode-section
	            String lectureKey = code + "-" + section;
	            
	            if (isLab(section)) {

	                // Parent section is everything before hyphen
	                String parentSection = section.substring(0, section.indexOf("-"));
	                String parentKey = code + "-" + parentSection;

	                OfferedCourse parentLecture = lectureMap.get(parentKey);

	                if (parentLecture != null) {
	                    oc.setLec(parentLecture);     // link lab → its lecture
	                    lecturesWithLabs.add(parentKey);
	                }

	                list.add(oc); // labs are always added immediately

	            } else {
	                // Lecture — store but do not add yet
	                lectureMap.put(lectureKey, oc);
	                oc.setLastLec(); // preserve your existing logic
	            }
	        }

	        // Add only lecture entries that have no labs
	        for (String key : lectureMap.keySet()) {
	            if (!lecturesWithLabs.contains(key)) {
	                list.add(lectureMap.get(key));
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }

	    return list;
	}
	
	// Persistence Functions (saving/loading)
	// Saves the entire OfferedCourseManager object to a file
	public static void save(OfferedCourseManager manager) {
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(STORAGE_PATH))) { // Put in the try block so it automatically closes at the end
			out.writeObject(manager); // Writes all the courses
		} catch (IOException e) {
			e.printStackTrace(); // Prints a detailed error report to the console
		}
	}

	// Loads the courses from the CSV
	public static OfferedCourseManager load() {
		OfferedCourseManager fallback = new OfferedCourseManager(OfferedCourseManager.loadOfferedCoursesFromCSV());
		
		if (!Files.exists(STORAGE_PATH)) return fallback; // File doesn't exist fallback to CSV loading

		try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(STORAGE_PATH))) { // Put in the try block so it automatically closes at the end
			OfferedCourseManager manager = (OfferedCourseManager) in.readObject(); // Read the serialized OfferedCourseManager object
			return manager; // Return the loaded object
		} catch (IOException | ClassNotFoundException e) {
			return fallback; // Return default course manager if failed to load
		}
	}
	
	private static boolean isLab(String section) {
		return section.matches(".+-\\d+L");
	}
}
