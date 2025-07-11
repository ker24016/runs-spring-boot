package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.IronSource;
import edu.byui.ker24016.runsspringboot.model.Run;
import org.springframework.data.repository.CrudRepository;

public interface IronSources extends CrudRepository<IronSource, Integer> {
}
