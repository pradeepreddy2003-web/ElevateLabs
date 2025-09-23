import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private int studentId;
    private String studentName;
    private double studentMarks;
    
    public Student() {
        this.studentId = 0;
        this.studentName = "";
        this.studentMarks = 0.0;
    }
    
    public Student(int id, String name, double marks) {
        this.studentId = id;
        this.studentName = name;
        this.studentMarks = marks;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public double getStudentMarks() {
        return studentMarks;
    }
    
    public void setStudentMarks(double studentMarks) {
        this.studentMarks = studentMarks;
    }
    
    public String getGrade() {
        if (studentMarks >= 90) return "A+";
        else if (studentMarks >= 80) return "A";
        else if (studentMarks >= 70) return "B";
        else if (studentMarks >= 60) return "C";
        else if (studentMarks >= 50) return "D";
        else return "F";
    }
    
    public void displayStudentInfo() {
        System.out.println("ID: " + studentId + " | Name: " + studentName + 
                         " | Marks: " + studentMarks + " | Grade: " + getGrade());
    }
}

public class StudentRecordManager {
    private static ArrayList<Student> studentDatabase = new ArrayList<>();
    private static Scanner inputScanner = new Scanner(System.in);
    
    public static void showMainMenu() {
        System.out.println("\n=== STUDENT RECORD MANAGEMENT SYSTEM ===");
        System.out.println("1. Add New Student");
        System.out.println("2. View All Students");
        System.out.println("3. Update Student Record");
        System.out.println("4. Delete Student Record");
        System.out.println("5. Search Student by ID");
        System.out.println("6. Display Statistics");
        System.out.println("7. Exit System");
        System.out.print("Select your option (1-7): ");
    }
    
    public static void addNewStudent() {
        System.out.println("\n--- ADD NEW STUDENT ---");
        
        System.out.print("Enter Student ID: ");
        int id = getValidIntegerInput();
        
        if (findStudentById(id) != null) {
            System.out.println("Error: Student with ID " + id + " already exists!");
            return;
        }
        
        System.out.print("Enter Student Name: ");
        String name = inputScanner.nextLine();
        
        System.out.print("Enter Student Marks (0-100): ");
        double marks = getValidMarksInput();
        
        Student newStudent = new Student(id, name, marks);
        studentDatabase.add(newStudent);
        
        System.out.println("Student added successfully!");
        System.out.println("Added: " + name + " (ID: " + id + ")");
    }
    
    public static void viewAllStudents() {
        System.out.println("\n--- ALL STUDENT RECORDS ---");
        
        if (studentDatabase.isEmpty()) {
            System.out.println("No student records found in the database.");
            return;
        }
        
        System.out.println("Total Students: " + studentDatabase.size());
        System.out.println("----------------------------------------");
        
        for (Student student : studentDatabase) {
            student.displayStudentInfo();
        }
    }
    
