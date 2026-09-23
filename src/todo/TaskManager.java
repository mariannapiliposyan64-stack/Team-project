package todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = FileManager.loadTasks();
    }
    
    public void saveAll() {
        FileManager.saveTasks(this.tasks);
    }

    public Task addTask(String title, String description, TaskPriority priority) {
        Task newTask = new Task(UUID.randomUUID().toString(), title, description, priority);
        tasks.add(newTask);
        saveAll();
        return newTask;
    }

    public boolean updateTask(String id, String title, String description, TaskPriority priority, TaskStatus status) {
        Optional<Task> taskOpt = getTaskById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            if (title != null) task.setTitle(title);
            if (description != null) task.setDescription(description);
            if (priority != null) task.setPriority(priority);
            if (status != null) task.setStatus(status);
            saveAll();
            return true;
        }
        return false;
    }

    public boolean deleteTask(String id) {
        boolean removed = tasks.removeIf(task -> task.getId().equals(id));
        if (removed) {
            saveAll();
        }
        return removed;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Optional<Task> getTaskById(String id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst();
    }

    public List<Task> searchTasks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllTasks();
        }
        String lowerQuery = query.toLowerCase().trim();
        return tasks.stream()
                .filter(task -> (task.getTitle() != null && task.getTitle().toLowerCase().contains(lowerQuery)) ||
                                (task.getDescription() != null && task.getDescription().toLowerCase().contains(lowerQuery)))
                .collect(Collectors.toList());
    }

    public List<Task> filterTasks(TaskStatus status, TaskPriority priority) {
        return tasks.stream()
                .filter(task -> (status == null || task.getStatus() == status))
                .filter(task -> (priority == null || task.getPriority() == priority))
                .collect(Collectors.toList());
    }

    public List<Task> searchAndFilterTasks(String query, TaskStatus status, TaskPriority priority) {
        String lowerQuery = (query != null) ? query.toLowerCase().trim() : "";
        return tasks.stream()
                .filter(task -> lowerQuery.isEmpty() ||
                        (task.getTitle() != null && task.getTitle().toLowerCase().contains(lowerQuery)) ||
                        (task.getDescription() != null && task.getDescription().toLowerCase().contains(lowerQuery)))
                .filter(task -> (status == null || task.getStatus() == status))
                .filter(task -> (priority == null || task.getPriority() == priority))
                .collect(Collectors.toList());
    }
}
