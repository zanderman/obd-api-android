package com.github.zanderman.obd.classes;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.github.zanderman.obd.interfaces.BluetoothCallbackInterface;
import com.github.zanderman.obd.receivers.OBDReceiver;

/**
 * Class:
 *      OBDManager
 *
 * Description:
 *      Primary class for managing all Bluetooth interactions.
 *
 * Author:
 *      Alexander DeRieux
 */
public class OBDManager {

    /**
     * Constants
     */
    private static final int REQUEST_ENABLE_BT = 1;

    /**
     * Private Members
     */
    private BluetoothAdapter adapter;
    private IntentFilter btFilter;
    private OBDReceiver receiver;


    /**
     * Constructor:
     *      OBDManager(  )
     *
     * Description:
     *      Creates new OBDManager object.
     */
    public OBDManager() {
        super();
    }

    /**
     * Method:
     *      init( Context, final BluetoothCallbackInterface )
     *
     * Description:
     *      Sets up Bluetooth management for app.
     *
     * @param context                       Context for which the manager appears.
     * @param bluetoothCallbackInterface    Custom Bluetooth callback interface.
     */
    public void init(Context context, final BluetoothCallbackInterface bluetoothCallbackInterface) {

        // Obtain access to the BT adapter.
        this.adapter = BluetoothAdapter.getDefaultAdapter();

        // Ensure device supports bluetooth.
        if(adapter == null) {
            bluetoothCallbackInterface.bluetoothError("BT not supported on this device");
        }

        else {
            // Turn on the BT adapter if it is NOT already enabled.
            if ( !this.adapter.isEnabled() ) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity) context).startActivityForResult(enableBtIntent, this.REQUEST_ENABLE_BT);
            }

            // Create the receiver object.
            this.receiver = new OBDReceiver(bluetoothCallbackInterface);

            // Create intent filter and add BT actions.
            this.btFilter = new IntentFilter();
            this.btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            this.btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            this.btFilter.addAction(BluetoothDevice.ACTION_FOUND);

            // Register the broadcast receivers.
            this.registerBroadcastReceiver(context);
        }
    }



    /**
     * Method:
     *      registerBroadcastReceiver( Context )
     *
     * Description:
     *      Links the Bluetooth actions receiver to the given context.
     *
     * @param   context     Context to which the broadcast receiver is to be linked.
     * @return  boolean     Success status of registry.
     */
    public boolean registerBroadcastReceiver(Context context) {
        try {
            // Register the intent filter to the above broadcast receiver.
            context.registerReceiver(this.receiver, this.btFilter);
            return (true);
        } catch (Exception e) {
            return (false);
        }
    }


    /**
     * Method:
     *      unregisterBroadcastReceiver( Context )
     *
     * Description:
     *      Completely remove broadcast receiver for Bluetooth actions.
     *
     * @param   context     Context in which the broadcast receiver was created.
     * @return  boolean     Success status of removal.
     */
    public boolean unregisterBroadcastReceiver(Context context) {
        try {
            context.unregisterReceiver(this.receiver);
            return (true);
        } catch (Exception e) {
            return (false);
        }
    }


    /**
     * Method:
     *      startScan( )
     *
     * Description:
     *      Commence scanning for Bluetooth devices.
     */
    public void startScan() {

        // Start the BT discovery process.
        this.adapter.startDiscovery();
    }


    /**
     * Method:
     *      stopScan( )
     *
     * Description:
     *      Terminate Bluetooth scanning process.
     */
    public void stopScan() {

        // Start the BT discovery process.
        this.adapter.cancelDiscovery();
    }
}
