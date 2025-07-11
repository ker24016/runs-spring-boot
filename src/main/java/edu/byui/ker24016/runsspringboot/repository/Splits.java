package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.Run;
import edu.byui.ker24016.runsspringboot.model.Split;
import org.springframework.data.repository.CrudRepository;

public interface Splits extends CrudRepository<Split, Integer> {
}
