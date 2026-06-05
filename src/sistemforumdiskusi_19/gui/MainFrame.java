package sistemforumdiskusi_19.gui;

import sistemforumdiskusi_19.model.Post;
import sistemforumdiskusi_19.model.User;
import sistemforumdiskusi_19.service.ForumService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {
    private ForumService forumService;
    private User currentUser;
    private JTable postTable;
    private DefaultTableModel tableModel;
    private List<Post> currentPosts;

    public MainFrame(ForumService forumService, User currentUser) {
        this.forumService = forumService;
        this.currentUser = currentUser;

        setTitle("Menu Forum - Selamat Datang, " + currentUser.getUsername());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadPosts();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCreatePost = new JButton("Buat Postingan Baru");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnLogout = new JButton("Logout");

        topPanel.add(btnCreatePost);
        topPanel.add(btnRefresh);
        topPanel.add(btnLogout);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Table
        String[] columns = {"ID", "Judul", "Penulis", "Waktu", "Jml Komentar"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };
        postTable = new JTable(tableModel);
        
        postTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) { // double click
                    int row = postTable.getSelectedRow();
                    if (row != -1) {
                        Post selectedPost = currentPosts.get(row);
                        openPostDetail(selectedPost);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(postTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Klik ganda (double-click) pada baris untuk melihat / komentar postingan"));
        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        btnCreatePost.addActionListener(e -> {
            new CreateEditPostFrame(this, forumService, currentUser, null).setVisible(true);
        });

        btnRefresh.addActionListener(e -> loadPosts());

        btnLogout.addActionListener(e -> {
            new LoginFrame(forumService).setVisible(true);
            this.dispose();
        });
    }

    public void loadPosts() {
        tableModel.setRowCount(0); // clear table
        currentPosts = forumService.getPosts();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        
        for (Post post : currentPosts) {
            Object[] rowData = {
                post.getId(),
                post.getTitle(),
                post.getAuthor().getUsername(),
                post.getTimestamp().format(dtf),
                post.getComments().size()
            };
            tableModel.addRow(rowData);
        }
    }

    private void openPostDetail(Post post) {
        new PostDetailFrame(this, forumService, currentUser, post).setVisible(true);
    }
}
