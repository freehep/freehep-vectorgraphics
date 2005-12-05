// Copyright 2001-2003 FreeHEP.
package org.freehep.graphicsio.cgm;

import org.freehep.util.io.TagSet;

/**
 * CGM specific tagset.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CGMTagSet.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CGMTagSet extends TagSet {

    public CGMTagSet(int version) {
        // FIXME register all tags here
        if (version >= 1) {
            addTag(new BeginMetafile());
            addTag(new MetafileVersion());
            addTag(new MetafileDefaultsReplacement());
            addTag(new MetafileDescription());
            addTag(new MetafileElementList());
            addTag(new BeginPicture());
            addTag(new BeginPictureBody());
            addTag(new EndPicture());
            addTag(new EndMetafile());

            addTag(new AlternateCharacterSetIndex());
            addTag(new AppendText());
            addTag(new ApplicationData());
            addTag(new AspectSourceFlags());
            addTag(new AuxiliaryColor());
            addTag(new BackgroundColor());
            addTag(new CellArray());
            addTag(new CharacterCodingAnnouncer());
            addTag(new CharacterExpansionFactor());
            addTag(new CharacterHeight());
            addTag(new CharacterOrientation());
            addTag(new CharacterSetIndex());
            addTag(new CharacterSetList());
            addTag(new CharacterSpacing());
            addTag(new Circle());
            addTag(new CircularArc3Point());
            addTag(new CircularArc3PointClose());
            addTag(new CircularArcCentre());
            addTag(new CircularArcCentreClose());
            addTag(new ClipIndicator());
            addTag(new ClipRectangle());
            addTag(new ColorIndexPrecision());
            addTag(new ColorPrecision());
            addTag(new ColorSelectionMode());
            addTag(new ColorTable());
            addTag(new ColorValueExtent());
            addTag(new DisjointPolyline());
            addTag(new EdgeBundleIndex());
            addTag(new EdgeColor());
            addTag(new EdgeType());
            addTag(new EdgeVisibility());
            addTag(new EdgeWidth());
            addTag(new EdgeWidthSpecificationMode());
            addTag(new Ellipse());
            addTag(new EllipticalArc());
            addTag(new EllipticalArcClose());
            addTag(new Escape());
            addTag(new FillBundleIndex());
            addTag(new FillColor());
            addTag(new FillReferencePoint());
            addTag(new FontList());
            addTag(new GeneralizedDrawingPrimitive());
            addTag(new HatchIndex());
            addTag(new IndexPrecision());
            addTag(new IntegerPrecision());
            addTag(new InteriorStyle());
            addTag(new LineBundleIndex());
            addTag(new LineColor());
            addTag(new LineType());
            addTag(new LineWidth());
            addTag(new LineWidthSpecificationMode());
            addTag(new MarkerBundleIndex());
            addTag(new MarkerColor());
            addTag(new MarkerSize());
            addTag(new MarkerSizeSpecificationMode());
            addTag(new MarkerType());
            addTag(new MaximumColorIndex());
            addTag(new Message());
            addTag(new NoOp());
            addTag(new PatternIndex());
            addTag(new PatternSize());
            addTag(new PatternTable());
            addTag(new Polygon());
            addTag(new PolygonSet());
            addTag(new Polyline());
            addTag(new Polymarker());
            addTag(new RealPrecision());
            addTag(new Rectangle());
            addTag(new RestrictedText());
            addTag(new ScalingMode());
            addTag(new Text());
            addTag(new TextAlignment());
            addTag(new TextBundleIndex());
            addTag(new TextColor());
            addTag(new TextFontIndex());
            addTag(new TextPath());
            addTag(new TextPrecision());
            addTag(new Transparency());
            addTag(new VDCExtent());
            addTag(new VDCIntegerPrecision());
            addTag(new VDCType());
            addTag(new VDCRealPrecision());
        }

        if (version >= 2) {
            addTag(new BeginFigure());
            addTag(new BeginSegment());
            addTag(new EndFigure());
            addTag(new EndSegment());

            addTag(new ConnectingEdge());
            addTag(new MaximumVDCExtent());
            addTag(new NamePrecision());
            addTag(new SegmentPriorityExtent());
        }

        if (version >= 3) {
            addTag(new EdgeCap());
            addTag(new EdgeJoin());
            addTag(new InteriorStyleSpecificationMode());
            addTag(new LineCap());
            addTag(new LineJoin());
            addTag(new MitreLimit());
            addTag(new TransparentCellColour());
        }
    }
}
