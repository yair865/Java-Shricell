package implementation;

import api.UI;

public enum Menu  {

    LOAD_SPREADSHEET("Load Spreadsheet from XML File") {
        @Override
        public void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleLoadXMLFile();
        }
    },
    DISPLAY_SPREADSHEET("Display Current Spreadsheet") {
        @Override
        public void invoke(UI consoleUserInterface) {
            consoleUserInterface.displaySpreadSheet();
        }
    },
    DISPLAY_CELL_VALUE("Display Cell Value") {
        @Override
        public void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleDisplayCell();
        }
    },
    UPDATE_CELL_VALUE("Update Cell Value") {
        @Override
        public void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleUpdateCell();
        }
    },
    DISPLAY_VERSION_HISTORY("Display Version History") {
        @Override
        public void invoke(UI consoleUserInterface) {
            consoleUserInterface.displayVersions();
        }
    },
    SAVE_SYSTEM_STATE("Save System State to a File") {
        @Override
        public void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleSaveSystemState();
        }
    },
    LOAD_SYSTEM_STATE("Load System State from a File") {
        @Override
        public void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleLoadSystemState();
        }
    },
    EXIT("Exit") {
        @Override
        public void invoke(UI consoleUserInterface) {
            consoleUserInterface.handleExit();
        }
    };

    private final String menuOption;
    Menu(String commandToExecute) {
        this.menuOption = commandToExecute;
    }

    public abstract void invoke(UI consoleUserInterface);

    @Override
    public String toString() {
        return menuOption;
    }
}
