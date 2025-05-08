import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryManager_safe_name {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;
    DBConnect db;
    Scanner input = new Scanner(System.in);

    public void run(){
        db.initDBConnect();//콘, 스테이트 변화로 연결, 이부분 함수부 윗줄 추가(오류 확인용인데 크게 의미는 없음)
    }


    // ↓↓ 희용추가(0508)
    // 일반검색
    public int normalSearch() {
        System.out.print("검색어를 입력해 주세요: ");
        String searchWord = input.nextLine().trim();
//        if (searchWord == "") {
//            System.out.println("검색어를 입력해 주세요.");
//        }
        String sql = "select * from booktbl b inner join keywordtbl k on b.isbn = k.isbn " +
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
        System.out.print("검색할 " + categoryWord + ": ");
        String searchWord = input.nextLine().trim();
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
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectIsbn();
    }
    // 키워드검색
    public int detailKeywordSearch() {
        System.out.print("검색할 키워드: ");
        String searchKeyword = input.nextLine().trim();
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
                System.out.print("선택할 책의 ISBN을 입력해 주세요. (나가려면 0) ");
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


}
