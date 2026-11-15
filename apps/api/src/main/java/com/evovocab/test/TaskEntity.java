package com.evovocab.test;

import com.evovocab.shared.entities.IdAutoIncrement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class TaskEntity extends IdAutoIncrement {

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;
}
