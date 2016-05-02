/*
 * EditedZoneCommand.cs
 * Copyright © 2010-2011 kbinani
 *
 * This file is part of org.kbinani.cadencii.
 *
 * org.kbinani.cadencii is free software; you can redistribute it and/or
 * modify it under the terms of the GPLv3 License.
 *
 * org.kbinani.cadencii is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.cadencii;

import java.util.*;


public class EditedZoneCommand {
    public Vector<EditedZoneUnit> mAdd;
    public Vector<EditedZoneUnit> mRemove;

    public EditedZoneCommand(int addStart, int addEnd) {
        this(new EditedZoneUnit[] { new EditedZoneUnit(addStart, addEnd) },
            new EditedZoneUnit[] {  });
    }

    public EditedZoneCommand(EditedZoneUnit[] add, EditedZoneUnit[] remove) {
        this.mAdd = new Vector<EditedZoneUnit>();

        for (int i = 0; i < add.length; i++) {
            this.mAdd.add((EditedZoneUnit) add[i].clone());
        }

        this.mRemove = new Vector<EditedZoneUnit>();

        for (int i = 0; i < remove.length; i++) {
            this.mRemove.add((EditedZoneUnit) remove[i].clone());
        }
    }

    public EditedZoneCommand(Vector<EditedZoneUnit> add,
        Vector<EditedZoneUnit> remove) {
        this(add.toArray(new EditedZoneUnit[] {  }),
            remove.toArray(new EditedZoneUnit[] {  }));
    }
}
