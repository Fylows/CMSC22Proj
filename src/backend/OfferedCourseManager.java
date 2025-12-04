package backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * OffereCourseManager
 * 
 * A class for managing all offered courses
 */
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
<<<<<<< Updated upstream
		ArrayList<OfferedCourse> list = new ArrayList<>();
		Map<String, OfferedCourse> lectureMap = new HashMap<>();
		Set<String> lecturesWithLabs = new HashSet<>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(OfferedCourseManager.class.getResourceAsStream("/dataset/course_offerings.csv")))) {
			
			String line;
			
			while ((line = br.readLine()) != null) {
				if (line.isBlank() || line.toLowerCase().contains("course code")) continue; // Skip the header of the CSV file

				String[] parts = line.split(","); // Split the lines by commas
				if (parts.length < 7) continue; // Must contain code, title, units, section, time, days, room, if one is missing, skip

				String code = parts[0].trim(); // Trims the course code
				String section = parts[3].trim(); // Trims the section (e.g., "U-1L")
				String times = parts[4].trim(); // Trims the time range
				String days = parts[5].trim(); // Trims the days
				String room = parts[6].trim(); // Trims the room

				Course baseCourse = CourseManager.getCourse(code, CourseManager.courseDegreeMap.get(code).name());
				if (baseCourse == null) continue;

				// Build OfferedCourse using course info + schedule info
				OfferedCourse oc = new OfferedCourse(baseCourse, section, times, days, room, "1st Semester"); 

				if (isLab(section)) {
					String parentSection = section.substring(0, section.indexOf("-")); // Extract lecture prefix: everything before the dash

					// Assign parent lecture if it exists
					OfferedCourse parentLecture = lectureMap.get(parentSection);
					if (parentLecture != null) {
						oc.setLec(parentLecture); // Assign instance field
						lecturesWithLabs.add(parentSection);
					}

					list.add(oc); // Always add the lab
	            } else {
	            	lectureMap.put(section, oc); // Lecture row: store it in the map, do not add yet
	            	oc.setLastLec(); // Set global static reference
	            }
			}

			// After reading CSV, add only lectures that have no labs
			for (String lecSection : lectureMap.keySet()) {
				if (!lecturesWithLabs.contains(lecSection)) {
					list.add(lectureMap.get(lecSection));
				}
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return null;
	    }
		return list;
=======
	    ArrayList<OfferedCourse> list = new ArrayList<>();
	    Map<String, OfferedCourse> lectureMap = new HashMap<>();
	    Set<String> lecturesWithLabs = new HashSet<>();

	    try (BufferedReader br = new BufferedReader(
	            new InputStreamReader(OfferedCourseManager.class.getResourceAsStream(
	                    "/dataset/course_offerings.csv")))) {

	        String line;
	        while ((line = br.readLine()) != null) {

	            if (line.isBlank() || line.toLowerCase().contains("course code")) continue;

	            String[] parts = line.split(",");
	            if (parts.length < 7) continue;

	            String code = parts[0].trim();
	            String section = parts[3].trim();
	            String times = parts[4].trim();
	            String days = parts[5].trim();
	            String room = parts[6].trim();

	            Course baseCourse = CourseManager.getCourse(code,
	                    CourseManager.courseDegreeMap.get(code).name());
	            if (baseCourse == null) continue;

	            OfferedCourse oc = new OfferedCourse(baseCourse, section, times, days, room, "1st Semester");

	            if (isLab(section)) {
	                // Extract lecture prefix: everything before the dash
	                String parentSection = section.substring(0, section.indexOf("-"));

	                // Assign parent lecture if it exists
	                OfferedCourse parentLecture = lectureMap.get(parentSection);
	                if (parentLecture != null) {
	                    oc.setLec(parentLecture); // assign instance field
	                    lecturesWithLabs.add(parentSection);
	                }

	                // Always add the lab
	                list.add(oc);

	            } else {
	                // Lecture row: store it in the map, do not add yet
	                lectureMap.put(section, oc);
	                oc.setLastLec(); // set global static reference
	            }
	        }

	        // After reading CSV, add only lectures that have no labs
	        for (String lecSection : lectureMap.keySet()) {
	            if (!lecturesWithLabs.contains(lecSection)) {
	                list.add(lectureMap.get(lecSection));
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
//	    for (OfferedCourse oc : list) {
//	    	if (oc.getParentLecture() != null) System.out.println(oc.getCourseCode() + " | " + oc.getParentLecture().getCourseCode());
//	    }
	    return list;
>>>>>>> Stashed changes
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
