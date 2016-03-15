/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class Receive implements Runnable {

//    private Socket client = null;
    private BufferedReader br = null;
    private String msg;
    private boolean isrunning = true;
    
    public Receive(Socket client) {
//        this.client = client;
        try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException ex) {
            isrunning = false;
        }
    }
    
    @Override
    public void run() {
        while(isrunning) {
            try {
                msg = br.readLine();
                System.out.println(msg);
            } catch (IOException ex) {
                isrunning = false;
            }
        }
    }
    
}
