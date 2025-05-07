import java.sql.*;
import java.util.Scanner;

public class MemberManager_safe_name {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;
    DBConnect db;
    Scanner input = new Scanner(System.in);

    public void checkMyInfo() throws SQLException {
        DBConnect db=new DBConnect();
        db.initDBConnect();
        String checkquery="select * from usertbl where userid=?";

        try(Connection conn=db.getConnection();
            PreparedStatement pstmt=conn.prepareStatement(checkquery)){
            pstmt.setString(1, currentUser.getUserid());
            ResultSet rs=pstmt.executeQuery();

            while(rs.next()){
                String id=rs.getString("userid");
                String pw=rs.getString("userpw");
                String name=rs.getString("username");
                Date userdate=rs.getDate("userdate");
                int age=rs.getInt("userage");
                String interest=rs.getString("userinterest");
                String grade=rs.getString("usergrade");

                System.out.println("===== "+name+"님의 정보"+ "=====");
                System.out.println("ID : "+id+'\t' +"PW : "+pw);
                System.out.println("가입일자 : "+userdate);
                System.out.println("나이 : "+age+'\t'+"관심분야 : "+interest+'\t'+"등급 : "+grade);
            }
        }
    }

    public void run(){
        db.initDBConnect();//콘, 스테이트 변화로 연결, 이부분 함수부 윗줄 추가(오류 확인용인데 크게 의미는 없음)
    }
}
