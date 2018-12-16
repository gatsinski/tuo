package tuo.session;

public class Points {
    int knowledge;
    int reputation;

    public Points(int knowledge, int reputation) {
        this.knowledge = knowledge;
        this.reputation = reputation;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(int knowledge) {
        this.knowledge = knowledge;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
}
