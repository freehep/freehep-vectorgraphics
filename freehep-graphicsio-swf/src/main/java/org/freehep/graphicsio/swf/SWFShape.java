// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.util.Vector;

import org.freehep.util.io.BitOutputStream;

/**
 * Shape TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFShape.java fe6d709a107e 2006/11/27 18:25:46 duns $
 */
public class SWFShape {

    private int numFillBits;

    private int numLineBits;

    protected FillStyleArray fillStyles;

    protected LineStyleArray lineStyles;

    private Vector records;

    public SWFShape(Vector records) {
        this.records = records;
    }

    // for font shapes
    public SWFShape(SWFInputStream input) throws IOException {
        this(input, null, null, false, false, false);
    }

    public SWFShape(SWFInputStream input, FillStyleArray fillStyles,
            LineStyleArray lineStyles, boolean isMorphStyle, boolean hasAlpha, boolean hasStyles)
            throws IOException {

        this.fillStyles = fillStyles;
        this.lineStyles = lineStyles;

        input.byteAlign();
        numFillBits = (int) input.readUBits(4);
        numLineBits = (int) input.readUBits(4);

        records = new Vector();

        boolean endOfShape = false;
        do {
            boolean edge = input.readBitFlag();
            if (edge) {
                // Edge Record
                records.add(new EdgeRecord(input));
            } else {
                // Shape Record
                int state = (int) input.readUBits(5);
                if (state == 0) {
                    endOfShape = true;
                } else {
                    records.add(new ShapeRecord(this, input, numFillBits,
                            numLineBits, isMorphStyle, hasAlpha, hasStyles, state));
                }
            }
        } while (!endOfShape);
    }

    // for font shapes
    public void write(SWFOutputStream swf) throws IOException {
        write(swf, false, false);	// FIXME, false for hasStyles is a guess.
    }

    public void write(SWFOutputStream swf, boolean hasAlpha, boolean hasStyles) throws IOException {
        swf.byteAlign();
        int numFillBits = 0;
        int numLineBits = 0;

        for (int i = 0; i < records.size(); i++) {
            Record r = (Record) records.get(i);
            if (r instanceof ShapeRecord) {
                ShapeRecord s = (ShapeRecord) r;
                numFillBits = Math.max(numFillBits, s.getNumFillBits());
                numLineBits = Math.max(numLineBits, s.getNumLineBits());
            }
        }
        swf.writeUBits(numFillBits, 4);
        swf.writeUBits(numLineBits, 4);

        for (int i = 0; i < records.size(); i++) {
            Record r = (Record) records.get(i);
            r.write(swf, numFillBits, numLineBits, hasAlpha, hasStyles);
        }
        swf.writeUBits(0, 6); // End of Shape
        swf.byteAlign(); // make sure any offset calculations will be correct
    }

    void setNumFillBits(int numFillBits) {
        this.numFillBits = numFillBits;
    }

    void setNumLineBits(int numLineBits) {
        this.numLineBits = numLineBits;
    }

    void setFillStyles(FillStyleArray fillStyles) {
        this.fillStyles = fillStyles;
    }

