package lib.printer.thermal;

public class PrinterCommands {
    public static final int TIME_BETWEEN_TWO_PRINT = 150;
    
    public static final byte[] WESTERN_EUROPE_ENCODING = new byte[]{0x1B, 0x74, 0x06};
    
    public static final byte LF = 0x0A;
    
    public static final byte[] TEXT_ALIGN_LEFT = new byte[]{0x1B, 0x61, 0x00};
    public static final byte[] TEXT_ALIGN_CENTER = new byte[]{0x1B, 0x61, 0x01};
    public static final byte[] TEXT_ALIGN_RIGHT = new byte[]{0x1B, 0x61, 0x02};
    
    public static final byte[] TEXT_WEIGHT_NORMAL = new byte[]{0x1B, 0x45, 0x00};
    public static final byte[] TEXT_WEIGHT_BOLD = new byte[]{0x1B, 0x45, 0x01};
    
    public static final byte[] TEXT_SIZE_NORMAL = new byte[]{0x1B, 0x21, 0x03};
    public static final byte[] TEXT_SIZE_MEDIUM = new byte[]{0x1B, 0x21, 0x08};
    public static final byte[] TEXT_SIZE_DOUBLE_HEIGHT = new byte[]{0x1B, 0x21, 0x10};
    public static final byte[] TEXT_SIZE_DOUBLE_WIDTH = new byte[]{0x1B, 0x21, 0x20};
    public static final byte[] TEXT_SIZE_BIG = new byte[]{0x1B, 0x21, 0x30};
    
    public static final byte[] TEXT_UNDERLINE_OFF = new byte[]{0x1B, 0x2D, 0x00};
    public static final byte[] TEXT_UNDERLINE_ON = new byte[]{0x1B, 0x2D, 0x01};
    public static final byte[] TEXT_UNDERLINE_LARGE = new byte[]{0x1B, 0x2D, 0x02};
    
    public static final byte[] TEXT_DOUBLE_STRIKE_OFF = new byte[]{0x1B, 0x47, 0x00};
    public static final byte[] TEXT_DOUBLE_STRIKE_ON = new byte[]{0x1B, 0x47, 0x01};
    
    
    public static final int BARCODE_UPCA = 0;
    public static final int BARCODE_UPCE = 1;
    public static final int BARCODE_EAN13 = 2;
    public static final int BARCODE_EAN8 = 3;
    public static final int BARCODE_ITF = 5;
}
