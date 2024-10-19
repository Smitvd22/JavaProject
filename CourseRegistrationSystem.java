package Assignment_1_trial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;

public class CourseRegistrationSystem {
    private List<User> users;
    private List<Course> courses;
    private List<Complaint> complaints;
    private Scanner scanner;

    public CourseRegistrationSystem() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        complaints = new ArrayList<>();
        scanner = new Scanner(System.in);
        initializeData();
    }

    private void initializeData() {
        // Add sample data (users, courses, etc.)
        users.add(new Student("John Doe", "john@example.com", "password123"));
        users.add(new Professor("Jane Smith", "jane@example.com", "prof456"));
        users.add(new Administrator("Admin", "admin@example.com", "admin123"));
        
        courses.add(new Course("CS101", "Introduction to Programming", 3));
        courses.add(new Course("CS201", "Data Structures", 4));
    }

    public void start() {
        while (true) {
            System.out.println("Welcome to the University Course Registration System");
            System.out.println("1. Enter the Application");
            System.out.println("2. Exit the Application");
            int choice = getIntInput("Enter your choice: ");

            if (choice == 1) {
                login();
            } else if (choice == 2) {
                System.out.println("Thank you for using the Course Registration System. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void login() {
        System.out.println("Login as:");
        System.out.println("1. Student");
        System.out.println("2. Professor");
        System.out.println("3. Administrator");
        int role = getIntInput("Enter your role: ");

        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = authenticateUser(email, password, role);
        if (user != null) {
            handleUserMenu(user);
        } else {
            System.out.println("Invalid credentials or user not found.");
        }
    }

    private User authenticateUser(String email, String password, int role) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                if ((role == 1 && user instanceof Student) ||
                    (role == 2 && user instanceof Professor) ||
                    (role == 3 && user instanceof Administrator)) {
                    return user;
                }
            }
        }
        return null;
    }

    private void handleUserMenu(User user) {
        while (true) {
            user.displayMenu();
            int choice = getIntInput("Enter your choice: ");

            if (user instanceof Student) {
                if (handleStudentMenu((Student) user, choice)) break;
            } else if (user instanceof Professor) {
                if (handleProfessorMenu((Professor) user, choice)) break;
            } else if (user instanceof Administrator) {
                if (handleAdminMenu((Administrator) user, choice)) break;
            }
        }
    }

    private boolean handleStudentMenu(Student student, int choice) {
        switch (choice) {
            case 1:
                viewAvailableCourses();
                break;
            case 2:
                registerForCourses(student);
                break;
            case 3:
                viewSchedule(student);
                break;
            case 4:
                trackAcademicProgress(student);
                break;
            case 5:
                dropCourse(student);
                break;
            case 6:
                submitComplaint(student);
                break;
            case 7:
                System.out.println("Logging out...");
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    private boolean handleProfessorMenu(Professor professor, int choice) {
        switch (choice) {
            case 1:
                manageCourses(professor);
                break;
            case 2:
                viewEnrolledStudents(professor);
                break;
            case 3:
                System.out.println("Logging out...");
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    private boolean handleAdminMenu(Administrator admin, int choice) {
        switch (choice) {
            case 1:
                manageCourses(admin);
                break;
            case 2:
                manageStudentRecords();
                break;
            case 3:
                assignProfessorsToCourses();
                break;
            case 4:
                handleComplaints();
                break;
            case 5:
                System.out.println("Logging out...");
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }
    
    private void submitComplaint(Student student) {
        System.out.println("Submit a Complaint");
        System.out.print("Please describe your complaint: ");
        String complaintText = scanner.nextLine();
        
        Complaint newComplaint = new Complaint(student, complaintText);
        complaints.add(newComplaint);
        
        System.out.println("Your complaint has been submitted successfully.");
    }

    private void handleComplaints() {
        if (complaints.isEmpty()) {
            System.out.println("There are no complaints to handle at this time.");
            return;
        }

        System.out.println("Handling Complaints");
        Iterator<Complaint> iterator = complaints.iterator();
        while (iterator.hasNext()) {
            Complaint complaint = iterator.next();
            System.out.println("\nComplaint from " + complaint.getStudent().getName() + ":");
            System.out.println(complaint.getText());
            
            System.out.print("Mark this complaint as resolved? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("y")) {
                iterator.remove();
                System.out.println("Complaint marked as resolved and removed from the list.");
            } else {
                System.out.println("Complaint left unresolved.");
            }
        }
        
        System.out.println("\nAll complaints have been reviewed.");
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void viewAvailableCourses() {
        System.out.println("Available Courses:");
        for (Course course : courses) {
            System.out.println(course.getCode() + " - " + course.getName() + " (" + course.getCredits() + " credits)");
        }
    }

    private void registerForCourses(Student student) {
        viewAvailableCourses();
        System.out.print("Enter the course code you want to register for: ");
        String courseCode = scanner.nextLine();
        Course selectedCourse = findCourseByCode(courseCode);
        if (selectedCourse != null) {
            student.registerForCourse(selectedCourse);
            System.out.println("Successfully registered for " + selectedCourse.getName());
        } else {
            System.out.println("Course not found.");
        }
    }

    private void viewSchedule(Student student) {
        List<Course> enrolledCourses = student.getEnrolledCourses();
        if (enrolledCourses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
        } else {
            System.out.println("Your Schedule:");
            for (Course course : enrolledCourses) {
                System.out.println(course.getCode() + " - " + course.getName());
            }
        }
    }

    private void trackAcademicProgress(Student student) {
        System.out.println("Academic Progress:");
        System.out.println("Current Semester: " + student.getCurrentSemester());
        System.out.println("CGPA: " + student.getCgpa());
        System.out.println("Completed Courses:");
        for (Course course : student.getCompletedCourses()) {
            System.out.println(course.getCode() + " - " + course.getName());
        }
    }

    private void dropCourse(Student student) {
        viewSchedule(student);
        System.out.print("Enter the course code you want to drop: ");
        String courseCode = scanner.nextLine();
        Course selectedCourse = findCourseByCode(courseCode);
        if (selectedCourse != null && student.getEnrolledCourses().contains(selectedCourse)) {
            student.dropCourse(selectedCourse);
            System.out.println("Successfully dropped " + selectedCourse.getName());
        } else {
            System.out.println("Course not found or not enrolled.");
        }
    }
    
    private void viewTaughtCourses(Professor professor) {
        List<Course> taughtCourses = professor.getTaughtCourses();
        if (taughtCourses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
        } else {
            System.out.println("Courses you are teaching:");
            for (Course course : taughtCourses) {
                System.out.println(course.getCode() + " - " + course.getName());
            }
        }
    }

    private void addNewCourse() {
        System.out.print("Enter course code: ");
        String code = scanner.nextLine();
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();
        System.out.print("Enter course credits: ");
        int credits = getIntInput("Credits: ");

        Course newCourse = new Course(code, name, credits);
        courses.add(newCourse);
        System.out.println("Course added successfully.");
    }

    private void removeCourse() {
        viewAllCourses();
        System.out.print("Enter the code of the course to remove: ");
        String code = scanner.nextLine();
        Course courseToRemove = findCourseByCode(code);
        if (courseToRemove != null) {
            courses.remove(courseToRemove);
            System.out.println("Course removed successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }

    private void manageCourses(Professor professor) {
        System.out.println("1. View taught courses");
        System.out.println("2. Add a new course");
        System.out.println("3. Remove a course");
        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                viewTaughtCourses(professor);
                break;
            case 2:
                addNewCourse();
                break;
            case 3:
                removeCourse();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewEnrolledStudents(Professor professor) {
        List<Course> taughtCourses = professor.getTaughtCourses();
        if (taughtCourses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
        } else {
            for (Course course : taughtCourses) {
                System.out.println("Enrolled students for " + course.getName() + ":");
                for (Student student : course.getEnrolledStudents()) {
                    System.out.println("- " + student.getName());
                }
            }
        }
    }

    private void manageCourses(Administrator admin) {
        System.out.println("1. Add a new course");
        System.out.println("2. Remove a course");
        System.out.println("3. View all courses");
        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addNewCourse();
                break;
            case 2:
                removeCourse();
                break;
            case 3:
                viewAllCourses();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void manageStudentRecords() {
        System.out.println("1. View all students");
        System.out.println("2. Update student information");
        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                viewAllStudents();
                break;
            case 2:
                updateStudentInformation();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void assignProfessorsToCourses() {
        viewAllCourses();
        System.out.print("Enter the course code to assign a professor: ");
        String courseCode = scanner.nextLine();
        Course selectedCourse = findCourseByCode(courseCode);
        if (selectedCourse != null) {
            viewAllProfessors();
            System.out.print("Enter the email of the professor to assign: ");
            String profEmail = scanner.nextLine();
            Professor selectedProf = findProfessorByEmail(profEmail);
            if (selectedProf != null) {
                selectedCourse.assignProfessor(selectedProf);
                selectedProf.addCourse(selectedCourse);
                System.out.println("Professor successfully assigned to the course.");
            } else {
                System.out.println("Professor not found.");
            }
        } else {
            System.out.println("Course not found.");
        }
    }

    // Helper methods
    private Course findCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCode().equalsIgnoreCase(code)) {
                return course;
            }
        }
        return null;
    }

    private Professor findProfessorByEmail(String email) {
        for (User user : users) {
            if (user instanceof Professor && user.getEmail().equalsIgnoreCase(email)) {
                return (Professor) user;
            }
        }
        return null;
    }

    private void viewAllCourses() {
        System.out.println("All Courses:");
        for (Course course : courses) {
            System.out.println(course.getCode() + " - " + course.getName());
        }
    }

    private void viewAllStudents() {
        System.out.println("All Students:");
        for (User user : users) {
            if (user instanceof Student) {
                Student student = (Student) user;
                System.out.println(student.getName() + " - " + student.getEmail());
            }
        }
    }

    private void viewAllProfessors() {
        System.out.println("All Professors:");
        for (User user : users) {
            if (user instanceof Professor) {
                Professor professor = (Professor) user;
                System.out.println(professor.getName() + " - " + professor.getEmail());
            }
        }
    }

    private void updateStudentInformation() {
        viewAllStudents();
        System.out.print("Enter the email of the student to update: ");
        String email = scanner.nextLine();
        Student student = findStudentByEmail(email);
        if (student != null) {
            System.out.print("Enter new name (or press enter to skip): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                student.setName(newName);
            }
            System.out.print("Enter new email (or press enter to skip): ");
            String newEmail = scanner.nextLine();
            if (!newEmail.isEmpty()) {
                student.setEmail(newEmail);
            }
            System.out.println("Student information updated successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private Student findStudentByEmail(String email) {
        for (User user : users) {
            if (user instanceof Student && user.getEmail().equalsIgnoreCase(email)) {
                return (Student) user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        CourseRegistrationSystem system = new CourseRegistrationSystem();
        system.start();
    }
}