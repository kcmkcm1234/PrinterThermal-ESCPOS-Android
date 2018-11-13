package lib.printer.thermal.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.OutputStream;

import lib.bluetooth.BluetoothDeviceSocketConnexion;
import lib.printer.thermal.PrinterCommands;

public class BluetoothPrinterSocketConnexion extends BluetoothDeviceSocketConnexion {
    
    /**
     * Convert
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth(),
            bitmapHeight = bitmap.getHeight();

        int bytesByLine = (int) Math.ceil(((float) bitmapWidth) / 8f);
        
        byte[] imageBytes = new byte[8 + bytesByLine * bitmapHeight];
        System.arraycopy(new byte[]{0x1D, 0x76, 0x30, 0x00, (byte) bytesByLine, 0x00, (byte) bitmapHeight, 0x00}, 0, imageBytes, 0, 8);
        
        int i = 8;
        for (int posY = 0; posY < bitmapHeight; posY++) {
            for (int j = 0; j < bitmapWidth; j += 8) {
                StringBuffer stringBinary = new StringBuffer();
                for (int k = 0; k < 8; k++) {
                    int posX = j + k;
                    if (posX < bitmapWidth) {
                        int color = bitmap.getPixel(posX, posY),
                            r = (color >> 16) & 0xff,
                            g = (color >> 8) & 0xff,
                            b = color & 0xff;
                        
                        if (r > 160 && g > 160 && b > 160) {
                            stringBinary.append("0");
                        } else {
                            stringBinary.append("1");
                        }
                    } else {
                        stringBinary.append("0");
                    }
                }
                imageBytes[i++] = (byte) Integer.parseInt(stringBinary.toString(), 2);
            }
        }
        
        return imageBytes;
    }
    
    
    
    
    protected OutputStream outputStream = null;
    
    public BluetoothPrinterSocketConnexion(BluetoothDevice device) {
        super(device);
    }
    
    public boolean isOpenedStream() {
        return (this.outputStream != null);
    }
    
    @Override
    public boolean connect() {
        try {
            if (super.connect()) {
                this.outputStream = this.bluetoothSocket.getOutputStream();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.outputStream = null;
        }
        return false;
    }
    
    @Override
    public boolean disconnect() {
        super.disconnect();
        
        if (!this.isOpenedStream()) {
            return true;
        }
        
        try {
            this.outputStream.close();
            this.outputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public BluetoothPrinterSocketConnexion flush() {
        if (this.isOpenedStream()) {
            try {
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
    
    public BluetoothPrinterSocketConnexion setAlign(byte[] align) {
        if (!this.isOpenedStream()) {
            return this;
        }
        try {
            this.outputStream.write(align);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public BluetoothPrinterSocketConnexion printText(String text) {
        return this.printText(text, 0);
    }
    
    public BluetoothPrinterSocketConnexion printText(String text, int maxlength) {
        return this.printText(text, null, null, null, maxlength);
    }
    
    public BluetoothPrinterSocketConnexion printText(String text, byte[] textStyle) {
        return this.printText(text, textStyle, 0);
    }
    
    public BluetoothPrinterSocketConnexion printText(String text, byte[] textStyle, int maxlength) {
        return this.printText(text, textStyle, null, null, maxlength);
    }
    
    public BluetoothPrinterSocketConnexion printText(String text, byte[] textStyle, byte[] textBold) {
        return this.printText(text, textStyle, textBold, 0);
    }
    
    public BluetoothPrinterSocketConnexion printText(String text, byte[] textStyle, byte[] textBold, int maxlength) {
        return this.printText(text, textStyle, textBold, null, maxlength);
    }
    
    public BluetoothPrinterSocketConnexion printText(String text, byte[] textStyle, byte[] textBold, byte[] textUnderline) {
        return this.printText(text, textStyle, textBold, textUnderline, 0);
    }
    
    public BluetoothPrinterSocketConnexion printText(String text, byte[] textStyle, byte[] textBold, byte[] textUnderline, int maxlength) {
        if (!this.isOpenedStream()) {
            return this;
        }
        
        try {
            if (maxlength == 0) {
                maxlength = text.getBytes("ISO-8859-1").length;
            }
            
            this.outputStream.write(PrinterCommands.WESTERN_EUROPE_ENCODING);
            this.outputStream.write(PrinterCommands.TEXT_SIZE_NORMAL);
            this.outputStream.write(PrinterCommands.TEXT_WEIGHT_NORMAL);
            this.outputStream.write(PrinterCommands.TEXT_UNDERLINE_OFF);
            
            if (textStyle != null) {
                this.outputStream.write(textStyle);
            }
            if (textBold != null) {
                this.outputStream.write(textBold);
            }
            if (textUnderline != null) {
                this.outputStream.write(textUnderline);
            }
            
            this.outputStream.write(text.getBytes("ISO-8859-1"), 0, maxlength);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return this;
    }
    
    public BluetoothPrinterSocketConnexion printImage(Bitmap bitmap) {
        return this.printImage(BluetoothPrinterSocketConnexion.bitmapToBytes(bitmap));
    }
    public BluetoothPrinterSocketConnexion printImage(byte[] image) {
        if (!this.isOpenedStream()) {
            return this;
        }
        try {
            this.outputStream.write(image);
            Thread.sleep(PrinterCommands.TIME_BETWEEN_TWO_PRINT * 2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public BluetoothPrinterSocketConnexion printBarCode(int barCodeType, String barCode, int heightPx) {
        if (!this.isOpenedStream()) {
            return this;
        }
        
        int barCodeLength = 0;
        
        switch (barCodeType) {
            case PrinterCommands.BARCODE_UPCA:
                barCodeLength = 11;
                break;
            case PrinterCommands.BARCODE_UPCE:
                barCodeLength = 6;
                break;
            case PrinterCommands.BARCODE_EAN13:
                barCodeLength = 12;
                break;
            case PrinterCommands.BARCODE_EAN8:
                barCodeLength = 7;
                break;
        }
        
        if (barCodeLength == 0 || barCode.length() < barCodeLength) {
            return this;
        }
        
        barCode = barCode.substring(0, barCodeLength);
        
        try {
            switch (barCodeType) {
                case PrinterCommands.BARCODE_UPCE:
                    String firstChar = barCode.substring(0, 1);
                    if (!firstChar.equals("0") && !firstChar.equals("1")) {
                        barCode = "0" + barCode.substring(0, 5);
                    }
                    break;
                case PrinterCommands.BARCODE_UPCA:
                case PrinterCommands.BARCODE_EAN13:
                case PrinterCommands.BARCODE_EAN8:
                    int stringBarCodeLength = barCode.length(), totalBarCodeKey = 0;
                    for (int i = 0; i < stringBarCodeLength; i++) {
                        int pos = stringBarCodeLength - 1 - i,
                            intCode = Integer.parseInt(barCode.substring(pos, pos + 1), 10);
                        if (i % 2 == 0) {
                            intCode = 3 * intCode;
                        }
                        totalBarCodeKey += intCode;
                    }
                    
                    String barCodeKey = String.valueOf(10 - (totalBarCodeKey % 10));
                    if (barCodeKey.length() == 2) {
                        barCodeKey = "0";
                    }
                    barCode += barCodeKey;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
        
        barCodeLength = barCode.length();
        byte[] barCodeCommand = new byte[barCodeLength + 4];
        System.arraycopy(new byte[]{0x1d, 0x6b, (byte) barCodeType}, 0, barCodeCommand, 0, 3);
        
        try {
            for (int i = 0; i < barCodeLength; i++) {
                barCodeCommand[i + 3] = (byte) (Integer.parseInt(barCode.substring(i, i + 1), 10) + 48);
            }
            
            this.outputStream.write(new byte[]{0x1d, 0x68, (byte) heightPx});
            this.outputStream.write(barCodeCommand);
            Thread.sleep(PrinterCommands.TIME_BETWEEN_TWO_PRINT * 2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public BluetoothPrinterSocketConnexion newLine() {
        return this.newLine(null);
    }
    
    public BluetoothPrinterSocketConnexion newLine(byte[] textAlign) {
        if (!this.isOpenedStream()) {
            return this;
        }
        
        try {
            this.outputStream.write(PrinterCommands.LF);
            Thread.sleep(PrinterCommands.TIME_BETWEEN_TWO_PRINT);
            if (textAlign != null) {
                this.outputStream.write(textAlign);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return this;
    }
}
