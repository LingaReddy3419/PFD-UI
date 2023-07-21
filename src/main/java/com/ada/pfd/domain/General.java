package com.ada.pfd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A General.
 */
@Entity
@Table(name = "general")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class General implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Operations operations;

    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Action action;

    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ModeOfCharging modeOfCharging;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public General id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operations getOperations() {
        return this.operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    public General operations(Operations operations) {
        this.setOperations(operations);
        return this;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public General action(Action action) {
        this.setAction(action);
        return this;
    }

    public ModeOfCharging getModeOfCharging() {
        return this.modeOfCharging;
    }

    public void setModeOfCharging(ModeOfCharging modeOfCharging) {
        this.modeOfCharging = modeOfCharging;
    }

    public General modeOfCharging(ModeOfCharging modeOfCharging) {
        this.setModeOfCharging(modeOfCharging);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof General)) {
            return false;
        }
        return id != null && id.equals(((General) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "General{" +
            "id=" + getId() +
            "}";
    }
}
