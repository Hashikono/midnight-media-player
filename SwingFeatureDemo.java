import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.text.*;

/**
 * A comprehensive Java Swing application demonstrating multiple Swing features
 */
public class SwingFeatureDemo extends JFrame {
    
    // Various Swing components
    private JTabbedPane tabbedPane;
    private JTextArea statusArea;
    private DefaultListModel<String> listModel;
    private DefaultTableModel tableModel;
    private DefaultTreeModel treeModel;
    private DefaultComboBoxModel<String> comboModel;
    
    // Timer for animation
    private Timer timer;
    private int progressValue = 0;
    
    public SwingFeatureDemo() {
        // 1. JFrame setup - main application window
        setTitle("Java Swing Feature Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null); // Center the window
        
        // 2. JMenuBar - top level menu container
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        // 3. JMenu - dropdown menu container
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");
        JMenu helpMenu = new JMenu("Help");
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        
        // 4. JMenuItem - individual menu items
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem aboutItem = new JMenuItem("About");
        
        // 5. Mnemonics - keyboard shortcuts for menus
        fileMenu.setMnemonic(KeyEvent.VK_F);
        newItem.setMnemonic(KeyEvent.VK_N);
        openItem.setMnemonic(KeyEvent.VK_O);
        exitItem.setMnemonic(KeyEvent.VK_X);
        
        // 6. Accelerators - keyboard shortcuts with modifiers
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        
        // 7. Icons for menu items
        newItem.setIcon(new ImageIcon("📄"));
        openItem.setIcon(new ImageIcon("📂"));
        saveItem.setIcon(new ImageIcon("💾"));
        
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator(); // 8. JSeparator - visual divider
        fileMenu.add(exitItem);
        helpMenu.add(aboutItem);
        
        // 9. ToolBar - quick access toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // Prevent toolbar from being dragged
        
        // 10. JButton - clickable buttons
        JButton newButton = new JButton(new ImageIcon("📄"));
        newButton.setToolTipText("Create new document"); // 11. ToolTip - hover text
        JButton openButton = new JButton(new ImageIcon("📂"));
        openButton.setToolTipText("Open file");
        JButton saveButton = new JButton(new ImageIcon("💾"));
        saveButton.setToolTipText("Save file");
        
        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.addSeparator();
        
        // 12. JToggleButton - button with on/off state
        JToggleButton boldButton = new JToggleButton("B");
        boldButton.setToolTipText("Bold text");
        JToggleButton italicButton = new JToggleButton("I");
        italicButton.setToolTipText("Italic text");
        
        toolBar.add(boldButton);
        toolBar.add(italicButton);
        
        // 13. JTabbedPane - tabbed interface container
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        
        // Create tabs
        createBasicComponentsTab();
        createTextComponentsTab();
        createListTableTreeTab();
        createSliderProgressTab();
        createAdvancedComponentsTab();
        
        // 14. Status Bar using JPanel with BorderLayout
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        
        // 15. JLabel - text label component
        JLabel statusLabel = new JLabel("Status: Ready");
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        // 16. JProgressBar - progress indicator
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true); // Show percentage text
        progressBar.setValue(0);
        statusPanel.add(progressBar, BorderLayout.EAST);
        
        // 17. Text area for logging
        statusArea = new JTextArea(3, 30);
        statusArea.setEditable(false);
        JScrollPane statusScroll = new JScrollPane(statusArea);
        
        // Add components to frame
        add(toolBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        // 18. Event listeners
        setupEventListeners(exitItem, aboutItem, newButton, openButton, saveButton, 
                           boldButton, italicButton, progressBar);
        
        // 19. Timer for animation
        timer = new Timer(100, e -> {
            progressValue = (progressValue + 5) % 100;
            progressBar.setValue(progressValue);
        });
        timer.start();
        
        // 20. System Tray Icon (if supported)
        if (SystemTray.isSupported()) {
            setupSystemTray();
        }
    }
    
    private void createBasicComponentsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 21. JCheckBox - checkbox for boolean selection
        JCheckBox checkBox1 = new JCheckBox("Enable Feature A");
        JCheckBox checkBox2 = new JCheckBox("Enable Feature B", true); // Checked by default
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(checkBox1, gbc);
        gbc.gridy = 1;
        panel.add(checkBox2, gbc);
        
        // 22. JRadioButton - exclusive selection within a group
        JRadioButton radio1 = new JRadioButton("Option 1");
        JRadioButton radio2 = new JRadioButton("Option 2");
        JRadioButton radio3 = new JRadioButton("Option 3");
        
        // 23. ButtonGroup - groups radio buttons for exclusive selection
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(radio1);
        radioGroup.add(radio2);
        radioGroup.add(radio3);
        radio1.setSelected(true);
        
        gbc.gridy = 2;
        panel.add(radio1, gbc);
        gbc.gridy = 3;
        panel.add(radio2, gbc);
        gbc.gridy = 4;
        panel.add(radio3, gbc);
        
        // 24. JComboBox - dropdown selection list
        comboModel = new DefaultComboBoxModel<>();
        comboModel.addElement("Select a color");
        comboModel.addElement("Red");
        comboModel.addElement("Green");
        comboModel.addElement("Blue");
        comboModel.addElement("Yellow");
        JComboBox<String> comboBox = new JComboBox<>(comboModel);
        gbc.gridy = 5;
        panel.add(comboBox, gbc);
        
        // 25. JTextField - single line text input
        JTextField textField = new JTextField(15);
        textField.setToolTipText("Enter your name");
        gbc.gridy = 6;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(textField, gbc);
        
        // 26. JPasswordField - password input with masking
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        // 27. JSpinner - number selector with increment/decrement buttons
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 0, 100, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        panel.add(spinner, gbc);
        
        // Add tab with icon
        tabbedPane.addTab("Basic", new ImageIcon("⚙️"), panel, "Basic Swing Components");
    }
    
