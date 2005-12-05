// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;

import org.freehep.util.io.Action;
import org.freehep.util.io.TaggedInputStream;
import org.freehep.util.io.TaggedOutputStream;

/**
 * SWF Abstract Action Class.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFAction.java db861da05344 2005/12/05 00:59:43 duns $
 */
public abstract class SWFAction extends Action {

    private int version;

    protected SWFAction(int code, int version) {
        super(code);
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public Action read(int actionCode, TaggedInputStream input, int length)
            throws IOException {

        return read(actionCode, (SWFInputStream) input, length);
    }

    public SWFAction read(int actionCode, SWFInputStream swf, int length)
            throws IOException {

        return this;
    }

    public void write(int actionCode, TaggedOutputStream input)
            throws IOException {

        write(actionCode, (SWFOutputStream) input);
    }

    public void write(int actionCode, SWFOutputStream swf) throws IOException {

        // empty
    }

    /**
     * Goto Frame Action.
     */
    public static class GotoFrame extends SWFAction {
        private int frame;

        public GotoFrame(int frame) {
            this();
            this.frame = frame;
        }

        public GotoFrame() {
            super(0x81, 3);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            GotoFrame action = new GotoFrame();
            action.frame = swf.readUnsignedShort();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeUnsignedShort(frame);
        }

        public String toString() {
            return super.toString() + ", frame " + frame;
        }
    }

    /**
     * Get URL Action.
     */
    public static class GetURL extends SWFAction {
        private String url;

        private String window;

        public GetURL(String url, String window) {
            this();
            this.url = url;
            this.window = window;
        }

        public GetURL() {
            super(0x83, 3);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            GetURL action = new GetURL();
            action.url = swf.readString();
            action.window = swf.readString();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeString(url);
            swf.writeString(window);
        }

        public String toString() {
            return super.toString() + ", URL " + url + ", window " + window;
        }
    }

    /**
     * Next Frame Action.
     */

    public static class NextFrame extends SWFAction {
        public NextFrame() {
            super(0x04, 3);
        }
    }

    /**
     * Previous Frame Action.
     */
    public static class PreviousFrame extends SWFAction {
        public PreviousFrame() {
            super(0x05, 3);
        }
    }

    /**
     * Play Action.
     */
    public static class Play extends SWFAction {
        public Play() {
            super(0x06, 3);
        }
    }

    /**
     * Stop Action.
     */
    public static class Stop extends SWFAction {
        public Stop() {
            super(0x07, 3);
        }
    }

    /**
     * Toggle Quality Action.
     */
    public static class ToggleQuality extends SWFAction {
        public ToggleQuality() {
            super(0x08, 3);
        }
    }

    /**
     * Stop Sounds Action.
     */
    public static class StopSounds extends SWFAction {
        public StopSounds() {
            super(0x09, 3);
        }
    }

    /**
     * Wait For Frame Action.
     */
    public static class WaitForFrame extends SWFAction {
        private int frame;

        private int skip;

        public WaitForFrame(int frame, int skip) {
            this();
            this.frame = frame;
            this.skip = skip;
        }

        public WaitForFrame() {
            super(0x8a, 3);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            WaitForFrame action = new WaitForFrame();
            action.frame = swf.readUnsignedShort();
            action.skip = swf.readUnsignedByte();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeUnsignedShort(frame);
            swf.writeUnsignedByte(skip);
        }

        public String toString() {
            return super.toString() + ", frame " + frame + ", skip " + skip;
        }
    }

    /**
     * Set Target Action.
     */
    public static class SetTarget extends SWFAction {
        private String target;

        public SetTarget(String target) {
            this();
            this.target = target;
        }

        public SetTarget() {
            super(0x8b, 3);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            SetTarget action = new SetTarget();
            action.target = swf.readString();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeString(target);
        }

        public String toString() {
            return super.toString() + ", target " + target;
        }
    }

    /**
     * Goto Label Action.
     */
    public static class GotoLabel extends SWFAction {
        private String label;

        public GotoLabel(String label) {
            this();
            this.label = label;
        }

        public GotoLabel() {
            super(0x8c, 3);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            GotoLabel action = new GotoLabel();
            action.label = swf.readString();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeString(label);
        }

        public String toString() {
            return super.toString() + ", label " + label;
        }
    }

    // Flash 4 Actions Set
    /**
     * Add Action.
     */
    public static class Add extends SWFAction {
        public Add() {
            super(0x0a, 4);
        }
    }

    /**
     * Subtract Action.
     */
    public static class Subtract extends SWFAction {
        public Subtract() {
            super(0x0b, 4);
        }
    }

    /**
     * Multiply Action.
     */
    public static class Multiply extends SWFAction {
        public Multiply() {
            super(0x0c, 4);
        }
    }

    /**
     * Divide Action.
     */
    public static class Divide extends SWFAction {
        public Divide() {
            super(0x0d, 4);
        }
    }

    /**
     * WaitForFrame2 Action.
     */
    public static class WaitForFrame2 extends SWFAction {
        private int skip;

        public WaitForFrame2(int skip) {
            this();
            this.skip = skip;
        }

        public WaitForFrame2() {
            super(0x8D, 4);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            WaitForFrame2 action = new WaitForFrame2();
            action.skip = swf.readUnsignedByte();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeUnsignedByte(skip);
        }

        public String toString() {
            return super.toString() + ", skipCount:" + skip;
        }
    }

    /**
     * Equals Action.
     */
    public static class Equals extends SWFAction {
        public Equals() {
            super(0x0e, 4);
        }
    }

    /**
     * Less Action.
     */
    public static class Less extends SWFAction {
        public Less() {
            super(0x0f, 4);
        }
    }

    /**
     * And Action.
     */
    public static class And extends SWFAction {
        public And() {
            super(0x10, 4);
        }
    }

    /**
     * Or Action.
     */
    public static class Or extends SWFAction {
        public Or() {
            super(0x11, 4);
        }
    }

    /**
     * Not Action.
     */
    public static class Not extends SWFAction {
        public Not() {
            super(0x12, 4);
        }
    }

    /**
     * StringEquals Action.
     */
    public static class StringEquals extends SWFAction {
        public StringEquals() {
            super(0x13, 4);
        }
    }

    /**
     * StringLength Action.
     */
    public static class StringLength extends SWFAction {
        public StringLength() {
            super(0x14, 4);
        }
    }

    /**
     * StringExtract Action.
     */
    public static class StringExtract extends SWFAction {
        public StringExtract() {
            super(0x15, 4);
        }
    }

    /**
     * Push Action.
     */
    public static class Push extends SWFAction {
        public static final int STRING = 0;

        public static final int FLOAT = 1;

        public static final int NULL = 2;

        public static final int UNDEFINED = 3;

        public static final int REGISTER = 4;

        public static final int BOOLEAN = 5;

        public static final int DOUBLE = 6;

        public static final int INTEGER = 7;

        public static final int LOOKUP = 8;

        public static final int LOOKUP2 = 9;

        private Vector values;

        public static class Value {
            private byte type;

            private Object value;

            private byte[] data;

            public Value(String s) {
                type = STRING;
                value = s;
            }

            public Value(float f) {
                type = FLOAT;
                value = new Float(f);
            }

            public Value(Object x) {
                type = NULL;
                value = null;
            }

            public Value(byte r) {
                type = REGISTER;
                value = new Byte(r);
            }

            public Value(boolean b) {
                type = BOOLEAN;
                value = new Boolean(b);
            }

            public Value(double d) {
                type = DOUBLE;
                value = new Double(d);
            }

            public Value(int i) {
                type = INTEGER;
                value = new Integer(i);
            }

            public Value(short index) {
                type = LOOKUP;
                value = new Short(index);
            }

            public Value(byte type, byte[] data) {
                this.type = type;
                this.data = data;
            }

            public static Value read(SWFInputStream swf) throws IOException {
                byte type = swf.readByte();
                switch (type) {
                case STRING:
                    return new Value(swf.readString());
                case FLOAT:
                    return new Value(swf.readFloat());
                case NULL:
                    return new Value(null);
                case REGISTER:
                    return new Value((byte) swf.readUnsignedByte());
                case BOOLEAN:
                    return new Value((swf.readByte() != 0) ? true : false);
                case DOUBLE:
                    return new Value(swf.readDouble());
                case INTEGER:
                    return new Value(swf.readInt());
                case LOOKUP:
                    return new Value((short) swf.readUnsignedByte());
                case LOOKUP2:
                    return new Value((byte) LOOKUP2, swf.readByte(2));
                default:
                    return new Value(type, swf.readByte((int) swf.getLength()));
                }
            }

            public void write(SWFOutputStream swf) throws IOException {
                swf.writeByte(type);
                switch (type) {
                case STRING:
                    swf.writeString((String) value);
                    break;
                case FLOAT:
                    swf.writeFloat(((Float) value).floatValue());
                    break;
                case NULL:
                    break;
                case UNDEFINED:
                    break;
                case REGISTER:
                    swf.writeUnsignedByte(((Byte) value).byteValue());
                    break;
                case BOOLEAN:
                    swf.writeBoolean(((Boolean) value).booleanValue());
                    break;
                case DOUBLE:
                    swf.writeDouble(((Double) value).doubleValue());
                    break;
                case INTEGER:
                    swf.writeInt(((Integer) value).intValue());
                    break;
                case LOOKUP:
                    swf.writeUnsignedByte(((Short) value).shortValue());
                    break;
                case LOOKUP2:
                    swf.writeShort(((Short) value).shortValue());
                    break;
                default:
                    swf.writeByte(data);
                    break;
                }
            }

            public String toString() {
                StringBuffer s = new StringBuffer("PushValue ");
                if ((type < 0) || (type > 9)) {
                    s.append("Unknown Data Type " + type + " with length "
                            + data.length);
                } else {
                    s.append(value);
                }
                return s.toString();
            }
        }

        public Push() {
            super(0x96, 4);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            Push action = new Push();
            action.values = new Vector();

            try {
                while (true) {
                    action.values.add(Value.read(swf));
                }
            } catch (EOFException e) {
            }
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            for (int i = 0; i < values.size(); i++) {
                ((Value) values.get(i)).write(swf);
            }
        }

        public String toString() {
            StringBuffer s = new StringBuffer(super.toString());
            s.append("\n");
            for (int i = 0; i < values.size(); i++) {
                s.append("   ");
                s.append(values.get(i));
                s.append("\n");
            }
            return s.toString();
        }
    }

    /**
     * Pop Action.
     */
    public static class Pop extends SWFAction {
        public Pop() {
            super(0x17, 4);
        }
    }

    /**
     * ToInteger Action.
     */
    public static class ToInteger extends SWFAction {
        public ToInteger() {
            super(0x18, 4);
        }
    }

    /**
     * Jump Action.
     */
    public static class Jump extends SWFAction {
        private short offset;

        public Jump(short offset) {
            this();
            this.offset = offset;
        }

        public Jump() {
            super(0x99, 4);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            Jump action = new Jump();
            action.offset = swf.readShort();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeShort(offset);
        }

        public String toString() {
            return super.toString() + ", " + offset;
        }
    }

    /**
     * GetURL2 Action.
     */
    public static class GetURL2 extends SWFAction {
        private byte method;

        public static final int NONE = 0;

        public static final int GET = 1;

        public static final int POST = 2;

        public GetURL2(byte method) {
            this();
            this.method = method;
        }

        public GetURL2() {
            super(0x9A, 4);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            GetURL2 action = new GetURL2();
            action.method = swf.readByte();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeByte(method);
        }

        public String toString() {
            return super.toString() + ", " + method;
        }
    }

    /**
     * GetVariable Action.
     */
    public static class GetVariable extends SWFAction {
        public GetVariable() {
            super(0x1c, 4);
        }
    }

    /**
     * SetVariable Action.
     */
    public static class SetVariable extends SWFAction {
        public SetVariable() {
            super(0x1d, 4);
        }
    }

    /**
     * If Action.
     */
    public static class If extends SWFAction {
        private short offset;

        public If(short offset) {
            this();
            this.offset = offset;
        }

        public If() {
            super(0x9d, 4);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            If action = new If();
            action.offset = swf.readShort();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeShort(offset);
        }

        public String toString() {
            return super.toString() + ", " + offset;
        }
    }

    /**
     * Call Action.
     */
    public static class Call extends SWFAction {
        public Call() {
            super(0x9e, 4);
        }
    }

    /**
     * GotoFrame2 Action.
     */
    public static class GotoFrame2 extends SWFAction {
        private byte play;

        public GotoFrame2(byte play) {
            this();
            this.play = play;
        }

        public GotoFrame2() {
            super(0x9f, 4);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            GotoFrame2 action = new GotoFrame2();
            action.play = swf.readByte();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeByte(play);
        }

        public String toString() {
            return super.toString() + ", " + play;
        }
    }

    /**
     * SetTarget2 Action.
     */
    public static class SetTarget2 extends SWFAction {
        public SetTarget2() {
            super(0x20, 4);
        }
    }

    /**
     * StringAdd Action.
     */
    public static class StringAdd extends SWFAction {
        public StringAdd() {
            super(0x21, 4);
        }
    }

    /**
     * GetProperty Action.
     */
    public static class GetProperty extends SWFAction {
        public GetProperty() {
            super(0x22, 4);
        }
    }

    /**
     * SetProperty Action.
     */
    public static class SetProperty extends SWFAction {
        public SetProperty() {
            super(0x23, 4);
        }
    }

    /**
     * CloneSprite Action.
     */
    public static class CloneSprite extends SWFAction {
        public CloneSprite() {
            super(0x24, 4);
        }
    }

    /**
     * RemoveSprite Action.
     */
    public static class RemoveSprite extends SWFAction {
        public RemoveSprite() {
            super(0x25, 4);
        }
    }

    /**
     * Trace Action.
     */
    public static class Trace extends SWFAction {
        public Trace() {
            super(0x26, 4);
        }
    }

    /**
     * StartDrag Action.
     */
    public static class StartDrag extends SWFAction {
        public StartDrag() {
            super(0x27, 4);
        }
    }

    /**
     * EndDrag Action.
     */
    public static class EndDrag extends SWFAction {
        public EndDrag() {
            super(0x28, 4);
        }
    }

    /**
     * StringLess Action.
     */
    public static class StringLess extends SWFAction {
        public StringLess() {
            super(0x29, 4);
        }
    }

    /**
     * RandomNumber Action.
     */
    public static class RandomNumber extends SWFAction {
        public RandomNumber() {
            super(0x30, 4);
        }
    }

    /**
     * MBStringLength Action.
     */
    public static class MBStringLength extends SWFAction {
        public MBStringLength() {
            super(0x31, 4);
        }
    }

    /**
     * CharToAscii Action.
     */
    public static class CharToAscii extends SWFAction {
        public CharToAscii() {
            super(0x32, 4);
        }
    }

    /**
     * AsciiToChar Action.
     */
    public static class AsciiToChar extends SWFAction {
        public AsciiToChar() {
            super(0x33, 4);
        }
    }

    /**
     * GetTime Action.
     */
    public static class GetTime extends SWFAction {
        public GetTime() {
            super(0x34, 4);
        }
    }

    /**
     * MBStringExtract Action.
     */
    public static class MBStringExtract extends SWFAction {
        public MBStringExtract() {
            super(0x35, 4);
        }
    }

    /**
     * MBCharToAscii Action.
     */
    public static class MBCharToAscii extends SWFAction {
        public MBCharToAscii() {
            super(0x36, 4);
        }
    }

    /**
     * MBAsciiToChar Action.
     */
    public static class MBAsciiToChar extends SWFAction {
        public MBAsciiToChar() {
            super(0x37, 4);
        }
    }

    // Flash 5 actions.
    /**
     * StoreRegister Action.
     */
    public static class StoreRegister extends SWFAction {
        private byte number;

        public StoreRegister(byte number) {
            this();
            this.number = number;
        }

        public StoreRegister() {
            super(0x87, 5);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            StoreRegister action = new StoreRegister();
            action.number = swf.readByte();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeByte(number);
        }

        public String toString() {
            return super.toString() + ", " + number;
        }
    }

    /**
     * ConstantPool Action.
     */
    public static class ConstantPool extends SWFAction {
        private String[] pool;

        public ConstantPool(String[] pool) {
            this();
            this.pool = pool;
        }

        public ConstantPool() {
            super(0x88, 5);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            ConstantPool action = new ConstantPool();
            action.pool = new String[swf.readUnsignedShort()];
            for (int i = 0; i < action.pool.length; i++) {
                action.pool[i] = swf.readString();
            }
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeUnsignedShort(pool.length);
            for (int i = 0; i < pool.length; i++) {
                swf.writeString(pool[i]);
            }
        }

        public String toString() {
            StringBuffer s = new StringBuffer(super.toString());
            s.append("[");
            for (int i = 0; i < pool.length; i++) {
                if (i != 0)
                    s.append(", ");
                s.append(pool[i]);
            }
            s.append("]");
            return s.toString();
        }
    }

    /**
     * With Action.
     */
    public static class With extends SWFAction {
        private int size;

        private String block;

        public With(int size, String block) {
            this();
            this.size = size;
            this.block = block;
        }

        public With() {
            super(0x94, 5);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            With action = new With();
            action.size = swf.readUnsignedShort();
            action.block = swf.readString();
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeUnsignedShort(size);
            swf.writeString(block);
        }

        public String toString() {
            return super.toString() + ", " + size + ", " + block;
        }
    }

    /**
     * DefineFunction Action.
     */
    public static class DefineFunction extends SWFAction {
        private String name;

        private String[] params;

        private byte[] code;

        public DefineFunction(String name, String[] params, byte[] code) {
            this();
            this.name = name;
            this.params = params;
            this.code = code;
        }

        public DefineFunction() {
            super(0x9b, 5);
        }

        public SWFAction read(int actionCode, SWFInputStream swf, int length)
                throws IOException {

            DefineFunction action = new DefineFunction();
            action.name = swf.readString();
            int n = swf.readUnsignedShort();
            for (int i = 0; i < n; i++) {
                action.params[i] = swf.readString();
            }
            int cs = swf.readUnsignedShort();
            action.code = swf.readByte(cs);
            return action;
        }

        public void write(int actionCode, SWFOutputStream swf)
                throws IOException {

            swf.writeString(name);
            swf.writeUnsignedShort(params.length);
            for (int i = 0; i < params.length; i++) {
                swf.writeString(params[i]);
            }
            swf.writeUnsignedShort(code.length);
            swf.writeByte(code);
        }

        public String toString() {
            return super.toString() + ", " + name + ", " + (new String(code));
        }
    }

    /**
     * Delete Action.
     */
    public static class Delete extends SWFAction {
        public Delete() {
            super(0x3a, 5);
        }
    }

    /**
     * Delete2 Action.
     */
    public static class Delete2 extends SWFAction {
        public Delete2() {
            super(0x3b, 5);
        }
    }

    /**
     * DefineLocal Action.
     */
    public static class DefineLocal extends SWFAction {
        public DefineLocal() {
            super(0x3c, 5);
        }
    }

    /**
     * CallFunction Action.
     */
    public static class CallFunction extends SWFAction {
        public CallFunction() {
            super(0x3d, 5);
        }
    }

    /**
     * Return Action.
     */
    public static class Return extends SWFAction {
        public Return() {
            super(0x3e, 5);
        }
    }

    /**
     * Modulo Action.
     */
    public static class Modulo extends SWFAction {
        public Modulo() {
            super(0x3f, 5);
        }
    }

    /**
     * NewObject Action.
     */
    public static class NewObject extends SWFAction {
        public NewObject() {
            super(0x40, 5);
        }
    }

    /**
     * DefineLocal2 Action.
     */
    public static class DefineLocal2 extends SWFAction {
        public DefineLocal2() {
            super(0x41, 5);
        }
    }

    /**
     * InitArray Action.
     */
    public static class InitArray extends SWFAction {
        public InitArray() {
            super(0x42, 5);
        }
    }

    /**
     * InitObject Action.
     */
    public static class InitObject extends SWFAction {
        public InitObject() {
            super(0x43, 5);
        }
    }

    /**
     * TypeOf Action.
     */
    public static class TypeOf extends SWFAction {
        public TypeOf() {
            super(0x44, 5);
        }
    }

    /**
     * TargetPath Action.
     */
    public static class TargetPath extends SWFAction {
        public TargetPath() {
            super(0x45, 5);
        }
    }

    /**
     * Enumerate Action.
     */
    public static class Enumerate extends SWFAction {
        public Enumerate() {
            super(0x46, 5);
        }
    }

    /**
     * Add2 Action.
     */
    public static class Add2 extends SWFAction {
        public Add2() {
            super(0x47, 5);
        }
    }

    /**
     * Less2 Action.
     */
    public static class Less2 extends SWFAction {
        public Less2() {
            super(0x48, 5);
        }
    }

    /**
     * Equals2 Action.
     */
    public static class Equals2 extends SWFAction {
        public Equals2() {
            super(0x49, 5);
        }
    }

    /**
     * ToNumber Action.
     */
    public static class ToNumber extends SWFAction {
        public ToNumber() {
            super(0x4a, 5);
        }
    }

    /**
     * ToString Action.
     */
    public static class ToString extends SWFAction {
        public ToString() {
            super(0x4b, 5);
        }
    }

    /**
     * PushDuplicate Action.
     */
    public static class PushDuplicate extends SWFAction {
        public PushDuplicate() {
            super(0x4c, 5);
        }
    }

    /**
     * StackSwap Action.
     */
    public static class StackSwap extends SWFAction {
        public StackSwap() {
            super(0x4d, 5);
        }
    }

    /**
     * GetMember Action.
     */
    public static class GetMember extends SWFAction {
        public GetMember() {
            super(0x4e, 5);
        }
    }

    /**
     * SetMember Action.
     */
    public static class SetMember extends SWFAction {
        public SetMember() {
            super(0x4f, 5);
        }
    }

    /**
     * Increment Action.
     */
    public static class Increment extends SWFAction {
        public Increment() {
            super(0x50, 5);
        }
    }

    /**
     * Decrement Action.
     */
    public static class Decrement extends SWFAction {
        public Decrement() {
            super(0x51, 5);
        }
    }

    /**
     * CallMethod Action.
     */
    public static class CallMethod extends SWFAction {
        public CallMethod() {
            super(0x52, 5);
        }
    }

    /**
     * NewMethod Action.
     */
    public static class NewMethod extends SWFAction {
        public NewMethod() {
            super(0x53, 5);
        }
    }

    /**
     * BitAnd Action.
     */
    public static class BitAnd extends SWFAction {
        public BitAnd() {
            super(0x60, 5);
        }
    }

    /**
     * BitOr Action.
     */
    public static class BitOr extends SWFAction {
        public BitOr() {
            super(0x61, 5);
        }
    }

    /**
     * BitXor Action.
     */
    public static class BitXor extends SWFAction {
        public BitXor() {
            super(0x62, 5);
        }
    }

    /**
     * BitLShift Action.
     */
    public static class BitLShift extends SWFAction {
        public BitLShift() {
            super(0x63, 5);
        }
    }

    /**
     * BitRShift Action.
     */
    public static class BitRShift extends SWFAction {
        public BitRShift() {
            super(0x64, 5);
        }
    }

    /**
     * BitURShift Action.
     */
    public static class BitURShift extends SWFAction {
        public BitURShift() {
            super(0x65, 5);
        }
    }

    /**
     * InstanceOf Action.
     */
    public static class InstanceOf extends SWFAction {
        public InstanceOf() {
            super(0x54, 6);
        }
    }

    /**
     * Enumerate2 Action.
     */
    public static class Enumerate2 extends SWFAction {
        public Enumerate2() {
            super(0x55, 6);
        }
    }

    /**
     * StrictEquals Action.
     */
    public static class StrictEquals extends SWFAction {
        public StrictEquals() {
            super(0x66, 6);
        }
    }

    /**
     * Greater Action.
     */
    public static class Greater extends SWFAction {
        public Greater() {
            super(0x67, 6);
        }
    }

    /**
     * StringGreater Action.
     */
    public static class StringGreater extends SWFAction {
        public StringGreater() {
            super(0x68, 6);
        }
    }
}
