import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminManager {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;

    //<<메뉴 함수>>
    // 회원정보 관리 메뉴 실행
// 회원 관리 메뉴 실행
    public void memberAdminProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.memberAdmin();  // 메뉴 출력
            int select = MenuManager.menuInput(MenuManager.CHECKMEMBERADMIN, MenuManager.EXITMEMBERADMIN);
            switch (select) {
                case MenuManager.CHECKMEMBERADMIN:
                    break;
                case MenuManager.UPDATEMEMBERADMIN:
                    break;
                case MenuManager.EXITMEMBERADMIN:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // 도서정보 관리 메뉴 실행
    public void bookAdminProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.bookAdmin();  // 메뉴 출력
            int select = MenuManager.menuInput(MenuManager.CHECKREQUEST, MenuManager.EXITBOOKADMIN);
            switch (select) {
                case MenuManager.CHECKREQUEST:
                    this.bookAdmin();
                    break;
                case MenuManager.BUYBOOK:
                    break;
                case MenuManager.EXITBOOKADMIN:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // 회원등급 메뉴 실행
    public void gradeAdminProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.rateAdmin();  // 메뉴 출력
            int select = MenuManager.menuInput(MenuManager.CHECKRANK, MenuManager.EXITGRADEADMIN);
            switch (select) {
                case MenuManager.CHECKRANK:
                    break;
                case MenuManager.UPDATERANK:
                    this.gradeAdmin();
                    break;
                case MenuManager.EXITGRADEADMIN:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // 연체대상자 메뉴 실행
    public void blackAdminProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.blackAdmin();  // 메뉴 출력
            int select = MenuManager.menuInput(MenuManager.CHECKOVERDUELIST, MenuManager.EXITBLACKADMIN);
            switch (select) {
                case MenuManager.CHECKOVERDUELIST:
                    break;
                case MenuManager.CHECKBLACKLIST:
                    break;
                case MenuManager.EXITBLACKADMIN:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // <<기능 함수부>>
    // 초기함수
    public AdminManager(Connection connect,User user) {
        try {
            this.conn = connect;
            this.stmt = this.conn.createStatement();
            this.currentUser = user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bookAdmin() {
        String requestquery = "SELECT * FROM requesttbl WHERE comrequest='n'";
        try {
            ResultSet rs = stmt.executeQuery(requestquery);
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
                        System.out.println("해당 책을 구매 신청하였습니다.");
                    } catch (SQLException e) {
                        System.out.println("책을 업데이트하는 도중 오류가 발생했습니다.");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("요청이 반려되었습니다.");
                }
            }
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 또는 쿼리 실행 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public void gradeAdmin() {
        // userDiffMap을 날짜와 대여 횟수 정보로 채운다.
        Map<String, GradeInfo> userDiffMap = datediff();
        String updatedatequery = "UPDATE usertbl SET usergrade = ? WHERE userid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updatedatequery)) {

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
        String datequery = "SELECT u.userid, u.username, u.userdate, u.usergrade, COUNT(r.userid) AS rent_count " +
                "FROM usertbl u " +
                "JOIN renttbl r ON u.userid = r.userid " +
                "GROUP BY u.userid, u.username, u.userdate, u.usergrade";

        DBConnect db = new DBConnect();
        db.initDBConnect();

        try (ResultSet rs = stmt.executeQuery(datequery)) {
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
}
