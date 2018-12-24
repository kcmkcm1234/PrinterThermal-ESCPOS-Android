package lib.printer.thermal.textparser;

import java.util.Arrays;

import lib.printer.thermal.PrinterCommands;

public class PrinterTextParserString implements PrinterTextParserElement {
    private String text;
    private byte[] textSize;
    private byte[] textBold;
    private byte[] textUnderline;
    
    public PrinterTextParserString(String text, byte[] textSize, byte[] textBold, byte[] textUnderline) {
        this.text = text;
        this.textSize = textSize;
        this.textBold = textBold;
        this.textUnderline = textUnderline;
    }
    
    public String getText() {
        return text;
    }
    
    public byte[] getTextSize() {
        return textSize;
    }
    
    public byte[] getTextBold() {
        return textBold;
    }
    
    public byte[] getTextUnderline() {
        return textUnderline;
    }
    
    
    @Override
    public int length() {
        int coef = 1;
        
        if (Arrays.equals(this.textSize, PrinterCommands.TEXT_SIZE_DOUBLE_WIDTH) || Arrays.equals(this.textSize, PrinterCommands.TEXT_SIZE_BIG)) {
            coef = 2;
        }
        
        return this.text.length() * coef;
    }
    @Override
    public byte[] getAlign() {
        return PrinterCommands.TEXT_ALIGN_LEFT;
    }
}
