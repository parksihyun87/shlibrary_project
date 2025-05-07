
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Scanner;

public class LibraryManager {
//  도서관 메뉴-상위
    public final static int BOOKSEARCH=1;
    public final static int BESTSELLER=2;
    public final static int INTEREST=3;
    public final static int BOOKRETURN=4;
    public final static int BOOKREQUEST=5;
    public final static int EXITLIBRARY=6;
// 책 검색 메뉴
    public final static int NORMALSEARCH=1;
    public final static int SPECIFICSEARCH=2;
    public final static int EXITBOOKSEARCH=3;

// 책 상세 검색 메뉴
    public final static int TITLESEARCH=1;
    public final static int AUTHORSEARCH=2;
    public final static int PUBLISHERSEARCH=3;
    public final static int KEYWORDSEARCH=4;
    public final static int EXITDETAILEDSEARCH=5;

    // 베스트셀러 메뉴
    public final static int BESTSELLERBOOK = 1;
    public final static int EXITBESTSELLERBOOK = 2;

    // 관심분야 도서도서 분류 메뉴
    public final static int GENERALWORKS = 1;       // 총류
    public final static int PHILOSOPHY = 2;         // 철학
    public final static int RELIGION = 3;           // 종교
    public final static int SOCIALSCIENCE = 4;      // 사회과학
    public final static int NATURALSCIENCE = 5;     // 자연과학
    public final static int TECHNOLOGY = 6;         // 기술과학
    public final static int ART = 7;                // 예술
    public final static int LANGUAGE = 8;           // 언어
    public final static int LITERATURE = 9;         // 문학
    public final static int HISTORY = 10;           // 역사
    public final static int EXITCATEGORY = 11;
    //  책 반납 메뉴
    public final static int BOOKTURNIN=1;
    public final static int BOOKPROLONG=2;
    public final static int EXITBOOKRETURN=3;

    // 책 신청 메뉴 상수
    public final static int REQUESTBOOK = 1;
    public final static int CHECKDUPLICATE = 2;
    public final static int EXITBOOKREQUEST = 3;

//  도서관 메뉴 출력-상위
    public static void libraryMenu(){
        System.out.println("1. 도서 검색");
        System.out.println("2. 베스트셀러");
        System.out.println("3. 관심분야 도서");
        System.out.println("4. 도서 반납");
        System.out.println("5. 도서 신청");
        System.out.println("6. 메인메뉴로");
    }
//  책 검색 메뉴 출력
    public static void searchbook(){
        System.out.println("1. 일반 검색");
        System.out.println("2. 상세 검색");
        System.out.println("3. 검색 메뉴 나가기");
    }
//  책 상세 검색 메뉴 출력
    public static void detailedSearch(){
        System.out.println("1. 제목 검색");
        System.out.println("2. 저자 검색");
        System.out.println("3. 출판사 검색");
        System.out.println("4. 키워드 검색");
        System.out.println("5. 상세검색 나가기");
    }
    // 베스트셀러 메뉴 출력
    public static void bestsellerMenu() {
        System.out.println("1. 베스트셀러 보기");
        System.out.println("2. 나가기");
    }
    // 도서 분류 메뉴 출력

    public static void interestCategory(){
        System.out.println("1. 총류");
        System.out.println("2. 철학");
        System.out.println("3. 종교");
        System.out.println("4. 사회과학");
        System.out.println("5. 자연과학");
        System.out.println("6. 기술과학");
        System.out.println("7. 예술");
        System.out.println("8. 언어");
        System.out.println("9. 문학");
        System.out.println("10. 역사");
        System.out.println("11. 분류 검색 나가기");
    }

    //  책 반납 메뉴 출력
    public static void bookreturn(){
        System.out.println("1. 책 반납");
        System.out.println("2. 반납 연장");
        System.out.println("3. 책 반납 메뉴 나가기");
    }
    // 책 신청 메뉴 출력
    public static void bookRequestMenu() {
        System.out.println("1. 책 신청하기");
        System.out.println("2. 신청 전 중복여부 검색");
        System.out.println("3. 나가기");
    }


import java.sql.*;
import java.time.LocalDate;
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
    public void interestCategoryProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.interestCategory();
            int select = MenuManager.menuInput(MenuManager.GENERALWORKS, MenuManager.EXITCATEGORY);
            switch (select) {
                case MenuManager.GENERALWORKS:
                    break;
                case MenuManager.PHILOSOPHY:
                    break;
                case MenuManager.RELIGION:
                    break;
                case MenuManager.SOCIALSCIENCE:
                    break;
                case MenuManager.NATURALSCIENCE:
                    break;
                case MenuManager.TECHNOLOGY:
                    break;
                case MenuManager.ART:
                    break;
                case MenuManager.LANGUAGE:
                    break;
                case MenuManager.LITERATURE:
                    break;
                case MenuManager.HISTORY:
                    break;
                case MenuManager.EXITCATEGORY:
                    endFlag = true;
                    break;
            }
            if (endFlag) break;
        }
    }

    // 책 반납 메뉴 실행
    public void bookReturnProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.bookreturn();
            int select = MenuManager.menuInput(MenuManager.BOOKTURNIN, MenuManager.EXITBOOKRETURN);
            switch (select) {
                case MenuManager.BOOKTURNIN:
                    break;
                case MenuManager.BOOKPROLONG:
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

                case REQUESTBOOK:
                    //applyForBookRequest(userid);

                case MenuManager.REQUESTBOOK:

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
            Book book = getRentedBook(inputIsbn);
            Rent rent = getBookedRent(inputIsbn);// 대여중 책 확인
            boolean isRented = book != null;
            if (isRented) {
                // 대출중 → 예약 가능 여부 확인
                if (countUserReservation(inputIsbn, currentUser) > 0) {
                    System.out.println("이미 예약중인 책입니다.");
                    return;
                }
                int reserveCount = getReserveCount(inputIsbn);
                printBook(book);
                System.out.println("대출상태: 대출중");
                System.out.println("대출기간: " + rent.getRentdate()+"~"+rent.getDuedate());
                System.out.println("예약대기자: " + reserveCount + "명");

                if (reserveCount >= 3) {
                    System.out.println("예약인원 초과로 예약 하실 수 없습니다.");
                    return;
                }

                System.out.println("예약 하시겠습니까? (y/n)");
                if (confirm()) {
                    reserveBook(book, today, reserveCount + 1);
                } else {
                    System.out.println("예약이 취소되었습니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Rent getBookedRent(int isbn) throws SQLException {
        String sql = "SELECT * FROM renttbl r INNER JOIN booktbl b ON r.isbn = b.isbn WHERE r.isbn = ? AND r.turnin = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapRent(rs);
        }
        return null;
    }
    private Book getRentedBook(int isbn) throws SQLException {
        String sql = "SELECT * FROM renttbl r INNER JOIN booktbl b ON r.isbn = b.isbn WHERE r.isbn = ? AND r.turnin = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapBook(rs);
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

    public void printBook(Book book){
        System.out.println(book.getIsbn());
        System.out.println(book.getTitle());
        System.out.println(book.getAuthor());
        System.out.println(book.getPublisher());
        System.out.println(book.getPubyear());
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
        String query="insert into requesttbl (userid, title, author, publisher, pubyear, comrequest) values(?, ?, ?, ?, ?, 'N'";

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
