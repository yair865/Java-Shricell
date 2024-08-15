package implementation;
import api.UI;

public class Main {
    public static void main(String[] args) {
        UI userInterface = new ConsoleUI();

        userInterface.executeProgram();
    }
}