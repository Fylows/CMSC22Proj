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
		BSCS,
		MSCS,
		PHCS,
		MSIT
	}
	/**
	 * courseDegreeMap
	 * 
	 * An Immutable map that consists of all courses from ICT, mapped to their respective degrees
	 */
	private static Map<String, Degree> courseDegreeMap = Map.ofEntries(
			// Bachelors
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
			
			// Masters
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
			
			// Doctorate
			Map.entry("CMSC 214", Degree.PHCS),
			Map.entry("CMSC 215", Degree.PHCS),
			Map.entry("CMSC 227", Degree.PHCS),
			Map.entry("CMSC 241", Degree.PHCS),
			Map.entry("CMSC 244", Degree.PHCS),
			Map.entry("CMSC 245", Degree.PHCS),
			Map.entry("CMSC 250", Degree.PHCS),
			Map.entry("CMSC 265", Degree.PHCS),
			Map.entry("CMSC 271", Degree.PHCS),
			Map.entry("CMSC 272", Degree.PHCS),
			Map.entry("CMSC 280", Degree.PHCS),
			Map.entry("CMSC 290", Degree.PHCS),
			Map.entry("CMSC 291", Degree.PHCS),
			Map.entry("CMSC 299", Degree.PHCS),
			Map.entry("CMSC 300", Degree.PHCS),
			
			// Masters IT
			Map.entry("IT 210", Degree.MSIT),
			Map.entry("IT 226", Degree.MSIT),
			Map.entry("IT 227", Degree.MSIT),
			Map.entry("IT 238", Degree.MSIT),
			Map.entry("IT 280", Degree.MSIT),
			Map.entry("IT 295", Degree.MSIT),
			Map.entry("IT 299", Degree.MSIT)
	);
	
	
	/**
	 *  Maps of the different degrees with their respective courses
	 */
	private static Map<String, Course> BSCS  = new HashMap<>();
	private static Map<String, Course> MSCS  = new HashMap<>();
	private static Map<String, Course> PHCS  = new HashMap<>();
	private static Map<String, Course> MSIT  = new HashMap<>();
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
	public static Course getCourse(String courseCode, String degree) {
		switch(degree) {
			case "BSCS":
				return BSCS.get(courseCode);
			case "MSCS":
				return MSCS.get(courseCode);
			case "PHCS":
				return PHCS.get(courseCode);
			case "MSIT":
				return MSIT.get(courseCode);
		}
		return null;
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
		return courseDegreeMap.get(courseCode).name();
	}
}
