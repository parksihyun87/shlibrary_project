import java.sql.*;

public class MemberManager {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;

    // 현재 대여 현황 메뉴 실행
    public void rentalStatusProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.rentalStatus();
            int select = MenuManager.menuInput(MenuManager.CURRENTRENTALSTATUS, MenuManager.EXITRENTALSTATUS);
            switch (select) {
                case MenuManager.CURRENTRENTALSTATUS:
                    break;
                case MenuManager.EXITRENTALSTATUS:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // 나의 정보 메뉴 실행
    public void myInfoProcess() throws SQLException {
        //MemberManager_safe_name man_safe=new MemberManager_safe_name();
        while (true) {
            boolean endFlag = false;
            MenuManager.myinfo();
            int select = MenuManager.menuInput(MenuManager.CHECKINFO, MenuManager.EXITMYINFO);
            switch (select) {
                case MenuManager.CHECKINFO:
                    checkMyInfo();
                    break;
                case MenuManager.UPDATEINFO:
                    break;
                case MenuManager.EXITMYINFO:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // 연체 확인 메뉴 실행
    public void overdueCheckProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.overdueMenu();
            int select = MenuManager.menuInput(MenuManager.CHECKOVERDUE, MenuManager.EXITOVERDUEMENU);
            switch (select) {
                case MenuManager.CHECKOVERDUE:
                    break;
                case MenuManager.EXITOVERDUEMENU:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

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

    // <<기능 함수부>>
    // 초기함수
    public MemberManager(Connection connect,User user){
        try{
            this.conn = connect;
            this.stmt = this.conn.createStatement();
            this.currentUser= user;
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}


