package smartdermato.esprit.tn.smartdermato.Entities;

public class Model {
    private int image;
    private String date,time;

    public Model(int image, String date, String time) {
        this.image = image;
        this.date = date;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Model{" +
                "image=" + image +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
