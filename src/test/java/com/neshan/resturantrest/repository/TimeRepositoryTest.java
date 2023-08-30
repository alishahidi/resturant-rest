package com.neshan.resturantrest.repository;

import com.neshan.resturantrest.model.Time;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TimeRepositoryTest {
//
////    private SessionFactory sessionFactory;
////
////    @BeforeEach
////    void setUp() {
////        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
////        try {
////            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
////        } catch (Exception e) {
////            StandardServiceRegistryBuilder.destroy(registry);
////        }
////    }
////
////    @AfterEach
////    void tearDown() {
////        if (sessionFactory != null) {
////            sessionFactory.close();
////        }
////    }
//
//    @Autowired
//    TimeRepository timeRepository;
//
//    @Test
//    void save() {
////        try (Session session = sessionFactory.openSession()) {
////            session.beginTransaction();
////
////            Time time = Time.builder()
////                    .methodName("test")
////                    .className("test")
////                    .totalTime(40L)
////                    .build();
////
////            session.persist(time);
////
////            session.getTransaction().commit();
////        }
//
//        Time time = Time.builder()
//                    .methodName("test")
//                    .className("test")
//                    .totalTime(40L)
//                    .build();
//
//        timeRepository.save(time);
//    }
//
//    @Test
//    void getByMethodNameAndPrint(){
//        List<Time> times = timeRepository.findByMethodName("get");
//
//        System.out.println("times = " + times);
//    }
//
}