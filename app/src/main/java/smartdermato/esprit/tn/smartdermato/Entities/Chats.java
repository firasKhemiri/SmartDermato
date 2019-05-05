package smartdermato.esprit.tn.smartdermato.Entities;

public class Chats {
    private int id;
    private int sender;
    private int receiver;
    private String imageName;
    private String message;
    private boolean isseen;
    private  int idCon;

    public Chats(int sender, int receiver, String message, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
    }

    public Chats() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getIdCon() {
        return idCon;
    }

    public void setIdCon(int idCon) {
        this.idCon = idCon;
    }

    @Override
    public String toString() {
        return "Chats{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", imageName='" + imageName + '\'' +
                ", message='" + message + '\'' +
                ", isseen=" + isseen +
                ", idCon=" + idCon +
                '}';
    }
}
