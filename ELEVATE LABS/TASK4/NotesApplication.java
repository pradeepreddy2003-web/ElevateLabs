import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Note {
    private String noteTitle;
    private String noteContent;
    private String creationDate;
    private String lastModified;
    
    public Note() {
        this.noteTitle = "";
        this.noteContent = "";
        this.creationDate = getCurrentDateTime();
        this.lastModified = getCurrentDateTime();
    }
    
    public Note(String title, String content) {
        this.noteTitle = title;
        this.noteContent = content;
        this.creationDate = getCurrentDateTime();
        this.lastModified = getCurrentDateTime();
    }
    
    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
    
    public void updateLastModified() {
        this.lastModified = getCurrentDateTime();
    }
    
    public void displayNote() {
        System.out.println("Title: " + noteTitle);
        System.out.println("Content: " + noteContent);
        System.out.println("Created: " + creationDate);
        System.out.println("Modified: " + lastModified);
        System.out.println("----------------------------------------");
    }
    
    public String toFileFormat() {
        return noteTitle + "|" + noteContent + "|" + creationDate + "|" + lastModified + "\n";
    }
    
    public static Note fromFileFormat(String fileLine) {
        String[] parts = fileLine.split("\\|", 4);
        if (parts.length == 4) {
            Note note = new Note();
            note.noteTitle = parts[0];
            note.noteContent = parts[1];
            note.creationDate = parts[2];
            note.lastModified = parts[3];
            return note;
        }
        return null;
    }
    
    public String getNoteTitle() {
        return noteTitle;
    }
    
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
        updateLastModified();
    }
    
    public String getNoteContent() {
        return noteContent;
    }
    
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
        updateLastModified();
    }
    
    public String getCreationDate() {
        return creationDate;
    }
    
    public String getLastModified() {
        return lastModified;
    }
}

class FileManager {
    private static final String NOTES_FILE = "notes_data.txt";
    private static final String BACKUP_FILE = "notes_backup.txt";
    
    public static void saveNotesToFile(ArrayList<Note> notesList) throws IOException {
        try (FileWriter writer = new FileWriter(NOTES_FILE, false)) {
            for (Note note : notesList) {
                writer.write(note.toFileFormat());
            }
            System.out.println("Notes saved successfully to " + NOTES_FILE);
        } catch (IOException e) {
            System.err.println("Error saving notes: " + e.getMessage());
            logException(e);
            throw e;
        }
    }
    
    public static ArrayList<Note> loadNotesFromFile() throws IOException {
        ArrayList<Note> loadedNotes = new ArrayList<>();
        File notesFile = new File(NOTES_FILE);
        
        if (!notesFile.exists()) {
            System.out.println("No existing notes file found. Starting with empty notes.");
            return loadedNotes;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(NOTES_FILE))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.trim().isEmpty()) {
                    Note note = Note.fromFileFormat(currentLine);
                    if (note != null) {
                        loadedNotes.add(note);
                    }
                }
            }
            System.out.println("Loaded " + loadedNotes.size() + " notes from file.");
        } catch (IOException e) {
            System.err.println("Error loading notes: " + e.getMessage());
            logException(e);
            throw e;
        }
        
        return loadedNotes;
    }
    
    public static void appendNoteToFile(Note note) throws IOException {
        try (FileWriter writer = new FileWriter(NOTES_FILE, true)) {
            writer.write(note.toFileFormat());
            System.out.println("Note appended to file successfully.");
        } catch (IOException e) {
            System.err.println("Error appending note: " + e.getMessage());
            logException(e);
            throw e;
        }
    }
    
    public static void createBackup(ArrayList<Note> notesList) {
        try (FileWriter backupWriter = new FileWriter(BACKUP_FILE, false)) {
            for (Note note : notesList) {
                backupWriter.write(note.toFileFormat());
            }
            System.out.println("Backup created successfully.");
        } catch (IOException e) {
            System.err.println("Failed to create backup: " + e.getMessage());
            logException(e);
        }
    }
    
    public static void exportNotesToText(ArrayList<Note> notesList, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("=== EXPORTED NOTES ===\n");
            writer.write("Export Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n\n");
            
            for (int i = 0; i < notesList.size(); i++) {
                Note note = notesList.get(i);
                writer.write("Note " + (i + 1) + ":\n");
                writer.write("Title: " + note.getNoteTitle() + "\n");
                writer.write("Content: " + note.getNoteContent() + "\n");
                writer.write("Created: " + note.getCreationDate() + "\n");
                writer.write("Modified: " + note.getLastModified() + "\n");
                writer.write("----------------------------------------\n");
            }
            
            System.out.println("Notes exported to " + fileName);
        } catch (IOException e) {
            System.err.println("Error exporting notes: " + e.getMessage());
            logException(e);
            throw e;
        }
    }
    
    public static void logException(Exception e) {
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("error_log.txt", true))) {
            logWriter.println("Exception occurred at: " + LocalDateTime.now());
            logWriter.println("Exception type: " + e.getClass().getSimpleName());
            logWriter.println("Message: " + e.getMessage());
            logWriter.println("Stack trace:");
            e.printStackTrace(logWriter);
            logWriter.println("----------------------------------------");
        } catch (IOException logError) {
            System.err.println("Failed to log exception: " + logError.getMessage());
        }
    }
    
    public static boolean fileExists(String fileName) {
        return new File(fileName).exists();
    }
    
    public static long getFileSize(String fileName) {
        File file = new File(fileName);
        return file.exists() ? file.length() : 0;
    }
}

