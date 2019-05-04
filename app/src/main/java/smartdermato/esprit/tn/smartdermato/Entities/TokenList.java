package smartdermato.esprit.tn.smartdermato.Entities;

public class TokenList {

    private int id;
    private int userToken;
    private String Token;

    public TokenList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserToken() {
        return userToken;
    }

    public void setUserToken(int userToken) {
        this.userToken = userToken;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "id=" + id +
                ", userToken=" + userToken +
                ", Token='" + Token + '\'' +
                '}';
    }
}
