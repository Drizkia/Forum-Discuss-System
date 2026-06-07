package sistemforumdiskusi_19.gui;

import sistemforumdiskusi_19.controller.ForumController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame
 */
public class LoginFrame extends JFrame {

    // Palette
    private static final Color BG1     = new Color(79, 70, 229);   
    private static final Color BG2     = new Color(124, 58, 237);  
    private static final Color PRIMARY = new Color(79, 70, 229);
    private static final Color PHOVER  = new Color(67, 56, 202);
    private static final Color SUCCESS = new Color(16, 185, 129);
    private static final Color SHOVER  = new Color(5, 150, 105);
    private static final Color CTEXT   = new Color(15, 23, 42);
    private static final Color CSUB    = new Color(100, 116, 139);
    private static final Color CBORDER = new Color(203, 213, 225);
    private static final Color CFBG    = new Color(248, 250, 252);

    private final ForumController controller;

    public LoginFrame(ForumController controller) {
        this.controller = controller;
        setTitle("Forum Diskusi — Login");
        setSize(430, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(buildBackground());
    }

    // Background
    private JPanel buildBackground() {
        JPanel bg = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setPaint(new GradientPaint(0, 0, BG1, 0, getHeight(), BG2));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bg.add(buildCard());
        return bg;
    }

    // Card
    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                g2.setColor(new Color(0, 0, 0, 35));
                g2.fillRoundRect(5, 7, getWidth() - 5, getHeight() - 5, 22, 22);
                // Body
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 7, 22, 22);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(370, 490));
        card.setBorder(new EmptyBorder(36, 38, 36, 38));
        card.add(buildHeader(), BorderLayout.NORTH);
        card.add(buildTabs(),   BorderLayout.CENTER);
        return card;
    }

    // Card header
    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Forum Diskusi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(CTEXT);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Komunitas diskusi online Anda");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(CSUB);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(3));
        p.add(sub);
        p.add(Box.createVerticalStrut(20));
        return p;
    }

    // Tabs
    private JTabbedPane buildTabs() {
        JTabbedPane tp = new JTabbedPane();
        tp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.addTab("   Masuk   ",  buildLoginTab());
        tp.addTab("   Daftar   ", buildRegisterTab());
        return tp;
    }

    // Login tab
    private JPanel buildLoginTab() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 4, 4, 4));

        JTextField txtUser = mkField();
        JButton    btnLogin = mkBtn("Masuk", PRIMARY, PHOVER);

        p.add(fGroup("Username", txtUser));
        p.add(Box.createVerticalStrut(20));
        p.add(btnLogin);
        p.add(Box.createVerticalGlue());

        btnLogin.addActionListener(e -> {
            String u = txtUser.getText().trim();
            if (u.isEmpty()) { warn("Username tidak boleh kosong!"); return; }
            if (controller.login(u)) {
                JOptionPane.showMessageDialog(this,
                    "Selamat datang, " + controller.getCurrentUser().getUsername() + "!",
                    "Login Berhasil", JOptionPane.INFORMATION_MESSAGE);
                controller.openMainFrame();
                dispose();
            } else {
                warn("Username tidak ditemukan.\nSilakan daftar terlebih dahulu.");
            }
        });
        return p;
    }

    // Register tab
    private JPanel buildRegisterTab() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 4, 4, 4));

        JTextField txtUser  = mkField();
        JTextField txtEmail = mkField();
        JButton    btnReg   = mkBtn("Buat Akun", SUCCESS, SHOVER);

        p.add(fGroup("Username", txtUser));
        p.add(Box.createVerticalStrut(12));
        p.add(fGroup("Email", txtEmail));
        p.add(Box.createVerticalStrut(20));
        p.add(btnReg);
        p.add(Box.createVerticalGlue());

        btnReg.addActionListener(e -> {
            String u  = txtUser.getText().trim();
            String em = txtEmail.getText().trim();
            if (u.isEmpty() || em.isEmpty()) { warn("Semua field harus diisi!"); return; }
            if (controller.usernameExists(u)) {
                warn("Username \'" + u + "\' sudah digunakan.\nCoba username lain.");
                return;
            }
            if (controller.register(u, em)) {
                JOptionPane.showMessageDialog(this,
                    "Akun berhasil dibuat!\nSilakan masuk di tab Masuk.",
                    "Registrasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
                txtUser.setText(""); txtEmail.setText("");
            } else {
                warn("Gagal membuat akun. Coba lagi.");
            }
        });
        return p;
    }

    // Helpers
    private JPanel fGroup(String label, JTextField f) {
        JPanel g = new JPanel(new BorderLayout(0, 5));
        g.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(CTEXT);
        g.add(l, BorderLayout.NORTH);
        g.add(f, BorderLayout.CENTER);
        g.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        return g;
    }

    private JTextField mkField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(CFBG);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CBORDER, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return f;
    }

    private JButton mkBtn(String text, Color bg, Color hover) {
        JButton b = new JButton(text) {
            boolean ov;
            {
                setContentAreaFilled(false); setBorderPainted(false);
                setOpaque(false); setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
                setAlignmentX(CENTER_ALIGNMENT);
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { ov = true; repaint(); }
                    public void mouseExited (MouseEvent e) { ov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ov ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setFont(getFont()); g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return b;
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Perhatian", JOptionPane.WARNING_MESSAGE);
    }
}
