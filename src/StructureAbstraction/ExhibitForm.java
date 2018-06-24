package StructureAbstraction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import simplegui.GUIListener;
import simplegui.SimpleGUI;


public class ExhibitForm implements GUIListener {

    //store coordinates in array
    Boundary[] pixels = new Boundary[400]; 
    SimpleGUI sg;
    boolean dots = false;

    public void start() throws IOException {
        sg = new SimpleGUI(530, 700);
        sg.registerToGUI(this);
        readPixels();
        checkSigVals();
        drawPixels();
    }

    public void readPixels() throws IOException {
        //read coordinate values from file
        try (BufferedReader br = new BufferedReader(new FileReader("shapeList.txt"))) {
            String line = br.readLine();
            int index = 0;
            while (line != null) {
                int first = Integer.parseInt(line.substring(0, line.indexOf(" ")));
                int second = 700 - Integer.parseInt(line.substring(line.indexOf(" ") + 1));
                pixels[index] = new Boundary(first, second, (index == 0 ? null : pixels[index - 1]));
                
                line = br.readLine();
                index++;
            }
            pixels[0].setPrev(pixels[pixels.length - 1]);//loop link the first and last point together
            pixels[pixels.length - 1].setNext(pixels[0]);
            pixels[0].setSigVal(Double.POSITIVE_INFINITY);//first and last pixel are never deleted (set to inf)
            pixels[pixels.length - 1].setSigVal(Double.POSITIVE_INFINITY);
        }
    }

    public void checkSigVals() {
        for (int i = 1; i < pixels.length - 1; i++) {
            pixels[i].computeSig(pixels[i - 1], pixels[i + 1]);
        }
    }

    public void updateSigVals(int i) {
        //computes the sig values, O(1)
        if (i != 0 && i != pixels.length) {
            Boundary left = pixels[i].getPrev();
            Boundary right = pixels[i].getNext();
            left.computeSig(left.getPrev(), right);
            right.computeSig(left, right.getNext());
        }
        pixels[0].setSigVal(Double.POSITIVE_INFINITY);
        pixels[pixels.length - 1].setSigVal(Double.POSITIVE_INFINITY);
    }

    public void MowLoSigVal() {
        //find lowest sigVal pixel and removes it until there's enough pixels #
        resetPix();
        for (int i = 0; i < (sg.getSliderValue() * 2) + 165; i++) {
        //i: pixels to be removed, more i = fewer pixels
            int lowest = 1;
            for (int n = 1; n < pixels.length - 2; n++) {
                if (pixels[n].getSigVal() < pixels[lowest].getSigVal() && pixels[n].getDisplay()) {
                    lowest = n;
                }
            }
            pixels[lowest].computeSig(pixels[lowest].getPrev(), pixels[lowest].getNext());
            pixels[lowest].erase();
            updateSigVals(lowest);
        }
    }

    public void drawPixels() {
        //O(n)
        sg.eraseAllDrawables();
        int prevIndex = -1;
        int numPix;
        numPix = 1;
        for (int i = 0; i < pixels.length - 1; i++) {
            if (pixels[i].getDisplay()) {
                if (dots) {
                    sg.drawDot(pixels[i].getNext().x, pixels[i].getNext().y, 3);
                    sg.drawDot(pixels[i].getPrev().x, pixels[i].getPrev().y, 3);
                }
                if (prevIndex == -1) {
                    prevIndex = i;
                } else {
                    pixels[prevIndex].draw(sg, pixels[i]);
                    prevIndex = i;
                    numPix++;
                }
            }
        }
    }

    private void resetPix() {
        for (Boundary pixel : pixels) {
            pixel.show();
        }
    }

    private void printPix() {
        for (Boundary pixel : pixels) {
            System.out.println(pixel);
        }
    }

    @Override
    public void reactToButton1() {

    }

    @Override
    public void reactToButton2() {

    }

    @Override
    public void reactToSwitch(boolean bln) {
        dots = bln;
    }

    @Override
    public void reactToSlider(int i) {
        MowLoSigVal();
        drawPixels();
    }

}
