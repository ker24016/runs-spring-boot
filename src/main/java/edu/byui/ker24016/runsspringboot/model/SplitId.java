package edu.byui.ker24016.runsspringboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SplitId implements Serializable {
    private static final long serialVersionUID = -1282341329934357224L;
    @Column(name = "split_type_id", nullable = false) private Integer splitTypeId;

    @Column(name = "run_id", nullable = false) private Integer runId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SplitId entity = (SplitId) o;
        return Objects.equals(this.splitTypeId, entity.splitTypeId) &&
               Objects.equals(this.runId, entity.runId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(splitTypeId, runId);
    }

}