package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.Biome;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Biomes extends CrudRepository<Biome, Integer> {
    List<Biome> findFirstByNameEqualsIgnoreCase(String name, Limit limit);
}
