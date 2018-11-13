package lib.printer.thermal;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import lib.printer.thermal.bluetooth.BluetoothPrinterSocketConnexion;
import lib.printer.thermal.textparser.PrinterTextParser;
import lib.printer.thermal.textparser.PrinterTextParserBarCode;
import lib.printer.thermal.textparser.PrinterTextParserColumn;
import lib.printer.thermal.textparser.PrinterTextParserElement;
import lib.printer.thermal.textparser.PrinterTextParserImg;
import lib.printer.thermal.textparser.PrinterTextParserLine;
import lib.printer.thermal.textparser.PrinterTextParserString;

public class Printer {
    
    public static final float INCH_TO_MM = 25.4f;
    
    
    public static byte[] bitmapToBytes(Printer printer, Bitmap bitmap) {
        boolean isSizeEdit = false;
        int bitmapWidth = bitmap.getWidth(),
            bitmapHeight = bitmap.getHeight(),
            maxWidth = printer.getPrintingWidthPx(),
            maxHeight = 256;
        
        if (bitmapWidth > maxWidth) {
            bitmapHeight = Math.round(((float) bitmapHeight) * ((float) maxWidth) / ((float) bitmapWidth));
            bitmapWidth = maxWidth;
            isSizeEdit = true;
        }
        if (bitmapHeight > maxHeight) {
            bitmapWidth = Math.round(((float) bitmapWidth) * ((float) maxHeight) / ((float) bitmapHeight));
            bitmapHeight = maxHeight;
            isSizeEdit = true;
        }
        
        if (isSizeEdit) {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false);
        }
        
        return BluetoothPrinterSocketConnexion.bitmapToBytes(bitmap);
    }
    
    
    
    private int printerDpi;
    private float printingWidthMM;
    private int nbrCharactersPerLine;
    private int printingWidthPx;
    private int charSizeWidthPx;
    
    
    private BluetoothPrinterSocketConnexion bluetoothPrinter = null;
    
    public Printer(BluetoothPrinterSocketConnexion printer, int printerDpi, float printingWidthMM, int nbrCharactersPerLine) {
        if (printer != null && (printer.isConnected() || (!printer.isConnected() && printer.connect()))) {
            this.bluetoothPrinter = printer;
        }
        this.printerDpi = printerDpi;
        this.printingWidthMM = printingWidthMM;
        this.nbrCharactersPerLine = nbrCharactersPerLine;
        
        int printingWidthPx = this.mmToPx(this.printingWidthMM);
        this.printingWidthPx = printingWidthPx + (printingWidthPx % 8);
        
        this.charSizeWidthPx = printingWidthPx / this.nbrCharactersPerLine;
    }
    
    public Printer disconnectPrinter() {
        if (this.bluetoothPrinter != null) {
            this.bluetoothPrinter.disconnect();
            this.bluetoothPrinter = null;
        }
        return this;
    }
    
    public int getNbrCharactersPerLine() {
        return this.nbrCharactersPerLine;
    }
    
    public float getPrintingWidthMM() {
        return this.printingWidthMM;
    }
    
    public int getPrinterDpi() {
        return this.printerDpi;
    }
    
    public int getPrintingWidthPx() {
        return this.printingWidthPx;
    }
    
    public int getCharSizeWidthPx() {
        return charSizeWidthPx;
    }
    
    public String bitmapToHexadecimalString(Drawable drawable) {
        return PrinterTextParserImg.bitmapToHexadecimalString(this, drawable);
    }
    
    public String bitmapToHexadecimalString(BitmapDrawable drawable) {
        return PrinterTextParserImg.bitmapToHexadecimalString(this, drawable);
    }
    
    public String bitmapToHexadecimalString(Bitmap bitmap) {
        return PrinterTextParserImg.bitmapToHexadecimalString(this, bitmap);
    }
    
    public int mmToPx(float mmSize) {
        return Math.round(mmSize * ((float) this.printerDpi) / Printer.INCH_TO_MM);
    }
    
    
    public Printer printFormattedText(String text) {
        if (this.bluetoothPrinter == null || this.nbrCharactersPerLine == 0) {
            return this;
        }
        
        PrinterTextParser textParser = new PrinterTextParser(this);
        PrinterTextParserLine[] linesParsed = textParser
            .setFormattedText(text)
            .parse();
        
        for (PrinterTextParserLine line : linesParsed) {
            PrinterTextParserColumn[] columns = line.getColumns();
            
            for (PrinterTextParserColumn column : columns) {
                PrinterTextParserElement[] elements = column.getElements();
                for (PrinterTextParserElement element : elements) {
                    if(element instanceof PrinterTextParserString) {
                        PrinterTextParserString string = (PrinterTextParserString) element;
                        this.bluetoothPrinter.printText(string.getText(), string.getTextSize(), string.getTextBold(), string.getTextUnderline());
                    } else if(element instanceof PrinterTextParserImg) {
                        PrinterTextParserImg img = (PrinterTextParserImg) element;
                        this.bluetoothPrinter.printImage(img.getImage());
                    } else if(element instanceof PrinterTextParserBarCode) {
                        PrinterTextParserBarCode barCode = (PrinterTextParserBarCode) element;
                        this.bluetoothPrinter
                            .setAlign(barCode.getAlign())
                            .printBarCode(barCode.getBarCodeType(), barCode.getCode(), barCode.getHeight());
                    }
                }
            }
            this.bluetoothPrinter.newLine();
        }
        
        this.bluetoothPrinter
            .newLine()
            .newLine()
            .newLine()
            .newLine();
        
        return this;
    }
    
}
