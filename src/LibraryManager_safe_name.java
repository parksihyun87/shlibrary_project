import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryManager_safe_name {
    //* 멤버 변수 부
    private Connection conn;
    private Statement stmt;
    private User currentUser= null;
    DBConnect db;
    Scanner input = new Scanner(System.in);

    public void run(){
        db.initDBConnect();//콘, 스테이트 변화로 연결, 이부분 함수부 윗줄 추가(오류 확인용인데 크게 의미는 없음)
    }



}
