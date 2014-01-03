/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
package com.xyphos.vmtgen;

import com.google.common.io.LittleEndianDataInputStream;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.Preferences;
import javax.swing.*;
import org.apache.commons.io.FilenameUtils;



/**

 @author William Scott, Xyphos Software
 */
public class GUI extends javax.swing.JFrame implements KeyEventDispatcher {
  private static final Logger logger = Logger.getLogger( GUI.class.getName() );
  //
  private static final Preferences preferences = Preferences.userNodeForPackage( GUI.class );
  private static final String pref_ROOT = "ROOT_FOLDER";
  private static final String pref_WORK = "WORK_FOLDER";
  //
  private static String rootPath;
  private static String workPath;
  private static String basePath;
  //
  private static final Map ShaderMap = new HashMap();
  private static final int ShaderDefault = 5;
  private static final String[] Shaders = {
    //"Custom --->",
    "BaseTimesLightmap",
    "Cable",
    "Decal",
    "DecalModulate",
    "LightMappedGeneric",
    "Modulate",
    "MonitorScreen",
    "Predator",
    "Refract",
    "ShatteredGlass",
    "Sprite",
    "UnlitGeneric",
    "VertexLitGeneric",
    "Water", };
  //
  private static final Map SurfaceMap = new HashMap();
  private static final int SurfaceDefault = 12;
  private static final String[] Surfaces = {
    //"--- NONE ---",
    //"Custom --->",
    "AlienFlesh",
    "ArmorFlesh",
    "BloodyFlesh",
    "Boulder",
    "Brick",
    "Chain",
    "ChainLink",
    "Computer",
    "Concrete",
    "Concrete_Block",
    "Default",
    "Default_Silent",
    "Dirt",
    "Flesh",
    "Glass",
    "Grass",
    "Gravel",
    "Ice",
    "Ladder",
    "Metal_Box",
    "Metal",
    "MetalGrate",
    "MetalPanel",
    "MetalVent",
    "MudSlipperySlime",
    "Player_Control_Clip",
    "Porcelain",
    "QuickSand",
    "Rock",
    "Slime",
    "SlipperyMetal",
    "Snow",
    "SolidMetal",
    "Tile",
    "Wade",
    "Water",
    "WaterMelon",
    "Wood_Box",
    "Wood_Crate",
    "Wood_Furniture",
    "Wood_Panel",
    "Wood_Plank",
    "Wood_Solid",
    "WoodWood_LowDensity", };
  //
  private static final int SIGNATURE_VTF = 0x00465456; // 56 54 46 00   V T F .
  private static boolean animated = false;
  //
  private static final String EMPTY_STRING = "";

  /**
   Creates new form GUI
   */
  public GUI () {
    initComponents();

    // Global keyboard hook
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( this );

    // Minor hack for better readability of disabled combo boxes;
    UIManager.put( "ComboBox.disabledForeground", Color.RED );


    int i = 1;
    cmbShader.addItem( "Custom --->" );
    for ( String item : Shaders ) {
      cmbShader.addItem( item );
      ShaderMap.put( item.toUpperCase(), i++ );
    }
    cmbShader.setSelectedIndex( ShaderDefault );

    cmbSurface1.addItem( "--- NONE ---" );
    cmbSurface1.addItem( "Cutstom --->" );
    cmbSurface2.addItem( "--- NONE ---" );
    cmbSurface2.addItem( "Custom --->" );

    i = 1;
    for ( String item : Surfaces ) {
      cmbSurface1.addItem( item );
      cmbSurface2.addItem( item );
      SurfaceMap.put( item.toUpperCase(), i++ );
    }
    cmbSurface1.setSelectedIndex( SurfaceDefault );
    cmbSurface2.setSelectedIndex( 0 );

    txtRootFolder.setText( rootPath = preferences.get( pref_ROOT, "" ) );
    txtWorkFolder.setText( workPath = preferences.get( pref_WORK, "" ) );
    ShowTextureFiles();
  }

  @Override
  public boolean dispatchKeyEvent ( KeyEvent ke ) {
    if ( KeyEvent.KEY_PRESSED == ke.getID() ) {
      System.out.println( ke.toString() );
      switch ( ke.getKeyCode() ) {
        case KeyEvent.VK_F1:
          GenerateVMT();
          return true;

        case KeyEvent.VK_F2:
          SetAllLocks( true );
          return true;

        case KeyEvent.VK_F3:
          SetAllLocks( false );
          return true;

        case KeyEvent.VK_F4:
          ToggleAllLocks();
          return true;

        case KeyEvent.VK_F5:
          ShowTextureFiles();
          return true;

        case KeyEvent.VK_F6:
          ResetAllInput();
          return true;
      }
    }
    return false;
  }

  /**
   This method is called from within the constructor to
   initialize the form.
   WARNING: Do NOT modify this code. The content of this method is
   always regenerated by the Form Editor.
   */
  @SuppressWarnings ( "unchecked" )
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    panFolders = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    txtRootFolder = new javax.swing.JTextField();
    btnRootFolderBrowse = new javax.swing.JButton();
    btnWorkFolderBrowse = new javax.swing.JButton();
    txtWorkFolder = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    panFlags = new javax.swing.JPanel();
    chkAdditive = new javax.swing.JCheckBox();
    chkAlphaTest = new javax.swing.JCheckBox();
    chkEnvMapContrast = new javax.swing.JCheckBox();
    chkEnvMapSaturation = new javax.swing.JCheckBox();
    chkNoCull = new javax.swing.JCheckBox();
    chkNoDecal = new javax.swing.JCheckBox();
    chkNoLOD = new javax.swing.JCheckBox();
    chkTranslucent = new javax.swing.JCheckBox();
    chkVertexAlpha = new javax.swing.JCheckBox();
    chkVertexColor = new javax.swing.JCheckBox();
    panTexture = new javax.swing.JPanel();
    jLabel8 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    chkLockBaseTexture2 = new javax.swing.JCheckBox();
    chkLockBaseTexture1 = new javax.swing.JCheckBox();
    txtBaseTexture2 = new javax.swing.JTextField();
    btnBaseTexture2 = new javax.swing.JButton();
    chkLockDetailTexture = new javax.swing.JCheckBox();
    jLabel10 = new javax.swing.JLabel();
    txtDetailTexture = new javax.swing.JTextField();
    btnDetailTexture = new javax.swing.JButton();
    jLabel11 = new javax.swing.JLabel();
    txtToolTexture = new javax.swing.JTextField();
    chkLockToolTexture = new javax.swing.JCheckBox();
    btnToolTexture = new javax.swing.JButton();
    chkLockBumpMap1 = new javax.swing.JCheckBox();
    txtBumpMap1 = new javax.swing.JTextField();
    btnBumpMap1 = new javax.swing.JButton();
    jLabel12 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    txtBumpMap2 = new javax.swing.JTextField();
    btnBumpMap2 = new javax.swing.JButton();
    chkLockBumpMap2 = new javax.swing.JCheckBox();
    jLabel14 = new javax.swing.JLabel();
    txtEnvMap = new javax.swing.JTextField();
    chkLockEnvMap = new javax.swing.JCheckBox();
    btnEnvMap = new javax.swing.JButton();
    jLabel15 = new javax.swing.JLabel();
    txtEnvMapMask = new javax.swing.JTextField();
    chkLockEnvMapMask = new javax.swing.JCheckBox();
    btnEnvMapMask = new javax.swing.JButton();
    jLabel16 = new javax.swing.JLabel();
    txtNormalMap = new javax.swing.JTextField();
    chkLockNormalMap = new javax.swing.JCheckBox();
    btnNormalMap = new javax.swing.JButton();
    jLabel17 = new javax.swing.JLabel();
    txtDuDvMap = new javax.swing.JTextField();
    chkLockDuDvMap = new javax.swing.JCheckBox();
    btnDuDvMap = new javax.swing.JButton();
    txtBaseTexture1 = new javax.swing.JTextField();
    btnBaseTexture1 = new javax.swing.JButton();
    panOptions = new javax.swing.JPanel();
    chkLockSurface1 = new javax.swing.JCheckBox();
    cmbShader = new javax.swing.JComboBox();
    txtShader = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    chkLockShader = new javax.swing.JCheckBox();
    jLabel4 = new javax.swing.JLabel();
    chkLockSurface2 = new javax.swing.JCheckBox();
    cmbSurface1 = new javax.swing.JComboBox();
    txtSurface1 = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    cmbSurface2 = new javax.swing.JComboBox();
    txtSurface2 = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    txtKeywords = new javax.swing.JTextField();
    chkLockKeywords = new javax.swing.JCheckBox();
    nudFrameRate = new javax.swing.JSpinner();
    chkLockFrameRate = new javax.swing.JCheckBox();
    jLabel7 = new javax.swing.JLabel();
    chkLockAlpha = new javax.swing.JCheckBox();
    jLabel19 = new javax.swing.JLabel();
    nudAlpha = new javax.swing.JSpinner();
    panFiles = new javax.swing.JPanel();
    chkOnlyMissing = new javax.swing.JCheckBox();
    jScrollPane1 = new javax.swing.JScrollPane();
    lstFiles = new javax.swing.JList();
    jPanel1 = new javax.swing.JPanel();
    jLabel18 = new javax.swing.JLabel();
    jLabel20 = new javax.swing.JLabel();
    jLabel21 = new javax.swing.JLabel();
    jLabel22 = new javax.swing.JLabel();
    jLabel23 = new javax.swing.JLabel();
    jLabel25 = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    chkCompileTrigger = new javax.swing.JCheckBox();
    chkCompileSky = new javax.swing.JCheckBox();
    chkCompileSkip = new javax.swing.JCheckBox();
    chkCompilePlayerClip = new javax.swing.JCheckBox();
    chkCompileNoDraw = new javax.swing.JCheckBox();
    chkCompilePassBullets = new javax.swing.JCheckBox();
    chkCompileOrigin = new javax.swing.JCheckBox();
    chkCompileNoLight = new javax.swing.JCheckBox();
    chkCompileNpcClip = new javax.swing.JCheckBox();
    chkCompileLadder = new javax.swing.JCheckBox();
    chkCompileHint = new javax.swing.JCheckBox();
    chkCompileNonSolid = new javax.swing.JCheckBox();
    chkCompileDetail = new javax.swing.JCheckBox();
    chkCompileClip = new javax.swing.JCheckBox();
    chkCompileFog = new javax.swing.JCheckBox();
    chkCompilePlayerControlClip = new javax.swing.JCheckBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("VMTGen");
    setName("frmGUI"); // NOI18N
    setResizable(false);

