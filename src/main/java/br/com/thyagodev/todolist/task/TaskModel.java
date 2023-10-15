package br.com.thyagodev.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    private UUID userId;

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String description;
    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception{
        if (title.length() > 50) {
            throw new Exception("title prop out of bounds");
        }
        this.title = title;
    }


}
