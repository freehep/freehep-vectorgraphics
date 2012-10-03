// Copyright 2003, FreeHEP.
package org.freehep.graphicsbase.swing.layout;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This layoutmanager uses all of the GridBagLayout's managers functionality
 * and power but allows you to set the options easily. It will construct
 * the GridBagConstraints object for you.
 *
 * To use it, set the layout manager for your Container to TableLayout and
 * add each component with a name string for which the format is specified
 * below:
 *
 * "gridx gridy gridwidth gridheight ( ipadx ipady ) [ top left bottom right ] { weightx weighty} options"
 *
 * you may use spaces or commas as separators.
 *
 * gridx and gridy are mandatory, can be * for RELATIVE
 * gridwidth and gridheight are optional, can be * for REMAINDER
 * ipadx and ipady are optional
 * insets (top...right) are optional
 * weights are optional and override any settings made to them by the options
 * options are optional
 *
 * Options may contain a set of characters which will set the corresponding
 * flag to true. If not set, the flag is false.
 *
 * "r" right align
 * "l" left align
 * "t" top align
 * "b" bottom align
 * if none of these are set the component is placed in the center.
 *
 * "w" resize both cell and component in width
 * "W" resize only cell in width
 * "h" resize both cell and component in height
 * "H" resize only cell in height
 * if none of these are set neither the component nor the cell is not resized.
 * and the extra space is put on the outside of the table.
 *
 * The weight is calculated as follows, if not set explicitly by the {weight} section:
 * 1 if character set, fill is set if lowercase.
 *
 * You may construct a TableLayout with some default name string.
 *
 * @author Mark Donszelmann
 * @version $Id: TableLayout.java 8584 2006-08-10 23:06:37Z duns $
 */
public class TableLayout extends GridBagLayout {

    // defaults are relative, single row and column, no padding or insets, center
    // resizing is done for both components and cells.
    public static final String NO_RESIZE        = "* * 1 1 (0, 0) [0, 0, 0, 0] {0.0, 0.0}";
    public static final String RESIZE_WIDTH     = "* * 1 1 (0, 0) [0, 0, 0, 0] {1.0, 0.0} w";
    public static final String RESIZE_HEIGHT    = "* * 1 1 (0, 0) [0, 0, 0, 0] {0.0, 1.0} h";
    public static final String RESIZE_BOTH      = "* * 1 1 (0, 0) [0, 0, 0, 0] {1.0, 1.0} wh";

    public static final String LEFT             = "0 * [5 15 5 15] r";
    public static final String RIGHT            = "1 * [5 15 5 15] lw";
    public static final String VERY_LEFT        = "0 * [5 0 5 15] r";
    public static final String VERY_RIGHT       = "1 * [5 15 5 0] lw";
    public static final String FULL             = "0 * * 1 [5 15 5 15] w";
    public static final String COLUMN           = "0 * wt";
    public static final String COLUMN_FILL      = "0 * wh";

    private GridBagConstraints defaults;

    public TableLayout() {
        this(NO_RESIZE);
    }

    public TableLayout(String defaultOptions) {
        super();
        defaults = getGridBagConstraints(defaultOptions);
        if (defaults == null) {
            usage();
            defaults = new GridBagConstraints();
            System.err.println("Using Default GridBagConstraints.");
        }
    }

    public void usage() {
         System.err.println("Usage: gridx gridy gridwidth gridheight ( ipadx ipady ) [ top left bottom right ] { weightx weighty} options");
         System.err.println();
         System.err.println("you may use spaces or commas as separators.");
         System.err.println();
         System.err.println("  gridx and gridy are mandatory, can be * for RELATIVE");
         System.err.println("  gridwidth and gridheight are optional, can be * for REMAINDER");
         System.err.println("  ipadx and ipady are optional");
         System.err.println("  insets (top...right) are optional");
         System.err.println("  weights are optional and override any settings made to them by the options");
         System.err.println("  options are optional");
         System.err.println();
         System.err.println("Options may contain a set of characters which will set the corresponding");
         System.err.println("flag to true. If not set, the flag is false.");
         System.err.println();
         System.err.println("  \"r\" right align");
         System.err.println("  \"l\" left align");
         System.err.println("  \"t\" top align");
         System.err.println("  \"b\" bottom align");
         System.err.println("if none of these are set the component is placed in the center.");
         System.err.println();
         System.err.println("  \"w\" resize both cell and component in width");
         System.err.println("  \"W\" resize only cell in width");
         System.err.println("  \"h\" resize both cell and component in height");
         System.err.println("  \"H\" resize only cell in height");
         System.err.println("if none of these are set neither the component nor the cell is not resized.");
         System.err.println("and the extra space is put on the outside of the table.");
         System.err.println();
         System.err.println("The weight is calculated as follows, if not set explicitly by the {weight} section:");
         System.err.println("1.0 if character set, fill is set if lowercase.");
         System.err.println();
    }

