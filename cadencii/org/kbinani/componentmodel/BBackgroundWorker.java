/*
 * BBackgroundWorker.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.
 *
 * org.kbinani is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.componentmodel;

import org.kbinani.BEvent;


public class BBackgroundWorker {
    public final BEvent<BDoWorkEventHandler> doWorkEvent = new BEvent<BDoWorkEventHandler>();
    public final BEvent<BProgressChangedEventHandler> progressChangedEvent = new BEvent<BProgressChangedEventHandler>();
    public final BEvent<BRunWorkerCompletedEventHandler> runWorkerCompletedEvent =
        new BEvent<BRunWorkerCompletedEventHandler>();
    private WorkerRunner mRunner = null;
    private Thread mThread = null;

    public BBackgroundWorker() {
    }

    public boolean isBusy() {
        if (mRunner == null) {
            return false;
        } else {
            return mRunner.mIsBusy;
        }
    }

    public void cancelAsync() {
        mThread.interrupt();
        System.err.println("info; BBackgroundWorker#cancelAsync");
    }

    public void runWorkerAsync() {
        runWorkerAsync(null);
    }

    public void runWorkerAsync(Object argument) {
        mRunner = new WorkerRunner(this, doWorkEvent, argument);
        mThread = new Thread(mRunner);
        mThread.start();
    }

    public void reportProgress(int percentProgress) {
        reportProgress(percentProgress, null);
    }

    public void reportProgress(int percentProgress, Object userState) {
        BProgressChangedEventArgs e = new BProgressChangedEventArgs(percentProgress,
                userState);

        try {
            progressChangedEvent.raise(this, e);
        } catch (Exception ex) {
            System.err.println(
                "BBackgroundWorker#reportProgress(int,Object); ex=" + ex);
        }
    }

    class WorkerRunner implements Runnable {
        private BDoWorkEventArgs mArg = null;
        private BEvent<BDoWorkEventHandler> mDelegate = null;
        private boolean mIsBusy = false;
        private BBackgroundWorker mParent = null;

        public WorkerRunner(BBackgroundWorker parent,
            BEvent<BDoWorkEventHandler> delegate, Object argument) {
            mParent = parent;
            mDelegate = delegate;
            mArg = new BDoWorkEventArgs(argument);
        }

        public void run() {
            mIsBusy = true;

            try {
                mDelegate.raise(mParent, mArg);

                BRunWorkerCompletedEventArgs e = new BRunWorkerCompletedEventArgs(null,
                        null, false);
                runWorkerCompletedEvent.raise(mParent, e);
            } catch (Exception ex) {
                System.err.println(
                    "BBackgroundWorker#WorkerRunner#run(void); ex=" + ex);
            }

            mIsBusy = false;
        }
    }
}
