package com.thomasdriscoll.productivityapi.lib.enums;

import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

class PriorityTaskValidatorImplTest {
    private static Validator validator;

    @BeforeClass
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    public void whenStringAnyOfEnum_thenShouldNotReportConstraintViolations() {
        TaskDto task = TaskDto.builder()
                .priorityTask(PriorityTask.HIGH)
                .build();
        Set<ConstraintViolation<TaskDto>> violations = validator.validate(task);
        assert(violations.isEmpty());
    }

    @Test
    public void whenStringNull_thenShouldNotReportConstraintViolations() {
        TaskDto task = TaskDto.builder()
                .priorityTask(null)
                .build();
        Set<ConstraintViolation<TaskDto>> violations = validator.validate(task);
        assert(violations.isEmpty());
    }

    @Test
    public void whenStringNotAnyOfEnum_thenShouldGiveOccurrenceOfConstraintViolations() {
        TaskDto task = TaskDto.builder()
                .priorityTask(PriorityTask.valueOf("test"))
                .build();
        Set<ConstraintViolation<TaskDto>> violations = validator.validate(task);
        assertEquals(1, violations.size());
    }
}