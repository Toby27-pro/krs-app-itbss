package com.mycompany.krs_sistem.utils;

import com.mycompany.krs_sistem.config.connection;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PDFGenerator {

    public static boolean generateKRSPdf(String krsId, String nimMahasiswa, String filePath) {
        try (Connection conn = connection.koneksi()) {
            String sqlMhs = "SELECT m.nama_lengkap, m.nim, p.nama_prodi, ta.tahun_ajaran, ta.semester, kr.tahun_id " +
                            "FROM krs kr " +
                            "JOIN mahasiswa m ON kr.nim = m.nim " +
                            "LEFT JOIN prodi p ON m.prodi_id = p.prodi_id " +
                            "LEFT JOIN tahun_akademik ta ON kr.tahun_id = ta.tahun_id " +
                            "WHERE kr.krs_id = ?";
            
            String namaLengkap = "";
            String prodi = "-";
            String tahunAjaranSemester = "";
            int tahunId = 0;
            String nimDb = "";

            try (PreparedStatement ps = conn.prepareStatement(sqlMhs)) {
                ps.setString(1, krsId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        tahunId = rs.getInt("tahun_id");
                        nimDb = rs.getString("nim");
                        namaLengkap = rs.getString("nama_lengkap");
                        prodi = rs.getString("nama_prodi") != null ? rs.getString("nama_prodi") : "-";
                        tahunAjaranSemester = rs.getString("tahun_ajaran") + " - Semester " + rs.getString("semester");
                    }
                }
            }
            
            java.io.InputStream jrxmlStream = PDFGenerator.class.getResourceAsStream("/reports/krs_mahasiswa.jrxml");
            net.sf.jasperreports.engine.JasperReport jasperReport = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jrxmlStream);
            
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("NIM", nimDb);
            params.put("NAMA", namaLengkap);
            params.put("PRODI", prodi);
            params.put("TAHUN_AKADEMIK", tahunAjaranSemester);
            params.put("NIM_PARAM", nimDb);
            params.put("TAHUN_ID_PARAM", tahunId);
            
            net.sf.jasperreports.engine.JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(jasperReport, params, conn);
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Generate PDF rekap KRS seluruh mahasiswa bimbingan dosen (menggunakan JasperReports).
     */
    public static boolean generateRekapKRSBimbingan(String nipDosen, String namaDosen, String filePath) {
        try (Connection conn = connection.koneksi()) {
            java.io.InputStream jrxmlStream = PDFGenerator.class.getResourceAsStream("/reports/rekap_krs_bimbingan.jrxml");
            net.sf.jasperreports.engine.JasperReport jasperReport = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jrxmlStream);
            
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("NIP_DOSEN", nipDosen);
            params.put("NAMA_DOSEN", namaDosen);
            
            net.sf.jasperreports.engine.JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(jasperReport, params, conn);
            
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Generate PDF rekap seluruh KRS semua mahasiswa (menggunakan JasperReports).
     */
    public static boolean generateRekapKRSAll(String filePath) {
        try (Connection conn = connection.koneksi()) {
            java.io.InputStream jrxmlStream = PDFGenerator.class.getResourceAsStream("/reports/rekap_krs_all.jrxml");
            net.sf.jasperreports.engine.JasperReport jasperReport = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jrxmlStream);
            
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            net.sf.jasperreports.engine.JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(jasperReport, params, conn);
            
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
