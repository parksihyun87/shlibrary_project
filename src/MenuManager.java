import java.util.Scanner;

public class MenuManager {
    //  초기 메뉴(로그인,회원가입)
    public final static int LOGIN=1;
    public final static int NEWMEMBER=2;
    public final static int EXIT=3;
    //  메인 메뉴
    public final static int LIBRARY=1;
    public final static int MYINFO=2;
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
