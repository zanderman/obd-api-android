package com.github.zanderman.obd.classes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.UUID;

/**
 * Created by zanderieux on 2/5/16.
 */
//
public class OBDAdapter {

    //  -----------------
    // | Private Members |
    //  -----------------
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private UUID uuid;

    //  ----------------
    // | Public Members |
    //  ----------------
    public String name;     // String name listed in a BT scan.
    public String address;  // MAC address.

    //  -------------
    // | Constructor |
    //  -------------
    public OBDAdapter(BluetoothDevice device) {
        super();

        // Configure the adapter.
        this.configure(device);
    }

    private boolean configure(BluetoothDevice device) {

        // Set the parameters of the device.
        this.name = device.getName();
        this.address = device.getAddress();
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        this.device = device;
        this.socket = null;

        // Generate a random UUID for the device to connect with.
        this.uuid = UUID.randomUUID();

        return (true);
    }

    // Connect to this bluetooth OBD adapter using it's MAC address.
    public boolean connect() {

        try {
            // Bluetooth NOT supported.
            if (this.adapter == null) {
                return (false);
            }
            // Bluetooth is supported.
            else {
                // Gain access to the actual device.
                this.device = this.adapter.getRemoteDevice(this.address);

                // Create a communications socket with the device.
                this.socket = this.device.createRfcommSocketToServiceRecord(this.uuid);

                // Connect to the socket.
                socket.connect();

                // Connection worked out correctly.
                return (true);
            }
        } catch (Exception e) {
            return (false);
        }
    }
}
