// Copyright 2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * DefineShape4 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineShape4.java 3e48ba4ef214 2006/11/27 22:51:07 duns $
 */
public class DefineShape4 extends DefineShape {

	protected Rectangle2D edgeBounds;
	protected boolean usesNonScalingStrokes;
	protected boolean usesScalingStrokes = true;
	
    public DefineShape4(int id, Rectangle2D bounds, FillStyleArray fillStyles, LineStyleArray lineStyles, SWFShape shape) {
        this(id, bounds, bounds, false, true, fillStyles, lineStyles, shape);
    }
    
    public DefineShape4(int id, Rectangle2D bounds, Rectangle2D edgeBounds, 
    		boolean usesNonScalingStrokes, boolean usesScalingStrokes,
    		FillStyleArray fillStyles,
            LineStyleArray lineStyles, SWFShape shape) {
        this();
        character = id;
        this.bounds = bounds;
        this.edgeBounds = edgeBounds;
        this.usesNonScalingStrokes = usesNonScalingStrokes;
        this.usesScalingStrokes = usesScalingStrokes;
        this.fillStyles = fillStyles;
        this.lineStyles = lineStyles;
        this.shape = shape;
    }

    public DefineShape4() {
        super(83, 8);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        DefineShape4 tag = new DefineShape4();
        
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);
        bounds = swf.readRect();

        tag.edgeBounds = swf.readRect();
        swf.readUBits(6);
        tag.usesNonScalingStrokes = swf.readBitFlag();
        tag.usesScalingStrokes = swf.readBitFlag();
        
        tag.fillStyles = new FillStyleArray(swf, false, true);
        tag.lineStyles = new LineStyleArray(swf, false, true, true);

        tag.shape = new SWFShape(swf, tag.fillStyles, tag.lineStyles, false, true, false);

        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(character);
        swf.writeRect(bounds);

    	swf.writeRect(edgeBounds);
    	swf.writeUBits(0, 6);
    	swf.writeBitFlag(usesNonScalingStrokes);
    	swf.writeBitFlag(usesScalingStrokes);
        
        fillStyles.write(swf, false, true);
        lineStyles.write(swf, false, true, true);

        shape.write(swf, false, true, true);
    }
}
