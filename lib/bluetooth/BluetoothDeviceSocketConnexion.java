package lib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

public class BluetoothDeviceSocketConnexion {
    
    protected BluetoothDevice device;
    protected BluetoothSocket bluetoothSocket = null;
    
    public BluetoothDeviceSocketConnexion(BluetoothDevice device) {
        this.device = device;
    }
    
    public BluetoothDevice getDevice() {
        return this.device;
    }
    
    public boolean isConnected() {
        return (this.bluetoothSocket != null);
    }
    
    public boolean connect() {
        try {
            this.bluetoothSocket = this.device.createRfcommSocketToServiceRecord(this.device.getUuids()[0].getUuid());
            this.bluetoothSocket.connect();
            return true;
        } catch (IOException ex) {
            try {
                this.bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.bluetoothSocket = null;
        }
        return false;
    }
    
    public boolean disconnect() {
        if(!this.isConnected()) {
            return true;
        }
        try {
            this.bluetoothSocket.close();
            this.bluetoothSocket = null;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