    panFolders.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Settings"));

    jLabel1.setText("Root Folder");

    txtRootFolder.setEditable(false);
    txtRootFolder.setBackground(java.awt.SystemColor.text);
    txtRootFolder.setFocusable(false);
    txtRootFolder.setName(""); // NOI18N
    txtRootFolder.setPreferredSize(new java.awt.Dimension(59, 25));

    btnRootFolderBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/folder_16x16.png"))); // NOI18N
    btnRootFolderBrowse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRootFolderBrowseActionPerformed(evt);
      }
    });

    btnWorkFolderBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/folder_16x16.png"))); // NOI18N
    btnWorkFolderBrowse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnWorkFolderBrowseActionPerformed(evt);
      }
    });

    txtWorkFolder.setEditable(false);
    txtWorkFolder.setBackground(java.awt.SystemColor.text);
    txtWorkFolder.setFocusable(false);
    txtWorkFolder.setName(""); // NOI18N
    txtWorkFolder.setPreferredSize(new java.awt.Dimension(59, 25));

    jLabel2.setText("Working Folder");

    javax.swing.GroupLayout panFoldersLayout = new javax.swing.GroupLayout(panFolders);
    panFolders.setLayout(panFoldersLayout);
    panFoldersLayout.setHorizontalGroup(
      panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panFoldersLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel2)
          .addComponent(jLabel1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtWorkFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(txtRootFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnWorkFolderBrowse, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnRootFolderBrowse, javax.swing.GroupLayout.Alignment.TRAILING))
        .addGap(13, 13, 13))
    );
    panFoldersLayout.setVerticalGroup(
      panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panFoldersLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnRootFolderBrowse)
          .addGroup(panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtRootFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel1)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnWorkFolderBrowse)
          .addGroup(panFoldersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtWorkFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel2)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    panFlags.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Flags"));

    chkAdditive.setText("Additive");

    chkAlphaTest.setText("Alpha Test");

    chkEnvMapContrast.setText("Environment Map Contrast");

    chkEnvMapSaturation.setText("Environment Map Saturation");

    chkNoCull.setText("No Cull");

    chkNoDecal.setText("No Decal");

    chkNoLOD.setForeground(new java.awt.Color(10, 36, 106));
    chkNoLOD.setText("No LOD");

    chkTranslucent.setForeground(new java.awt.Color(10, 36, 106));
    chkTranslucent.setText("Translucent");

    chkVertexAlpha.setText("Vertex Alpha");

    chkVertexColor.setText("Vertex Color");

    javax.swing.GroupLayout panFlagsLayout = new javax.swing.GroupLayout(panFlags);
    panFlags.setLayout(panFlagsLayout);
    panFlagsLayout.setHorizontalGroup(
      panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panFlagsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(chkEnvMapContrast)
          .addComponent(chkEnvMapSaturation)
          .addComponent(chkNoCull)
          .addComponent(chkNoDecal)
          .addComponent(chkNoLOD)
          .addComponent(chkTranslucent)
          .addComponent(chkVertexAlpha)
          .addComponent(chkVertexColor)
          .addComponent(chkAdditive)
          .addComponent(chkAlphaTest))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    panFlagsLayout.setVerticalGroup(
      panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panFlagsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(chkAdditive)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkAlphaTest)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, Short.MAX_VALUE)
        .addComponent(chkEnvMapContrast)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkEnvMapSaturation)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkNoCull)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkNoDecal)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkNoLOD)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkTranslucent)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkVertexAlpha)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkVertexColor)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    panTexture.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Textures"));

    jLabel8.setText("Base Texture 1");

    jLabel9.setText("Base Texture 2");

    chkLockBaseTexture2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockBaseTexture2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockBaseTexture2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockBaseTexture2ActionPerformed(evt);
      }
    });

    chkLockBaseTexture1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockBaseTexture1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockBaseTexture1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockBaseTexture1ActionPerformed(evt);
      }
    });

    txtBaseTexture2.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    btnBaseTexture2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnBaseTexture2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBaseTexture2ActionPerformed(evt);
      }
    });

    chkLockDetailTexture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockDetailTexture.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockDetailTexture.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockDetailTextureActionPerformed(evt);
      }
    });

    jLabel10.setText("Detail Texture");

    txtDetailTexture.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    btnDetailTexture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnDetailTexture.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnDetailTextureActionPerformed(evt);
      }
    });

    jLabel11.setText("Tool Texture");

    txtToolTexture.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    chkLockToolTexture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockToolTexture.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockToolTexture.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockToolTextureActionPerformed(evt);
      }
    });

    btnToolTexture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnToolTexture.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnToolTextureActionPerformed(evt);
      }
    });

    chkLockBumpMap1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockBumpMap1.setPreferredSize(new java.awt.Dimension(25, 20));
    chkLockBumpMap1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockBumpMap1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockBumpMap1ActionPerformed(evt);
      }
    });

    txtBumpMap1.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    btnBumpMap1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnBumpMap1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBumpMap1ActionPerformed(evt);
      }
    });

    jLabel12.setText("Bump Map 1");

    jLabel13.setText("Bump Map 2");

    txtBumpMap2.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    btnBumpMap2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnBumpMap2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBumpMap2ActionPerformed(evt);
      }
    });

    chkLockBumpMap2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockBumpMap2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockBumpMap2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockBumpMap2ActionPerformed(evt);
      }
    });

    jLabel14.setText("Environment Map");

    txtEnvMap.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    chkLockEnvMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockEnvMap.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockEnvMap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockEnvMapActionPerformed(evt);
      }
    });

    btnEnvMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnEnvMap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEnvMapActionPerformed(evt);
      }
    });

    jLabel15.setText("Environment Map Mask");

    txtEnvMapMask.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    chkLockEnvMapMask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockEnvMapMask.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockEnvMapMask.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockEnvMapMaskActionPerformed(evt);
      }
    });

    btnEnvMapMask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnEnvMapMask.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEnvMapMaskActionPerformed(evt);
      }
    });

    jLabel16.setText("Normal Map");

    txtNormalMap.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    chkLockNormalMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockNormalMap.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockNormalMap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockNormalMapActionPerformed(evt);
      }
    });

    btnNormalMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnNormalMap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnNormalMapActionPerformed(evt);
      }
    });

    jLabel17.setText("DuDv Map");

    txtDuDvMap.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    chkLockDuDvMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockDuDvMap.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockDuDvMap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockDuDvMapActionPerformed(evt);
      }
    });

    btnDuDvMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnDuDvMap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnDuDvMapActionPerformed(evt);
      }
    });

    txtBaseTexture1.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    btnBaseTexture1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnBaseTexture1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBaseTexture1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout panTextureLayout = new javax.swing.GroupLayout(panTexture);
    panTexture.setLayout(panTextureLayout);
    panTextureLayout.setHorizontalGroup(
      panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panTextureLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockBaseTexture1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel8))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockBaseTexture2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel9))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockDetailTexture)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel10))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockToolTexture)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel11))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockBumpMap2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel13))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockBumpMap1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel12))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockEnvMap)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel14))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockEnvMapMask)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel15))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockNormalMap)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel16))
          .addGroup(panTextureLayout.createSequentialGroup()
            .addComponent(chkLockDuDvMap)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel17)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtDuDvMap)
          .addComponent(txtNormalMap)
          .addComponent(txtEnvMapMask)
          .addComponent(txtBumpMap2)
          .addComponent(txtBumpMap1)
          .addComponent(txtToolTexture)
          .addComponent(txtDetailTexture, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(txtBaseTexture2, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(txtBaseTexture1, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(txtEnvMap, javax.swing.GroupLayout.Alignment.TRAILING))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnBaseTexture1, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnBaseTexture2, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnDetailTexture, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnToolTexture, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnBumpMap1, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnBumpMap2, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnEnvMap, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnEnvMapMask, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnNormalMap, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnDuDvMap, javax.swing.GroupLayout.Alignment.TRAILING))
        .addContainerGap())
    );
    panTextureLayout.setVerticalGroup(
      panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panTextureLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtBaseTexture1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel8))
          .addComponent(chkLockBaseTexture1)
          .addComponent(btnBaseTexture1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtBaseTexture2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel9))
          .addComponent(chkLockBaseTexture2)
          .addComponent(btnBaseTexture2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtDetailTexture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(chkLockDetailTexture)
          .addComponent(btnDetailTexture))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtToolTexture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel11))
          .addComponent(chkLockToolTexture)
          .addComponent(btnToolTexture))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtBumpMap1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel12))
          .addComponent(chkLockBumpMap1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnBumpMap1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtBumpMap2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel13))
          .addComponent(chkLockBumpMap2)
          .addComponent(btnBumpMap2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtEnvMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel14))
          .addComponent(chkLockEnvMap)
          .addComponent(btnEnvMap))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtEnvMapMask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel15))
          .addComponent(chkLockEnvMapMask)
          .addComponent(btnEnvMapMask))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtNormalMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel16))
          .addComponent(chkLockNormalMap)
          .addComponent(btnNormalMap))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtDuDvMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel17))
          .addComponent(chkLockDuDvMap)
          .addComponent(btnDuDvMap))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    panOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Options"));

    chkLockSurface1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockSurface1.setRequestFocusEnabled(false);
    chkLockSurface1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockSurface1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockSurface1ActionPerformed(evt);
      }
    });

    cmbShader.setPreferredSize(new java.awt.Dimension(100, 22));
    cmbShader.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmbShaderActionPerformed(evt);
      }
    });

    txtShader.setDisabledTextColor(new java.awt.Color(255, 0, 0));
    txtShader.setEnabled(false);
    txtShader.setPreferredSize(new java.awt.Dimension(100, 20));

    jLabel3.setText("Shader");

    chkLockShader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockShader.setRequestFocusEnabled(false);
    chkLockShader.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockShader.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockShaderActionPerformed(evt);
      }
    });

    jLabel4.setText("Surface 1");

    chkLockSurface2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockSurface2.setRequestFocusEnabled(false);
    chkLockSurface2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockSurface2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockSurface2ActionPerformed(evt);
      }
    });

    cmbSurface1.setPreferredSize(new java.awt.Dimension(100, 22));
    cmbSurface1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmbSurface1ActionPerformed(evt);
      }
    });

    txtSurface1.setDisabledTextColor(new java.awt.Color(255, 0, 0));
    txtSurface1.setEnabled(false);
    txtSurface1.setPreferredSize(new java.awt.Dimension(100, 20));

    jLabel5.setText("Surface 2");

    cmbSurface2.setPreferredSize(new java.awt.Dimension(100, 22));
    cmbSurface2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmbSurface2ActionPerformed(evt);
      }
    });

    txtSurface2.setDisabledTextColor(new java.awt.Color(255, 0, 0));
    txtSurface2.setEnabled(false);
    txtSurface2.setPreferredSize(new java.awt.Dimension(100, 20));

    jLabel6.setText("Keywords");

    txtKeywords.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    chkLockKeywords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockKeywords.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockKeywords.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockKeywordsActionPerformed(evt);
      }
    });

    nudFrameRate.setModel(new javax.swing.SpinnerNumberModel(0, 0, 999999, 1));
    nudFrameRate.setEnabled(false);
    nudFrameRate.setPreferredSize(new java.awt.Dimension(80, 18));

    chkLockFrameRate.setEnabled(false);
    chkLockFrameRate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockFrameRate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockFrameRate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockFrameRateActionPerformed(evt);
      }
    });

    jLabel7.setText("Frame Rate");

    chkLockAlpha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockAlpha.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N

    jLabel19.setText("Alpha");

    nudAlpha.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
    nudAlpha.setPreferredSize(new java.awt.Dimension(80, 18));

    javax.swing.GroupLayout panOptionsLayout = new javax.swing.GroupLayout(panOptions);
    panOptions.setLayout(panOptionsLayout);
    panOptionsLayout.setHorizontalGroup(
      panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panOptionsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panOptionsLayout.createSequentialGroup()
            .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(chkLockShader)
              .addComponent(chkLockSurface1)
              .addComponent(chkLockSurface2)
              .addComponent(chkLockKeywords))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(panOptionsLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(panOptionsLayout.createSequentialGroup()
                    .addComponent(nudAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                  .addComponent(txtKeywords)))
              .addGroup(panOptionsLayout.createSequentialGroup()
                .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(panOptionsLayout.createSequentialGroup()
                    .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(jLabel4)
                      .addComponent(jLabel3))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                      .addComponent(cmbShader, 0, 151, Short.MAX_VALUE)
                      .addComponent(cmbSurface1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                  .addGroup(panOptionsLayout.createSequentialGroup()
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cmbSurface2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(txtSurface2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(txtSurface1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addGroup(panOptionsLayout.createSequentialGroup()
                    .addComponent(chkLockFrameRate)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(nudFrameRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                  .addComponent(txtShader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
          .addGroup(panOptionsLayout.createSequentialGroup()
            .addComponent(chkLockAlpha)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel19)))
        .addContainerGap())
    );
    panOptionsLayout.setVerticalGroup(
      panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panOptionsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel3)
            .addComponent(cmbShader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtShader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(chkLockShader))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel4)
          .addComponent(chkLockSurface1)
          .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtSurface1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cmbSurface1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(cmbSurface2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel5)
            .addComponent(txtSurface2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(chkLockSurface2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtKeywords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel6))
          .addComponent(chkLockKeywords))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel19)
          .addComponent(chkLockAlpha)
          .addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(nudFrameRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel7))
          .addComponent(nudAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(chkLockFrameRate))
        .addContainerGap(22, Short.MAX_VALUE))
    );

    panFiles.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Texture Files"));

    chkOnlyMissing.setText("Only show Texture files without Material files");
    chkOnlyMissing.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkOnlyMissingActionPerformed(evt);
      }
    });

    lstFiles.setModel(new DefaultListModel());
    lstFiles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    lstFiles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        lstFilesValueChanged(evt);
      }
    });
    jScrollPane1.setViewportView(lstFiles);

    javax.swing.GroupLayout panFilesLayout = new javax.swing.GroupLayout(panFiles);
    panFiles.setLayout(panFilesLayout);
    panFilesLayout.setHorizontalGroup(
      panFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFilesLayout.createSequentialGroup()
        .addGap(13, 13, 13)
        .addGroup(panFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
          .addComponent(chkOnlyMissing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    panFilesLayout.setVerticalGroup(
      panFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panFilesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(chkOnlyMissing)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        .addContainerGap())
    );

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Hotkeys", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

    jLabel18.setText("F1 = Generate Material File");

    jLabel20.setText("F3 = Unlock All Input");

    jLabel21.setText("F2 = Lock All Input");

    jLabel22.setText("F5 = Refresh File List");

    jLabel23.setText("F4 = Toggle All Locks");

    jLabel25.setText("F6 = Reset/Default");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel18)
          .addComponent(jLabel23))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel21)
          .addComponent(jLabel22))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel25)
          .addComponent(jLabel20))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel18)
          .addComponent(jLabel21)
          .addComponent(jLabel20))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel23)
          .addComponent(jLabel22)
          .addComponent(jLabel25))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Compile Flags"));

    chkCompileTrigger.setText("Trigger");

    chkCompileSky.setText("Sky");

    chkCompileSkip.setText("Skip");

    chkCompilePlayerClip.setText("Player Clip");

    chkCompileNoDraw.setText("No Draw");

    chkCompilePassBullets.setText("Pass Bullets");

    chkCompileOrigin.setText("Origin");

    chkCompileNoLight.setText("No Light");

    chkCompileNpcClip.setText("NPC Clip");

    chkCompileLadder.setText("Ladder");

    chkCompileHint.setText("Hint");

    chkCompileNonSolid.setText("Non-Solid");

    chkCompileDetail.setText("Detail");

    chkCompileClip.setText("Clip");

    chkCompileFog.setText("Fog");

    chkCompilePlayerControlClip.setText("Player Control Clip");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(chkCompileClip)
              .addComponent(chkCompileNoLight)
              .addComponent(chkCompileNonSolid)
              .addComponent(chkCompileDetail)
              .addComponent(chkCompileHint)
              .addComponent(chkCompileNoDraw)
              .addComponent(chkCompileLadder))
            .addGap(18, 18, 18)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(chkCompilePlayerClip)
              .addComponent(chkCompileSkip)
              .addComponent(chkCompileSky)
              .addComponent(chkCompileTrigger)
              .addComponent(chkCompileOrigin)
              .addComponent(chkCompilePassBullets)
              .addComponent(chkCompileNpcClip)
              .addComponent(chkCompilePlayerControlClip)))
          .addComponent(chkCompileFog))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkCompileClip)
          .addComponent(chkCompileNpcClip))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkCompileDetail)
          .addComponent(chkCompileOrigin))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkCompileFog)
          .addComponent(chkCompilePassBullets))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkCompileHint)
          .addComponent(chkCompilePlayerClip))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkCompilePlayerControlClip)
          .addComponent(chkCompileLadder))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkCompileSkip)
          .addComponent(chkCompileNoDraw))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkCompileSky)
          .addComponent(chkCompileNoLight))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkCompileTrigger)
          .addComponent(chkCompileNonSolid))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(panFolders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
          .addComponent(panTexture, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
            .addComponent(panFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(panOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(panFlags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(panFolders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(panFlags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(panOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(panFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(panTexture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addGap(0, 0, 0))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void WriteText ( PrintWriter out,
                           boolean condition,
                           int padding,
                           String textureName,
                           String textureValue ) {

    if ( condition ) {
      out.printf( "\t\"%s\"\t\t", textureName );
      for ( int i = 0; i < padding; i++ ) {
        out.print( "\t" );
      }
      out.printf( "\"%s\"\r\n", textureValue );
    }
  }

  private void WriteFlag ( PrintWriter out, boolean condition, String flagName ) {
    if ( condition ) {
      out.printf( "\t\"%s\"\t\t1\r\n", flagName );
    }
  }

  private void GenerateVMT () {
    if ( -1 == lstFiles.getSelectedIndex() ) {
      return;
    }

    String file = lstFiles.getSelectedValue().toString();
    String path = FilenameUtils.separatorsToSystem(
      FilenameUtils.concat( workPath,
                            FilenameUtils.getBaseName( file ) + ".VMT" ) );

    try {
      File fileVMT = new File( path );
      PrintWriter out = new PrintWriter( fileVMT );

      file = ( 0 == cmbShader.getSelectedIndex() )
             ? txtShader.getText()
             : cmbShader.getSelectedItem().toString();

      out.printf( "\"%s\"\r\n", file );
      out.println( "{" );

      WriteText( out, !( file = txtKeywords.getText() ).isEmpty(), 0, "%keywords", file );
      WriteText( out, !( file = txtToolTexture.getText() ).isEmpty(), 0, "%toolTexture", file );

      int index = cmbSurface1.getSelectedIndex();
      if ( 0 != index ) {
        file = ( 1 == index )
               ? txtSurface1.getText()
               : cmbSurface1.getSelectedItem().toString();

        out.printf( "\t\"$surfaceProp\"\t\t\"%s\"\r\n", file );
      }

      index = cmbSurface2.getSelectedIndex();
      if ( 0 != index ) {
        file = ( 1 == index )
               ? txtSurface2.getText()
               : cmbSurface2.getSelectedItem().toString();

        out.printf( "\t\"$surfaceProp2\"\t\t\"%s\"\r\n", file );
      }

      if ( !( file = nudAlpha.getValue().toString() ).equals( "1.0" ) ) {
        out.printf( "\t\"$alpha\"\t\t\t\"%s\"\r\n", file );
      }

      WriteText( out, !( file = txtBaseTexture1.getText() ).isEmpty(), 0, "$baseTexture", file );
      WriteText( out, !( file = txtBaseTexture2.getText() ).isEmpty(), 0, "$baseTexture2", file );
      WriteText( out, !( file = txtDetailTexture.getText() ).isEmpty(), 1, "$detail", file );
      WriteText( out, !( file = txtBumpMap1.getText() ).isEmpty(), 0, "$bumpMap", file );
      WriteText( out, !( file = txtBumpMap2.getText() ).isEmpty(), 0, "$bumpMap2", file );
      WriteText( out, !( file = txtEnvMap.getText() ).isEmpty(), 0, "$envMap", file );
      WriteText( out, !( file = txtEnvMapMask.getText() ).isEmpty(), 0, "$envMapMask", file );
      WriteText( out, !( file = txtNormalMap.getText() ).isEmpty(), 0, "$normalMap", file );
      WriteText( out, !( file = txtDuDvMap.getText() ).isEmpty(), 0, "$DuDvMap", file );

      WriteFlag( out, chkAdditive.isSelected(), "$additive" );
      WriteFlag( out, chkAlphaTest.isSelected(), "$alphaTest" );
      WriteFlag( out, chkEnvMapContrast.isSelected(), "$envMapContrast" );
      WriteFlag( out, chkEnvMapSaturation.isSelected(), "$envMapSaturation" );
      WriteFlag( out, chkNoCull.isSelected(), "$noCull" );
      WriteFlag( out, chkNoLOD.isSelected(), "$noLOD" );
      WriteFlag( out, chkTranslucent.isSelected(), "$translucent" );
      WriteFlag( out, chkVertexAlpha.isSelected(), "$vertexAlpha" );
      WriteFlag( out, chkVertexColor.isSelected(), "$vertexColor" );

      WriteFlag( out, chkCompileClip.isSelected(), "%compileClip" );
      WriteFlag( out, chkCompileDetail.isSelected(), "%compileDetail" );
      WriteFlag( out, chkCompileHint.isSelected(), "%compileHint" );
      WriteFlag( out, chkCompileLadder.isSelected(), "%compileLadder" );
      WriteFlag( out, chkCompileNoDraw.isSelected(), "%compileNoDraw" );
      WriteFlag( out, chkCompileNonSolid.isSelected(), "%compileNonSolid" );
      WriteFlag( out, chkCompileNpcClip.isSelected(), "%compileNpcClip" );
      WriteFlag( out, chkCompilePassBullets.isSelected(), "%compilePassBullets" );
      WriteFlag( out, chkCompilePlayerClip.isSelected(), "%compilePlayerClip" );
      WriteFlag( out, chkCompilePlayerControlClip.isSelected(), "%compilePlayerControlClip" );
      WriteFlag( out, chkCompileSkip.isSelected(), "%compileSkip" );
      WriteFlag( out, chkCompileSky.isSelected(), "%compileSky" );
      WriteFlag( out, chkCompileTrigger.isSelected(), "%compileTrigger" );

      if ( animated ) {
        file = nudFrameRate.getValue().toString();
        out.print( "\r\n\t\"proxies\"\r\n" );
        out.print( "\t{\r\n" );
        out.print( "\t\t\"animatedTexture\"\r\n" );
        out.print( "\t\t{\r\n" );
        out.print( "\t\t\t\"$animatedTextureVar\"\t\t\"$baseTexture\"\r\n" );
        out.print( "\t\t\t\"$animatedTextureFrameVar\"\t\t\"$frame\"\r\n" );
        out.printf( "\t\t\t\"$animatedTextureFrameRate\"\t%s\r\n", file );
        out.print( "\t\t}\r\n" );
        out.print( "\t}\r\n" );
      }

      out.print( "}" );
      out.flush();
      out.close();
    } catch ( FileNotFoundException ex ) {
      logger.log( Level.SEVERE, null, ex );
    }
  }

  private void ResetAllInput () {
    SetShaderIndex( ShaderDefault );
    SetSurface1Index( SurfaceDefault );
    SetSurface2Index( 0 );
    SetKeywords( EMPTY_STRING );
    SetFrameRate( 0 );
    SetBaseTexture1( EMPTY_STRING );
    SetBaseTexture2( EMPTY_STRING );
    SetDetailTexture( EMPTY_STRING );
    SetToolTexture( EMPTY_STRING );
    SetBumpMap1( EMPTY_STRING );
    SetBumpMap2( EMPTY_STRING );
    SetEnvMap( EMPTY_STRING );
    SetEnvMapMask( EMPTY_STRING );
    SetNormalMap( EMPTY_STRING );
    SetDuDvMap( EMPTY_STRING );

    chkAdditive.setSelected( false );
    chkAlphaTest.setSelected( false );
    chkEnvMapContrast.setSelected( false );
    chkEnvMapSaturation.setSelected( false );
    chkNoCull.setSelected( false );
    chkNoDecal.setSelected( false );
    chkVertexAlpha.setSelected( false );
    chkVertexColor.setSelected( false );

    chkCompileClip.setSelected( false );
    chkCompileDetail.setSelected( false );
    chkCompileFog.setSelected( false );
    chkCompileHint.setSelected( false );
    chkCompileLadder.setSelected( false );
    chkCompileNoDraw.setSelected( false );
    chkCompileNoLight.setSelected( false );
    chkCompileNonSolid.setSelected( false );
    chkCompileNpcClip.setSelected( false );
    chkCompileOrigin.setSelected( false );
    chkCompilePassBullets.setSelected( false );
    chkCompilePlayerClip.setSelected( false );
    chkCompilePlayerControlClip.setSelected( false );
    chkCompileSkip.setSelected( false );
    chkCompileSky.setSelected( false );
    chkCompileTrigger.setSelected( false );
  }

  private void ToggleAllLocks () {
    boolean locked = !chkLockShader.isSelected();
    chkLockShader.setSelected( locked );
    cmbShader.setEnabled( !locked );
    txtShader.setEnabled( !locked & ( 0 == cmbShader.getSelectedIndex() ) );

    locked = !chkLockSurface1.isSelected();
    chkLockSurface1.setSelected( locked );
    cmbSurface1.setEnabled( !locked );
    txtSurface1.setEnabled( !locked & ( 0 == cmbSurface1.getSelectedIndex() ) );

    locked = !chkLockSurface2.isSelected();
    chkLockSurface2.setSelected( locked );
    cmbSurface2.setEnabled( !locked );
    txtSurface2.setEnabled( !locked & ( 1 == cmbSurface2.getSelectedIndex() ) );

    locked = !chkLockKeywords.isSelected();
    chkLockKeywords.setSelected( locked );
    txtKeywords.setEnabled( !locked );

    locked = !chkLockFrameRate.isSelected();
    chkLockFrameRate.setSelected( locked );
    nudFrameRate.setEnabled( !locked & animated );

    locked = !chkLockBaseTexture1.isSelected();
    chkLockBaseTexture1.setSelected( locked );
    txtBaseTexture1.setEnabled( !locked );
    btnBaseTexture1.setEnabled( !locked );

    locked = !chkLockBaseTexture2.isSelected();
    chkLockBaseTexture2.setSelected( locked );
    txtBaseTexture2.setEnabled( !locked );
    btnBaseTexture2.setEnabled( !locked );

    locked = !chkLockDetailTexture.isSelected();
    chkLockDetailTexture.setSelected( locked );
    txtDetailTexture.setEnabled( !locked );
    btnDetailTexture.setEnabled( !locked );

    locked = !chkLockToolTexture.isSelected();
    chkLockToolTexture.setSelected( locked );
    txtToolTexture.setEnabled( !locked );
    btnToolTexture.setEnabled( !locked );

    locked = !chkLockBumpMap1.isSelected();
    chkLockBumpMap1.setSelected( locked );
    txtBumpMap1.setEnabled( !locked );
    btnBumpMap1.setEnabled( !locked );

    locked = !chkLockBumpMap2.isSelected();
    chkLockBumpMap2.setSelected( locked );
    txtBumpMap2.setEnabled( !locked );
    btnBumpMap2.setEnabled( !locked );

    locked = !chkLockEnvMap.isSelected();
    chkLockEnvMap.setSelected( locked );
    txtEnvMap.setEnabled( !locked );
    btnEnvMap.setEnabled( !locked );

    locked = !chkLockEnvMapMask.isSelected();
    chkLockEnvMapMask.setSelected( locked );
    txtEnvMapMask.setEnabled( !locked );
    btnEnvMapMask.setEnabled( !locked );

    locked = !chkLockNormalMap.isSelected();
    chkLockNormalMap.setSelected( locked );
    txtNormalMap.setEnabled( !locked );
    btnNormalMap.setEnabled( !locked );

    locked = !chkLockDuDvMap.isSelected();
    chkLockDuDvMap.setSelected( locked );
    txtDuDvMap.setEnabled( !locked );
    btnDuDvMap.setEnabled( !locked );
  }

  private void SetAllLocks ( boolean locked ) {
    chkLockShader.setSelected( locked );
    cmbShader.setEnabled( !locked );
    txtShader.setEnabled( !locked & ( 0 == cmbShader.getSelectedIndex() ) );

    chkLockSurface1.setSelected( locked );
    cmbSurface1.setEnabled( !locked );
    txtSurface1.setEnabled( !locked & ( 0 == cmbSurface1.getSelectedIndex() ) );

    chkLockSurface2.setSelected( locked );
    cmbSurface2.setEnabled( !locked );
    txtSurface2.setEnabled( !locked & ( 1 == cmbSurface2.getSelectedIndex() ) );

    chkLockKeywords.setSelected( locked );
    txtKeywords.setEnabled( !locked );

    chkLockFrameRate.setSelected( locked );
    nudFrameRate.setEnabled( !locked & animated );

    chkLockBaseTexture1.setSelected( locked );
    txtBaseTexture1.setEnabled( !locked );
    btnBaseTexture1.setEnabled( !locked );

    chkLockBaseTexture2.setSelected( locked );
    txtBaseTexture2.setEnabled( !locked );
    btnBaseTexture2.setEnabled( !locked );

    chkLockDetailTexture.setSelected( locked );
    txtDetailTexture.setEnabled( !locked );
    btnDetailTexture.setEnabled( !locked );

    chkLockToolTexture.setSelected( locked );
    txtToolTexture.setEnabled( !locked );
    btnToolTexture.setEnabled( !locked );

    chkLockBumpMap1.setSelected( locked );
    txtBumpMap1.setEnabled( !locked );
    btnBumpMap1.setEnabled( !locked );

    chkLockBumpMap2.setSelected( locked );
    txtBumpMap2.setEnabled( !locked );
    btnBumpMap2.setEnabled( !locked );

    chkLockEnvMap.setSelected( locked );
    txtEnvMap.setEnabled( !locked );
    btnEnvMap.setEnabled( !locked );

    chkLockEnvMapMask.setSelected( locked );
    txtEnvMapMask.setEnabled( !locked );
    btnEnvMapMask.setEnabled( !locked );

    chkLockNormalMap.setSelected( locked );
    txtNormalMap.setEnabled( !locked );
    btnNormalMap.setEnabled( !locked );

    chkLockDuDvMap.setSelected( locked );
    txtDuDvMap.setEnabled( !locked );
    btnDuDvMap.setEnabled( !locked );
  }

  private String SelectVTF () {
    JFileChooser fc = new JFileChooser( txtWorkFolder.getText() );
    fc.setAcceptAllFileFilterUsed( false );
    fc.setFileFilter( new FileFilterVTF() );
    fc.setFileSelectionMode( JFileChooser.FILES_ONLY );

    int result = fc.showOpenDialog( this );
    if ( JFileChooser.APPROVE_OPTION == result ) {
      String file = fc.getSelectedFile().getName();
      return FilenameUtils.separatorsToUnix(
        FilenameUtils.concat( basePath,
                              FilenameUtils.getBaseName( file ) ) )
        .replaceFirst( "/", "" );
    }

    return null;
  }

  private void ShowTextureFiles () {
    DefaultListModel model = ( DefaultListModel ) lstFiles.getModel();
    model.clear();

    String root = txtRootFolder.getText();
    String work = txtWorkFolder.getText();

    if ( root.isEmpty() || work.isEmpty() ) {
      return;
    }

    basePath = work.replace( root, "" );
    File dir = new File( work );
    String full, name, ext;

    if ( chkOnlyMissing.isSelected() ) {
      List<String> fileVTF = new ArrayList<>();
      List<String> fileVMT = new ArrayList<>();

      for ( File file : dir.listFiles() ) {
        if ( file.isFile() ) {
          full = file.getName();
          name = FilenameUtils.getBaseName( full );
          ext = FilenameUtils.getExtension( full );

          if ( ext.equalsIgnoreCase( "vtf" ) ) {
            fileVTF.add( full );
          } else if ( ext.equalsIgnoreCase( "vmt" ) ) {
            fileVMT.add( full );
          }
        }
      }

      String baseName;
      Iterator<String> itr;
      boolean matched;
      for ( String file : fileVTF ) {
        baseName = FilenameUtils.getBaseName( file );
        itr = fileVMT.iterator();
        matched = false;
        while ( itr.hasNext() ) {
          if ( FilenameUtils.getBaseName( itr.next() ).equalsIgnoreCase( baseName ) ) {
            matched = true;
            break;
          }
        }

        if ( !matched ) {
          model.addElement( file );
        }
      }
    } else {
      for ( File file : dir.listFiles() ) {
        name = file.getName();
        ext = FilenameUtils.getExtension( name );
        if ( file.isFile() && ext.equalsIgnoreCase( "vtf" ) ) {
          model.addElement( name );
        }
      }
    }
  }

  private void SetShaderIndex ( int i ) {
    if ( !chkLockSurface1.isSelected() ) {
      cmbShader.setSelectedIndex( i );
    }
  }

  private void SetSurface1Index ( int i ) {
    if ( !chkLockSurface1.isSelected() ) {
      cmbSurface1.setSelectedIndex( i );
    }
  }

  private void SetSurface2Index ( int i ) {
    if ( !chkLockSurface2.isSelected() ) {
      cmbSurface2.setSelectedIndex( i );
    }
  }

  private void SetKeywords ( String keywords ) {
    if ( !chkLockKeywords.isSelected() ) {
      txtKeywords.setText( keywords );
    }
  }

  private void SetFrameRate ( int i ) {
    if ( !chkLockFrameRate.isSelected() ) {
      nudFrameRate.setValue( i );
    }
  }

  private void SetBaseTexture1 ( String path ) {
    if ( !chkLockBaseTexture1.isSelected() ) {
      txtBaseTexture1.setText( path );
    }
  }

  private void SetBaseTexture2 ( String path ) {
    if ( !chkLockBaseTexture2.isSelected() ) {
      txtBaseTexture2.setText( path );
    }
  }

  private void SetDetailTexture ( String path ) {
    if ( !chkLockDetailTexture.isSelected() ) {
      txtDetailTexture.setText( path );
    }
  }

  private void SetToolTexture ( String path ) {
    if ( !chkLockToolTexture.isSelected() ) {
      txtToolTexture.setText( path );
    }
  }

  private void SetBumpMap1 ( String path ) {
    if ( !chkLockBumpMap1.isSelected() ) {
      txtBumpMap1.setText( path );
    }
  }

  private void SetBumpMap2 ( String path ) {
    if ( !chkLockBumpMap2.isSelected() ) {
      txtBumpMap2.setText( path );
    }
  }

  private void SetEnvMap ( String path ) {
    if ( !chkLockEnvMap.isSelected() ) {
      txtEnvMap.setText( path );
    }
  }

  private void SetEnvMapMask ( String path ) {
    if ( !chkLockEnvMapMask.isSelected() ) {
      txtEnvMapMask.setText( path );
    }
  }

  private void SetNormalMap ( String path ) {
    if ( !chkLockNormalMap.isSelected() ) {
      txtNormalMap.setText( path );
    }
  }

  private void SetDuDvMap ( String path ) {
    if ( !chkLockDuDvMap.isSelected() ) {
      txtDuDvMap.setText( path );
    }
  }

  private void btnRootFolderBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRootFolderBrowseActionPerformed
    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    int result = fc.showOpenDialog( this );
    if ( JFileChooser.APPROVE_OPTION == result ) {
      rootPath = fc.getSelectedFile().getAbsolutePath();
      txtRootFolder.setText( rootPath );
      preferences.put( pref_ROOT, rootPath );
      ShowTextureFiles();
    }
  }//GEN-LAST:event_btnRootFolderBrowseActionPerformed

  private void btnWorkFolderBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWorkFolderBrowseActionPerformed
    File dir = new File( rootPath );
    JFileChooser fc = new JFileChooser( dir );
    fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    int result = fc.showOpenDialog( this );
    if ( JFileChooser.APPROVE_OPTION == result ) {
      workPath = fc.getSelectedFile().getAbsolutePath();
      txtWorkFolder.setText( workPath );
      preferences.put( pref_WORK, workPath );
      ShowTextureFiles();
    }
  }//GEN-LAST:event_btnWorkFolderBrowseActionPerformed

  private void cmbShaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShaderActionPerformed
    txtShader.setEnabled( 0 == cmbShader.getSelectedIndex() );
  }//GEN-LAST:event_cmbShaderActionPerformed

  private void cmbSurface1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSurface1ActionPerformed
    txtSurface1.setEnabled( 1 == cmbSurface1.getSelectedIndex() );
  }//GEN-LAST:event_cmbSurface1ActionPerformed

  private void cmbSurface2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSurface2ActionPerformed
    txtSurface2.setEnabled( 1 == cmbSurface2.getSelectedIndex() );
  }//GEN-LAST:event_cmbSurface2ActionPerformed

  private void chkOnlyMissingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOnlyMissingActionPerformed
    ShowTextureFiles();
  }//GEN-LAST:event_chkOnlyMissingActionPerformed

  private void chkLockShaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockShaderActionPerformed
    cmbShader.setEnabled( !chkLockShader.isSelected() );
    txtShader.setEnabled( !chkLockShader.isSelected() & ( 0 == cmbShader.getSelectedIndex() ) );
  }//GEN-LAST:event_chkLockShaderActionPerformed

  private void chkLockSurface1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockSurface1ActionPerformed
    cmbSurface1.setEnabled( !chkLockSurface1.isSelected() );
    txtSurface1.setEnabled( !chkLockSurface1.isSelected() & ( 1 == cmbSurface1.getSelectedIndex() ) );
  }//GEN-LAST:event_chkLockSurface1ActionPerformed

  private void chkLockSurface2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockSurface2ActionPerformed
    cmbSurface2.setEnabled( !chkLockSurface2.isSelected() );
    txtSurface2.setEnabled( !chkLockSurface2.isSelected() & ( 1 == cmbSurface2.getSelectedIndex() ) );
  }//GEN-LAST:event_chkLockSurface2ActionPerformed

  private void chkLockKeywordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockKeywordsActionPerformed
    txtKeywords.setEnabled( !chkLockKeywords.isSelected() );
  }//GEN-LAST:event_chkLockKeywordsActionPerformed

  private void chkLockFrameRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockFrameRateActionPerformed
    nudFrameRate.setEnabled( !chkLockFrameRate.isSelected() );
  }//GEN-LAST:event_chkLockFrameRateActionPerformed

  private void chkLockBaseTexture1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockBaseTexture1ActionPerformed
    txtBaseTexture1.setEnabled( !chkLockBaseTexture1.isSelected() );
    btnBaseTexture1.setEnabled( !chkLockBaseTexture1.isSelected() );
  }//GEN-LAST:event_chkLockBaseTexture1ActionPerformed

  private void chkLockBaseTexture2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockBaseTexture2ActionPerformed
    txtBaseTexture2.setEnabled( !chkLockBaseTexture2.isSelected() );
    btnBaseTexture2.setEnabled( !chkLockBaseTexture2.isSelected() );
  }//GEN-LAST:event_chkLockBaseTexture2ActionPerformed

  private void chkLockDetailTextureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockDetailTextureActionPerformed
    txtDetailTexture.setEnabled( !chkLockDetailTexture.isSelected() );
    btnDetailTexture.setEnabled( !chkLockDetailTexture.isSelected() );
  }//GEN-LAST:event_chkLockDetailTextureActionPerformed

  private void chkLockToolTextureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockToolTextureActionPerformed
    txtToolTexture.setEnabled( !chkLockToolTexture.isSelected() );
    btnToolTexture.setEnabled( !chkLockToolTexture.isSelected() );
  }//GEN-LAST:event_chkLockToolTextureActionPerformed

  private void chkLockBumpMap1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockBumpMap1ActionPerformed
    txtBumpMap1.setEnabled( !chkLockBumpMap1.isSelected() );
    btnBumpMap1.setEnabled( !chkLockBumpMap1.isSelected() );
  }//GEN-LAST:event_chkLockBumpMap1ActionPerformed

  private void chkLockBumpMap2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockBumpMap2ActionPerformed
    txtBumpMap2.setEnabled( !chkLockBumpMap2.isSelected() );
    btnBumpMap2.setEnabled( !chkLockBumpMap2.isSelected() );
  }//GEN-LAST:event_chkLockBumpMap2ActionPerformed

  private void chkLockEnvMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockEnvMapActionPerformed
    txtEnvMap.setEnabled( !chkLockEnvMap.isSelected() );
    btnEnvMap.setEnabled( !chkLockEnvMap.isSelected() );
  }//GEN-LAST:event_chkLockEnvMapActionPerformed

  private void chkLockEnvMapMaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockEnvMapMaskActionPerformed
    txtEnvMapMask.setEnabled( !chkLockEnvMapMask.isSelected() );
    btnEnvMapMask.setEnabled( !chkLockEnvMapMask.isSelected() );
  }//GEN-LAST:event_chkLockEnvMapMaskActionPerformed

  private void chkLockNormalMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockNormalMapActionPerformed
    txtNormalMap.setEnabled( !chkLockNormalMap.isSelected() );
    btnNormalMap.setEnabled( !chkLockNormalMap.isSelected() );
  }//GEN-LAST:event_chkLockNormalMapActionPerformed

  private void chkLockDuDvMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockDuDvMapActionPerformed
    txtDuDvMap.setEnabled( !chkLockDuDvMap.isSelected() );
    btnDuDvMap.setEnabled( !chkLockDuDvMap.isSelected() );
  }//GEN-LAST:event_chkLockDuDvMapActionPerformed

  private void btnBaseTexture1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaseTexture1ActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetBaseTexture1( path );
    }
  }//GEN-LAST:event_btnBaseTexture1ActionPerformed

  private void btnBaseTexture2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaseTexture2ActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetBaseTexture2( path );
    }
  }//GEN-LAST:event_btnBaseTexture2ActionPerformed

  private void btnDetailTextureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailTextureActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetDetailTexture( path );
    }
  }//GEN-LAST:event_btnDetailTextureActionPerformed

  private void btnToolTextureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToolTextureActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetToolTexture( path );
    }
  }//GEN-LAST:event_btnToolTextureActionPerformed

  private void btnBumpMap1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBumpMap1ActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetBumpMap1( path );
    }
  }//GEN-LAST:event_btnBumpMap1ActionPerformed

  private void btnBumpMap2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBumpMap2ActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetBumpMap2( path );
    }
  }//GEN-LAST:event_btnBumpMap2ActionPerformed

  private void btnEnvMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnvMapActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetEnvMap( path );
    }
  }//GEN-LAST:event_btnEnvMapActionPerformed

  private void btnEnvMapMaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnvMapMaskActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetEnvMapMask( path );
    }
  }//GEN-LAST:event_btnEnvMapMaskActionPerformed

  private void btnNormalMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNormalMapActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetNormalMap( path );
    }
  }//GEN-LAST:event_btnNormalMapActionPerformed

  private void btnDuDvMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDuDvMapActionPerformed
    String path = SelectVTF();
    if ( null != path ) {
      SetDuDvMap( path );
    }
  }//GEN-LAST:event_btnDuDvMapActionPerformed

  private void lstFilesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstFilesValueChanged
    if ( !evt.getValueIsAdjusting() && ( -1 != lstFiles.getSelectedIndex() ) ) {
      String file = lstFiles.getSelectedValue().toString();
      String path = FilenameUtils.separatorsToUnix(
        FilenameUtils.concat( basePath,
                              FilenameUtils.getBaseName( file ) ) )
        .replaceFirst( "/", "" );

      SetBaseTexture1( path );

      // read the vtf header
      file = FilenameUtils.concat( workPath, file );
      File fileVTF = new File( file );

      try ( LittleEndianDataInputStream in = new LittleEndianDataInputStream(
        new FileInputStream( fileVTF ) ) ) {

        int sig = in.readInt();
        if ( SIGNATURE_VTF != sig ) {
          throw new IOException( "Not a VTF file" );
        }

        in.skipBytes( 16 );
        int flags = in.readInt();
        short frames = in.readShort();
        in.close(); // don't need any more information

        chkNoLOD.setSelected( 0 != ( 0x200 & flags ) );
        chkTranslucent.setSelected( 0 != ( 0x3000 & flags ) );

        if ( 1 < frames ) {
          SetFrameRate( frames );
          nudFrameRate.setEnabled( !chkLockFrameRate.isSelected() );
          chkLockFrameRate.setEnabled( animated = true );
        } else {
          nudFrameRate.setEnabled( false );
          chkLockFrameRate.setEnabled( animated = false );
        }
      } catch ( FileNotFoundException ex ) {
        logger.log( Level.SEVERE, null, ex );
      } catch ( IOException ex ) {
        logger.log( Level.SEVERE, null, ex );
      }
    }
  }//GEN-LAST:event_lstFilesValueChanged

  /**
   @param args the command line arguments
   */
  public static void main ( String args[] ) {
    try {
      javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getCrossPlatformLookAndFeelClassName() );




    } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex ) {
      Logger.getLogger( GUI.class
        .getName() ).log( Level.SEVERE, null, ex );
    }

    /*
     Create and display the form
     */ java.awt.EventQueue.invokeLater( new Runnable() {
      @Override
      public void run () {
        new GUI().setVisible( true );
      }
    } );
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnBaseTexture1;
  private javax.swing.JButton btnBaseTexture2;
  private javax.swing.JButton btnBumpMap1;
  private javax.swing.JButton btnBumpMap2;
  private javax.swing.JButton btnDetailTexture;
  private javax.swing.JButton btnDuDvMap;
  private javax.swing.JButton btnEnvMap;
  private javax.swing.JButton btnEnvMapMask;
  private javax.swing.JButton btnNormalMap;
  private javax.swing.JButton btnRootFolderBrowse;
  private javax.swing.JButton btnToolTexture;
  private javax.swing.JButton btnWorkFolderBrowse;
  private javax.swing.JCheckBox chkAdditive;
  private javax.swing.JCheckBox chkAlphaTest;
  private javax.swing.JCheckBox chkCompileClip;
  private javax.swing.JCheckBox chkCompileDetail;
  private javax.swing.JCheckBox chkCompileFog;
  private javax.swing.JCheckBox chkCompileHint;
  private javax.swing.JCheckBox chkCompileLadder;
  private javax.swing.JCheckBox chkCompileNoDraw;
  private javax.swing.JCheckBox chkCompileNoLight;
  private javax.swing.JCheckBox chkCompileNonSolid;
  private javax.swing.JCheckBox chkCompileNpcClip;
  private javax.swing.JCheckBox chkCompileOrigin;
  private javax.swing.JCheckBox chkCompilePassBullets;
  private javax.swing.JCheckBox chkCompilePlayerClip;
  private javax.swing.JCheckBox chkCompilePlayerControlClip;
  private javax.swing.JCheckBox chkCompileSkip;
  private javax.swing.JCheckBox chkCompileSky;
  private javax.swing.JCheckBox chkCompileTrigger;
  private javax.swing.JCheckBox chkEnvMapContrast;
  private javax.swing.JCheckBox chkEnvMapSaturation;
  private javax.swing.JCheckBox chkLockAlpha;
  private javax.swing.JCheckBox chkLockBaseTexture1;
  private javax.swing.JCheckBox chkLockBaseTexture2;
  private javax.swing.JCheckBox chkLockBumpMap1;
  private javax.swing.JCheckBox chkLockBumpMap2;
  private javax.swing.JCheckBox chkLockDetailTexture;
  private javax.swing.JCheckBox chkLockDuDvMap;
  private javax.swing.JCheckBox chkLockEnvMap;
  private javax.swing.JCheckBox chkLockEnvMapMask;
  private javax.swing.JCheckBox chkLockFrameRate;
  private javax.swing.JCheckBox chkLockKeywords;
  private javax.swing.JCheckBox chkLockNormalMap;
  private javax.swing.JCheckBox chkLockShader;
  private javax.swing.JCheckBox chkLockSurface1;
  private javax.swing.JCheckBox chkLockSurface2;
  private javax.swing.JCheckBox chkLockToolTexture;
  private javax.swing.JCheckBox chkNoCull;
  private javax.swing.JCheckBox chkNoDecal;
  private javax.swing.JCheckBox chkNoLOD;
  private javax.swing.JCheckBox chkOnlyMissing;
  private javax.swing.JCheckBox chkTranslucent;
  private javax.swing.JCheckBox chkVertexAlpha;
  private javax.swing.JCheckBox chkVertexColor;
  private javax.swing.JComboBox cmbShader;
  private javax.swing.JComboBox cmbSurface1;
  private javax.swing.JComboBox cmbSurface2;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel18;
  private javax.swing.JLabel jLabel19;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel20;
  private javax.swing.JLabel jLabel21;
  private javax.swing.JLabel jLabel22;
  private javax.swing.JLabel jLabel23;
  private javax.swing.JLabel jLabel25;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JList lstFiles;
  private javax.swing.JSpinner nudAlpha;
  private javax.swing.JSpinner nudFrameRate;
  private javax.swing.JPanel panFiles;
  private javax.swing.JPanel panFlags;
  private javax.swing.JPanel panFolders;
  private javax.swing.JPanel panOptions;
  private javax.swing.JPanel panTexture;
  private javax.swing.JTextField txtBaseTexture1;
  private javax.swing.JTextField txtBaseTexture2;
  private javax.swing.JTextField txtBumpMap1;
  private javax.swing.JTextField txtBumpMap2;
  private javax.swing.JTextField txtDetailTexture;
  private javax.swing.JTextField txtDuDvMap;
  private javax.swing.JTextField txtEnvMap;
  private javax.swing.JTextField txtEnvMapMask;
  private javax.swing.JTextField txtKeywords;
  private javax.swing.JTextField txtNormalMap;
  private javax.swing.JTextField txtRootFolder;
  private javax.swing.JTextField txtShader;
  private javax.swing.JTextField txtSurface1;
  private javax.swing.JTextField txtSurface2;
  private javax.swing.JTextField txtToolTexture;
  private javax.swing.JTextField txtWorkFolder;
  // End of variables declaration//GEN-END:variables
}


