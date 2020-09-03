package ni.gob.minsa.alerta.utilities.excelUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class builds an Excel spreadsheet document using Apache POI library.
 * @author www.codejava.net
 *
 */
public class ExcelBuilder extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        String reporte = model.get("reporte").toString();
        if (reporte.equalsIgnoreCase("DXVIG")) {
            buildExcelDocumentVigDx(model, workbook);
        }else if (reporte.equalsIgnoreCase("RESULTDX")){
            buildExcelResultDx(model, workbook);
        }
	}

    public void buildExcelDocumentVigDx(Map<String, Object> model, HSSFWorkbook workbook){
        List<Object[]> listaDxPos = (List<Object[]>) model.get("listaDxPos");
        List<Object[]> listaDxNeg = (List<Object[]>) model.get("listaDxNeg");
        List<Object[]> listaDxInadec = (List<Object[]>) model.get("listaDxInadec");

        List<String> columnas = (List<String>) model.get("columnas");
        boolean incluirMxInadecuadas = (boolean)model.get("incluirMxInadecuadas");
        boolean mostrarTabla1 = (boolean)model.get("mostrarTabla1");
        boolean mostrarTabla2 = (boolean)model.get("mostrarTabla2");
        String tipoReporte =  model.get("tipoReporte").toString();
        // create a new Excel sheet
        HSSFSheet sheet = workbook.createSheet(tipoReporte);
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeight((short)(11*20));
        font.setColor(HSSFColor.BLACK.index);
        headerStyle.setFont(font);

        //Cell style for content cells
        font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeight((short)(11*20));
        font.setColor(HSSFColor.BLACK.index);

        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
        dateCellStyle.setBorderBottom(BorderStyle.THIN);
        dateCellStyle.setBorderTop(BorderStyle.THIN);
        dateCellStyle.setBorderLeft(BorderStyle.THIN);
        dateCellStyle.setBorderRight(BorderStyle.THIN);
        dateCellStyle.setFont(font);

        CellStyle contentCellStyle = workbook.createCellStyle();
        contentCellStyle.setBorderBottom(BorderStyle.THIN);
        contentCellStyle.setBorderTop(BorderStyle.THIN);
        contentCellStyle.setBorderLeft(BorderStyle.THIN);
        contentCellStyle.setBorderRight(BorderStyle.THIN);
        contentCellStyle.setFont(font);

        CellStyle noDataCellStyle = workbook.createCellStyle();
        noDataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        noDataCellStyle.setFont(font);
        // create data rows
        int rowCount = 4;
        int filaInicioNeg = 0;

        if (mostrarTabla1) {
            //tabla con dx positivos
            // create header row
            HSSFRow header = sheet.createRow(3);
            setHeaderTable(header, headerStyle, columnas);

            for (Object[] registro : listaDxPos) {
                HSSFRow aRow = sheet.createRow(rowCount++);
                setRowData(aRow, registro, contentCellStyle, dateCellStyle);
            }
            if (listaDxPos.size() <= 0) {
                HSSFRow aRow = sheet.createRow(rowCount++);
                sheet.addMergedRegion(new CellRangeAddress(aRow.getRowNum(), aRow.getRowNum(), 0, columnas.size() - 1));
                aRow.createCell(0).setCellValue(model.get("sinDatos").toString());
                aRow.getCell(0).setCellStyle(noDataCellStyle);
            }
        }

        if (mostrarTabla2) {
            //tabla con dx negativos
            rowCount += 2; // PARA DEJAR UNA FILA EN BLANCO ENTRE AMBAS TABLAS
            filaInicioNeg = rowCount++;
            HSSFRow headerPos = sheet.createRow(rowCount++);
            setHeaderTable(headerPos, headerStyle, columnas);
            for (Object[] registro : listaDxNeg) {
                HSSFRow aRow = sheet.createRow(rowCount++);
                setRowData(aRow, registro, contentCellStyle, dateCellStyle);
            }
            if (listaDxNeg.size() <= 0) {
                HSSFRow aRow = sheet.createRow(rowCount);
                sheet.addMergedRegion(new CellRangeAddress(aRow.getRowNum(), aRow.getRowNum(), 0, columnas.size() - 1));
                aRow.createCell(0).setCellValue(model.get("sinDatos").toString());
                aRow.getCell(0).setCellStyle(noDataCellStyle);
            }
        }
        for(int i =0;i<columnas.size();i++){
            sheet.autoSizeColumn(i);
        }

        // create style for title cells
        CellStyle titleStyle = workbook.createCellStyle();
        font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeight((short)(16*20));
        font.setColor(HSSFColor.BLACK.index);
        titleStyle.setFont(font);

        // create style for filters cells
        CellStyle filterStyle = workbook.createCellStyle();
        font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeight((short)(14*20));
        font.setColor(HSSFColor.BLACK.index);
        filterStyle.setFont(font);

        HSSFRow titulo = sheet.createRow(0);
        titulo.createCell(1).setCellValue(model.get("titulo").toString());
        titulo.getCell(1).setCellStyle(titleStyle);

        HSSFRow subtitulo = sheet.createRow(1);
        subtitulo.createCell(1).setCellValue(model.get("subtitulo").toString());
        subtitulo.getCell(1).setCellStyle(titleStyle);

        if (mostrarTabla1) {
            HSSFRow filtros = sheet.createRow(2);
            filtros.createCell(1).setCellValue(model.get("tablaPos").toString());
            filtros.getCell(1).setCellStyle(filterStyle);
        }

        if (mostrarTabla2) {
            HSSFRow filtrosNeg = sheet.createRow(filaInicioNeg);
            filtrosNeg.createCell(1).setCellValue(model.get("tablaNeg").toString());
            filtrosNeg.getCell(1).setCellStyle(filterStyle);
        }

        if (incluirMxInadecuadas ){
            // create a new Excel sheet
            HSSFSheet sheetInadec = workbook.createSheet("MX INADEC");
            sheetInadec.setDefaultColumnWidth(30);
            //tabla con dx muestras inadecuadas
            // create header row
            HSSFRow headerInadec = sheetInadec.createRow(3);
            setHeaderTable(headerInadec, headerStyle, columnas);
            // create data rows
            rowCount = 4;

            for (Object[] registro : listaDxInadec) {
                HSSFRow aRow = sheetInadec.createRow(rowCount++);
                setRowData(aRow, registro, contentCellStyle, dateCellStyle);
            }
            if (listaDxInadec.size()<=0){
                HSSFRow aRow = sheetInadec.createRow(rowCount);
                sheetInadec.addMergedRegion(new CellRangeAddress(rowCount, rowCount,0,columnas.size()-1));
                aRow.createCell(0).setCellValue(model.get("sinDatos").toString());
                aRow.getCell(0).setCellStyle(noDataCellStyle);
            }
            for(int i =0;i<columnas.size();i++){
                sheetInadec.autoSizeColumn(i);
            }

            HSSFRow tituloInadec = sheetInadec.createRow(0);
            tituloInadec.createCell(1).setCellValue(model.get("titulo").toString());
            tituloInadec.getCell(1).setCellStyle(titleStyle);

            HSSFRow subtituloInadec = sheetInadec.createRow(1);
            subtituloInadec.createCell(1).setCellValue(model.get("subtitulo").toString());
            subtituloInadec.getCell(1).setCellStyle(titleStyle);

            HSSFRow filtroInadec = sheetInadec.createRow(2);
            filtroInadec.createCell(1).setCellValue(model.get("tablaMxInadec").toString());
            filtroInadec.getCell(1).setCellStyle(filterStyle);

        }
    }

    public void buildExcelResultDx(Map<String, Object> model, HSSFWorkbook workbook){
        List<String> columnas = (List<String>) model.get("columnas");
        List<Object[]> datos = (List<Object[]>) model.get("datos");
        String tipoReporte =  model.get("reporte").toString();

        // create style for title cells
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeight((short)(16*20));
        font.setColor(HSSFColor.BLACK.index);
        titleStyle.setFont(font);

        // create style for header cells
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeight((short)(11*20));
        font.setColor(HSSFColor.BLACK.index);

        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
        dateCellStyle.setBorderBottom(BorderStyle.THIN);
        dateCellStyle.setBorderTop(BorderStyle.THIN);
        dateCellStyle.setBorderLeft(BorderStyle.THIN);
        dateCellStyle.setBorderRight(BorderStyle.THIN);
        dateCellStyle.setFont(font);

        CellStyle contentCellStyle = workbook.createCellStyle();
        contentCellStyle.setBorderBottom(BorderStyle.THIN);
        contentCellStyle.setBorderTop(BorderStyle.THIN);
        contentCellStyle.setBorderLeft(BorderStyle.THIN);
        contentCellStyle.setBorderRight(BorderStyle.THIN);
        contentCellStyle.setFont(font);

        CellStyle noDataCellStyle = workbook.createCellStyle();
        noDataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        noDataCellStyle.setFont(font);

        font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeight((short)(11*20));
        font.setColor(HSSFColor.RED.index);

        CellStyle alertCellStyle = workbook.createCellStyle();
        alertCellStyle.setBorderBottom(BorderStyle.THIN);
        alertCellStyle.setBorderTop(BorderStyle.THIN);
        alertCellStyle.setBorderLeft(BorderStyle.THIN);
        alertCellStyle.setBorderRight(BorderStyle.THIN);
        alertCellStyle.setFont(font);

        // create a new Excel sheet
        HSSFSheet sheet = workbook.createSheet(tipoReporte);
        sheet.setDefaultColumnWidth(30);

        HSSFRow header = sheet.createRow(3);
        setHeaderTable(header, headerStyle, columnas);
        int rowCount = 4;
        for (Object[] registro : datos) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            setRowData(aRow, registro, contentCellStyle, dateCellStyle);
        }

        if (datos.size() <= 0) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            sheet.addMergedRegion(new CellRangeAddress(aRow.getRowNum(), aRow.getRowNum(), 0, columnas.size() - 1));
            aRow.createCell(0).setCellValue(model.get("sinDatos").toString());
            aRow.getCell(0).setCellStyle(noDataCellStyle);
        }else{
            //estilo para celdas de totales en la �ltima fila
            Font font2 = workbook.createFont();
            font2.setFontName("Arial");
            font2.setFontHeight((short) (10 * 20));
            font2.setColor(HSSFColor.BLACK.index);
            font2.setBold(true);
            CellStyle totalCellStyle = workbook.createCellStyle();
            totalCellStyle.setAlignment(HorizontalAlignment.CENTER);
            totalCellStyle.setBorderBottom(BorderStyle.THIN);
            totalCellStyle.setBorderTop(BorderStyle.THIN);
            totalCellStyle.setBorderLeft(BorderStyle.THIN);
            totalCellStyle.setBorderRight(BorderStyle.THIN);
            totalCellStyle.setFont(font2);
            //totalColumnas restar 2 para omitir la primer columna que tiene la entidad y la �ltima que tiene el %P
            setRowTotalsDat(sheet, contentCellStyle, totalCellStyle, rowCount++, columnas.size()-2, 5, datos.size()+4);
        }
        //validar positividad
        for(int i=4; i < rowCount-1; i++){
            HSSFRow aRow = sheet.getRow(i);
            //validar valores positivos
            for (int j=2; j < columnas.size()-5; j++){
                HSSFCell aCell = aRow.getCell(j);
                Double valor = aCell.getNumericCellValue();
                if (valor>0){
                    aCell.setCellStyle(alertCellStyle);
                }
            }
            HSSFCell aCell = aRow.getCell(columnas.size()-1); //en la �ltima que esta la positividad
            Double valor = aCell.getNumericCellValue();
            if (valor>0){
                aCell.setCellStyle(alertCellStyle);
            }
        }
        //ajustar el ancho de la celda al tamanio del texto
        for(int i =0;i<columnas.size();i++){
            sheet.autoSizeColumn(i);
        }

        HSSFRow titulo = sheet.createRow(0);
        titulo.createCell(0).setCellValue(model.get("titulo").toString());
        titulo.getCell(0).setCellStyle(titleStyle);

        HSSFRow subtitulo = sheet.createRow(1);
        subtitulo.createCell(0).setCellValue(model.get("subtitulo").toString());
        subtitulo.getCell(0).setCellStyle(titleStyle);

        HSSFRow rangos = sheet.createRow(2);
        rangos.createCell(0).setCellValue(model.get("rangoFechas").toString());
        rangos.getCell(0).setCellStyle(titleStyle);
    }

    public HSSFWorkbook buildExcel(Map<String, Object> model){
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<Object[]> listaDxPos = (List<Object[]>) model.get("listaDxPos");
        List<Object[]> listaDxNeg = (List<Object[]>) model.get("listaDxNeg");
        List<Object[]> listaDxInadec = (List<Object[]>) model.get("listaDxInadec");

        List<String> columnas = (List<String>) model.get("columnas");
        boolean incluirMxInadecuadas = (boolean)model.get("incluirMxInadecuadas");
        String tipoReporte =  model.get("tipoReporte").toString();
        // create a new Excel sheet
        HSSFSheet sheet = workbook.createSheet(tipoReporte);
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeight((short)(11*20));
        font.setColor(HSSFColor.BLACK.index);
        headerStyle.setFont(font);

        //Cell style for content cells
        font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeight((short)(11*20));
        font.setColor(HSSFColor.BLACK.index);

        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
        dateCellStyle.setBorderBottom(BorderStyle.THIN);
        dateCellStyle.setBorderTop(BorderStyle.THIN);
        dateCellStyle.setBorderLeft(BorderStyle.THIN);
        dateCellStyle.setBorderRight(BorderStyle.THIN);
        dateCellStyle.setFont(font);

        CellStyle contentCellStyle = workbook.createCellStyle();
        contentCellStyle.setBorderBottom(BorderStyle.THIN);
        contentCellStyle.setBorderTop(BorderStyle.THIN);
        contentCellStyle.setBorderLeft(BorderStyle.THIN);
        contentCellStyle.setBorderRight(BorderStyle.THIN);
        contentCellStyle.setFont(font);

        CellStyle noDataCellStyle = workbook.createCellStyle();
        noDataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        noDataCellStyle.setFont(font);

        //tabla con dx positivos
        // create header row
        HSSFRow header = sheet.createRow(3);
        setHeaderTable(header, headerStyle, columnas);
        // create data rows
        int rowCount = 4;
        int filaInicioNeg = 0;

        for (Object[] registro : listaDxPos) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            setRowData(aRow, registro, contentCellStyle, dateCellStyle);
        }
        if (listaDxPos.size()<=0){
            HSSFRow aRow = sheet.createRow(rowCount++);
            sheet.addMergedRegion(new CellRangeAddress(aRow.getRowNum(), aRow.getRowNum(),0,columnas.size()-1));
            aRow.createCell(0).setCellValue(model.get("sinDatos").toString());
            aRow.getCell(0).setCellStyle(noDataCellStyle);
        }

        //tabla con dx negativos
        rowCount+=2; // PARA DEJAR UNA FILA EN BLANCO ENTRE AMBAS TABLAS
        filaInicioNeg = rowCount++;
        HSSFRow headerPos = sheet.createRow(rowCount++);
        setHeaderTable(headerPos, headerStyle, columnas);
        for (Object[] registro : listaDxNeg) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            setRowData(aRow, registro, contentCellStyle, dateCellStyle);
        }
        if (listaDxNeg.size()<=0){
            HSSFRow aRow = sheet.createRow(rowCount);
            sheet.addMergedRegion(new CellRangeAddress(aRow.getRowNum(), aRow.getRowNum(),0,columnas.size()-1));
            aRow.createCell(0).setCellValue(model.get("sinDatos").toString());
            aRow.getCell(0).setCellStyle(noDataCellStyle);
        }
        for(int i =0;i<columnas.size();i++){
            sheet.autoSizeColumn(i);
        }

        // create style for title cells
        CellStyle titleStyle = workbook.createCellStyle();
        font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeight((short)(16*20));
        font.setColor(HSSFColor.BLACK.index);
        titleStyle.setFont(font);

        // create style for filters cells
        CellStyle filterStyle = workbook.createCellStyle();
        font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeight((short)(14*20));
        font.setColor(HSSFColor.BLACK.index);
        filterStyle.setFont(font);

        HSSFRow titulo = sheet.createRow(0);
        titulo.createCell(1).setCellValue(model.get("titulo").toString());
        titulo.getCell(1).setCellStyle(titleStyle);

        HSSFRow subtitulo = sheet.createRow(1);
        subtitulo.createCell(1).setCellValue(model.get("subtitulo").toString());
        subtitulo.getCell(1).setCellStyle(titleStyle);

        HSSFRow filtros = sheet.createRow(2);
        filtros.createCell(1).setCellValue(model.get("tablaPos").toString());
        filtros.getCell(1).setCellStyle(filterStyle);

        HSSFRow filtrosNeg = sheet.createRow(filaInicioNeg);
        filtrosNeg.createCell(1).setCellValue(model.get("tablaNeg").toString());
        filtrosNeg.getCell(1).setCellStyle(filterStyle);

        if (incluirMxInadecuadas){
            // create a new Excel sheet
            HSSFSheet sheetInadec = workbook.createSheet("MX INADEC");
            sheetInadec.setDefaultColumnWidth(30);
            //tabla con dx muestras inadecuadas
            // create header row
            HSSFRow headerInadec = sheetInadec.createRow(3);
            setHeaderTable(headerInadec, headerStyle, columnas);
            // create data rows
            rowCount = 4;

            for (Object[] registro : listaDxInadec) {
                HSSFRow aRow = sheetInadec.createRow(rowCount++);
                setRowData(aRow, registro, contentCellStyle, dateCellStyle);
            }
            if (listaDxInadec.size()<=0){
                HSSFRow aRow = sheetInadec.createRow(rowCount);
                sheetInadec.addMergedRegion(new CellRangeAddress(rowCount, rowCount,0,columnas.size()-1));
                aRow.createCell(0).setCellValue(model.get("sinDatos").toString());
                aRow.getCell(0).setCellStyle(noDataCellStyle);
            }
            for(int i =0;i<columnas.size();i++){
                sheetInadec.autoSizeColumn(i);
            }

            HSSFRow tituloInadec = sheetInadec.createRow(0);
            tituloInadec.createCell(1).setCellValue(model.get("titulo").toString());
            tituloInadec.getCell(1).setCellStyle(titleStyle);

            HSSFRow subtituloInadec = sheetInadec.createRow(1);
            subtituloInadec.createCell(1).setCellValue(model.get("subtitulo").toString());
            subtituloInadec.getCell(1).setCellStyle(titleStyle);

            HSSFRow filtroInadec = sheetInadec.createRow(2);
            filtroInadec.createCell(1).setCellValue(model.get("tablaMxInadec").toString());
            filtroInadec.getCell(1).setCellStyle(filterStyle);

        }

        return workbook;
    }

    private void setHeaderTable(HSSFRow header, CellStyle style, List<String> columnas){
        int indice = 0;
        for(String columna : columnas){
            header.createCell(indice).setCellValue(columna);
            header.getCell(indice).setCellStyle(style);
            indice++;
        }
    }

    private void setRowData(HSSFRow aRow, Object[] registro, CellStyle contentCellStyle, CellStyle dateCellStyle){
        int indice = 0;
        for(Object dato : registro){
            aRow.createCell(indice);
            boolean isDate= false;
            if (dato !=null){
                if (dato instanceof Date){
                    aRow.getCell(indice).setCellValue((Date)dato);
                    isDate = true;
                }else if (dato instanceof Integer){
                    aRow.getCell(indice).setCellValue((int)dato);
                }else if (dato instanceof Float){
                    aRow.getCell(indice).setCellValue((float)dato);
                }else if (dato instanceof Double){
                    aRow.getCell(indice).setCellValue((double)dato);
                }else if (dato instanceof Long){
                    aRow.getCell(indice).setCellValue((long)dato);
                }
                else{
                    aRow.createCell(indice).setCellValue(dato.toString());
                }
            }
            if (!isDate)
                aRow.getCell(indice).setCellStyle(contentCellStyle);
            else
                aRow.getCell(indice).setCellStyle(dateCellStyle);

            indice++;
        }
    }

    /**
     * M�todo para totalizar cada columna de datos en la hoja de datos del respectivo dx
     */
    private static void setRowTotalsDat(HSSFSheet sheet, CellStyle style, CellStyle styleTot, int rowCount, int totalColumnas, int indicePrimeraFila, int indiceUltimafila){
        HSSFRow aRowTot = sheet.createRow(rowCount);
        ExcelBuilder.createCell(aRowTot, "Total", 0, false, styleTot);

        for(int i = 1; i <= totalColumnas ; i++){
            String columnLetter = CellReference.convertNumToColString(i);
            String formula = "SUM("+columnLetter+indicePrimeraFila+":"+columnLetter+indiceUltimafila+")";
            ExcelBuilder.createCell(aRowTot, formula, i, true, style);
        }
    }

    /**
     * M�todo para crear una celda y ponerle el valor que va a contener deacuerdo al tipo de dato
     * @param row Fila en la que se crear� la celda
     * @param value Valor que se le asignar�
     * @param posicion n�mero de la columna en la fila (recordar que la primera celda tiene posici�n 0)
     * @param esFormula TRUE para indicar si la celda contendr� una f�rmula
     * @param style Estilo que se le aplicar� a la celda
     */
    public static void createCell(HSSFRow row, Object value, int posicion, boolean esFormula, CellStyle style){
        row.createCell(posicion);
        if (esFormula){
            row.getCell(posicion).setCellFormula(value.toString());
            row.getCell(posicion).setCellType(CellType.FORMULA);
        }else{
            if (value instanceof Integer){
                row.getCell(posicion).setCellValue((int)value);
                row.getCell(posicion).setCellType(CellType.NUMERIC);
            }else if (value instanceof Float){
                row.getCell(posicion).setCellValue((float)value);
                row.getCell(posicion).setCellType(CellType.NUMERIC);
            }else if (value instanceof Double){
                row.getCell(posicion).setCellValue((double)value);
                row.getCell(posicion).setCellType(CellType.NUMERIC);
            }
            else{
                row.createCell(posicion).setCellValue(value.toString());
                row.getCell(posicion).setCellType(CellType.STRING);
            }
        }
        row.getCell(posicion).setCellStyle(style);
    }
}