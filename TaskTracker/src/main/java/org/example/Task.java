package org.example;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String desc;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(int id , String desc){
        this.id = id;
        this.desc = desc;
        this.status = "todo";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
