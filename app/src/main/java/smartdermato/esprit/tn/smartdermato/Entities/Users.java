package smartdermato.esprit.tn.smartdermato.Entities;

public class Users {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String user_pic;
    private String FirstName;
    private String LastName;
    private String DateOfBirth = "00-00-0000";
    private String Sexe;
    private String PhoneNumber = "00000000";
    private String CountryPhoneNumber ;
    private String certification;
    private int EtatCertification;
    private String OfficeNumber;
    private String CountryOfficeNumber;
    private String OfficeAddess;
    private String PostalCode;
    private String City;
    private String Country;
    private String Status;
    private String CertificatDate;


    public Users() {
    }

    public Users(int id, String username, String password, String email, String role, String user_pic) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.user_pic = user_pic;
    }

    public Users(int id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Users(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getSexe() {
        return Sexe;
    }

    public void setSexe(String sexe) {
        Sexe = sexe;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public int getEtatCertification() {
        return EtatCertification;
    }

    public void setEtatCertification(int etatCertification) {
        EtatCertification = etatCertification;
    }

    public String getOfficeNumber() {
        return OfficeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        OfficeNumber = officeNumber;
    }

    public String getOfficeAddess() {
        return OfficeAddess;
    }

    public void setOfficeAddess(String officeAddess) {
        OfficeAddess = officeAddess;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCertificatDate() {
        return CertificatDate;
    }

    public void setCertificatDate(String certificatDate) {
        CertificatDate = certificatDate;
    }

    public String getCountryPhoneNumber() {
        return CountryPhoneNumber;
    }

    public void setCountryPhoneNumber(String countryPhoneNumber) {
        CountryPhoneNumber = countryPhoneNumber;
    }

    public String getCountryOfficeNumber() {
        return CountryOfficeNumber;
    }

    public void setCountryOfficeNumber(String countryOfficeNumber) {
        CountryOfficeNumber = countryOfficeNumber;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", user_pic='" + user_pic + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", DateOfBirth='" + DateOfBirth + '\'' +
                ", Sexe='" + Sexe + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", CountryPhoneNumber='" + CountryPhoneNumber + '\'' +
                ", certification='" + certification + '\'' +
                ", EtatCertification=" + EtatCertification +
                ", OfficeNumber='" + OfficeNumber + '\'' +
                ", CountryOfficeNumber='" + CountryOfficeNumber + '\'' +
                ", OfficeAddess='" + OfficeAddess + '\'' +
                ", PostalCode='" + PostalCode + '\'' +
                ", City='" + City + '\'' +
                ", Country='" + Country + '\'' +
                ", Status='" + Status + '\'' +
                ", CertificatDate='" + CertificatDate + '\'' +
                '}';
    }
}