    public void addLayoutComponent(String name, Component component) {
        addLayoutComponent(component, name);
    }

    public void addLayoutComponent(Component component, Object constraints) {
        if (constraints instanceof String) {
            constraints = getGridBagConstraints((String)constraints);
            if (constraints == null) {
                usage();
                constraints = new GridBagConstraints();
                System.err.println("Using Default GridBagConstraints.");
            }
        }
        super.addLayoutComponent(component, constraints);
    }

    public String toString() {
        return "[TableLayout: "+super.toString()+"]";
    }

    protected GridBagConstraints getGridBagConstraints(String name) {
        return getGridBagConstraints(name, defaults);
    }

    public static GridBagConstraints getGridBagConstraints(String name, GridBagConstraints def) {

        // optional whitespace
        String ws = "\\s*";

        // whitespace or comma
        String s = "[\\s,]+";

        // integer
        String d = "[+-]?\\d+";

        // grouped integer
        String gd = "([+-]?\\d+)";

        // grouped row column span
        String gdstar = "((?:"+d+")|\\*)";

        // grouped float
        String gf = "([+-]?\\d*(?:\\.\\d*)?)";

        // gouped options
        String go = "([tlbrWwHh]*)";

        // gridx gridy gridwidth gridheight
        //  0     1      2       3
        String coordPattern     = ws+gdstar+s+gdstar+"(?:"+s+gdstar+s+gdstar+")?";

        // ( ipadx ipady )
        //      4   5
        String ipadPattern      = ws+"(?:\\("+ws+gd+s+gd+ws+"\\))?";

        // [ top left bottom right ]
        //    6     7   8    9
        String insetsPattern    = ws+"(?:\\["+ws+gd+s+gd+s+gd+s+gd+ws+"\\])?";

        // { weightx weigthy }
        //      10     11
        String weightPattern    = ws+"(?:\\{"+ws+gf+s+gf+ws+"\\})?";

        // options
        //  12
        String optionPattern    = ws+go;

        // rest
        String restPattern      = ws+"(.*)";

        Pattern pattern = Pattern.compile(coordPattern + ipadPattern + insetsPattern + weightPattern +optionPattern + restPattern);
        Matcher matcher = pattern.matcher(name);

        int nArgs = 14;
        if (!matcher.find()) {
            if (matcher.groupCount() != nArgs) {
                System.err.println("Expected "+nArgs+" arguments, but got "+matcher.groupCount()+" in '"+name+"'.");
            } else {
                System.err.println("Cannot parse '"+name+"'.");
            }
            return null;
        }

        try {
            // non optional
            int gridx       = getPosition(matcher.group(1));
            int gridy       = getPosition(matcher.group(2));

            // optional
            int gridwidth   = matcher.group(3) != null ?
                                getSpan(matcher.group(3)) :
                                def.gridwidth;
            int gridheight  = matcher.group(4) != null ?
                                getSpan(matcher.group(4)) :
                                def.gridheight;

            int ipadx       = matcher.group(5) != null ?
                                Integer.parseInt(matcher.group(5)) :
                                def.ipadx;
            int ipady       = matcher.group(6) != null ?
                                Integer.parseInt(matcher.group(6)) :
                                def.ipady;

            Insets insets;
            if (matcher.group(7) != null) {
                insets      = new Insets(Integer.parseInt(matcher.group(7)),
                                         Integer.parseInt(matcher.group(8)),
                                         Integer.parseInt(matcher.group(9)),
                                         Integer.parseInt(matcher.group(10)));
            } else {
                insets      = def.insets;
            }

            double weightx  = matcher.group(11) != null ?
                                Double.parseDouble(matcher.group(11)) :
                                def.weightx;
            double weighty  = matcher.group(12) != null ?
                                Double.parseDouble(matcher.group(12)) :
                                def.weighty;

            String options  = matcher.group(13);
            int anchor = (def != null) ? def.anchor : GridBagConstraints.CENTER;
            int fill   = (def != null) ? def.fill   : GridBagConstraints.NONE ;
            if (options != null) {
                int position = 0x00;
                if ((options.indexOf('l') >= 0) && (options.indexOf('r') < 0)) {
                    position |= 0x10;
                }

                if ((options.indexOf('r') >= 0) && (options.indexOf('l') < 0)) {
                    position |= 0x20;
                }

                if ((options.indexOf('t') >= 0) && (options.indexOf('b') < 0)) {
                    position |= 0x01;
                }

                if ((options.indexOf('b') >= 0) && (options.indexOf('t') < 0)) {
                    position |= 0x02;
                }

                switch(position) {
                    default:
                    case 0x00: break;
                    case 0x01: anchor = GridBagConstraints.NORTH;       break;
                    case 0x02: anchor = GridBagConstraints.SOUTH;       break;
                    case 0x10: anchor = GridBagConstraints.WEST;        break;
                    case 0x11: anchor = GridBagConstraints.NORTHWEST;   break;
                    case 0x12: anchor = GridBagConstraints.SOUTHWEST;   break;
                    case 0x20: anchor = GridBagConstraints.EAST;        break;
                    case 0x21: anchor = GridBagConstraints.NORTHEAST;   break;
                    case 0x22: anchor = GridBagConstraints.SOUTHEAST;   break;
                }

                boolean fillWidth, fillHeight;
                switch(fill) {
                    default:
                    case GridBagConstraints.NONE:       fillWidth = false; fillHeight = false; break;
                    case GridBagConstraints.HORIZONTAL: fillWidth = true;  fillHeight = false; break;
                    case GridBagConstraints.VERTICAL:   fillWidth = false; fillHeight = true;  break;
                    case GridBagConstraints.BOTH:       fillWidth = true;  fillHeight = true;  break;
                }

                if (options.indexOf('w') >= 0) {
                    fillWidth = true;
                    if (matcher.group(11) == null) {
                        weightx = 1.0;
                    }
                }

                if (options.indexOf('W') >= 0) {
                    if (matcher.group(11) == null) {
                        weightx = 1.0;
                    }
                }

                if (options.indexOf('h') >= 0) {
                    fillHeight = true;
                    if (matcher.group(12) == null) {
                        weighty = 1.0;
                    }
                }

                if (options.indexOf('H') >= 0) {
                    if (matcher.group(12) == null) {
                        weighty = 1.0;
                    }
                }

                if (fillWidth) {
                    if (fillHeight) {
                        fill = GridBagConstraints.BOTH;
                    } else {
                        fill = GridBagConstraints.HORIZONTAL;
                    }
                } else if (fillHeight) {
                    fill = GridBagConstraints.VERTICAL;
                }

                if ((matcher.group(14) != null) && !matcher.group(14).equals("")) {
                    System.err.println("Cannot parse: '"+matcher.group(14)+"' in '"+name+"'.");
                    return null;
                }
            }
            return new TableConstraints(
                gridx, gridy,
                gridwidth, gridheight,
                weightx, weighty,
                anchor, fill, insets,
                ipadx, ipady
            );
        } catch (NumberFormatException nfe) {
            System.err.println("Problem "+nfe.getMessage()+" in '"+name+"'.");
            return null;
        }
    }

