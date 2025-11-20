<?php

include 'connect.php';

$username = trim($_POST['username'] ?? '');
$email = trim($_POST['email'] ?? '');
$password = $_POST['password'] ?? '';

if (empty($username) || empty($email) || empty($password)) {
    header('Content-Type: application/json');
    echo json_encode(['status' => 'error', 'message' => 'Semua kolom wajib diisi!']);
    exit;
}

$stmt = $conn->prepare("SELECT iduser FROM user WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    header('Content-Type: application/json');
    echo json_encode(['status' => 'error', 'message' => 'Email sudah terdaftar!']);
    $stmt->close(); 
    $conn->close(); 
    exit;
}
$stmt->close(); 

$hashed = password_hash($password, PASSWORD_DEFAULT);
$stmt = $conn->prepare("INSERT INTO user (username, email, password) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $username, $email, $hashed);

if ($stmt->execute()) {
    header('Content-Type: application/json');
    echo json_encode(['status' => 'success', 'message' => 'Registrasi berhasil!']);
} else {
    header('Content-Type: application/json');
    echo json_encode(['status' => 'error', 'message' => 'Gagal menyimpan data user!']);
}

$stmt->close();
$conn->close();
?>
