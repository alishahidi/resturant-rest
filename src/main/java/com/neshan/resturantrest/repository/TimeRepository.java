package com.neshan.resturantrest.repository;

import com.neshan.resturantrest.model.Time;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@NamedQueries({
        @NamedQuery(name = "Time.findByClassName", query = "SELECT t FROM Time t WHERE t.className = :className"),
        @NamedQuery(name = "Time.findByMethodName", query = "SELECT t FROM Time t WHERE t.methodName = :methodName")
})
public interface TimeRepository extends JpaRepository<Time, Long>, JpaSpecificationExecutor<Time> {
    List<Time> findByMethodName(String methodName);
    @Query(name = "Time.findByClassName")
    List<Time> findByClassName(String className);
}
