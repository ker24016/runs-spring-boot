package edu.byui.ker24016.runsspringboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "iron_source", schema = "runs")
public class IronSource {
    @Id
    @Column(name = "iron_source_id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 45) private String name;

}