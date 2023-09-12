package com.neshan.resturantrest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Geometry;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "mashhad_areas")
public class MashhadArea {
    @Id
    @Column(name = "id")
    Integer id;

    @Column(columnDefinition = "geography")
    Geometry geom;

    @Column(name = "osm")
    Integer osm;

    @Column(name = "area")
    Integer area;
}
