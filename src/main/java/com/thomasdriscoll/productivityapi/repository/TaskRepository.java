package com.thomasdriscoll.productivityapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<TaskDao, Long> {
    Optional<TaskDao> findByUserIdAndTaskId(String userId, Long taskId);
}
