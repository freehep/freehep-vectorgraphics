// Copyright 2006, FreeHEP
package org.freehep.graphicsio.test;

import java.io.InputStream;
import java.io.InputStreamReader;

import jas.hist.JASHist;
import jas.hist.XMLHistBuilder;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestScatterPlot.java 00143e69ce01 2006/11/16 01:02:20 duns $
 */
public class TestScatterPlot extends TestingPanel {

	private JASHist plot;

	public TestScatterPlot(String[] args) throws Exception {

		super(args);
		setName("ScatterPlot");

		String plotml = "TestScatterPlot.plotml";
		InputStream in = getClass().getResourceAsStream(plotml);
		XMLHistBuilder xhb = new XMLHistBuilder(new InputStreamReader(in), plotml);
		plot = xhb.getSoloPlot();
		plot.setAllowUserInteraction(false);
        add(plot);
	}

	public static void main(String[] args) throws Exception {
		new TestScatterPlot(args).runTest();
	}

}