    private static int getPosition(String position) {
        if (position.equals("*")) {
            return GridBagConstraints.RELATIVE;
        }

        return Integer.parseInt(position);
    }

    private static int getSpan(String span) {
        if (span.equals("*")) {
            return GridBagConstraints.REMAINDER;
        }

        return Integer.parseInt(span);
    }

    public static String toString(GridBagConstraints c) {
        StringBuffer s = new StringBuffer();
        s.append(c.gridx == TableConstraints.RELATIVE ? "*" : String.valueOf(c.gridx));
        s.append(" ");
        s.append(c.gridy == TableConstraints.RELATIVE ? "*" : String.valueOf(c.gridy));
        s.append(" ");
        s.append(c.gridwidth  == TableConstraints.REMAINDER ? "*" : String.valueOf(c.gridwidth));
        s.append(" ");
        s.append(c.gridheight == TableConstraints.REMAINDER ? "*" : String.valueOf(c.gridheight));
        s.append(" (");
        s.append(String.valueOf(c.ipadx));
        s.append(" ");
        s.append(String.valueOf(c.ipady));
        s.append(") [");
        s.append(String.valueOf(c.insets.top));
        s.append(" ");
        s.append(String.valueOf(c.insets.left));
        s.append(" ");
        s.append(String.valueOf(c.insets.bottom));
        s.append(" ");
        s.append(String.valueOf(c.insets.right));
        s.append("] {");
        s.append(String.valueOf(c.weightx));
        s.append(" ");
        s.append(String.valueOf(c.weighty));
        s.append("} ");
        switch (c.anchor) {
            default:                                             break;
            case GridBagConstraints.CENTER:                      break;
            case GridBagConstraints.NORTH:       s.append("t");  break;
            case GridBagConstraints.NORTHWEST:   s.append("tl"); break;
            case GridBagConstraints.WEST:        s.append("l");  break;
            case GridBagConstraints.SOUTHWEST:   s.append("bl"); break;
            case GridBagConstraints.SOUTH:       s.append("b");  break;
            case GridBagConstraints.SOUTHEAST:   s.append("br"); break;
            case GridBagConstraints.EAST:        s.append("r");  break;
            case GridBagConstraints.NORTHEAST:   s.append("tr"); break;
        }
        switch (c.fill) {
            default: break;
            case GridBagConstraints.NONE:
                if (c.weightx == 1.0) s.append("W");
                if (c.weighty == 1.0) s.append("H");
                break;
            case GridBagConstraints.HORIZONTAL:
                s.append("w");
                if (c.weighty == 1.0) s.append("H");
                break;
            case GridBagConstraints.VERTICAL:
                if (c.weightx == 1.0) s.append("W");
                s.append("h");
                break;
            case GridBagConstraints.BOTH:
                s.append("wh");
                break;
        }
        return s.toString();
    }

