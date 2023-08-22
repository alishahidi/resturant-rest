package com.neshan.resturantrest.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Time {
    private String className;
    private String methodName;
    private Long totalTime;

}
