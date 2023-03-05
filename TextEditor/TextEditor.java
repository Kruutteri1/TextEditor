package TextEditor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener {


    // main element of program
    JTextArea textArea;
    JScrollPane scrollPane;
    JSpinner fontSizeSpinner; // change fontSize
    JLabel fontLabel; // Text near JSpinner
    JButton fontColorButton;
    JComboBox fontBox;

    // ------menuBar------
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;


    // constructor
    TextEditor() {
        //main window settings
        this.setTitle("Text Editor");
        this.setSize(500, 500);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        textArea = new JTextArea();
        //textArea.setPreferredSize(new Dimension(450,450));
        textArea.setLineWrap(true); // switch to next line when line ends
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20)); // new font and size for text

        //create a scrollLine panel
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        fontLabel = new JLabel("Font: "); //Text near JSpinner

        fontSizeSpinner = new JSpinner(); //create a chose spinner font size
        fontSizeSpinner.setPreferredSize(new Dimension(50,25)); //position
        fontSizeSpinner.setValue(20); // default size on the start program
        fontSizeSpinner.addChangeListener(new ChangeListener() { // change font size on click +-
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
            }
        });

        fontColorButton = new JButton("Color");
        fontColorButton.setFocusable(false);
        fontColorButton.addActionListener(this);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); //add all fonts to massive

        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial"); //default font on the button


        // -------- MenuBar---------:
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);


        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        // -------------------------:


        // add elements to main frame
        this.setJMenuBar(menuBar); //add menuBar
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() ==  fontColorButton) {
            JColorChooser colorChooser = new JColorChooser();

            Color color = colorChooser.showDialog(null, "Choose a color", Color.black); //open a window colorChooser

            textArea.setForeground(color); // change a text color
        }
        if (e.getSource() == fontBox) { // if click
            textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize())); //change Font
        }
        if (e.getSource() == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt"); //add filter to only txt files
            fileChooser.setFileFilter(filter); // add to fileChooser a filter

            int response = fileChooser.showOpenDialog(null); // open window changer files

            /**
             if user choose file and click open button method showOpenDialog() return value JFileChooser.APPROVE_OPTION
             and the code to continue executing
             **/

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try (Scanner fileIn = new Scanner(file)) {
                    if (file.isFile()) {
                        while (fileIn.hasNextLine()) {
                            String line = fileIn.nextLine() + "\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

            }
        }
        if (e.getSource() == saveItem) { // save file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));  // currentCurrentDirectory

            int response = fileChooser.showSaveDialog(null); // open save window chooser

            if (response == JFileChooser.APPROVE_OPTION) { // user choose where save file
                File file;
                PrintWriter fileOut = null; // writes the contents of the JTextArea to the selected file using the println method.

                file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".txt"); // at last add format (.txt)
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                finally {
                    fileOut.close();
                }
            }
        }
        if (e.getSource() == exitItem) {
            System.exit(0);
        }
    }
}
