import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

abstract class Person {
    protected String personId;
    protected String personName;
    protected String contactNumber;
    
    public Person() {
        this.personId = "";
        this.personName = "";
        this.contactNumber = "";
    }
    
    public Person(String id, String name, String contact) {
        this.personId = id;
        this.personName = name;
        this.contactNumber = contact;
    }
    
    public abstract void displayInfo();
    
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}

interface LibraryOperations {
    void borrowBook(String bookId);
    void returnBook(String bookId);
    void viewBorrowedBooks();
}

class Book {
    private String bookId;
    private String bookTitle;
    private String authorName;
    private String category;
    private boolean availabilityStatus;
    private String borrowedBy;
    private Date issueDate;
    
    public Book() {
        this.bookId = "";
        this.bookTitle = "";
        this.authorName = "";
        this.category = "";
        this.availabilityStatus = true;
        this.borrowedBy = "";
        this.issueDate = null;
    }
    
    public Book(String id, String title, String author, String category) {
        this.bookId = id;
        this.bookTitle = title;
        this.authorName = author;
        this.category = category;
        this.availabilityStatus = true;
        this.borrowedBy = "";
        this.issueDate = null;
    }
    
    public void displayBookDetails() {
        System.out.println("Book ID: " + bookId);
        System.out.println("Title: " + bookTitle);
        System.out.println("Author: " + authorName);
        System.out.println("Category: " + category);
        System.out.println("Status: " + (availabilityStatus ? "Available" : "Issued"));
        if (!availabilityStatus) {
            System.out.println("Borrowed by: " + borrowedBy);
        }
        System.out.println("------------------------");
    }
    
    public String getBookId() {
        return bookId;
    }
    
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }
    
    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
    
    public String getBorrowedBy() {
        return borrowedBy;
    }
    
    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }
    
    public Date getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
}

class User extends Person implements LibraryOperations {
    private ArrayList<String> borrowedBooksList;
    private int maxBooksAllowed;
    
    public User() {
        super();
        this.borrowedBooksList = new ArrayList<>();
        this.maxBooksAllowed = 3;
    }
    
    public User(String id, String name, String contact) {
        super(id, name, contact);
        this.borrowedBooksList = new ArrayList<>();
        this.maxBooksAllowed = 3;
    }
    
    @Override
    public void displayInfo() {
        System.out.println("User ID: " + personId);
        System.out.println("Name: " + personName);
        System.out.println("Contact: " + contactNumber);
        System.out.println("Books Borrowed: " + borrowedBooksList.size() + "/" + maxBooksAllowed);
    }
    
    @Override
    public void borrowBook(String bookId) {
        if (borrowedBooksList.size() >= maxBooksAllowed) {
            System.out.println("Cannot borrow more books. Limit reached!");
            return;
        }
        borrowedBooksList.add(bookId);
    }
    
    @Override
    public void returnBook(String bookId) {
        borrowedBooksList.remove(bookId);
    }
    
    @Override
    public void viewBorrowedBooks() {
        if (borrowedBooksList.isEmpty()) {
            System.out.println("No books currently borrowed.");
            return;
        }
        System.out.println("Borrowed Books:");
        for (String bookId : borrowedBooksList) {
            System.out.println("- Book ID: " + bookId);
        }
    }
    
    public ArrayList<String> getBorrowedBooksList() {
        return borrowedBooksList;
    }
    
    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }
}

class Librarian extends Person {
    private String employeeId;
    private String workShift;
    
    public Librarian() {
        super();
        this.employeeId = "";
        this.workShift = "";
    }
    
    public Librarian(String id, String name, String contact, String empId, String shift) {
        super(id, name, contact);
        this.employeeId = empId;
        this.workShift = shift;
    }
    
    @Override
    public void displayInfo() {
        System.out.println("Librarian ID: " + personId);
        System.out.println("Name: " + personName);
        System.out.println("Contact: " + contactNumber);
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Work Shift: " + workShift);
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getWorkShift() {
        return workShift;
    }
    
    public void setWorkShift(String workShift) {
        this.workShift = workShift;
    }
}

class Library {
    private ArrayList<Book> bookCollection;
    private ArrayList<User> registeredUsers;
    private ArrayList<Librarian> libraryStaff;
    private String libraryName;
    
    public Library(String name) {
        this.libraryName = name;
        this.bookCollection = new ArrayList<>();
        this.registeredUsers = new ArrayList<>();
        this.libraryStaff = new ArrayList<>();
    }
    
    public void addNewBook(Book book) {
        if (findBookById(book.getBookId()) != null) {
            System.out.println("Book with ID " + book.getBookId() + " already exists!");
            return;
        }
        bookCollection.add(book);
        System.out.println("Book added successfully: " + book.getBookTitle());
    }
    
