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
    private ArrayList<OfferedCourse> offeredCourses; // Courses offered this semester

    public OfferedCourseManager(ArrayList<Course> allCourses, ArrayList<OfferedCourse> offeredCourses) {
        this.allCourses = allCourses;
        this.offeredCourses = offeredCourses;
    }

    public ArrayList<Course> getAllCourses() {
        return allCourses;
    }

    public ArrayList<OfferedCourse> getOfferedCourses() {
        return offeredCourses;
    }

    /* ================================
       PERSISTENCE
       ================================= */
    
    public void save(Path path) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path));
            out.writeObject(this);
            out.close();
        } catch (IOException e) { return; }
    }

    public static OfferedCourseManager load(Path path) {
        try {
            ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path));
            OfferedCourseManager handler = (OfferedCourseManager) in.readObject();
            in.close();
            return handler;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static void saveData(ArrayList<Course> courses, ArrayList<OfferedCourse> offered) {
        OfferedCourseManager handler = new OfferedCourseManager(courses, offered);
        handler.save(Path.of("offered_courses.txt"));
    }

    public static OfferedCourseManager loadData() {
        OfferedCourseManager handler = load(Path.of("offered_courses.txt"));
        if (handler == null) return new OfferedCourseManager(new ArrayList<>(), new ArrayList<>());
        return handler;
    }

    /* ================================
       CSV LOADING
       ================================= */

    /**
     * Loads courses from the 4 degree-specific CSVs
     */
    public static Map<String, Course> loadCourses() {
        Map<String, Course> courseMap = new LinkedHashMap<>(); // Linked HashMap to preserve the order
        String[] files = {"/dataset/ics_cmsc_courses.csv", "/dataset/ics_mit_courses.csv", "/dataset/ics_mscs_courses.csv", "/dataset/ics_phd_courses.csv"};

        for (String resourcePath : files) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(OfferedCourseManager.class.getResourceAsStream(resourcePath)))) {
              
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isBlank() || line.toLowerCase().contains("code")) continue;

                    // Split line by commas
                    String[] parts = line.split(",");
                    if (parts.length < 4) continue;

                    // Code = first field
                    String code = parts[0].trim();

                    // Units = second-to-last field
                    String unitStr = parts[parts.length - 2].trim();
                    int units = 1;
                    try {
                        units = Integer.parseInt(unitStr);
                    } catch (NumberFormatException e) {
                        if (unitStr.contains("-")) {
                            String[] range = unitStr.split("-");
                            try {
                                units = Integer.parseInt(range[1].trim()); // take max
                            } catch (NumberFormatException ex) {
                                units = 1;
                            }
                        } else {
                            units = 1;
                        }
                    }

                    // Title = everything from index 1 to parts.length-3
                    StringBuilder titleBuilder = new StringBuilder();
                    for (int i = 1; i <= parts.length - 3; i++) {
                        if (i > 1) titleBuilder.append(","); // keep commas in title
                        titleBuilder.append(parts[i].trim());
                    }
                    String title = titleBuilder.toString();

                    // Description = last field
                    String desc = parts[parts.length - 1].trim();

                    // Create course
                    Course c = new Course(code, title, units);
                    courseMap.put(code, c);
                }

            } catch (Exception e) {
                System.out.println("Error loading CSV " + resourcePath + ": " + e.getMessage());
            }
        }

        return courseMap;
    }

    public static ArrayList<OfferedCourse> loadOfferedCoursesFromCSV() {
        ArrayList<OfferedCourse> list = new ArrayList<>();
        Map<String, Course> courseMap = loadCourses(); // Load all course info first

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(OfferedCourseManager.class.getResourceAsStream("/dataset/course_offerings.csv")))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank() || line.toLowerCase().contains("code")) continue;

                // Expect: code, title, units, section, times, days, room
                String[] parts = line.split(",");
                if (parts.length < 7) continue;

                String code = parts[0].trim();
                String section = parts[3].trim();
                String times = parts[4].trim();
                String days = parts[5].trim();
                String room = parts[6].trim();

                Course baseCourse = courseMap.getOrDefault(code, 
                        new Course(code, parts[1].trim(), Integer.parseInt(parts[2].trim())));
                OfferedCourse oc = new OfferedCourse(baseCourse, section, times, days, room);
                list.add(oc);
            }
        } catch (Exception e) {
            System.out.println("Error loading course offering CSV: " + e.getMessage());
        }

        return list;
    }
}
