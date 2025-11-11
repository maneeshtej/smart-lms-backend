package com.lms.repositories;

import com.lms.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherUsername(String teacherUsername);
}
