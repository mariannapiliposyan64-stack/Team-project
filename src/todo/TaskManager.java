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
            return true;
        }
        return false;
    }

    public boolean deleteTask(String id) {
        return tasks.removeIf(task -> task.getId().equals(id));
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Optional<Task> getTaskById(String id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst();
    }
}