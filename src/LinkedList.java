public class LinkedList {
    private Node head;

    public LinkedList() {
        this.head = null;
    }

    public void addTerm(int coefficient, int exponent) {
        Term newTerm = new Term(coefficient, exponent);
        Node newNode = new Node(newTerm);

        if (head == null || exponent > head.getTerm().getExponent()) {
            newNode.setNext(head);
            head = newNode;
        } else {
            Node current = head;
            Node previous = null;
            while (current != null && current.getTerm().getExponent() >= exponent) {
                if (current.getTerm().getExponent() == exponent) {
                    // Combine like terms
                    int newCoeff = current.getTerm().getCoefficient() + coefficient;
                    current.getTerm().setCoefficient(newCoeff);
                    return;
                }
                previous = current;
                current = current.getNext();
            }
            newNode.setNext(current);
            previous.setNext(newNode);
        }
    }

    public Node getHead() {
        return head;
    }

    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.getTerm() + " ");
            current = current.getNext();
        }
        System.out.println();
    }
}
