import java.sql.*;
import java.util.Scanner;

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
                case MenuManager.LASTRENTALSTATUS:
                    break;
                case MenuManager.RESERVEDBOOKSTATUS:
                    break;
                case MenuManager.REQUESTBOOKSTATUS:
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
                    updateMyInfo();
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
    public void overdueCheckProcess() throws SQLException {
        while (true) {
            boolean endFlag = false;
            MenuManager.overdueMenu();
            int select = MenuManager.menuInput(MenuManager.CHECKOVERDUE, MenuManager.EXITOVERDUEMENU);
            switch (select) {
                case MenuManager.CHECKOVERDUE:
                    System.out.println("===== 연체 도서 확인 =====");
                    myOverdue();
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
        SHLibraryManager sh_man=new SHLibraryManager();
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

    //나의 연체 상황을 확인하는 메서드
    public void myOverdue() throws SQLException {
        String overduequery="select r.userid, r.rentdate, r.duedate, r.turnin, b.title from renttbl r "+
                "join booktbl b on r.isbn=b.isbn "+
                "where userid=? ";

        DBConnect db=new DBConnect();
        db.initDBConnect();

        try(PreparedStatement pstmt=db.getConnection().prepareStatement(overduequery)){
            pstmt.setString(1, currentUser.getUserid());


            try(ResultSet rs=pstmt.executeQuery()) {
                //현재 연체중인지를 판별하는 boolean값
                boolean hasOverdue=false;
                while (rs.next()) {
                    String id = rs.getString("userid");
                    java.util.Date rentdate = rs.getDate("rentdate");
                    java.util.Date duedate = rs.getDate("duedate");
                    String title = rs.getString("title");
                    int turnin = rs.getInt("turnin");

                    java.util.Date today = new java.util.Date(System.currentTimeMillis());
                    long diffInMillies = today.getTime() - duedate.getTime();
                    long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

                    //현재 연체 중이라면 하단 문구 출력
                    if (turnin == 0 && diffInDays > 0) {
                        hasOverdue=true;
                        System.out.println(currentUser.getUsername() + "님이 대여하신 " + " [ "+title+" ] " + " 책이 연체된 지 " + diffInDays + "일 되었습니다.");
                    }
                }
                //현재 연체 중이 아니라면 하단 문구 출력
                if(!hasOverdue){
                    System.out.println("연체 중인 도서가 없습니다. ");
                }
            }
        }
    }

    //나의 정보 수정 메서드
    public void updateMyInfo(){
        Scanner input=new Scanner(System.in);
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

            DBConnect db=new DBConnect();
            db.initDBConnect();
            Connection conn=db.getConnection();
            try{
                PreparedStatement pstmt=conn.prepareStatement(updateinfoquery);
                pstmt.setString(1, new_id);
                pstmt.setString(2, new_pw);
                pstmt.setInt(3, new_age);
                pstmt.setString(4, new_interest);
                pstmt.setString(5, currentUser.getUserid());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.println("====== "+this.currentUser.getUsername()+"님의 정보가 변경되었습니다. =====");
        }else{
            System.out.println("비밀번호가 틀렸습니다. 다시 시도하세요.");
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


