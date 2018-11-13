package lib.printer.thermal.textparser;

import java.util.Hashtable;

import lib.printer.thermal.Printer;
import lib.printer.thermal.PrinterCommands;

public class PrinterTextParserBarCode implements PrinterTextParserElement {
    
    private int length;
    private int height;
    private byte[] align;
    private String code;
    private int barCodeType;
    
    public PrinterTextParserBarCode(PrinterTextParserColumn printerTextParserColumn, String textAlign, Hashtable<String, String> barCodeAttributes, String code) {
        Printer printer = printerTextParserColumn.getLine().getTextParser().getPrinter();
        code = code.trim();
        
        this.align = PrinterCommands.TEXT_ALIGN_LEFT;
        switch (textAlign) {
            case PrinterTextParser.TAGS_ALIGN_CENTER:
                this.align = PrinterCommands.TEXT_ALIGN_CENTER;
                break;
            case PrinterTextParser.TAGS_ALIGN_RIGHT:
                this.align = PrinterCommands.TEXT_ALIGN_RIGHT;
                break;
        }
        
        this.barCodeType = PrinterCommands.BARCODE_EAN13;
        switch (barCodeAttributes.get(PrinterTextParser.ATTR_BARCODE_TYPE)) {
            case PrinterTextParser.ATTR_BARCODE_TYPE_EAN8:
                this.barCodeType = PrinterCommands.BARCODE_EAN8;
                break;
            case PrinterTextParser.ATTR_BARCODE_TYPE_UPCA:
                this.barCodeType = PrinterCommands.BARCODE_UPCA;
                break;
            case PrinterTextParser.ATTR_BARCODE_TYPE_UPCE:
                this.barCodeType = PrinterCommands.BARCODE_UPCE;
                break;
        }
        
        this.length = printer.getNbrCharactersPerLine();
        
        
        this.height = printer.mmToPx(10f);
        try {
            if (barCodeAttributes.containsKey(PrinterTextParser.ATTR_BARCODE_HEIGHT)) {
                this.height = printer.mmToPx(Float.parseFloat(barCodeAttributes.get(PrinterTextParser.ATTR_BARCODE_HEIGHT)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public int getBarCodeType() {
        return this.barCodeType;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    @Override
    public int length() {
        return this.length;
    }
    
    @Override
    public byte[] getAlign() {
        return this.align;
    }
}
