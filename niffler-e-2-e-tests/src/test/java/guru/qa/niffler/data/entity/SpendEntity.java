package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private Date spendDate;
    private Double amount;
    private String description;
    private CategoryEntity category;

    public static SpendEntity fromJson(SpendJson spendJson, CategoryJson category) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(spendJson.id());
        spendEntity.setUsername(spendJson.username());
        spendEntity.setCurrency(spendJson.currency());
        spendEntity.setSpendDate(spendJson.spendDate());
        spendEntity.setAmount(spendJson.amount());
        spendEntity.setDescription(spendJson.description());
        spendEntity.setCategory(CategoryEntity.fromJson(category));
        return spendEntity;
    }

    public static SpendEntity fromJson(SpendJson spendJson) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(spendJson.id());
        spendEntity.setSpendDate(spendJson.spendDate());
        spendEntity.setAmount(spendJson.amount());
        spendEntity.setDescription(spendJson.description());
        return spendEntity;
    }
}
