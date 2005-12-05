// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * ScalingMode TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/ScalingMode.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class ScalingMode extends CGMTag {

    public static final int ABSTRACT = 0;

    public static final int METRIC = 1;

    private int type;

    private double factor;

    public ScalingMode() {
        super(2, 1, 1);
    }

    public ScalingMode(int type) {
        this(ABSTRACT, 0.0);
    }

    public ScalingMode(int type, double factor) {
        this();
        this.type = type;
        this.factor = factor;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeEnumerate(type);
        if (type == METRIC) {
            cgm.writeFloatingPoint(factor);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("SCALEMODE ");
        cgm.print((type == ABSTRACT) ? "ABSTRACT " : "METRIC ");
        if (type == METRIC) {
            cgm.writeFloatingPoint(factor);
        }
    }
}
