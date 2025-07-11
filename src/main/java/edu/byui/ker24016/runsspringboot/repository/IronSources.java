package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.IronSource;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IronSources extends CrudRepository<IronSource, Integer> {
    List<IronSource> findFirstByNameEqualsIgnoreCase(String name, Limit limit);
}
