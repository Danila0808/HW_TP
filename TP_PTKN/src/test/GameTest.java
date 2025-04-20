//import Game.Console;
//import Game.Game;
//import Game.GameExitException;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.AfterAll;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.io.PrintStream;
//import java.util.Scanner;
//
//import static org.junit.jupiter.api.Assertions.*;
//
class GameTest {
//    private static final InputStream originalSystemIn = System.in;
//    private static ByteArrayOutputStream outputStream;
//    private static ByteArrayInputStream inputStream;
//    private Game game;
//
//    void setInput(String input){
//        inputStream = new ByteArrayInputStream(input.getBytes());
//        System.setIn(inputStream);
//        game = new Game();
//    }
//
//    String getOutput(){
//        return outputStream.toString().trim();
//    }
//
//    String getLine(){
//        String[] arr = getOutput().split("\n");
//        return arr[arr.length - 1];
//    }
//
//    String getLine(int ind){
//        String[] arr = getOutput().split("\n");
//        return arr[arr.length - 1 - ind];
//    }
//
//    @BeforeEach
//    void init() {
//        outputStream = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStream));
//    }
//
//    @AfterAll
//    static void returnInitState() {
//        System.setOut(System.out);
//        System.setIn(originalSystemIn);
//    }
//
//    @Test
//    void testPlayerVictory() {
//        setInput("B\n2\nT\nCROSSBOMEN\ne\ne\ne\n1\n9\nselect\n1\nd\nd\nd\nattack\n8\n9\nend");
//
//        GameExitException exception = assertThrows(GameExitException.class, () -> {
//            game.start();
//        });
//
//        assertEquals(0, exception.getStatus());
//        assertEquals(1, exception.getWinner());
//        assertEquals("Игра окончена... Вы выйграли!", exception.getMessage());
//    }
//
//    @Test
//    void testBotVictory() {
//        setInput("e\ne\ne\nend");
//
//        GameExitException exception = assertThrows(GameExitException.class, () -> {
//            game.start();
//        });
//
//        assertEquals(0, exception.getStatus());
//        assertEquals(-1, exception.getWinner());
//        assertEquals("Игра окончена... Вы проиграли!", exception.getMessage());
//    }
//
////    @Test
////    void testFatigueUpdate() {
////        setInput("s\ns\ne\ns");
////
////        game.start();
////
////        Console.PrintEvents();
////
////    }
//
////    @Test
////    void testPlayerSwitch() {
////
////    }
////
////    @Test
////    void testLighthouseExtinguish() {
////
////    }
////
////    @Test
////    void testCastleIncome() {
////
////    }
////
////    @Test
////    void testNoCastleIncome() {
////
////    }
////
////    @Test
////    void testBotCastleCapture() {
////
////    }
}
//Победа игрока +
//Победа бота +
//Обновление усталости
//Смена игрока
//Тушение маяка
//Заработок от замка
//Отсутствие заработка от замка
//Захват замка бота

//Просить исключение на обработку scanner...