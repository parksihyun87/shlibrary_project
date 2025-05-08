import java.sql.*;
import java.util.*;
import java.util.Date;

public class AdminManager {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;
    private Scanner input=new Scanner(System.in);

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
                    this.checkMember();
                    break;
                case MenuManager.UPDATEMEMBERADMIN:
                    this.updateMemberInfo();
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
    public void bookAdminProcess() throws SQLException {
        while (true) {
            boolean endFlag = false;
              // 메뉴 출력
            int select = MenuManager.menuInput(MenuManager.CHECKREQUEST, MenuManager.EXITBOOKADMIN);
            switch (select) {
                case MenuManager.CHECKREQUEST:
                    this.BookRequestMenu();

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
                    this.blackAdmin();
                    break;
                case MenuManager.CHECKBLACKLIST:
                    this.longBlackAdmin();
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

    public void BookRequestMenu() throws SQLException{
        while(true){
            boolean endFlag=false;
            bookAdmin();
            int select=MenuManager.menuInput(MenuManager.CHECKREQUEST, MenuManager.EXITBOOKADMIN);

            switch(select){
                case MenuManager.CHECKREQUEST :
                    System.out.println("1. 승인 대기중인 목록");
                    System.out.println("2. 승인 완료된 목록");
                    System.out.println("3. 승인 반려된 목록");
                    System.out.println("4. 도서 신청 목록 나가기");
                    int reselect=0;
                    while(true){
                        System.out.println("메뉴를 선택하세요. ");
                        if(input.hasNextInt()){
                            select=input.nextInt();
                            input.nextLine();

                            if(select>=1&&select<=4){
                                break;
                            }else{
                                System.out.println("잘못된 입력입니다.");
                            }
                        }else{
                            input.nextLine();
                            System.out.println("숫자를 입력해주세요. ");
                        }
                    }

                    switch(reselect){
                        case 1:

                            List<Request> requestList=getRequestListByStatus("n");
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
                            List<Request> approvedList=getRequestListByStatus("y");
                            System.out.println("승인 완료 신청 목록 : ");
                            for(Request req:approvedList){
                                System.out.println(req);
                            }
                            break;

                        case 3:
                            List<Request> rejectedList=getRequestListByStatus("r");
                            System.out.println("반려된 신청 목록 : ");
                            for(Request req:rejectedList){
                                System.out.println(req);
                            }
                            break;

                        case 4:
                            System.out.println("도서 신청 목록 나가기");
                            endFlag=true;
                            break;
                    }
                    if(endFlag){
                        break;
                    }
            }
        }
    }

    //도서 신청 목록 확인 함수부 Start
    public static List<Request> getRequestListByStatus(String status) throws SQLException{
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

    public void approveBookRequest(int requestnum, String callnum){
        DBConnect db=new DBConnect();
        db.initDBConnect();

        String selectQuery="select * from requesttbl where requestnum=?";
        String insertBookQuery="insert into booktbl (isbn, title, author, publisher, pubyear, callnum, rentnum) values (?, ?, ?, ?, ?, ?, 0)";
        String updateRequestQuery="update requesttbl set comrequest='y' where requestnum=?";

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
            throw new RuntimeException(e);
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

    public void bookAdmin() {
        String requestquery = "SELECT * FROM requesttbl WHERE comrequest='n'";
        Scanner input=new Scanner(System.in);
        try {
            ResultSet rs = stmt.executeQuery(requestquery);

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
        }finally{
            input.close();
        }
    }
    //도서 신청 확인 메서드 end

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
                if(isLongTermOverdue(id)){
                    grade="장기연체자";
                }else{
                    if (diffMonth >= 18 && rentcount >= 20) {
                        grade = "모범회원";
                    } else if (diffMonth >= 12 && diffMonth < 18 && rentcount >= 15) {
                        grade = "우수회원";
                    } else if (diffMonth >= 6 && diffMonth < 12 && rentcount >= 10) {
                        grade = "일반회원";
                    } else {
                        grade = "신입회원";
                    }
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

    public boolean isLongTermOverdue(String userid) throws SQLException{
        String query="select 1 from renttbl "+
                "where userid=? "+
                "and datediff(turnindate, duedate)>=30 "+
                "limit 1";
        DBConnect db=new DBConnect();
        db.initDBConnect();

        try(Connection conn=db.getConnection();
            PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setString(1, userid);
            ResultSet rs=pstmt.executeQuery();
            return rs.next();
        }
    }
//    <<연체자 함수부>>
    public void blackAdmin(){
        System.out.println("현재 연체자 목록");
        Map<String,Overdue> notReturnedList=overdue();
            for(Map.Entry<String,Overdue> entry: notReturnedList.entrySet()){
                String userid = entry.getKey();
                Overdue overdue = entry.getValue();
                printOverdue(userid,overdue);
            }
    }

    public void longBlackAdmin(){
        System.out.println("현재 연체자 목록");
        Map<String,Overdue> notReturnedList=overdue();
        for(Map.Entry<String,Overdue> entry: notReturnedList.entrySet()){
            String userid = entry.getKey();
            Overdue overdue = entry.getValue();
            if(overdue.getDiffdays()>30) {
                printOverdue(userid,overdue);
            }
        }
    }

    public void printOverdue(String userid, Overdue overdue){
        String name = overdue.getName();
        int isbn = overdue.getIsbn();
        String title = overdue.getTitle();
        java.sql.Date duedate = overdue.getdueDate();
        int diffdays = overdue.getDiffdays();

        System.out.print(" 대출자id: " + userid);
        System.out.print(", 대출자 이름: " + name);
        System.out.print(", 책 isbn: " + isbn);
        System.out.print(", 책 제목: " + title);
        System.out.print(", 반납기한: " + duedate);
        System.out.print(", 연체일자: " + diffdays);
        System.out.println();
    }

    public Map<String, Overdue> overdue() {
        Map<String, Overdue> notReturnedList = new HashMap<>();
        try {
            String sql = """
                select r.isbn, b.title, r.duedate, u.userid, u.username, (curdate()-duedate) as datediffdays
                from renttbl r
                join usertbl u
                on u.userid=r.userid
                join booktbl b
                on r.isbn=b.isbn
                where r.turnin=0 and (curdate()-duedate)>0
                """;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String userid= rs.getString("userid");
                String name= rs.getString("username");
                int isbn= rs.getInt("isbn");
                String title= rs.getString("title");
                java.sql.Date dueDate= rs.getDate("duedate");
                int datediffdays = rs.getInt("datediffdays");

                notReturnedList.put(userid,new Overdue(name, isbn, title, dueDate, datediffdays));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notReturnedList;
    }
//<<유저 정보 업데이트부>>
    public void printAllmemeber(){
        System.out.println("회원 목록");
        String sqlAll = "select * from usertbl";
        try (ResultSet rs = stmt.executeQuery(sqlAll)) {
            while (rs.next()) {
                String id = rs.getString("userid");
                String name = rs.getString("username");
                System.out.print("id: " + id);
                System.out.print(", 이름: " + name + " ");
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void checkMember(){
        printAllmemeber();
        System.out.println("열람할 회원의 id를 입력하세요");
        String inputId= input.nextLine();

        String sql="select * from usertbl where userid=?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,inputId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                String id=rs.getString("userid");
                String pw=rs.getString("userpw");
                String name=rs.getString("username");
                java.sql.Date userdate=rs.getDate("userdate");
                int age=rs.getInt("userage");
                String interest=rs.getString("userinterest");
                String grade=rs.getString("usergrade");


                System.out.println("===== "+name+"님의 정보"+ "=====");
                System.out.println("ID : "+id+'\t' +"PW : "+pw);
                System.out.println("가입일자 : "+userdate);
                System.out.println("나이 : "+age+'\t'+"관심분야 : "+interest+'\t'+"등급 : "+grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMemberInfo() {
        boolean endFlag = false;
        System.out.println("관리자 비밀번호를 다시 입력하세요.");
        String inputAdminPw = input.nextLine();
        if (currentUser.getUserpw().equals(inputAdminPw)) {
            System.out.println("회원 목록");
            String sqlAll = "select * from usertbl";
            try (ResultSet rs = stmt.executeQuery(sqlAll)) {
                while (rs.next()) {
                    String id = rs.getString("userid");
                    String name = rs.getString("username");
                    System.out.print("id: " + id);
                    System.out.print(", 이름: " + name + " ");
                    System.out.println();
                }
                System.out.println("수정할 회원의 아이디를 입력하세요.");
                String inputId = input.nextLine();
                while (true) {
                    System.out.println("선택 번호를 입력해주세요");
                    System.out.println("1. 회원의 pw 수정");
                    System.out.println("2. 회원의 나이 수정");
                    System.out.println("3. 회원의 관심 분야 수정");
                    System.out.println("4. 나가기");

                    int inputSelect = input.nextInt();
                    input.nextLine();

                    switch (inputSelect) {
                        case 1 -> {
                            System.out.println("수정 pw 입력");
                            String inputPw = input.nextLine();
                            String sql = "update usertbl set userpw=? where userid=?";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setString(1, inputPw);
                                pstmt.setString(2, inputId);
                                pstmt.executeUpdate();
                            }
                        }
                        case 2 -> {
                            System.out.println("수정 나이 입력");
                            int inputAge = input.nextInt();
                            input.nextLine();
                            String sql = "update usertbl set userage=? where userid=?";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setInt(1, inputAge);
                                pstmt.setString(2, inputId);
                                pstmt.executeUpdate();
                            }
                        }
                        case 3 -> {
                            System.out.println("관심사를 골라주세요. \n(0.총류, 1.철학, 2.종교, 3.사회과학, 4.자연과학, 5.기술과학, 6.예술, 7.언어, 8.문학, 9.역사) ");
                            int userinterestNum = input.nextInt();
                            input.nextLine();
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
                            if (userinterest != null) {
                                String sql = "update usertbl set userinterest=? where userid=?";
                                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                    pstmt.setString(1, userinterest);
                                    pstmt.setString(2, inputId);
                                    pstmt.executeUpdate();
                                }
                            } else {
                                System.out.println("잘못된 번호입니다.");
                            }
                        }
                        case 4 -> {
                            endFlag = true;
                        }
                        default -> System.out.println("잘못된 입력입니다.");
                    }
                    if (endFlag) break;
                }
                System.out.println("수정이 완료 되었습니다.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("비밀번호가 틀렸습니다.");
        }
    }
}//클래스 끝
