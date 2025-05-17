public class Term {
    private int coefficient;
    private int exponent;

    public Term(int coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    @Override
    public String toString() {
        int coeff = coefficient;
        int exp = exponent;

        if (coeff == 0) return "";

        StringBuilder sb = new StringBuilder();

        if (coeff == -1 && exp != 0) sb.append("-");
        else if (coeff != 1 || exp == 0) sb.append(coeff);

        if (exp != 0) {
            sb.append("X");
            if (exp != 1) sb.append("^").append(exp);
        }

        return sb.toString();
    }

}
