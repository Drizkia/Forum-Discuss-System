package sistemforumdiskusi_19.gui;

import sistemforumdiskusi_19.model.User;
import sistemforumdiskusi_19.service.ForumService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private ForumService forumService;

    public LoginFrame(ForumService forumService) {
        this.forumService = forumService;

        setTitle("Sistem Forum Diskusi - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Login", createLoginPanel());
        tabbedPane.addTab("Register", createRegisterPanel());

        add(tabbedPane);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField(15);
        JButton btnLogin = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtUsername, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = forumService.loginUser(username);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat datang " + user.getUsername());
                new MainFrame(forumService, user).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username tidak ditemukan. Silakan mendaftar terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField(15);
        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField(15);
        JButton btnRegister = new JButton("Register");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtEmail, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(btnRegister, gbc);

        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            
            if (username.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Email tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cek apakah username sudah ada
            if (forumService.loginUser(username) != null) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = forumService.registerUser(username, email);
            if (newUser != null) {
                JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan login di tab Login.");
                txtUsername.setText("");
                txtEmail.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Registrasi Gagal!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
}
