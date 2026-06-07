package sistemforumdiskusi_19.gui;

import sistemforumdiskusi_19.controller.ForumController;
import sistemforumdiskusi_19.model.Post;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * MainFrame — tampilan daftar postingan berbasis kartu (card layout).
 * Menampilkan nama, email, dan isi diskusi setiap postingan.
 * Tombol Refresh dihapus; konten muat otomatis saat frame dibuka.
 */
public class MainFrame extends JFrame {

    // Palette
    private static final Color HDR1    = new Color(79,  70, 229);   
    private static final Color HDR2    = new Color(124, 58, 237);   
    private static final Color C_BG    = new Color(241, 245, 249);  
    private static final Color C_CARD  = Color.WHITE;
    private static final Color C_HOVER = new Color(238, 242, 255);  
    private static final Color C_BORD  = new Color(226, 232, 240);  
    private static final Color C_BORD_HOV = new Color(165, 180, 252);
    private static final Color C_TEXT  = new Color(15,  23,  42);
    private static final Color C_SUB   = new Color(100, 116, 139);
    private static final Color C_ACC   = new Color(79,  70, 229);
    private static final Color C_HDR_SUB = new Color(199, 210, 254); 

    // Avatar warna berdasarkan indeks post
    private static final Color[] AV_COLORS = {
        new Color(99,  102, 241),  
        new Color(236, 72,  153),  
        new Color(245, 158, 11),   
        new Color(16,  185, 129),  
        new Color(59,  130, 246),  
        new Color(239, 68,  68)    
    };

    private final ForumController controller;
    private JPanel         postContainer;
    private List<Post>     currentPosts;

    public MainFrame(ForumController controller) {
        this.controller = controller;
        setTitle("Forum Diskusi — " + controller.getCurrentUser().getUsername());
        setSize(860, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 480));

