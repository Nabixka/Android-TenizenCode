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

if (empty($nis)) {
    http_response_code(400); 
    echo json_encode(['status' => 'error', 'message' => 'NIS siswa wajib diisi!']);
    exit;
}

$escaped_nis = mysqli_real_escape_string($conn, $nis);

$query = "DELETE FROM siswa WHERE nis = '$escaped_nis'";

if (mysqli_query($conn, $query)) {
    if (mysqli_affected_rows($conn) > 0) {
        http_response_code(200); 
        echo json_encode(['status' => 'success', 'message' => 'Siswa berhasil dihapus!']);
    } else {
        http_response_code(404); 
        echo json_encode(['status' => 'error', 'message' => 'Siswa dengan NIS tersebut tidak ditemukan.']);
    }
} else {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Gagal menghapus siswa: ' . mysqli_error($conn)]);
}

mysqli_close($conn);
?>
