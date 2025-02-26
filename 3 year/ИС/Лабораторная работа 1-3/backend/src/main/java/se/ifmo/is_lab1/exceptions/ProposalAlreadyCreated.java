package se.ifmo.is_lab1.exceptions;

import org.springframework.http.HttpStatus;

public class ProposalAlreadyCreated extends StudyGroupRuntimeException {
    public ProposalAlreadyCreated() {
        super("Proposal already created", HttpStatus.CONFLICT);
    }
}
