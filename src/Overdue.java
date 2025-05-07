import java.sql.Date;

public class Overdue {
    String name;
    int isbn;
    String title;
    Date dueDate;
    int diffdays;

    public Overdue(String name, int isbn, String title, Date dueDate, int diffdays) {
        this.name = name;
        this.isbn = isbn;
        this.title = title;
        this.dueDate = dueDate;
        this.diffdays = diffdays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getdueDate() {
        return dueDate;
    }

    public void setdueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getDiffdays() {
        return diffdays;
    }

    public void setDiffdays(int diffdays) {
        this.diffdays = diffdays;
    }
}
