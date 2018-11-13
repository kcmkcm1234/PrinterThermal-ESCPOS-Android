package lib.printer.thermal.bluetooth;

import lib.bluetooth.*;
import android.bluetooth.BluetoothClass;


public class BluetoothPrinters extends BluetoothDevices {
    
    public BluetoothPrinterSocketConnexion[] getList() {
        BluetoothDeviceSocketConnexion[] bluetoothDevicesList = super.getList();
    
        if(bluetoothDevicesList == null) {
            return null;
        }
    
        int i = 0, j = 0;
        BluetoothPrinterSocketConnexion[] bluetoothPrintersTmp = new BluetoothPrinterSocketConnexion[bluetoothDevicesList.length];
        for (BluetoothDeviceSocketConnexion device : bluetoothDevicesList) {
            if (device.getDevice().getBluetoothClass().getMajorDeviceClass() == BluetoothClass.Device.Major.IMAGING && device.getDevice().getBluetoothClass().getDeviceClass() == 1664) {
                bluetoothPrintersTmp[i++] = new BluetoothPrinterSocketConnexion(device.getDevice());
            }
        }
        BluetoothPrinterSocketConnexion[] bluetoothPrinters = new BluetoothPrinterSocketConnexion[i];
        for (BluetoothPrinterSocketConnexion device : bluetoothPrintersTmp) {
            if (device != null) {
                bluetoothPrinters[j++] = device;
            }
        }
        return bluetoothPrinters;
    }
    
    public static BluetoothPrinterSocketConnexion selectFirstPairedBluetoothPrinter() {
        BluetoothPrinters printers = new BluetoothPrinters();
        BluetoothPrinterSocketConnexion[] bluetoothPrinters = printers.getList();
        
        if (bluetoothPrinters != null && bluetoothPrinters.length > 0) {
            for (BluetoothPrinterSocketConnexion printer : bluetoothPrinters) {
                if (printer.connect()) {
                    return printer;
                }
            }
        }
        return null;
    }
    
}
