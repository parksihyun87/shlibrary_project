import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class SHLibraryManager {
    // 멤버 변수
    DBConnect connect = new DBConnect();
    User currentUser = null;
    private Connection conn = null;
    private Statement stmt = null;
    LibraryManager lbM;
    AdminManager adM;
    MemberManager meM;

    //<<메뉴 함수>>
    // DB 시작 및 초기 메뉴 실행
    // 메인 메뉴 실행
    public void run() {
        connect.initDBConnect();
        try {
            this.conn = connect.getConnection();
            this.stmt = this.conn.createStatement();
            while (true) {
                boolean endFlag = false;
                MenuManager.initMenu();
                int select = MenuManager.menuInput(MenuManager.LOGIN, MenuManager.EXIT);
                switch (select) {
                    case MenuManager.LOGIN:
                        while (this.login()) {
                            lbM = new LibraryManager(conn, currentUser);
                            adM = new AdminManager(conn, currentUser);
                            meM = new MemberManager(conn, currentUser);
                            if (!this.mainMenuProcess()) {
                                break;
                            }
                        }
                        break;
                    case MenuManager.NEWMEMBER:
                        this.newMember();
                        break;
                    case MenuManager.EXIT:
                        endFlag = true;
                        System.out.println("감사합니다.");
                        connect.releaseDB();
                        break;
                }
                if (endFlag) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  메인 메뉴 실행
    public boolean mainMenuProcess() throws SQLException {
        while (true) {
            boolean endFlag = false;
            MenuManager.mainMenu();
            int select = MenuManager.menuInput(MenuManager.LIBRARY, MenuManager.LOGOUT);
            switch (select) {
                case MenuManager.LIBRARY:
                    this.libraryProcess();
                    break;
                case MenuManager.INFORM:
                    this.memberProcess();
                    break;
                case MenuManager.ADMIN:
                    if(currentUser.getUserid().equals("adm1")||currentUser.getUserid().equals("adm2")){
                        this.adminProcess();
                    }
                    System.out.println("관리자만 이용 가능합니다.");
                    break;
                case MenuManager.LOGOUT:
                    endFlag = true;
            }
            if (endFlag) {
                System.out.println("로그아웃됨.");
                currentUser = null;
                return false;
            }
        }
    }

    // 도서관 메뉴 실행
    public void libraryProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.libraryMenu();
            int select = MenuManager.menuInput(MenuManager.BOOKSEARCH, MenuManager.EXITLIBRARY);
            switch (select) {
                case MenuManager.BOOKSEARCH:
                    lbM.bookSearchProcess();
                    break;
                case MenuManager.BESTSELLER:
                    lbM.bestsellerProcess();
                    break;
                case MenuManager.BOOKEDBOOK:
                    //예약도서 대출 넣기.
                    break;
                case MenuManager.BOOKRETURN:
                    lbM.bookReturnProcess();
                    break;
                case MenuManager.BOOKREQUEST:
                    lbM.bookRequestProcess();
                    break;
                case MenuManager.EXITLIBRARY:
                    endFlag = true;
                    break;
            }
            if (endFlag) break;
        }
    }

    // 회원 메뉴 실행
    public void memberProcess() throws SQLException {
        while (true) {
            boolean endFlag = false;
            MenuManager.memberMenu();
            int select = MenuManager.menuInput(MenuManager.MYBOOK, MenuManager.EXITMEMBER);
            switch (select) {
                case MenuManager.MYBOOK:
                    meM.rentalStatusProcess();
                    break;
                case MenuManager.MYINFO:
                    meM.myInfoProcess();
                    break;
                case MenuManager.DELAY:
                    meM.overdueCheckProcess();
                    break;
                case MenuManager.EXITMEMBER:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // 관리자 메뉴 실행
    public void adminProcess() throws SQLException {
        while (true) {
            boolean endFlag = false;
            MenuManager.AdminMenu();  // 메뉴 출력
            int select = MenuManager.menuInput(MenuManager.MEMBERADMIN, MenuManager.EXITADMIN);
            switch (select) {
                case MenuManager.MEMBERADMIN:
                    adM.memberAdminProcess();
                    break;
                case MenuManager.BOOKADMIN:
                    adM.bookAdminProcess();
                    break;
                case MenuManager.GRADEADMIN:
                    adM.gradeAdminProcess();
                    break;
                case MenuManager.BLACKADMIN:
                    adM.blackAdminProcess();
                    break;
                case MenuManager.EXITADMIN:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    //<<기능 함수 부>>
//<회원가입 및 로그인>
// 회원가입
    public void newMember() {
        User user = null;
        Scanner input = new Scanner(System.in);
        System.out.println("회원정보를 입력해 주세요.");
        System.out.print("ID: ");
        String id = input.nextLine();
        System.out.print("PW: ");
        String pw = input.nextLine();
        System.out.print("이름: ");
        String name = input.nextLine();
        System.out.print("나이: ");
        int age = input.nextInt();
        System.out.println("관심사를 골라주세요. \n(0.총류, 1.철학, 2.종교, 3.사회과학, 4.자연과학, 5.기술과학, 6.예술, 7.언어, 8.문학, 9.역사) ");
        int userinterestNum = input.nextInt();
        String userinterest = switch (userinterestNum) {
            case 0 -> "총류";
            case 1 -> "철학";
            case 2 -> "종교";
            case 3 -> "사회과학";
            case 4 -> "자연과학";
            case 5 -> "기술과학";
            case 6 -> "예술";
            case 7 -> "언어";
            case 8 -> "문학";
            case 9 -> "역사";
            default -> null;
        };
        user = new User(id, pw, name, null, age, userinterest, null);
        inputUser(user);
        System.out.println("회원가입이 완료되었습니다.");
    }
    // 사용자 정보 입력
    public void inputUser(User user) {
        String sql = "insert into usertbl values(?,?,?,null,?,?,null)";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserid());
            pstmt.setString(2, user.getUserpw());
            pstmt.setString(3, user.getUsername());
            pstmt.setInt(4, user.getUserage());
            pstmt.setString(5, user.getUserinterest());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // 로그인 메뉴 실행
    public boolean login() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.print("ID: ");
        String id = input.nextLine();
        System.out.print("PW: ");
        String pw = input.nextLine();

        User user = selectUser(id);
        if (user == null) {
            System.out.println("존재하지 않는 사용자 입니다.");
            return false;
        }
        if (user.getUserpw().equals(pw)) {
            System.out.println(user.getUsername() + "님 환영합니다!");
            this.currentUser = user;
            overdue();
            return true;
        } else {
            System.out.println("비밀번호를 다시 확인해 주세요.");
            return false;
        }
    }

    public void overdue() throws SQLException {
        String overduequery="select r.userid, r.rentdate, r.duedate, r.turnin, b.title from renttbl r "+
                "join booktbl b on r.isbn=b.isbn "+
                "where userid=? ";

        DBConnect db=new DBConnect();
        db.initDBConnect();

        try(PreparedStatement pstmt=db.getConnection().prepareStatement(overduequery)){
            pstmt.setString(1, currentUser.getUserid());


            try(ResultSet rs=pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("userid");
                    Date rentdate = rs.getDate("rentdate");
                    Date duedate = rs.getDate("duedate");
                    String title = rs.getString("title");
                    int turnin = rs.getInt("turnin");

                    Date today = new Date(System.currentTimeMillis());
                    long diffInMillies = today.getTime() - duedate.getTime();
                    long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

                    if (turnin == 0 && diffInDays > 0) {
                        System.out.println(currentUser.getUsername() + "님이 대여하신 " + " [ "+title+" ] " + " 책이 연체된 지 " + diffInDays + "일 되었습니다.");
                    }
                }
            }
        }
    }

    // 사용자 정보 조회
    public User selectUser(String pUserid) {
        String sql = "select * from usertbl where userid = ?";
        User user = null;
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, pUserid);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userid = rs.getString("userid");
                String userpw = rs.getString("userpw");
                String username = rs.getString("username");
                Date userdate = rs.getDate("userdate");
                int userage = rs.getInt("userage");
                String userinterest = rs.getString("userinterest");
                String usergrade = rs.getString("usergrade");
                user = new User(userid,userpw,username,userdate,userage,userinterest,usergrade);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}// 클래스 끝
