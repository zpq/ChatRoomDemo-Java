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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Client {
    
    public static void main(String[] args) {
        new Client().init();
    }
    
    public void init() {
        try {
            Socket client = new Socket("localhost", 55555);
            new Thread(new Send(client)).start();
            new Thread(new Receive(client)).start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