public class NotesApplication {
    private static ArrayList<Note> notesCollection = new ArrayList<>();
    private static Scanner userInput = new Scanner(System.in);
    
    public static void displayMainMenu() {
        System.out.println("\n=== NOTES MANAGER APPLICATION ===");
        System.out.println("1. Create New Note");
        System.out.println("2. View All Notes");
        System.out.println("3. Edit Existing Note");
        System.out.println("4. Delete Note");
        System.out.println("5. Search Notes");
        System.out.println("6. Save Notes to File");
        System.out.println("7. Load Notes from File");
        System.out.println("8. Export Notes");
        System.out.println("9. Create Backup");
        System.out.println("10. File Information");
        System.out.println("11. Exit Application");
        System.out.print("Choose option (1-11): ");
    }
    
    public static void createNewNote() {
        System.out.println("\n--- CREATE NEW NOTE ---");
        
        System.out.print("Enter note title: ");
        String title = userInput.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty!");
            return;
        }
        
        System.out.print("Enter note content: ");
        String content = userInput.nextLine().trim();
        
        if (content.isEmpty()) {
            System.out.println("Content cannot be empty!");
            return;
        }
        
        Note newNote = new Note(title, content);
        notesCollection.add(newNote);
        
        System.out.println("Note created successfully!");
        System.out.println("Title: " + title);
        
        System.out.print("Save to file immediately? (y/n): ");
        String saveChoice = userInput.nextLine().toLowerCase();
        
