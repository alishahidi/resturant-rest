package com.neshan.resturantrest.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "times")

public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String className;
    private String methodName;
    private Long totalTime;

    @CreationTimestamp
    Date createdAt;
}
