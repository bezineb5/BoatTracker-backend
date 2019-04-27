package courageous.webservices.resources;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;

import courageous.models.Assignation;
import courageous.repositories.AssignationRepository;
import courageous.webservices.exceptions.InvalidRequestException;
import courageous.webservices.exceptions.NotAuthorizedException;
import courageous.webservices.messages.AssignationUpdate;

public class AssignationResources {
    private AssignationResources() {
    }

    public static List<Assignation> list() {
        return new AssignationRepository().list();
    }

    public static Assignation create(Assignation newAssignation) throws InvalidRequestException {
        if (newAssignation.getInventoryItemId() == null) {
            throw new InvalidRequestException("InventoryItemId cannot be empty");
        }

        return new AssignationRepository().save(newAssignation);
    }

    public static Assignation get(String inventoryItemId) throws InvalidRequestException {
        Assignation assignation = new Assignation().inventoryItemId(ResourcesHelper.parseUuid(inventoryItemId));
        return new AssignationRepository().get(assignation);
    }

    public static void delete(String inventoryItemId) throws InvalidRequestException {
        Assignation assignation = new Assignation().inventoryItemId(ResourcesHelper.parseUuid(inventoryItemId));
        new AssignationRepository().delete(assignation);
    }

    public static Assignation getFromSecret(String secret) throws InvalidRequestException {
        if (secret == null) {
            throw new InvalidRequestException("Secret cannot be empty");
        }

        Assignation searchAssignation = new Assignation().secret(ResourcesHelper.parseUuid(secret));
        AssignationRepository repo = new AssignationRepository();

        final DynamoDBQueryExpression<Assignation> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.setHashKeyValues(searchAssignation);
        queryExpression.setIndexName("Secret-index");
        queryExpression.setConsistentRead(false); // cannot use consistent read on GSI
        final PaginatedQueryList<Assignation> results = repo.getMapper().query(Assignation.class, queryExpression);

        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    public static Assignation checkOut(String secret, AssignationUpdate newAssignation) throws InvalidRequestException, NotAuthorizedException {
        Assignation assignation = getFromSecret(secret);
        // Check if it's already check-out; it's forbidden to check out twice
        if (assignation.isCheckedOut()) {
            throw new NotAuthorizedException("Already checked-out. You cannot check-out an item twice.");
        }

        assignation.checkedOut(true).userName(newAssignation.getUserName()).userPhone(newAssignation.getUserPhone());

        return new AssignationRepository().save(assignation);
    }

    public static void checkIn(String secret) throws InvalidRequestException {
        Assignation assignation = getFromSecret(secret);
        new AssignationRepository().delete(assignation);
    }
}