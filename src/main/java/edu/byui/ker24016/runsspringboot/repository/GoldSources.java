package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.GoldSource;
import edu.byui.ker24016.runsspringboot.model.Run;
import org.springframework.data.repository.CrudRepository;

public interface GoldSources extends CrudRepository<GoldSource, Integer> {
}
