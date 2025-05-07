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

    //나의 정보 수정 메서드
    public void updateMyInfo(){
        System.out.println("PW를 다시 입력해주세요. ");
        String pwd=input.nextLine();

        if(this.currentUser.getUserpw().equals(pwd)){
            System.out.println("===== 나의 정보 수정 =====");
            System.out.println("수정할 ID : ");
            String new_id=input.nextLine();
            System.out.println("수정할 PW : ");
            String new_pw=input.nextLine();
            System.out.println("수정할 나이 : ");
            int new_age=input.nextInt();
            input.nextLine();
            System.out.println("수정할 관심분야 : ");
            String new_interest=input.nextLine();

            String updateinfoquery="update usertbl "+
                    " set userid=?, userpw=?, userage=?, userinterest=? "+
                    " where userid=? ";
            Connection conn=db.getConnection();
            try{
                PreparedStatement pstmt=conn.prepareStatement(updateinfoquery);
                pstmt.setString(1, new_id);
                pstmt.setString(2, new_pw);
                pstmt.setInt(3, new_age);
                pstmt.setString(4, new_interest);
                pstmt.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.println("====== "+this.currentUser.getUsername()+"님의 정보가 변경되었습니다. =====");
        }else{
            System.out.println("비밀번호가 틀렸습니다. 다시 시도하세요.");
        }

    }

    public void run(){
        db.initDBConnect();//콘, 스테이트 변화로 연결, 이부분 함수부 윗줄 추가(오류 확인용인데 크게 의미는 없음)
    }
}
