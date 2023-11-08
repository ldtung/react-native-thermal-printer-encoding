package com.dantsu.escposprinter_encoding.connection.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.Nullable;

import com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnections;
import com.dantsu.escposprinter_encoding.exceptions.EscPosConnectionException;

public class BluetoothPrintersConnections extends BluetoothConnections {

    /**
     * Easy way to get the first bluetooth printer paired / connected.
     *
     * @return a EscPosPrinterCommands instance
     */
    @Nullable
    public static com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection selectFirstPaired() {
        BluetoothPrintersConnections printers = new BluetoothPrintersConnections();
        com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection[] bluetoothPrinters = printers.getList();

        if (bluetoothPrinters != null && bluetoothPrinters.length > 0) {
            for (com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection printer : bluetoothPrinters) {
                try {
                    return printer.connect();
                } catch (EscPosConnectionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Get a list of bluetooth printers.
     *
     * @return an array of EscPosPrinterCommands
     */
    @SuppressLint("MissingPermission")
    @Nullable
    public com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection[] getList() {
        com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection[] bluetoothDevicesList = super.getList();

        if (bluetoothDevicesList == null) {
            return null;
        }

        int i = 0;
        com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection[] printersTmp = new com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection[bluetoothDevicesList.length];
        for (com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection bluetoothConnection : bluetoothDevicesList) {
            BluetoothDevice device = bluetoothConnection.getDevice();

            int majDeviceCl = device.getBluetoothClass().getMajorDeviceClass(),
                    deviceCl = device.getBluetoothClass().getDeviceClass();

            if (majDeviceCl == BluetoothClass.Device.Major.IMAGING && (deviceCl == 1664 || deviceCl == BluetoothClass.Device.Major.IMAGING)) {
                printersTmp[i++] = new com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection(device);
            }
        }
        com.dantsu.escposprinter_encoding.connection.bluetooth.BluetoothConnection[] bluetoothPrinters = new BluetoothConnection[i];
        System.arraycopy(printersTmp, 0, bluetoothPrinters, 0, i);
        return bluetoothPrinters;
    }

}
