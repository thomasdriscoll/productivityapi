package com.thomasdriscoll.productivityapi.repository;

import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<TaskDao, Long> {
}
