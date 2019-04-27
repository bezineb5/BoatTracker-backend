package courageous.repositories;

import courageous.models.Assignation;

public class AssignationRepository extends BaseRepository<Assignation> {    
    public AssignationRepository() {
        super(Assignation.class);
    }
}