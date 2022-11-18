package cz.muni.fi.pv168.cashflowevidence.data.storage.mapper;

import cz.muni.fi.pv168.cashflowevidence.data.storage.entity.CategoryEntity;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.awt.Color;

/**
 * Mapper from the {@link CategoryEntity} to {@link Category}.
 */
public class CategoryMapper implements EntityMapper<CategoryEntity, Category>{
    // TODO: add validators
    @Override
    public CategoryEntity mapToEntity(Category source) {
        return new CategoryEntity(source.getId() ,source.getName(), source.getColorString());
    }

    @Override
    public Category mapToModel(CategoryEntity entity) {
        return new Category(entity.id(), entity.name(), Color.decode(entity.color()));
    }
}
