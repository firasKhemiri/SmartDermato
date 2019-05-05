package smartdermato.esprit.tn.smartdermato.Entities;

public class Consultation {

    private int id;
    private String dateC;
    private String imagePath;
    private int idUser;
    private String type;
    private String pourcentage;

    public Consultation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateC() {
        return dateC;
    }

    public void setDateC(String dateC) {
        this.dateC = dateC;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(String pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Consultation(int id, String dateC, String imagePath, int idUser, String type, String pourcentage) {
        this.id = id;
        this.dateC = dateC;
        this.imagePath = imagePath;
        this.idUser = idUser;
        this.type = type;
        this.pourcentage = pourcentage;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", dateC='" + dateC + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", idUser=" + idUser +
                ", type='" + type + '\'' +
                ", pourcentage='" + pourcentage + '\'' +
                '}';
    }
}
