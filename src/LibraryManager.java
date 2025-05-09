import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryManager {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;
    Scanner input = new Scanner(System.in);
    DBConnect db = new DBConnect();
    // <<메뉴 함수 부>>
    // 책 검색 메뉴 실행
    public void bookSearchProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.searchbook();
            int select = MenuManager.menuInput(MenuManager.NORMALSEARCH, MenuManager.EXITBOOKSEARCH);
            switch (select) {
                case MenuManager.NORMALSEARCH:
                    handleBookTransaction(normalSearch(), currentUser.getUserid(), this.conn);
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
                    detailSearch("title");
                    break;
                case MenuManager.AUTHORSEARCH:
                    detailSearch("author");
                    break;
                case MenuManager.PUBLISHERSEARCH:
                    detailSearch("publisher");
                    break;
                case MenuManager.KEYWORDSEARCH:
                    detailKeywordSearch();
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
                case MenuManager.CHECKROMANCE:
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

    public LibraryManager() {
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

            if(delayedBook()){
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
    // 책 반납 연장 메서드
    public void prolongBook(){
        try{
            if(delayedBook()){
                return;
            }
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

    //예약 도서 대출
    public void rentBookedBook(){
        LocalDate today = LocalDate.now();
        LocalDate returnDate = today.plusDays(13);
        Boolean trueFlag= false;
        try{
            // 실시간 대출 가능 여부 확인 (rent 테이블 기준)
            PreparedStatement gradeStmt = conn.prepareStatement(
                    "SELECT usergrade FROM usertbl WHERE userid = ?");
            gradeStmt.setString(1, currentUser.getUserid());
            ResultSet gradeRs = gradeStmt.executeQuery();

            String userGrade = "";
            if (gradeRs.next()) {
                userGrade = gradeRs.getString("usergrade");
            }

            //  등급별 대출 한도 설정
            int rentLimit = switch (userGrade) {
                case "신입회원" -> 2;
                case "일반회원" -> 3;
                case "우수회원" -> 5;
                case "모범회원" -> 7;
                case "장기연체자" -> 1;
                default -> 3; // 기본값
            };

            //  현재 대출 중인 책 수 조회
            PreparedStatement countStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM renttbl WHERE userid = ? AND turnin = 0");
            countStmt.setString(1, currentUser.getUserid());
            ResultSet countRs = countStmt.executeQuery();
            countRs.next();
            int currentRents = countRs.getInt(1);

            //  대출 가능 여부 확인
            if (currentRents >= rentLimit) {
                System.out.println("대출 권수를 초과하였습니다. 현재 대출 수: " + currentRents +
                        ", 등급: " + userGrade + ", 최대 가능: " + rentLimit);
                return;
            }
            // 연체 마크
            if(delayedBook()){
                return;
            }
            ArrayList<Book> bookList = new ArrayList<>();
            String sql = "select * from reservetbl r join booktbl b on b.isbn= r.isbn where r.userid=? and r.reservestatus='예약대기' and r.reserverank=0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,currentUser.getUserid());
            ResultSet rs = pstmt.executeQuery();
            //와일문
            while(true){
                while(rs.next()){
                    int isbn = rs.getInt("isbn");
                    String bookTitle = rs.getString("title");
                    String author = rs.getString("author");
                    System.out.println("현재 대여 가능한 예약책은");
                    System.out.print("isbn: "+ isbn);
                    System.out.print(", 제목: " + bookTitle);
                    System.out.print(", 저자: " + author+ " 입니다.");
                    System.out.println();
                    trueFlag=true;
                    bookList.add(mapBook(rs));
                }
                if(trueFlag){
                    Book book=null;
                    System.out.println("대출할 예약책의 isbn을 입력해주세요");
                    int inputIsbn = input.nextInt();
                    input.nextLine();
                    for(Book bookE:bookList){
                        if(bookE.getIsbn()==inputIsbn){
                            book=bookE;
                            break;
                        }
                    }
                    if(book==null){
                        System.out.println("잘못된 isbn입니다..");
                        continue;
                    }
                    System.out.println("예약책을 대출하겠습니까?(y/n)");
                    if(confirm()){
                        rentNow(book,today,returnDate);
                        String sql2= "update reservetbl set reservestatus='대출완료', reserverank=null where userid=? and isbn=? and reservestatus='예약대기' and reserverank=0";
                        PreparedStatement pstmt2= conn.prepareStatement(sql2);
                        pstmt2.setString(1,currentUser.getUserid());
                        pstmt2.setInt(2,inputIsbn);
                        pstmt2.executeUpdate();
                        PreparedStatement updateBookStmt = conn.prepareStatement(
                                "UPDATE booktbl SET rentnum = rentnum + 1 WHERE isbn = ?");
                        updateBookStmt.setInt(1, inputIsbn);
                        updateBookStmt.executeUpdate();
                        break;
                    }else{
                        System.out.println("예약대출이 진행되지 않았습니다.");
                        break;
                    }
                } else {
                    System.out.println("예약대출 가능한 책이 없습니다.");
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean delayedBook(){
        //연체여부 확인
        DBConnect db=new DBConnect();
        db.initDBConnect();

        String overduequery="select r.userid, r.rentdate, r.duedate, r.turnin, b.title from renttbl r "+
                "join booktbl b on r.isbn=b.isbn "+
                "where userid=? ";
        try(PreparedStatement pstmt=db.getConnection().prepareStatement(overduequery)){
            pstmt.setString(1, currentUser.getUserid());

            try(ResultSet rs=pstmt.executeQuery()) {
                //현재 연체중인지를 판별하는 boolean값
                while (rs.next()) {
                    String id = rs.getString("userid");
                    java.util.Date rentdate = rs.getDate("rentdate");
                    java.util.Date duedate = rs.getDate("duedate");
                    String title = rs.getString("title");
                    int turnin = rs.getInt("turnin");

                    java.util.Date today2 = new java.util.Date(System.currentTimeMillis());
                    long diffInMillies = today2.getTime() - duedate.getTime();
                    long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

                    //현재 연체 중이라면 하단 문구 출력
                    if (turnin == 0 && diffInDays > 0) {
                        System.out.println("현재 연체중 도서로 인해 해당 서비스 이용이 불가합니다.");
                        return true;
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private void rentNow(Book book, LocalDate today, LocalDate returnDate) throws SQLException {
        String sql = "INSERT INTO renttbl VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 0);
            pstmt.setString(2, currentUser.getUserid());
            pstmt.setInt(3, book.getIsbn());
            pstmt.setDate(4, Date.valueOf(today));
            pstmt.setDate(5, Date.valueOf(returnDate));
            pstmt.setInt(6, 0);
            pstmt.setString(7, null);
            pstmt.setInt(8, 0);
            pstmt.executeUpdate();
        }
        System.out.println("대출이 완료되었습니다.");
    }

    // ↓↓ 희용추가(0508)
    // 일반검색
    public int normalSearch() {
        String searchWord;
        while (true) {
            System.out.print("검색어를 입력해 주세요: ");
            searchWord = input.nextLine().trim();
            if (searchWord.isEmpty()) {
                System.out.println("검색어를 한글자 이상 입력해 주세요.");
                continue;
            }
            break;
        }
        String sql = "select * from booktbl b left join keywordtbl k on b.isbn = k.isbn " +
                "where b.title like ? or b.author like ? or b.publisher like ? or " +
                "k.keyword1 like ? or k.keyword2 like ? or k.keyword3 like ? or k.keyword4 like ? or k.keyword5 like ? or k.keyword6 like ?";
        db.initDBConnect();
        printBookHead();
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            for (int i = 1; i <= 9; i++) {
                pstmt.setString(i, "%"+searchWord+"%");
            }
            ResultSet rs = pstmt.executeQuery();
            boolean isFound = false;
            while (rs.next()){
                int isbn = rs.getInt("isbn");
                String callnum = rs.getString("callnum");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int year = rs.getInt("pubyear");
                printBookRow(isbn,callnum,title,author,publisher,year);
                isFound = true;
            }
            if (!isFound) {
                System.out.println("검색 결과가 없습니다.");
                System.out.println("-".repeat(115));
                return 0;
            }
            System.out.println("-".repeat(115));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectIsbn();
    }
    // 상세검색
    public int detailSearch(String category) {
        String categoryWord = switch (category) {
            case "title" -> "제목";
            case "author" -> "저자";
            case "publisher" -> "출판사";
            default -> "제목";
        };

        String searchWord;
        while (true) {
            System.out.print("검색할 " + categoryWord + "을(를) 입력해 주세요: ");
            searchWord = input.nextLine().trim();
            if (searchWord.isEmpty()) {
                System.out.println("검색어를 한글자 이상 입력해 주세요.");
                continue;
            }
            break;
        }

        String sql = "select * from booktbl where " + category + " like ?";
        db.initDBConnect();
        printBookHead();
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, "%"+searchWord+"%");
            ResultSet rs = pstmt.executeQuery();
            boolean isFound = false;
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String callnum = rs.getString("callnum");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int year = rs.getInt("pubyear");
                printBookRow(isbn,callnum,title,author,publisher,year);
                isFound = true;
            }
            if (!isFound) {
                System.out.println("검색 결과가 없습니다.");
                System.out.println("-".repeat(115));
                return 0;
            }
            System.out.println("-".repeat(115));
            int selectedIsbn = selectIsbn();
            if (selectedIsbn == 0) { // 추가된 부분
                return 0; // 추가된 부분
            }

            handleBookTransaction(selectedIsbn, currentUser.getUserid(), conn);
            rs.close();
            return selectedIsbn; // 옮겨진 부분 (정상 종료 시 반환)
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // 예외 발생 시 기본값 반환
    }
    // 키워드검색
    public int detailKeywordSearch() {
        String searchKeyword;
        while (true) {
            System.out.print("검색할 키워드를 입력해 주세요: ");
            searchKeyword = input.nextLine().trim();
            if (searchKeyword.isEmpty()) {
                System.out.println("검색어를 한글자 이상 입력해 주세요.");
                continue;
            }
            break;
        }
        String sql = "select * from booktbl b " +
                "inner join keywordtbl k on b.isbn = k.isbn " +
                "where k.keyword1 like ? or k.keyword2 like ? or k.keyword3 like ? or k.keyword4 like ? or k.keyword5 like ? or k.keyword6 like ?";
        db.initDBConnect();
        printBookHead();
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            for (int i = 1; i <= 6; i++) {
                pstmt.setString(i, "%"+searchKeyword+"%");
            }
            ResultSet rs = pstmt.executeQuery();
            boolean isFound = false;
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String callnum = rs.getString("callnum");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int year = rs.getInt("pubyear");

                String[] keywords = {
                        rs.getString("keyword1"),
                        rs.getString("keyword2"),
                        rs.getString("keyword3"),
                        rs.getString("keyword4"),
                        rs.getString("keyword5"),
                        rs.getString("keyword6"),
                };
                printBookRow(isbn,callnum,title,author,publisher,year);
                System.out.print(" └ 키워드: ");
                for (String keyword : keywords) {
                    if (keyword != null) {
                        System.out.print(keyword+", ");
                    }
                }
                System.out.println();
                isFound = true;
            }
            if (!isFound) {
                System.out.println("검색 결과가 없습니다.");
                System.out.println("-".repeat(115));
                return 0;
            }
            System.out.println("-".repeat(115));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectIsbn();
    }
    public int selectIsbn(){
        int isbn = 0;
        while (true) {
            try {
                System.out.print("대출할 책의 ISBN을 입력해 주세요. (나가려면 0) ");
                isbn =  input.nextInt();
                input.nextLine(); // 키보드버퍼 비우기
                if ((isbn < 1000 || isbn > 9999) && isbn != 0) {
                    System.out.println("올바른 ISBN 형식이 아닙니다. (4자리 숫자)");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력 가능합니다.");
                input.nextLine(); // 키보드버퍼 비우기
            }
        }
        if (isbn == 0) {
            System.out.println("검색을 종료합니다.");
        }
        return isbn;
    }
    public void printBookHead() {
        System.out.printf("%-5s %-5s %-55s %-15s %-10s %-4s %n", "ISBN", "분류코드", "도서명", "저자", "출판사", "발행년");
        System.out.println("-".repeat(115));
    }
    public void printBookRow(int isbn, String callnum, String title, String author, String publisher, int year) {
        System.out.println(
                pad(String.valueOf(isbn), 7) +
                        pad(callnum, 7) +
                        pad(title, 60) +
                        pad(author, 20) +
                        pad(publisher, 17) +
                        pad(String.valueOf(year), 4)
        );
    }
    public String pad(String s, int totalWidth) {
        int width = 0;
        for (char c : s.toCharArray()) {
            width += ((Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES) ? 2 : 1);
        }
        int pad = totalWidth - width;
        return s + " ".repeat(Math.max(pad,0));
    }
    // ↑↑ 희용추가(0508) 끝

    public void handleBookTransaction(int isbn, String userId, Connection conn) {
        if (isbn == 0){
            return;
        }
        try {
            // 책 상세정보 출력
            PreparedStatement bookStmt = conn.prepareStatement("SELECT * FROM booktbl WHERE isbn = ?");
            bookStmt.setInt(1, isbn);
            ResultSet bookRs = bookStmt.executeQuery();

            if (bookRs.next()) {
                System.out.println("책 제목: " + bookRs.getString("title"));
                System.out.println("저자: " + bookRs.getString("author"));
            } else {
                System.out.println("해당 ISBN의 책이 존재하지 않습니다.");
                return;
            }

            // 책이 이미 대출 중인지 혹은 예약 중인지 확인
            //대출중
            PreparedStatement rentCheckStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM renttbl WHERE isbn = ? AND turnin = 0");
            rentCheckStmt.setInt(1, isbn);
            ResultSet rentCheckRs = rentCheckStmt.executeQuery();
            rentCheckRs.next();
            boolean isRented = rentCheckRs.getInt(1) > 0;
            //예약중(잠시 반납만 된 상태)
            PreparedStatement reserveCheckStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM reservetbl WHERE isbn = ? AND reservestatus ='예약대기'");
            reserveCheckStmt.setInt(1, isbn);
            ResultSet reserveCheckRs = reserveCheckStmt.executeQuery();
            reserveCheckRs.next();
            boolean isReserved = reserveCheckRs.getInt(1) > 0;

            if (isRented) {
                System.out.println("이 책은 현재 대출 중입니다.");
                this.reserveBook(isbn);
                return;
            }

            if(isReserved){
                System.out.println("이 책은 현재 예약대기중입니다.");
                this.reserveBook(isbn);
                return;
            }

            // 실시간 대출 가능 여부 확인 (rent 테이블 기준)
            PreparedStatement gradeStmt = conn.prepareStatement(
                    "SELECT usergrade FROM usertbl WHERE userid = ?");
            gradeStmt.setString(1, userId);
            ResultSet gradeRs = gradeStmt.executeQuery();

            String userGrade = "";
            if (gradeRs.next()) {
                userGrade = gradeRs.getString("usergrade");
            }

            //  등급별 대출 한도 설정
            int rentLimit = switch (userGrade) {
                case "신입회원" -> 2;
                case "일반회원" -> 3;
                case "우수회원" -> 5;
                case "모범회원" -> 7;
                case "장기연체자" -> 1;
                default -> 3; // 기본값
            };

            //  현재 대출 중인 책 수 조회
            PreparedStatement countStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM renttbl WHERE userid = ? AND turnin = 0");
            countStmt.setString(1, userId);
            ResultSet countRs = countStmt.executeQuery();
            countRs.next();
            int currentRents = countRs.getInt(1);

            //  대출 가능 여부 확인
            if (currentRents >= rentLimit) {
                System.out.println("대출 권수를 초과하였습니다. 현재 대출 수: " + currentRents +
                        ", 등급: " + userGrade + ", 최대 가능: " + rentLimit);
                return;
            }
            //연체여부 확인
            if(delayedBook()){
                return;
            }

            // 대출 처리
            // rent 테이블 등록
            PreparedStatement insertRentStmt = conn.prepareStatement(
                    "INSERT INTO renttbl (isbn, userid, rentdate, duedate, prolong, turnin) VALUES (?, ?, CURRENT_DATE, ?, ?, 0)");
            insertRentStmt.setInt(1, isbn);
            insertRentStmt.setString(2, userId);
            LocalDate dueDate = LocalDate.now().plusDays(13);
            insertRentStmt.setDate(3, java.sql.Date.valueOf(dueDate));
            insertRentStmt.setInt(4, 0);
            insertRentStmt.executeUpdate();

            // booktbl의 rentnum 증가
            PreparedStatement updateBookStmt = conn.prepareStatement(
                    "UPDATE booktbl SET rentnum = rentnum + 1 WHERE isbn = ?");
            updateBookStmt.setInt(1, isbn);
            updateBookStmt.executeUpdate();

            System.out.println("✅ 대출이 완료되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
