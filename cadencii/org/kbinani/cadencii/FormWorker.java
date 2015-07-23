/*
 * FormWorker.cs
 * Copyright © 2011 kbinani
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
import org.kbinani.*;


    class FormWorkerJobArgument
    {
        public Object invoker;
        public String name;
        public Object arguments;
        public int index;
        public WorkerState state;
    }


    class FormWorkerThread extends Thread
    {
        private BDelegate mDelegate = null;
        private FormWorkerJobArgument mArgument = null;
        private ProgressBarWithLabel mProgressBar;
        private double mJobAmount;
        private int mIndex;
        private FormWorker mControl;
        
        public FormWorkerThread( Object invoker, String method_name, FormWorkerJobArgument arg, ProgressBarWithLabel progress_bar, double job_amount, int index, FormWorker worker )
        {
try{
    mDelegate = new BDelegate( invoker, method_name, Void.TYPE, WorkerState.class, Object.class );
}catch( Exception ex ){
    Logger.write( FormWorkerThread.class + "..ctor; ex=" + ex + "\n" );
    mDelegate = null;
}
mArgument = arg;
mProgressBar = progress_bar;
mJobAmount = job_amount;
mControl = worker;
mIndex = index;
mArgument.state = new WorkerState(){
    private boolean mCancelRequested = false;
    private double mProcessedJob = 0.0;

    @Override
    public void reportProgress(double processed_job) {
        mProcessedJob = processed_job;
        int prog = (int)(processed_job / mJobAmount * 100.0);
        if( prog < 0 ) prog = 0;
        if( 100 < prog ) prog = 100;
        mProgressBar.setProgress( prog );
        mControl.workerProgressChanged( mIndex, prog );
    }

    @Override
    public void reportComplete() {
        mControl.workerCompleted( mIndex );
    }

    @Override
    public boolean isCancelRequested() {
        return mCancelRequested;
    }

    @Override
    public void requestCancel() {
        mCancelRequested = true;
    }

    @Override
    public double getProcessedAmount() {
        return mProcessedJob;
    }

    @Override
    public double getJobAmount() {
        return mJobAmount;
    }
};
        }
        
        public void run()
        {
if( mDelegate == null ) return;
try{
    mDelegate.invoke( mArgument.state, mArgument.arguments );
}catch( Exception ex ){
}
        }
    }

    /// <summary>
    /// 複数のジョブを順に実行し，その進捗状況を表示するダイアログを表示します
    /// </summary>
    public class FormWorker implements IFormWorkerControl
    {
        private FormWorkerUi ptrUi = null;
        private Vector<ProgressBarWithLabel> mLabels;
        private mman mMemManager;
        private Vector<FormWorkerJobArgument> mArguments;
        private Vector<FormWorkerThread> mThreads;

        /// <summary>
        /// コンストラクタ
        /// </summary>
        public FormWorker()
        {
mLabels = new Vector<ProgressBarWithLabel>();
mMemManager = new mman();
mArguments = new Vector<FormWorkerJobArgument>();

mThreads = new Vector<FormWorkerThread>();
        }

        /// <summary>
        /// 登録済みのジョブを開始します
        /// </summary>
        public void startJob()
        {
int size = vec.size( mLabels );
if ( size <= 0 ) return;
startWorker( 0 );
        }

        /// <summary>
        /// ビューのセットアップを行います
        /// </summary>
        /// <param name="value"></param>
        public void setupUi( FormWorkerUi value )
        {
ptrUi = value;
ptrUi.applyLanguage();
        }

        /// <summary>
        /// ジョブを追加します．objで指定したオブジェクトの，名前がnameであるメソッドを呼び出します．
        /// 当該メソッドは，戻り値は任意，引数は( WorkerState, Object )である必要があります．
        /// また，当該メソッドは第一引数で渡されたWorkerStateのインスタンスのisCancelRequestedメソッドを
        /// 監視し，その戻り値がtrueの場合速やかに処理を中止しなければなりません．その際，処理の中止後にreportCompleteの呼び出しを
        /// 行ってはいけません．
        /// </summary>
        /// <param name="obj">メソッドの呼び出し元となるオブジェクト</param>
        /// <param name="method_name">メソッドの名前</param>
        /// <param name="job_description">ジョブの概要</param>
        /// <param name="job_amount">ジョブの処理量を表す，何らかの量．</param>
        /// <param name="argument">メソッドの第二引数</param>
        public void addJob( Object obj, String method_name, String job_description, double job_amount, Object argument )
        {
// プログレスバーのUIを作成
ProgressBarWithLabelUi ui = new ProgressBarWithLabelUi();
ProgressBarWithLabel label = new ProgressBarWithLabel();
label.setupUi( ui );
label.setText( job_description );
// フォームのビューにUIを追加
ptrUi.addProgressBar( ui );

// ラベルのリストに登録
int index = vec.size( mLabels );
vec.add( mLabels, label );
mMemManager.add( label );

// スレッドを作成して起動(platform依存)
FormWorkerJobArgument arg = new FormWorkerJobArgument();
arg.invoker = obj;
arg.name = method_name;
arg.arguments = argument;
arg.index = index;
FormWorkerThread worker = 
    new FormWorkerThread(
        obj, method_name, arg, 
        label, job_amount, index, this );
vec.add( mThreads, worker );
vec.add( mArguments, arg );
        }

        /// <summary>
        /// ジョブをキャンセルします(非同期)
        /// </summary>
        public void cancelJobSlot()
        {
int size = vec.size( mArguments );
for ( int i = 0; i < size; i++ ) {
    FormWorkerJobArgument arg = vec.get( mArguments, i );
    arg.state.requestCancel();
}
        }

        /// <summary>
        /// ビューのインスタンスを取得します
        /// </summary>
        /// <returns></returns>
        public FormWorkerUi getUi()
        {
return ptrUi;
        }

        public void workerProgressChanged( int index, int percentage )
        {
ProgressBarWithLabel label = vec.get( mLabels, index );
if ( label != null ) {
    label.setProgress( percentage );
}
int size = vec.size( mArguments );
double total = 0.0;
double processed = 0.0;
for ( int i = 0; i < size; i++ ) {
    FormWorkerJobArgument arg = vec.get( mArguments, i );
    total += arg.state.getJobAmount();
    if ( i < index ) {
        processed += arg.state.getJobAmount();
    } else if ( i == index ) {
        processed += arg.state.getProcessedAmount();
    }
}
ptrUi.setTotalProgress( (int)(processed / total * 100.0) );
ptrUi.repaint();
        }

        public void workerCompleted( int index )
        {
ProgressBarWithLabel label = vec.get( mLabels, index );
ptrUi.removeProgressBar( label.getUi() );
mman.del( label );
vec.set( mLabels, index, null );
int size = vec.size( mLabels );
index++;
if ( index < size ) {
    startWorker( index );
} else {
    ptrUi.close( false );//.setDialogResult( BDialogResult.OK );//.close();
}
        }

        private void startWorker( int index )
        {
FormWorkerJobArgument arg = vec.get( mArguments, index );
vec.get( mThreads, index ).start();
        }

    }