    public static void updateStudentRecord() {
        System.out.println("\n--- UPDATE STUDENT RECORD ---");
        
        if (studentDatabase.isEmpty()) {
            System.out.println("No students available to update.");
            return;
        }
        
        System.out.print("Enter Student ID to update: ");
        int searchId = getValidIntegerInput();
        
        Student targetStudent = findStudentById(searchId);
        
        if (targetStudent == null) {
            System.out.println("Student with ID " + searchId + " not found!");
            return;
        }
        
        System.out.println("Current Record:");
        targetStudent.displayStudentInfo();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name only");
        System.out.println("2. Marks only");
        System.out.println("3. Both Name and Marks");
        System.out.print("Choose option: ");
        
        int updateChoice = getValidIntegerInput();
        
        switch (updateChoice) {
            case 1:
                System.out.print("Enter new name: ");
                String newName = inputScanner.nextLine();
                targetStudent.setStudentName(newName);
                break;
            case 2:
                System.out.print("Enter new marks (0-100): ");
                double newMarks = getValidMarksInput();
                targetStudent.setStudentMarks(newMarks);
                break;
            case 3:
                System.out.print("Enter new name: ");
                String updatedName = inputScanner.nextLine();
                System.out.print("Enter new marks (0-100): ");
                double updatedMarks = getValidMarksInput();
                targetStudent.setStudentName(updatedName);
                targetStudent.setStudentMarks(updatedMarks);
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        System.out.println("Record updated successfully!");
        System.out.println("Updated Record:");
        targetStudent.displayStudentInfo();
    }
    
    public static void deleteStudentRecord() {
        System.out.println("\n--- DELETE STUDENT RECORD ---");
        
        if (studentDatabase.isEmpty()) {
            System.out.println("No students available to delete.");
            return;
        }
        
        System.out.print("Enter Student ID to delete: ");
        int deleteId = getValidIntegerInput();
        
        Student studentToDelete = findStudentById(deleteId);
        
        if (studentToDelete == null) {
            System.out.println("Student with ID " + deleteId + " not found!");
            return;
        }
        
        System.out.println("Record to be deleted:");
        studentToDelete.displayStudentInfo();
        
        System.out.print("Are you sure you want to delete this record? (y/n): ");
        String confirmation = inputScanner.nextLine().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            studentDatabase.remove(studentToDelete);
            System.out.println("Student record deleted successfully!");
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    public static void searchStudentById() {
        System.out.println("\n--- SEARCH STUDENT ---");
        
        if (studentDatabase.isEmpty()) {
            System.out.println("No students in database to search.");
            return;
        }
        
        System.out.print("Enter Student ID to search: ");
        int searchId = getValidIntegerInput();
        
        Student foundStudent = findStudentById(searchId);
        
        if (foundStudent != null) {
            System.out.println("Student found:");
            foundStudent.displayStudentInfo();
        } else {
            System.out.println("Student with ID " + searchId + " not found!");
        }
    }
    
    public static void displayStatistics() {
        System.out.println("\n--- DATABASE STATISTICS ---");
        
        if (studentDatabase.isEmpty()) {
            System.out.println("No data available for statistics.");
            return;
        }
        
        int totalStudents = studentDatabase.size();
        double totalMarks = 0;
        double highestMarks = Double.MIN_VALUE;
        double lowestMarks = Double.MAX_VALUE;
        
        for (Student student : studentDatabase) {
            double marks = student.getStudentMarks();
            totalMarks += marks;
            
            if (marks > highestMarks) {
                highestMarks = marks;
            }
            if (marks < lowestMarks) {
                lowestMarks = marks;
            }
        }
        
        double averageMarks = totalMarks / totalStudents;
        
        System.out.println("Total Students: " + totalStudents);
        System.out.printf("Average Marks: %.2f%n", averageMarks);
        System.out.printf("Highest Marks: %.2f%n", highestMarks);
        System.out.printf("Lowest Marks: %.2f%n", lowestMarks);
    }
    
    public static Student findStudentById(int id) {
        for (Student student : studentDatabase) {
            if (student.getStudentId() == id) {
                return student;
            }
        }
        return null;
    }
    
    public static int getValidIntegerInput() {
        while (!inputScanner.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            System.out.print("Try again: ");
            inputScanner.next();
        }
        int value = inputScanner.nextInt();
        inputScanner.nextLine();
        return value;
    }
    
    public static double getValidMarksInput() {
        double marks;
        do {
            while (!inputScanner.hasNextDouble()) {
                System.out.println("Please enter a valid number!");
                System.out.print("Enter marks (0-100): ");
                inputScanner.next();
            }
            marks = inputScanner.nextDouble();
            inputScanner.nextLine();
            
            if (marks < 0 || marks > 100) {
                System.out.println("Marks must be between 0 and 100!");
                System.out.print("Enter valid marks: ");
            }
        } while (marks < 0 || marks > 100);
        
        return marks;
    }
    
    public static void main(String[] args) {
        System.out.println("Welcome to Student Record Management System!");
        System.out.println("Manage student data with ease.");
        
        boolean systemRunning = true;
        
        while (systemRunning) {
            showMainMenu();
            
            int userChoice = getValidIntegerInput();
            
            switch (userChoice) {
                case 1:
                    addNewStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    updateStudentRecord();
                    break;
                case 4:
                    deleteStudentRecord();
                    break;
                case 5:
                    searchStudentById();
                    break;
                case 6:
                    displayStatistics();
                    break;
                case 7:
                    System.out.println("\nThank you for using Student Record Management System!");
                    System.out.println("System shutting down...");
                    systemRunning = false;
                    break;
                default:
                    System.out.println("Invalid option! Please choose between 1-7.");
            }
        }
        
        inputScanner.close();
    }
}