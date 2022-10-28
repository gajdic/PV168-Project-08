package cz.muni.fi.pv168.cashflowevidence.model;

import java.awt.Color;
import java.util.Objects;

/**
 * Class representing categories used for transactions
 */
public class Category {
    private String name;
    private Color color;

    public Category() {
        setColor(Color.RED);
    }

    public Category(String name, Color color) {
        setName(name);
        setColor(color);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    @Override
    public String toString() {
        return name;
    }
}
