package sistemforumdiskusi_19.gui;

import sistemforumdiskusi_19.controller.ForumController;
import sistemforumdiskusi_19.model.Comment;
import sistemforumdiskusi_19.model.Post;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PostDetailFrame — tampilan detail postingan beserta komentar.
 * Desain modern dengan header gradient, area konten bertema, dan tabel komentar bergaya.
 */
public class PostDetailFrame extends JFrame {

    // Palette (sama dengan MainFrame)
    private static final Color HDR1      = new Color(79,  70, 229);
    private static final Color HDR2      = new Color(124, 58, 237);
    private static final Color C_BG      = new Color(241, 245, 249);
    private static final Color C_CARD    = Color.WHITE;
    private static final Color C_TEXT    = new Color(15,  23,  42);
    private static final Color C_SUB     = new Color(100, 116, 139);
    private static final Color C_ACC     = new Color(79,  70, 229);
    private static final Color C_BORD    = new Color(226, 232, 240);
    private static final Color C_ROW_ODD = Color.WHITE;
    private static final Color C_ROW_EVN = new Color(248, 250, 252);
    private static final Color C_ROW_SEL = new Color(238, 242, 255);
    private static final Color C_SUCCESS = new Color(16, 185, 129);
    private static final Color C_DANGER  = new Color(239, 68, 68);
    private static final Color C_DHOVER  = new Color(220, 38, 38);

    private static final Color[] AV_COLORS = {
        new Color(99,102,241), new Color(236,72,153),
        new Color(245,158,11), new Color(16,185,129),
        new Color(59,130,246), new Color(239,68,68)
    };

    private final MainFrame       parentFrame;
    private final ForumController controller;
    private       Post            currentPost;

    private DefaultTableModel commentTableModel;
    private JTable            commentTable;
    private List<Comment>     currentComments;

