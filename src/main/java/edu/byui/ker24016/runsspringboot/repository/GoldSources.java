package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.GoldSource;
import edu.byui.ker24016.runsspringboot.model.Run;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoldSources extends CrudRepository<GoldSource, Integer> {
    List<GoldSource> findFirstByNameEqualsIgnoreCase(String name, Limit limit);
}
