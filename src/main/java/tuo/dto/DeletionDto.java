package tuo.dto;

import javax.validation.constraints.AssertTrue;

public class DeletionDto {

    @AssertTrue
    private Boolean confirmation;

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }
}
