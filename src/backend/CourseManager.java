package backend;

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
	public static Map<String, Set<Degree>> courseDegreeMap = Map.ofEntries(
			// Bachelor of Science in Computer Science (BSCS)
			Map.entry("CMSC 2", Set.of(Degree.BSCS)),
			Map.entry("CMSC 11", Set.of(Degree.BSCS)),
			Map.entry("CMSC 12", Set.of(Degree.BSCS)),
			Map.entry("CMSC 21", Set.of(Degree.BSCS, Degree.MSCS)),
			Map.entry("CMSC 22", Set.of(Degree.BSCS, Degree.MSCS, Degree.MSIT)),
			Map.entry("CMSC 23", Set.of(Degree.BSCS)),
			Map.entry("CMSC 56", Set.of(Degree.BSCS)),
			Map.entry("CMSC 57", Set.of(Degree.BSCS)),
			Map.entry("CMSC 100", Set.of(Degree.BSCS)),
			Map.entry("CMSC 123", Set.of(Degree.BSCS, Degree.MSCS)),
			Map.entry("CMSC 124", Set.of(Degree.BSCS, Degree.MSCS)),
			Map.entry("CMSC 125", Set.of(Degree.BSCS, Degree.MSCS)),
			Map.entry("CMSC 127", Set.of(Degree.BSCS, Degree.MSIT)),
			Map.entry("CMSC 128", Set.of(Degree.BSCS)),
			Map.entry("CMSC 129", Set.of(Degree.BSCS)),
			Map.entry("CMSC 130", Set.of(Degree.BSCS)),
			Map.entry("CMSC 131", Set.of(Degree.BSCS, Degree.MSCS)),
			Map.entry("CMSC 132", Set.of(Degree.BSCS)),
			Map.entry("CMSC 137", Set.of(Degree.BSCS)),
			Map.entry("CMSC 141", Set.of(Degree.BSCS, Degree.MSCS)),
			Map.entry("CMSC 142", Set.of(Degree.BSCS, Degree.MSCS)),
			Map.entry("CMSC 150", Set.of(Degree.BSCS)),
			Map.entry("CMSC 161", Set.of(Degree.BSCS)),
			Map.entry("CMSC 165", Set.of(Degree.BSCS)),
			Map.entry("CMSC 170", Set.of(Degree.BSCS)),
			Map.entry("CMSC 172", Set.of(Degree.BSCS)),
			Map.entry("CMSC 173", Set.of(Degree.BSCS)),
			Map.entry("CMSC 180", Set.of(Degree.BSCS)),
			Map.entry("CMSC 190", Set.of(Degree.BSCS)),
			Map.entry("CMSC 191", Set.of(Degree.BSCS)),
			Map.entry("CMSC 198", Set.of(Degree.BSCS)),
			Map.entry("CMSC 199", Set.of(Degree.BSCS)),
			Map.entry("CMSC 200", Set.of(Degree.BSCS)),
			
			// Master of Science in Computer Science (MSCS)
			Map.entry("CMSC 214", Set.of(Degree.MSCS, Degree.MSIT)),
			Map.entry("CMSC 215", Set.of(Degree.MSCS, Degree.MSIT)),
			Map.entry("CMSC 227", Set.of(Degree.MSCS)),
			Map.entry("CMSC 241", Set.of(Degree.MSCS, Degree.PHD)),
			Map.entry("CMSC 244", Set.of(Degree.MSCS, Degree.PHD)),
			Map.entry("CMSC 245", Set.of(Degree.MSCS, Degree.PHD)),
			Map.entry("CMSC 250", Set.of(Degree.MSCS)),
			Map.entry("CMSC 265", Set.of(Degree.MSCS)),
			Map.entry("CMSC 271", Set.of(Degree.MSCS)),
			Map.entry("CMSC 272", Set.of(Degree.MSCS, Degree.MSIT)),
			Map.entry("CMSC 280", Set.of(Degree.MSCS)),
			Map.entry("CMSC 290", Set.of(Degree.MSCS)),
			Map.entry("CMSC 291", Set.of(Degree.MSCS)),
			Map.entry("CMSC 299", Set.of(Degree.MSCS)),
			Map.entry("CMSC 300", Set.of(Degree.MSCS)),
		
			// Master of Information Technology (MIT)
			Map.entry("IT 210", Set.of(Degree.MSIT)),
			Map.entry("IT 226", Set.of(Degree.MSIT)),
			Map.entry("IT 227", Set.of(Degree.MSIT)),
			Map.entry("IT 238", Set.of(Degree.MSIT)),
			Map.entry("IT 280", Set.of(Degree.MSIT)),
			Map.entry("IT 295", Set.of(Degree.MSIT)),
			Map.entry("IT 299", Set.of(Degree.MSIT)),
			
			// PhD Computer Science (PhD)
			Map.entry("CMSC 341", Set.of(Degree.PHD)),
			Map.entry("CMSC 342", Set.of(Degree.PHD)),
			Map.entry("CMSC 391", Set.of(Degree.PHD)),
			Map.entry("CMSC 399", Set.of(Degree.PHD)),
			Map.entry("CMSC 400", Set.of(Degree.PHD))
						
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
			Map.entry("CMSC 214", List.of("CMSC 124", "CMSC 141")),
			Map.entry("CMSC 215", List.of("CMSC 125", "CMSC 131")),
			Map.entry("CMSC 244", List.of("CMSC 142")),
			Map.entry("CMSC 245", List.of("CMSC 244")),
	
	
			// Master of Information Technology (MIT)
			Map.entry("IT 227", List.of("IT 210", "IT 226")),
			Map.entry("IT 280", List.of("IT 238")),
			Map.entry("CMSC 272", List.of("CMSC 127")),

			// PhD Computer Science (PhD)
			Map.entry("CMSC 341", List.of("CMSC 241")),
			Map.entry("CMSC 342", List.of("CMSC 245"))

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
