# Android library for Printer Thermal ESC/POS Command

Usefull library to help Android developers to print with bluetooth thermal printers ESC/POS.

## Android version

Developed for SDK version 16 (Android 4.1 Jelly Bean) and above.

## Printer tested

Tested with [HOIN Bluetooth Thermal Printer ESC / POS](https://www.gearbest.com/printers/pp_662658.html).

## Bluetooth permission

Be sure to have `Manifest.permission.BLUETOOTH` permission for your app. Like this :

```java
if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
} else {
    // Your code HERE
}
```

## Code example

The code below is an example to write in your activity :

```java
Printer printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 203, 48f, 32);
printer
    .printFormattedText(
        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
        "[L]\n" +
        "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
        "[L]\n" +
        "[C]================================\n" +
        "[L]\n" +
        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
        "[L]  + Size : S\n" +
        "[L]\n" +
        "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
        "[L]  + Size : 57/58\n" +
        "[L]\n" +
        "[C]--------------------------------\n" +
        "[L][R]TOTAL PRICE :[R]34.98e\n" +
        "[L][R]TAX :[R]4.23e\n" +
        "[L]\n" +
        "[C]================================\n" +
        "[L]\n" +
        "[L]<font size='tall'>Customer :</font>\n" +
        "[L]Raymond DUPONT\n" +
        "[L]5 rue des girafes\n" +
        "[L]31547 PERPETES\n" +
        "[L]Tel : 0645789663\n" +
        "[L]\n" +
        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n"
    )
    .disconnectPrinter();
```

Below a picture of the receipt printed with the code above :

![Example of a printed receipt](http://www.developpeur-web.dantsu.com/files/librairie/receipt-thermal-printer.png)

## Formatted Text : Syntax guide

### New line

Use `\n` to create a new line of text.

### Text alignment and column separation

Add an alignment tag on a same line of text implicitly create a new column.

Column alignment tags :

- `[L]` : left side alignment
- `[C]` : center alignment
- `[R]` : right side alignment

Example :

- `[L]Some text` : One column aligned to left
- `[C]Some text` : One column aligned to center
- `[R]Some text` : One column aligned to right
- `[L]Some text[L]Some other text` : Two columns aligned to left. `Some other text` starts in the center of the paper.
- `[L]Some text[R]Some other text` : Two columns, first aligned to left, second aligned to right. `Some other text` is printed at the right of paper.
- `[L]Some[R]text[R]here` : Three columns.
- `[L][R]text[R]here` : Three columns. The first is empty but it takes a third of the available space.

### Font

#### Size

`<font></font>` tag allows you to change the font size. Default size is `medium`.

- `<font size='small'>Some text</font>` : Small size
- `<font size='medium'>Some text</font>` : Medium size
- `<font size='wide'>Some text</font>` : Double width of medium size
- `<font size='tall'>Some text</font>` : Double height of medium size
- `<font size='big'>Some text</font>` : Double width and height of medium size

#### Bold

`<b></b>` tag allows you to change the font weight.

- `<b>Some text</b>`

#### Underline

`<u></u>` tag allows you to underline the text.

- `<u>Some text</u>`

### Image

`<img></img>` tag allows you to print image. Inside the tag you need to write a hexadecimal string of an image.

Use `PrinterTextParserImg.bitmapToHexadecimalString` to convert `Drawable`, `BitmapDrawable` or `Bitmap` to hexadecimal string.

- `<img>`hexadecimal string of an image`</img>`

**/!\\ WARNING /!\\** : This tag has several constraints :

- A line that contains `<img></img>` can have only one alignment tag and it must be at the beginning of the line.
- `<img>` must be directly preceded by nothing or an alignment tag (`[L][C][R]`).
- `</img>` must be directly followed by a new line `\n`.
- You can't write text on a line that contains `<img></img>`.

### Barcode

`<barcode></barcode>` tag allows you to print a barcode. Inside the tag you need to write the code number to print.

- `<barcode>451278452159</barcode>` : **(12 numbers)**  
Prints a EAN13 barcode with a height of 10 millimeters.
- `<barcode height='15'>451278452159</barcode>` : **(12 numbers)**  
Prints a EAN13 barcode with a height of 15 millimeters.
- `<barcode type='ean13'>451278452159</barcode>` : **(12 numbers)**  
Prints a EAN13 barcode with a height of 10 millimeters.
- `<barcode type='ean8'>4512784</barcode>` : **(7 numbers)**  
Prints a EAN8 barcode with a height of 10 millimeters.
- `<barcode type='upca' height='20'>4512784521</barcode>` : **(11 numbers)**  
Prints a UPC-A barcode with a height of 20 millimeters.
- `<barcode type='upce' height='25'>051278</barcode>` : **(6 numbers)**  
Prints a UPC-E barcode with a height of 25 millimeters.

**/!\\ WARNING /!\\** : This tag has several constraints :

- A line that contains `<barcode></barcode>` can have only one alignment tag and it must be at the beginning of the line.
- `<barcode>` must be directly preceded by nothing or an alignment tag (`[L][C][R]`).
- `</barcode>` must be directly followed by a new line `\n`.
- You can't write text on a line that contains `<barcode></barcode>`.

## Class list

### Class : `lib.printer.thermal.bluetooth.BluetoothPrinters`

#### **Static** Method : `selectFirstPairedBluetoothPrinter()`
Easy way to get the first bluetooth printer paired / connected.
- **return** `BluetoothPrinterSocketConnexion`

#### Method : `getList()`
Get a list of bluetooth printers.
- **return** `BluetoothPrinterSocketConnexion[]`

### Class : `lib.printer.thermal.Printer`

#### Constructor : `Printer(BluetoothPrinterSocketConnexion printer, int printerDpi, float printingWidthMM, int nbrCharactersPerLine)`
- **param** `BluetoothPrinterSocketConnexion printer` : Instance of a connected bluetooth printer
- **param** `int printerDpi` : DPI of the connected printer
- **param** `float printingWidthMM` : Printing width in millimeters
- **param** `int nbrCharactersPerLine` : The maximum number of characters that can be printed on a line.

#### Method : `disconnectPrinter()`
Close the Bluetooth connexion with the printer.
- **return** `Printer` : Fluent interface

#### Method : `getNbrCharactersPerLine()`
Get the maximum number of characters that can be printed on a line.
- **return** `int`

#### Method : `getPrintingWidthMM()`
Get the printing width in millimeters
- **return** `float`

#### Method : `getPrinterDpi()`
Get the printer DPI
- **return** `int`

#### Method : `getPrintingWidthPx()`
Get the printing width in dot
- **return** `int`

#### Method : `getCharSizeWidthPx()`
Get the number of dot that a printed character contain
- **return** `int`

#### Method : `mmToPx(float mmSize)`
Convert the mmSize variable from millimeters to dot.
- **param** `float mmSize` : Distance in millimeters to be converted
- **return** `int` : Dot size of mmSize.

#### Method : `printFormattedText(String text)`
Print a formatted text. Read the ["Formatted Text : Syntax guide" section](#formatted-text--syntax-guide) for more information about text formatting options.
- **param** `String text` : Formatted text to be printed.
- **return** `Printer` : Fluent interface

#### Method : `bitmapToBytes(Bitmap bitmap)`
Convert Bitmap object to ESC/POS image.
- **param** `Bitmap bitmap` : Instance of Bitmap
- **return** `byte[]` : Bytes contain the image in ESC/POS command

### Class : `lib.printer.thermal.textparser.PrinterTextParserImg`

#### **Static** Method : `bitmapToHexadecimalString(Printer printer, Drawable drawable)`
Convert Drawable instance to a hexadecimal string of the image data.
- **param** `Printer printer` : A Printer instance that will print the image.
- **param** `Drawable drawable` : Drawable instance to be converted.
- **return** `String` : A hexadecimal string of the image data. Empty string if Drawable cannot be cast to BitmapDrawable.

#### **Static** Method : `bitmapToHexadecimalString(Printer printer, BitmapDrawable bitmapDrawable)`
Convert BitmapDrawable instance to a hexadecimal string of the image data.
- **param** `Printer printer` : A Printer instance that will print the image.
- **param** `BitmapDrawable bitmapDrawable` : BitmapDrawable instance to be converted.
- **return** `String` : A hexadecimal string of the image data.

#### **Static** Method : `bitmapToHexadecimalString(Printer printer, Bitmap bitmap)`
Convert Bitmap instance to a hexadecimal string of the image data.
- **param** `Printer printer` : A Printer instance that will print the image.
- **param** `Bitmap bitmap` : Bitmap instance to be converted.
- **return** `String` : A hexadecimal string of the image data.

#### **Static** Method : `bytesToHexadecimalString(byte[] bytes)`
Convert byte array to a hexadecimal string of the image data.
- **param** `byte[] bytes` : Bytes contain the image in ESC/POS command.
- **return** `String` : A hexadecimal string of the image data.

#### **Static** Method : `hexadecimalStringToBytes(String hexString)`
Convert hexadecimal string of the image data to bytes ESC/POS command.
- **param** `String hexString` : Hexadecimal string of the image data.
- **return** `byte[]` : Bytes contain the image in ESC/POS command.


