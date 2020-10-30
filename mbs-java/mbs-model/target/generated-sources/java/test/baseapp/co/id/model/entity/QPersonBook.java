package test.baseapp.co.id.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPersonBook is a Querydsl query type for PersonBook
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPersonBook extends EntityPathBase<PersonBook> {

    private static final long serialVersionUID = -729201095L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPersonBook personBook = new QPersonBook("personBook");

    public final QBook book;

    public final QPerson person;

    public final QPersonBookId personBookId;

    public QPersonBook(String variable) {
        this(PersonBook.class, forVariable(variable), INITS);
    }

    public QPersonBook(Path<? extends PersonBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPersonBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPersonBook(PathMetadata metadata, PathInits inits) {
        this(PersonBook.class, metadata, inits);
    }

    public QPersonBook(Class<? extends PersonBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book")) : null;
        this.person = inits.isInitialized("person") ? new QPerson(forProperty("person")) : null;
        this.personBookId = inits.isInitialized("personBookId") ? new QPersonBookId(forProperty("personBookId")) : null;
    }

}

