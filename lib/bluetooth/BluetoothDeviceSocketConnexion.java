package lib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

public class BluetoothDeviceSocketConnexion {
    
    protected BluetoothDevice device;
    protected BluetoothSocket bluetoothSocket = null;
    
    /**
     * Create un instance of BluetoothDeviceSocketConnexion.
     *
     * @param device an instance of android.bluetooth.BluetoothDevice
     */
    public BluetoothDeviceSocketConnexion(BluetoothDevice device) {
        this.device = device;
    }
    
    /**
     * Get the instance android.bluetooth.BluetoothDevice connected.
     *
     * @return an instance of android.bluetooth.BluetoothDevice
     */
    public BluetoothDevice getDevice() {
        return this.device;
    }
    
    /**
     * Check if the bluetooth device is connected by socket.
     *
     * @return true if is connected
     */
    public boolean isConnected() {
        return (this.bluetoothSocket != null);
    }
    
    /**
     * Start socket connexion with the bluetooth device.
     *
     * @return return true if success
     */
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
    
    /**
     * Close the socket connexion with the bluetooth device.
     *
     * @return return true if success
     */
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
