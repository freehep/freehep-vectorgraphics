// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * DefineShape TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineShape.java fe6d709a107e 2006/11/27 18:25:46 duns $
 */
public class DefineShape extends DefinitionTag {

    protected int character;

    protected Rectangle2D bounds;

    protected FillStyleArray fillStyles;

    protected LineStyleArray lineStyles;

    protected SWFShape shape;

    public DefineShape(int id, Rectangle2D bounds, FillStyleArray fillStyles,
            LineStyleArray lineStyles, SWFShape shape) {
        this();
        character = id;
        this.bounds = bounds;
        this.fillStyles = fillStyles;
        this.lineStyles = lineStyles;
        this.shape = shape;
    }

    public DefineShape() {
        super(2, 1);
    }

    protected DefineShape(int tagID, int version) {
        super(tagID, version);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        DefineShape tag = new DefineShape();
        tag.read(tagID, swf, false);
        return tag;
    }

    protected void read(int tagID, SWFInputStream swf, boolean hasAlpha)
            throws IOException {
        character = swf.readUnsignedShort();
        swf.getDictionary().put(character, this);
        bounds = swf.readRect();

        fillStyles = new FillStyleArray(swf, false, hasAlpha);
        lineStyles = new LineStyleArray(swf, false, hasAlpha, false);

        shape = new SWFShape(swf, fillStyles, lineStyles, false, hasAlpha, false);
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        write(swf, false);
    }

    public void write(SWFOutputStream swf, boolean hasAlpha) throws IOException {
        swf.writeUnsignedShort(character);
        swf.writeRect(bounds);
        
        fillStyles.write(swf, hasAlpha);
        lineStyles.write(swf, hasAlpha, false);

        shape.write(swf, hasAlpha, false);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append(fillStyles.toString());
        s.append(lineStyles.toString());
        s.append("  shape: \n");
        s.append((shape != null) ? shape.toString() : "null");
        return s.toString();
    }

}
