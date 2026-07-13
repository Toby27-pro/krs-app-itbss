/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.krs_sistem;

import java.sql.Connection;
import com.mycompany.krs_sistem.config.connection;
import com.mycompany.krs_sistem.auth.Login;

/**
 *
 * @author USER
 */

public class KRS {
    
    public static void main(String[] args) {
        // Test koneksi database (opsional)
        connection conn = new connection();
        Connection koneksi = conn.koneksi();
        
        // Menjalankan halaman Login pertama kali
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
