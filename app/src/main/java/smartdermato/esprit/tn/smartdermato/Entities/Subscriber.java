package smartdermato.esprit.tn.smartdermato.Entities;

public class Subscriber {
    private int pationId;
    private int medceinId;
    private int etat;

    public Subscriber() {
    }

    public int getPationId() {
        return pationId;
    }

    public void setPationId(int pationId) {
        this.pationId = pationId;
    }

    public int getMedceinId() {
        return medceinId;
    }

    public void setMedceinId(int medceinId) {
        this.medceinId = medceinId;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "pationId=" + pationId +
                ", medceinId=" + medceinId +
                ", etat=" + etat +
                '}';
    }
}
