package test.baseapp.co.id.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPerson is a Querydsl query type for Person
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPerson extends EntityPathBase<Person> {

    private static final long serialVersionUID = 699707184L;

    public static final QPerson person = new QPerson("person");

    public final QBaseEntityMaster _super = new QBaseEntityMaster(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final StringPath firstName = createString("firstName");

    //inherited
    public final NumberPath<java.math.BigInteger> id = _super.id;

    //inherited
    public final BooleanPath isActive = _super.isActive;

    public final StringPath lastName = createString("lastName");

    public final StringPath nik = createString("nik");

    public final SetPath<PersonBook, QPersonBook> personBooks = this.<PersonBook, QPersonBook>createSet("personBooks", PersonBook.class, QPersonBook.class, PathInits.DIRECT2);

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    //inherited
    public final DateTimePath<java.util.Date> updatedDate = _super.updatedDate;

    public QPerson(String variable) {
        super(Person.class, forVariable(variable));
    }

    public QPerson(Path<? extends Person> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPerson(PathMetadata metadata) {
        super(Person.class, metadata);
    }

}

