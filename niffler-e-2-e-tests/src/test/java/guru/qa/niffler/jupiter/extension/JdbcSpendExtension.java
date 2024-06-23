package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public class JdbcSpendExtension extends AbstractSpendExtension {

    private final SpendRepository spendRepository = new SpendRepositoryJdbc();


    @Override
    protected SpendJson createSpend(SpendJson spendJson, CategoryJson categoryJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson, categoryJson);
        spendEntity = spendRepository.createSpend(spendEntity);
        return SpendJson.fromEntity(spendEntity);
    }

    @Override
    protected void removeSpend(SpendJson spendJson) {
        spendRepository.removeSpend(SpendEntity.fromJson(spendJson));
    }
}
