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
@Table(name = "enter_type", schema = "runs")
public class EnterType {
    @Id
    @Column(name = "enter_type_id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 45) private String name;

}