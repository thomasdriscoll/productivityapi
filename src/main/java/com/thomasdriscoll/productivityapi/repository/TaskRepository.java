package com.thomasdriscoll.productivityapi.repository;

import com.thomasdriscoll.productivityapi.lib.enums.StatusType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<TaskDao, Long> {
    Optional<TaskDao> findByUserIdAndTaskId(String userId, Long taskId);
    List<TaskDao> findByUserIdAndStatusType(String userId, StatusType statusType);
}
