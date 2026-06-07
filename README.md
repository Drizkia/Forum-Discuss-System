# Forum Discussion System

## Overview

Forum Discussion System adalah aplikasi forum diskusi berbasis desktop yang dibangun menggunakan Java Swing dan SQLite. Sistem memungkinkan pengguna untuk melakukan registrasi akun, login, membuat postingan, memberikan komentar, mengedit postingan, serta mengelola diskusi melalui antarmuka grafis yang sederhana dan mudah digunakan.

Proyek ini dikembangkan sebagai implementasi konsep Pemrograman Berorientasi Objek (Object-Oriented Programming), penerapan prinsip SOLID, serta penggunaan pola arsitektur Model-View-Controller (MVC) dalam pengembangan aplikasi desktop.

---

## Features

### User Management

* Registrasi pengguna baru
* Login pengguna
* Validasi username
* Penyimpanan data pengguna ke database

### Discussion Management

* Membuat postingan diskusi
* Menampilkan seluruh postingan
* Mengedit postingan
* Menghapus postingan
* Menampilkan detail diskusi

### Comment Management

* Menambahkan komentar
* Menampilkan komentar pada postingan
* Menghapus komentar

### Database Integration

* SQLite Database
* JDBC Connection
* Pembuatan tabel otomatis saat aplikasi dijalankan

---

## Technologies

| Technology       | Purpose                    |
| ---------------- | -------------------------- |
| Java             | Main Programming Language  |
| Java Swing       | Graphical User Interface   |
| SQLite           | Database Storage           |
| JDBC             | Database Connectivity      |
| MVC Pattern      | Application Architecture   |
| SOLID Principles | Software Design Principles |

---

## Project Structure

```text
src/
│
├── Main/
│   └── MainApp.java
│
├── controller/
│   ├── ControllerLogin.java
│   ├── ControllerMain.java
│   ├── ForumController.java
│   ├── ForumService.java
│   └── IForumService.java
│
├── database/
│   └── DatabaseHelper.java
│
├── gui/
│   ├── LoginFrame.java
│   ├── MainFrame.java
│   ├── CreateEditPostFrame.java
│   └── PostDetailFrame.java
│
└── model/
    ├── User.java
    ├── Post.java
    ├── Comment.java
    ├── ForumItem.java
    └── Displayable.java
```

---

## MVC Architecture

### Model

Merepresentasikan data dan aturan bisnis aplikasi.

* User
* Post
* Comment
* ForumItem
* Displayable

### View

Menangani tampilan antarmuka pengguna.

* LoginFrame
* MainFrame
* CreateEditPostFrame
* PostDetailFrame

### Controller

Menghubungkan View dengan Model dan mengelola alur aplikasi.

* ControllerLogin
* ControllerMain
* ForumController
* ForumService

---

## Object-Oriented Programming Implementation

### Encapsulation

Setiap model menyimpan data menggunakan atribut private dan menyediakan akses melalui getter dan setter.

```java
private String username;

public String getUsername() {
    return username;
}
```

### Inheritance

Class Post dan Comment mewarisi abstract class ForumItem.

```java
public class Post extends ForumItem
```

```java
public class Comment extends ForumItem
```

### Abstraction

ForumItem dibuat sebagai abstract class yang mendefinisikan perilaku dasar seluruh konten forum.

```java
public abstract class ForumItem
```

### Polymorphism

Method getDisplayText() dioverride oleh class turunan untuk menghasilkan representasi tampilan yang berbeda.

```java
@Override
public String getDisplayText()
```

### Composition

Post memiliki relasi dengan User dan kumpulan Comment.

```java
private User author;
private List<Comment> comments;
```

---

## SOLID Principles Implementation

### Single Responsibility Principle (SRP)

Setiap class memiliki satu tanggung jawab utama.

| Class           | Responsibility              |
| --------------- | --------------------------- |
| User            | Menyimpan data pengguna     |
| Post            | Menyimpan data postingan    |
| Comment         | Menyimpan data komentar     |
| DatabaseHelper  | Mengelola database          |
| ControllerLogin | Mengelola login             |
| ControllerMain  | Mengelola navigasi aplikasi |
| ForumService    | Menjalankan logika bisnis   |

---

### Open Closed Principle (OCP)

Sistem dapat diperluas dengan menambahkan jenis ForumItem baru tanpa mengubah struktur utama aplikasi.

---

### Liskov Substitution Principle (LSP)

Objek Post dan Comment dapat digunakan sebagai ForumItem tanpa mengubah perilaku program.

```java
ForumItem item = new Post(...);
```

---

### Interface Segregation Principle (ISP)

Interface Displayable menyediakan kontrak khusus untuk objek yang dapat ditampilkan.

```java
public interface Displayable {
    String getDisplayText();
}
```

---

### Dependency Inversion Principle (DIP)

Controller bergantung pada abstraksi IForumService, bukan implementasi konkret.

```java
public interface IForumService
```

```java
public class ForumService implements IForumService
```

---

## Database Design

### users

| Field    | Type    |
| -------- | ------- |
| id       | INTEGER |
| username | TEXT    |
| email    | TEXT    |

### posts

| Field     | Type     |
| --------- | -------- |
| id        | INTEGER  |
| title     | TEXT     |
| content   | TEXT     |
| author_id | INTEGER  |
| timestamp | DATETIME |

### comments

| Field     | Type     |
| --------- | -------- |
| id        | INTEGER  |
| content   | TEXT     |
| post_id   | INTEGER  |
| author_id | INTEGER  |
| timestamp | DATETIME |

---

## How to Run

### Requirements

* Java JDK 8+
* NetBeans IDE (Recommended)
* SQLite JDBC Driver

### Steps

Clone repository:

```bash
git clone https://github.com/Drizkia/Forum-Discuss-System.git
```

Open project in NetBeans.

Ensure SQLite JDBC library is available.

Run:

```bash
MainApp.java
```

The database and tables will be initialized automatically.

---

## Learning Outcomes

Proyek ini menunjukkan implementasi:

* Object-Oriented Programming (OOP)
* MVC Architecture
* SQLite Database Integration
* Java Swing GUI Development
* SOLID Design Principles
* JDBC Connectivity

---

## Authors

Kelompok 19

Forum Discussion System dibuat sebagai proyek pembelajaran untuk menerapkan konsep OOP, SOLID, MVC, GUI Programming, dan Database Management menggunakan Java.
