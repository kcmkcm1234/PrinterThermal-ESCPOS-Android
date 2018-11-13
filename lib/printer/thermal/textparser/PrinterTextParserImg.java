package lib.printer.thermal.textparser;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import lib.printer.thermal.Printer;
import lib.printer.thermal.PrinterCommands;


public class PrinterTextParserImg implements PrinterTextParserElement {
    
    public static String bitmapToHexadecimalString(Printer printer, Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return PrinterTextParserImg.bitmapToHexadecimalString(printer, (BitmapDrawable) drawable);
        }
        return "";
    }
    
    public static String bitmapToHexadecimalString(Printer printer, BitmapDrawable bitmapDrawable) {
        return PrinterTextParserImg.bitmapToHexadecimalString(printer, bitmapDrawable.getBitmap());
    }
    
    public static String bitmapToHexadecimalString(Printer printer, Bitmap bitmap) {
        return PrinterTextParserImg.bytesToHexadecimalString(Printer.bitmapToBytes(printer, bitmap));
    }
    
    public static String bytesToHexadecimalString(byte[] bytes) {
        StringBuffer imageHexString = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            imageHexString.append(hexString);
        }
        return imageHexString.toString();
    }
    
    
    public static byte[] hexadecimalStringToBytes(String hexString) {
        byte[] bytes = new byte[0];
        
        try {
            bytes = new byte[hexString.length() / 2];
            for (int i = 0; i < bytes.length; i++) {
                int pos = i * 2;
                bytes[i] = (byte) Integer.parseInt(hexString.substring(pos, pos + 2), 16);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bytes;
    }
    
    
    private int length = 0;
    private byte[] image;
    
    public PrinterTextParserImg(PrinterTextParserColumn printerTextParserColumn, String textAlign, String hexadecimalString) {
        Printer printer = printerTextParserColumn.getLine().getTextParser().getPrinter();
        
        byte[] image = PrinterTextParserImg.hexadecimalStringToBytes(hexadecimalString);
        
        int byteWidth = ((int) image[4] & 0xFF),
            width = byteWidth * 8,
            height = ((int) image[6] & 0xFF),
            nbrByteDiff = (int) Math.floor(((float) (printer.getPrintingWidthPx() - width)) / 8f),
            nbrWhiteByteToInsert = 0;
        
        switch (textAlign) {
            case PrinterTextParser.TAGS_ALIGN_CENTER:
                nbrWhiteByteToInsert = Math.round(((float) nbrByteDiff) / 2f);
                break;
            case PrinterTextParser.TAGS_ALIGN_RIGHT:
                nbrWhiteByteToInsert = nbrByteDiff;
                break;
        }
        
        if (nbrWhiteByteToInsert > 0) {
            int newByteWidth = byteWidth + nbrWhiteByteToInsert;
            byte[] newImage = new byte[newByteWidth * height + 8];
            System.arraycopy(image, 0, newImage, 0, 8);
            newImage[4] = (byte) newByteWidth;
            for (int i = 0; i < height; i++) {
                System.arraycopy(image, (byteWidth * i + 8), newImage, (newByteWidth * i + nbrWhiteByteToInsert + 8), byteWidth);
            }
            image = newImage;
        }
        
        this.length = (int) Math.ceil(((float) (((int) image[4] & 0xFF) * 8)) / ((float) printer.getCharSizeWidthPx()));
        this.image = image;
    }
    
    public byte[] getImage() {
        return this.image;
    }
    
    @Override
    public int length() {
        return this.length;
    }
    @Override
    public byte[] getAlign() {
        return PrinterCommands.TEXT_ALIGN_LEFT;
    }
}
