---
layout: default
title: FreeHEP - Vector Graphics
lead: Frequently Asked Questions (FAQ)
slug: user
dir: .
---
## General
      
### The vectorgraphics formats are distributed as separate jar files. I merged the jar files into one, but now I only see a few (or no) formats appear the ExportDialog option box. Where did the other formats go? ###

Every format registers itself as an ExportFileType with a lookup service, so that 
        a program can find ExportFileTypes for a particular format.
	The registration is done by scanning each of the jar files on the
	classpath for the file `META-INF/services/org.freehep.util.export.ExportFileType`.
	This file contains the names of all the ExportFileTypes it should
	register, normally only one per jar file, but sometimes more, in the case of 
	PS and EPS for instance.
	
If you merge all the jar files into one, you should also make 
        a `META-INF/services/org.freehep.util.export.ExportFileType` file with
        ALL the formats in it.


## Clipping

### PostScript and PDF can only reduce the clipping region. Is there a way in my java program to enlarge the region and still output correct PostScript and PDF? ###

Indeed in PostScript and PDF one can only reduce the clipping region.
	VectorGraphics has no protection for you setting a larger region and exporting
	to PostScript or PDF. However, both format can save the graphics state, reduce
	the clipping region and then restore the graphics state (including the initially
	larger clipping region).
        
You do not have direct control over save/restore, but creating a 
	sub-graphics context is used to do a save/restore, as shown in the code below:

```Java
... large (or no) clipping exists in context g.

Graphics2D tempg = g.create();
tempg.clipRect(100, 100, 200, 200);

... draw stuff with smaller clipping

tempg.dispose();

... large (or no) clipping exists in context g.
```
        
Note that it is important to call `tempg.dispose();` at the end, otherwise the 
	newly created graphics object will remain as a memory leak.

