import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;

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
                    lbM.rentBookedBook();
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
                case MenuManager.OWNSERVICE:
                    meM.checkMyOwnBookMenu();
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
        String id;
        String pw;
        String name;
        int age;
        String userinterest;

        System.out.println("가입할 회원정보를 입력해 주세요.");
        // ID
        while (true) {
            System.out.print("ID: ");
            id = input.nextLine().trim();
            if (id.contains(" ")) {
                System.out.println("ID에 공백을 포함할 수 없습니다.");
                continue;
            } else if (!id.matches("^(?=.*[A-Za-z])[A-Za-z0-9]+$")) {
                System.out.println("ID는 영문자를 반드시 포함하고 있어야 하며, 특수문자와 한글은 들어갈수 없습니다.");
                continue;
            } else if (id.length() > 10 || id.length() < 4) {
                System.out.println("ID는 4자 이상 10자 이하여야 합니다.");
                continue;
            } else if(isExistUserid(id)) {
                System.out.println("중복된 ID가 있습니다. 다른 ID를 입력해 주세요.");
                continue;
            }
            System.out.println("사용가능한 ID 입니다.");
            break;
        }
        // PW
        while (true) {
            System.out.print("PW: ");
            pw = input.nextLine().trim();
            if (pw.contains(" ")) {
                System.out.println("비밀번호에 공백을 포함할 수 없습니다.");
                continue;
            }else if (!pw.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,10}$")) {
                System.out.println("비밀번호는 영문, 숫자를 포함하고 4자 이상 10자 이하여야 합니다.");
                continue;
            }
            System.out.println("사용가능한 비밀번호 입니다.");
            break;
        }
        //이름
        while (true) {
            System.out.print("이름: ");
            name = input.nextLine().trim();
            if (name.length() > 10) {
                System.out.println("이름은 10자를 초과할 수 없습니다.");
                continue;
            } else if (name.length() <= 2) {
                System.out.println("이름을 2자이상 입력해 주세요.");
                continue;
            }
            break;
        }
        // 나이
        while (true) {
            System.out.print("나이: ");
            try {
                age = input.nextInt();
                input.nextLine();
                if (age < 0 || age > 100) {
                    System.out.println("나이를 정확히 입력해 주세요.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("나이는 숫자로 입력해주세요.");
                input.nextLine();
            }
        }
        // 관심사
        while (true) {
            System.out.println("관심사를 골라주세요. \n(0.총류, 1.철학, 2.종교, 3.사회과학, 4.자연과학, 5.기술과학, 6.예술, 7.언어, 8.문학, 9.역사) ");
            try {
                int userinterestNum = input.nextInt();
                input.nextLine();
                if (userinterestNum >= 0 && userinterestNum < 10){
                    userinterest = switch (userinterestNum) {
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
                    break;
                } else {
                    System.out.println("잘못된 입력입니다.");
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력 가능합니다.");
                input.nextLine();
            }
        }
        System.out.printf("ID: %s | PW: %s | 이름: %s | 나이: %d | 관심사: %s\n", id, pw, name, age, userinterest);
        System.out.print("위 정보로 가입하시겠습니까? (Y/N) ");
        char permit = input.nextLine().trim().toUpperCase().charAt(0);
        if (permit == 'Y') {
            user = new User(id, pw, name, null, age, userinterest, null);
            inputUser(user);
            System.out.println("회원가입이 완료되었습니다.");
        } else {
            System.out.println("회원가입이 취소되었습니다.");
        }
    }
    public boolean isExistUserid(String userid) { // userid 중복 체크
        String sql = "select 1 from usertbl where userid = ?";
        connect.initDBConnect();
        try (
                Connection conn = connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, userid);
            try (ResultSet rs = pstmt.executeQuery()){
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
            System.out.println("DB오류");
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
            booked();
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

    public void booked() {
        String sql = """
        select * from reservetbl r join booktbl b on b.isbn= r.isbn
        where r.userid=? and r.reservestatus='예약대기' and r.reserverank=0
    """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currentUser.getUserid());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date turninDate = null;
                    int thisIsbn = rs.getInt("isbn");
                    String bookTitle = rs.getString("title");
                    String author = rs.getString("author");

                    System.out.println("예약도서가 도착하였으며,");
                    System.out.print(" 제목: " + bookTitle);
                    System.out.print(", 저자: " + author + " 입니다.");
                    System.out.println();

                    String sql2 = """
                    select rt.turnindate
                    from reservetbl r
                    join booktbl b on b.isbn = r.isbn
                    left join renttbl rt on rt.isbn = r.isbn and rt.turnin = 1
                    where r.userid = ? and b.isbn = ? and r.reservestatus = '예약대기' and r.reserverank = 0
                    order by turnindate desc limit 1;
                """;
                    try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                        pstmt2.setString(1, currentUser.getUserid());
                        pstmt2.setInt(2, thisIsbn);

                        try (ResultSet rs2 = pstmt2.executeQuery()) {
                            if (rs2.next()) {
                                turninDate = rs2.getDate("turnindate");
                            }
                        }
                    }
                    if (turninDate != null) {
                        LocalDate turninLocal = turninDate.toLocalDate();
                        LocalDate today = LocalDate.now();
                        long diffInDays = ChronoUnit.DAYS.between(turninLocal, today);

                        System.out.println("도착일자: " + turninLocal +" 이며, 3일내 미수령시 예약취소됩니다.");

                        if (diffInDays > 2) {
                            String sql3 = """
                            update reservetbl
                            set reservestatus='예약취소', reserverank=null
                            where userid=? and isbn=? and reservestatus='예약대기' and reserverank=0
                        """;

                            try (PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {
                                pstmt3.setString(1, currentUser.getUserid());
                                pstmt3.setInt(2, thisIsbn);
                                pstmt3.executeUpdate();
                            }
                            String sql4= "update reservetbl set reserverank = reserverank-1 where isbn=?";
                            PreparedStatement pstmt4= conn.prepareStatement(sql4);
                            pstmt4.setInt(1,thisIsbn);
                            pstmt4.executeUpdate();
                        }
                    } else {
                        System.out.println("turninDate가 null입니다.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