    public PostDetailFrame(MainFrame parent, ForumController controller, Post post) {
        this.parentFrame = parent;
        this.controller  = controller;
        this.currentPost = post;

        setTitle("Detail — " + post.getTitle());
        setSize(680, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setBackground(C_BG);

        setLayout(new BorderLayout());
        add(buildPostSection(),     BorderLayout.NORTH);
        add(buildCommentSection(),  BorderLayout.CENTER);

        loadComments();
    }

    // Bagian Postingan
    private JPanel buildPostSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(C_BG);

        // Header gradient dengan judul
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

        JLabel lblTitle = new JLabel(currentPost.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);

        JButton btnBack = mkHdrBtn("< Kembali");
        btnBack.addActionListener(e -> dispose());

        hdr.add(btnBack, BorderLayout.WEST);
        hdr.add(lblTitle, BorderLayout.CENTER);

        // Kartu isi postingan
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(C_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORD),
            BorderFactory.createEmptyBorder(14, 20, 10, 20)));

        // Baris author
        JPanel authorRow = buildAuthorRow(currentPost, 0);

        // Isi konten
        JTextArea txtContent = new JTextArea(currentPost.getContent());
        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContent.setForeground(C_TEXT);
        txtContent.setEditable(false);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setOpaque(false);
        txtContent.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        JScrollPane spContent = new JScrollPane(txtContent);
        spContent.setBorder(null);
        spContent.setPreferredSize(new Dimension(0, 110));
        spContent.setOpaque(false);
        spContent.getViewport().setOpaque(false);

        card.add(authorRow,  BorderLayout.NORTH);
        card.add(spContent,  BorderLayout.CENTER);

        // Tombol Edit (hanya untuk penulis)
        if (currentPost.getAuthor().getId() == controller.getCurrentUser().getId()) {
            JPanel editBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
            editBar.setBackground(C_CARD);
            JButton btnEdit = mkOutlineBtn("Edit Postingan", C_ACC);
            btnEdit.addActionListener(e -> {
                new CreateEditPostFrame(parentFrame, controller, currentPost).setVisible(true);
                dispose();
            });
            editBar.add(btnEdit);
            card.add(editBar, BorderLayout.SOUTH);
        }

        section.add(hdr,  BorderLayout.NORTH);
        section.add(card, BorderLayout.CENTER);
        return section;
    }

    private JPanel buildAuthorRow(Post post, int idx) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        row.setBackground(C_CARD);

        Color ac = AV_COLORS[Math.abs(post.getAuthor().getUsername().hashCode()) % AV_COLORS.length];
        JPanel av = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ac);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String lt = post.getAuthor().getUsername().substring(0, 1).toUpperCase();
                g2.drawString(lt, (getWidth()-fm.stringWidth(lt))/2,
                    (getHeight()-fm.getHeight())/2 + fm.getAscent());
                g2.dispose();
            }
        };
        av.setPreferredSize(new Dimension(32, 32));
        av.setOpaque(false);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel lName  = new JLabel(post.getAuthor().getUsername());
        lName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lName.setForeground(C_TEXT);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        JLabel lTime  = new JLabel(post.getAuthor().getEmail() +
            "  |  " + post.getTimestamp().format(dtf));
        lTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lTime.setForeground(C_SUB);
        info.add(lName); info.add(lTime);

        row.add(av); row.add(info);
        return row;
    }

    // Bagian Komentar
    private JPanel buildCommentSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(C_BG);
        section.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Judul seksi
        JPanel cmtHdr = new JPanel(new BorderLayout());
        cmtHdr.setBackground(C_CARD);
        cmtHdr.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORD),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        JLabel lblCmt = new JLabel("Komentar");
        lblCmt.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCmt.setForeground(C_TEXT);

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionBar.setOpaque(false);

        if (currentPost.getAuthor().getId() == controller.getCurrentUser().getId()) {
            JButton btnDel = mkFilledBtn("Hapus Terpilih", C_DANGER, C_DHOVER);
            btnDel.addActionListener(e -> deleteSelectedComment());
            actionBar.add(btnDel);
        }

        cmtHdr.add(lblCmt,    BorderLayout.WEST);
        cmtHdr.add(actionBar, BorderLayout.EAST);

        // Tabel komentar
        String[] cols = {"Penulis", "Komentar", "Waktu"};
        commentTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        commentTable = new JTable(commentTableModel);
        styleTable(commentTable);

        JScrollPane sp = new JScrollPane(commentTable);
        sp.setBorder(null);
        sp.setBackground(C_CARD);
        sp.getViewport().setBackground(C_CARD);

        section.add(cmtHdr, BorderLayout.NORTH);
        section.add(sp,     BorderLayout.CENTER);
        section.add(buildInlineCommentPanel(), BorderLayout.SOUTH);

        return section;
    }

    private JPanel buildInlineCommentPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(C_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORD),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        JTextField txtNewComment = new JTextField();
        txtNewComment.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNewComment.setBackground(new Color(248, 250, 252));
        txtNewComment.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORD, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        
        // Placeholder text
        txtNewComment.setForeground(C_SUB);
        txtNewComment.setText("Tulis komentar Anda di sini...");
        
        txtNewComment.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtNewComment.getText().equals("Tulis komentar Anda di sini...")) {
                    txtNewComment.setText("");
                    txtNewComment.setForeground(C_TEXT);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtNewComment.getText().trim().isEmpty()) {
                    txtNewComment.setText("Tulis komentar Anda di sini...");
                    txtNewComment.setForeground(C_SUB);
                }
            }
        });

        JButton btnSend = mkFilledBtn("Kirim", C_SUCCESS, new Color(5, 150, 105));
        btnSend.setPreferredSize(new Dimension(80, 32));

        ActionListener sendAction = e -> {
            String text = txtNewComment.getText().trim();
            if (text.isEmpty() || text.equals("Tulis komentar Anda di sini...")) {
                return;
            }
            controller.addCommentToPost(currentPost.getId(), text);
            txtNewComment.setText("");
            txtNewComment.setForeground(C_SUB);
            txtNewComment.setText("Tulis komentar Anda di sini...");
            p.requestFocusInWindow();
            refreshPost();
        };

        txtNewComment.addActionListener(sendAction);
        btnSend.addActionListener(sendAction);

        p.add(txtNewComment, BorderLayout.CENTER);
        p.add(btnSend, BorderLayout.EAST);
        return p;
    }

    private void styleTable(JTable tbl) {
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tbl.setRowHeight(36);
        tbl.setShowGrid(false);
        tbl.setIntercellSpacing(new Dimension(0, 0));
        tbl.setBackground(C_CARD);
        tbl.setSelectionBackground(C_ROW_SEL);
        tbl.setSelectionForeground(C_TEXT);

        // Header
        JTableHeader th = tbl.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 11));
        th.setBackground(new Color(248, 250, 252));
        th.setForeground(C_SUB);
        th.setPreferredSize(new Dimension(0, 32));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORD));

        // Renderer baris bergantian
        tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                if (!sel) setBackground(row % 2 == 0 ? C_ROW_ODD : C_ROW_EVN);
                else      setBackground(C_ROW_SEL);
                setForeground(C_TEXT);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return this;
            }
        });

        // Lebar kolom
        tbl.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl.getColumnModel().getColumn(0).setMaxWidth(140);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(130);
        tbl.getColumnModel().getColumn(2).setMaxWidth(170);
    }

    // Operasi komentar
    private void loadComments() {
        commentTableModel.setRowCount(0);
        currentComments = currentPost.getComments();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        for (Comment c : currentComments) {
            commentTableModel.addRow(new Object[]{
                c.getAuthor().getUsername(),
                c.getContent(),
                c.getTimestamp().format(dtf)
            });
        }
    }

    /** Muat ulang post + komentar dari DB tanpa menutup frame. */
    private void refreshPost() {
        List<Post> posts = controller.getAllPosts();
        for (Post p : posts) {
            if (p.getId() == currentPost.getId()) {
                currentPost = p;
                loadComments();
                parentFrame.loadPosts();
                return;
            }
        }
    }



    private void deleteSelectedComment() {
        int row = commentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih komentar yang ingin dihapus.", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
            "Hapus komentar ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.deleteComment(currentComments.get(row).getId());
            refreshPost();
        }
    }

    // Helper tombol
    private JButton mkHdrBtn(String text) {
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
                    g2.setColor(new Color(255,255,255,35));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                }
                g2.setFont(getFont()); g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()-fm.getHeight())/2+fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(100, 36));
        return b;
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
                if (ov) { g2.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),20)); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); }
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1,1,getWidth()-3,getHeight()-3,8,8);
                g2.setFont(getFont()); FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()-fm.getHeight())/2+fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(color);
        b.setPreferredSize(new Dimension(140, 32));
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
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setFont(getFont()); g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()-fm.getHeight())/2+fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setPreferredSize(new Dimension(120, 30));
        return b;
    }
}
