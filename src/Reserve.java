import java.sql.Date;

public class Reserve {
    int reservenumber;
    String userid;
    int isbn;
    Date reservedate;
    int reserverank;
    String reservestatus;

    public Reserve(int reservenumber, String userid, int isbn, Date reservedate, int reserverank, String reservestatus) {
        this.reservenumber = reservenumber;
        this.userid = userid;
        this.isbn = isbn;
        this.reservedate = reservedate;
        this.reserverank = reserverank;
        this.reservestatus = reservestatus;
    }

    public int getReservenumber() {
        return reservenumber;
    }

    public void setReservenumber(int reservenumber) {
        this.reservenumber = reservenumber;
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

    public Date getReservedate() {
        return reservedate;
    }

    public void setReservedate(Date reservedate) {
        this.reservedate = reservedate;
    }

    public int getReserverank() {
        return reserverank;
    }

    public void setReserverank(int reserverank) {
        this.reserverank = reserverank;
    }

    public String getReservestatus() {
        return reservestatus;
    }

    public void setReservestatus(String reservestatus) {
        this.reservestatus = reservestatus;
    }
}
