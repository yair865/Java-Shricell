package engine.interfaces;

public interface Engine
{
    void loadFile(String filePath) throws Exception;
    void updateCell(String cellIdentifier, String newValue);
    int getCurrentVersion();
    void exit();
}
