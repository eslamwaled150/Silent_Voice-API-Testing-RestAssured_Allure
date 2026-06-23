package Pages;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Auth {

    private String fName;
    private String lName;
    private String email;
    private String password;
    private String conPassword;
    private String otp;

    // Default constructor (required for Jackson)
    public Auth() {}

    // Constructor for Register
    public Auth(String fName, String lName, String email, String password, String conPassword) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
        this.conPassword = conPassword;
    }

    // Constructor for Confirm Email
    public Auth(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    // Constructor for Login
    public Auth(String email, String password, boolean isLogin) {
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
    public String getfName() { return fName; }
    public void setfName(String fName) { this.fName = fName; }

    public String getlName() { return lName; }
    public void setlName(String lName) { this.lName = lName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConPassword() { return conPassword; }
    public void setConPassword(String conPassword) { this.conPassword = conPassword; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}