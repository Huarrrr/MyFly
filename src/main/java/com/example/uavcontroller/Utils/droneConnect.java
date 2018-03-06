package com.example.uavcontroller.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2017/4/16.
 */

public class droneConnect {
    private static Socket socket =null;

    static {
        try {
            socket = new Socket("192.168.4.1",333);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void send(byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(data);
        out.flush();
        out.close();

    }
    public static void close() throws IOException {
        socket.close();
    }


}
