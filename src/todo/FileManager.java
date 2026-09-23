package todo;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "tasks.csv";
    private static final String SEPARATOR = ";;;"; 

    public static void saveTasks(List<Task> tasks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task t : tasks) {
                String desc = t.getDescription() != null ? t.getDescription().replace("\n", "\\n") : "";
                String line = String.join(SEPARATOR,
                        t.getId(),
                        t.getTitle(),
                        desc,
                        t.getCreationDate().toString(),
                        t.getPriority().name(),
                        t.getStatus().name()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return tasks;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(SEPARATOR, -1);
                if (parts.length >= 6) {
                    String id = parts[0];
                    String title = parts[1];
                    String desc = parts[2].replace("\\n", "\n");
                    LocalDateTime date = LocalDateTime.parse(parts[3]);
                    TaskPriority priority = TaskPriority.valueOf(parts[4]);
                    TaskStatus status = TaskStatus.valueOf(parts[5]);
                    
                    tasks.add(new Task(id, title, desc, date, priority, status));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }
}