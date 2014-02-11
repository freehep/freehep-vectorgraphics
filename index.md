---
layout: home
title: FreeHEP - VectorGraphics
slug: vectorgraphics
dir: .
---
# Overview

The **Vector Graphics** package of the [FreeHEP Java Library](http://java.freehep.org) 
enables any Java program to export to a variety of
vector graphics formats as well as bitmap image formats. Among the
[vector formats](Manual.html#Vector_Formats_Alphabetical) are 
PostScript, PDF, EMF, SVF, and Flash SWF, 
while the [image formats](./Manual.html#Image_Formats_Alphabetical)
include GIF, PNG, JPG and PPM.

The package uses the standard java.awt.Graphics2D class as its interface to
the user program. Coupling this package to a standard Java program is therefore
straightforward. It also comes with a dialog box which allows you to choose between
all the formats mentioned above and set specific parameters for them.

![VectorGraphics Architecture](img/Architecture.png "VectorGraphics Architecture")
