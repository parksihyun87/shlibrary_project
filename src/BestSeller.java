import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BestSeller {
    private Connection conn;
    private Statement stmt;
    public BestSeller(Connection conn){
        try {
            this.conn = conn;
            this.stmt = conn.createStatement();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void printTop5Books(){
        String sql = """
        SELECT b.isbn, b.`분류코드`, b.`책 제목`, b.`저자`, b.`출판사`, b.`출판년도`, b.`대여 횟수`,
               k.keyword1, k.keyword2, k.keyword3, k.keyword4, k.keyword5, k.keyword6
        FROM 도서 b
        JOIN keywordtbl k ON b.isbn = k.isbn
        ORDER BY b.`대여 횟수` DESC
        LIMIT 5
    """;

        try {
            ResultSet rs = this.stmt.executeQuery(sql);
            System.out.println("Top 5 BEST Selling Books.");
            int rank = 1;
            while (rs.next()) {
                System.out.println(rank + "위:");
                printBookInfo(rs);
                rank++;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void printBookInfo(ResultSet rs) throws SQLException {
        int isbn = rs.getInt("isbn");
        String callnum = rs.getString("분류코드");
        String title = rs.getString("책 제목");
        String author = rs.getString("저자");
        String publisher = rs.getString("출판사");
        int pubyear = rs.getInt("출판년도");
        int rentnum = rs.getInt("대여 횟수");

        String[] keywords = new String[6];
        for (int i = 0; i < 6; i++) {
            keywords[i] = rs.getString("keyword" + (i + 1));
        }

        System.out.printf("ISBN: %d | 분류코드: %s | 제목: %s | 저자: %s | 출판사: %s | 출판년도: %d | 대여횟수: %d\n",
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
}
