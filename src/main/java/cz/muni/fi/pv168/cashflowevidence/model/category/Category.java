package cz.muni.fi.pv168.cashflowevidence.model.category;

import java.awt.Color;
import java.util.Objects;

/**
 * Class representing categories used for transactions
 */
public class Category {
    private String name;
    private Color color;
    private Long id;

    public Category() {
        setColor(Color.RED);
    }

    public Category(String name, Color color) {
        this(null, name, color);
    }

    public Category(Long id, String name, Color color) {
        this.id = id;
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

    public String getColorString() {
        return ("#"+Integer.toHexString(this.getColor().getRGB()).substring(2));
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    @Override
    public String toString() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = Objects.requireNonNull(id, "when setting id, it must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        return Objects.equals(name, category.name) && Objects.equals(color, category.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, id);
    }
}
