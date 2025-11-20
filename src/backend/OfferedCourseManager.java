package backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    // Paths to CSVs
    private static final String BSCS_CSV = "src/dataset/bscs.csv";
    private static final String MSCS_CSV = "src/dataset/mscs.csv";
    private static final String MSIT_CSV = "src/dataset/msit.csv";
    private static final String PHD_CSV  = "src/dataset/phd.csv";
    private static final String OFFERING_CSV = "src/dataset/course_offering.csv";

    /**
     * Loads courses from the 4 degree-specific CSVs
     */
    public static Map<String, Course> loadCourses() {
        Map<String, Course> courseMap = new HashMap<>();
        String[] files = {BSCS_CSV, MSCS_CSV, MSIT_CSV, PHD_CSV};

        for (String path : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Skip empty lines or headers
                    if (line.isBlank() || line.toLowerCase().contains("code")) continue;

                    String[] parts = line.split(",");
                    if (parts.length < 4) continue; // Expect code, title, units, description

                    String code = parts[0].trim();
                    String title = parts[1].trim();
                    int units = Integer.parseInt(parts[2].trim());
                    String desc = parts[3].trim();

                    Course c = new Course(code, title, units);
                    // Optionally, add description to Course class later
                    courseMap.put(code, c);
                }
            } catch (IOException e) {
                System.out.println("Error loading CSV " + path + ": " + e.getMessage());
            }
        }

        return courseMap;
    }

    /**
     * Loads offered courses from the offering CSV, merging with Course info
     */
    public static ArrayList<OfferedCourse> loadOfferedCoursesFromCSV() {
        ArrayList<OfferedCourse> list = new ArrayList<>();
        Map<String, Course> courseMap = loadCourses(); // Load all course info first

        try (BufferedReader br = new BufferedReader(new FileReader(OFFERING_CSV))) {
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

                Course baseCourse = courseMap.getOrDefault(code, new Course(code, parts[1].trim(), Integer.parseInt(parts[2].trim())));
                OfferedCourse oc = new OfferedCourse(baseCourse, section, times, days, room);
                list.add(oc);
            }
        } catch (IOException e) {
            System.out.println("Error loading course offering CSV: " + e.getMessage());
        }

        return list;
    }
}
