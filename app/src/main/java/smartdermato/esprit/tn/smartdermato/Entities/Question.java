package smartdermato.esprit.tn.smartdermato.Entities;

public class Question {

    private int id;

    private String question;

    private int type;

    private int response;


    public Question() {
    }

    public Question(int id, String question, int type,int response) {
        this.id = id;
        this.question = question;
        this.type = type;
        this.response = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }
}
