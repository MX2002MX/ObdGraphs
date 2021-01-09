package org.openobd2.core.logger.bl

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import org.openobd2.core.connection.Connection
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.concurrent.TimeUnit

internal class BluetoothConnection : Connection {

    private val RFCOMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var input: InputStream? = null
    private var output: OutputStream? = null
    private lateinit var socket: BluetoothSocket
    private var device: String? = null
    private val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    constructor(btDeviceName: String) {
        this.device = btDeviceName
    }

    override fun reconnect() {
        Log.i("DATA_LOGGER_BT", "Reconnecting to the device: $device")
        input?.close()
        output?.close()

        socket.close()
        TimeUnit.MILLISECONDS.sleep(200)
        init(device)
        Log.i("DATA_LOGGER_BT", "Successfully reconnect to the device: $device")
    }

    override fun init() {
        init(device)
    }

    override fun close() {
        socket.close()
        Log.i("DATA_LOGGER_BT", "Socket for device: $device has been closed.")
    }

    override fun openOutputStream(): OutputStream? {
        return output
    }

    override fun openInputStream(): InputStream? {
        return input
    }

    override fun isClosed(): Boolean {
        return !socket.isConnected
    }

    private fun init(btDeviceName: String?) {

        mBluetoothAdapter.cancelDiscovery()

        for (currentDevice in mBluetoothAdapter.bondedDevices) {
            if (currentDevice.name.equals(btDeviceName)) {
                Log.i("DATA_LOGGER_BT", "Opening connection to device: $btDeviceName")
                socket =
                    currentDevice.createRfcommSocketToServiceRecord(RFCOMM_UUID)
                socket.connect()
                if (socket.isConnected) {
                    input = socket.inputStream
                    output = socket.outputStream
                    Log.i(
                        "DATA_LOGGER_BT",
                        "Successfully opened  the connection to device: $btDeviceName"
                    )
                }
            }
        }

    }
}