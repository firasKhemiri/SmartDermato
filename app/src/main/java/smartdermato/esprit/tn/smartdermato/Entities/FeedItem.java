package smartdermato.esprit.tn.smartdermato.Entities;


public class FeedItem {

    private int id;
    private String post_name;
    private int num_likes;
    private boolean liked;
    private boolean is_picture;
    private String pic_url;


    private String username;

    private String photoprof;

    private int user_id;
    private int idme;

    private String date;


    public FeedItem() {
    }

    public FeedItem(int id, String post_name, int num_likes, boolean liked, boolean is_picture, String pic_url, String username, String photoprof, int user_id, int idme, String date) {
        this.id = id;
        this.post_name = post_name;
        this.num_likes = num_likes;
        this.liked = liked;
        this.is_picture = is_picture;
        this.pic_url = pic_url;
        this.username = username;
        this.photoprof = photoprof;
        this.user_id = user_id;
        this.idme = idme;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public int getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean is_picture() {
        return is_picture;
    }

    public void setIs_picture(boolean is_picture) {
        this.is_picture = is_picture;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoprof() {
        return photoprof;
    }

    public void setPhotoprof(String photoprof) {
        this.photoprof = photoprof;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIdme() {
        return idme;
    }

    public void setIdme(int idme) {
        this.idme = idme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
