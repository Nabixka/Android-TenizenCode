<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

include '../connect.php';

$input = json_decode(file_get_contents("php://input"), true);

if (
    empty($input['idproduk']) || empty($input['namaproduk']) ||
    empty($input['jumlah']) || empty($input['harga']) || empty($input['barcode'])
) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Semua kolom wajib diisi!'
    ]);
    exit;
}

$idproduk   = mysqli_real_escape_string($conn, $input['idproduk']);
$namaproduk = mysqli_real_escape_string($conn, $input['namaproduk']);
$jumlah     = (int)$input['jumlah'];
$harga      = (int)$input['harga'];
$barcode    = mysqli_real_escape_string($conn, $input['barcode']);

$query = "INSERT INTO produk (idproduk, namaproduk, jumlah, harga, barcode)
          VALUES ('$idproduk', '$namaproduk', '$jumlah', '$harga', '$barcode')";

if (mysqli_query($conn, $query)) {
    echo json_encode([
        'status' => 'success',
        'message' => 'Produk berhasil ditambahkan!'
    ]);
} else {
    echo json_encode([
        'status' => 'error',
        'message' => 'Gagal menambahkan produk: ' . mysqli_error($conn)
    ]);
}
?>
