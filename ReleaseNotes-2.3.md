---
layout: default
title: FreeHEP - Vector Graphics
lead: Release Notes 2.3
slug: user
dir: .
---
The VectorGraphics package of <a href="http://java.frehep.org">FreeHEP Java Library</a> enables any Java program to export to a variety of vector graphics formats as well as bitmap image formats. Among the vector formats are PostScript, PDF, EMF, SVG, and SWF (Flash). The image formats include GIF, PNG, JPG, BMP, WBPM and PPM. All image formats included in the Java runtime, as well as any in the Java Advanced Imaging package (if installed) are also be available. A special JAVA export format will write a java file with all the calls your application makes to Graphics2D. This java file can help us debug any potential problems.
    
The package uses the standard java.awt.Graphics2D class as its interface to the user program. Using this package from any Java GUI program is therefore quite easy. It also comes with a dialog box which allows you to choose between all the formats mentioned above and set specific parameters for them.

### Changes

Note: since version 2.2 freehep-vectorgraphics is moved to github and no longer relies on
the base freehep libraries, except for freehep-io which is also available from github. 
All other freehep libraries needed are packaged in freehep-graphicsbase and use a different
package name than freehep used to use for these libraries.

* Changes made for version 2.2 are documented in [github](https://github.com/freehep/freehep-vectorgraphics/issues?milestone=1&state=closed)
* Changes made for version 2.2.1 are documented in [github](https://github.com/freehep/freehep-vectorgraphics/issues?milestone=3&state=closed).
* Changes made for version 2.3 are documented in [github](https://github.com/freehep/freehep-vectorgraphics/issues?milestone=2&state=closed). 

These versions are binary compatible at the java.awt.Graphics2d level with version 2.0. However if you used classes/methods from the low-level format drivers or have used methods of the VectorGraphics class or its descendants you may have to adjust your code and recompile.

This version runs on Java 1.6 or better.
    
### Documentation

To use VectorGraphics see the [Manual](), [API]() and [Installation]() guide. To build it yourself look at the [Building]() documentation. To view its output, look at the [Screeshots]() and the [Test pages](). Two simple examples are included in the [downloads](). VectorGraphics is licensed under both [Apache and LGPL](). All code is available from [github]() and released version are published to [Maven Central](). Bugs can be reported on [github]() as well. 
