import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MathSolverGUI {
    private JFrame frame;

    public MathSolverGUI() {
        showEmailPrompt();
    }

    private void showEmailPrompt() {
        frame = new JFrame("Email Validation");
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Enter your email:");
        JTextField emailField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(emailField);
        panel.add(submitButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        submitButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (EmailValidator.isValid(email)) {
                frame.dispose();
                showMainCalculator();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid email format.\nTry again (e.g., user@example.com)",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showMainCalculator() {
        JFrame calcFrame = new JFrame("Math Equation Solver");
        calcFrame.setSize(800, 600);
        calcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calcFrame.setLayout(new GridLayout(12, 1));

        JLabel poly1Label = new JLabel("Enter First Polynomial (e.g., X^2 + 2X + 1):");
        JTextField poly1Field = new JTextField();

        JLabel poly2Label = new JLabel("Enter Second Polynomial (e.g., 3X^3 + X + 10):");
        JTextField poly2Field = new JTextField();

        String[] operations = {"+", "-", "*", "/", "Value Substitution"};
        JComboBox<String> operationBox = new JComboBox<>(operations);

        JLabel formatLabel = new JLabel("Select Format:");
        String[] formats = {"Infix", "Postfix", "Prefix"};
        JComboBox<String> formatBox = new JComboBox<>(formats);

        JLabel xValueLabel = new JLabel("Substitute X = ");
        JTextField xValueField = new JTextField(10);
        xValueField.setEnabled(false);

        JButton calcButton = new JButton("Calculate");
        JButton bonusSolverButton = new JButton("Bonus: Solve Equation");

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        calcFrame.add(poly1Label);
        calcFrame.add(poly1Field);
        calcFrame.add(poly2Label);
        calcFrame.add(poly2Field);
        calcFrame.add(operationBox);
        calcFrame.add(formatLabel);
        calcFrame.add(formatBox);

        JPanel xPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        xPanel.add(xValueLabel);
        xPanel.add(xValueField);
        calcFrame.add(xPanel);

        calcFrame.add(calcButton);
        calcFrame.add(bonusSolverButton);
        calcFrame.add(new JScrollPane(resultArea));

        calcFrame.setLocationRelativeTo(null);
        calcFrame.setVisible(true);

        operationBox.addActionListener(e -> {
            String op = (String) operationBox.getSelectedItem();
            if ("Value Substitution".equals(op)) {
                poly2Field.setEnabled(false);
                formatBox.setEnabled(false);
                xValueField.setEnabled(true);
            } else {
                poly2Field.setEnabled(true);
                formatBox.setEnabled(true);
                xValueField.setEnabled(false);
                xValueField.setText("");
            }
        });

        calcButton.addActionListener(e -> {
            try {
                String op = (String) operationBox.getSelectedItem();

                if ("Value Substitution".equals(op)) {
                    String input = poly1Field.getText();
                    String xValStr = xValueField.getText().trim();
                    if (input.isEmpty() || xValStr.isEmpty()) {
                        resultArea.setText("Please enter polynomial and value for X.");
                        return;
                    }
                    Polynomial p = PolynomialParser.parse(input);
                    int xVal = Integer.parseInt(xValStr);
                    int evaluationResult = p.evaluateAt(xVal);
                    resultArea.setText("Polynomial evaluated at X = " + xVal + " is: " + evaluationResult);
                    return;
                }

                String input1 = poly1Field.getText();
                String input2 = poly2Field.getText();
                if (input1.isEmpty() || input2.isEmpty()) {
                    resultArea.setText("Please enter both polynomials.");
                    return;
                }
                Polynomial p1 = PolynomialParser.parse(input1);
                Polynomial p2 = PolynomialParser.parse(input2);

                Polynomial result;
                if (op.equals("/")) {
                    Object[] divResult = p1.divide(p2);
                    Polynomial quotient = (Polynomial) divResult[0];
                    Polynomial remainder = (Polynomial) divResult[1];

                    String selectedFormat = (String) formatBox.getSelectedItem();
                    String quotientStr, remainderStr;

                    switch (selectedFormat.toLowerCase()) {
                        case "postfix":
                            quotientStr = quotient.toPostfix();
                            remainderStr = remainder.toPostfix();
                            break;
                        case "prefix":
                            quotientStr = quotient.toPrefix();
                            remainderStr = remainder.toPrefix();
                            break;
                        default:
                            quotientStr = quotient.toInfix();
                            remainderStr = remainder.toInfix();
                            break;
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append("Quotient (").append(selectedFormat).append("): ").append(quotientStr).append("\n");
                    sb.append("Remainder (").append(selectedFormat).append("): ").append(remainderStr);

                    resultArea.setText(sb.toString());
                    return;
                }

                switch (op) {
                    case "+":
                        result = p1.add(p2);
                        break;
                    case "-":
                        result = p1.subtract(p2);
                        break;
                    case "*":
                        result = p1.multiply(p2);
                        break;
                    default:
                        resultArea.setText("Invalid operation selected.");
                        return;
                }

                String format = (String) formatBox.getSelectedItem();
                String output;
                switch (format.toLowerCase()) {
                    case "postfix":
                        output = result.toPostfix();
                        break;
                    case "prefix":
                        output = result.toPrefix();
                        break;
                    default:
                        output = result.toInfix();
                        break;
                }

                resultArea.setText("Result (" + format + "): " + output);

            } catch (Exception ex) {
                resultArea.setText("Error: " + ex.getMessage());
            }
        });

        bonusSolverButton.addActionListener(e -> showSolverWindow());
    }

    private void showSolverWindow() {
        JFrame solverFrame = new JFrame("X-Solver (Find Roots)");
        solverFrame.setSize(450, 220);
        solverFrame.setLayout(new BorderLayout());
        solverFrame.setLocationRelativeTo(frame);

        // إدخال المعادلة والزر في نفس السطر
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel label = new JLabel("Enter Polynomial:");
        JTextField polyField = new JTextField(20);
        JButton solveButton = new JButton("Find Roots");

        inputPanel.add(label);
        inputPanel.add(polyField);
        inputPanel.add(solveButton);

        // TextArea صغيرة ومغلفة بـ ScrollPane
        JTextArea resultArea = new JTextArea(5, 35);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        solverFrame.add(inputPanel, BorderLayout.NORTH);
        solverFrame.add(scrollPane, BorderLayout.CENTER);

        solverFrame.setVisible(true);

        // Action للزر
        solveButton.addActionListener(e -> {
            try {
                String input = polyField.getText().replace("=0", "").replace("= 0", "").trim();
                if (input.isEmpty()) {
                    resultArea.setText("Please enter a polynomial.");
                    return;
                }
                Polynomial poly = PolynomialParser.parse(input);
                double[] roots = poly.findRoots();
                if (roots == null || roots.length == 0) {
                    resultArea.setText("No real roots found or unsupported polynomial degree.");
                } else {
                    StringBuilder sb = new StringBuilder("Roots found:\n");
                    for (double root : roots) {
                        sb.append("X = ").append(root).append("\n");
                    }
                    resultArea.setText(sb.toString());
                }
            } catch (Exception ex) {
                resultArea.setText("Error parsing polynomial or finding roots: " + ex.getMessage());
            }
        });
    }



    public static void main(String[] args) {
        new MathSolverGUI();
    }
}
