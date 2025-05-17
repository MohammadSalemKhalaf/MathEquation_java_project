import javax.swing.*;

public class Polynomial {
    private LinkedList terms;

    public Polynomial() {
        this.terms = new LinkedList();
    }

    public void addTerm(int coefficient, int exponent) {
        terms.addTerm(coefficient, exponent);
    }

    public LinkedList getTerms() {
        return terms;
    }

    public Polynomial add(Polynomial other) {
        Polynomial result = new Polynomial();
        Node current1 = this.terms.getHead();
        while (current1 != null) {
            result.addTerm(current1.getTerm().getCoefficient(), current1.getTerm().getExponent());
            current1 = current1.getNext();
        }
        Node current2 = other.terms.getHead();
        while (current2 != null) {
            result.addTerm(current2.getTerm().getCoefficient(), current2.getTerm().getExponent());
            current2 = current2.getNext();
        }
        return result;
    }

    public Polynomial subtract(Polynomial other) {
        Polynomial result = new Polynomial();
        Node current1 = this.terms.getHead();
        while (current1 != null) {
            result.addTerm(current1.getTerm().getCoefficient(), current1.getTerm().getExponent());
            current1 = current1.getNext();
        }
        Node current2 = other.getTerms().getHead();
        while (current2 != null) {
            result.addTerm(-current2.getTerm().getCoefficient(), current2.getTerm().getExponent());
            current2 = current2.getNext();
        }
        return result;
    }

    public Polynomial multiply(Polynomial other) {
        Polynomial result = new Polynomial();
        Node current1 = this.terms.getHead();
        while (current1 != null) {
            Node current2 = other.getTerms().getHead();
            while (current2 != null) {
                int newCoeff = current1.getTerm().getCoefficient() * current2.getTerm().getCoefficient();
                int newExp = current1.getTerm().getExponent() + current2.getTerm().getExponent();
                result.addTerm(newCoeff, newExp);
                current2 = current2.getNext();
            }
            current1 = current1.getNext();
        }
        return result;
    }

    public Object[] divide(Polynomial divisor) {
        boolean divisorIsZero = true;
        Node divisorNode = divisor.getTerms().getHead();
        while (divisorNode != null) {
            if (divisorNode.getTerm().getCoefficient() != 0) {
                divisorIsZero = false;
                break;
            }
            divisorNode = divisorNode.getNext();
        }

        if (divisorIsZero) {
            throw new ArithmeticException("Division by zero polynomial");
        }

        Polynomial remainder = new Polynomial();
        Node current = this.terms.getHead();
        while (current != null) {
            remainder.addTerm(current.getTerm().getCoefficient(), current.getTerm().getExponent());
            current = current.getNext();
        }

        Polynomial quotient = new Polynomial();

        int divisorHighestExp = -1;
        int divisorLeadCoeff = 0;
        divisorNode = divisor.getTerms().getHead();
        while (divisorNode != null) {
            if (divisorNode.getTerm().getExponent() > divisorHighestExp &&
                    divisorNode.getTerm().getCoefficient() != 0) {
                divisorHighestExp = divisorNode.getTerm().getExponent();
                divisorLeadCoeff = divisorNode.getTerm().getCoefficient();
            }
            divisorNode = divisorNode.getNext();
        }

        while (true) {
            int remainderHighestExp = -1;
            int remainderLeadCoeff = 0;
            current = remainder.getTerms().getHead();
            while (current != null) {
                if (current.getTerm().getExponent() > remainderHighestExp &&
                        current.getTerm().getCoefficient() != 0) {
                    remainderHighestExp = current.getTerm().getExponent();
                    remainderLeadCoeff = current.getTerm().getCoefficient();
                }
                current = current.getNext();
            }

            if (remainderHighestExp < divisorHighestExp || remainderLeadCoeff == 0) {
                break;
            }

            int newCoeff = remainderLeadCoeff / divisorLeadCoeff;
            int newExp = remainderHighestExp - divisorHighestExp;

            if (newCoeff == 0) break;

            quotient.addTerm(newCoeff, newExp);

            Polynomial term = new Polynomial();
            term.addTerm(newCoeff, newExp);

            Polynomial product = term.multiply(divisor);
            remainder = remainder.subtract(product);
        }

        return new Object[]{quotient, remainder};
    }

    public Polynomial divideByConstant(int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }

        Polynomial result = new Polynomial();
        Node current = this.terms.getHead();

