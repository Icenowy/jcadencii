/*
 * FormGenerateKeySound.cs
 * Copyright © 2008-2011 kbinani
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

import org.kbinani.*;

import org.kbinani.componentmodel.*;

import org.kbinani.media.*;

import org.kbinani.vsq.*;

import org.kbinani.windows.forms.*;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BDialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.*;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FormGenerateKeySound extends BDialog {
    static final int _SAMPLE_RATE = 44100;
    private static final long serialVersionUID = 3420499863033740708L;
    private BFolderBrowser folderBrowser;
    private BBackgroundWorker bgWork;
    private SingerConfig[] m_singer_config1;
    private SingerConfig[] m_singer_config2;
    private SingerConfig[] m_singer_config_utau;
    private boolean m_cancel_required = false;

    /// <summary>
    /// 処理が終わったら自動でフォームを閉じるかどうか。デフォルトではfalse（閉じない）
    /// </summary>
    private boolean m_close_when_finished = false;
    private JPanel jPanel1 = null;
    private BButton btnCancel = null;
    private BButton btnExecute = null;
    private JPanel jPanel = null;
    private JPanel jPanel2 = null;
    private JLabel lblDir = null;
    private JTextField txtDir = null;
    private BButton btnBrowse = null;
    private JLabel lblSingingSynthSystem = null;
    private JComboBox comboSingingSynthSystem = null;
    private JLabel lblSinger = null;
    private JComboBox comboSinger = null;
    private JCheckBox chkIgnoreExistingWavs = null;

    public FormGenerateKeySound(boolean close_when_finished) {
        super();
        initialize();
        bgWork = new BBackgroundWorker();
        folderBrowser = new BFolderBrowser();

        m_close_when_finished = close_when_finished;
        m_singer_config1 = VocaloSysUtil.getSingerConfigs(SynthesizerType.VOCALOID1);
        m_singer_config2 = VocaloSysUtil.getSingerConfigs(SynthesizerType.VOCALOID2);
        m_singer_config_utau = AppManager.editorConfig.UtauSingers.toArray(new SingerConfig[] {
                    
                });

        if (m_singer_config1.length > 0) {
            comboSingingSynthSystem.addItem("VOCALOID1");
        }

        if (m_singer_config2.length > 0) {
            comboSingingSynthSystem.addItem("VOCALOID2");
        }

        // 取りあえず最初に登録されているresamplerを使うってことで
        String resampler = AppManager.editorConfig.getResamplerAt(0);

        if ((m_singer_config_utau.length > 0) &&
                (AppManager.editorConfig.PathWavtool != null) &&
                fsys.isFileExists(AppManager.editorConfig.PathWavtool) &&
                (resampler != null) && fsys.isFileExists(resampler)) {
            comboSingingSynthSystem.addItem("UTAU");
        }

        if (comboSingingSynthSystem.getItemCount() > 0) {
            comboSingingSynthSystem.setSelectedIndex(0);
        }

        updateSinger();
        txtDir.setText(Utility.getKeySoundPath());

        registerEventHandlers();
    }

    private void registerEventHandlers() {
        bgWork.doWorkEvent.add(new BDoWorkEventHandler(this, "bgWork_DoWork"));
        bgWork.runWorkerCompletedEvent.add(new BRunWorkerCompletedEventHandler(
                this, "bgWork_RunWorkerCompleted"));
        bgWork.progressChangedEvent.add(new BProgressChangedEventHandler(this,
                "bgWork_ProgressChanged"));
    }

    private void updateSinger() {
        if (comboSingingSynthSystem.getSelectedIndex() < 0) {
            return;
        }

        String singer = (String) comboSingingSynthSystem.getSelectedItem();
        SingerConfig[] list = null;

        if (singer.equals("VOCALOID1")) {
            list = m_singer_config1;
        } else if (singer.equals("VOCALOID2")) {
            list = m_singer_config2;
        } else if (singer.equals("UTAU")) {
            list = m_singer_config_utau;
        }

        comboSinger.removeAllItems();

        if (list == null) {
            return;
        }

        for (int i = 0; i < list.length; i++) {
            comboSinger.addItem(list[i].VOICENAME);
        }

        if (comboSinger.getItemCount() > 0) {
            comboSinger.setSelectedIndex(0);
        }
    }

    private void updateTitle(String title) {
        setTitle(title);
    }

    private void updateEnabled(boolean enabled) {
        comboSinger.setEnabled(enabled);
        comboSingingSynthSystem.setEnabled(enabled);
        txtDir.setEditable(enabled);
        btnBrowse.setEnabled(enabled);
        btnExecute.setEnabled(enabled);
        chkIgnoreExistingWavs.setEnabled(enabled);

        if (enabled) {
            btnCancel.setText("Close");
        } else {
            btnCancel.setText("Cancel");
        }
    }

    public void comboSingingSynthSystem_SelectedIndexChanged(Object sender,
        BEventArgs e) {
        updateSinger();
    }

    public void btnBrowse_Click(Object sender, BEventArgs e) {
        folderBrowser.setSelectedPath(txtDir.getText());

        if (folderBrowser.showDialog(this) != BDialogResult.OK) {
            return;
        }

        txtDir.setText(folderBrowser.getSelectedPath());
    }

    public void btnCancel_Click(Object sender, BEventArgs e) {
        if (bgWork.isBusy()) {
            m_cancel_required = true;

            while (m_cancel_required) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException ex) {
                    Logger.write(FormGenerateKeySound.class +
                        ".btnCancel_Click; ex=" + ex + "\n");

                    break;
                }
            }
        } else {
            this.close();
        }
    }

    public void btnExecute_Click(Object sender, BEventArgs e) {
        PrepareStartArgument arg = new PrepareStartArgument();
        arg.singer = (String) comboSinger.getSelectedItem();
        arg.amplitude = 1.0;
        arg.directory = txtDir.getText();
        arg.replace = chkIgnoreExistingWavs.isSelected();
        updateEnabled(false);
        bgWork.runWorkerAsync(arg);
    }

    public void bgWork_DoWork(Object sender, BDoWorkEventArgs e) {
        PrepareStartArgument arg = (PrepareStartArgument) e.Argument;
        String singer = arg.singer;
        double amp = arg.amplitude;
        String dir = arg.directory;
        boolean replace = arg.replace;

        // 音源を準備
        if (!fsys.isDirectoryExists(dir)) {
            PortUtil.createDirectory(dir);
        }

        for (int i = 0; i < 127; i++) {
            String path = fsys.combine(dir, i + ".wav");
            sout.println("writing \"" + path + "\" ...");

            if (replace || (!replace && !fsys.isFileExists(path))) {
                try {
                    GenerateSinglePhone(i, singer, path, amp);

                    if (fsys.isFileExists(path)) {
                        try {
                            Wave wv = new Wave(path);
                            wv.trimSilence();
                            wv.monoralize();
                            wv.write(path);
                        } catch (Exception ex0) {
                            serr.println(
                                "FormGenerateKeySound#bgWork_DoWork; ex0=" +
                                ex0);
                            Logger.write(FormGenerateKeySound.class +
                                ".bgWork_DoWork; ex=" + ex0 + "\n");
                        }
                    }
                } catch (Exception ex) {
                    Logger.write(FormGenerateKeySound.class +
                        ".bgWork_DoWork; ex=" + ex + "\n");
                    serr.println("FormGenerateKeySound#bgWork_DoWork; ex=" +
                        ex);
                }
            }

            sout.println(" done");

            if (m_cancel_required) {
                m_cancel_required = false;

                break;
            }

            bgWork.reportProgress((int) (i / 127.0 * 100.0));
        }

        m_cancel_required = false;
    }

    private void bgWork_ProgressChanged(Object sender,
        BProgressChangedEventArgs e) {
        String title = "Progress: " + e.ProgressPercentage + "%";
        updateTitle(title);
    }

    public void Program_FormClosed(Object sender, BFormClosedEventArgs e) {
        VSTiDllManager.terminate();
    }

    public void bgWork_RunWorkerCompleted(Object sender,
        BRunWorkerCompletedEventArgs e) {
        updateEnabled(true);

        if (m_close_when_finished) {
            close();
        }
    }

    public static void GenerateSinglePhone(int note, String singer,
        String file, double amp) {
        String renderer = "";
        SingerConfig[] singers1 = VocaloSysUtil.getSingerConfigs(SynthesizerType.VOCALOID1);
        int c = singers1.length;
        String first_found_singer = "";
        String first_found_renderer = "";

        for (int i = 0; i < c; i++) {
            if (first_found_singer.equals("")) {
                first_found_singer = singers1[i].VOICENAME;
                first_found_renderer = VSTiDllManager.RENDERER_DSB2;
            }

            if (singers1[i].VOICENAME.equals(singer)) {
                renderer = VSTiDllManager.RENDERER_DSB2;

                break;
            }
        }

        SingerConfig[] singers2 = VocaloSysUtil.getSingerConfigs(SynthesizerType.VOCALOID2);
        c = singers2.length;

        for (int i = 0; i < c; i++) {
            if (first_found_singer.equals("")) {
                first_found_singer = singers2[i].VOICENAME;
                first_found_renderer = VSTiDllManager.RENDERER_DSB3;
            }

            if (singers2[i].VOICENAME.equals(singer)) {
                renderer = VSTiDllManager.RENDERER_DSB3;

                break;
            }
        }

        for (Iterator<SingerConfig> itr = AppManager.editorConfig.UtauSingers.iterator();
                itr.hasNext();) {
            SingerConfig sc = itr.next();

            if (first_found_singer.equals("")) {
                first_found_singer = sc.VOICENAME;
                first_found_renderer = VSTiDllManager.RENDERER_UTU0;
            }

            if (sc.VOICENAME.equals(singer)) {
                renderer = VSTiDllManager.RENDERER_UTU0;

                break;
            }
        }

        VsqFileEx vsq = new VsqFileEx(singer, 1, 4, 4, 500000);

        if (renderer.equals("")) {
            singer = first_found_singer;
            renderer = first_found_renderer;
        }

        vsq.Track.get(1).getCommon().Version = renderer;

        VsqEvent item = new VsqEvent(1920, new VsqID(0));
        item.ID.LyricHandle = new LyricHandle("あ", "a");
        item.ID.setLength(480);
        item.ID.Note = note;
        item.ID.VibratoHandle = null;
        item.ID.type = VsqIDType.Anote;
        vsq.Track.get(1).addEvent(item);
        vsq.updateTotalClocks();

        int ms_presend = 500;
        String tempdir = fsys.combine(AppManager.getCadenciiTempDir(),
                AppManager.getID());

        if (!fsys.isDirectoryExists(tempdir)) {
            try {
                PortUtil.createDirectory(tempdir);
            } catch (Exception ex) {
                Logger.write(FormGenerateKeySound.class +
                    ".GenerateSinglePhone; ex=" + ex + "\n");
                serr.println("Program#GenerateSinglePhone; ex=" + ex);

                return;
            }
        }

        WaveWriter ww = null;

        try {
            ww = new WaveWriter(file);

            RendererKind kind = VsqFileEx.getTrackRendererKind(vsq.Track.get(1));
            WaveGenerator generator = VSTiDllManager.getWaveGenerator(kind);
            int sample_rate = vsq.config.SamplingRate;
            FileWaveReceiver receiver = new FileWaveReceiver(file, 1, 16,
                    sample_rate);
            generator.setReceiver(receiver);
            generator.setGlobalConfig(AppManager.editorConfig);
            generator.init(vsq, 1, 0, vsq.TotalClocks, sample_rate);

            double total_sec = vsq.getSecFromClock(vsq.TotalClocks) + 1.0;
            WorkerStateImp state = new WorkerStateImp();
            generator.begin((long) (total_sec * sample_rate), state);
        } catch (Exception ex) {
            serr.println("FormGenerateKeySound#GenerateSinglePhone; ex=" + ex);
            Logger.write(FormGenerateKeySound.class +
                ".GenerateSinglePhone; ex=" + ex + "\n");
        } finally {
            if (ww != null) {
                try {
                    ww.close();
                } catch (Exception ex2) {
                    Logger.write(FormGenerateKeySound.class +
                        ".GenerateSinglePhone; ex=" + ex2 + "\n");
                    serr.println(
                        "FormGenerateKeySound#GenerateSinglePhone; ex2=" + ex2);
                }
            }
        }
    }

    private void initialize() {
        this.setSize(new Dimension(382, 208));
        this.setContentPane(getJPanel());
        setCancelButton(btnCancel);
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.anchor = GridBagConstraints.WEST;
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.insets = new Insets(0, 0, 0, 12);

            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.anchor = GridBagConstraints.WEST;
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.insets = new Insets(0, 0, 0, 0);
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(getBtnCancel(), gridBagConstraints4);
            jPanel1.add(getBtnExecute(), gridBagConstraints5);
        }

        return jPanel1;
    }

    /**
     * This method initializes btnCancel
     *
     * @return org.kbinani.windows.forms.BButton
     */
    private BButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new BButton();
            btnCancel.setText("Close");
            btnCancel.setPreferredSize(new Dimension(100, 29));
        }

        return btnCancel;
    }

    /**
     * This method initializes btnExecute
     *
     * @return org.kbinani.windows.forms.BButton
     */
    private BButton getBtnExecute() {
        if (btnExecute == null) {
            btnExecute = new BButton();
            btnExecute.setText("Execute");
            btnExecute.setPreferredSize(new Dimension(100, 29));
        }

        return btnExecute;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.weighty = 1.0D;
            gridBagConstraints11.gridwidth = 2;
            gridBagConstraints11.anchor = GridBagConstraints.EAST;
            gridBagConstraints11.gridy = 4;

            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.weightx = 1.0D;
            gridBagConstraints10.gridwidth = 2;
            gridBagConstraints10.anchor = GridBagConstraints.WEST;
            gridBagConstraints10.insets = new Insets(6, 12, 6, 12);
            gridBagConstraints10.gridy = 2;

            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.weightx = 1.0D;
            gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.anchor = GridBagConstraints.WEST;
            gridBagConstraints9.gridwidth = 2;
            gridBagConstraints9.gridy = 3;

            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = GridBagConstraints.NONE;
            gridBagConstraints8.gridy = 1;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.anchor = GridBagConstraints.WEST;
            gridBagConstraints8.insets = new Insets(4, 12, 4, 12);
            gridBagConstraints8.gridx = 1;

            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.anchor = GridBagConstraints.WEST;
            gridBagConstraints7.insets = new Insets(6, 12, 6, 12);
            gridBagConstraints7.gridy = 1;
            lblSinger = new JLabel();
            lblSinger.setText("Singer");

            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.NONE;
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.insets = new Insets(12, 12, 4, 12);
            gridBagConstraints6.gridx = 1;

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.insets = new Insets(12, 12, 6, 12);
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            gridBagConstraints3.gridy = 0;
            lblSingingSynthSystem = new JLabel();
            lblSingingSynthSystem.setText("Singing Synth. System");
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(lblSingingSynthSystem, gridBagConstraints3);
            jPanel.add(getComboSingingSynthSystem(), gridBagConstraints6);
            jPanel.add(lblSinger, gridBagConstraints7);
            jPanel.add(getComboSinger(), gridBagConstraints8);
            jPanel.add(getJPanel2(), gridBagConstraints9);
            jPanel.add(getChkIgnoreExistingWavs(), gridBagConstraints10);
            jPanel.add(getJPanel1(), gridBagConstraints11);
        }

        return jPanel;
    }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 2;
            gridBagConstraints2.insets = new Insets(0, 0, 0, 12);
            gridBagConstraints2.gridy = 0;

            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new Insets(0, 12, 0, 12);
            gridBagConstraints1.gridx = 1;

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(0, 12, 0, 0);
            gridBagConstraints.gridy = 0;
            lblDir = new JLabel();
            lblDir.setText("Output Path");
            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.add(lblDir, gridBagConstraints);
            jPanel2.add(getTxtDir(), gridBagConstraints1);
            jPanel2.add(getBtnBrowse(), gridBagConstraints2);
        }

        return jPanel2;
    }

    /**
     * This method initializes txtDir
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTxtDir() {
        if (txtDir == null) {
            txtDir = new JTextField();
        }

        return txtDir;
    }

    /**
     * This method initializes btnBrowse
     *
     * @return org.kbinani.windows.forms.BButton
     */
    private BButton getBtnBrowse() {
        if (btnBrowse == null) {
            btnBrowse = new BButton();
            btnBrowse.setPreferredSize(new Dimension(41, 23));
            btnBrowse.setText("...");
        }

        return btnBrowse;
    }

    /**
     * This method initializes comboSingingSynthSystem
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getComboSingingSynthSystem() {
        if (comboSingingSynthSystem == null) {
            comboSingingSynthSystem = new JComboBox();
            comboSingingSynthSystem.setPreferredSize(new Dimension(121, 27));
        }

        return comboSingingSynthSystem;
    }

    /**
     * This method initializes comboSinger
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getComboSinger() {
        if (comboSinger == null) {
            comboSinger = new JComboBox();
            comboSinger.setPreferredSize(new Dimension(121, 27));
        }

        return comboSinger;
    }

    /**
     * This method initializes chkIgnoreExistingWavs
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getChkIgnoreExistingWavs() {
        if (chkIgnoreExistingWavs == null) {
            chkIgnoreExistingWavs = new JCheckBox();
            chkIgnoreExistingWavs.setText("Ignore Existing WAVs");
        }

        return chkIgnoreExistingWavs;
    }

    public class PrepareStartArgument {
        public String singer = "Miku";
        public double amplitude = 1.0;
        public String directory = "";
        public boolean replace = true;
    }
}
