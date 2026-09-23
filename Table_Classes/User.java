package Table_Classes;
import java.time.LocalDate;


public class User {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String role;
    private String password;
    private String username;
    private String email;
    private String cellular;


    public User(){};


    public User(String firstName,String lastName, LocalDate birthDate,String role,
    String password,String username, String email, String cellular)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.role = role;
        this.password = password;
        this.username = username;
        this.email = email;
        this.cellular = cellular;
    }

    
    public User(int id, String firstName, String lastName, LocalDate birthDate, String role, String password,
            String username, String email, String cellular) {
       
        this(firstName, lastName, birthDate, role, password, username, email, cellular);
        this.id = id;

    }

    @Override
    public String toString() {

        String c=lastName.toUpperCase();

        return String.format("User[ ID: %-10d | %s %s | Date of Birth: %s | Role: %s | UserName: %s |Email: %s]"
            ,id,firstName,c,birthDate,role,username,email);
    }

    public boolean isManager(){
        return this.role!=null && !this.role.equalsIgnoreCase("regular");
    }

        public boolean isAdmin() {
            return this.role!=null && this.role.equalsIgnoreCase("admin");
        }

    //setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellular() {
        return cellular;
    }

    public void setCellular(String cellular) {
        this.cellular = cellular;
    }



    

    
}
