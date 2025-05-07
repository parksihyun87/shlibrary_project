import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryManager {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;
    Scanner input = new Scanner(System.in);
    // <<메뉴 함수 부>>
    // 책 검색 메뉴 실행
    public void bookSearchProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.searchbook();
            int select = MenuManager.menuInput(MenuManager.NORMALSEARCH, MenuManager.EXITBOOKSEARCH);
            switch (select) {
                case MenuManager.NORMALSEARCH:
                    break;
                case MenuManager.SPECIFICSEARCH:
                    detailedSearchProcess();
                    break;
                case MenuManager.EXITBOOKSEARCH:
                    endFlag = true;
                    break;
            }
            if (endFlag) break;
        }
    }

    // 책 상세 검색 메뉴 실행
    public void detailedSearchProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.detailedSearch();
            int select = MenuManager.menuInput(MenuManager.TITLESEARCH, MenuManager.EXITDETAILEDSEARCH);
            switch (select) {
                case MenuManager.TITLESEARCH:
                    break;
                case MenuManager.AUTHORSEARCH:
                    break;
                case MenuManager.PUBLISHERSEARCH:
                    break;
                case MenuManager.KEYWORDSEARCH:
                    break;
                case MenuManager.EXITDETAILEDSEARCH:
                    endFlag = true;
                    break;
            }
            if (endFlag) break;
        }
    }

    // 베스트셀러 메뉴 실행
    public void bestsellerProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.bestsellerMenu();
            int select = MenuManager.menuInput(MenuManager.BESTSELLERBOOK, MenuManager.EXITBESTSELLERBOOK);
            switch (select) {
                case MenuManager.BESTSELLERBOOK:
                    this.printTop5Books();
                    break;
                case MenuManager.EXITBESTSELLERBOOK:
                    endFlag = true;
                    break;
            }
            if (endFlag) break;
        }
    }

    // 도서 분류별 추천 실행
