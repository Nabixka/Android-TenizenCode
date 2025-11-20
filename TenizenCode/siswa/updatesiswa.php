<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With');

if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
    http_response_code(200);
    exit();
}

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Metode tidak diizinkan. Gunakan POST.']);
    exit();
}

include '../connect.php';

$nis = $_POST['nis'] ?? null;
$namasiswa = $_POST['namasiswa'] ?? null;
$gender = $_POST['gender'] ?? null;
$alamat = $_POST['alamat'] ?? null;
$tanggallahir = $_POST['tanggallahir'] ?? null;

if (empty($nis)) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'NIS wajib diisi untuk update!']);
    exit;
}

if (empty($namasiswa) || empty($gender) || empty($alamat) || empty($tanggallahir)) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Semua kolom wajib diisi!']);
    exit;
}

$escaped_nis = mysqli_real_escape_string($conn, $nis);
$escaped_namasiswa = mysqli_real_escape_string($conn, $namasiswa);
$escaped_gender = mysqli_real_escape_string($conn, $gender);
$escaped_alamat = mysqli_real_escape_string($conn, $alamat);
$escaped_tanggallahir = mysqli_real_escape_string($conn, $tanggallahir);

$query = "UPDATE siswa SET 
            namasiswa = '$escaped_namasiswa', 
            gender = '$escaped_gender', 
            alamat = '$escaped_alamat', 
            tanggallahir = '$escaped_tanggallahir'
          WHERE nis = '$escaped_nis'";

if (mysqli_query($conn, $query)) {
    if (mysqli_affected_rows($conn) > 0) {
        echo json_encode(['status' => 'success', 'message' => 'Data siswa berhasil diperbarui!']);
    } else {
        echo json_encode(['status' => 'success', 'message' => 'Tidak ada perubahan data.']); 
    }
} else {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Gagal memperbarui data: ' . mysqli_error($conn)]);
}

mysqli_close($conn);
?>
