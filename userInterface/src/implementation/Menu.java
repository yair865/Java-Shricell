package implementation;

import interfaces.UI;

public enum Menu  {

    LOAD_SPREADSHEET {
        @Override
        void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleLoadFile();
        }
    },
    DISPLAY_SPREADSHEET {
        @Override
        void invoke(UI consoleUserInterface) {
            consoleUserInterface.displaySpreadSheet();;
        }
    },
    DISPLAY_CELL_VALUE {
        @Override
        void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleDisplayCell();
        }
    },
    UPDATE_CELL_VALUE {
        @Override
        void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleUpdateCell();
        }
    },
    DISPLAY_VERSION_HISTORY {
        @Override
        void invoke(UI consoleUserInterface) {
            consoleUserInterface.displayVersions();
        }
    },
    EXIT {
        @Override
        void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleExit();
        }
    };

    abstract void invoke(UI consoleUserInterface);
};

