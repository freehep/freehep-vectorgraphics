// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.ps;

import java.awt.Color;

/**
 * 
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/ColorMap.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class ColorMap {

    final public static int red = 0;

    final public static int green = 1;

    final public static int blue = 2;

    final public static int cyan = 3;

    final public static int magenta = 4;

    final public static int yellow = 5;

    final public static int orange = 6;

    final public static int pink = 7;

    final public static int white = 8;

    final public static int lightGray = 9;

    final public static int gray = 10;

    final public static int darkGray = 11;

    final public static int black = 12;

    final public static int redAsGray = 13;

    final public static int greenAsGray = 14;

    final public static int blueAsGray = 15;

    final public static int cyanAsGray = 16;

    final public static int magentaAsGray = 17;

    final public static int yellowAsGray = 18;

    final public static int orangeAsGray = 19;

    final public static int pinkAsGray = 20;

    final public static int whiteAsGray = 21;

    final public static int lightGrayAsGray = 22;

    final public static int grayAsGray = 23;

    final public static int darkGrayAsGray = 24;

    final public static int blackAsGray = 25;

    final public static int redInvisible = 26;

    final public static int greenInvisible = 27;

    final public static int blueInvisible = 28;

    final public static int cyanInvisible = 29;

    final public static int magentaInvisible = 30;

    final public static int yellowInvisible = 31;

    final public static int orangeInvisible = 32;

    final public static int pinkInvisible = 33;

    final public static int whiteInvisible = 34;

    final public static int lightGrayInvisible = 35;

    final public static int grayInvisible = 36;

    final public static int darkGrayInvisible = 37;

    final public static int blackInvisible = 38;

    final public static String[] psColorTag = { "Cr", "Cg", "Cb", "Cc", "Cm",
            "Cy", "Co", "Cp", "Cw", "Cgrl", "Cgr", "Cgrd", "Ck", "CGr", "CGg",
            "CGb", "CGc", "CGm", "CGy", "CGo", "CGp", "CGw", "CGgrl", "CGgr",
            "CGgrd", "CGk", "CIr", "CIg", "CIb", "CIc", "CIm", "CIy", "CIo",
            "CIp", "CIw", "CIgrl", "CIgr", "CIgrd", "CIk" };

    // The "foreground" colors. They remain true colors within the
    // display and print color maps. In the grayscale and b&w color
    // maps these always appear as black.
    final protected static MappedColor displayRed = new MappedColor(255, 0, 0,
            red);

    final protected static MappedColor printRed = new MappedColor(255, 84, 84,
            red);

    final protected static MappedColor grayRed = new MappedColor(0, 0, 0, red);

    final protected static MappedColor bwRed = new MappedColor(0, 0, 0, red);

    final protected static MappedColor displayGreen = new MappedColor(0, 255,
            0, green);

    final protected static MappedColor printGreen = new MappedColor(84, 255,
            84, green);

    final protected static MappedColor grayGreen = new MappedColor(0, 0, 0,
            green);

    final protected static MappedColor bwGreen = new MappedColor(0, 0, 0, green);

    final protected static MappedColor displayBlue = new MappedColor(0, 0, 255,
            blue);

    final protected static MappedColor printBlue = new MappedColor(84, 84, 255,
            blue);

    final protected static MappedColor grayBlue = new MappedColor(0, 0, 0, blue);

    final protected static MappedColor bwBlue = new MappedColor(0, 0, 0, blue);

    final protected static MappedColor displayCyan = new MappedColor(0, 255,
            255, cyan);

    final protected static MappedColor printCyan = new MappedColor(0, 255, 255,
            cyan);

    final protected static MappedColor grayCyan = new MappedColor(0, 0, 0, cyan);

    final protected static MappedColor bwCyan = new MappedColor(0, 0, 0, cyan);

    final protected static MappedColor displayMagenta = new MappedColor(255, 0,
            255, magenta);

    final protected static MappedColor printMagenta = new MappedColor(255, 0,
            255, magenta);

    final protected static MappedColor grayMagenta = new MappedColor(0, 0, 0,
            magenta);

    final protected static MappedColor bwMagenta = new MappedColor(0, 0, 0,
            magenta);

    final protected static MappedColor displayYellow = new MappedColor(255,
            255, 0, yellow);

    final protected static MappedColor printYellow = new MappedColor(255, 255,
            0, yellow);

    final protected static MappedColor grayYellow = new MappedColor(0, 0, 0,
            yellow);

    final protected static MappedColor bwYellow = new MappedColor(0, 0, 0,
            yellow);

    final protected static MappedColor displayOrange = new MappedColor(255,
            200, 0, orange);

    final protected static MappedColor printOrange = new MappedColor(255, 200,
            0, orange);

    final protected static MappedColor grayOrange = new MappedColor(0, 0, 0,
            orange);

    final protected static MappedColor bwOrange = new MappedColor(0, 0, 0,
            orange);

    final protected static MappedColor displayPink = new MappedColor(255, 175,
            175, pink);

    final protected static MappedColor printPink = new MappedColor(255, 175,
            175, pink);

    final protected static MappedColor grayPink = new MappedColor(0, 0, 0, pink);

    final protected static MappedColor bwPink = new MappedColor(0, 0, 0, pink);

    final protected static MappedColor displayWhite = new MappedColor(255, 255,
            255, white);

    final protected static MappedColor printWhite = new MappedColor(255, 255,
            255, white);

    final protected static MappedColor grayWhite = new MappedColor(0, 0, 0,
            white);

    final protected static MappedColor bwWhite = new MappedColor(0, 0, 0, white);

    final protected static MappedColor displayLightGray = new MappedColor(192,
            192, 192, lightGray);

    final protected static MappedColor printLightGray = new MappedColor(192,
            192, 192, lightGray);

    final protected static MappedColor grayLightGray = new MappedColor(0, 0, 0,
            lightGray);

    final protected static MappedColor bwLightGray = new MappedColor(0, 0, 0,
            lightGray);

    final protected static MappedColor displayGray = new MappedColor(128, 128,
            128, gray);

    final protected static MappedColor printGray = new MappedColor(128, 128,
            128, gray);

    final protected static MappedColor grayGray = new MappedColor(0, 0, 0, gray);

    final protected static MappedColor bwGray = new MappedColor(0, 0, 0, gray);

    final protected static MappedColor displayDarkGray = new MappedColor(64,
            64, 64, darkGray);

    final protected static MappedColor printDarkGray = new MappedColor(64, 64,
            64, darkGray);

    final protected static MappedColor grayDarkGray = new MappedColor(0, 0, 0,
            darkGray);

    final protected static MappedColor bwDarkGray = new MappedColor(0, 0, 0,
            darkGray);

    final protected static MappedColor displayBlack = new MappedColor(0, 0, 0,
            black);

    final protected static MappedColor printBlack = new MappedColor(0, 0, 0,
            black);

    final protected static MappedColor grayBlack = new MappedColor(0, 0, 0,
            black);

    final protected static MappedColor bwBlack = new MappedColor(0, 0, 0, black);

    // The "fill" colors. These retain their colors under the display
    // and print color maps. Under the grayscale color map, these
    // become light shades of gray. These become white under the b&w
    // color map.
    final protected static MappedColor displayRedAsGray = new MappedColor(255,
            0, 0, redAsGray);

    final protected static MappedColor printRedAsGray = new MappedColor(255,
            84, 84, redAsGray);

    final protected static MappedColor grayRedAsGray = new MappedColor(192,
            192, 192, redAsGray);

    final protected static MappedColor bwRedAsGray = new MappedColor(255, 255,
            255, redAsGray);

    final protected static MappedColor displayGreenAsGray = new MappedColor(0,
            255, 0, greenAsGray);

    final protected static MappedColor printGreenAsGray = new MappedColor(84,
            255, 84, greenAsGray);

    final protected static MappedColor grayGreenAsGray = new MappedColor(255,
            255, 255, greenAsGray);

    final protected static MappedColor bwGreenAsGray = new MappedColor(255,
            255, 255, greenAsGray);

    final protected static MappedColor displayBlueAsGray = new MappedColor(0,
            0, 255, blueAsGray);

    final protected static MappedColor printBlueAsGray = new MappedColor(84,
            84, 255, blueAsGray);

    final protected static MappedColor grayBlueAsGray = new MappedColor(128,
            128, 128, blueAsGray);

    final protected static MappedColor bwBlueAsGray = new MappedColor(255, 255,
            255, blueAsGray);

    final protected static MappedColor displayCyanAsGray = new MappedColor(0,
            255, 255, cyanAsGray);

    final protected static MappedColor printCyanAsGray = new MappedColor(0,
            255, 255, cyanAsGray);

    final protected static MappedColor grayCyanAsGray = new MappedColor(192,
            192, 192, cyanAsGray);

    final protected static MappedColor bwCyanAsGray = new MappedColor(255, 255,
            255, cyanAsGray);

    final protected static MappedColor displayMagentaAsGray = new MappedColor(
            255, 0, 255, magentaAsGray);

    final protected static MappedColor printMagentaAsGray = new MappedColor(
            255, 0, 255, magentaAsGray);

    final protected static MappedColor grayMagentaAsGray = new MappedColor(128,
            128, 128, magentaAsGray);

    final protected static MappedColor bwMagentaAsGray = new MappedColor(255,
            255, 255, magentaAsGray);

    final protected static MappedColor displayYellowAsGray = new MappedColor(
            255, 255, 0, yellowAsGray);

    final protected static MappedColor printYellowAsGray = new MappedColor(255,
            255, 0, yellowAsGray);

    final protected static MappedColor grayYellowAsGray = new MappedColor(255,
            255, 255, yellowAsGray);

    final protected static MappedColor bwYellowAsGray = new MappedColor(255,
            255, 255, yellowAsGray);

    final protected static MappedColor displayOrangeAsGray = new MappedColor(
            255, 200, 0, orangeAsGray);

    final protected static MappedColor printOrangeAsGray = new MappedColor(255,
            200, 0, orangeAsGray);

    final protected static MappedColor grayOrangeAsGray = new MappedColor(192,
            192, 192, orangeAsGray);

    final protected static MappedColor bwOrangeAsGray = new MappedColor(255,
            255, 255, orangeAsGray);

    final protected static MappedColor displayPinkAsGray = new MappedColor(255,
            175, 175, pinkAsGray);

    final protected static MappedColor printPinkAsGray = new MappedColor(255,
            175, 175, pinkAsGray);

    final protected static MappedColor grayPinkAsGray = new MappedColor(255,
            255, 255, pinkAsGray);

    final protected static MappedColor bwPinkAsGray = new MappedColor(255, 255,
            255, pinkAsGray);

    final protected static MappedColor displayWhiteAsGray = new MappedColor(
            255, 255, 255, whiteAsGray);

    final protected static MappedColor printWhiteAsGray = new MappedColor(255,
            255, 255, whiteAsGray);

    final protected static MappedColor grayWhiteAsGray = new MappedColor(0, 0,
            0, whiteAsGray);

    final protected static MappedColor bwWhiteAsGray = new MappedColor(0, 0, 0,
            whiteAsGray);

    final protected static MappedColor displayLightGrayAsGray = new MappedColor(
            192, 192, 192, lightGrayAsGray);

    final protected static MappedColor printLightGrayAsGray = new MappedColor(
            192, 192, 192, lightGrayAsGray);

    final protected static MappedColor grayLightGrayAsGray = new MappedColor(
            64, 64, 64, lightGrayAsGray);

    final protected static MappedColor bwLightGrayAsGray = new MappedColor(255,
            255, 255, lightGrayAsGray);

    final protected static MappedColor displayGrayAsGray = new MappedColor(128,
            128, 128, grayAsGray);

    final protected static MappedColor printGrayAsGray = new MappedColor(128,
            128, 128, grayAsGray);

    final protected static MappedColor grayGrayAsGray = new MappedColor(128,
            128, 128, grayAsGray);

    final protected static MappedColor bwGrayAsGray = new MappedColor(255, 255,
            255, grayAsGray);

    final protected static MappedColor displayDarkGrayAsGray = new MappedColor(
            64, 64, 64, darkGrayAsGray);

    final protected static MappedColor printDarkGrayAsGray = new MappedColor(
            64, 64, 64, darkGrayAsGray);

    final protected static MappedColor grayDarkGrayAsGray = new MappedColor(
            192, 192, 192, darkGrayAsGray);

    final protected static MappedColor bwDarkGrayAsGray = new MappedColor(255,
            255, 255, darkGrayAsGray);

    final protected static MappedColor displayBlackAsGray = new MappedColor(0,
            0, 0, blackAsGray);

    final protected static MappedColor printBlackAsGray = new MappedColor(0, 0,
            0, blackAsGray);

    final protected static MappedColor grayBlackAsGray = new MappedColor(255,
            255, 255, blackAsGray);

    final protected static MappedColor bwBlackAsGray = new MappedColor(255,
            255, 255, blackAsGray);

    // The "background" colors. These retain their colors under the
    // display and print color maps. Under the grayscale color map
    // and the b&w color map these are white.
    final protected static MappedColor displayRedInvisible = new MappedColor(
            255, 0, 0, redInvisible);

    final protected static MappedColor printRedInvisible = new MappedColor(255,
            84, 84, redInvisible);

    final protected static MappedColor grayRedInvisible = new MappedColor(255,
            255, 255, redInvisible);

    final protected static MappedColor bwRedInvisible = new MappedColor(255,
            255, 255, redInvisible);

    final protected static MappedColor displayGreenInvisible = new MappedColor(
            0, 255, 0, greenInvisible);

    final protected static MappedColor printGreenInvisible = new MappedColor(
            84, 255, 84, greenInvisible);

    final protected static MappedColor grayGreenInvisible = new MappedColor(
            255, 255, 255, greenInvisible);

    final protected static MappedColor bwGreenInvisible = new MappedColor(255,
            255, 255, greenInvisible);

    final protected static MappedColor displayBlueInvisible = new MappedColor(
            0, 0, 255, blueInvisible);

    final protected static MappedColor printBlueInvisible = new MappedColor(84,
            84, 255, blueInvisible);

    final protected static MappedColor grayBlueInvisible = new MappedColor(255,
            255, 255, blueInvisible);

    final protected static MappedColor bwBlueInvisible = new MappedColor(255,
            255, 255, blueInvisible);

    final protected static MappedColor displayCyanInvisible = new MappedColor(
            0, 255, 255, cyanInvisible);

    final protected static MappedColor printCyanInvisible = new MappedColor(0,
            255, 255, cyanInvisible);

    final protected static MappedColor grayCyanInvisible = new MappedColor(255,
            255, 255, cyanInvisible);

    final protected static MappedColor bwCyanInvisible = new MappedColor(255,
            255, 255, cyanInvisible);

    final protected static MappedColor displayMagentaInvisible = new MappedColor(
            255, 0, 255, magentaInvisible);

    final protected static MappedColor printMagentaInvisible = new MappedColor(
            255, 0, 255, magentaInvisible);

    final protected static MappedColor grayMagentaInvisible = new MappedColor(
            255, 255, 255, magentaInvisible);

    final protected static MappedColor bwMagentaInvisible = new MappedColor(
            255, 255, 255, magentaInvisible);

    final protected static MappedColor displayYellowInvisible = new MappedColor(
            255, 255, 0, yellowInvisible);

    final protected static MappedColor printYellowInvisible = new MappedColor(
            255, 255, 0, yellowInvisible);

    final protected static MappedColor grayYellowInvisible = new MappedColor(
            255, 255, 255, yellowInvisible);

    final protected static MappedColor bwYellowInvisible = new MappedColor(255,
            255, 255, yellowInvisible);

    final protected static MappedColor displayOrangeInvisible = new MappedColor(
            255, 200, 0, orangeInvisible);

    final protected static MappedColor printOrangeInvisible = new MappedColor(
            255, 200, 0, orangeInvisible);

    final protected static MappedColor grayOrangeInvisible = new MappedColor(
            255, 255, 255, orangeInvisible);

    final protected static MappedColor bwOrangeInvisible = new MappedColor(255,
            255, 255, orangeInvisible);

    final protected static MappedColor displayPinkInvisible = new MappedColor(
            255, 175, 175, pinkInvisible);

    final protected static MappedColor printPinkInvisible = new MappedColor(
            255, 175, 175, pinkInvisible);

    final protected static MappedColor grayPinkInvisible = new MappedColor(255,
            255, 255, pinkInvisible);

    final protected static MappedColor bwPinkInvisible = new MappedColor(255,
            255, 255, pinkInvisible);

    final protected static MappedColor displayWhiteInvisible = new MappedColor(
            255, 255, 255, whiteInvisible);

    final protected static MappedColor printWhiteInvisible = new MappedColor(
            255, 255, 255, whiteInvisible);

    final protected static MappedColor grayWhiteInvisible = new MappedColor(
            255, 255, 255, whiteInvisible);

    final protected static MappedColor bwWhiteInvisible = new MappedColor(255,
            255, 255, whiteInvisible);

    final protected static MappedColor displayLightGrayInvisible = new MappedColor(
            192, 192, 192, lightGrayInvisible);

    final protected static MappedColor printLightGrayInvisible = new MappedColor(
            192, 192, 192, lightGrayInvisible);

    final protected static MappedColor grayLightGrayInvisible = new MappedColor(
            255, 255, 255, lightGrayInvisible);

    final protected static MappedColor bwLightGrayInvisible = new MappedColor(
            255, 255, 255, lightGrayInvisible);

    final protected static MappedColor displayGrayInvisible = new MappedColor(
            128, 128, 128, grayInvisible);

    final protected static MappedColor printGrayInvisible = new MappedColor(
            128, 128, 128, grayInvisible);

    final protected static MappedColor grayGrayInvisible = new MappedColor(255,
            255, 255, grayInvisible);

    final protected static MappedColor bwGrayInvisible = new MappedColor(255,
            255, 255, grayInvisible);

    final protected static MappedColor displayDarkGrayInvisible = new MappedColor(
            64, 64, 64, darkGrayInvisible);

    final protected static MappedColor printDarkGrayInvisible = new MappedColor(
            64, 64, 64, darkGrayInvisible);

    final protected static MappedColor grayDarkGrayInvisible = new MappedColor(
            255, 255, 255, darkGrayInvisible);

    final protected static MappedColor bwDarkGrayInvisible = new MappedColor(
            255, 255, 255, darkGrayInvisible);

    final protected static MappedColor displayBlackInvisible = new MappedColor(
            0, 0, 0, blackInvisible);

    final protected static MappedColor printBlackInvisible = new MappedColor(0,
            0, 0, blackInvisible);

    final protected static MappedColor grayBlackInvisible = new MappedColor(
            255, 255, 255, blackInvisible);

    final protected static MappedColor bwBlackInvisible = new MappedColor(255,
            255, 255, blackInvisible);

    // Now define the display color map.
    final protected static MappedColor[] displayMap = { displayRed,
            displayGreen, displayBlue, displayCyan, displayMagenta,
            displayYellow, displayOrange, displayPink, displayWhite,
            displayLightGray, displayGray, displayDarkGray, displayBlack,
            displayRedAsGray, displayGreenAsGray, displayBlueAsGray,
            displayCyanAsGray, displayMagentaAsGray, displayYellowAsGray,
            displayOrangeAsGray, displayPinkAsGray, displayWhiteAsGray,
            displayLightGrayAsGray, displayGrayAsGray, displayDarkGrayAsGray,
            displayBlackAsGray, displayRedInvisible, displayGreenInvisible,
            displayBlueInvisible, displayCyanInvisible,
            displayMagentaInvisible, displayYellowInvisible,
            displayOrangeInvisible, displayPinkInvisible,
            displayWhiteInvisible, displayLightGrayInvisible,
            displayGrayInvisible, displayDarkGrayInvisible,
            displayBlackInvisible };

    // Now define the print color map.
    final protected static MappedColor[] printMap = { printRed, printGreen,
            printBlue, printCyan, printMagenta, printYellow, printOrange,
            printPink, printWhite, printLightGray, printGray, printDarkGray,
            printBlack, printRedAsGray, printGreenAsGray, printBlueAsGray,
            printCyanAsGray, printMagentaAsGray, printYellowAsGray,
            printOrangeAsGray, printPinkAsGray, printWhiteAsGray,
            printLightGrayAsGray, printGrayAsGray, printDarkGrayAsGray,
            printBlackAsGray, printRedInvisible, printGreenInvisible,
            printBlueInvisible, printCyanInvisible, printMagentaInvisible,
            printYellowInvisible, printOrangeInvisible, printPinkInvisible,
            printWhiteInvisible, printLightGrayInvisible, printGrayInvisible,
            printDarkGrayInvisible, printBlackInvisible };

    // Now define the grayscale color map.
    final protected static MappedColor[] grayMap = { grayRed, grayGreen,
            grayBlue, grayCyan, grayMagenta, grayYellow, grayOrange, grayPink,
            grayWhite, grayLightGray, grayGray, grayDarkGray, grayBlack,
            grayRedAsGray, grayGreenAsGray, grayBlueAsGray, grayCyanAsGray,
            grayMagentaAsGray, grayYellowAsGray, grayOrangeAsGray,
            grayPinkAsGray, grayWhiteAsGray, grayLightGrayAsGray,
            grayGrayAsGray, grayDarkGrayAsGray, grayBlackAsGray,
            grayRedInvisible, grayGreenInvisible, grayBlueInvisible,
            grayCyanInvisible, grayMagentaInvisible, grayYellowInvisible,
            grayOrangeInvisible, grayPinkInvisible, grayWhiteInvisible,
            grayLightGrayInvisible, grayGrayInvisible, grayDarkGrayInvisible,
            grayBlackInvisible };

    // Now define the black&white color map.
    final protected static MappedColor[] bwMap = { bwRed, bwGreen, bwBlue,
            bwCyan, bwMagenta, bwYellow, bwOrange, bwPink, bwWhite,
            bwLightGray, bwGray, bwDarkGray, bwBlack, bwRedAsGray,
            bwGreenAsGray, bwBlueAsGray, bwCyanAsGray, bwMagentaAsGray,
            bwYellowAsGray, bwOrangeAsGray, bwPinkAsGray, bwWhiteAsGray,
            bwLightGrayAsGray, bwGrayAsGray, bwDarkGrayAsGray, bwBlackAsGray,
            bwRedInvisible, bwGreenInvisible, bwBlueInvisible, bwCyanInvisible,
            bwMagentaInvisible, bwYellowInvisible, bwOrangeInvisible,
            bwPinkInvisible, bwWhiteInvisible, bwLightGrayInvisible,
            bwGrayInvisible, bwDarkGrayInvisible, bwBlackInvisible };

    // The current map references one of the above color maps.
    protected MappedColor[] currentColorMap;

    /**
     * Constructor takes no arguments. The display color map is the default.
     */
    public ColorMap() {
        currentColorMap = displayMap;
    }

    /**
     * Change to the display color map.
     */
    public void useDisplayColorMap() {
        currentColorMap = displayMap;
    }

    /**
     * Change to the print color map.
     */
    public void usePrintColorMap() {
        currentColorMap = printMap;
    }

    /**
     * Change to the grayscale color map.
     */
    public void useGrayscaleColorMap() {
        currentColorMap = grayMap;
    }

    /**
     * Change to the black&white color map.
     */
    public void useBlackAndWhiteColorMap() {
        currentColorMap = bwMap;
    }

    /**
     * Retrieve a mapped color from the color map.
     */
    public MappedColor getMappedColor(int colorIndex) {
        if (colorIndex < 0 && colorIndex > 38) {
            return null;
        } else {
            return currentColorMap[colorIndex];
        }
    }

    /**
     * Retrieve a mapped color as a Java Color from the color map.
     */
    public Color getColor(int colorIndex) {
        if (colorIndex < 0 && colorIndex > 38) {
            return null;
        } else {
            return (Color) currentColorMap[colorIndex];
        }
    }

    /**
     * Get a terse tag which describes which color a particular color index
     * represents.
     */
    public static String getTag(int colorIndex) {
        if (colorIndex < 0 && colorIndex > 38) {
            return null;
        } else {
            return psColorTag[colorIndex];
        }
    }

}
