package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryJdbc;
import guru.qa.niffler.model.CategoryJson;

public class JdbcCategoryExtension extends AbstractCategoryExtension {
    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    @Override
    protected CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        categoryEntity = spendRepository.createCategory(categoryEntity);
        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    protected void removeCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        spendRepository.removeCategory(categoryEntity);
    }
}