<?php
// Set header untuk respons JSON dan izinkan metode DELETE
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: DELETE');
header('Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With');

include '../connect.php';

// Ambil data dari body request, karena metode DELETE tidak menggunakan $_POST
$input = json_decode(file_get_contents("php://input"), true);

// Validasi input: pastikan 'nis' ada dan tidak kosong
if (empty($input['nis'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'NIS siswa wajib diisi!'
    ]);
    exit;
}

// Amankan NIS dari SQL Injection
$nis = mysqli_real_escape_string($conn, $input['nis']);

// Query untuk menghapus siswa berdasarkan NIS
$query = "DELETE FROM siswa WHERE nis = '$nis'";

if (mysqli_query($conn, $query)) {
    // Cek apakah ada baris yang terhapus
    if (mysqli_affected_rows($conn) > 0) {
        echo json_encode([
            'status' => 'success',
            'message' => 'Siswa berhasil dihapus!'
        ]);
    } else {
        echo json_encode([
            'status' => 'error',
            'message' => 'Siswa dengan NIS tersebut tidak ditemukan.'
        ]);
    }
} else {
    // Jika query gagal
    echo json_encode([
        'status' => 'error',
        'message' => 'Gagal menghapus siswa: ' . mysqli_error($conn)
    ]);
}

mysqli_close($conn);
?>
