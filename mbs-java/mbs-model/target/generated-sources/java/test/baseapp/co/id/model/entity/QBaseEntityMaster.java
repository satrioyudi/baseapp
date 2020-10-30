package test.baseapp.co.id.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseEntityMaster is a Querydsl query type for BaseEntityMaster
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseEntityMaster extends EntityPathBase<BaseEntityMaster> {

    private static final long serialVersionUID = -854381647L;

    public static final QBaseEntityMaster baseEntityMaster = new QBaseEntityMaster("baseEntityMaster");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    //inherited
    public final NumberPath<java.math.BigInteger> id = _super.id;

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath updatedBy = createString("updatedBy");

    public final DateTimePath<java.util.Date> updatedDate = createDateTime("updatedDate", java.util.Date.class);

    public QBaseEntityMaster(String variable) {
        super(BaseEntityMaster.class, forVariable(variable));
    }

    public QBaseEntityMaster(Path<? extends BaseEntityMaster> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseEntityMaster(PathMetadata metadata) {
        super(BaseEntityMaster.class, metadata);
    }

}

