import java.util.Scanner;

public class SHLibraryManager {
    DBConnect connect = new DBConnect();



    public void run(){
        connect.initDBConnect();

        while(true){
            boolean endFlag=false;
            MenuManager.initMenu();
            int select=MenuManager.selectInitMenu();
            switch(select){
                case MenuManager.LOGIN:
                    login();
                    break;
                case MenuManager.NEWMEMBER:
                    break;
                case MenuManager.EXIT:
                    endFlag=true;
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }


    public boolean login(){


        return true;
    }



















    public void LibraryProcess(){
        while(true){
            boolean endFlag=false;
            MenuManager.libraryMenu();
            int select=MenuManager.selectLibraryMenu();
            switch(select){
                case MenuManager.LIBRARY:
                    break;
                case MenuManager.MYINFO:
                    break;
                case MenuManager.ADMIN:
                    break;
                case MenuManager.LOGOUT:
                    endFlag=true;
            }
            if(endFlag){
                break;
            }
        }
    }

    public void AdminProcess(){
        while(true){
            boolean endFlag=false;
            AdminManager.AdminMenu();
            int select=AdminManager.selectAdminMenu();
            switch(select){
                case AdminManager.MEMBERADMIN :
                    break;
                case AdminManager.BOOKADMIN:
                    break;
                case AdminManager.GRADEADMIN:
                    break;
                case AdminManager.BLACKADMIN:
                    break;
                case AdminManager.EXITADMIN:
                    endFlag=true;
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }


}