//    public void interestCategoryProcess() {
//        while (true) {
//            boolean endFlag = false;
//            MenuManager.interestCategory();
//            int select = MenuManager.menuInput(MenuManager.GENERALWORKS, MenuManager.EXITCATEGORY);
//            switch (select) {
//                case MenuManager.GENERALWORKS:
//                    break;
//                case MenuManager.PHILOSOPHY:
//                    break;
//                case MenuManager.RELIGION:
//                    break;
//                case MenuManager.SOCIALSCIENCE:
//                    break;
//                case MenuManager.NATURALSCIENCE:
//                    break;
//                case MenuManager.TECHNOLOGY:
//                    break;
//                case MenuManager.ART:
//                    break;
//                case MenuManager.LANGUAGE:
//                    break;
//                case MenuManager.LITERATURE:
//                    break;
//                case MenuManager.HISTORY:
//                    break;
//                case MenuManager.EXITCATEGORY:
//                    endFlag = true;
//                    break;
//            }
//            if (endFlag) break;
//        }
//    }

    // 책 반납 메뉴 실행
    public void bookReturnProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.bookreturn();
            int select = MenuManager.menuInput(MenuManager.BOOKTURNIN, MenuManager.EXITBOOKRETURN);
            switch (select) {
                case MenuManager.BOOKTURNIN:
                    this.returnBook();
                    break;
                case MenuManager.BOOKPROLONG:
                    this.prolongBook();
                    break;
                case MenuManager.EXITBOOKRETURN:
                    endFlag = true;
                    break;
            }
            if (endFlag) break;
        }
    }

    // 책 신청 메뉴 실행
    public void bookRequestProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.bookRequestMenu();
            int select = MenuManager.menuInput(MenuManager.REQUESTBOOK, MenuManager.EXITBOOKREQUEST);
            switch (select) {
                case MenuManager.REQUESTBOOK:
                    applyForBookRequest(currentUser.userid);
                    break;
                case MenuManager.CHECKDUPLICATE:
                    break;
                case MenuManager.EXITBOOKREQUEST:
                    endFlag = true;
                    break;
            }
            if (endFlag) break;
        }
    }

    // <<기능 함수 부>>
    // 초기 세팅 함수
    public LibraryManager(Connection connect,User user){
        try{
            this.conn = connect;
            this.stmt = this.conn.createStatement();
            this.currentUser= user;
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    // << 베스트 셀러 >>
    public void printTop5Books(){
        String sql = """
        SELECT b.isbn, b.callnum, b.title, b.author, b.publisher, b.pubyear, b.rentnum,
               k.keyword1, k.keyword2, k.keyword3, k.keyword4, k.keyword5, k.keyword6
        FROM booktbl b
        JOIN keywordtbl k ON b.isbn = k.isbn
        ORDER BY b.rentnum DESC
        LIMIT 5
    """;

        try {
            ResultSet rs = this.stmt.executeQuery(sql);
            System.out.println("Top 5 BEST Selling Books.");
            int rank = 1;
            while (rs.next()) {
                System.out.println(rank + "위:");
                this.printBookInfo(rs);
                rank++;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void printBookInfo(ResultSet rs) throws SQLException {
        int isbn = rs.getInt("isbn");
        String callnum = rs.getString("callnum");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String publisher = rs.getString("publisher");
        int pubyear = rs.getInt("pubyear");
        int rentnum = rs.getInt("rentnum");

        String[] keywords = new String[6];
        for (int i = 0; i < 6; i++) {
            keywords[i] = rs.getString("keyword" + (i + 1));
        }

        System.out.printf("ISBN: %d | 청구기호: %s | 제목: %s | 저자: %s | 출판사: %s | 출판연도: %d | 대여횟수: %d\n",
                isbn, callnum, title, author, publisher, pubyear, rentnum);

        System.out.print("키워드: ");
        boolean hasKeyword = false;
        for (String kw : keywords) {
            if (kw != null && !kw.isEmpty()) {
                System.out.print("[" + kw + "] ");
                hasKeyword = true;
            }
        }
        if (!hasKeyword) System.out.print("없음");
        System.out.println("\n-----------------------------");
    }

    // <<도서 예약>>
    public void reserveBook(int inputIsbn) {
        LocalDate today = LocalDate.now();
        LocalDate returnDate = today.plusDays(13);
        try {
            //가져오는 쿼리->1. 북 일반 (2. 렌트(turn in 0) or 3. 예약(status:예약대기)) 둘 중 하나의 쿼리결과 !=null 이므로 기준으로 조건문
            Book book = getBook(inputIsbn);
            Rent rent = getNotReturnRent(inputIsbn);

            int reserveCount = getReserveCount(inputIsbn);
            printBook(book);
            if(rent!=null) {
                System.out.println("대출상태: 대출중");
                System.out.println("대출기간: " + rent.getRentdate() + "~" + rent.getDuedate());
            } else {
                System.out.println("대출상태: 예약 대여 진행중");
            }
                System.out.println("예약대기자: " + reserveCount + "명");
                if (reserveCount >= 3) {
                    System.out.println("예약인원 초과로 예약 하실 수 없습니다.");
                    return;
                }
                System.out.println("예약 하시겠습니까? (y/n)");
                // 이미 예약중 → 예약 불가

                if (confirm()) {
                    if (countUserReservation(inputIsbn, currentUser) > 0) {
                        System.out.println("이미 내가 예약중인 책입니다.");
                        return;
                    }
                    if(countUserReservation(currentUser)==3){
                        System.out.println("예약은 3권까지만 가능합니다.");
                        return;
                    }
                    reserveBook(book, today, reserveCount + 1);
                } else {
                    System.out.println("예약이 취소되었습니다.");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Book getBook(int isbn) throws SQLException {
        String sql = "SELECT * FROM booktbl WHERE isbn = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapBook(rs);
        }
        return null;
    }
    private Rent getNotReturnRent(int isbn) throws SQLException {
        String sql = "SELECT * FROM renttbl r INNER JOIN booktbl b ON r.isbn = b.isbn WHERE r.isbn = ? AND r.turnin = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapRent(rs);
        }
        return null;
    }
    private Reserve getBookedReserve(int isbn) throws SQLException {
        String sql = "SELECT * FROM reservetbl WHERE isbn = ? AND reservestatus = '예약대기'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapReserve(rs);
        }
        return null;
    }
    private int getReserveCount(int isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservetbl WHERE isbn=? AND reservestatus='예약대기'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    private int countUserReservation(int isbn, User user) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservetbl WHERE isbn=? AND userid=? AND reservestatus='예약대기'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isbn);
            pstmt.setString(2, user.getUserid());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    private int countUserReservation(User user) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservetbl WHERE userid=? AND reservestatus='예약대기'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserid());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    private boolean confirm() {
        String inputYn = input.nextLine().trim().toUpperCase();
        return inputYn.equals("Y");
    }
    private Book mapBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("isbn"),
                rs.getString("callnum"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("publisher"),
                rs.getInt("pubyear"),
                rs.getInt("rentnum")
        );
    }
    private Rent mapRent(ResultSet rs) throws SQLException {
        return new Rent(
                rs.getInt("rentnumber"),
                rs.getString("userid"),
                rs.getInt("isbn"),
                rs.getDate("rentdate"),
                rs.getDate("duedate"),
                rs.getBoolean("prolong"),
                rs.getDate("turnindate"),
                rs.getBoolean("turnin")
        );
    }

    private Reserve mapReserve(ResultSet rs) throws SQLException {
        return new Reserve(
                rs.getInt("reservenumber"),
                rs.getString("userid"),
                rs.getInt("isbn"),
                rs.getDate("reservedate"),
                rs.getInt("reserverank"),
                rs.getString("reservestatus")
        );
    }

    public void printBook(Book book){
        System.out.println(book.getIsbn());
        System.out.println(book.getTitle());
        System.out.println(book.getAuthor());
        System.out.println(book.getPublisher());
        System.out.println(book.getPubyear());
    }

    public void printReturnBook(Book book){
        System.out.print("isbn: " + book.getIsbn());
        System.out.println(", 제목: " + book.getTitle());
    }

    private void reserveBook(Book book, LocalDate today, int position) throws SQLException {
        String sql = "INSERT INTO reservetbl VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 0);
            pstmt.setString(2, currentUser.getUserid());
            pstmt.setInt(3, book.getIsbn());
            pstmt.setDate(4, Date.valueOf(today));
            pstmt.setInt(5, position);
            pstmt.setString(6, "예약대기");
            pstmt.executeUpdate();
        }
        System.out.println("예약이 완료되었습니다.");
    }
    // 책 반납
    public void returnBook() {
        //키워드: 현재 대여책/ 현재 대여 책 보여주는 쿼리문-> 모두 보여 주면서 반납할 책의 isbn 입력 받음->
        //입력시 반납처리- 1.update 쿼리분으로 대여책의 turnindate 기입, turnin을 1로 바꿈.
        // 2. 예약 테이블에 존재하는 책인지 확인하는 쿼리문, 3.있으면 해당 책의 대기자들 예약순위를 -1씩 감소시킴.
        LocalDate today = LocalDate.now();

        try{
            myRentedBook(currentUser);
            System.out.println("반납할 책 isbn 입력");
            int inputIsbn=input.nextInt();
            input.nextLine();
            String sql= "update renttbl set turnindate = ? , turnin=? where userid=? and isbn=? and turnin=false";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1,Date.valueOf(today));
            pstmt.setBoolean(2,true);
            pstmt.setString(3,currentUser.getUserid());//수정
            pstmt.setInt(4,inputIsbn);
            String sql2= "update reservetbl set reserverank = reserverank-1 where isbn=?";
            PreparedStatement pstmt2= conn.prepareStatement(sql2);
            pstmt2.setInt(1,inputIsbn);
            pstmt2.executeUpdate();
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                System.out.println("정상적으로 반납되었습니다.");
            } else {
                System.out.println("반납 실패 - 조건에 맞는 대여 기록이 없습니다.");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void myRentedBook(User user) throws SQLException {
        ArrayList<Book> bookList = new ArrayList<>();
        ArrayList<Rent> rentList = new ArrayList<>();

        String sql="select * from renttbl r inner join booktbl b on r.isbn = b.isbn where userid=? and turnin=false";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,user.getUserid());
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            bookList.add(mapBook(rs));
            rentList.add(mapRent(rs));
        }
        if(bookList.size()==0){
            System.out.println("대여중인 책이 없습니다.");
        }
        rs.close();
        for(int i=0;i<bookList.size();i++){
            String str;
            if(rentList.get(i).getProlong()){
                str = "연장함";
            } else{
                str = "연장안함";
            }
            printReturnBook(bookList.get(i));
            System.out.print("대여일: "+rentList.get(i).getRentdate());
            System.out.print(", 반납일: "+rentList.get(i).getDuedate());
            System.out.print(", 연장여부: "+str);
            System.out.println();
        }
    }

    public void prolongBook(){
        try{
            myRentedBook(currentUser);
            System.out.println("연장할 책 isbn을 입력하세요");
            int inputIsbn= input.nextInt();
            input.nextLine();
            Book book = getBook(inputIsbn);
            Rent rent = getNotReturnRent(inputIsbn);
            System.out.println("연장 하시겠습니까?");
            if(confirm()){
                if(rent.getProlong()){
                    System.out.println("이미 연장한 책입니다.");
                    return;
                }
                String sql= "update renttbl set prolong = true, duedate = duedate+6 where userid=? and isbn=? and turnin=false";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,currentUser.getUserid());//수정
                pstmt.setInt(2,inputIsbn);
                int updated = pstmt.executeUpdate();
                if (updated > 0) {
                    System.out.println("정상적으로 연장되었습니다.");
                } else {
                    System.out.println("연장 실패 - 해당 대여 기록이 없습니다.");
                }
            } else {
                System.out.println("연장이 취소되었습니다.");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //도서 신청 메서드
    public static void applyForBookRequest(String userid){
        Scanner input=new Scanner(System.in);
        System.out.print("신청할 도서 제목을 입력하세요: ");
        String title=input.nextLine();
        System.out.println("신청할 도서의 저자를 입력하세요: ");
        String author=input.nextLine();
        System.out.println("신청할 도서의 출판사를 입력하세요: ");
        String publisher=input.nextLine();
        System.out.println("신청할 도서의 출판연도를 입력하세요: ");
        int pubyear=input.nextInt();
        String query="insert into requesttbl (userid, title, author, publisher, pubyear, comrequest) values(?, ?, ?, ?, ?, 'n')";

        DBConnect db=new DBConnect();
        db.initDBConnect();
        try(Connection conn=db.getConnection();
            PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setString(1, userid);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setString(4, publisher);
            pstmt.setInt(5, pubyear);

            int result=pstmt.executeUpdate();
            if(result>0){
                System.out.println("도서 신청이 완료되었습니다.");
            }else{
                System.out.println("도서 신청에 실패하였습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
