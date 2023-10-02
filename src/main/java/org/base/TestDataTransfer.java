package org.base;

public class TestDataTransfer<A,B,C> {
    private final A status;
    private final B category;
    private final C error;

    public TestDataTransfer(A status, B category, C error) {
        this.status = status;
        this.category = category;
        this.error = error;
    }

    public A getStatus() { return this.status; }
    public B getCategory() { return this.category; }
    public C getError() { return this.error; }

    @Override
    public String toString() {
        return "TestDataTransfer {" +
                "\n <br> status: " + status + "" +
                "\n <br> category: " + category + "" +
                "\n <br> error desc: " + error + "<br>" +
                '}';
    }
}
