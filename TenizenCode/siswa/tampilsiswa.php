<?php

require_once('../connect.php'); 

$baseUrl = "http://" . $_SERVER['SERVER_NAME'] . "/TenizenCode/public/foto/";

$sql = "SELECT * FROM siswa ORDER BY namasiswa ASC";
$result = mysqli_query($conn, $sql);

$dataSiswa = array(); 

if ($result) {
    while($row = mysqli_fetch_assoc($result)){
        $fotoUrl = !empty($row['foto']) ? $baseUrl . $row['foto'] : null;

        $dataSiswa[] = array(
            "nis" => $row["nis"],
            "namasiswa" => $row["namasiswa"],
            "gender" => $row["gender"],
            "alamat" => $row["alamat"],
            "tanggallahir" => $row["tanggallahir"],
            "foto" => $fotoUrl 
        );
    }
}

$response = array(
    'status' => 'success',
    'message' => 'Data siswa berhasil diambil.',
    'data' => $dataSiswa
);

header('Content-Type: application/json');
echo json_encode($response);

mysqli_close($conn);
?>
