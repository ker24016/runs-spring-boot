package edu.byui.ker24016.runsspringboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "split", schema = "runs", indexes = {
        @Index(name = "fk_split_run1_idx", columnList = "run_id")
})
public class Split {
    @EmbeddedId private SplitId id;

    @MapsId("splitTypeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "split_type_id", nullable = false)
    private SplitType splitType;

    @MapsId("runId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "run_id", nullable = false)
    private Run run;

    @Column(name = "time", nullable = false) private LocalTime time;

}