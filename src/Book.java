public class Book {

    private int isbn;
    private String callnum;
    private String title;
    private String author;
    private String publisher;
    private int pubyear;
    private int rentnum;

    public Book(int isbn, String callnum, String title, String author, String publisher, int pubyear, int rentnum) {
        this.isbn = isbn;
        this.callnum = callnum;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pubyear = pubyear;
        this.rentnum = rentnum;
    }

    // getter
    public int getIsbn() {
        return this.isbn;
    }
    public String getCallnum() {
        return this.callnum;
    }
    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getPublisher() {
        return this.publisher;
    }
    public int getPubyear() {
        return this.pubyear;
    }
    public int getRentnum() {
        return this.rentnum;
    }

    // setter
    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
    public void setCallnum(String callnum) {
        this.callnum = callnum;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setPubyear(int pubyear) {
        this.pubyear = pubyear;
    }
    public void setRentnum(int rentnum) {
        this.rentnum = rentnum;
    }

}