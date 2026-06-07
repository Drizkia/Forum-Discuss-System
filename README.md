# Forum Discussion System

## Deskripsi Proyek

Forum Discussion System merupakan aplikasi forum diskusi berbasis desktop yang dikembangkan menggunakan Java Swing dan SQLite. Sistem ini memungkinkan pengguna untuk melakukan registrasi, login, membuat postingan diskusi, mengedit postingan, menghapus postingan, serta memberikan komentar pada setiap diskusi yang tersedia.

Aplikasi dibangun menggunakan pola arsitektur MVC (Model-View-Controller) untuk memisahkan logika bisnis, tampilan, dan pengelolaan data sehingga kode lebih terstruktur, mudah dikembangkan, dan mudah dipelihara.

---

## Tujuan Proyek

* Menerapkan konsep Pemrograman Berorientasi Objek (OOP).
* Mengimplementasikan pola desain MVC.
* Mengelola data menggunakan database SQLite.
* Membangun aplikasi desktop interaktif menggunakan Java Swing.
* Menerapkan prinsip-prinsip SOLID dalam pengembangan perangkat lunak.

---

## Fitur Utama

### Manajemen Pengguna

* Registrasi akun baru
* Login pengguna
* Logout pengguna

### Manajemen Postingan

* Membuat postingan diskusi
* Melihat seluruh postingan
* Mengedit postingan
* Menghapus postingan

### Manajemen Komentar

* Menambahkan komentar
* Menampilkan komentar pada postingan
* Menghapus komentar

### Database

* Penyimpanan data pengguna
* Penyimpanan postingan
* Penyimpanan komentar
* Database SQLite terintegrasi

---

## Teknologi yang Digunakan

| Teknologi   | Kegunaan                     |
| ----------- | ---------------------------- |
| Java        | Bahasa pemrograman utama     |
| Java Swing  | Antarmuka pengguna (GUI)     |
| SQLite      | Database lokal               |
| JDBC        | Koneksi Java dengan SQLite   |
| MVC Pattern | Struktur arsitektur aplikasi |

---

## Struktur Proyek

```text
src/
│
├── controller/
│   ├── ForumController.java
│   ├── ForumService.java
│   ├── ControllerLogin.java
│   └── ControllerMain.java
│
├── model/
│   ├── User.java
│   ├── Post.java
│   └── Comment.java
│
├── gui/
│   ├── LoginFrame.java
│   ├── MainFrame.java
│   ├── CreateEditPostFrame.java
│   └── PostDetailFrame.java
│
├── database/
│   └── DatabaseHelper.java
│
└── SistemForumDiskusi_19.java
```

---

## Arsitektur MVC

### Model

Menyimpan struktur data aplikasi.

* User
* Post
* Comment

### View

Menampilkan antarmuka pengguna.

* LoginFrame
* MainFrame
* CreateEditPostFrame
* PostDetailFrame

### Controller

Menghubungkan View dan Model.

* ForumController
* ControllerLogin
* ControllerMain
* ForumService

---

## Konsep OOP yang Digunakan

### Encapsulation

Data pada setiap model disimpan menggunakan atribut private dan diakses melalui getter serta setter.

Contoh:

```java
private String username;

public String getUsername() {
    return username;
}
```

### Composition

Relasi antar objek dibangun menggunakan komposisi.

Contoh:

```java
private User author;
private List<Comment> comments;
```

### Abstraction

GUI hanya berinteraksi dengan controller tanpa mengetahui detail database.

Contoh:

```java
controller.createPost(...);
```

### Inheritance

Seluruh tampilan GUI mewarisi komponen Swing seperti:

```java
public class LoginFrame extends JFrame
```

```java
public class CreateEditPostFrame extends JDialog
```

---

## Implementasi SOLID

### Single Responsibility Principle (SRP)

Setiap kelas memiliki satu tanggung jawab utama.

* User → data pengguna
* Post → data postingan
* Comment → data komentar
* ControllerLogin → autentikasi
* ControllerMain → navigasi frame
* ForumService → logika bisnis forum

### Open Closed Principle (OCP)

GUI menggunakan ForumController sebagai façade sehingga fitur baru dapat ditambahkan tanpa mengubah seluruh tampilan.

### Liskov Substitution Principle (LSP)

Turunan JFrame dan JDialog dapat digunakan menggantikan superclass Swing tanpa mengubah perilaku program.

### Interface Segregation Principle (ISP)

Belum menggunakan interface khusus karena kebutuhan proyek masih sederhana.

### Dependency Inversion Principle (DIP)

Controller bergantung pada abstraksi layanan (ForumService) untuk mengakses logika bisnis, bukan langsung mengakses database.

---

## Cara Menjalankan Program

### Persyaratan

* JDK 8 atau lebih baru
* NetBeans IDE (disarankan)
* SQLite JDBC Driver

### Langkah Menjalankan

1. Clone repository:

```bash
git clone https://github.com/Drizkia/Forum-Discuss-System.git
```

2. Buka project pada NetBeans.

3. Pastikan library SQLite JDBC sudah terpasang.

4. Jalankan:

```bash
SistemForumDiskusi_19.java
```

5. Database akan dibuat otomatis apabila belum tersedia.

---

## Database

Aplikasi menggunakan tiga tabel utama:

### users

| Field    | Tipe    |
| -------- | ------- |
| id       | INTEGER |
| username | TEXT    |
| email    | TEXT    |

### posts

| Field     | Tipe     |
| --------- | -------- |
| id        | INTEGER  |
| title     | TEXT     |
| content   | TEXT     |
| author_id | INTEGER  |
| timestamp | DATETIME |

### comments

| Field     | Tipe     |
| --------- | -------- |
| id        | INTEGER  |
| content   | TEXT     |
| post_id   | INTEGER  |
| author_id | INTEGER  |
| timestamp | DATETIME |

---

Proyek dibuat sebagai implementasi konsep Pemrograman Berorientasi Objek (OOP), MVC Pattern, dan Database Management menggunakan Java Swing dan SQLite.
