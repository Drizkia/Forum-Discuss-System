package sistemforumdiskusi_19.gui;

import sistemforumdiskusi_19.model.Post;
import sistemforumdiskusi_19.controller.ForumController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class CreateEditPostFrame extends JDialog {
    private static final Color HDR1    = new Color(79, 70, 229);
    private static final Color HDR2    = new Color(124, 58, 237); 
    private static final Color C_BG    = new Color(241, 245, 249);
    private static final Color C_CARD  = Color.WHITE;
    private static final Color C_TEXT  = new Color(15, 23, 42);
    private static final Color C_SUB   = new Color(100, 116, 139);
    private static final Color C_BORD  = new Color(226, 232, 240); 
    private static final Color PRIMARY = new Color(79, 70, 229);
    private static final Color PHOVER  = new Color(67, 56, 202);
    private static final Color SUCCESS = new Color(16, 185, 129);
    private static final Color SHOVER  = new Color(5, 150, 105);

    private final MainFrame parentFrame;
    private final ForumController controller;
    private final Post postToEdit;

    private JTextField txtTitle;
    private JTextArea txtContent;

    public CreateEditPostFrame(MainFrame parent, ForumController controller, Post postToEdit) {
        super(parent, postToEdit == null ? "Buat Postingan Baru" : "Edit Postingan", true);
        this.parentFrame = parent;
        this.controller = controller;
        this.postToEdit = postToEdit;

        setSize(560, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout());

        // Header
        add(buildHeader(), BorderLayout.NORTH);

        // Body (form card)
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(C_CARD);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label Judul
        JLabel lblTitle = new JLabel("Judul Postingan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(C_TEXT);
        gbc.insets = new Insets(0, 0, 6, 0);
        card.add(lblTitle, gbc);

        // Input Judul
        txtTitle = new JTextField();
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTitle.setBackground(new Color(248, 250, 252));
        txtTitle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORD, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtTitle.setPreferredSize(new Dimension(0, 36));
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(txtTitle, gbc);

        // Label Isi
        JLabel lblContent = new JLabel("Isi Postingan");
        lblContent.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContent.setForeground(C_TEXT);
        gbc.insets = new Insets(0, 0, 6, 0);
        card.add(lblContent, gbc);

        // Input Isi (JTextArea dalam JScrollPane)
        txtContent = new JTextArea();
        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setBackground(new Color(248, 250, 252));
        txtContent.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JScrollPane scroll = new JScrollPane(txtContent);
        scroll.setBorder(BorderFactory.createLineBorder(C_BORD, 1));
        scroll.setPreferredSize(new Dimension(0, 180));
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(scroll, gbc);

        // Footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setBackground(C_CARD);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JButton btnCancel = mkOutlineBtn("Batal", C_SUB);
        JButton btnSave = mkFilledBtn("Simpan", SUCCESS, SHOVER);

        footer.add(btnCancel);
        footer.add(btnSave);

        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(footer, gbc);

        add(card, BorderLayout.CENTER);

        // Pre-fill if editing
        if (postToEdit != null) {
            txtTitle.setText(postToEdit.getTitle());
            txtContent.setText(postToEdit.getContent());
        }

        btnSave.addActionListener(e -> savePost());
        btnCancel.addActionListener(e -> dispose());
    }

    private JPanel buildHeader() {
        JPanel hdr = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setPaint(new GradientPaint(0, 0, HDR1, getWidth(), 0, HDR2));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        hdr.setPreferredSize(new Dimension(0, 56));
        hdr.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel(postToEdit == null ? "Buat Postingan Baru" : "Edit Postingan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        hdr.add(title, BorderLayout.CENTER);
        return hdr;
    }

    private void savePost() {
        String title = txtTitle.getText().trim();
        String content = txtContent.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul dan isi postingan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (postToEdit == null) {
            controller.createPost(title, content);
            JOptionPane.showMessageDialog(this, "Postingan berhasil dibuat.");
        } else {
            controller.editPost(postToEdit.getId(), title, content);
            JOptionPane.showMessageDialog(this, "Postingan berhasil diupdate.");
        }

        parentFrame.loadPosts();
        dispose();
    }

    private JButton mkOutlineBtn(String text, Color color) {
        JButton b = new JButton(text) {
            boolean ov;
            {
                setContentAreaFilled(false); setOpaque(false); setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { ov = true; repaint(); }
                    public void mouseExited (MouseEvent e) { ov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (ov) { 
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); 
                }
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
                g2.setFont(getFont()); FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(color);
        b.setPreferredSize(new Dimension(100, 34));
        return b;
    }

    private JButton mkFilledBtn(String text, Color bg, Color hover) {
        JButton b = new JButton(text) {
            boolean ov;
            {
                setContentAreaFilled(false); setBorderPainted(false);
                setOpaque(false); setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { ov = true; repaint(); }
                    public void mouseExited (MouseEvent e) { ov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ov ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setFont(getFont()); g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setPreferredSize(new Dimension(100, 34));
        return b;
    }
}
