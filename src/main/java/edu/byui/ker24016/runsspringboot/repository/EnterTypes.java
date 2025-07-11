package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.EnterType;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnterTypes extends CrudRepository<EnterType, Integer> {
    List<EnterType> findFirstByNameEqualsIgnoreCase(String name, Limit limit);
}
