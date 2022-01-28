package io.daobab.test.dao.table.enhanced;

import io.daobab.model.EnhancedEntity;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryJoin;
import io.daobab.test.dao.table.Film;
import io.daobab.test.dao.table.Language;


public class EnglishFilm extends Film implements EnhancedEntity {

    private static final Language tabLanguage = new Language();

    @Override
    public <Q extends Query & QueryJoin<Q>> Q enhanceQuery(Q query) {
        return null;
//        return (Q) query
//                .join(tabLanguage, colLanguageId()), and().and(tabLanguage.colName()), English));
    }

}
