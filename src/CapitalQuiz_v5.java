import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class CapitalQuiz_v5 {
	private static int correctAnswers = 0;
    private static int wrongAnswers = 0;
    private static int currentQuestionIndex = 0;
    private static Timer questionTimer;
    private static List<String> filenames = new ArrayList<>(Arrays.asList(
            "srilanka1.jpg", "srilanka2.jpeg", "srilanka3.jpeg", "srilanka4.jpeg", 
            "srilanka5.jpeg", "srilanka6.jpg", "srilanka7.jpeg", "srilanka8.jpg", 
            "srilanka9.jpg", "srilanka10.jpg", "srilanka11.jpeg", "srilanka12.jpg", 
            "srilanka13.jpg", "srilanka14.jpg", "srilanka15.jpg"
        ));
    private static String imagePath;
    private static JPanel overlayPanel;
    private static BufferedImage pixelatedImage;
    private static BufferedImage originalImage;
    private static int imageWidth;
    private static int imageHeight;
    private static int screenWidth;
    private static int screenHeight;
    private static double scale;
    private static BufferedImage scaledImage;
    private static int scaledWidth;
    private static int scaledHeight;
    private static JLabel backgroundLabel = new JLabel();
    private static JLayeredPane layeredPane;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true); // Remove window decorations
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set frame to fullscreen

            
            // Create a layered pane for layering components
            layeredPane = new JLayeredPane();
            layeredPane.setLayout(null);  // Use null layout to manually set bounds

            BufferedImage imageImp=GetImage();
            // Create a label to hold the scaled image
             backgroundLabel = new JLabel(new ImageIcon(imageImp));
            
            // Add background label to the layered pane
            backgroundLabel.setBounds(0, 0, scaledWidth, scaledHeight);
            layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

         // Create a panel to hold the buttons and labels (set up FlowLayout for center alignment)
            overlayPanel = new JPanel();
            
            // Add overlay panel to the layered pane on top of the background
            overlayPanel.setBounds(0, 0, scaledWidth, scaledHeight);
            layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);

            // Add layered pane to the frame (no BorderLayout needed)
            frame.add(layeredPane);

            

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            
            overlayPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Using FlowLayout for center alignment
            overlayPanel.setOpaque(false);  // Makes the panel transparent

            JPanel questionField = new JPanel(new BorderLayout()); // Using BorderLayout for question field
            JLabel questionText = new JLabel();
            questionText.setHorizontalAlignment(SwingConstants.CENTER); // Center aligning question text

            questionField.setPreferredSize(new Dimension(1050, 300));
            questionField.add(questionText, BorderLayout.CENTER);
            questionField.setOpaque(false);

            overlayPanel.add(questionField);

            // Buttons for options
            gbc.gridy = 1;
            JButton optionA = new JButton();
            JButton optionB = new JButton();
            JButton optionC = new JButton();
            JButton optionD = new JButton();

            // Set buttons' sizes to match those in CapitalQuiz_v3
            optionA.setPreferredSize(new Dimension(400, 120)); // Adjust size as needed
            optionB.setPreferredSize(new Dimension(400, 120));
            optionC.setPreferredSize(new Dimension(400, 120));
            optionD.setPreferredSize(new Dimension(400, 120));

            // Set button styles to match those in CapitalQuiz_v3
            optionA.setBackground(Color.WHITE);
            optionB.setBackground(Color.WHITE);
            optionC.setBackground(Color.WHITE);
            optionD.setBackground(Color.WHITE);

            optionA.setFont(new Font("Arial", Font.BOLD, 35));
            optionB.setFont(new Font("Arial", Font.BOLD, 35));
            optionC.setFont(new Font("Arial", Font.BOLD, 35));
            optionD.setFont(new Font("Arial", Font.BOLD, 35));

            optionA.setHorizontalAlignment(SwingConstants.LEFT);
            optionB.setHorizontalAlignment(SwingConstants.LEFT);
            optionC.setHorizontalAlignment(SwingConstants.LEFT);
            optionD.setHorizontalAlignment(SwingConstants.LEFT);

            // Add the buttons to the overlay panel
            overlayPanel.add(optionA);
            gbc.gridy = 2;
            overlayPanel.add(optionB);
            gbc.gridy = 3;
            overlayPanel.add(optionC);
            gbc.gridy = 4;
            overlayPanel.add(optionD);

            File file = new File("C:/Users/DELL/Desktop/Aqthas/states and capitals of Sri lanka.txt");
            if (!file.exists()) {
                System.out.println("File doesn't exist.");
                return;
            }

            List<String> states = new ArrayList<>();
            HashMap<String, String> stateCapitalMap = new HashMap<>();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" - ");
                    if (parts.length == 2) {
                        String state = parts[0].trim();
                        String capital = parts[1].trim();
                        states.add(state);
                        stateCapitalMap.put(state, capital);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Collections.shuffle(states);
            Random random = new Random();
            displayNextQuestion(frame, questionText, optionA, optionB, optionC, optionD, states, stateCapitalMap, random);

          

            // Determine whether to display black bars
            boolean displayBlackBars = true;

            if (displayBlackBars) {
                // Create black bar panels
                JPanel leftBlackBar = new JPanel();
                leftBlackBar.setBackground(Color.BLACK);
                leftBlackBar.setPreferredSize(new Dimension(200, screenHeight));

                JPanel rightBlackBar = new JPanel();
                rightBlackBar.setBackground(Color.BLACK);
                rightBlackBar.setPreferredSize(new Dimension(200, screenHeight));

                frame.add(leftBlackBar, BorderLayout.WEST);
                frame.add(rightBlackBar, BorderLayout.EAST);
            }

            // Set frame visible after adding components
            frame.setVisible(true);
        });
    }
    
    private static BufferedImage GetImage() {
        // Load the image
        originalImage = null;
        Collections.shuffle(filenames);
        imagePath = filenames.get(0);
        try {
            // Check if the file is .jpg; if not, convert to .jpg
            originalImage = loadImageWithExtensionCheck(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check and resize the image if needed
        if (originalImage != null) {
            imageWidth = originalImage.getWidth();
            imageHeight = originalImage.getHeight();

            // If the dimensions are not 1024x768, resize the image
            if (imageWidth != 1024 || imageHeight != 768) {
                originalImage = resizeImage(originalImage, 1024, 768);
            }
        }

        // Pixelate the image
        pixelatedImage = pixelateImage(originalImage, 3); // Adjust pixel size as needed

        // Get screen resolution
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;

        // Determine the initial scale factor
        scale = 1.0;

        // Now, calculate the scaled width and height
         scaledWidth = (int) (pixelatedImage.getWidth() * scale);
         scaledHeight = (int) (pixelatedImage.getHeight() * scale);

        // Keep adjusting the scale factor until the image fills the center frame
        while (scaledWidth < screenWidth && scaledHeight < screenHeight) {
            scale += 0.01; // Increment the scale factor
            scaledWidth = (int) (pixelatedImage.getWidth() * scale);
            scaledHeight = (int) (pixelatedImage.getHeight() * scale);
        }

        // Create a scaled image with transparency
        scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.36f)); // Change the alpha value as needed (0.0f to 1.0f)
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(pixelatedImage, 0, 0, scaledWidth, scaledHeight, null);
        graphics2D.dispose();

        return scaledImage;
    }


    // Function to load image and check its extension
    private static BufferedImage loadImageWithExtensionCheck(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        String fileName = imageFile.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // If the file is not a .jpg file, convert it
        if (!fileExtension.equals("jpg")) {
            BufferedImage originalImage = ImageIO.read(imageFile);
            File jpgFile = new File(imagePath.substring(0, imagePath.lastIndexOf(".")) + ".jpg");
            ImageIO.write(originalImage, "jpg", jpgFile);
            return originalImage;
        }
        return ImageIO.read(imageFile);
    }

    // Function to pixelate the image
    private static BufferedImage pixelateImage(BufferedImage originalImage, int pixelSize) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Create a new buffered image to store the pixelated version
        BufferedImage pixelatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = pixelatedImage.createGraphics();

        // Pixelate the image
        for (int y = 0; y < height; y += pixelSize) {
            for (int x = 0; x < width; x += pixelSize) {
                int w = Math.min(pixelSize, width - x);
                int h = Math.min(pixelSize, height - y);
                int pixel = originalImage.getRGB(x, y);
                g2d.setColor(new java.awt.Color(pixel));
                g2d.fillRect(x, y, w, h);
            }
        }
        g2d.dispose();

        return pixelatedImage;
    }

    // Resize method
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedScaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedScaledImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return bufferedScaledImage;
    }

    private static void displayNextQuestion(JFrame frame, JLabel questionText, JButton optionA, JButton optionB, JButton optionC, JButton optionD, List<String> states, HashMap<String, String> stateCapitalMap, Random random) {
        if (currentQuestionIndex < states.size()) {
            String state = states.get(currentQuestionIndex);
            String capital = stateCapitalMap.get(state);

            List<String> options = new ArrayList<>(stateCapitalMap.values());
            options.remove(capital);
            Collections.shuffle(options);
            options = options.subList(0, 3);
            options.add(capital);
            Collections.shuffle(options);

            questionText.setText("Q" + (currentQuestionIndex + 1) + ") What is the capital of " + state + "?");
            questionText.setFont(new Font("Arial", Font.BOLD, 40)); // Adjust font size as needed

            optionA.setText("A. " + options.get(0));
            optionB.setText("B. " + options.get(1));
            optionC.setText("C. " + options.get(2));
            optionD.setText("D. " + options.get(3));

            String correctCapital = capital;

            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    optionA.removeActionListener(this);
                    optionB.removeActionListener(this);
                    optionC.removeActionListener(this);
                    optionD.removeActionListener(this);

                    if (questionTimer != null && questionTimer.isRunning()) {
                        questionTimer.stop();
                    }

                    String selectedCapital = ((JButton) e.getSource()).getText().substring(3);
                    checkAnswer(selectedCapital, correctCapital);
                    disableAllButtons(optionA, optionB, optionC, optionD);
                    highlightCorrectAnswer(optionA, optionB, optionC, optionD, correctCapital, selectedCapital);

                    questionTimer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            optionA.setBackground(Color.WHITE);
                            optionB.setBackground(Color.WHITE);
                            optionC.setBackground(Color.WHITE);
                            optionD.setBackground(Color.WHITE);
                            questionText.setText("");
                            enableAllButtons(optionA, optionB, optionC, optionD);
                            displayNextQuestion(frame, questionText, optionA, optionB, optionC, optionD, states, stateCapitalMap, random);
                            layeredPane.remove(backgroundLabel);  // Remove the old background label
                            BufferedImage imageImp=GetImage();
                            // Create a label to hold the scaled image
                             backgroundLabel = new JLabel(new ImageIcon(imageImp));
                            
                            // Add background label to the layered pane
                            backgroundLabel.setBounds(0, 0, scaledWidth, scaledHeight);
                            layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
                            
                        }
                    });
                    questionTimer.setRepeats(false);
                    questionTimer.start();
                }
            };

            optionA.addActionListener(listener);
            optionB.addActionListener(listener);
            optionC.addActionListener(listener);
            optionD.addActionListener(listener);

            currentQuestionIndex++;
        } else {
            JOptionPane.showMessageDialog(frame, "Quiz finished! You scored " + correctAnswers + " correct answers and " + wrongAnswers + " wrong answers");
            frame.dispose();
        }
    }

    private static void checkAnswer(String selectedCapital, String correctCapital) {
        if (selectedCapital.equals(correctCapital)) {
            System.out.println("Correct!");
            correctAnswers++;
        } else {
            System.out.println("Incorrect!");
            wrongAnswers++;
        }
    }

    private static void disableAllButtons(JButton optionA, JButton optionB, JButton optionC, JButton optionD) {
        optionA.setEnabled(false);
        optionB.setEnabled(false);
        optionC.setEnabled(false);
        optionD.setEnabled(false);
    }

    private static void enableAllButtons(JButton optionA, JButton optionB, JButton optionC, JButton optionD) {
        optionA.setEnabled(true);
        optionB.setEnabled(true);
        optionC.setEnabled(true);
        optionD.setEnabled(true);
    }

    private static void highlightCorrectAnswer(JButton optionA, JButton optionB, JButton optionC, JButton optionD, String correctCapital, String selectedCapital) {
        optionA.setBackground(optionA.getText().substring(3).equals(correctCapital) ? Color.GREEN : (optionA.getText().substring(3).equals(selectedCapital) ? Color.RED : Color.WHITE));
        optionB.setBackground(optionB.getText().substring(3).equals(correctCapital) ? Color.GREEN : (optionB.getText().substring(3).equals(selectedCapital) ? Color.RED : Color.WHITE));
        optionC.setBackground(optionC.getText().substring(3).equals(correctCapital) ? Color.GREEN : (optionC.getText().substring(3).equals(selectedCapital) ? Color.RED : Color.WHITE));
        optionD.setBackground(optionD.getText().substring(3).equals(correctCapital) ? Color.GREEN : (optionD.getText().substring(3).equals(selectedCapital) ? Color.RED : Color.WHITE));
    }
}