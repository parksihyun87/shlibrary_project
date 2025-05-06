import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MemberManager {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;

    // 현재 대여 현황 메뉴 실행
    public void rentalStatusProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.rentalStatus();
            int select = MenuManager.menuInput(MenuManager.CURRENTRENTALSTATUS, MenuManager.EXITRENTALSTATUS);
            switch (select) {
                case MenuManager.CURRENTRENTALSTATUS:
                    break;
                case MenuManager.EXITRENTALSTATUS:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // 나의 정보 메뉴 실행
    public void myInfoProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.myinfo();
            int select = MenuManager.menuInput(MenuManager.CHECKINFO, MenuManager.EXITMYINFO);
            switch (select) {
                case MenuManager.CHECKINFO:
                    break;
                case MenuManager.UPDATEINFO:
                    break;
                case MenuManager.EXITMYINFO:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }

    // 연체 확인 메뉴 실행
    public void overdueCheckProcess() {
        while (true) {
            boolean endFlag = false;
            MenuManager.overdueMenu();
            int select = MenuManager.menuInput(MenuManager.CHECKOVERDUE, MenuManager.EXITOVERDUEMENU);
            switch (select) {
                case MenuManager.CHECKOVERDUE:
                    break;
                case MenuManager.EXITOVERDUEMENU:
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
    public MemberManager(Connection connect,User user){
        try{
            this.conn = connect;
            this.stmt = this.conn.createStatement();
            this.currentUser= user;
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}


