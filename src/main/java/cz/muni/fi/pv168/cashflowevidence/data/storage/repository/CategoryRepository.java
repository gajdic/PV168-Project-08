package cz.muni.fi.pv168.cashflowevidence.data.storage.repository;

import cz.muni.fi.pv168.cashflowevidence.data.storage.dao.CategoryDao;
import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.cashflowevidence.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represent a repository for categories
 */
public class CategoryRepository implements Repository<Category> {

    private List<Category> categories = new ArrayList<>();

    private final CategoryDao dao;

    private final EntityMapper<CategoryEntity, Category> mapper;

    public CategoryRepository(CategoryDao dao,
                              EntityMapper<CategoryEntity, Category> mapper) {
        this.dao = dao;
        this.mapper = mapper;
        refresh();
    }

    @Override
    public int getSize() {
        return categories.size();
    }

    @Override
    public Optional<Category> findByID(long id) {
        return categories.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<Category> findByIndex(int index) {
        if (index >= 0 && index < getSize()) {
            return Optional.of(categories.get(index));
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public void create(Category newEntity) {
        Stream.of(newEntity)
                .map(mapper::mapToEntity)
                .map(dao::create)
                .map(mapper::mapToModel)
                .forEach(c -> categories.add(c));
    }

    @Override
    public void deleteByIndex(int index) {
        dao.deleteById(categories.get(index).getId());
        categories.remove(index);
    }

    @Override
    public void update(Category entity) {
        int index = categories.indexOf(entity);
        Stream.of(entity)
                .map(mapper::mapToEntity)
                .map(dao::update)
                .map(mapper::mapToModel)
                .forEach(c -> categories.set(index, c));
    }

    @Override
    public void refresh() {
        categories = fetchAll();
    }

    private List<Category> fetchAll() {
        return dao.findAll().stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
