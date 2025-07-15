package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.Biome;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface Biomes extends CrudRepository<Biome, Integer> {
    Optional<Biome> findTopByNameEqualsIgnoreCase(String name);
}
