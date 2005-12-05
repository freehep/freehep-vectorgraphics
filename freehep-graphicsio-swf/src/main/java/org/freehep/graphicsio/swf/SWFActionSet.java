// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import org.freehep.util.io.ActionSet;

/**
 * Defines the actions for SWF.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFActionSet.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SWFActionSet extends ActionSet {

    public SWFActionSet(int version) {

        super();

        // Initialize actions
        // version 3
        if (version >= 3) {
            addAction(new SWFAction.GotoFrame());
            addAction(new SWFAction.GetURL());
            addAction(new SWFAction.NextFrame());
            addAction(new SWFAction.PreviousFrame());
            addAction(new SWFAction.Play());
            addAction(new SWFAction.Stop());
            addAction(new SWFAction.ToggleQuality());
            addAction(new SWFAction.StopSounds());
            addAction(new SWFAction.WaitForFrame());
            addAction(new SWFAction.SetTarget());
            addAction(new SWFAction.GotoLabel());
        }

        // version 4
        if (version >= 4) {
            addAction(new SWFAction.Add()); // 0x0a
            addAction(new SWFAction.Subtract()); // 0x0b
            addAction(new SWFAction.Multiply()); // 0x0c
            addAction(new SWFAction.Divide()); // 0x0d
            addAction(new SWFAction.WaitForFrame2()); // 0x8d
            addAction(new SWFAction.Equals()); // 0x0e
            addAction(new SWFAction.Less()); // 0x0f
            addAction(new SWFAction.And()); // 0x10
            addAction(new SWFAction.Or()); // 0x11
            addAction(new SWFAction.Not()); // 0x12
            addAction(new SWFAction.StringEquals()); // 0x13
            addAction(new SWFAction.StringLength()); // 0x14
            addAction(new SWFAction.StringExtract()); // 0x15
            addAction(new SWFAction.Push()); // 0x96
            addAction(new SWFAction.Pop()); // 0x17
            addAction(new SWFAction.ToInteger()); // 0x18
            addAction(new SWFAction.Jump()); // 0x99
            addAction(new SWFAction.GetURL2()); // 0x9a

            addAction(new SWFAction.GetVariable()); // 0x1c
            addAction(new SWFAction.SetVariable()); // 0x1d
            addAction(new SWFAction.If()); // 0x9d
            addAction(new SWFAction.Call()); // 0x9e
            addAction(new SWFAction.GotoFrame2()); // 0x9f
            addAction(new SWFAction.SetTarget2()); // 0x20
            addAction(new SWFAction.StringAdd()); // 0x21
            addAction(new SWFAction.GetProperty()); // 0x22
            addAction(new SWFAction.SetProperty()); // 0x23
            addAction(new SWFAction.CloneSprite()); // 0x24
            addAction(new SWFAction.RemoveSprite()); // 0x25
            addAction(new SWFAction.Trace()); // 0x26
            addAction(new SWFAction.StartDrag()); // 0x27
            addAction(new SWFAction.EndDrag()); // 0x28
            addAction(new SWFAction.StringLess()); // 0x29

            addAction(new SWFAction.RandomNumber()); // 0x30
            addAction(new SWFAction.MBStringLength()); // 0x31
            addAction(new SWFAction.CharToAscii()); // 0x32
            addAction(new SWFAction.AsciiToChar()); // 0x33
            addAction(new SWFAction.GetTime()); // 0x34
            addAction(new SWFAction.MBStringExtract()); // 0x35
            addAction(new SWFAction.MBCharToAscii()); // 0x36
            addAction(new SWFAction.MBAsciiToChar()); // 0x37
        }

        // version 5
        if (version >= 5) {
            addAction(new SWFAction.StoreRegister()); // 0x87
            addAction(new SWFAction.ConstantPool()); // 0x88

            addAction(new SWFAction.With()); // 0x94
            addAction(new SWFAction.DefineFunction()); // 0x9b

            addAction(new SWFAction.Delete()); // 0x3a
            addAction(new SWFAction.Delete2()); // 0x3b
            addAction(new SWFAction.DefineLocal()); // 0x3c
            addAction(new SWFAction.CallFunction()); // 0x3d
            addAction(new SWFAction.Return()); // 0x3e
            addAction(new SWFAction.Modulo()); // 0x3f

            addAction(new SWFAction.NewObject()); // 0x40
            addAction(new SWFAction.DefineLocal2()); // 0x41
            addAction(new SWFAction.InitArray()); // 0x42
            addAction(new SWFAction.InitObject()); // 0x43
            addAction(new SWFAction.TypeOf()); // 0x44
            addAction(new SWFAction.TargetPath()); // 0x45
            addAction(new SWFAction.Enumerate()); // 0x46
            addAction(new SWFAction.Add2()); // 0x47
            addAction(new SWFAction.Less2()); // 0x48
            addAction(new SWFAction.Equals2()); // 0x49
            addAction(new SWFAction.ToNumber()); // 0x4a
            addAction(new SWFAction.ToString()); // 0x4b
            addAction(new SWFAction.PushDuplicate()); // 0x4c
            addAction(new SWFAction.StackSwap()); // 0x4d
            addAction(new SWFAction.GetMember()); // 0x4e
            addAction(new SWFAction.SetMember()); // 0x4f

            addAction(new SWFAction.Increment()); // 0x50
            addAction(new SWFAction.Decrement()); // 0x51
            addAction(new SWFAction.CallMethod()); // 0x52
            addAction(new SWFAction.NewMethod()); // 0x53

            addAction(new SWFAction.BitAnd()); // 0x60
            addAction(new SWFAction.BitOr()); // 0x61
            addAction(new SWFAction.BitXor()); // 0x62
            addAction(new SWFAction.BitLShift()); // 0x63
            addAction(new SWFAction.BitRShift()); // 0x64
            addAction(new SWFAction.BitURShift()); // 0x65
        }

        // version 6
        if (version >= 6) {
            addAction(new SWFAction.InstanceOf()); // 0x54
            addAction(new SWFAction.Enumerate2()); // 0x55

            addAction(new SWFAction.StrictEquals()); // 0x66
            addAction(new SWFAction.Greater()); // 0x67
            addAction(new SWFAction.StringGreater()); // 0x68
        }
    }
}