        setLayout(new BorderLayout());
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        loadPosts();
    }

    // Header
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
        hdr.setPreferredSize(new Dimension(0, 64));
        hdr.setBorder(new EmptyBorder(0, 24, 0, 20));

        // Kiri: judul
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        JLabel title = new JLabel("Forum Diskusi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Color.WHITE);
        left.add(title);

        // Kanan: info user + tombol
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JLabel userLbl = new JLabel("User: " + controller.getCurrentUser().getUsername());
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLbl.setForeground(C_HDR_SUB);
        JButton btnNew    = mkHdrBtn("+ Buat Post", Color.WHITE);
        JButton btnLogout = mkHdrBtn("Keluar",      new Color(254, 202, 202));
        right.add(userLbl); right.add(btnNew); right.add(btnLogout);

        hdr.add(left,  BorderLayout.WEST);
        hdr.add(right, BorderLayout.EAST);

        btnNew.addActionListener(e ->
            new CreateEditPostFrame(this, controller, null).setVisible(true));
        btnLogout.addActionListener(e -> {
            controller.logout();
            dispose();
        });
        return hdr;
    }

    private JButton mkHdrBtn(String text, Color fg) {
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
                if (ov) {
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.setFont(getFont()); g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setForeground(fg);
        b.setPreferredSize(new Dimension(90, 34));
        return b;
    }

    // Konten
    private JScrollPane buildContent() {
        postContainer = new JPanel(new GridBagLayout());
        postContainer.setBackground(C_BG);
        postContainer.setBorder(new EmptyBorder(24, 48, 24, 48));

        JScrollPane sp = new JScrollPane(postContainer);
        sp.setBorder(null);
        sp.getViewport().setBackground(C_BG);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        return sp;
    }

    /** Muat ulang semua postingan ke dalam card list. */
    public void loadPosts() {
        postContainer.removeAll();
        currentPosts = controller.getAllPosts();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(0, 0, 14, 0);

        if (currentPosts.isEmpty()) {
            gbc.fill   = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weighty = 1.0;
            JLabel empty = new JLabel(
                "<html><center>Belum ada postingan.<br>Jadilah yang pertama berdiskusi!</center></html>",
                SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            empty.setForeground(C_SUB);
            postContainer.add(empty, gbc);
        } else {
            for (int i = 0; i < currentPosts.size(); i++) {
                postContainer.add(new PostCard(currentPosts.get(i), i), gbc);
            }
            // Filler bawah agar kartu tidak meregang
            GridBagConstraints filler = new GridBagConstraints();
            filler.gridx  = 0; filler.gridy = GridBagConstraints.RELATIVE;
            filler.weighty = 1.0; filler.fill = GridBagConstraints.VERTICAL;
            postContainer.add(Box.createVerticalGlue(), filler);
        }
        postContainer.revalidate();
        postContainer.repaint();
    }

    // PostCard
    private class PostCard extends JPanel {

        private boolean hovered = false;
        private final Post post;

        PostCard(Post post, int idx) {
            this.post = post;
            setLayout(new BorderLayout());
            setOpaque(false);
            setPreferredSize(new Dimension(400, 178)); // lebar diatur GBL
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            Color ac = AV_COLORS[idx % AV_COLORS.length];

            // Baris Author
            JPanel authorRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
            authorRow.setOpaque(false);

            // Avatar lingkaran berisi inisial
            JPanel avatar = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(ac);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                    FontMetrics fm = g2.getFontMetrics();
                    String lt = post.getAuthor().getUsername().substring(0, 1).toUpperCase();
                    g2.drawString(lt,
                        (getWidth()  - fm.stringWidth(lt)) / 2,
                        (getHeight() - fm.getHeight())      / 2 + fm.getAscent());
                    g2.dispose();
                }
            };
            avatar.setPreferredSize(new Dimension(38, 38));
            avatar.setOpaque(false);

            // Info: nama + email
            JPanel info = new JPanel();
            info.setOpaque(false);
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            JLabel lName  = new JLabel(post.getAuthor().getUsername());
            lName.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lName.setForeground(C_TEXT);
            JLabel lEmail = new JLabel(post.getAuthor().getEmail());
            lEmail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lEmail.setForeground(C_SUB);
            info.add(lName); info.add(lEmail);

            authorRow.add(avatar); authorRow.add(info);

            // Body: judul + isi
            JPanel body = new JPanel();
            body.setOpaque(false);
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setBorder(new EmptyBorder(4, 14, 8, 14));

            JLabel lTitle = new JLabel(post.getTitle());
            lTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lTitle.setForeground(C_TEXT);
            lTitle.setAlignmentX(LEFT_ALIGNMENT);

            // Preview isi (maks 220 karakter, 2 baris)
            String raw = post.getContent();
            String preview = raw.length() > 220 ? raw.substring(0, 220) + "\u2026" : raw;
            JTextArea ta = new JTextArea(preview);
            ta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            ta.setForeground(C_SUB);
            ta.setOpaque(false);
            ta.setEditable(false);
            ta.setFocusable(false);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setRows(2);
            ta.setBorder(null);
            ta.setAlignmentX(LEFT_ALIGNMENT);

            body.add(lTitle);
            body.add(Box.createVerticalStrut(5));
            body.add(ta);

            // Footer: komentar + waktu
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
            JPanel footer = new JPanel(new BorderLayout());
            footer.setOpaque(false);
            footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORD),
                BorderFactory.createEmptyBorder(8, 14, 10, 14)));
            JLabel lCmt = new JLabel(
                post.getComments().size() + " komentar  |  klik 2x untuk buka");
            lCmt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lCmt.setForeground(C_ACC);
            JLabel lTime = new JLabel(post.getTimestamp().format(dtf));
            lTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lTime.setForeground(C_SUB);
            footer.add(lCmt,  BorderLayout.WEST);
            footer.add(lTime, BorderLayout.EAST);

            add(authorRow, BorderLayout.NORTH);
            add(body,      BorderLayout.CENTER);
            add(footer,    BorderLayout.SOUTH);

            // Hover & double-click (disebarkan ke semua child)
            MouseAdapter ma = new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    hovered = true; repaint();
                }
                @Override public void mouseExited(MouseEvent e) {
                    Point p = SwingUtilities.convertPoint(
                        (Component) e.getSource(), e.getPoint(), PostCard.this);
                    if (!PostCard.this.contains(p)) { hovered = false; repaint(); }
                }
                @Override public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2)
                        new PostDetailFrame(MainFrame.this, controller, post).setVisible(true);
                }
            };
            addDeep(this, ma);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? C_HOVER : C_CARD);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.setColor(hovered ? C_BORD_HOV : C_BORD);
            g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 14, 14);
            g2.dispose();
        }

        /** Tambahkan MouseAdapter ke komponen ini dan seluruh anak-anaknya. */
        private void addDeep(Component c, MouseAdapter ma) {
            c.addMouseListener(ma);
            if (c instanceof Container) {
                for (Component child : ((Container) c).getComponents()) addDeep(child, ma);
            }
        }
    }
}
