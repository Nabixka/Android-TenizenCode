<?php

include '../connect.php'; 

$nis = $_POST['nis'] ?? '';
$namasiswa = $_POST['namasiswa'] ?? '';
$alamat = $_POST['alamat'] ?? '';
$gender = $_POST['gender'] ?? '';
$tanggallahir = $_POST['tanggallahir'] ?? '';

if (empty($nis) || empty($namasiswa) || empty($alamat) || empty($gender) || empty($tanggallahir)) {
    echo 'Gagal: Semua kolom wajib diisi!';
    exit;
}

$stmt = $conn->prepare("INSERT INTO siswa (nis, namasiswa, alamat, gender, tanggallahir) 
                        VALUES (?, ?, ?, ?, ?)");

$stmt->bind_param("sssss", $nis, $namasiswa, $alamat, $gender, $tanggallahir);

if ($stmt->execute()) {
    echo 'Data siswa berhasil disimpan!';
} else {
    echo 'Gagal: ' . $stmt->error;
}

$stmt->close();
$conn->close();
?>
