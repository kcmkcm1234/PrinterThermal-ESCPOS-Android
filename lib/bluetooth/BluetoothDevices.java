package lib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

public class BluetoothDevices {
    protected BluetoothAdapter bluetoothAdapter;
    
    public BluetoothDevices() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    
    public BluetoothDeviceSocketConnexion[] getList() {
        if (this.bluetoothAdapter == null) {
            return null;
        }
    
        if(!this.bluetoothAdapter.isEnabled()) {
            return null;
        }
        
        Set<BluetoothDevice> bluetoothDevicesList = this.bluetoothAdapter.getBondedDevices();
        BluetoothDeviceSocketConnexion[] bluetoothDevices = new BluetoothDeviceSocketConnexion[bluetoothDevicesList.size()];
    
        if (bluetoothDevicesList.size() > 0) {
            int i = 0;
            for (BluetoothDevice device : bluetoothDevicesList) {
                bluetoothDevices[i++] = new BluetoothDeviceSocketConnexion(device);
            }
        }
        
        return bluetoothDevices;
    }
}
