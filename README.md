# PrinterThermal-ESCPOS-Android
Usefull library to help Android developpers to print with bluetooth thermal printer.

## How to use it !

### Bluetooth Persmission
Be sure to have permission of `Manifest.permission.BLUETOOTH`. Like this :
```java
if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
} else {
    // Your code HERE
}
```

### Your first print
The code below is an exemple to write in your activity :
```java
Printer printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 203, 48f, 32);
printer
    .printFormattedText(
        "[C]<img>" + printer.bitmapToHexadecimalString(this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
        "[L]\n" +
        "[C]<u><big>COMMANDE N°045</big></u>\n" +
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
        "[L][R]TOTAL :[R]24.49e\n" +
        "[L][R]DONT TVA :[R]24.49e\n" +
        "[L]\n" +
        "[C]================================\n" +
        "[L]\n" +
        "[L]<tall>Client :</tall>\n" +
        "[L]Raymond DUPONT\n" +
        "[L]5 rue des girafes\n" +
        "[L]31547 PERPETES\n" +
        "[L]Tel : 0645789663\n" +
        "[L]\n" +
        "[C]<barcode type='ean13' size='10'>831254784551</barcode>\n"
    )
    .disconnectPrinter();
```