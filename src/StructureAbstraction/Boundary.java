package StructureAbstraction;

import simplegui.SimpleGUI;

public class Boundary {

    double x, y;
    private double sigVal;
    private boolean display = true;
    
    //previous and next pixels that are displayed
    private Boundary prevDisp; 
    private Boundary nextDisp;

    public Boundary(int x, int y, Boundary p) {
        this.x = x;
        this.y = y;
        prevDisp = p;
        //sets the prevDisp Boundary to itself when constructing the very first pixel
        if (p == null)
        {
            prevDisp = this;
        } else {
            p.nextDisp = this;
        }
    }

    public void show() {
        display = true;
    }

    public void erase() {
        int x = 1;
        prevDisp.nextDisp = this.nextDisp;
        nextDisp.prevDisp = this.prevDisp;
        display = false;
    }

    public void setPrev(Boundary p) {
        prevDisp = p;
    }

    public void setNext(Boundary n) {
        nextDisp = n;
    }

    public Boundary getPrev() {
        return prevDisp;
    }

    public Boundary getNext() {
        return nextDisp;
    }

    public boolean getDisplay() {
        return display;
    }

    public double getSigVal() {
        return sigVal;
    }

    public void setSigVal(double x) {
        sigVal = x;
    }

    public void draw(SimpleGUI sg, Boundary next) {
        sg.drawLine(x, y, next.x, next.y);
    }
    //Discrete Curve Evolution algorithm to define the visual point significance
    public double computeSig(Boundary prev, Boundary next) {
        double a = Math.sqrt(Math.pow((x - prev.x), 2) + Math.pow((y - prev.y), 2));
        double b = Math.sqrt(Math.pow((x - next.x), 2) + Math.pow((y - next.y), 2));
        double c = Math.sqrt(Math.pow((prev.x - next.x), 2) + Math.pow((prev.y - next.y), 2));
        sigVal = (a + b) - c;
        return sigVal;
    }

    @Override
    public String toString() {
        return ("Visible: " + display + " x: " + x + ", y: " + y + ", Significance: " + sigVal);
    }
}
