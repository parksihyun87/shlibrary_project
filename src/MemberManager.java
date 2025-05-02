import java.util.Scanner;

public class MemberManager {

    public final static int MYBOOK=1;
    public final static int MYINFO=2;
    public final static int DELAY=3;
    public final static int EXITMEMBER=4;

    public static void MemberManager(){
        System.out.println("1. 나의 도서");
        System.out.println("2. 나의정보 확인/수정");
        System.out.println("3. 연체정보 확인");
        System.out.println("4. 회원정보 나가기");
    }

    public static int selectMemberManager(){
        Scanner input=new Scanner(System.in);
        int select;
        while(true){
            select=input.nextInt();
            input.nextLine();
            if(select<MYBOOK || select>EXITMEMBER){
                System.out.println("잘못된 입력입니다. ");
                continue;
            }
            break;
        }
        return select;
    }

}


