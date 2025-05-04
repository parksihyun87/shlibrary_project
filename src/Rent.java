import java.sql.Date;

public class Rent {
    int rentnumber;
    String userid;
    int isbn;
    Date rentdate;
    Date duedate;
    Boolean prolong;
    Date turnindate;
    Boolean turnin;

    public Rent(int rentnumber, String userid, int isbn, Date rentdate, Date duedate, Boolean prolong, Date turnindate, Boolean turnin) {
        this.rentnumber = rentnumber;
        this.userid = userid;
        this.isbn = isbn;
        this.rentdate = rentdate;
        this.duedate = duedate;
        this.prolong = prolong;
        this.turnindate = turnindate;
        this.turnin = turnin;
    }

    public int getRentnumber() {
        return rentnumber;
    }

    public void setRentnumber(int rentnumber) {
        this.rentnumber = rentnumber;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public Date getRentdate() {
        return rentdate;
    }

    public void setRentdate(Date rentdate) {
        this.rentdate = rentdate;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public Boolean getProlong() {
        return prolong;
    }

    public void setProlong(Boolean prolong) {
        this.prolong = prolong;
    }

    public Date getTurnindate() {
        return turnindate;
    }

    public void setTurnindate(Date turnindate) {
        this.turnindate = turnindate;
    }

    public Boolean getTurnin() {
        return turnin;
    }

    public void setTurnin(Boolean turnin) {
        this.turnin = turnin;
    }
}
