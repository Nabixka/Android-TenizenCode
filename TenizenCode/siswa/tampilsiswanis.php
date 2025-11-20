<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

include '../connect.php'; 
if (isset($_GET['nis'])) {
    $nis = mysqli_real_escape_string($conn, $_GET['nis']);

    if (empty($nis)) {
        $query = "SELECT * FROM siswa ORDER BY namasiswa ASC";
    } else {
        $query = "SELECT * FROM siswa WHERE nis LIKE '%$nis%' ORDER BY namasiswa ASC";
    }
} else {
    $query = "SELECT * FROM siswa ORDER BY namasiswa ASC";
}

$result = mysqli_query($conn, $query);

if ($result) {
    $siswaList = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $siswaList[] = $row;
    }

    if (!empty($siswaList)) {
        echo json_encode([
            'status' => 'success',
            'data' => $siswaList
        ]);
    } else {
        echo json_encode([
            'status' => 'success', 
            'message' => 'Data siswa tidak ditemukan.',
            'data' => []
        ]);
    }
} else {
    http_response_code(500);
    echo json_encode([
        'status' => 'error',
        'message' => 'Query Gagal: ' . mysqli_error($conn)
    ]);
}

mysqli_close($conn);
?>
