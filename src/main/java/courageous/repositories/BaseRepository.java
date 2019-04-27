package courageous.repositories;

import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

public abstract class BaseRepository<T> {
    protected static final int DEFAULT_LIMIT = 200;

    private final DynamoDBMapper mapper;
    private final Class<T> typeParameterClass;

    public BaseRepository(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(client);
    }

    public final DynamoDBMapper getMapper() {
        return this.mapper;
    }

    public List<T> list() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(DEFAULT_LIMIT);

        List<T> locationsList = mapper.scan(typeParameterClass, scanExpression);
        return locationsList;
    }

    public T get(T data) {
        return mapper.load(data);
    }

    public T save(T data) {
        mapper.save(data);
        return mapper.load(data);
    }

    public void delete(T data) {
        mapper.delete(data);
    }
}