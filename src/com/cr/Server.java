/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class Server {
    
    private ServerSocket server;

    private Map<String, MyChannel> clients = new HashMap<String, MyChannel>();
    private Map<String, String> idNicknameMapping = new HashMap<String, String>();
    
    public static void main(String[] args) throws IOException {
        new Server().init();
    }
    
    public void init() throws IOException {
        server = new ServerSocket(55555);
        System.out.println("server is running at port 55555......");
        while(true) {
            new Thread(new MyChannel(server.accept())).start();
        }
    }
    
    private class MyChannel implements Runnable { 
        
        private Socket client = null;
        private String id = "";
        private BufferedReader br = null;
        private PrintWriter pw = null;
        private String nickname = "";
        private final String systip = "System message: ";
        private boolean isrunning = true;
        
        public MyChannel(Socket client) {
            this.client = client;
            id = client.getInetAddress() + ":" + client.getPort();
            System.out.println(id);
            Boolean isOld = clients.containsKey(id);
            if(!isOld) {
                clients.put(id, this);
                try {
                    br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    pw = new PrintWriter(client.getOutputStream());
                } catch (IOException ex) {
                    isrunning = false;
                    IoCloseUtil.close(br, pw, client);
                    clients.remove(id);
                    idNicknameMapping.remove(nickname);
                }
            }
        } 
        
        @Override
        public void run() {
            String msg;
            String target;
            while(isrunning) {
                try {
                    if(idNicknameMapping.containsValue(id)) {
                        msg = receive(); //@jack:hello!
                        if(msg.trim().equals("")) {
                            send(systip + "Are u kidding me? Empty! Be serious,ok?");
                        } else {
                            if(msg.startsWith("@") && msg.indexOf(":") > -1) { // private message
                                target = msg.substring(1, msg.indexOf(":"));
                                if(idNicknameMapping.containsKey(target)) { // nickname exist
                                    sendPrivate(nickname + " talk to you: " + msg.substring(msg.indexOf(":") + 1), target);   
                                } else {
                                    send(systip + target + " does not exist!");
                                }
                            } else { // broadcast message
                                sendOthers(nickname + ": " + msg);
                            }
                        }
                    } else { //first time to join the room
                        String name = "";
                        send(systip + "please input your nickname:");
                        while(name.trim().equals("") || name == null || name.trim().length() < 4) {
                            name = receive();
                            if(!name.trim().equals("") && name != null && name.trim().length() >= 4) {
                                if(idNicknameMapping.containsKey(name)) {
                                    send(systip + name + " already exists, please input a different nickname:");
                                } else {
                                    idNicknameMapping.put(name, id);
                                    this.nickname = name;
                                    send(systip + "welcome " + nickname);
                                    sendOthers(systip + nickname + " join the chat room");
                                    break;
                                }
                            } else {
                                send(systip + name + " is a invalid nickname, please input your nickname:");
                            }
                        } 
                    }
                } catch (IOException ex) {
                    isrunning = false;
                    IoCloseUtil.close(br, pw, client);
                    clients.remove(id);
                    idNicknameMapping.remove(nickname);
                }
            }
        }
        
        private void send(String msg) {
            pw.println(msg);
            pw.flush();
        }
        
        private void sendOthers(String msg) {
            for (Map.Entry entry : clients.entrySet()) {
//                String key = (String) entry.getKey();
                MyChannel val = (MyChannel) entry.getValue();
                if(val == this) continue;
                else {
                    val.send(msg);
                }
            }
        }
        
        private void sendPrivate(String msg, String target) {
            MyChannel val = clients.get(idNicknameMapping.get(target));
            val.send(msg);
        }
        
        private String receive() throws IOException {
            return br.readLine();
        }
        
    }
    
    
    
}
