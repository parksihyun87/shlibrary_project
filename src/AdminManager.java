import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class AdminManager {
    public final static int MEMBERADMIN=1;
    public final static int BOOKADMIN=2;
    public final static int GRADEADMIN=3;
    public final static int BLACKADMIN=4;
    public final static int EXITADMIN=5;

    public static void AdminMenu(){
        System.out.println("1. 회원정보 관리");
        System.out.println("2. 도서정보 관리");
        System.out.println("3. 회원등급 관리");
        System.out.println("4. 연체자 정보 조회");
        System.out.println("5. 관리자모드 나가기");
    }

    public static int selectAdminMenu(){
        Scanner input=new Scanner(System.in);
        int select;
        while(true){
            select=input.nextInt();
            input.nextLine();
            if(select<MEMBERADMIN || select>EXITADMIN){
                System.out.println("잘못된 입력입니다. ");
                continue;
            }
            break;
        }
        return select;
    }

    public void memberAdmin(){

    }

    public void bookAdmin(){

    }

    public void datediff(){
        java.util.Date utilDate=new java.util.Date();
        long currentSeconds=utilDate.getTime();
        java.sql.Date curDate=new java.sql.Date(currentSeconds);

        String datequery="Select u.userid, u.username, u.userdate, u.usergrade "+"from usertbl u";
        DBConnect db=new DBConnect();
        try(Connection conn=db.getConnection();
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(datequery)){
            while (rs.next()){
                String name=rs.getString("username");
                Date userDate=rs.getDate("userdate");
                String grade=rs.getString("usergrade");

                long diffSec=(curDate.getTime()-userDate.getTime())/1000;
                long diffDays=diffSec/(24*60*60);
                long diffMonth=diffDays/30;

                System.out.println(name+"님이 가입한 지 "+diffDays+"일 되었습니다. ");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection conn=db.getConnection();


    }

    public void gradeAdmin(){

    }

    public void blackAdmin(){

    }

}
