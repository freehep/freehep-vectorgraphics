// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * DefineMorphShape TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineMorphShape.java fe6d709a107e 2006/11/27 18:25:46 duns $
 */
public class DefineMorphShape extends DefinitionTag {

    private int character;

    private Rectangle2D startBounds, endBounds;

    protected FillStyleArray fillStyles;

    protected LineStyleArray lineStyles;

    protected SWFShape startEdges, endEdges;

    public DefineMorphShape(int id, Rectangle2D startBounds,
            Rectangle2D endBounds, FillStyleArray fillStyles,
            LineStyleArray lineStyles, SWFShape startEdges, SWFShape endEdges) {
        this();
        character = id;
        this.startBounds = startBounds;
        this.endBounds = endBounds;
        this.fillStyles = fillStyles;
        this.lineStyles = lineStyles;
        this.startEdges = startEdges;
        this.endEdges = endEdges;
    }

    public DefineMorphShape() {
        super(46, 3);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineMorphShape tag = new DefineMorphShape();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);

        tag.startBounds = swf.readRect();
        tag.endBounds = swf.readRect();

        // ignored
        /* long offset = */ swf.readUnsignedInt();

        fillStyles = new FillStyleArray(swf, true, true);
        lineStyles = new LineStyleArray(swf, true, true, false);

        startEdges = new SWFShape(swf, fillStyles, lineStyles, true, true, false);
        endEdges = new SWFShape(swf, fillStyles, lineStyles, true, true, false);

        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(character);
        swf.writeRect(startBounds);
        swf.writeRect(endBounds);

        swf.pushBuffer();

        fillStyles.write(swf, true);
        lineStyles.write(swf, true, false);

        startEdges.write(swf, true, false);
        int offset = swf.popBuffer();
        swf.writeUnsignedInt(offset);
        swf.append();

        endEdges.write(swf, true, false);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:   " + character + "\n");
        s.append("  startBounds: " + startBounds + "\n");
        s.append("  endBounds:   " + endBounds + "\n");
        s.append(fillStyles.toString());
        s.append(lineStyles.toString());
        s.append("  startEdges: " + startEdges + "\n");
        s.append("  endEdges:   " + endEdges + "\n");
        return s.toString();
    }
}
