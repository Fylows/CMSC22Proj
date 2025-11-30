package backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public enum Degree {
		BSCS, MSCS,	PHD, MSIT
	}
	
	/**
	 * courseDegreeMap
	 * 
	 * An Immutable map that consists of all courses from ICT, mapped to their respective degrees
	 */
	public static Map<String, Degree> courseDegreeMap = Map.ofEntries(
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
	/**
	 * prereqMap
	 * 
	 * An Immutable map that consists of the prerequisites of all the courses
	 * 
	 */
	public static Map<String, List<String>> prereqMap = Map.ofEntries(
			// Bachelor of Science in Computer Science (BSCS)
			Map.entry("CMSC 21", List.of("CMSC 12")),
			Map.entry("CMSC 57", List.of("CMSC 56")),
			
			Map.entry("CMSC 22", List.of("CMSC 12")),
			Map.entry("CMSC 123", List.of("CMSC 21","CMSC 57")),
			Map.entry("CMSC 130", List.of("CMSC 57")),
			
			Map.entry("CMSC 23", List.of("CMSC 22")),
			Map.entry("CMSC 100", List.of("CMSC 22")),
			Map.entry("CMSC 127", List.of("CMSC 22")),
			Map.entry("CMSC 131", List.of("CMSC 21")),
			
			Map.entry("CMSC 125", List.of("CMSC 123")),
			Map.entry("CMSC 124", List.of("CMSC 123")),
			Map.entry("CMSC 141", List.of("CMSC 123")),
			Map.entry("CMSC 170", List.of("CMSC 123")),
			Map.entry("CMSC 132", List.of("CMSC 131")),
			
			Map.entry("CMSC 137", List.of("CMSC 137")),
			Map.entry("CMSC 128", List.of("CMSC 123")),
			Map.entry("CMSC 142", List.of("CMSC 123")),
			Map.entry("CMSC 174", List.of("CMSC 123")),
			Map.entry("CMSC 180", List.of("CMSC 132")),
	
			
			// Master of Science in Computer Science (MSCS)
//			Map.entry("CMSC 214", List.of("CMSC 124", "CMSC 141")),
//			Map.entry("CMSC 215", List.of("CMSC 125", "CMSC 131")),
//			Map.entry("CMSC 244", List.of("CMSC 142")),
			Map.entry("CMSC 245", List.of("CMSC 244")),
	
	
			// Master of Information Technology (MIT)
			Map.entry("IT 227", List.of("IT 210", "IT 226")),
			Map.entry("IT 280", List.of("IT 238"))
//			Map.entry("CMSC 272", List.of("CMSC 127")),
//
//			// PhD Computer Science (PhD)
//			Map.entry("CMSC 341", List.of("CMSC 241")),
//			Map.entry("CMSC 342", List.of("CMSC 245"))

		);
	

	
	// Maps of the different degrees with their respective courses
	private static Map<String, Course> BSCS = new HashMap<>();
	private static Map<String, Course> MSCS = new HashMap<>();
	private static Map<String, Course> PHD = new HashMap<>();
	private static Map<String, Course> MSIT = new HashMap<>();
	// TODO add a observable list when doing UI

	
	
	/**
	 * getCourse
	 * 
	 * gets the course from the specific degree
	 * 
	 * @param	course code to be used as the search key of the Course Object
	 * @param	the degree of the course
	 * @return	returns the course of the given course code
	 */
	
	public static void loadFromCSV() {
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
				
				
				Course course = new Course(code, parts[1].trim(), Integer.parseInt(parts[2].trim()), "");


			}
		} catch (Exception e) {}
	}
	
	
	
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
	
	
}
