package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.EnterType;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EnterTypes extends CrudRepository<EnterType, Integer> {
    Optional<EnterType> findTopByNameEqualsIgnoreCase(String name);
}
