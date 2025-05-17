public class PolynomialParser {
    public static Polynomial parse(String input) {
        Polynomial poly = new Polynomial();
        input = input.replaceAll("-", "+-"); // التعامل مع الطرح
        input = input.replaceAll("−", "+-"); // بعض لوحات المفاتيح تعطي رمز طرح مختلف
        String[] terms = input.split("\\+");

        for (String term : terms) {
            term = term.trim();
            if (term.isEmpty()) continue;

            int coeff = 0;
            int exp = 0;

            if (term.contains("X") || term.contains("x")) {
                term = term.replace("x", "X"); // توحيد الحرف إلى "X"
                if (term.equals("X")) {
                    coeff = 1;
                    exp = 1;
                } else if (term.equals("-X")) {
                    coeff = -1;
                    exp = 1;
                } else if (term.contains("^")) {
                    String[] parts = term.split("X\\^");
                    coeff = parts[0].isEmpty() ? 1 : parts[0].equals("-") ? -1 : Integer.parseInt(parts[0]);
                    exp = Integer.parseInt(parts[1]);
                } else { // شكل مثل 3X أو -5X
                    String[] parts = term.split("X");
                    coeff = parts[0].isEmpty() ? 1 : parts[0].equals("-") ? -1 : Integer.parseInt(parts[0]);
                    exp = 1;
                }
            } else {
                coeff = Integer.parseInt(term); // ثابت فقط، مثل +5
                exp = 0;
            }

            poly.addTerm(coeff, exp);
        }

        return poly;
    }
}
