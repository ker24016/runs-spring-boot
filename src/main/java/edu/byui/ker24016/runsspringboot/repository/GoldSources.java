package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.GoldSource;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GoldSources extends CrudRepository<GoldSource, Integer> {
    Optional<GoldSource> findTopByNameEqualsIgnoreCase(String name);
}
