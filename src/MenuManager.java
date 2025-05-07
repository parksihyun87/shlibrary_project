import java.util.Scanner;

public class MenuManager {
    // * SHLibraryManager 모음 *
    //  초기 메뉴(로그인,회원가입)
    public final static int LOGIN=1;
    public final static int NEWMEMBER=2;
    public final static int EXIT=3;
    //  메인 메뉴
    public final static int LIBRARY=1;
    public final static int INFORM=2;
    public final static int ADMIN=3;
    public final static int LOGOUT=4;

    //  초기 메뉴 출력
    public static void initMenu(){
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
    }
    //  메인 메뉴 출력
    public static void mainMenu(){
        System.out.println("1. 도서관 입장");
        System.out.println("2. 나의 정보");
        System.out.println("3. 관리자 모드");
        System.out.println("4. 로그아웃");
    }

    // * MemeberManager 모음 *
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

    //* AdminManager 모음 *
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
    public final static int EXITBOOKADMIN=2;
    //  회원등급 메뉴
    public final static int CHECKRANK=1;
    public final static int UPDATERANK=2;
    public final static int EXITGRADEADMIN=3;
    // 연체대상자 메뉴
    public final static int CHECKOVERDUELIST=1;
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
    public static void bookAdmin(){
        System.out.println("1. 신청도서 확인/처리");
        System.out.println("2. 도서관리 나가기");
    }
    //  회원등급 메뉴 출력
    public static void rateAdmin(){
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

    //*MemberManager 모음*
    //  도서관 메뉴-상위
    public final static int BOOKSEARCH=1;
    public final static int BESTSELLER=2;
    public final static int BOOKEDBOOK=3;
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
        System.out.println("3. 예약도서 대출");
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

    //  메뉴 입력 (리턴,예외)기능 함수
    public static int menuInput(int static1, int static2){
        Scanner input=new Scanner(System.in);
        int select;
        while(true){
            select=input.nextInt();
            input.nextLine();
            if(select<static1 || select>static2){
                System.out.println("잘못된 입력입니다. ");
                continue;
            }
            break;
        }
        return select;
    }
}
