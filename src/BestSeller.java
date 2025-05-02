import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BestSeller {
    private Statement stmt = null;
    public void printTop5Books(){
        String sql = "select * from 도서 order by 대여횟수 desc limit 5";
        try {
            ResultSet rs = this.stmt.executeQuery(sql);
            System.out.println("Top 5 BEST Selling Books.");
            int rank = 1;//순위 변수
            while (rs.next()){
                System.out.println(rank + "위: ");
                printBookInfo(rs);
                rank++;
            }
        }catch (SQLException e){
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
    }
}
