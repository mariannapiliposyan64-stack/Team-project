package todo;

import java.time.LocalDateTime;

public class Task {
    private String id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private TaskPriority priority;
    private TaskStatus status;

    public Task(String id, String title, String description, TaskPriority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.priority = priority;
        this.status = TaskStatus.OPEN;
    }

    public Task(String id, String title, String description, LocalDateTime creationDate, TaskPriority priority, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.priority = priority;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}