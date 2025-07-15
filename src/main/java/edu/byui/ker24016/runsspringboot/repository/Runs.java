package edu.byui.ker24016.runsspringboot.repository;

import edu.byui.ker24016.runsspringboot.model.Run;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Runs extends CrudRepository<Run, Integer> {
    List<Run> getAllByIdBetweenOrderByIdAsc(Integer idAfter, Integer idBefore);

    Run getRunById(Integer id);
}
