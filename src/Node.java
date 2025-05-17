public class Node {
    private Term term;
    private Node next;

    public Node(Term term) {
        this.term = term;
        this.next = null;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
