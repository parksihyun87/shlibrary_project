import java.sql.*;

public class DBConnect {
    // git check
    private String driver="com.mysql.cj.jdbc.Driver";
    private String url="jdbc:mysql://localhost:3306/shlibrary";
    private String user="root";
    private String password="1234";

    private Connection conn=null;
    private Statement stmt=null;

    public void initDBConnect(){
        try{
            Class.forName(this.driver);
            this.conn=DriverManager.getConnection(this.url, this.user, this.password);
            this.stmt=this.conn.createStatement();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
