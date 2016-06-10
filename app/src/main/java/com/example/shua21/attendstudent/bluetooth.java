package com.example.shua21.attendstudent;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by shua21 on 2015-11-05.
 */
public class bluetooth extends Thread{
    BluetoothAdapter mBluetoothAdapter;
    String address,myPhoneNum;
    Handler mHandler;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    bluetooth(Handler h,String addr,String pn)
    {
        mHandler = h;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        myPhoneNum = pn;

        address = addr;
        this.start();

    }
    BluetoothSocket mmSocket = null;
    public void run()
    {

        mBluetoothAdapter.cancelDiscovery();

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        try {

            mmSocket = device.createInsecureRfcommSocketToServiceRecord(
                    MY_UUID_INSECURE);

            mmSocket.connect();
            mHandler.sendEmptyMessage(2);
            OutputStream mmOutStream =  mmSocket.getOutputStream();
            InputStream mmInStream =  mmSocket.getInputStream();
            mmOutStream.write(myPhoneNum.getBytes());
            byte[] buffer = new byte[1024];
            mHandler.sendEmptyMessage(3);
            int bytes=mmInStream.read(buffer);

            Message m = mHandler.obtainMessage(1, bytes, -1, buffer);
            mHandler.sendMessage(m);

            mmSocket.close();

        }catch (Exception e){
            try{
                mmSocket.close();

            }catch(Exception ee){}

        }
    }
}
