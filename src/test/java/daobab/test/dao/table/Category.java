package daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.daobab.clone.EntityDuplicator;
import io.daobab.model.*;
import io.daobab.test.dao.column.CategoryId;
import io.daobab.test.dao.column.LastUpdate;
import io.daobab.test.dao.column.Name;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@IdGenerator(type = IdGeneratorType.SEQUENCE, sequenceName = "CATEGORY_SEQ")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Category extends Table implements
        CategoryId<Category>,
        Name<Category>,
        LastUpdate<Category>,

        PrimaryKey<Category, Integer, CategoryId> {

    @Override
    public String getEntityName() {
        return "CATEGORY";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colCategoryId()),
                new TableColumn(colName()),
                new TableColumn(colLastUpdate())

        );
    }

    @Override
    public Category clone() {
        return EntityDuplicator.cloneEntity(this);
    }

    @Override
    public Column<Category, Integer, CategoryId> colID() {
        return colCategoryId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey other = (PrimaryKey) obj;
        return Objects.equals(getId(), other.getId());
    }

}