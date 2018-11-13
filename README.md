# PrinterThermal-ESCPOS-Android

Usefull library to help Android developpers to print with bluetooth thermal printer.

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
        "[L]  + Qte : 2\n" +
        "[L]\n" +
        "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
        "[L]  + Size : 57/58\n" +
        "[L]  + Qte : 1\n" +
        "[L]\n" +
        "[C]--------------------------------\n" +
        "[L][R]TOTAL PRICE :[R]24.49e\n" +
        "[L][R]TAX :[R]4.00e\n" +
        "[L]\n" +
        "[C]================================\n" +
        "[L]\n" +
        "[L]<font size='tall'>Client :</font>\n" +
        "[L]Raymond DUPONT\n" +
        "[L]5 rue des girafes\n" +
        "[L]31547 PERPETES\n" +
        "[L]Tel : 0645789663\n" +
        "[L]\n" +
        "[C]<barcode type='ean13' size='10'>831254784551</barcode>\n"
    )
    .disconnectPrinter();
```

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

**/!\\ WARNING /!\\** : This tag must be in one column line. Image tag must be the only tag of the line (except the alignment tag before).

Use `PrinterTextParserImg.bitmapToHexadecimalString` to convert `Drawable`, `BitmapDrawable` or `Bitmap` to hexadecimal string.

- `<img>`hexadecimal string of an image`</img>`

### Bar code

`<barcode></barcode>` tag allows you to print a bar code. Inside the tag you need to write the code number to print.

**/!\\ WARNING /!\\** : This tag must be in one column line. Bar code tag must be the only tag of the line (except the alignment tag before).

- `<barcode>451278452159</barcode>` : **(12 numbers)** Prints an EAN13 bar code with a height of 10 millimeters.
- `<barcode size='15'>451278452159</barcode>` : **(12 numbers)** Prints an EAN13 bar code with a height of 15 millimeters.
- `<barcode type='ean13'>451278452159</barcode>` : **(12 numbers)** Prints an EAN13 bar code with a height of 10 millimeters.
- `<barcode type='ean8'>4512784</barcode>` : **(7 numbers)** Prints an EAN8 bar code with a height of 10 millimeters.
- `<barcode type='upca' size='20'>4512784521</barcode>` : **(11 numbers)** Prints an UPC-A bar code with a height of 20 millimeters.
- `<barcode type='upce' size='25'>051278</barcode>` : **(6 numbers)** Prints an UPC-E bar code with a height of 25 millimeters.

## Class list

### Class : `Printer`

#### Constructor : `Printer(BluetoothPrinterSocketConnexion printer, int printerDpi, float printingWidthMM, int nbrCharactersPerLine)`
- **param** *printer* : Instance of a connected bluetooth printer
- **param** *printerDpi* : DPI of the connected printer
- **param** *printingWidthMM* : Printing width in millimeters
- **param** *nbrCharactersPerLine* : The maximum number of characters that can be printed on a line.

#### Method : `disconnectPrinter()`
Close the Bluetooth connexion with the printer.
- **return** *Printer* : Fluent interface

#### Method : `getNbrCharactersPerLine()`
Get the maximum number of characters that can be printed on a line.
- **return** *int*

#### Method : `getPrintingWidthMM()`
Get the printing width in millimeters
- **return** *float*

#### Method : `getPrinterDpi()`
Get the printer DPI
- **return** *int*

#### Method : `getPrintingWidthPx`
Get the printing width in dot
- **return** *int*

#### Method : `getCharSizeWidthPx()`
Get the number of dot that a printed character contain
- **return** *int*

#### Method : `mmToPx(float mmSize)`
Convert from millimeters to dot the mmSize variable.
- **param** *mmSize* : Distance in millimeters to be converted
- **return** *int*

#### Method : `printFormattedText(String text)`
Print a formatted text. Read the ["Formatted Text : Syntax guide" section](#formatted-text--syntax-guide) for more information about text formatting options.
- **param** *text* : Formatted text to be printed.
- **return** *Printer* : Fluent interface

#### Method : `bitmapToBytes(Bitmap bitmap)`
Convert Bitmap object to ESC/POS image.
- **param** *bitmap* : Instance of Bitmap
- **return** *byte[]* : Bytes contain the image in ESC/POS command