    private void createTextComponentsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 28. JTextArea - multi-line text area
        JTextArea textArea = new JTextArea(10, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // 29. JScrollPane - adds scrollbars to components
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // 30. Border - visual border around components
        TitledBorder titleBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Text Editor"
        );
        scrollPane.setBorder(titleBorder);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // 31. JPanel with FlowLayout for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        // 32. JButton with different states
        JButton clearButton = new JButton("Clear");
        JButton copyButton = new JButton("Copy");
        JButton pasteButton = new JButton("Paste");
        
        // 33. Setting button states
        pasteButton.setEnabled(false); // Disabled button
        
        buttonPanel.add(clearButton);
        buttonPanel.add(copyButton);
        buttonPanel.add(pasteButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Text", panel);
    }
    
    private void createListTableTreeTab() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        
        // 34. JList - list of selectable items
        listModel = new DefaultListModel<>();
        listModel.addElement("Apple");
        listModel.addElement("Banana");
        listModel.addElement("Cherry");
        listModel.addElement("Date");
        listModel.addElement("Elderberry");
        
        JList<String> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // 35. Selection modes
        JScrollPane listScroll = new JScrollPane(list);
        listScroll.setBorder(BorderFactory.createTitledBorder("JList Example"));
        
        // 36. JTable - tabular data display
        String[] columnNames = {"ID", "Name", "Price", "In Stock"};
        Object[][] data = {
            {1, "Laptop", 999.99, true},
            {2, "Mouse", 25.50, true},
            {3, "Keyboard", 45.00, false},
            {4, "Monitor", 299.99, true}
        };
        
        tableModel = new DefaultTableModel(data, columnNames) {
            // 37. Custom cell rendering by overriding getColumnClass
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setMaxWidth(50); // 38. Column sizing
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 39. JTable header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setReorderingAllowed(false); // Prevent column reordering
        
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("JTable Example"));
        
        // 40. JTree - hierarchical tree structure
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Company");
        
        DefaultMutableTreeNode sales = new DefaultMutableTreeNode("Sales");
        sales.add(new DefaultMutableTreeNode("North Region"));
        sales.add(new DefaultMutableTreeNode("South Region"));
        
