package smartdermato.esprit.tn.smartdermato.Entities;

public class User_Firebase {

    private String id;
    private  String username;
    private String imageURL;
    private String status;
    private boolean isseen;

    public User_Firebase(String id, String username, String imageURL ,String status,boolean isseen) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status =status;
        this.isseen = isseen;
    }

    public User_Firebase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    @Override
    public String toString() {
        return "User_Firebase{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", status='" + status + '\'' +
                ", isseen=" + isseen +
                '}';
    }
}
