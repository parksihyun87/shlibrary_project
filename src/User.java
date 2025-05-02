import java.util.Date;

public class User {
    String userid;
    String userpw;
    String username;
    Date userdate;
    int userage;
    String userinterest;
    String usergrade;

    public User(String userid, String userpw, String username, Date userdate, int userage, String userinterest, String usergrade){
        this.userid=userid;
        this.userpw=userpw;
        this.username=username;
        this.userdate=userdate;
        this.userage=userage;
        this.userinterest=userinterest;
        this.usergrade=usergrade;
    }

    // getter
    public String getUserid() {
        return userid;
    }
    public String getUserpw() {
        return userpw;
    }
    public String getUsername() {
        return username;
    }
    public Date getUserdate() {
        return userdate;
    }
    public int getUserage() {
        return userage;
    }
    public String getUserinterest() {
        return userinterest;
    }
    public String getUsergrade() {
        return usergrade;
    }

    //setter
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public void setUserpw(String userpw) {
        this.userpw = userpw;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUserdate(Date userdate) {
        this.userdate = userdate;
    }
    public void setUserage(int userage) {
        this.userage = userage;
    }
    public void setUserinterest(String userinterest) {
        this.userinterest = userinterest;
    }
    public void setUsergrade(String usergrade) {
        this.usergrade = usergrade;
    }
}
