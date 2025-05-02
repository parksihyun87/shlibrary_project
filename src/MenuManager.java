import java.util.Scanner;

public class MenuManager {
    public final static int LOGIN=1;
    public final static int NEWMEMBER=2;
    public final static int EXIT=3;

    public final static int LIBRARY=1;
    public final static int MYINFO=2;
    public final static int ADMIN=3;
    public final static int LOGOUT=4;

    public static void initMenu(){
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
    }

    public static void libraryMenu(){
        System.out.println("1. 도서관 입장");
        System.out.println("2. 나의 정보");
        System.out.println("3. 관리자 모드");
        System.out.println("4. 로그아웃");
    }

    public static int selectInitMenu(){
        Scanner input=new Scanner(System.in);
        int select;
        while(true){
            select=input.nextInt();
            input.nextLine();
            if(select<LOGIN || select>EXIT){
                System.out.println("잘못된 입력입니다. ");
                continue;
            }
            break;
        }
        return select;
    }

    public static int selectLibraryMenu(){
        Scanner input=new Scanner(System.in);
        int select;
        while(true){
            select=input.nextInt();
            input.nextLine();
            if(select<LIBRARY || select>LOGOUT){
                System.out.println("잘못된 입력입니다. ");
                continue;
            }
            break;
        }
        return select;
    }
}
