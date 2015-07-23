/*
 * MidiInDevice.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.media.
 *
 * org.kbinani.media is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.media is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.media;

import org.kbinani.*;


public class MidiInDevice implements javax.sound.midi.Receiver {
    private int m_port_number;
    private boolean mReceiveSystemCommonMessage = false;
    private boolean mReceiveSystemRealtimeMessage = false;
    private boolean mIsActive = false;
    public final BEvent<MidiReceivedEventHandler> midiReceivedEvent = new BEvent<MidiReceivedEventHandler>();

    public MidiInDevice(int port_number) {
        m_port_number = port_number;

        javax.sound.midi.MidiDevice.Info[] info = javax.sound.midi.MidiSystem.getMidiDeviceInfo();

        if ((port_number < 0) || (info.length <= port_number)) {
            System.err.println("MidiInDevice#.ctor; invalid port-number");

            return;
        }

        javax.sound.midi.Transmitter trans = null;
        javax.sound.midi.MidiDevice device = null;

        try {
            device = javax.sound.midi.MidiSystem.getMidiDevice(info[port_number]);
        } catch (Exception ex) {
            ex.printStackTrace();
            device = null;
        }

        if (device == null) {
            return;
        }

        int max = device.getMaxTransmitters();

        if ((max != -1) && (max <= 0)) {
            System.err.println("MidiInDevice#.ctor; cannot connect to device");

            return;
        }

        if (!device.isOpen()) {
            try {
                device.open();
            } catch (Exception ex) {
                ex.printStackTrace();

                return;
            }
        }

        try {
            trans = device.getTransmitter();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        trans.setReceiver(this);
    }

    public boolean isReceiveSystemRealtimeMessage() {
        return mReceiveSystemRealtimeMessage;
    }

    public void setReceiveSystemRealtimeMessage(boolean value) {
        mReceiveSystemRealtimeMessage = value;
    }

    public boolean isReceiveSystemCommonMessage() {
        return mReceiveSystemCommonMessage;
    }

    public void setReceiveSystemCommonMessage(boolean value) {
        mReceiveSystemCommonMessage = value;
    }

    public void start() {
        mIsActive = true;
    }

    public void stop() {
        mIsActive = false;
    }

    public void close() {
        mIsActive = false;
    }

    /*
            public static int getNumDevs() {
    #if JAVA
    return javax.sound.midi.MidiSystem.getMidiDeviceInfo().length;
    #else
    try {
        int i = (int)win32.midiInGetNumDevs();
        return i;
    } catch ( Exception ex ) {
        debug.push_log( "MidiInDevice.GetNumDevs" );
        debug.push_log( "    ex=" + ex );
    }
    return 0;
    #endif
            }
    */

    /*public static MIDIINCAPS[] GetMidiInDevices() {
    List<MIDIINCAPS> ret = new List<MIDIINCAPS>();
    uint num = 0;
    try {
    num = win32.midiInGetNumDevs();
    } catch {
    num = 0;
    }
    for ( uint i = 0; i < num; i++ ) {
    MIDIINCAPS m = new MIDIINCAPS();
    uint r = win32.midiInGetDevCaps( i, ref m, (uint)Marshal.SizeOf( m ) );
    #if DEBUG
    sout.println( "MidiInDevice#GetMidiDevices; #" + i + "; r=" + r + "; m=" + m );
    #endif
    ret.Add( m );
    }
    return ret.ToArray();
    }*/
    public void send(javax.sound.midi.MidiMessage message, long time) {
        if (!mIsActive) {
            return;
        }

        int status = message.getStatus();

        if (status >= 0xf8) {
            if (!mReceiveSystemRealtimeMessage) {
                return;
            }
        }

        if (status >= 0xf1) {
            if (!mReceiveSystemCommonMessage) {
                return;
            }
        }

        try {
            midiReceivedEvent.raise(this, message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        sout.println("MidiInDevice#send; message.getStatus()=0x" +
            PortUtil.toHexString(message.getStatus(), 2));
    }
}
