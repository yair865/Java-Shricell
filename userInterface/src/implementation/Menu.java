package implementation;

public enum Menu  {

       LOAD_SPREADSHEET {
           @Override
           void invoke(ConsoleUI consoleUserInterface) {
               consoleUserInterface.handleLoadFile();
           }
       };

    abstract void invoke(ConsoleUI consoleUserInterface);
};

