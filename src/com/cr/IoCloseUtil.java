/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cr;
import java.io.Closeable;
import java.io.IOException;
/**
 *
 * @author Administrator
 */
public class IoCloseUtil {
    public static void close(Closeable... io) {
        for (Closeable closeable : io) {
            try {
                if(null != io) {
                    closeable.close();
                }
            } catch (IOException ex) {
                ex.getStackTrace();
            }
        }
    }
}
