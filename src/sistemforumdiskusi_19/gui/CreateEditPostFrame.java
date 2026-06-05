package sistemforumdiskusi_19.gui;

import sistemforumdiskusi_19.model.Post;
import sistemforumdiskusi_19.model.User;
import sistemforumdiskusi_19.service.ForumService;

import javax.swing.*;
import java.awt.*;

public class CreateEditPostFrame extends JDialog {
    private MainFrame parentFrame;
    private ForumService forumService;
    private User currentUser;
    private Post postToEdit;

    private JTextField txtTitle;
    private JTextArea txtContent;

    public CreateEditPostFrame(MainFrame parent, ForumService forumService, User currentUser, Post postToEdit) {
        super(parent, postToEdit == null ? "Buat Postingan Baru" : "Edit Postingan", true);
        this.parentFrame = parent;
        this.forumService = forumService;
        this.currentUser = currentUser;
        this.postToEdit = postToEdit;

        setSize(500, 400);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new BorderLayout(5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(new JLabel("Judul:"), BorderLayout.WEST);
        txtTitle = new JTextField();
        titlePanel.add(txtTitle, BorderLayout.CENTER);
        formPanel.add(titlePanel, BorderLayout.NORTH);

        // Content
        txtContent = new JTextArea();
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        JScrollPane scrollContent = new JScrollPane(txtContent);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JLabel("Isi Postingan:"), BorderLayout.NORTH);
        contentPanel.add(scrollContent, BorderLayout.CENTER);
        formPanel.add(contentPanel, BorderLayout.CENTER);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Simpan");
        JButton btnCancel = new JButton("Batal");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        // Pre-fill if editing
        if (postToEdit != null) {
            txtTitle.setText(postToEdit.getTitle());
            txtContent.setText(postToEdit.getContent());
        }

        btnSave.addActionListener(e -> savePost());
        btnCancel.addActionListener(e -> dispose());
    }

    private void savePost() {
        String title = txtTitle.getText().trim();
        String content = txtContent.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul dan isi postingan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (postToEdit == null) {
            forumService.createPost(title, content, currentUser);
            JOptionPane.showMessageDialog(this, "Postingan berhasil dibuat.");
        } else {
            forumService.editPost(postToEdit.getId(), title, content);
            JOptionPane.showMessageDialog(this, "Postingan berhasil diupdate.");
        }

        parentFrame.loadPosts();
        dispose();
    }
}
