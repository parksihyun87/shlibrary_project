import java.util.Scanner;

public class AdminManager {
    public final static int MEMBERADMIN=1;
    public final static int BOOKADMIN=2;
    public final static int GRADEADMIN=3;
    public final static int BLACKADMIN=4;
    public final static int EXITADMIN=5;

    public static void AdminMenu(){
        System.out.println("1. 회원정보 관리");
        System.out.println("2. 도서정보 관리");
        System.out.println("3. 회원등급 관리");
        System.out.println("4. 연체자 정보 조회");
        System.out.println("5. 관리자모드 나가기");
    }

    public static int selectAdminMenu(){
        Scanner input=new Scanner(System.in);
        int select;
        while(true){
            select=input.nextInt();
            input.nextLine();
            if(select<MEMBERADMIN || select>EXITADMIN){
                System.out.println("잘못된 입력입니다. ");
                continue;
            }
            break;
        }
        return select;
    }



}
