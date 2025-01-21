package com.example.tidytime;

public class Task {
    private int id;
    private String taskName;
    private String taskDescription;
    private String taskDate;
    private String taskTime;
    private String additionalEvent;

    public Task(int id, String taskName, String taskDescription, String taskDate, String taskTime, String additionalEvent) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.additionalEvent = additionalEvent;
    }

    public int getId() { return id; }
    public String getTaskName() { return taskName; }
    public String getTaskDescription() { return taskDescription; }
    public String getTaskDate() { return taskDate; }
    public String getTaskTime() { return taskTime; }
    public String getAdditionalEvent() { return additionalEvent; }
}
