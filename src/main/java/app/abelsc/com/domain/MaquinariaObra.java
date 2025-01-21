package app.abelsc.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A MaquinariaObra.
 */
@Entity
@Table(name = "maquinaria_obra")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaquinariaObra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "horas")
    private Long horas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "empresa", "cliente" }, allowSetters = true)
    private Obra obra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "empresa" }, allowSetters = true)
    private Maquinaria maquinaria;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaquinariaObra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHoras() {
        return this.horas;
    }

    public MaquinariaObra horas(Long horas) {
        this.setHoras(horas);
        return this;
    }

    public void setHoras(Long horas) {
        this.horas = horas;
    }

    public Obra getObra() {
        return this.obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public MaquinariaObra obra(Obra obra) {
        this.setObra(obra);
        return this;
    }

    public Maquinaria getMaquinaria() {
        return this.maquinaria;
    }

    public void setMaquinaria(Maquinaria maquinaria) {
        this.maquinaria = maquinaria;
    }

    public MaquinariaObra maquinaria(Maquinaria maquinaria) {
        this.setMaquinaria(maquinaria);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaquinariaObra)) {
            return false;
        }
        return getId() != null && getId().equals(((MaquinariaObra) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaquinariaObra{" +
            "id=" + getId() +
            ", horas=" + getHoras() +
            "}";
    }
}
