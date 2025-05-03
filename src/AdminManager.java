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
    public final static int BUYBOOK=2;
    public final static int EXITBOOKADMIN=3;
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
        System.out.println("2. 도서 구매");
        System.out.println("3. 도서관리 나가기");
    }
    //  회원등급 메뉴 출력
    public static void gradeAdmin(){
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
    public static void BookAdminProcess(){
        while(true){
            boolean endFlag=false;
            bookadmin();
            int select=MenuManager.menuInput(CHECKREQUEST, EXITBOOKADMIN);
            switch(select){
                case CHECKREQUEST:
                    break;
                case BUYBOOK:
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

    //  회원등급 메뉴 실행
    public static void GradeAdminProcess(){
        while(true){
            boolean endFlag=false;
            gradeAdmin();
            int select=MenuManager.menuInput(CHECKRANK, EXITGRADEADMIN);
            switch(select){
                case CHECKRANK:
                    break;
                case UPDATERANK:
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
