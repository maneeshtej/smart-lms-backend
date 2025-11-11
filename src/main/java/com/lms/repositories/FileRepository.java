package com.lms.repositories;

import com.lms.models.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByCourseId(Long courseId);
}
