package com.abc.jdbc.util;

import com.abc.jdbc.main.MainApplication;

import java.io.IOException;
import java.util.Scanner;

public class Animation {
    public static void loading() throws InterruptedException, IOException {
        MainApplication.clearScreen();
        System.out.println("   _     \n" +
                "  |\"|    \n" +
                "U | | u  \n" +
                " \\| |/__ \n" +
                "  |_____|\n" +
                "  //  \\\\ \n" +
                " (_\")(\"_)");
        Thread.sleep(200);
        MainApplication.clearScreen();
        System.out.println("   _      U  ___ u\n" +
                "  |\"|      \\/\"_ \\/\n" +
                "U | | u    | | | |\n" +
                " \\| |/_.-,_| |_| |\n" +
                "  |_____\\_)-\\___/ \n" +
                "  //  \\\\     \\\\   \n" +
                " (_\")(\"_)   (__)  ");
        Thread.sleep(200);
        MainApplication.clearScreen();
        System.out.println("   _      U  ___ u   _     \n" +
                "  |\"|      \\/\"_ \\U  /\"\\  u \n" +
                "U | | u    | | | |\\/ _ \\/  \n" +
                " \\| |/_.-,_| |_| |/ ___ \\  \n" +
                "  |_____\\_)-\\___//_/   \\_\\ \n" +
                "  //  \\\\     \\\\   \\\\    >> \n" +
                " (_\")(\"_)   (__) (__)  (__)");
        Thread.sleep(200);
        MainApplication.clearScreen();
        System.out.println("   _      U  ___ u   _     ____   \n" +
                "  |\"|      \\/\"_ \\U  /\"\\  u|  _\"\\  \n" +
                "U | | u    | | | |\\/ _ \\//| | | | \n" +
                " \\| |/_.-,_| |_| |/ ___ \\U| |_| |\\\n" +
                "  |_____\\_)-\\___//_/   \\_\\|____/ u\n" +
                "  //  \\\\     \\\\   \\\\    >> |||_   \n" +
                " (_\")(\"_)   (__) (__)  (__(__)_)  ");
        Thread.sleep(200);
        MainApplication.clearScreen();
        System.out.println("   _      U  ___ u   _     ____            \n" +
                "  |\"|      \\/\"_ \\U  /\"\\  u|  _\"\\   ___     \n" +
                "U | | u    | | | |\\/ _ \\//| | | | |_\"_|    \n" +
                " \\| |/_.-,_| |_| |/ ___ \\U| |_| |\\ | |     \n" +
                "  |_____\\_)-\\___//_/   \\_\\|____/ U/| |\\u   \n" +
                "  //  \\\\     \\\\   \\\\    >> |||.-,_|___|_,-.\n" +
                " (_\")(\"_)   (__) (__)  (__(__)_\\_)-' '-(_/ ");
        Thread.sleep(200);
        MainApplication.clearScreen();
        System.out.println("   _      U  ___ u   _     ____            _   _    \n" +
                "  |\"|      \\/\"_ \\U  /\"\\  u|  _\"\\   ___    | \\ |\"|   \n" +
                "U | | u    | | | |\\/ _ \\//| | | | |_\"_|  <|  \\| |>  \n" +
                " \\| |/_.-,_| |_| |/ ___ \\U| |_| |\\ | |   U| |\\  |u  \n" +
                "  |_____\\_)-\\___//_/   \\_\\|____/ U/| |\\u  |_| \\_|   \n" +
                "  //  \\\\     \\\\   \\\\    >> |||.-,_|___|_,-||   \\\\,-.\n" +
                " (_\")(\"_)   (__) (__)  (__(__)_\\_)-' '-(_/(_\")  (_/ ");
        Thread.sleep(200);
        MainApplication.clearScreen();
        System.out.println("   _      U  ___ u   _     ____            _   _    ____  \n" +
                "  |\"|      \\/\"_ \\U  /\"\\  u|  _\"\\   ___    | \\ |\"|U /\"___|u\n" +
                "U | | u    | | | |\\/ _ \\//| | | | |_\"_|  <|  \\| |\\| |  _ /\n" +
                " \\| |/_.-,_| |_| |/ ___ \\U| |_| |\\ | |   U| |\\  |u| |_| | \n" +
                "  |_____\\_)-\\___//_/   \\_\\|____/ U/| |\\u  |_| \\_|  \\____| \n" +
                "  //  \\\\     \\\\   \\\\    >> |||.-,_|___|_,-||   \\\\,-_)(|_  \n" +
                " (_\")(\"_)   (__) (__)  (__(__)_\\_)-' '-(_/(_\")  (_(__)__) ");
        Thread.sleep(200);
        MainApplication.clearScreen();
    }

    public static void logo() throws IOException, InterruptedException {
        MainApplication.clearScreen();
        System.out.println(" .----------------. .----------------. .----------------. \n" +
                "| .--------------. | .--------------. | .--------------. |\n" +
                "| |      __      | | |   ______     | | |     ______   | |\n" +
                "| |     /  \\     | | |  |_   _ \\    | | |   .' ___  |  | |\n" +
                "| |    / /\\ \\    | | |    | |_) |   | | |  / .'   \\_|  | |\n" +
                "| |   / ____ \\   | | |    |  __'.   | | |  | |         | |\n" +
                "| | _/ /    \\ \\_ | | |   _| |__) |  | | |  \\ `.___.'\\  | |\n" +
                "| ||____|  |____|| | |  |_______/   | | |   `._____.'  | |\n" +
                "| |              | | |              | | |              | |\n" +
                "| '--------------' | '--------------' | '--------------' |\n" +
                " '----------------' '----------------' '----------------' ");
    }

    // 사용자가 'Enter' 입력할때까지 sleep() 상태를 유지하는 함수
    public static void waitMoment() throws IOException, InterruptedException {
        System.out.println("Enter 키를 눌러주세요...");
        Thread sleepThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000); // 1초 동안 슬립
                }
            } catch (InterruptedException e) {
                System.out.println("슬립이 해제되었습니다.");
            }
        });
        // 스레드 시작
        sleepThread.start();
        // 사용자의 Enter 키 입력을 기다림
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Enter 키 입력 대기
        // 사용자가 Enter 키를 누르면 슬립 스레드를 중단
        sleepThread.interrupt();
        MainApplication.clearScreen();
    }
}