    void setLineStyles(LineStyleArray lineStyles) {
        this.lineStyles = lineStyles;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < records.size(); i++) {
            s.append("    ");
            s.append(i);
            s.append(":");
            s.append(records.get(i));
            s.append("\n");
        }
        s.append("    End of shape\n");
        return s.toString();
    }

    /**
     * Abstract superclass for Records.
     */
    public static abstract class Record {
        public abstract void write(SWFOutputStream swf, int numFillBits,
                int numLineBits, boolean hasAlpha, boolean hasStyles) throws IOException;
    }

    /**
     * Shape Record, specifying style, move, ...
     */
    public static class ShapeRecord extends Record {

        private static final int MOVE = 0x01;

        private static final int FILLSTYLE0 = 0x02;

        private static final int FILLSTYLE1 = 0x04;

        private static final int LINESTYLE = 0x08;

        private static final int NEWSTYLES = 0x10;

        private boolean move;

        private int moveX = 0;

        private int moveY = 0;

        private int fillStyle0 = -1;

        private int fillStyle1 = -1;

        private int lineStyle = -1;

        private FillStyleArray newFillStyles;

        private LineStyleArray newLineStyles;

        public ShapeRecord(boolean move, int moveX, int moveY, int fillStyle0,
                int fillStyle1, int lineStyle) {
            this.move = move;
            this.moveX = moveX;
            this.moveY = moveY;
            this.fillStyle0 = fillStyle0;
            this.fillStyle1 = fillStyle1;
            this.lineStyle = lineStyle;
        }

        ShapeRecord(SWFShape shape, SWFInputStream input, int numFillBits,
                int numLineBits, boolean isMorphStyle, boolean hasAlpha, boolean hasStyles,
                int state) throws IOException {

            if ((state & MOVE) > 0) {
                move = true;
                int numMoveBits = (int) input.readUBits(5);
                moveX = (int) input.readSBits(numMoveBits);
                moveY = (int) input.readSBits(numMoveBits);
            }

            if ((state & FILLSTYLE0) > 0)
                fillStyle0 = (int) input.readUBits(numFillBits);
            if ((state & FILLSTYLE1) > 0)
                fillStyle1 = (int) input.readUBits(numFillBits);
            if ((state & LINESTYLE) > 0)
                lineStyle = (int) input.readUBits(numLineBits);

            if ((state & NEWSTYLES) > 0) {
                newFillStyles = new FillStyleArray(input, isMorphStyle,
                        hasAlpha);
                shape.setFillStyles(newFillStyles);
                newLineStyles = new LineStyleArray(input, isMorphStyle,
                        hasAlpha, hasStyles);
                shape.setLineStyles(newLineStyles);

                numFillBits = (int) input.readUBits(4);
                shape.setNumFillBits(numFillBits);
                numLineBits = (int) input.readUBits(4);
                shape.setNumLineBits(numLineBits);
            }
        }

        public void write(SWFOutputStream swf, int numFillBits,
                int numLineBits, boolean hasAlpha, boolean hasStyles) throws IOException {

            int state = 0x00;
            if (newFillStyles != null)
                state |= NEWSTYLES;
            if (lineStyle != -1)
                state |= LINESTYLE;
            if (fillStyle1 != -1)
                state |= FILLSTYLE1;
            if (fillStyle0 != -1)
                state |= FILLSTYLE0;
            if (move)
                state |= MOVE;
            swf.writeUBits(state, 6);

            if (move) {
                int numMoveBits = Math.max(BitOutputStream.minBits(moveX, true), BitOutputStream
                        .minBits(moveY, true));
                swf.writeUBits(numMoveBits, 5);
                swf.writeSBits(moveX, numMoveBits);
                swf.writeSBits(moveY, numMoveBits);
            }

            if (fillStyle0 != -1)
                swf.writeUBits(fillStyle0, numFillBits);
            if (fillStyle1 != -1)
                swf.writeUBits(fillStyle1, numFillBits);
            if (lineStyle != -1)
                swf.writeUBits(lineStyle, numLineBits);

            if (newFillStyles != null) {
                newFillStyles.write(swf, hasAlpha);
                newLineStyles.write(swf, hasAlpha, hasStyles);
                swf.writeUBits(numFillBits, 4);
                swf.writeUBits(numLineBits, 4);
            }
        }

        public int getNumFillBits() {
            int numFillBits = 0;
            if (fillStyle0 != -1)
                numFillBits = Math.max(numFillBits, BitOutputStream
                        .minBits(fillStyle0));
            if (fillStyle1 != -1)
                numFillBits = Math.max(numFillBits, BitOutputStream
                        .minBits(fillStyle1));
            return numFillBits;
        }

        public int getNumLineBits() {
            return (lineStyle != -1) ? BitOutputStream.minBits(lineStyle) : 0;
        }

        public String toString() {
            StringBuffer s = new StringBuffer();
            if (move)
                s.append("moveto(" + moveX + ", " + moveY + "); ");
            if (fillStyle0 != -1)
                s.append("fillStyle0=" + fillStyle0 + "; ");
            if (fillStyle1 != -1)
                s.append("fillStyle1=" + fillStyle1 + "; ");
            if (lineStyle != -1)
                s.append("lineStyle=" + lineStyle + "; ");
            if ((newFillStyles != null) || (newLineStyles != null))
                s.append("More Styles;\n");
            if (newFillStyles != null)
                s.append(newFillStyles.toString());
            if (newLineStyles != null)
                s.append(newLineStyles.toString());
            return s.toString();
        }
    }

    /**
     * Edge Record, specifying lines, curves, ...
     */
    public static class EdgeRecord extends Record {

        boolean curve;

        int controlDeltaX, controlDeltaY, anchorDeltaX, anchorDeltaY;

        int deltaX, deltaY;

        public EdgeRecord(int controlDeltaX, int controlDeltaY,
                int anchorDeltaX, int anchorDeltaY) {
            curve = true;
            this.controlDeltaX = controlDeltaX;
            this.controlDeltaY = controlDeltaY;
            this.anchorDeltaX = anchorDeltaX;
            this.anchorDeltaY = anchorDeltaY;
        }

        public EdgeRecord(int deltaX, int deltaY) {
            curve = false;
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        EdgeRecord(SWFInputStream input) throws IOException {
            curve = !input.readBitFlag();
            int numBits = (int) input.readUBits(4) + 2;
            if (curve) {
                controlDeltaX = (int) input.readSBits(numBits);
                controlDeltaY = (int) input.readSBits(numBits);
                anchorDeltaX = (int) input.readSBits(numBits);
                anchorDeltaY = (int) input.readSBits(numBits);
            } else {
                if (input.readBitFlag()) {
                    // general line
                    deltaX = (int) input.readSBits(numBits);
                    deltaY = (int) input.readSBits(numBits);
                } else {
                    if (input.readBitFlag()) {
                        // vertical
                        deltaX = 0;
                        deltaY = (int) input.readSBits(numBits);
                    } else {
                        // horizontal
                        deltaX = (int) input.readSBits(numBits);
                        deltaY = 0;
                    }
                }
            }
        }

        public void write(SWFOutputStream swf, int numFillBits,
                int numLineBits, boolean hasAlpha, boolean hasStyles) throws IOException {

            swf.writeBitFlag(true);
            swf.writeBitFlag(!curve);

            int numBits = 0;
            if (curve) {
                numBits = Math.max(numBits, BitOutputStream.minBits(controlDeltaX, true));
                numBits = Math.max(numBits, BitOutputStream.minBits(controlDeltaY, true));
                numBits = Math.max(numBits, BitOutputStream.minBits(anchorDeltaX, true));
                numBits = Math.max(numBits, BitOutputStream.minBits(anchorDeltaY, true));
            } else {
                if ((deltaX != 0) && (deltaY != 0)) {
                    numBits = Math.max(numBits, BitOutputStream.minBits(deltaX, true));
                    numBits = Math.max(numBits, BitOutputStream.minBits(deltaY, true));
                } else {
                    if (deltaX == 0) {
                        // vertical
                        numBits = Math.max(numBits, BitOutputStream.minBits(deltaY, true));
                    } else {
                        // horizontal
                        numBits = Math.max(numBits, BitOutputStream.minBits(deltaX, true));
                    }
                }
            }
            swf.writeUBits(numBits - 2, 4);

            if (curve) {
                swf.writeSBits(controlDeltaX, numBits);
                swf.writeSBits(controlDeltaY, numBits);
                swf.writeSBits(anchorDeltaX, numBits);
                swf.writeSBits(anchorDeltaY, numBits);
            } else {
                if ((deltaX != 0) && (deltaY != 0)) {
                    // general line
                    swf.writeBitFlag(true);
                    swf.writeSBits(deltaX, numBits);
                    swf.writeSBits(deltaY, numBits);
                } else {
                    swf.writeBitFlag(false);
                    if (deltaX == 0) {
                        // vertical
                        swf.writeBitFlag(true);
                        swf.writeSBits(deltaY, numBits);
                    } else {
                        // horizontal
                        swf.writeBitFlag(false);
                        swf.writeSBits(deltaX, numBits);
                    }
                }
            }
        }

        public String toString() {
            if (curve) {
                return "rcurveto(" + controlDeltaX + ", " + controlDeltaY
                        + ", " + anchorDeltaX + ", " + anchorDeltaY + ")";
            } else {
                return "rlineto(" + deltaX + ", " + deltaY + ")";
            }
        }
    }
}
