package sistemforumdiskusi_19.gui;

import sistemforumdiskusi_19.model.Comment;
import sistemforumdiskusi_19.model.Post;
import sistemforumdiskusi_19.model.User;
import sistemforumdiskusi_19.service.ForumService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PostDetailFrame extends JFrame {
    private MainFrame parentFrame;
    private ForumService forumService;
    private User currentUser;
    private Post currentPost;

    private JLabel lblTitle;
    private JTextArea txtContent;
    private JTable commentTable;
    private DefaultTableModel commentTableModel;
    private List<Comment> currentComments;

    public PostDetailFrame(MainFrame parent, ForumService forumService, User currentUser, Post post) {
        this.parentFrame = parent;
        this.forumService = forumService;
        this.currentUser = currentUser;
        this.currentPost = post;

        setTitle("Detail Postingan");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        initComponents();
        loadComments();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Post Detail Panel
        JPanel postPanel = new JPanel(new BorderLayout(5, 5));
        postPanel.setBorder(BorderFactory.createTitledBorder("Postingan"));
        
        lblTitle = new JLabel("Judul: " + currentPost.getTitle());
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        txtContent = new JTextArea(currentPost.getContent());
        txtContent.setEditable(false);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        
        postPanel.add(lblTitle, BorderLayout.NORTH);
        postPanel.add(new JScrollPane(txtContent), BorderLayout.CENTER);

        // Edit Post Button (only for author)
        if (currentPost.getAuthor().getId() == currentUser.getId()) {
            JButton btnEditPost = new JButton("Edit Post");
            btnEditPost.addActionListener(e -> {
                new CreateEditPostFrame(parentFrame, forumService, currentUser, currentPost).setVisible(true);
                this.dispose(); // Close detail, User can double-click again to see changes
            });
            JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            editPanel.add(btnEditPost);
            postPanel.add(editPanel, BorderLayout.SOUTH);
        }

        add(postPanel, BorderLayout.NORTH);

        // Comments Panel
        JPanel commentsPanel = new JPanel(new BorderLayout(5, 5));
        commentsPanel.setBorder(BorderFactory.createTitledBorder("Komentar"));

        String[] columns = {"ID", "Penulis", "Komentar", "Waktu"};
        commentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        commentTable = new JTable(commentTableModel);
        commentsPanel.add(new JScrollPane(commentTable), BorderLayout.CENTER);

        // Comment Actions
        JPanel commentActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAddComment = new JButton("Tambah Komentar");
        commentActionPanel.add(btnAddComment);

        // Delete Comment Button (only if author of the post)
        if (currentPost.getAuthor().getId() == currentUser.getId()) {
            JButton btnDeleteComment = new JButton("Hapus Komentar Terpilih");
            btnDeleteComment.addActionListener(e -> deleteSelectedComment());
            commentActionPanel.add(btnDeleteComment);
        }

        commentsPanel.add(commentActionPanel, BorderLayout.SOUTH);
        add(commentsPanel, BorderLayout.CENTER);

        // Listeners
        btnAddComment.addActionListener(e -> addComment());
    }

    private void loadComments() {
        commentTableModel.setRowCount(0);
        currentComments = currentPost.getComments();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        
        for (Comment comment : currentComments) {
            Object[] rowData = {
                comment.getId(),
                comment.getAuthor().getUsername(),
                comment.getContent(),
                comment.getTimestamp().format(dtf)
            };
            commentTableModel.addRow(rowData);
        }
    }

    private void addComment() {
        String commentStr = JOptionPane.showInputDialog(this, "Masukkan Komentar:");
        if (commentStr != null && !commentStr.trim().isEmpty()) {
            forumService.addCommentToPost(currentPost.getId(), commentStr.trim(), currentUser);
            
            // Reload the post to get the new comments from DB
            parentFrame.loadPosts(); 
            // In a real app we'd fetch just this post again, but we can just reload all
            
            // Quick workaround: close and reopen or just fetch again
            this.dispose();
            JOptionPane.showMessageDialog(parentFrame, "Komentar berhasil ditambahkan.");
        }
    }

    private void deleteSelectedComment() {
        int row = commentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih komentar yang ingin dihapus terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus komentar ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Comment selectedComment = currentComments.get(row);
            forumService.deleteComment(selectedComment.getId());
            
            // Reload
            parentFrame.loadPosts();
            this.dispose();
            JOptionPane.showMessageDialog(parentFrame, "Komentar berhasil dihapus.");
        }
    }
}
