-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 05, 2026 at 08:49 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `krs_akademik`
--

-- --------------------------------------------------------

--
-- Table structure for table `dosen`
--

CREATE TABLE `dosen` (
  `nip` varchar(20) NOT NULL,
  `user_id` bigint NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `jabatan` enum('Dosen','Kaprodi') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dosen`
--

INSERT INTO `dosen` (`nip`, `user_id`, `nama_lengkap`, `jabatan`) VALUES
('D001', 3, 'Andi Wijaya', 'Dosen'),
('D002', 4, 'Budi Santoso', 'Dosen'),
('D003', 5, 'Sinta Maharani', 'Kaprodi'),
('D004', 6, 'Rahmat Hidayat', 'Dosen'),
('D005', 7, 'Lina Permata', 'Dosen'),
('D006', 8, 'Yohanes Setiawan', 'Dosen'),
('D007', 9, 'Dewi Lestari', 'Dosen'),
('D008', 10, 'Ferdi Kurniawan', 'Dosen');

-- --------------------------------------------------------

--
-- Table structure for table `jadwal`
--

CREATE TABLE `jadwal` (
  `jadwal_id` int NOT NULL,
  `kelas_id` int NOT NULL,
  `ruangan_id` int NOT NULL,
  `hari` enum('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu') NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jadwal`
--

INSERT INTO `jadwal` (`jadwal_id`, `kelas_id`, `ruangan_id`, `hari`, `jam_mulai`, `jam_selesai`) VALUES
(1, 1, 1, 'Senin', '08:00:00', '10:00:00'),
(2, 2, 2, 'Senin', '10:00:00', '12:00:00'),
(3, 3, 3, 'Selasa', '08:00:00', '10:00:00'),
(4, 4, 4, 'Selasa', '13:00:00', '15:00:00'),
(5, 5, 5, 'Rabu', '08:00:00', '10:00:00'),
(6, 6, 6, 'Rabu', '10:00:00', '12:00:00'),
(7, 7, 7, 'Kamis', '08:00:00', '10:00:00'),
(8, 8, 8, 'Kamis', '13:00:00', '15:00:00'),
(9, 9, 1, 'Jumat', '08:00:00', '10:00:00'),
(10, 10, 2, 'Jumat', '10:00:00', '12:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `kelas`
--

CREATE TABLE `kelas` (
  `kelas_id` int NOT NULL,
  `mk_id` int NOT NULL,
  `nip` varchar(20) NOT NULL,
  `tahun_id` int NOT NULL,
  `nama_kelas` varchar(10) NOT NULL,
  `kuota` int NOT NULL,
  `kuota_terisi` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kelas`
--

INSERT INTO `kelas` (`kelas_id`, `mk_id`, `nip`, `tahun_id`, `nama_kelas`, `kuota`, `kuota_terisi`) VALUES
(1, 1, 'D001', 2, 'A', 40, 0),
(2, 1, 'D004', 2, 'B', 40, 0),
(3, 3, 'D002', 2, 'A', 35, 0),
(4, 4, 'D005', 2, 'A', 30, 0),
(5, 5, 'D006', 2, 'A', 40, 0),
(6, 6, 'D007', 2, 'A', 35, 0),
(7, 9, 'D003', 2, 'A', 40, 0),
(8, 11, 'D007', 2, 'A', 35, 0),
(9, 17, 'D008', 2, 'A', 40, 0),
(10, 19, 'D003', 2, 'A', 35, 0);

-- --------------------------------------------------------

--
-- Table structure for table `krs`
--

CREATE TABLE `krs` (
  `krs_id` int NOT NULL,
  `nim` varchar(20) NOT NULL,
  `tahun_id` int NOT NULL,
  `tanggal_krs` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('Pending','Approved','Rejected') NOT NULL,
  `catatan` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `krs_detail`
--

CREATE TABLE `krs_detail` (
  `detail_id` int NOT NULL,
  `krs_id` int NOT NULL,
  `jadwal_id` int NOT NULL,
  `status` varchar(20) DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `nim` varchar(20) NOT NULL,
  `user_id` bigint NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `prodi_id` int NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `angkatan` year NOT NULL,
  `semester_aktif` int NOT NULL,
  `max_sks` int NOT NULL,
  `dosen_pa` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mahasiswa`
--

INSERT INTO `mahasiswa` (`nim`, `user_id`, `nama_lengkap`, `prodi_id`, `tanggal_lahir`, `angkatan`, `semester_aktif`, `max_sks`, `dosen_pa`) VALUES
('22110011', 14, 'Felix Jonathan', 3, '2004-11-30', 2022, 6, 24, 'D003'),
('22110012', 19, 'Evan Saputra', 3, '2004-04-09', 2022, 6, 24, 'D008'),
('22110013', 20, 'Fiona Maharani', 3, '2004-06-18', 2022, 6, 24, 'D008'),
('22110014', 23, 'Ivan Gunawan', 3, '2004-10-27', 2022, 6, 24, 'D003'),
('23110007', 16, 'Brenda Olivia', 2, '2005-08-15', 2023, 4, 24, 'D002'),
('23110008', 17, 'Charles Darwin', 2, '2005-03-11', 2023, 4, 24, 'D007'),
('23110009', 18, 'Diana Angelica', 2, '2005-05-20', 2023, 4, 24, 'D007'),
('23110010', 22, 'Hanna Claudia', 2, '2005-11-01', 2023, 4, 24, 'D002'),
('24110001', 11, 'Anselmus Toby Adiputra', 1, '2006-04-01', 2024, 2, 24, 'D001'),
('24110002', 12, 'Nicholas Farrell', 1, '2006-02-11', 2024, 2, 24, 'D001'),
('24110003', 13, 'Kevin Saputra', 1, '2005-07-21', 2023, 4, 24, 'D004'),
('24110004', 15, 'Alex Pratama', 1, '2006-01-12', 2024, 2, 24, 'D006'),
('24110005', 21, 'Gabriel Fernando', 1, '2005-09-03', 2023, 4, 24, 'D006'),
('24110006', 24, 'Joana Michelle', 1, '2006-12-10', 2024, 2, 24, 'D001');

-- --------------------------------------------------------

--
-- Table structure for table `mata_kuliah`
--

CREATE TABLE `mata_kuliah` (
  `mk_id` int NOT NULL,
  `kode_mk` varchar(20) NOT NULL,
  `nama_mk` varchar(100) NOT NULL,
  `sks` int NOT NULL,
  `semester` int NOT NULL,
  `prodi_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mata_kuliah`
--

INSERT INTO `mata_kuliah` (`mk_id`, `kode_mk`, `nama_mk`, `sks`, `semester`, `prodi_id`) VALUES
(1, 'STI101', 'Algoritma dan Pemrograman', 3, 1, 1),
(2, 'STI102', 'Matematika Diskrit', 3, 1, 1),
(3, 'STI103', 'Pengantar Teknologi Informasi', 2, 1, 1),
(4, 'STI104', 'Logika Informatika', 2, 1, 1),
(5, 'STI105', 'Bahasa Inggris', 2, 1, 1),
(6, 'STI201', 'Basis Data', 3, 2, 1),
(7, 'STI202', 'Struktur Data', 3, 2, 1),
(8, 'STI203', 'Pemrograman Web', 3, 2, 1),
(9, 'STI204', 'Sistem Operasi', 3, 2, 1),
(10, 'STI205', 'Jaringan Komputer', 3, 2, 1),
(11, 'STI301', 'Pemrograman Berorientasi Objek', 3, 3, 1),
(12, 'STI302', 'Analisis dan Perancangan Sistem', 3, 3, 1),
(13, 'STI303', 'Cloud Computing', 3, 3, 1),
(14, 'STI304', 'Cyber Security', 3, 3, 1),
(15, 'STI305', 'Interaksi Manusia dan Komputer', 2, 3, 1),
(16, 'STI401', 'Machine Learning', 3, 4, 1),
(17, 'STI402', 'Mobile Programming', 3, 4, 1),
(18, 'STI403', 'Internet of Things', 3, 4, 1),
(19, 'STI404', 'Big Data Analytics', 3, 4, 1),
(20, 'STI405', 'Manajemen Proyek TI', 2, 4, 1),
(21, 'BD101', 'Pengantar Bisnis Digital', 3, 1, 2),
(22, 'BD102', 'Dasar Manajemen', 3, 1, 2),
(23, 'BD103', 'Komunikasi Bisnis', 2, 1, 2),
(24, 'BD104', 'Ekonomi Digital', 3, 1, 2),
(25, 'BD105', 'Statistika Bisnis', 3, 1, 2),
(26, 'BD201', 'Digital Marketing', 3, 2, 2),
(27, 'BD202', 'E-Commerce', 3, 2, 2),
(28, 'BD203', 'Search Engine Optimization', 2, 2, 2),
(29, 'BD204', 'Social Media Marketing', 3, 2, 2),
(30, 'BD205', 'Analisis Data Bisnis', 3, 2, 2),
(31, 'BD301', 'Business Intelligence', 3, 3, 2),
(32, 'BD302', 'Brand Management', 3, 3, 2),
(33, 'BD303', 'Strategi Media Sosial', 2, 3, 2),
(34, 'BD304', 'Startup Digital', 3, 3, 2),
(35, 'BD305', 'Customer Relationship Management', 3, 3, 2),
(36, 'BD401', 'Data Analytics', 3, 4, 2),
(37, 'BD402', 'Digital Advertising', 3, 4, 2),
(38, 'BD403', 'Technopreneurship', 3, 4, 2),
(39, 'BD404', 'UI UX Bisnis Digital', 2, 4, 2),
(40, 'BD405', 'Strategi Transformasi Digital', 3, 4, 2),
(41, 'KWU101', 'Pengantar Kewirausahaan', 3, 1, 3),
(42, 'KWU102', 'Dasar Akuntansi', 3, 1, 3),
(43, 'KWU103', 'Komunikasi Bisnis', 2, 1, 3),
(44, 'KWU104', 'Pengantar Ekonomi', 3, 1, 3),
(45, 'KWU105', 'Leadership', 2, 1, 3),
(46, 'KWU201', 'Manajemen Bisnis', 3, 2, 3),
(47, 'KWU202', 'Business Model Canvas', 2, 2, 3),
(48, 'KWU203', 'Kreativitas dan Inovasi', 3, 2, 3),
(49, 'KWU204', 'Manajemen Keuangan UMKM', 3, 2, 3),
(50, 'KWU205', 'Pemasaran Digital', 3, 2, 3),
(51, 'KWU301', 'Strategi Bisnis', 3, 3, 3),
(52, 'KWU302', 'Negosiasi Bisnis', 2, 3, 3),
(53, 'KWU303', 'Analisis Peluang Usaha', 3, 3, 3),
(54, 'KWU304', 'Manajemen SDM', 3, 3, 3),
(55, 'KWU305', 'Business Pitching', 2, 3, 3),
(56, 'KWU401', 'Startup dan Inovasi', 3, 4, 3),
(57, 'KWU402', 'Manajemen Risiko Bisnis', 3, 4, 3),
(58, 'KWU403', 'Strategi Pengembangan UMKM', 3, 4, 3),
(59, 'KWU404', 'Manajemen Operasional', 3, 4, 3),
(60, 'KWU405', 'Kepemimpinan Bisnis', 2, 4, 3);

-- --------------------------------------------------------

--
-- Table structure for table `prodi`
--

CREATE TABLE `prodi` (
  `prodi_id` int NOT NULL,
  `kode_prodi` varchar(10) NOT NULL,
  `nama_prodi` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prodi`
--

INSERT INTO `prodi` (`prodi_id`, `kode_prodi`, `nama_prodi`) VALUES
(1, 'STI', 'Sistem dan Teknologi Informasi'),
(2, 'BD', 'Bisnis Digital'),
(3, 'KWU', 'Kewirausahaan');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` int NOT NULL,
  `nama_role` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_id`, `nama_role`) VALUES
(1, 'Admin'),
(2, 'Mahasiswa'),
(3, 'Dosen'),
(4, 'Kaprodi');

-- --------------------------------------------------------

--
-- Table structure for table `ruangan`
--

CREATE TABLE `ruangan` (
  `ruangan_id` int NOT NULL,
  `nama_ruangan` varchar(50) NOT NULL,
  `kapasitas` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ruangan`
--

INSERT INTO `ruangan` (`ruangan_id`, `nama_ruangan`, `kapasitas`) VALUES
(1, 'Lab Komputer 1', 40),
(2, 'Lab Komputer 2', 35),
(3, 'Lab Multimedia', 30),
(4, 'Lab AI', 25),
(5, 'Ruang A101', 40),
(6, 'Ruang A102', 35),
(7, 'Ruang B201', 45),
(8, 'Ruang Smart Class', 50);

-- --------------------------------------------------------

--
-- Table structure for table `tahun_akademik`
--

CREATE TABLE `tahun_akademik` (
  `tahun_id` int NOT NULL,
  `tahun_ajaran` varchar(20) NOT NULL,
  `semester` enum('Ganjil','Genap') NOT NULL,
  `status_aktif` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tahun_akademik`
--

INSERT INTO `tahun_akademik` (`tahun_id`, `tahun_ajaran`, `semester`, `status_aktif`) VALUES
(1, '2024/2025', 'Ganjil', 0),
(2, '2024/2025', 'Genap', 1),
(3, '2025/2026', 'Ganjil', 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` bigint NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `role_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `email`, `role_id`) VALUES
(1, 'admin01', '123', 'admin01@kampus.ac.id', 1),
(2, 'admin02', '123', 'admin02@kampus.ac.id', 1),
(3, 'andi', '123', 'andi@kampus.ac.id', 3),
(4, 'budi', '123', 'budi@kampus.ac.id', 3),
(5, 'sinta', '123', 'sinta@kampus.ac.id', 4),
(6, 'rahmat', '123', 'rahmat@kampus.ac.id', 3),
(7, 'lina', '123', 'lina@kampus.ac.id', 3),
(8, 'yohanes', '123', 'yohanes@kampus.ac.id', 3),
(9, 'dewi', '123', 'dewi@kampus.ac.id', 3),
(10, 'ferdi', '123', 'ferdi@kampus.ac.id', 3),
(11, 'toby', '123', 'toby@student.ac.id', 2),
(12, 'nico', '123', 'nico@student.ac.id', 2),
(13, 'kevin', '123', 'kevin@student.ac.id', 2),
(14, 'felix', '123', 'felix@student.ac.id', 2),
(15, 'alex', '123', 'alex@student.ac.id', 2),
(16, 'brenda', '123', 'brenda@student.ac.id', 2),
(17, 'charles', '123', 'charles@student.ac.id', 2),
(18, 'diana', '123', 'diana@student.ac.id', 2),
(19, 'evan', '123', 'evan@student.ac.id', 2),
(20, 'fiona', '123', 'fiona@student.ac.id', 2),
(21, 'gabriel', '123', 'gabriel@student.ac.id', 2),
(22, 'hanna', '123', 'hanna@student.ac.id', 2),
(23, 'ivan', '123', 'ivan@student.ac.id', 2),
(24, 'joana', '123', 'joana@student.ac.id', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dosen`
--
ALTER TABLE `dosen`
  ADD PRIMARY KEY (`nip`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`jadwal_id`),
  ADD KEY `kelas_id` (`kelas_id`),
  ADD KEY `ruangan_id` (`ruangan_id`);

--
-- Indexes for table `kelas`
--
ALTER TABLE `kelas`
  ADD PRIMARY KEY (`kelas_id`),
  ADD KEY `mk_id` (`mk_id`),
  ADD KEY `nip` (`nip`),
  ADD KEY `tahun_id` (`tahun_id`);

--
-- Indexes for table `krs`
--
ALTER TABLE `krs`
  ADD PRIMARY KEY (`krs_id`),
  ADD KEY `nim` (`nim`),
  ADD KEY `tahun_id` (`tahun_id`);

--
-- Indexes for table `krs_detail`
--
ALTER TABLE `krs_detail`
  ADD PRIMARY KEY (`detail_id`),
  ADD KEY `krs_id` (`krs_id`),
  ADD KEY `jadwal_id` (`jadwal_id`);

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`nim`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `prodi_id` (`prodi_id`),
  ADD KEY `dosen_pa` (`dosen_pa`);

--
-- Indexes for table `mata_kuliah`
--
ALTER TABLE `mata_kuliah`
  ADD PRIMARY KEY (`mk_id`),
  ADD KEY `prodi_id` (`prodi_id`);

--
-- Indexes for table `prodi`
--
ALTER TABLE `prodi`
  ADD PRIMARY KEY (`prodi_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `ruangan`
--
ALTER TABLE `ruangan`
  ADD PRIMARY KEY (`ruangan_id`);

--
-- Indexes for table `tahun_akademik`
--
ALTER TABLE `tahun_akademik`
  ADD PRIMARY KEY (`tahun_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jadwal`
--
ALTER TABLE `jadwal`
  MODIFY `jadwal_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `kelas`
--
ALTER TABLE `kelas`
  MODIFY `kelas_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `krs`
--
ALTER TABLE `krs`
  MODIFY `krs_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `krs_detail`
--
ALTER TABLE `krs_detail`
  MODIFY `detail_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mata_kuliah`
--
ALTER TABLE `mata_kuliah`
  MODIFY `mk_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT for table `prodi`
--
ALTER TABLE `prodi`
  MODIFY `prodi_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `ruangan`
--
ALTER TABLE `ruangan`
  MODIFY `ruangan_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `tahun_akademik`
--
ALTER TABLE `tahun_akademik`
  MODIFY `tahun_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `dosen`
--
ALTER TABLE `dosen`
  ADD CONSTRAINT `dosen_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD CONSTRAINT `jadwal_ibfk_1` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`kelas_id`),
  ADD CONSTRAINT `jadwal_ibfk_2` FOREIGN KEY (`ruangan_id`) REFERENCES `ruangan` (`ruangan_id`);

--
-- Constraints for table `kelas`
--
ALTER TABLE `kelas`
  ADD CONSTRAINT `kelas_ibfk_1` FOREIGN KEY (`mk_id`) REFERENCES `mata_kuliah` (`mk_id`),
  ADD CONSTRAINT `kelas_ibfk_2` FOREIGN KEY (`nip`) REFERENCES `dosen` (`nip`),
  ADD CONSTRAINT `kelas_ibfk_3` FOREIGN KEY (`tahun_id`) REFERENCES `tahun_akademik` (`tahun_id`);

--
-- Constraints for table `krs`
--
ALTER TABLE `krs`
  ADD CONSTRAINT `krs_ibfk_1` FOREIGN KEY (`nim`) REFERENCES `mahasiswa` (`nim`),
  ADD CONSTRAINT `krs_ibfk_2` FOREIGN KEY (`tahun_id`) REFERENCES `tahun_akademik` (`tahun_id`);

--
-- Constraints for table `krs_detail`
--
ALTER TABLE `krs_detail`
  ADD CONSTRAINT `krs_detail_ibfk_1` FOREIGN KEY (`krs_id`) REFERENCES `krs` (`krs_id`),
  ADD CONSTRAINT `krs_detail_ibfk_2` FOREIGN KEY (`jadwal_id`) REFERENCES `jadwal` (`jadwal_id`);

--
-- Constraints for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD CONSTRAINT `mahasiswa_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `mahasiswa_ibfk_2` FOREIGN KEY (`prodi_id`) REFERENCES `prodi` (`prodi_id`),
  ADD CONSTRAINT `mahasiswa_ibfk_3` FOREIGN KEY (`dosen_pa`) REFERENCES `dosen` (`nip`);

--
-- Constraints for table `mata_kuliah`
--
ALTER TABLE `mata_kuliah`
  ADD CONSTRAINT `mata_kuliah_ibfk_1` FOREIGN KEY (`prodi_id`) REFERENCES `prodi` (`prodi_id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
