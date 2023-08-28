package com.neshan.resturantrest.model;

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
