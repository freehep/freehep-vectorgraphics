
public class HeadlessExample {

    public static void main(String[] args) throws Exception {
        JPanel yourPanel;
        
        FILL UP YOUR PANEL
        
        // run with -Djava.awt.headless=true
        Headless headless = new Headless(yourPanel);
        headless.pack();
        headless.setVisible(true);

        File out = new File("Output.eps");
        VectorGraphics graphics = new PSGraphics2D(out, yourPanel);
        graphics.startExport();
        yourPanel.print(graphics);
        graphics.endExport();
    }
}