        while (current != null) {
            int coeff = current.getTerm().getCoefficient();
            int exp = current.getTerm().getExponent();

            if (coeff % divisor != 0) {
                throw new ArithmeticException("Cannot divide all coefficients evenly by " + divisor);
            }

            result.addTerm(coeff / divisor, exp);
            current = current.getNext();
        }

        return result;
    }

    private String formatPolynomial() {
        java.util.TreeMap<Integer, Integer> map = new java.util.TreeMap<>(java.util.Collections.reverseOrder());

        Node current = terms.getHead();
        while (current != null) {
            int exp = current.getTerm().getExponent();
            int coeff = current.getTerm().getCoefficient();
            map.put(exp, map.getOrDefault(exp, 0) + coeff);
            current = current.getNext();
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int exp : map.keySet()) {
            int coeff = map.get(exp);
            if (coeff == 0) continue;

            if (!first) {
                sb.append(coeff > 0 ? " + " : " - ");
            } else if (coeff < 0) {
                sb.append("-");
            }

            int absCoeff = Math.abs(coeff);
            if (exp == 0) {
                sb.append(absCoeff);
            } else if (exp == 1) {
                sb.append(absCoeff == 1 ? "X" : absCoeff + "X");
            } else {
                sb.append(absCoeff == 1 ? "X^" + exp : absCoeff + "X^" + exp);
            }

            first = false;
        }

        return sb.length() == 0 ? "0" : sb.toString().trim();
    }

    public void printPolynomialToArea(JTextArea area, String operation) {
        area.setText("Result " + operation + ": " + formatPolynomial());
    }

    public void printPolynomialToArea(JTextArea area) {
        area.setText("Result: " + formatPolynomial());
    }

    public void printPolynomial() {
        terms.printList();
    }

    public String toInfix() {
        return formatPolynomial();
    }

    public String toPrefix() {
        java.util.Stack<String> stack = new java.util.Stack<>();
        Node current = terms.getHead();

        while (current != null) {
            Term t = current.getTerm();
            if (t.getCoefficient() != 0) {
                stack.push(t.toString());
            }
            current = current.getNext();
        }

        // أدخل العمليات قبل الحدود
        int termCount = stack.size();
        for (int i = 1; i < termCount; i++) {
            stack.push("+"); // العملية الافتراضية هي الجمع
        }

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop()).append(" ");
        }

        return sb.toString().trim();
    }


    public String toPostfix() {
        StringBuilder sb = new StringBuilder();
        Node current = terms.getHead();
        while (current != null) {
            Term t = current.getTerm();
            if (t.getCoefficient() != 0) {
                sb.append(t).append(" ");
            }
            current = current.getNext();
        }

        int termCount = countTerms();
        for (int i = 1; i < termCount; i++) {
            sb.append("+ ");
        }

        return sb.toString().trim();
    }

    private int countTerms() {
        int count = 0;
        Node current = terms.getHead();
        while (current != null) {
            if (current.getTerm().getCoefficient() != 0)
                count++;
            current = current.getNext();
        }
        return count;
    }


    public int evaluateAt(int xValue) {
        int result = 0;
        Node current = terms.getHead();
        while (current != null) {
            Term t = current.getTerm();
            int coeff = t.getCoefficient();
            int exp = t.getExponent();
            result += coeff * Math.pow(xValue, exp);
            current = current.getNext();
        }
        return result;
    }


    public double[] findRoots() {
        int a = 0, b = 0, c = 0;
        Node current = terms.getHead();
        while (current != null) {
            Term t = current.getTerm();
            if (t.getExponent() == 2) a = t.getCoefficient();
            else if (t.getExponent() == 1) b = t.getCoefficient();
            else if (t.getExponent() == 0) c = t.getCoefficient();
            current = current.getNext();
        }

        if (a == 0) {
            if (b != 0) {
                return new double[]{- (double)c / b};
            }
            return null;
        }

        double discriminant = b*b - 4*a*c;
        if (discriminant < 0) return null;
        else if (discriminant == 0) return new double[]{-b / (2.0*a)};
        else {
            double root1 = (-b + Math.sqrt(discriminant)) / (2.0*a);
            double root2 = (-b - Math.sqrt(discriminant)) / (2.0*a);
            return new double[]{root1, root2};
        }
    }


    private String formatTerm(int coeff, int exp) {
        if (exp == 0) return String.valueOf(coeff);
        if (exp == 1) return coeff == 1 ? "X" : coeff + "X";
        return coeff == 1 ? "X^" + exp : coeff + "X^" + exp;
    }
}
