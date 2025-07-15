package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.IronSource;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IronSources extends CrudRepository<IronSource, Integer> {
    Optional<IronSource> findTopByNameEqualsIgnoreCase(String name);
}
