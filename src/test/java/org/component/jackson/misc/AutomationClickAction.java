package org.component.jackson.misc;

public class AutomationClickAction {
    private String click;
    private String element;

    public AutomationClickAction() {}

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return "AutomationClickAction{" +
                "click='" + click + '\'' +
                ", element='" + element + '\'' +
                '}';
    }
}