        DefaultMutableTreeNode engineering = new DefaultMutableTreeNode("Engineering");
        engineering.add(new DefaultMutableTreeNode("Development"));
        engineering.add(new DefaultMutableTreeNode("QA"));
        
        root.add(sales);
        root.add(engineering);
        
        treeModel = new DefaultTreeModel(root);
        JTree tree = new JTree(treeModel);
        tree.setRootVisible(true);
        
        // 41. TreeSelectionListener for tree selection events
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) 
                tree.getLastSelectedPathComponent();
            if (node != null) {
                logStatus("Tree selected: " + node.getUserObject());
            }
        });
        
        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setBorder(BorderFactory.createTitledBorder("JTree Example"));
        
        panel.add(listScroll);
        panel.add(tableScroll);
        panel.add(treeScroll);
        
        tabbedPane.addTab("Data", panel);
    }
    
    private void createSliderProgressTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 42. JSlider - adjustable slider component
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(false);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(slider, gbc);
        
        // 43. JProgressBar - indeterminate mode
        JProgressBar indeterminateProgress = new JProgressBar();
        indeterminateProgress.setIndeterminate(true);
        indeterminateProgress.setString("Processing...");
        indeterminateProgress.setStringPainted(true);
        
        gbc.gridy = 1;
        panel.add(indeterminateProgress, gbc);
        
        // 44. JProgressBar - determinate mode
        JProgressBar determinateProgress = new JProgressBar(0, 100);
        determinateProgress.setValue(75);
        determinateProgress.setString("75% Complete");
        determinateProgress.setStringPainted(true);
        
        gbc.gridy = 2;
        panel.add(determinateProgress, gbc);
        
        // 45. JFormattedTextField - formatted text input
        JFormattedTextField formattedField;
        try {
            formattedField = new JFormattedTextField(new DecimalFormat("#,##0.00"));
            formattedField.setValue(1234.56);
            formattedField.setColumns(15);
            
            gbc.gridwidth = 1;
            gbc.gridy = 3; gbc.gridx = 0;
            panel.add(new JLabel("Formatted Number:"), gbc);
            gbc.gridx = 1;
            panel.add(formattedField, gbc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        tabbedPane.addTab("Sliders & Progress", panel);
    }
    
    private void createAdvancedComponentsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 46. JColorChooser - color selection dialog component
        JButton colorButton = new JButton("Choose Color");
        JPanel colorPreview = new JPanel();
        colorPreview.setBackground(Color.RED);
        colorPreview.setPreferredSize(new Dimension(50, 30));
        colorPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(colorButton, gbc);
        gbc.gridx = 1;
        panel.add(colorPreview, gbc);
        
        // 47. JFileChooser - file selection dialog
        JButton fileButton = new JButton("Select File");
        JLabel fileLabel = new JLabel("No file selected");
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(fileButton, gbc);
        gbc.gridx = 1;
        panel.add(fileLabel, gbc);
        
        // 48. JOptionPane - pre-built dialog boxes
        JButton messageButton = new JButton("Show Message Dialog");
        JButton confirmButton = new JButton("Show Confirm Dialog");
        JButton inputButton = new JButton("Show Input Dialog");
        
        gbc.gridwidth = 2;
        gbc.gridy = 2;
        panel.add(messageButton, gbc);
        gbc.gridy = 3;
        panel.add(confirmButton, gbc);
        gbc.gridy = 4;
        panel.add(inputButton, gbc);
        
        // 49. JDesktopPane and JInternalFrame - MDI interface
        JButton internalFrameButton = new JButton("Create Internal Frame");
        gbc.gridy = 5;
        panel.add(internalFrameButton, gbc);
        
        // Add action listeners
        colorButton.addActionListener(e -> {
            Color chosenColor = JColorChooser.showDialog(
                panel, "Choose a color", colorPreview.getBackground()
            );
            if (chosenColor != null) {
                colorPreview.setBackground(chosenColor);
            }
        });
        
        fileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileLabel.setText(fileChooser.getSelectedFile().getName());
            }
        });
        
        messageButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, 
                "This is an information message",
                "Message Dialog",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        confirmButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(panel,
                "Are you sure you want to continue?",
                "Confirmation Dialog",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            logStatus("Confirm dialog result: " + result);
        });
        
        inputButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(panel,
                "Enter your name:",
                "Input Dialog",
                JOptionPane.QUESTION_MESSAGE);
            
            if (input != null && !input.trim().isEmpty()) {
                logStatus("User entered: " + input);
            }
        });
        
        internalFrameButton.addActionListener(e -> {
            createInternalFrame();
        });
        
        tabbedPane.addTab("Advanced", panel);
    }
    
    private void createInternalFrame() {
        // 50. JInternalFrame - window-like container inside main window
        JInternalFrame internalFrame = new JInternalFrame(
            "Internal Frame",  // title
            true,              // resizable
            true,              // closable
            true,              // maximizable
            true               // iconifiable
        );
        
        internalFrame.setSize(300, 200);
        internalFrame.setLocation(
            (int)(Math.random() * (getWidth() - 300)),
            (int)(Math.random() * (getHeight() - 200))
        );
        
        internalFrame.add(new JLabel("This is an internal frame"));
        internalFrame.setVisible(true);
        
        // Add to a JDesktopPane or directly to content pane
        getContentPane().add(internalFrame);
        internalFrame.moveToFront();
    }
    
    private void setupEventListeners(JMenuItem exitItem, JMenuItem aboutItem,
                                    JButton newButton, JButton openButton, JButton saveButton,
                                    JToggleButton boldButton, JToggleButton italicButton,
                                    JProgressBar progressBar) {
        
        // 51. ActionListener - handles button clicks and menu selections
        exitItem.addActionListener(e -> System.exit(0));
        
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Java Swing Feature Demo\nDemonstrating 50+ Swing features",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        newButton.addActionListener(e -> logStatus("New button clicked"));
        openButton.addActionListener(e -> logStatus("Open button clicked"));
        saveButton.addActionListener(e -> logStatus("Save button clicked"));
        
        // 52. ChangeListener - handles state changes
        boldButton.addChangeListener(e -> {
            if (boldButton.isSelected()) {
                logStatus("Bold enabled");
            } else {
                logStatus("Bold disabled");
            }
        });
        
        italicButton.addChangeListener(e -> {
            if (italicButton.isSelected()) {
                logStatus("Italic enabled");
            } else {
                logStatus("Italic disabled");
            }
        });
        
        // 53. MouseListener - handles mouse events
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    progressBar.setValue(0);
                    logStatus("Progress bar reset");
                }
            }
        });
        
        // 54. KeyListener - handles keyboard events
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    logStatus("F1 Help key pressed");
                }
            }
        });
        setFocusable(true); // Required to receive key events
    }
    
    private void setupSystemTray() {
        try {
            // 55. SystemTray - system tray integration
            SystemTray tray = SystemTray.getSystemTray();
            
            // 56. TrayIcon - icon in system tray
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "Swing Demo");
            trayIcon.setImageAutoSize(true);
            
            // 57. PopupMenu for tray icon
            PopupMenu popup = new PopupMenu();
            MenuItem openItem = new MenuItem("Open");
            MenuItem exitItem = new MenuItem("Exit");
            
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            
            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
            
            trayIcon.addActionListener(e -> {
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
            });
            
            exitItem.addActionListener(e -> System.exit(0));
            
        } catch (Exception e) {
            logStatus("System tray not supported: " + e.getMessage());
        }
    }
    
    private void logStatus(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        statusArea.append("[" + timestamp + "] " + message + "\n");
        statusArea.setCaretPosition(statusArea.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        // 58. SwingUtilities.invokeLater - ensures thread safety for GUI updates
        SwingUtilities.invokeLater(() -> {
            try {
                // 59. UIManager - for look and feel customization
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // 60. Create and display the application window
                SwingFeatureDemo demo = new SwingFeatureDemo();
                demo.setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}