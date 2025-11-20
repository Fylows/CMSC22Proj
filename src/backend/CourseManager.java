package backend;

import java.util.HashMap;
import java.util.Map;

/**
 * An Object to manage the different Courses
 * 
 * @author Ljiel Saplan
 * @since 1.0
 */

public class CourseManager {
	/**
	 * Degree
	 * an Enum to get the different degrees
	 */
	private enum Degree {
		BSCS, MSCS,	PHD, MSIT
	}
	
	/**
	 * courseDegreeMap
	 * 
	 * An Immutable map that consists of all courses from ICT, mapped to their respective degrees
	 */
	private static Map<String, Degree> courseDegreeMap = Map.ofEntries(
			// Bachelor of Science in Computer Science (BSCS)
			Map.entry("CMSC 2", Degree.BSCS),
			Map.entry("CMSC 11", Degree.BSCS),
			Map.entry("CMSC 12", Degree.BSCS),
			Map.entry("CMSC 21", Degree.BSCS),
			Map.entry("CMSC 22", Degree.BSCS),
			Map.entry("CMSC 23", Degree.BSCS),
			Map.entry("CMSC 56", Degree.BSCS),
			Map.entry("CMSC 57", Degree.BSCS),
			Map.entry("CMSC 100", Degree.BSCS),
			Map.entry("CMSC 123", Degree.BSCS),
			Map.entry("CMSC 124", Degree.BSCS),
			Map.entry("CMSC 125", Degree.BSCS),
			Map.entry("CMSC 127", Degree.BSCS),
			Map.entry("CMSC 128", Degree.BSCS),
			Map.entry("CMSC 129", Degree.BSCS),
			Map.entry("CMSC 130", Degree.BSCS),
			Map.entry("CMSC 131", Degree.BSCS),
			Map.entry("CMSC 132", Degree.BSCS),
			Map.entry("CMSC 137", Degree.BSCS),
			Map.entry("CMSC 141", Degree.BSCS),
			Map.entry("CMSC 142", Degree.BSCS),
			Map.entry("CMSC 150", Degree.BSCS),
			Map.entry("CMSC 161", Degree.BSCS),
			Map.entry("CMSC 165", Degree.BSCS),
			Map.entry("CMSC 170", Degree.BSCS),
			Map.entry("CMSC 172", Degree.BSCS),
			Map.entry("CMSC 173", Degree.BSCS),
			Map.entry("CMSC 180", Degree.BSCS),
			Map.entry("CMSC 190", Degree.BSCS),
			Map.entry("CMSC 191", Degree.BSCS),
			Map.entry("CMSC 198", Degree.BSCS),
			Map.entry("CMSC 199", Degree.BSCS),
			Map.entry("CMSC 200", Degree.BSCS),
			
			// Master of Science in Computer Science (MSCS)
			Map.entry("CMSC 214", Degree.MSCS),
			Map.entry("CMSC 215", Degree.MSCS),
			Map.entry("CMSC 227", Degree.MSCS),
			Map.entry("CMSC 241", Degree.MSCS),
			Map.entry("CMSC 244", Degree.MSCS),
			Map.entry("CMSC 245", Degree.MSCS),
			Map.entry("CMSC 250", Degree.MSCS),
			Map.entry("CMSC 265", Degree.MSCS),
			Map.entry("CMSC 271", Degree.MSCS),
			Map.entry("CMSC 272", Degree.MSCS),
			Map.entry("CMSC 280", Degree.MSCS),
			Map.entry("CMSC 290", Degree.MSCS),
			Map.entry("CMSC 291", Degree.MSCS),
			Map.entry("CMSC 299", Degree.MSCS),
			Map.entry("CMSC 300", Degree.MSCS),
		
			// Master of Information Technology (MIT)
			Map.entry("IT 210", Degree.MSIT),
			Map.entry("IT 226", Degree.MSIT),
			Map.entry("IT 227", Degree.MSIT),
			Map.entry("IT 238", Degree.MSIT),
			Map.entry("IT 280", Degree.MSIT),
			Map.entry("IT 295", Degree.MSIT),
			Map.entry("IT 299", Degree.MSIT),
			
			// PhD Computer Science (PhD)
			Map.entry("CMSC 341", Degree.PHD),
			Map.entry("CMSC 342", Degree.PHD),
			Map.entry("CMSC 391", Degree.PHD),
			Map.entry("CMSC 399", Degree.PHD),
			Map.entry("CMSC 400", Degree.PHD)
						
	);
	
	// Maps of the different degrees with their respective courses
	private static Map<String, Course> BSCS = new HashMap<>();
	private static Map<String, Course> MSCS = new HashMap<>();
	private static Map<String, Course> PHD = new HashMap<>();
	private static Map<String, Course> MSIT = new HashMap<>();
	// TODO add a observable list when doing UI
	
	// Temporary Initializer (placeholder until file handling is okay)
	static {
		for (String courseCode : courseDegreeMap.keySet()) { // Loops through every course code defined in the course-degree map
			Degree degree = courseDegreeMap.get(courseCode); // Get the degree associated with this course (BSCS, MSCS, PHD, MSIT)

			// Temporary only placeholder data — replace with CSV loading later
			Course c = new Course(courseCode, "TBA Title", 3);

			switch (degree) { // Add the course to the appropriate Map based on its degree
				case BSCS -> BSCS.put(courseCode, c); // Use -> for a cleaner switch case look
				case MSCS -> MSCS.put(courseCode, c);
				case PHD -> PHD.put(courseCode, c);
				case MSIT -> MSIT.put(courseCode, c);
			}
		}
	}
	
	/**
	 * getCourse
	 * 
	 * gets the course from the specific degree
	 * 
	 * @param	course code to be used as the search key of the Course Object
	 * @param	the degree of the course
	 * @return	returns the course of the given course code
	 */
	
	// Getters
	public static Course getCourse(String courseCode, String degree) {
		return switch (degree) {
			case "BSCS" -> BSCS.get(courseCode);
			case "MSCS" -> MSCS.get(courseCode);
			case "PHD" -> PHD.get(courseCode);
			case "MSIT" -> MSIT.get(courseCode);
			default -> null; // If the degree does not match any known category, return null
		};
	}
		
	/**
	 * getCourseDegree
	 * 
	 * gets the degree given a course code
	 * 
	 * @param	course code to find the degree of
	 * @return	returns the degree of the given course code
	 */
	public static String getCourseDegree(String courseCode) {
		Degree d = courseDegreeMap.get(courseCode);
		if (d == null) {
			return null; // Return null if course not found
		}

		return d.name(); // Return the course if found
	}
}
