package cz.muni.fi.pv168.cashflowevidence.data.storage.dao;

import cz.muni.fi.pv168.cashflowevidence.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.cashflowevidence.exceptions.DataStorageException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CategoryDao implements DatabaseAccessObject<CategoryEntity> {
    private final Supplier<ConnectionHandler> connections;

    public CategoryDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    public CategoryEntity create(CategoryEntity entity) {
        String sql = "INSERT INTO Category (name, color) VALUES (?, ?)";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
            ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.color());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                long categoryId;

                if (keys.next()) {
                    categoryId = keys.getLong(1);
                } else {
                    throw new DataStorageException("Failed to retrieve generated key for: " + entity);
                }

                if (keys.next()) {
                    throw new DataStorageException("Retrieved multiple keys for: " + entity);
                }

                return findById(categoryId);
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to execute create for: %s %s".formatted(entity, ex));
        }
    }

    public CategoryEntity findById(long id) {
        String sql = "SELECT * FROM Category WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            if (result.next()) {
                return new CategoryEntity(
                        result.getString("name"),
                        result.getString("color"));
            }

            return null;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load category by id: " + id);
        }
    }

    public List<CategoryEntity> findAll() {
        String sql = "SELECT * FROM Category";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            List<CategoryEntity> categories = new ArrayList<>();

            try (var results = statement.executeQuery()) {
                while (results.next()) {
                    var category = new CategoryEntity(
                            results.getString("name"),
                            results.getString("color")
                    );
                    categories.add(category);
                }
            }

            return categories;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM Category WHERE id = ?";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql);
            ) {
            statement.setLong(1, id);
            int changedRows = statement.executeUpdate();

            if (changedRows == 0) {
                throw new DataStorageException("Category with id %d does not exist.".formatted(id));
            }

            if (changedRows > 0) {
                throw new DataStorageException("Multiple categories were deleted. (%d)".formatted(changedRows));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Unable to delete category with id %d %s".formatted(id, ex));
        }
    }
}
