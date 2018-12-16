package tuo.form;

import javax.validation.constraints.NotNull;

public class BattleRoundForm {
    @NotNull
    private Integer bet;  // int defaults to zero while Integer defaults to Null

    public Integer getBet() {
        return bet;
    }

    public void setBet(Integer bet) {
        this.bet = bet;
    }
}
