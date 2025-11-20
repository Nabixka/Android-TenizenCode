<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

include '../connect.php';

if (isset($_GET['idproduk'])) {
    $idproduk = mysqli_real_escape_string($conn, $_GET['idproduk']);
    $query = "SELECT * FROM produk WHERE idproduk = '$idproduk'";
} else {
    $query = "SELECT * FROM produk ORDER BY tanggal DESC";
}

$result = mysqli_query($conn, $query);
$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode([
    'status' => 'success',
    'data' => $data
]);
?>
