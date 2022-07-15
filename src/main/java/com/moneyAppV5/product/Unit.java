package com.moneyAppV5.product;

import com.moneyAppV5.product.dto.UnitDTO;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "units")
public class Unit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "main_unit_id")
    private MainUnit mainUnit;
    @ManyToOne
    @JoinColumn(name = "sub_unit_id")
    private SubUnit subUnit;
    @OneToMany(mappedBy = "unit")
    private Set<Product> products;

    public UnitDTO toDto()
    {
        var result = new UnitDTO();

        result.createName(this.subUnit.getName(), this.mainUnit.getName());
        result.createSymbol(this.subUnit.getPrefix(), this.mainUnit.getSymbol());
        result.setMultiplier(this.subUnit.getMultiplier());
        result.setBaseUnitSymbol(this.mainUnit.getSymbol());

        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MainUnit getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(MainUnit mainUnit) {
        this.mainUnit = mainUnit;
    }

    public SubUnit getSubUnit() {
        return subUnit;
    }

    public void setSubUnit(SubUnit subUnit) {
        this.subUnit = subUnit;
    }
}