    public void registerUser(User user) {
        if (findUserById(user.getPersonId()) != null) {
            System.out.println("User with ID " + user.getPersonId() + " already exists!");
            return;
        }
        registeredUsers.add(user);
        System.out.println("User registered successfully: " + user.getPersonName());
    }
    
    public void addLibrarian(Librarian librarian) {
        libraryStaff.add(librarian);
        System.out.println("Librarian added successfully: " + librarian.getPersonName());
    }
    
    public boolean issueBook(String bookId, String userId) {
        Book targetBook = findBookById(bookId);
        User targetUser = findUserById(userId);
        
        if (targetBook == null) {
            System.out.println("Book not found with ID: " + bookId);
            return false;
        }
        
        if (targetUser == null) {
            System.out.println("User not found with ID: " + userId);
            return false;
        }
        
        if (!targetBook.isAvailabilityStatus()) {
            System.out.println("Book is already issued to someone else!");
            return false;
        }
        
        if (targetUser.getBorrowedBooksList().size() >= targetUser.getMaxBooksAllowed()) {
            System.out.println("User has reached maximum book limit!");
            return false;
        }
        
        targetBook.setAvailabilityStatus(false);
        targetBook.setBorrowedBy(userId);
        targetBook.setIssueDate(new Date());
        targetUser.borrowBook(bookId);
        
        System.out.println("Book issued successfully!");
        System.out.println("Book: " + targetBook.getBookTitle());
        System.out.println("Issued to: " + targetUser.getPersonName());
        return true;
    }
    
    public boolean returnBook(String bookId, String userId) {
        Book targetBook = findBookById(bookId);
        User targetUser = findUserById(userId);
        
        if (targetBook == null) {
            System.out.println("Book not found with ID: " + bookId);
            return false;
        }
        
        if (targetUser == null) {
            System.out.println("User not found with ID: " + userId);
            return false;
        }
        
        if (targetBook.isAvailabilityStatus()) {
            System.out.println("Book is not currently issued!");
            return false;
        }
        
        if (!targetBook.getBorrowedBy().equals(userId)) {
            System.out.println("This book was not issued to this user!");
            return false;
        }
        
        targetBook.setAvailabilityStatus(true);
        targetBook.setBorrowedBy("");
        targetBook.setIssueDate(null);
        targetUser.returnBook(bookId);
        
        System.out.println("Book returned successfully!");
        System.out.println("Book: " + targetBook.getBookTitle());
        System.out.println("Returned by: " + targetUser.getPersonName());
        return true;
    }
    
    public void displayAllBooks() {
        if (bookCollection.isEmpty()) {
            System.out.println("No books available in library.");
            return;
        }
        
        System.out.println("\n=== ALL BOOKS IN LIBRARY ===");
        for (Book book : bookCollection) {
            book.displayBookDetails();
        }
    }
    
    public void displayAvailableBooks() {
        System.out.println("\n=== AVAILABLE BOOKS ===");
        boolean hasAvailable = false;
        
        for (Book book : bookCollection) {
            if (book.isAvailabilityStatus()) {
                book.displayBookDetails();
                hasAvailable = true;
            }
        }
        
        if (!hasAvailable) {
            System.out.println("No books currently available.");
        }
    }
    
    public void displayAllUsers() {
        if (registeredUsers.isEmpty()) {
            System.out.println("No registered users.");
            return;
        }
        
        System.out.println("\n=== REGISTERED USERS ===");
        for (User user : registeredUsers) {
            user.displayInfo();
            System.out.println("------------------------");
        }
    }
    
    public Book findBookById(String bookId) {
        for (Book book : bookCollection) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }
    
