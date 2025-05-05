import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminManager {
    //  관리자 메뉴-상위
    public final static int MEMBERADMIN=1;
    public final static int BOOKADMIN=2;
    public final static int GRADEADMIN=3;
    public final static int BLACKADMIN=4;
    public final static int EXITADMIN=5;
    //  회원 정보 메뉴
    public final static int CHECKMEMBERADMIN=1;
    public final static int UPDATEMEMBERADMIN=2;
    public final static int EXITMEMBERADMIN=3;
    //  도서정보 메뉴
    public final static int CHECKREQUEST=1;
    public final static int BUYBOOK=2;
    public final static int EXITBOOKADMIN=3;
    //  회원등급 메뉴
    public final static int CHECKRANK=1;
    public final static int UPDATERANK=2;
    public final static int EXITGRADEADMIN=3;
    // 연체대상자 메뉴
    public final static int CHECKOVERDUE=1;
    public final static int CHECKBLACKLIST=2;
    public final static int EXITBLACKADMIN=3;


    //  관리자 메뉴 출력-상위
    public static void AdminMenu(){
        System.out.println("1. 회원정보 관리");
        System.out.println("2. 도서정보 관리");
        System.out.println("3. 회원등급 관리");
        System.out.println("4. 연체자 정보 조회");
        System.out.println("5. 관리자모드 나가기");
    }
    //  회원정보 메뉴 출력
    public static void memberAdmin(){
        System.out.println("1. 회원정보 조회");
        System.out.println("2. 회원정보 수정");
        System.out.println("3. 회원정보 관리 나가기");
    }
    //  도서정보 메뉴 출력
    public static void bookadmin(){
        System.out.println("1. 신청도서 확인/처리");
        System.out.println("2. 도서 구매");
        System.out.println("3. 도서관리 나가기");
    }
    //  회원등급 메뉴 출력
    public static void rateAdmin(){
        System.out.println("1. 회원등급 확인");
        System.out.println("2. 회원등급 수정");
        System.out.println("3. 회원등급 나가기");
    }
    // 연체대상자 메뉴 출력
    public static void blackAdmin(){
        System.out.println("1. 연체대상자 확인");
        System.out.println("2. 장기연체자 확인");
        System.out.println("3. 연체대상자 메뉴 나가기");
    }

    //  회원정보 관리 메뉴 실행
    public static void MemberAdminProcess(){
        while(true){
            boolean endFlag=false;
            memberAdmin();
            int select=MenuManager.menuInput(CHECKMEMBERADMIN, EXITMEMBERADMIN);
            switch(select){
                case CHECKMEMBERADMIN:
                    break;
                case UPDATEMEMBERADMIN:
                    break;
                case EXITMEMBERADMIN:
                    endFlag=true;
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }

    //  도서정보 관리 메뉴 실행
    public static void BookAdminProcess(){
        while(true){
            boolean endFlag=false;
            bookadmin();
            int select=MenuManager.menuInput(CHECKREQUEST, EXITBOOKADMIN);
            switch(select){
                case CHECKREQUEST:
                    break;
                case BUYBOOK:
                    break;
                case EXITBOOKADMIN:
                    endFlag=true;
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }


    public void bookAdmin() throws SQLException {
        String requestquery = "select * from requesttbl where comrequest='n'";
        DBConnect db = new DBConnect();
        db.initDBConnect();

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(requestquery)) {

            Scanner input = new Scanner(System.in);

            while (rs.next()) {
                int num = rs.getInt("requestnum");
                String id = rs.getString("userid");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");

                System.out.println("----------------도서 요청 목록----------------");
                System.out.println(num + ". " + id + "님이 신청하신 책");
                System.out.println(title + " | " + author + " | " + publisher + " | ");
                System.out.println("책을 구입할까요? Y|N");
                String yn = input.nextLine();

                if (yn.equalsIgnoreCase("Y")) {
                    String updaterequestquery = "UPDATE requesttbl SET comrequest = 'y' WHERE requestnum = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updaterequestquery)) {
                        pstmt.setInt(1, num);
                        pstmt.executeUpdate();
                        System.out.println("책을 구매하여 책 목록에 추가하였습니다.");
                    }
                } else {
                    System.out.println("요청이 반려되었습니다.");
                }
            }
        }
    }


    public void gradeAdmin() throws SQLException {
        // userDiffMap을 날짜와 대여 횟수 정보로 채운다.
        Map<String, GradeInfo> userDiffMap = datediff();

        String updatedatequery = "UPDATE usertbl SET usergrade = ? WHERE userid = ?";
        DBConnect db = new DBConnect();
        db.initDBConnect();

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updatedatequery)) {

            // 회원 정보를 하나씩 처리
            for (Map.Entry<String, GradeInfo> entry : userDiffMap.entrySet()) {
                String id = entry.getKey();
                GradeInfo info = entry.getValue();
                long diffDays = info.getDiffDays();
                int rentcount = info.getRentCount();
                long diffMonth = diffDays / 30; // 가입한지 몇 개월 되었는지 계산

                // 등급 계산
                String grade;
                if (diffMonth >= 18 && rentcount >= 20) {
                    grade = "모범회원";
                } else if (diffMonth >= 12 && diffMonth < 18 && rentcount >= 15) {
                    grade = "우수회원";
                } else if (diffMonth >= 6 && diffMonth < 12 && rentcount >= 10) {
                    grade = "일반회원";
                } else {
                    grade = "신입회원";
                }

                // 등급을 업데이트하는 쿼리 실행
                pstmt.setString(1, grade);
                pstmt.setString(2, id);
                pstmt.executeUpdate();

                System.out.println(id + "님의 등급을 " + grade + "로 업데이트하였습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    public Map<String, GradeInfo> datediff() {
        Map<String, GradeInfo> userDaysMap = new HashMap<>();

        // 현재 날짜를 SQL 형식으로 받아옴
        java.util.Date utilDate = new java.util.Date();
        long currentSeconds = utilDate.getTime();
        java.sql.Date curDate = new java.sql.Date(currentSeconds);

        // usertbl에서 id, name, date, grade, 대여 횟수 가져오기
        String datequery = "SELECT u.userid, u.username, u.userdate, u.usergrade, COUNT(r.personid) AS rent_count " +
                "FROM usertbl u " +
                "JOIN renttbl r ON u.userid = r.personid " +
                "GROUP BY u.userid, u.username, u.userdate, u.usergrade";

        DBConnect db = new DBConnect();
        db.initDBConnect();

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(datequery)) {

            // 결과 처리
            while (rs.next()) {
                String id = rs.getString("userid");
                String name = rs.getString("username");
                Date userDate = rs.getDate("userdate");
                int rentcount = rs.getInt("rent_count"); // 유저별 대여 횟수

                // 가입일과 현재 날짜 차이 계산
                long diffSec = (curDate.getTime() - userDate.getTime()) / 1000;
                long diffDays = diffSec / (24 * 60 * 60);

                System.out.println(name + "님이 가입한 지 " + diffDays + "일 되었습니다.");
                userDaysMap.put(id, new GradeInfo(diffDays, rentcount)); // id와 (diffDays, rentCount)를 맵에 저장
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // 예외 발생 시 처리
        }
        return userDaysMap;
    }


//    public void blackAdmin(){
//
//    }

    //  회원등급 메뉴 실행
    public static void GradeAdminProcess(){
        while(true){
            boolean endFlag=false;
            rateAdmin();
            int select=MenuManager.menuInput(CHECKRANK, EXITGRADEADMIN);
            switch(select){
                case CHECKRANK:
                    break;
                case UPDATERANK:
                    break;
                case EXITGRADEADMIN:
                    endFlag=true;
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }
    //  연체대상자 메뉴 실행
    public static void BlackAdminProcess(){
        while(true){
            boolean endFlag=false;
            blackAdmin();
            int select=MenuManager.menuInput(CHECKOVERDUE, EXITBLACKADMIN);
            switch(select){
                case CHECKOVERDUE:
                    break;
                case CHECKBLACKLIST:
                    break;
                case EXITBLACKADMIN:
                    endFlag=true;
                    break;
            }
            if(endFlag){
                break;
            }
        }

    }

}
