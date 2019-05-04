package smartdermato.esprit.tn.smartdermato.Entities;

public class Rating {

    private int pationId;
    private int medcinId;
    private float note;

    public Rating() {
    }

    public int getPationId() {
        return pationId;
    }

    public void setPationId(int pationId) {
        this.pationId = pationId;
    }

    public int getMedcinId() {
        return medcinId;
    }

    public void setMedcinId(int medcinId) {
        this.medcinId = medcinId;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "pationId=" + pationId +
                ", medcinId=" + medcinId +
                ", note=" + note +
                '}';
    }
}
