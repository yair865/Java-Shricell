package dto.converter;
import dto.dtoPackage.SheetInfoDTO;
import engine.sheetmanager.SheetManager;

import java.util.List;
import java.util.stream.Collectors;

public class SheetListConverter {
    public static List<SheetInfoDTO> convertSheetsListToDTO(List<SheetManager> sheetsList) {
        return sheetsList.stream()
                .map(sheet -> new SheetInfoDTO(
                        sheet.getUserName(),
                        sheet.getSheetTitle(),
                        sheet.getSheetNumberOfRows(),
                        sheet.getSheetNumberOfColumns()))
                .collect(Collectors.toList());
    }
}

