/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class Send implements Runnable {

//    private Socket client = null;
    private PrintWriter pw = null;
    private BufferedReader console = null;
    private boolean isrunning = true;
    private String msg;
    private final String CLRF = "\r\n"; 
    
    public Send(Socket client) {
//        this.client = client;
        try {
            pw = new PrintWriter(client.getOutputStream());
            console = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException ex) {
            isrunning = false;
        }
    }
    
    @Override
    public void run() {
        while(isrunning) {
            try {
                msg = console.readLine();
                pw.write(msg + CLRF);
                pw.flush();
            } catch (IOException ex) {
//                Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                isrunning = false;
            }
        }
    }
}