    public static class TableConstraints extends GridBagConstraints {

        public TableConstraints() {
            super();
        }

        public TableConstraints(int gridx, int gridy,
                                int gridwidth, int gridheight,
                                double weightx, double weighty,
                                int anchor, int fill, Insets insets,
                                int ipadx, int ipady) {
            super(gridx, gridy, gridwidth, gridheight, weightx, weighty,
                  anchor, fill, insets, ipadx, ipady);
        }

        public String toString() {
            return "TableConstraints: "+TableLayout.toString(this);
        }
    }

    public static void main(String args[]) {
        TableLayout layout = new TableLayout();
        System.out.println(layout.getGridBagConstraints("1 2 3 4 (5 6) [7 8 9 10] {11 12} tlwh"));
        System.out.println(layout.getGridBagConstraints("1 2 3 4 [7 8 9 10] {11 12} trWh"));
        System.out.println(layout.getGridBagConstraints("1 2 3 4 (5 6) {11 12} tlwH"));
        System.out.println(layout.getGridBagConstraints("1 2 3 4 (5 6) [7 8 9 10] brwh"));
        System.out.println(layout.getGridBagConstraints("1 2 3 4 (5 6) tlWh"));
        System.out.println(layout.getGridBagConstraints("1 2 3 4"));
        System.out.println(layout.getGridBagConstraints("1 2"));
        System.out.println(layout.getGridBagConstraints("* * 2 4"));
        System.out.println(layout.getGridBagConstraints("* 0 2 4"));
        System.out.println(layout.getGridBagConstraints("0 * * *"));
        System.out.println(layout.getGridBagConstraints("0 0 * 4"));
    }
}
