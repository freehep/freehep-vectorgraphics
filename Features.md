---
layout: default
title: FreeHEP - Vector Graphics
lead: Features
slug: general
dir: .
---
The VectorGraphics package comes with the following features:

* [Image (bitmap) Formats](Manual.html#image) available: GIF, JPEG, PNG and PPM.

* [Vector Graphics Formats](Manual.html#vector) available: EMF, PDF, PostScript, EPS, SVG
    and SWF.

* Simple ExportDialog provided which can be added to application and allows user to choose from any of the
    available formats and set format specific options.

* No need to change user's painting code, the 
[VectorGraphics](apidocs/org/freehep/graphics2d/VectorGraphics.html)
class can be used anywhere where the standard `java.awt.Graphics` or `java.awt.Graphics2D` classes are used.

* Packaged in a per-format jar file, which when added to the CLASSPATH becomes available.

* Base classes provided to allow easy extension for new formats.

    
