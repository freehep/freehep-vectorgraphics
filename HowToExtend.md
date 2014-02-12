---
layout: default
title: FreeHEP - Vector Graphics
lead: How To Extend VectorGraphics
slug: developer
dir: .
---
To add an extra output format to the VectorGraphics package one needs to
extend it, and create a separate jar file for the new format. If the format is a
common one, we ([developers@freehep.org](mailto:developers@freehep.org))
may be interested in adding it to our library, with the proper mentioning of
credentials.

The following describes briefly how to extend VectorGraphics to create either
a new Image Format or a new Vector Format. Please refer to the class diagram 
below:

<img alt="VectorGraphics Classes" src="img/ClassDiagram.png" title="VectorGraphics Classes" width="100%"></img>

and to the following table to see how different methods are 
implemented in different formats. Refer to the drivers for these formats for 
examples:

### Vector Graphics Methods

<table class="table">
<thead>
<tr>
<th> <strong>Methods</strong>                     </th>
<th style="text-align:center;"> <strong>CGM</strong> </th>
<th style="text-align:center;"> <strong>EMF</strong> </th>
<th style="text-align:center;"> <strong>PDF</strong> </th>
<th style="text-align:center;"> <strong>PS</strong>  </th>
<th style="text-align:center;"> <strong>SVG</strong> </th>
<th style="text-align:center;"> <strong>SWF</strong> </th>
</tr>
</thead>
<tbody>
<tr>
<td> writeHeader / Trailer           </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
</tr>
<tr>
<td> closeStream                     </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X</td>
</tr>
<tr>
<td> implements MultipPageDocument   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> create                          </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X</td>
</tr>
<tr>
<td> writeGraphicsSave / Restore     </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> draw / fill / drawAndFill       </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X</td>
</tr>
<tr>
<td> drawRect, drawArc, etc          </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> drawSymbol / fillSymbol         </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> copyArea                        </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> drawRenderedImage               </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeImage                      </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X</td>
</tr>
<tr>
<td> drawString                      </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> drawGlyphFactor                 </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> setTransform                    </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeTransform                  </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> rotate / translate / ...        </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> setClip                         </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeClip                       </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeSetClip                    </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeStroke                     </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X</td>
</tr>
<tr>
<td> writeWidth                      </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeCap                        </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeJoin                       </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeMiterLimit                 </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeDash                       </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> setPaintMode                    </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> setXORMode                      </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writePaint                      </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X</td>
</tr>
<tr>
<td> setFont                         </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> getDeviceConfiguration          </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> hit                             </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -</td>
</tr>
<tr>
<td> writeComment                    </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> -   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> X   </td>
<td style="text-align:center;"> -</td>
</tr>
</tbody>
</table>

## Adding a new Image Format

Adding a new Image Format is relatively easy, because we let java do all the
drawing in the PixelGraphics2D class. Once finished drawing the pixels of 
PixelGraphics2D are grabbed and written to the proper output format.

The base class
for all Image formats is `org.freehep.graphicsio.ImageGraphicsIO`, which inherits 
from `PixelGraphics2D` and is therefore useable anywhere where a `java.awt.Graphics` 
context is expected. `ImageGraphicsIO` handles the allocation of the pixel buffer, 
closing of the stream, and production of a thumbnail image if necessary.

The actual class for the new Image Format needs to implement the following 
methods:

* Constructors: one or more constructors to create the actual class from a 
stream, given a size or a component.
  
* Create methods: two create methods which allow VectorGraphics2D to clone 
and make a sub-graphics component.
  
* Write methods: which will write out the bytes according of the pixel buffer 
to the stream.
  
To actually allow the user to select the new Image Format you still need to 
add [Export Class](#exportclass).

## Adding a new Vector Format

create methods

transforms

write methods

To actually allow the user to select the new Image Format you still need to 
add [Export Class](#exportclass).

## <a id="exportclass"></a> Adding a new ExportClass

to be done

Inherit from `org.freehep.graphicsio.exportchooser.ExportGraphicsFileTypeAdapter`.

## Making the output format MultiPage

If your output format allows multiple pages, then the driver for the output 
format could write multiple components each to one page. The driver needs to 
implement `org.freehep.graphicsio.MultiPageDocument`, which needs the following 
methods implemented:

* openPage(), called when a new page starts.
  
* closePage(), called when a page ends.
  
* setHeader(), called to set a header for a page.
  
* setFooter(), called to set a footer for a page.

The Export Class also needs some modifications, (to be defined).
