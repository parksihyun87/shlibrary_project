public class LibraryManager {
//  도서관 메뉴-상위
    public final static int BOOKSEARCH=1;
    public final static int BESTSELLER=2;
    public final static int INTEREST=3;
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

    // 관심분야 도서도서 분류 메뉴
    public final static int GENERALWORKS = 1;       // 총류
    public final static int PHILOSOPHY = 2;         // 철학
    public final static int RELIGION = 3;           // 종교
    public final static int SOCIALSCIENCE = 4;      // 사회과학
    public final static int NATURALSCIENCE = 5;     // 자연과학
    public final static int TECHNOLOGY = 6;         // 기술과학
    public final static int ART = 7;                // 예술
    public final static int LANGUAGE = 8;           // 언어
    public final static int LITERATURE = 9;         // 문학
    public final static int HISTORY = 10;           // 역사
    public final static int EXITCATEGORY = 11;
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
        System.out.println("3. 관심분야 도서");
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
    // 도서 분류 메뉴 출력

    public static void interestCategory(){
        System.out.println("1. 총류");
        System.out.println("2. 철학");
        System.out.println("3. 종교");
        System.out.println("4. 사회과학");
        System.out.println("5. 자연과학");
        System.out.println("6. 기술과학");
        System.out.println("7. 예술");
        System.out.println("8. 언어");
        System.out.println("9. 문학");
        System.out.println("10. 역사");
        System.out.println("11. 분류 검색 나가기");
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


    // 책 검색 메뉴 실행
    public static void BookSearchProcess(){
        while(true) {
            boolean endFlag = false;
            searchbook();
            int select = MenuManager.menuInput(NORMALSEARCH,EXITBOOKSEARCH);
            switch (select) {
                case NORMALSEARCH:

                    break;
                case SPECIFICSEARCH:
                    DetailedSearchProcess();
                    break;
                case EXITBOOKSEARCH:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    // 책 상세 검색 메뉴 실행
    public static void DetailedSearchProcess(){
        while(true) {
            boolean endFlag = false;
            detailedSearch();
            int select = MenuManager.menuInput(TITLESEARCH,EXITDETAILEDSEARCH);
            switch (select) {
                case TITLESEARCH:
                    break;
                case AUTHORSEARCH:
                    break;
                case PUBLISHERSEARCH:
                    break;
                case KEYWORDSEARCH:
                    break;
                case EXITDETAILEDSEARCH:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    // 베스트셀러 메뉴 실행
    public static void BestsellerProcess() {
        while (true) {
            boolean endFlag=false;
            bestsellerMenu();
            int select = MenuManager.menuInput(BESTSELLER, EXITBESTSELLERBOOK);
            switch (select) {
                case BESTSELLERBOOK:
                    break;
                case EXITBESTSELLERBOOK:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    // 도서 분류별 추천 실행
    public static void InterestCategoryProcess() {
        while (true) {
            boolean endFlag = false;
            interestCategory();
            int select = MenuManager.menuInput(GENERALWORKS, EXITCATEGORY);
            switch (select) {
                case GENERALWORKS:
                    break;
                case PHILOSOPHY:
                    break;
                case RELIGION:
                    break;
                case SOCIALSCIENCE:
                    break;
                case NATURALSCIENCE:
                    break;
                case TECHNOLOGY:
                    break;
                case ART:
                    break;
                case LANGUAGE:
                    break;
                case LITERATURE:
                    break;
                case HISTORY:
                    break;
                case EXITCATEGORY:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    // 책 반납 메뉴 실행
    public static void BookReturnProcess(){
        while(true) {
            boolean endFlag = false;
            bookreturn();
            int select = MenuManager.menuInput(BOOKTURNIN,EXITBOOKRETURN);
            switch (select) {
                case BOOKTURNIN:
                    break;
                case BOOKPROLONG:
                    break;
                case EXITBOOKRETURN:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
    // 책 신청 메뉴 실행
    public static void BookRequestProcess() {
        while (true) {
            boolean endFlag = false;
            bookRequestMenu();
            int select = MenuManager.menuInput(REQUESTBOOK, EXITBOOKREQUEST);
            switch (select) {
                case REQUESTBOOK:
                    break;
                case CHECKDUPLICATE:
                    break;
                case EXITBOOKREQUEST:
                    endFlag = true;
                    break;
            }
            if (endFlag) {
                break;
            }
        }
    }
}
