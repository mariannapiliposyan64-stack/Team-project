package todo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TodoFrame extends JFrame {
    private final TaskManager taskManager;
    private final DefaultTableModel tableModel;
    private final JTable taskTable;

    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JComboBox<TaskPriority> priorityComboBox;
    private final JComboBox<TaskStatus> statusComboBox;
    private final JTextField creationDateField;
    private final JTextField idField;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TodoFrame() {
        taskManager = new TaskManager();

        setTitle("To-Do List Application");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table Model
        String[] columnNames = {"ID", "Անվանում", "Նկարագրություն", "Ստեղծման Ամսաթիվ", "Առաջնահերթություն", "Կարգավիճակ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFieldsFromSelectedRow();
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(taskTable);

        // Input Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Առաջադրանքի տվյալներ"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: ID (Read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        idField = new JTextField();
        idField.setEditable(false);
        formPanel.add(idField, gbc);

        // Row 1: Title
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Անվանում:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        titleField = new JTextField();
        formPanel.add(titleField, gbc);

        // Row 2: Description
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Նկարագրություն:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // Row 3: Creation Date (Read-only)
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Ստեղծման Ամսաթիվ:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        creationDateField = new JTextField();
        creationDateField.setEditable(false);
        formPanel.add(creationDateField, gbc);

        // Row 4: Priority & Status
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Առաջնահերթություն:"), gbc);
        gbc.gridx = 1;
        priorityComboBox = new JComboBox<>(TaskPriority.values());
        formPanel.add(priorityComboBox, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Կարգավիճակ:"), gbc);
        gbc.gridx = 3;
        statusComboBox = new JComboBox<>(TaskStatus.values());
        formPanel.add(statusComboBox, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Ավելացնել");
        JButton updateButton = new JButton("Թարմացնել");
        JButton deleteButton = new JButton("Ջնջել");
        JButton clearButton = new JButton("Մաքրել");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Action Listeners
        addButton.addActionListener(e -> addTask());
        updateButton.addActionListener(e -> updateTask());
        deleteButton.addActionListener(e -> deleteTask());
        clearButton.addActionListener(e -> clearFields());

        // Top layout setup
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        refreshTable();
    }

    private void addTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        TaskPriority priority = (TaskPriority) priorityComboBox.getSelectedItem();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Խնդրում ենք մուտքագրել անվանումը!", "Սխալ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        taskManager.addTask(title, description, priority);
        refreshTable();
        clearFields();
    }

    private void updateTask() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Խնդրում ենք ընտրել առաջադրանք աղյուսակից:", "Զգուշացում", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        TaskPriority priority = (TaskPriority) priorityComboBox.getSelectedItem();
        TaskStatus status = (TaskStatus) statusComboBox.getSelectedItem();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Անվանումը չի կարող դատարկ լինել!", "Սխալ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = taskManager.updateTask(id, title, description, priority, status);
        if (updated) {
            refreshTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Առաջադրանքը չգտնվեց!", "Սխալ", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Խնդրում ենք ընտրել առաջադրանք աղյուսակից:", "Զգուշացում", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Ուզո՞ւմ եք ջնջել ընտրված առաջադրանքը:", "Հաստատում", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            taskManager.deleteTask(id);
            refreshTable();
            clearFields();
        }
    }

    private void populateFieldsFromSelectedRow() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            titleField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            descriptionArea.setText(tableModel.getValueAt(selectedRow, 2).toString());
            creationDateField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            priorityComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
            statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
        }
    }

    private void clearFields() {
        taskTable.clearSelection();
        idField.setText("");
        titleField.setText("");
        descriptionArea.setText("");
        creationDateField.setText("");
        priorityComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Task> tasks = taskManager.getAllTasks();
        for (Task task : tasks) {
            String formattedDate = task.getCreationDate() != null ? task.getCreationDate().format(DATE_FORMATTER) : "";
            tableModel.addRow(new Object[]{
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    formattedDate,
                    task.getPriority(),
                    task.getStatus()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoFrame frame = new TodoFrame();
            frame.setVisible(true);
        });
    }
}
