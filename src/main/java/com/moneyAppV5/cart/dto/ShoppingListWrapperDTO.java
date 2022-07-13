package com.moneyAppV5.cart.dto;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListWrapperDTO
{
    List<ShoppingPositionDTO> positions = new ArrayList<>();

    public ShoppingListWrapperDTO() {
        this.positions.add(new ShoppingPositionDTO());
    }

    public List<ShoppingPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<ShoppingPositionDTO> positions) {
        this.positions = positions;
    }
}
