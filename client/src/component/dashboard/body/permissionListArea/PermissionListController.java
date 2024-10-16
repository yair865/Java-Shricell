package component.dashboard.body.permissionListArea;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.io.Closeable;
import java.io.IOException;

public class PermissionListController implements Closeable {

    @FXML
    private TableView<?> permissionTableView;

    @Override
    public void close() throws IOException {

    }
}
