package backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An Object to store and manage the different Courses
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
			
			Map.entry("CMSC 137", List.of("CMSC 125")),
			Map.entry("CMSC 128", List.of("CMSC 123")),
			Map.entry("CMSC 142", List.of("CMSC 123")),
			Map.entry("CMSC 174", List.of("CMSC 123")),
			Map.entry("CMSC 180", List.of("CMSC 132")),
			
			// Electives in BSCS
			Map.entry("CMSC 129", List.of("CMSC 124")),
			Map.entry("CMSC 161", List.of("CMSC 123")),
			Map.entry("CMSC 165", List.of("CMSC 123")),
			Map.entry("CMSC 172", List.of("CMSC 123")),
			Map.entry("CMSC 191", List.of("CMSC 123")),
			
			// Master of Science in Computer Science (MSCS)
			Map.entry("CMSC 245", List.of("CMSC 244")),
	
	
			// Master of Information Technology (MIT)
			Map.entry("IT 227", List.of("IT 210", "IT 226")),
			Map.entry("IT 280", List.of("IT 238"))
		);
	

	
	// Maps of the different degrees with their respective courses
	private static Map<String, Course> BSCS = new LinkedHashMap<>();
	private static Map<String, Course> MSCS = new LinkedHashMap<>();
	private static Map<String, Course> PHD = new LinkedHashMap<>();
	private static Map<String, Course> MSIT = new LinkedHashMap<>();
	// TODO add a observable list when doing UI

	
	
	/**
	 * loadFromCSV
	 * 
	 * Loads all degree courses into hashmaps
	 * 
	 */
	public static void loadFromCSV() {
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
					String title = parts[1].trim(); // Trims the course name

					// For trimming the units (some units have range)
					String unitStr = parts[2].trim();
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

					// For trimming the description (some description have commas)
					StringBuilder descBuilder = new StringBuilder();
					for (int i = 3; i < parts.length; i++) {
						if (i > 3) descBuilder.append(","); // Restores the commas in the text
						descBuilder.append(parts[i].trim()); 
					}
					String description = descBuilder.toString(); // Assigns the fixed description


					Course c = new Course(code, title, units, description); // Creates the Course object from parsed values
					String degree = c.getType();
					switch(degree) {
						case "BSCS" -> BSCS.put(code, c);
						case "MSCS" -> MSCS.put(code, c);
						case "PHD" -> PHD.put(code, c);
						case "MSIT" -> MSIT.put(code, c);
					}
				}
			} catch (Exception e) {
				return;
			}
		}
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

	public static List<Course> getCoursesByDegree(String degree) {
	    return switch (degree) {
	        case "BSCS" -> new ArrayList<>(BSCS.values());
	        case "MSCS" -> new ArrayList<>(MSCS.values());
	        case "PHD"  -> new ArrayList<>(PHD.values());
	        case "MSIT" -> new ArrayList<>(MSIT.values());
	        default     -> List.of(); // empty, safe list
	    };
	}

}
