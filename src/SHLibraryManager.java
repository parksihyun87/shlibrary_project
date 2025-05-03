public class SHLibraryManager {
    DBConnect connect = new DBConnect();
//시현1
    public void run(){
        connect.initDBConnect();

        while(true){
            boolean endFlag=false;
            MenuManager.initMenu();
            int select=MenuManager.menuInput(MenuManager.LOGIN,MenuManager.EXIT);
            switch(select){
                case MenuManager.LOGIN:
                    this.MainMenuProcess();//변경
                    break;
                case MenuManager.NEWMEMBER:
                    break;
                case MenuManager.EXIT:
                    endFlag=true;
                    connect.releaseDB(); //변경
                    break;
            }
            if(endFlag){
                break;
            }
        }
    }
    //  메인 메뉴 실행
    public void MainMenuProcess(){
        while(true){
            boolean endFlag=false;
            MenuManager.mainMenu();
            int select=MenuManager.menuInput(MenuManager.LIBRARY, MenuManager.LOGOUT);
            switch(select){
                case MenuManager.LIBRARY:
                    this.LibraryProcess();
                    break;
                case MenuManager.MYINFO:
                    this.MemberProcess();
                    break;
                case MenuManager.ADMIN:
                    this.AdminProcess();
                    break;
                case MenuManager.LOGOUT:
                    endFlag=true;
            }
            if(endFlag){
                break;
            }
        }
    }
    //  도서관 메뉴 실행
    public void LibraryProcess(){
        while(true) {
            boolean endFlag = false;
            LibraryManager.libraryMenu();
            int select = MenuManager.menuInput(LibraryManager.BOOKSEARCH,LibraryManager.EXITLIBRARY);
            switch (select) {
                case LibraryManager.BOOKSEARCH:
                    LibraryManager.BookSearchProcess();
                    break;
                case LibraryManager.BESTSELLER:
                    LibraryManager.BestsellerProcess();
                    break;
                case LibraryManager.INTEREST:
                    LibraryManager.InterestCategoryProcess();
                    break;
                case LibraryManager.BOOKRETURN:
                    LibraryManager.BookReturnProcess();
                    break;
                case LibraryManager.BOOKREQUEST:
                    LibraryManager.BookRequestProcess();
                    break;
                case LibraryManager.EXITLIBRARY:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    //  회원 메뉴 실행
    public void MemberProcess(){
        while(true) {
            boolean endFlag = false;
            MemberManager.memberMenu();
            int select = MenuManager.menuInput(MemberManager.MYBOOK, MemberManager.EXITMEMBER);
            switch (select) {
                case MemberManager.MYBOOK:
                    MemberManager.RentalStatusProcess();
                    break;
                case MemberManager.MYINFO:
                    MemberManager.MyInfoProcess();
                    break;
                case MemberManager.DELAY:
                    MemberManager.OverdueCheckProcess();
                    break;
                case MemberManager.EXITMEMBER:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    //  관리자 메뉴 실행
    public void AdminProcess(){
        while(true){
            boolean endFlag=false;
            AdminManager.AdminMenu();
            int select=MenuManager.menuInput(AdminManager.MEMBERADMIN, AdminManager.EXITADMIN);
            switch(select){
                case AdminManager.MEMBERADMIN :
                    AdminManager.MemberAdminProcess();
                    break;
                case AdminManager.BOOKADMIN:
                    AdminManager.BookAdminProcess();
                    break;
                case AdminManager.GRADEADMIN:
                    AdminManager.GradeAdminProcess();
                    break;
                case AdminManager.BLACKADMIN:
                    AdminManager.BlackAdminProcess();
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
