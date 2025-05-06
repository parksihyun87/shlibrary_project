import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.*;
import java.util.Date;

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
    public final static int EXITBOOKADMIN=2;
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
        System.out.println("2. 도서관리 나가기");
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
    public static void BookAdminProcess() throws SQLException {
        Scanner input=new Scanner(System.in);
        while(true){
            boolean endFlag=false;
            bookadmin();
            int select=MenuManager.menuInput(CHECKREQUEST, EXITBOOKADMIN);

            switch(select){
                case CHECKREQUEST:
                    System.out.println("1. 승인 대기중인 목록");
                    System.out.println("2. 승인 완료된 목록");
                    System.out.println("3. 승인 반려된 목록");
                    System.out.println("4. 도서 신청 목록 나가기");
                    int subSelect=MenuManager.menuInput(1, 4);

                    switch(subSelect){
                        case 1:
                            List<Request> requestList=getRequestListByStatus("N");
                            for(Request req:requestList){
                                System.out.println(req);
                                System.out.println("이 책을 구매하시겠습니까? : Y|R");
                                String yn=input.nextLine().toUpperCase();

                                if(yn.equals("Y")){
                                    System.out.println("도서의 Call Number를 입력하세요. ");
                                    String callnum=input.nextLine();
                                    approveBookRequest(req.getRequestnum(), callnum);
                                    System.out.println("도서 구매 및 목록 등록 완료");
                                }else if(yn.equals("R")){
                                    rejectBookRequest(req.getRequestnum());
                                    System.out.println("도서 구매 신청이 반려되었습니다.");
                                }else{
                                    System.out.println("잘못된 입력입니다.");
                                }
                            }
                            break;

                        case 2:
                            List<Request> approvedList=getRequestListByStatus("Y");
                            System.out.println("승인 완료 신청 목록 : ");
                            for(Request req:approvedList){
                                System.out.println(req);
                            }
                            break;

                        case 3:
                            List<Request> rejectedList=getRequestListByStatus("R");
                            System.out.println("반려된 신청 목록 : ");
                            for(Request req:rejectedList){
                                System.out.println(req);
                            }
                            break;

                        case 4:
                            System.out.println("도서 신청 목록 나가기");
                            break;
                    }
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

    //입력값(N, Y, R)에 따라 도서 신청 목록을 확인할 수 있는 메서드
    public static List<Request> getRequestListByStatus(String status) throws SQLException {
        List<Request> list=new ArrayList<>();
        String query="select * from requesttbl where comrequest=?";
        DBConnect db=new DBConnect();
        db.initDBConnect();

        try(Connection conn=db.getConnection();
            PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setString(1, status);
            ResultSet rs=pstmt.executeQuery();

            while(rs.next()){
                int num=rs.getInt("requestnum");
                String id=rs.getString("userid");
                String title=rs.getString("title");
                String author=rs.getString("author");
                String publisher=rs.getString("publisher");

                list.add(new Request(num, id, title, author, publisher, status));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    //신청 도서 승인 메서드
    public static void approveBookRequest(int requestnum, String callnum){
        DBConnect db=new DBConnect();
        db.initDBConnect();

        String selectQuery="select * from requesttbl where requestnum=?";
        String insertBookQuery="insert into booktbl (isbn, title, author, publisher, pubyear, callnum, rentnum) values (?, ?, ?, ?, ?, ?, 0)";
        String updateRequestQuery="update requesttbl set comrequest='Y' where requestnum=?";

        try(Connection conn=db.getConnection();
            PreparedStatement pstmt=conn.prepareStatement(selectQuery)){
            pstmt.setInt(1, requestnum);
            ResultSet rs=pstmt.executeQuery();

            if(rs.next()){
                String title=rs.getString("title");
                String author=rs.getString("author");
                String publisher=rs.getString("publisher");
                int pubyear=rs.getInt("pubyear");
                int isbn=requestnum+3000;

                try(PreparedStatement insertPstmt=conn.prepareStatement(insertBookQuery);
                    PreparedStatement updatePstmt=conn.prepareStatement(updateRequestQuery)){

                    insertPstmt.setInt(1, isbn);
                    insertPstmt.setString(2, title);
                    insertPstmt.setString(3, author);
                    insertPstmt.setString(4, publisher);
                    insertPstmt.setInt(5, pubyear);
                    insertPstmt.setString(6, callnum);
                    insertPstmt.executeUpdate();

                    updatePstmt.setInt(1, requestnum);
                    updatePstmt.executeUpdate();
                    System.out.println("도서 구매가 승인되어 도서 목록에 추가되었습니다. ");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rejectBookRequest(int requestnum){
        String updateQuery="update requesttbl set comrequest='r' where requestnum=?";
        DBConnect db=new DBConnect();
        db.initDBConnect();

        try(Connection conn=db.getConnection();
            PreparedStatement pstmt=conn.prepareStatement(updateQuery)){
            pstmt.setInt(1, requestnum);
            pstmt.executeUpdate();
            System.out.println("도서 신청이 반려 처리되었습니다. ");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void gradeAdmin() throws SQLException {
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

    public static Map<String, GradeInfo> datediff() {
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

                //System.out.println(name + "님이 가입한 지 " + diffDays + "일 되었습니다.");
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
                //회원 등급 정보 확인
                case CHECKRANK:
                    System.out.println("===== 회원 등급 정보 =====");
                    String rankcheckquery="SELECT userid, username, usergrade FROM usertbl";
                    DBConnect db=new DBConnect();
                    db.initDBConnect();

                    try(Connection conn=db.getConnection();
                        Statement stmt=conn.createStatement();
                        ResultSet rs=stmt.executeQuery(rankcheckquery)) {

                        while(rs.next()){
                            String id=rs.getString("userid");
                            String name=rs.getString("username");
                            String grade=rs.getString("usergrade");
                            System.out.println(id+" | "+name+" | 등급 : "+grade);
                        }
                    } catch (SQLException e) {
                        System.out.println("회원 정보 조회 중 오류 발생 : "+e.getMessage());
                    }
                    break;
                //회원 등급 정보 갱신
                case UPDATERANK:
                    try{
                        gradeAdmin();
                        System.out.println("회원 등급이 성공적으로 갱신되었습니다. ");
                    }catch(SQLException e){
                        System.out.println("등급 갱신 중 오류 발생 : "+e.getMessage());
                    }
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