    public User findUserById(String userId) {
        for (User user : registeredUsers) {
            if (user.getPersonId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    public void searchBooks(String searchTerm) {
        System.out.println("\n=== SEARCH RESULTS ===");
        boolean found = false;
        
        for (Book book : bookCollection) {
            if (book.getBookTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                book.getAuthorName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                book.getCategory().toLowerCase().contains(searchTerm.toLowerCase())) {
                book.displayBookDetails();
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No books found matching: " + searchTerm);
        }
    }
    
    public String getLibraryName() {
        return libraryName;
    }
}

public class LibraryManagementSystem {
    private static Library mainLibrary = new Library("City Central Library");
    private static Scanner inputScanner = new Scanner(System.in);
    
    public static void displayMainMenu() {
        System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
        System.out.println("Library: " + mainLibrary.getLibraryName());
        System.out.println("1. Add New Book");
        System.out.println("2. Register New User");
        System.out.println("3. Issue Book");
        System.out.println("4. Return Book");
        System.out.println("5. View All Books");
        System.out.println("6. View Available Books");
        System.out.println("7. View All Users");
        System.out.println("8. Search Books");
        System.out.println("9. User Book History");
        System.out.println("10. Exit System");
        System.out.print("Select option (1-10): ");
    }
    
    public static void addNewBook() {
        System.out.println("\n--- ADD NEW BOOK ---");
        
        System.out.print("Enter Book ID: ");
        String bookId = inputScanner.nextLine();
        
        System.out.print("Enter Book Title: ");
        String title = inputScanner.nextLine();
        
        System.out.print("Enter Author Name: ");
        String author = inputScanner.nextLine();
        
        System.out.print("Enter Category: ");
        String category = inputScanner.nextLine();
        
        Book newBook = new Book(bookId, title, author, category);
        mainLibrary.addNewBook(newBook);
    }
    
    public static void registerNewUser() {
        System.out.println("\n--- REGISTER NEW USER ---");
        
        System.out.print("Enter User ID: ");
        String userId = inputScanner.nextLine();
        
        System.out.print("Enter User Name: ");
        String userName = inputScanner.nextLine();
        
        System.out.print("Enter Contact Number: ");
        String contact = inputScanner.nextLine();
        
        User newUser = new User(userId, userName, contact);
        mainLibrary.registerUser(newUser);
    }
    
    public static void issueBook() {
        System.out.println("\n--- ISSUE BOOK ---");
        
        System.out.print("Enter Book ID: ");
        String bookId = inputScanner.nextLine();
        
        System.out.print("Enter User ID: ");
        String userId = inputScanner.nextLine();
        
        mainLibrary.issueBook(bookId, userId);
    }
    
    public static void returnBook() {
        System.out.println("\n--- RETURN BOOK ---");
        
        System.out.print("Enter Book ID: ");
        String bookId = inputScanner.nextLine();
        
        System.out.print("Enter User ID: ");
        String userId = inputScanner.nextLine();
        
        mainLibrary.returnBook(bookId, userId);
    }
    
    public static void searchBooks() {
        System.out.println("\n--- SEARCH BOOKS ---");
        
        System.out.print("Enter search term (title/author/category): ");
        String searchTerm = inputScanner.nextLine();
        
        mainLibrary.searchBooks(searchTerm);
    }
    
    public static void viewUserBookHistory() {
        System.out.println("\n--- USER BOOK HISTORY ---");
        
        System.out.print("Enter User ID: ");
        String userId = inputScanner.nextLine();
        
        User targetUser = mainLibrary.findUserById(userId);
        
        if (targetUser == null) {
            System.out.println("User not found!");
            return;
        }
        
        System.out.println("\nUser Information:");
        targetUser.displayInfo();
        System.out.println();
        targetUser.viewBorrowedBooks();
    }
    
    public static int getValidChoice() {
        while (!inputScanner.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            System.out.print("Try again: ");
            inputScanner.next();
        }
        int choice = inputScanner.nextInt();
        inputScanner.nextLine();
        return choice;
    }
    
    public static void initializeSampleData() {
        Book book1 = new Book("B001", "Java Programming", "James Gosling", "Programming");
        Book book2 = new Book("B002", "Data Structures", "Robert Sedgewick", "Computer Science");
        Book book3 = new Book("B003", "Clean Code", "Robert Martin", "Software Engineering");
        
        mainLibrary.addNewBook(book1);
        mainLibrary.addNewBook(book2);
        mainLibrary.addNewBook(book3);
        
        User user1 = new User("U001", "Alice Johnson", "9876543210");
        User user2 = new User("U002", "Bob Smith", "9876543211");
        
        mainLibrary.registerUser(user1);
        mainLibrary.registerUser(user2);
    }
    
    public static void main(String[] args) {
        System.out.println("Welcome to Library Management System!");
        System.out.println("Initializing system with sample data...");
        
        initializeSampleData();
        
        boolean systemRunning = true;
        
        while (systemRunning) {
            displayMainMenu();
            
            int userChoice = getValidChoice();
            
            switch (userChoice) {
                case 1:
                    addNewBook();
                    break;
                case 2:
                    registerNewUser();
                    break;
                case 3:
                    issueBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    mainLibrary.displayAllBooks();
                    break;
                case 6:
                    mainLibrary.displayAvailableBooks();
                    break;
                case 7:
                    mainLibrary.displayAllUsers();
                    break;
                case 8:
                    searchBooks();
                    break;
                case 9:
                    viewUserBookHistory();
                    break;
                case 10:
                    System.out.println("\nThank you for using Library Management System!");
                    System.out.println("System closing...");
                    systemRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1-10.");
            }
        }
        
        inputScanner.close();
    }
}