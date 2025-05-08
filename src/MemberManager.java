import java.sql.*;
import java.util.Scanner;

public class MemberManager {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;
    Scanner input = new Scanner(System.in);
    DBConnect db = new DBConnect();

    // 현재 대여 현황 메뉴 실행
    public void rentalStatusProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.rentalStatus();
            int select = MenuManager.menuInput(MenuManager.CURRENTRENTALSTATUS, MenuManager.EXITRENTALSTATUS);
            switch (select) {
                case MenuManager.CURRENTRENTALSTATUS:
                    currentRentalStatus();
                    break;
                case MenuManager.LASTRENTALSTATUS:
                    pastRentalStatus();
                    break;
                case MenuManager.RESERVEDBOOKSTATUS:
                    reservedBookStatus();
                    break;
                case MenuManager.REQUESTBOOKSTATUS:
                    requestBookStatus();
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

    // 나만의 추천 책 메뉴 실행
    public void checkMyOwnBookMenu() throws SQLException {
        DBConnect db=new DBConnect();
        db.initDBConnect();
        while (true) {
            boolean endFlag = false;
            MenuManager.checkMyOwnBookMenu();
            int select = MenuManager.menuInput(MenuManager.CHECKMYOWNBOOK, MenuManager.EXITCHECKMYOWNBOOK);
            switch (select) {
                case MenuManager.CHECKMYOWNBOOK:
                    String sql = "SELECT b.title, b.author, b.publisher, b.callnum, b.rentnum " +
                                    "from usertbl u"+
                                    "RIGHT OUTER JOIN renttbl r on u.userid = r.userid"+
                                    "inner join booktbl b on r.isbn = b.isbn";
                    try (Connection conn = db.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, currentUser.getUserinterest());
                        ResultSet rs = pstmt.executeQuery();

                        System.out.println("\n[내 관심 분야 인기 도서 목록]");
                        boolean hasResult = false;

                        // 결과 출력
                        while (rs.next()) {
                            hasResult = true;
                            String title = rs.getString("title");
                            String author = rs.getString("author");
                            String publisher = rs.getString("publisher");
                            String callnum = rs.getString("callnum");
                            int rentnum = rs.getInt("rentnum");

                            System.out.printf("제목: %s | 저자: %s | 출판사: %s | 분야: %s | 대여 횟수: %d\n",
                                    title, author, publisher, callnum, rentnum);
                        }

                        // 결과가 없을 경우 처리
                        if (!hasResult) {
                            System.out.println("관심 분야에 해당하는 책이 없습니다.");
                        }

                    } catch (SQLException e) {
                        System.out.println("도서 조회 중 오류 발생: " + e.getMessage());
                    }
                    break;
                case MenuManager.EXITCHECKMYOWNBOOK:
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


    // ↓↓ 희용추가(0508)
    // 나의 대여 현황 > 현재 대여 도서 보기
    public void currentRentalStatus() {
        db.initDBConnect();
        String sql = "select * from usertbl u " +
                "inner join renttbl r on u.userid = r.userid " +
                "inner join booktbl b on r.isbn = b.isbn " +
                "where r.turnin = 0 and u.userid = ? " +
                "order by r.rentdate desc";
        System.out.println(currentUser.getUsername() + " 님의 현재 대여 도서 목록");
        System.out.printf("%-15s %-25s %-10s %-15s %-15s %-5s %n", "ISBN", "도서명", "작가", "대출일", "반납기한", "연장");
        System.out.println("-".repeat(100));
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, currentUser.getUserid());
            ResultSet rs = pstmt.executeQuery();
            boolean isFound = false;
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                Date rentdate = rs.getDate("rentdate");
                Date duedate = rs.getDate("duedate");
                boolean prolong = rs.getBoolean("prolong");
                System.out.printf("%-10d %-27s %-10s %-18s %-15s %-5s %n", isbn, title, author, rentdate, duedate, prolong);
                isFound = true;
            }
            if (!isFound) {
                System.out.println("현재 대여중인 도서가 없습니다.");
            }
            System.out.println("-".repeat(100));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 나의 대여 현황 > 지난 대여 도서 보기
    public void pastRentalStatus() {
        db.initDBConnect();
        String sql = "select * from usertbl u " +
                "inner join renttbl r on u.userid = r.userid " +
                "inner join booktbl b on r.isbn = b.isbn " +
                "where r.turnin = 1 and u.userid = ? " +
                "order by r.rentdate desc";
        System.out.println(currentUser.getUsername() + " 님의 지난 도서대여 기록");
        System.out.printf("%-15s %-37s %-15s %-15s %-15s %n", "ISBN", "도서명", "작가", "대출일", "반납일");
        System.out.println("-".repeat(110));
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, currentUser.getUserid());
            ResultSet rs = pstmt.executeQuery();
            boolean isFound = false;
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                Date rentdate = rs.getDate("rentdate");
                Date turnindate = rs.getDate("turnindate");
                System.out.printf("%-10d %-40s %-15s %-18s %-15s %n", isbn, title, author, rentdate, turnindate);
                isFound = true;
            }
            if (!isFound) {
                System.out.println("지난 도서대여 기록이 없습니다.");
            }
            System.out.println("-".repeat(110));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 나의 대여 현황 > 나의 예약 도서 보기
    public void reservedBookStatus(){
        db.initDBConnect();
        String sql = "select * from reservetbl rs " +
                "inner join booktbl b on rs.isbn = b.isbn " +
                "where rs.userid = ? " +
                "order by reservetdate desc";
        System.out.println(currentUser.getUsername() + "님의 예약도서 현황");
        System.out.printf("%-15s %-25s %-10s %-15s %-10s %n", "ISBN", "도서명", "작가", "예약일", "현재상태");
        System.out.println("-".repeat(90));
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, currentUser.getUserid());
            ResultSet rs = pstmt.executeQuery();
            boolean isFound = false;
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                Date reservetdate = rs.getDate("reservetdate");
                String reservestatus = rs.getString("reservestatus");
                System.out.printf("%-10d %-27s %-10s %-18s %-10s %n", isbn, title, author, reservetdate, reservestatus);
                isFound = true;
            }
            if (!isFound) {
                System.out.println("에약도서 정보가 없습니다.");
            }
            System.out.println("-".repeat(90));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 나의 대여 현황 > 나의 신청 도서 보기
    public void requestBookStatus() {
        db.initDBConnect();
        String sql = "select * from requesttbl where userid = ? order by requestnum desc";
        System.out.println(currentUser.getUsername() + "님의 신청도서 현황");
        System.out.printf("%-37s %-15s %-15s %-15s %-15s %n", "도서명", "작가", "출판사", "발행년", "승인여부");
        System.out.println("-".repeat(110));
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, currentUser.getUserid());
            ResultSet rs = pstmt.executeQuery();
            boolean isFound = false;
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int pubyear = rs.getInt("pubyear");
                String comrequest = rs.getString("comrequest");
                System.out.printf("%-37s %-13s %-15s %-20d %-10s %n", title, author, publisher, pubyear, comrequest);
                isFound = true;
            }
            if (!isFound) {
                System.out.println("신청도서 정보가 없습니다.");
            }
            System.out.println("-".repeat(110));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // ↑↑ 희용추가(0508) 끝


}


