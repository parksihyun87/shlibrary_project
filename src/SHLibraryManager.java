import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class SHLibraryManager {
    DBConnect connect = new DBConnect();

    User currentUser = null;


    public void run(){
        connect.initDBConnect();

        while(true){
            boolean endFlag=false;
            MenuManager.initMenu();
            int select=MenuManager.menuInput(MenuManager.LOGIN,MenuManager.EXIT);
            switch(select){
                case MenuManager.LOGIN:
                    while (this.login()) {
                        if (!this.MainMenuProcess()) {
                            break;
                        }
                    }
                    break;
                case MenuManager.NEWMEMBER:
                    this.newMember();
                    break;
                case MenuManager.EXIT:
                    endFlag=true;
                    System.out.println("감사합니다.");
                    connect.releaseDB(); //변경
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }
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
            Connection conn = connect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
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
    public boolean login() {
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
            return true;
        } else {
            System.out.println("비밀번호를 다시 확인해 주세요.");
            return false;
        }
    }
    // 사용자 정보 조회
    public User selectUser(String pUserid) {
        String sql = "select * from usertbl where userid = ?";
        User user = null;
        try {
            Connection conn = connect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
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

    //  메인 메뉴 실행
    public boolean MainMenuProcess(){
        while(true){
            boolean endFlag=false;
            MenuManager.mainMenu();
            int select=MenuManager.menuInput(MenuManager.LIBRARY, MenuManager.LOGOUT);
            switch(select){
                case MenuManager.LIBRARY:
                    this.LibraryProcess();
                    break;
                case MenuManager.MYINFO:
                    this.MemberProcess();
                    break;
                case MenuManager.ADMIN:
                    this.AdminProcess();
                    break;
                case MenuManager.LOGOUT:
                    endFlag=true;
            }
            if(endFlag){
                System.out.println("로그아웃됨.");
                currentUser = null;
                return false;
            }
        }
    }
    //  도서관 메뉴 실행
    public void LibraryProcess(){
        while(true) {
            boolean endFlag = false;
            LibraryManager.libraryMenu();
            int select = MenuManager.menuInput(LibraryManager.BOOKSEARCH,LibraryManager.EXITLIBRARY);
            switch (select) {
                case LibraryManager.BOOKSEARCH:
                    LibraryManager.BookSearchProcess();
                    break;
                case LibraryManager.BESTSELLER:
                    LibraryManager.BestsellerProcess();
                    break;
                case LibraryManager.INTEREST:
                    LibraryManager.InterestCategoryProcess();
                    break;
                case LibraryManager.BOOKRETURN:
                    LibraryManager.BookReturnProcess();
                    break;
                case LibraryManager.BOOKREQUEST:
                    LibraryManager.BookRequestProcess();
                    break;
                case LibraryManager.EXITLIBRARY:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    //  회원 메뉴 실행
    public void MemberProcess(){
        while(true) {
            boolean endFlag = false;
            MemberManager.memberMenu();
            int select = MenuManager.menuInput(MemberManager.MYBOOK, MemberManager.EXITMEMBER);
            switch (select) {
                case MemberManager.MYBOOK:
                    MemberManager.RentalStatusProcess();
                    break;
                case MemberManager.MYINFO:
                    MemberManager.MyInfoProcess();
                    break;
                case MemberManager.DELAY:
                    MemberManager.OverdueCheckProcess();
                    break;
                case MemberManager.EXITMEMBER:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    //  관리자 메뉴 실행
    public void AdminProcess(){
        while(true){
            boolean endFlag=false;
            AdminManager.AdminMenu();
            int select=MenuManager.menuInput(AdminManager.MEMBERADMIN, AdminManager.EXITADMIN);
            switch(select){
                case AdminManager.MEMBERADMIN :
                    AdminManager.MemberAdminProcess();
                    break;
                case AdminManager.BOOKADMIN:
                    AdminManager.BookAdminProcess();
                    break;
                case AdminManager.GRADEADMIN:
                    AdminManager.GradeAdminProcess();
                    break;
                case AdminManager.BLACKADMIN:
                    AdminManager.BlackAdminProcess();
                    break;
                case AdminManager.EXITADMIN:
                    endFlag=true;
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }
}
