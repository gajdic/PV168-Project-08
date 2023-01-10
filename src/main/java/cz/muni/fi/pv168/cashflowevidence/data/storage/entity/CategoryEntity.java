package cz.muni.fi.pv168.cashflowevidence.data.storage.entity;

import java.util.Objects;

/**
 * Represents the Category entity
 *
 * @param id        Primary key in the database (sequential)
 * @param name      Name of the category
 * @param color     Color of the category
 */
public record CategoryEntity(
    Long id,
    String name,
    String color
) {
    public CategoryEntity(String name, String color) {
        this(null, name, color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryEntity that = (CategoryEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}