        if (saveChoice.equals("y") || saveChoice.equals("yes")) {
            try {
                FileManager.appendNoteToFile(newNote);
            } catch (IOException e) {
                System.err.println("Failed to save note to file immediately.");
                handleIOException(e);
            }
        }
    }
    
    public static void viewAllNotes() {
        System.out.println("\n--- ALL NOTES ---");
        
        if (notesCollection.isEmpty()) {
            System.out.println("No notes available. Create some notes first!");
            return;
        }
        
        System.out.println("Total Notes: " + notesCollection.size());
        System.out.println();
        
        for (int i = 0; i < notesCollection.size(); i++) {
            System.out.println("Note " + (i + 1) + ":");
            notesCollection.get(i).displayNote();
        }
    }
    
    public static void editExistingNote() {
        System.out.println("\n--- EDIT NOTE ---");
        
        if (notesCollection.isEmpty()) {
            System.out.println("No notes available to edit!");
            return;
        }
        
        displayNoteTitles();
        
        System.out.print("Enter note number to edit (1-" + notesCollection.size() + "): ");
        int noteIndex = getValidNoteIndex();
        
        if (noteIndex == -1) return;
        
        Note targetNote = notesCollection.get(noteIndex);
        
        System.out.println("\nCurrent Note:");
        targetNote.displayNote();
        
        System.out.println("What would you like to edit?");
        System.out.println("1. Title only");
        System.out.println("2. Content only");
        System.out.println("3. Both title and content");
        System.out.print("Choose option: ");
        
        int editChoice = getValidIntegerInput();
        
        switch (editChoice) {
            case 1:
                System.out.print("Enter new title: ");
                String newTitle = userInput.nextLine().trim();
                if (!newTitle.isEmpty()) {
                    targetNote.setNoteTitle(newTitle);
                    System.out.println("Title updated successfully!");
                }
                break;
            case 2:
                System.out.print("Enter new content: ");
                String newContent = userInput.nextLine().trim();
                if (!newContent.isEmpty()) {
                    targetNote.setNoteContent(newContent);
                    System.out.println("Content updated successfully!");
                }
                break;
            case 3:
                System.out.print("Enter new title: ");
                String updatedTitle = userInput.nextLine().trim();
                System.out.print("Enter new content: ");
                String updatedContent = userInput.nextLine().trim();
                
                if (!updatedTitle.isEmpty() && !updatedContent.isEmpty()) {
                    targetNote.setNoteTitle(updatedTitle);
                    targetNote.setNoteContent(updatedContent);
                    System.out.println("Note updated successfully!");
                }
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        System.out.println("\nUpdated Note:");
        targetNote.displayNote();
    }
    
    public static void deleteNote() {
        System.out.println("\n--- DELETE NOTE ---");
        
        if (notesCollection.isEmpty()) {
            System.out.println("No notes available to delete!");
            return;
        }
        
        displayNoteTitles();
        
        System.out.print("Enter note number to delete (1-" + notesCollection.size() + "): ");
        int noteIndex = getValidNoteIndex();
        
        if (noteIndex == -1) return;
        
        Note noteToDelete = notesCollection.get(noteIndex);
        
        System.out.println("\nNote to be deleted:");
        noteToDelete.displayNote();
        
        System.out.print("Are you sure you want to delete this note? (y/n): ");
        String confirmation = userInput.nextLine().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            notesCollection.remove(noteIndex);
            System.out.println("Note deleted successfully!");
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    public static void searchNotes() {
        System.out.println("\n--- SEARCH NOTES ---");
        
        if (notesCollection.isEmpty()) {
            System.out.println("No notes available to search!");
            return;
        }
        
        System.out.print("Enter search term (title or content): ");
        String searchTerm = userInput.nextLine().toLowerCase().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Search term cannot be empty!");
            return;
        }
        
        ArrayList<Note> searchResults = new ArrayList<>();
        
        for (Note note : notesCollection) {
            if (note.getNoteTitle().toLowerCase().contains(searchTerm) ||
                note.getNoteContent().toLowerCase().contains(searchTerm)) {
                searchResults.add(note);
            }
        }
        
        if (searchResults.isEmpty()) {
            System.out.println("No notes found containing: " + searchTerm);
        } else {
            System.out.println("Found " + searchResults.size() + " note(s):");
            System.out.println();
            
            for (int i = 0; i < searchResults.size(); i++) {
                System.out.println("Result " + (i + 1) + ":");
                searchResults.get(i).displayNote();
            }
        }
    }
    
    public static void saveNotesToFile() {
        System.out.println("\n--- SAVE NOTES ---");
        
        if (notesCollection.isEmpty()) {
            System.out.println("No notes to save!");
            return;
        }
        
        try {
            FileManager.saveNotesToFile(notesCollection);
            System.out.println("All notes have been saved to file successfully!");
        } catch (IOException e) {
            System.err.println("Failed to save notes to file!");
            handleIOException(e);
        }
    }
    
    public static void loadNotesFromFile() {
        System.out.println("\n--- LOAD NOTES ---");
        
        if (!notesCollection.isEmpty()) {
            System.out.print("Current notes will be replaced. Continue? (y/n): ");
            String confirmation = userInput.nextLine().toLowerCase();
            
            if (!confirmation.equals("y") && !confirmation.equals("yes")) {
                System.out.println("Load operation cancelled.");
                return;
            }
        }
        
        try {
            ArrayList<Note> loadedNotes = FileManager.loadNotesFromFile();
            notesCollection = loadedNotes;
            System.out.println("Notes loaded successfully!");
            System.out.println("Total notes loaded: " + notesCollection.size());
        } catch (IOException e) {
            System.err.println("Failed to load notes from file!");
            handleIOException(e);
        }
    }
    
    public static void exportNotes() {
        System.out.println("\n--- EXPORT NOTES ---");
        
        if (notesCollection.isEmpty()) {
            System.out.println("No notes to export!");
            return;
        }
        
        System.out.print("Enter export file name (without extension): ");
        String fileName = userInput.nextLine().trim();
        
        if (fileName.isEmpty()) {
            fileName = "exported_notes";
        }
        
        fileName += ".txt";
        
        try {
            FileManager.exportNotesToText(notesCollection, fileName);
        } catch (IOException e) {
            System.err.println("Failed to export notes!");
            handleIOException(e);
        }
    }
    
    public static void createBackup() {
        System.out.println("\n--- CREATE BACKUP ---");
        
        if (notesCollection.isEmpty()) {
            System.out.println("No notes to backup!");
            return;
        }
        
        FileManager.createBackup(notesCollection);
    }
    
    public static void displayFileInformation() {
        System.out.println("\n--- FILE INFORMATION ---");
        
        String[] fileNames = {"notes_data.txt", "notes_backup.txt", "error_log.txt"};
        
        for (String fileName : fileNames) {
            if (FileManager.fileExists(fileName)) {
                long fileSize = FileManager.getFileSize(fileName);
                System.out.println(fileName + ": EXISTS (" + fileSize + " bytes)");
            } else {
                System.out.println(fileName + ": NOT FOUND");
            }
        }
    }
    
    public static void displayNoteTitles() {
        System.out.println("\nAvailable Notes:");
        for (int i = 0; i < notesCollection.size(); i++) {
            System.out.println((i + 1) + ". " + notesCollection.get(i).getNoteTitle());
        }
    }
    
    public static int getValidNoteIndex() {
        int choice = getValidIntegerInput();
        
        if (choice < 1 || choice > notesCollection.size()) {
            System.out.println("Invalid note number!");
            return -1;
        }
        
        return choice - 1;
    }
    
    public static int getValidIntegerInput() {
        while (!userInput.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            System.out.print("Try again: ");
            userInput.next();
        }
        int value = userInput.nextInt();
        userInput.nextLine();
        return value;
    }
    
    public static void handleIOException(IOException e) {
        System.err.println("I/O Error Details:");
        System.err.println("Error Type: " + e.getClass().getSimpleName());
        System.err.println("Message: " + e.getMessage());
        
        System.out.print("View full stack trace? (y/n): ");
        String showTrace = userInput.nextLine().toLowerCase();
        
        if (showTrace.equals("y") || showTrace.equals("yes")) {
            System.err.println("Full Stack Trace:");
            e.printStackTrace();
        }
        
        FileManager.logException(e);
        System.out.println("Error has been logged to error_log.txt");
    }
    
    public static void initializeApplication() {
        System.out.println("Initializing Notes Application...");
        
        try {
            if (FileManager.fileExists("notes_data.txt")) {
                System.out.println("Found existing notes file. Loading automatically...");
                notesCollection = FileManager.loadNotesFromFile();
            } else {
                System.out.println("No existing notes found. Starting fresh.");
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load existing notes.");
            handleIOException(e);
        }
        
        System.out.println("Application ready!");
    }
    
    public static void shutdownApplication() {
        System.out.println("\nShutting down application...");
        
        if (!notesCollection.isEmpty()) {
            System.out.print("Save current notes before exit? (y/n): ");
            String saveChoice = userInput.nextLine().toLowerCase();
            
            if (saveChoice.equals("y") || saveChoice.equals("yes")) {
                try {
                    FileManager.saveNotesToFile(notesCollection);
                    System.out.println("Notes saved successfully!");
                } catch (IOException e) {
                    System.err.println("Warning: Could not save notes!");
                    handleIOException(e);
                }
            }
        }
        
        System.out.println("Thank you for using Notes Application!");
        System.out.println("Goodbye!");
    }
    
    public static void main(String[] args) {
        System.out.println("Welcome to Notes Application!");
        System.out.println("Manage your text notes with file persistence.");
        
        initializeApplication();
        
        boolean applicationRunning = true;
        
        while (applicationRunning) {
            try {
                displayMainMenu();
                
                int userChoice = getValidIntegerInput();
                
                switch (userChoice) {
                    case 1:
                        createNewNote();
                        break;
                    case 2:
                        viewAllNotes();
                        break;
                    case 3:
                        editExistingNote();
                        break;
                    case 4:
                        deleteNote();
                        break;
                    case 5:
                        searchNotes();
                        break;
                    case 6:
                        saveNotesToFile();
                        break;
                    case 7:
                        loadNotesFromFile();
                        break;
                    case 8:
                        exportNotes();
                        break;
                    case 9:
                        createBackup();
                        break;
                    case 10:
                        displayFileInformation();
                        break;
                    case 11:
                        shutdownApplication();
                        applicationRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice! Please select 1-11.");
                }
                
            } catch (Exception e) {
                System.err.println("Unexpected error occurred: " + e.getMessage());
                FileManager.logException(e);
                System.out.println("Application will continue running...");
            }
        }
        
        userInput.close();
    }
}