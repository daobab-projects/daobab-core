package io.daobab.target.buffer.single.function.aggregate;

import io.daobab.model.Column;
import io.daobab.model.RelatedTo;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.function.AggregateFunction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FunctionSum extends AggregateFunction {


    @Override
    public String mode() {
        return "SUM";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <F> String applyFunction(Entities entities, ColumnFunction<?, F, ?, ?> columnFunction) {
        Column<?, F, RelatedTo> targetColumn = columnFunction.getFinalColumn();
        List<BigDecimal> fields = (List<BigDecimal>) entities.stream()
                .map(e -> targetColumn.getValueOf((RelatedTo) e))
                .filter(Objects::nonNull)
                .map(c -> new BigDecimal(c.toString()))
                .collect(Collectors.toList());

        BigDecimal rv = fields.stream().reduce(new BigDecimal(0), BigDecimal::add);
        return String.valueOf(rv);
    }
}
