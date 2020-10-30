package test.baseapp.co.id.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonBookId is a Querydsl query type for PersonBookId
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QPersonBookId extends BeanPath<PersonBookId> {

    private static final long serialVersionUID = -682580684L;

    public static final QPersonBookId personBookId = new QPersonBookId("personBookId");

    public final NumberPath<java.math.BigInteger> bookId = createNumber("bookId", java.math.BigInteger.class);

    public final NumberPath<java.math.BigInteger> personId = createNumber("personId", java.math.BigInteger.class);

    public QPersonBookId(String variable) {
        super(PersonBookId.class, forVariable(variable));
    }

    public QPersonBookId(Path<? extends PersonBookId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonBookId(PathMetadata metadata) {
        super(PersonBookId.class, metadata);
    }

}

