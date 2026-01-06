import javax.swing.*;

//merely 
public class SimpleWindow {
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Create the main window
            JFrame frame = new JFrame("Simple Window");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            
            // Center the window on screen
            frame.setLocationRelativeTo(null);
            
            // Create a label and button
            JLabel label = new JLabel("Hello! Click OK to close.", SwingConstants.CENTER);
            JButton okButton = new JButton("OK");
            
            // Set button action
            okButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(frame, "Goodbye!");
                System.exit(0);
            });
            
            // Set layout and add components
            frame.setLayout(new java.awt.BorderLayout());
            frame.add(label, java.awt.BorderLayout.CENTER);
            frame.add(okButton, java.awt.BorderLayout.SOUTH);
            
            // Make window visible
            frame.setVisible(true);
        });
    }
}