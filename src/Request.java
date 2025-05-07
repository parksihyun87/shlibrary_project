public class Request {
    int requestnum;
    String userid;
    String title;
    String author;
    String publisher;
    String comrequest;

    public Request(int requestnum, String userid, String title, String author, String publisher, String comrequest) {
        this.requestnum = requestnum;
        this.userid = userid;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.comrequest = comrequest;
    }

    public int getRequestnum() {
        return requestnum;
    }

    public void setRequestnum(int requestnum) {
        this.requestnum = requestnum;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getComrequest() {
        return comrequest;
    }

    public void setComrequest(String comrequest) {
        this.comrequest = comrequest;
    }
}
