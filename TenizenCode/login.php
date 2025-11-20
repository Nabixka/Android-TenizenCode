<?php


include 'connect.php';

$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';

if (empty($email) || empty($password)) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Email dan password wajib diisi!'
    ]);
    exit;
}

$stmt = $conn->prepare("SELECT iduser, username, email, password FROM user WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {
    $user = $result->fetch_assoc();

    if (password_verify($password, $user['password'])) {
        echo json_encode([
            'status' => 'success',
            'message' => 'Login berhasil!',
            'data' => [
                'iduser' => $user['iduser'],
                'username' => $user['username'],
                'email' => $user['email']
            ]
        ]);
    } else {
        echo json_encode([
            'status' => 'error',
            'message' => 'Password salah!'
        ]);
    }
} else {
    echo json_encode([
        'status' => 'error',
        'message' => 'Email tidak ditemukan!'
    ]);
}

$stmt->close();
$conn->close();
?>
