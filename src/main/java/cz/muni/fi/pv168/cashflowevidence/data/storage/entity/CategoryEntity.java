package cz.muni.fi.pv168.cashflowevidence.data.storage.entity;

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
}