public class MemberManager {
    //  회원 메뉴-상위
    public final static int MYBOOK=1;
    public final static int MYINFO=2;
    public final static int DELAY=3;
    public final static int EXITMEMBER=4;
    // 현재 대여 현황 메뉴
    public final static int CURRENTRENTALSTATUS = 1;
    public final static int EXITRENTALSTATUS = 2;
    //  나의 정보 메뉴
    public final static int CHECKINFO=1;
    public final static int UPDATEINFO=2;
    public final static int EXITMYINFO=3;

    // 연체 확인 메뉴
    public final static int CHECKOVERDUE = 1;
    public final static int EXITOVERDUEMENU = 2;

    // 회원 메뉴 출력
    public static void memberMenu(){
        System.out.println("1. 내 대출 현황");
        System.out.println("2. 나의 정보 확인/수정");
        System.out.println("3. 연체 정보 확인");
        System.out.println("4. 나의 회원 정보 나가기");
    }
    // 현재 대여 현황 메뉴 출력
    public static void rentalStatus() {
        System.out.println("1. 현재 대여 현황 보기");
        System.out.println("2. 나가기");
    }
    // 나의 정보 메뉴 출력
    public static void myinfo(){
        System.out.println("1. 나의 정보 열람");
        System.out.println("2. 나의 정보 수정");
        System.out.println("3. 나의 정보 나가기");
    }
    // 연체 확인 메뉴 출력
    public static void overdueMenu() {
        System.out.println("1. 연체 여부 확인");
        System.out.println("2. 나가기");
    }

    // 현재 대여 현황 메뉴 실행
    public static void RentalStatusProcess() {
        while (true) {
            boolean endFlag = false;
            rentalStatus();
            int select = MenuManager.menuInput(CURRENTRENTALSTATUS, EXITRENTALSTATUS);
            switch (select) {
                case CURRENTRENTALSTATUS:
                    break;
                case EXITRENTALSTATUS:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    //  나의 정보 메뉴 실행
    public static void MyInfoProcess(){
        while(true){
            boolean endFlag=false;
            myinfo();
            int select=MenuManager.menuInput(CHECKINFO, EXITMYINFO);
            switch(select){
                case CHECKINFO:
                    break;
                case UPDATEINFO:
                    break;
                case EXITMYINFO:
                    endFlag=true;
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }
    // 연체 확인 메뉴 실행
    public static void OverdueCheckProcess() {
        while (true) {
            boolean endFlag = false;
            overdueMenu();
            int select = MenuManager.menuInput(CHECKOVERDUE, EXITOVERDUEMENU);
            switch (select) {
                case CHECKOVERDUE:
                    break;
                case EXITOVERDUEMENU:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
}


