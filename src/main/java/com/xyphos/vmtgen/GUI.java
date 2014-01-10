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
import java.net.URL;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.Preferences;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;
import javax.swing.*;
import org.apache.commons.io.FilenameUtils;



/**
 @author William Scott, Xyphos Software
 */
public class GUI extends javax.swing.JFrame implements KeyEventDispatcher {
  private static final Logger logger = Logger.getLogger( GUI.class.getName() );
  private static final Locale locale = Locale.US;
  //
  private static final Preferences preferences = Preferences
    .userNodeForPackage( GUI.class );
  private static final String pref_ROOT = "ROOT_FOLDER";
  private static final String pref_WORK = "WORK_FOLDER";
  //
  private String rootPath;
  private String workPath;
  private String basePath;
  //
  private static final Map ShaderMap = new HashMap();
  private static final int ShaderDefault = 5;
  private static final String[] Shaders = {
    // "Custom --->",
    "BaseTimesLightmap", "Cable", "Decal", "DecalModulate",
    "LightMappedGeneric", "Modulate", "MonitorScreen", "Predator",
    "Refract", "ShatteredGlass", "Sprite", "UnlitGeneric",
    "VertexLitGeneric", "Water", };
  //
  private static final Map SurfaceMap = new HashMap();
  private static final int SurfaceDefault = 12;
  private static final String[] Surfaces = {
    // "--- NONE ---",
    // "Custom --->",
    "AlienFlesh", "ArmorFlesh", "BloodyFlesh", "Boulder", "Brick",
    "Chain", "ChainLink", "Computer", "Concrete", "Concrete_Block",
    "Default", "Default_Silent", "Dirt", "Flesh", "Glass", "Grass",
    "Gravel", "Ice", "Ladder", "Metal_Box", "Metal", "MetalGrate",
    "MetalPanel", "MetalVent", "MudSlipperySlime",
    "Player_Control_Clip", "Porcelain", "QuickSand", "Rock", "Slime",
    "SlipperyMetal", "Snow", "SolidMetal", "Tile", "Wade", "Water",
    "WaterMelon", "Wood_Box", "Wood_Crate", "Wood_Furniture",
    "Wood_Panel", "Wood_Plank", "Wood_Solid", "WoodWood_LowDensity", };
  //
  private static final int SIGNATURE_VTF = 0x00465456; // 56 54 46 00 V T F .
  private boolean animated = false;
  private int frameCount = 0;
  //
  private static final String EMPTY_STRING = "";
  private Clip clipBlip;

  /**
   Creates new form GUI
   */
  public GUI () {
    initComponents();

    // Minor hack for better readability of disabled combo boxes;
    UIManager.put( "ComboBox.disabledForeground", Color.RED );
    lafSpinner( nudAlpha );
    lafSpinner( nudFrameRate );
    lafSpinner( nudEnvMapContrast );
    lafSpinner( nudEnvMapSaturation );
    lafSpinner( nudEnvMapFrame );

    int i = 1;
    cmbShader.addItem( "Custom --->" );
    for ( String item : Shaders ) {
      cmbShader.addItem( item );
      ShaderMap.put( item.toUpperCase( locale ), i++ );
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
      SurfaceMap.put( item.toUpperCase( locale ), i++ );
    }
    cmbSurface1.setSelectedIndex( SurfaceDefault );
    cmbSurface2.setSelectedIndex( 0 );

    txtRootFolder.setText( rootPath = preferences.get( pref_ROOT, "" ) );
    txtWorkFolder.setText( workPath = preferences.get( pref_WORK, "" ) );
    showTextureFiles();
  }

  private void btnBaseTexture1ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnBaseTexture1ActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setBaseTexture1( path );
    }
  }// GEN-LAST:event_btnBaseTexture1ActionPerformed

  private void btnBaseTexture2ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnBaseTexture2ActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setBaseTexture2( path );
    }
  }// GEN-LAST:event_btnBaseTexture2ActionPerformed

  private void btnBumpMap1ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnBumpMap1ActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setBumpMap1( path );
    }
  }// GEN-LAST:event_btnBumpMap1ActionPerformed

  private void btnBumpMap2ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnBumpMap2ActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setBumpMap2( path );
    }
  }// GEN-LAST:event_btnBumpMap2ActionPerformed

  private void btnDetailTextureActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnDetailTextureActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setDetailTexture( path );
    }
  }// GEN-LAST:event_btnDetailTextureActionPerformed

  private void btnDuDvMapActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnDuDvMapActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setDuDvMap( path );
    }
  }// GEN-LAST:event_btnDuDvMapActionPerformed

  private void btnEnvMapActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnEnvMapActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setEnvMap( path );
    }
  }// GEN-LAST:event_btnEnvMapActionPerformed

  private void btnEnvMapMaskActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnEnvMapMaskActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setEnvMapMask( path );
    }
  }// GEN-LAST:event_btnEnvMapMaskActionPerformed

  private void btnNormalMapActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnNormalMapActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setNormalMap( path );
    }
  }// GEN-LAST:event_btnNormalMapActionPerformed

  private void btnRootFolderBrowseActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnRootFolderBrowseActionPerformed
    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    int result = fc.showOpenDialog( this );
    if ( JFileChooser.APPROVE_OPTION == result ) {
      rootPath = fc.getSelectedFile().getAbsolutePath();
      txtRootFolder.setText( rootPath );
      preferences.put( pref_ROOT, rootPath );
      showTextureFiles();
    }
  }// GEN-LAST:event_btnRootFolderBrowseActionPerformed

  private void btnToolTextureActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnToolTextureActionPerformed
    String path = selectVTF();
    if ( null != path ) {
      setToolTexture( path );
    }
  }// GEN-LAST:event_btnToolTextureActionPerformed

  private void btnWorkFolderBrowseActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_btnWorkFolderBrowseActionPerformed
    File dir = new File( rootPath );
    JFileChooser fc = new JFileChooser( dir );
    fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    int result = fc.showOpenDialog( this );
    if ( JFileChooser.APPROVE_OPTION == result ) {
      workPath = fc.getSelectedFile().getAbsolutePath();
      txtWorkFolder.setText( workPath );
      preferences.put( pref_WORK, workPath );
      showTextureFiles();
    }
  }// GEN-LAST:event_btnWorkFolderBrowseActionPerformed

  private void chkLockAlphaActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockAlphaActionPerformed
    nudAlpha.setEnabled( !chkLockAlpha.isSelected() );
  }// GEN-LAST:event_chkLockAlphaActionPerformed

  private void chkLockBaseTexture1ActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockBaseTexture1ActionPerformed
    txtBaseTexture1.setEnabled( !chkLockBaseTexture1.isSelected() );
    btnBaseTexture1.setEnabled( !chkLockBaseTexture1.isSelected() );
  }// GEN-LAST:event_chkLockBaseTexture1ActionPerformed

  private void chkLockBaseTexture2ActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockBaseTexture2ActionPerformed
    txtBaseTexture2.setEnabled( !chkLockBaseTexture2.isSelected() );
    btnBaseTexture2.setEnabled( !chkLockBaseTexture2.isSelected() );
  }// GEN-LAST:event_chkLockBaseTexture2ActionPerformed

  private void chkLockBumpMap1ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockBumpMap1ActionPerformed
    txtBumpMap1.setEnabled( !chkLockBumpMap1.isSelected() );
    btnBumpMap1.setEnabled( !chkLockBumpMap1.isSelected() );
  }// GEN-LAST:event_chkLockBumpMap1ActionPerformed

  private void chkLockBumpMap2ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockBumpMap2ActionPerformed
    txtBumpMap2.setEnabled( !chkLockBumpMap2.isSelected() );
    btnBumpMap2.setEnabled( !chkLockBumpMap2.isSelected() );
  }// GEN-LAST:event_chkLockBumpMap2ActionPerformed

  private void chkLockDetailTextureActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockDetailTextureActionPerformed
    txtDetailTexture.setEnabled( !chkLockDetailTexture.isSelected() );
    btnDetailTexture.setEnabled( !chkLockDetailTexture.isSelected() );
  }// GEN-LAST:event_chkLockDetailTextureActionPerformed

  private void chkLockDuDvMapActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockDuDvMapActionPerformed
    txtDuDvMap.setEnabled( !chkLockDuDvMap.isSelected() );
    btnDuDvMap.setEnabled( !chkLockDuDvMap.isSelected() );
  }// GEN-LAST:event_chkLockDuDvMapActionPerformed

  private void chkLockEnvMapActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockEnvMapActionPerformed
    txtEnvMap.setEnabled( !chkLockEnvMap.isSelected() );
    btnEnvMap.setEnabled( !chkLockEnvMap.isSelected() );
  }// GEN-LAST:event_chkLockEnvMapActionPerformed

  private void chkLockEnvMapContrastActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockEnvMapContrastActionPerformed
    nudEnvMapContrast.setEnabled( !chkLockEnvMapContrast.isSelected() );
  }// GEN-LAST:event_chkLockEnvMapContrastActionPerformed

  private void chkLockEnvMapFrameActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockEnvMapFrameActionPerformed
    nudEnvMapFrame.setEnabled( !chkLockEnvMapFrame.isSelected() );
  }// GEN-LAST:event_chkLockEnvMapFrameActionPerformed

  private void chkLockEnvMapMaskActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockEnvMapMaskActionPerformed
    txtEnvMapMask.setEnabled( !chkLockEnvMapMask.isSelected() );
    btnEnvMapMask.setEnabled( !chkLockEnvMapMask.isSelected() );
  }// GEN-LAST:event_chkLockEnvMapMaskActionPerformed

  private void chkLockEnvMapSaturationActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockEnvMapSaturationActionPerformed
    nudEnvMapSaturation.setEnabled( !chkLockEnvMapSaturation.isSelected() );
  }// GEN-LAST:event_chkLockEnvMapSaturationActionPerformed

  private void chkLockFrameRateActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockFrameRateActionPerformed
    nudFrameRate.setEnabled( animated & !chkLockFrameRate.isSelected() );
  }// GEN-LAST:event_chkLockFrameRateActionPerformed

  private void chkLockKeywordsActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockKeywordsActionPerformed
    txtKeywords.setEnabled( !chkLockKeywords.isSelected() );
  }// GEN-LAST:event_chkLockKeywordsActionPerformed

  private void chkLockNormalMapActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockNormalMapActionPerformed
    txtNormalMap.setEnabled( !chkLockNormalMap.isSelected() );
    btnNormalMap.setEnabled( !chkLockNormalMap.isSelected() );
  }// GEN-LAST:event_chkLockNormalMapActionPerformed

  private void chkLockShaderActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockShaderActionPerformed
    cmbShader.setEnabled( !chkLockShader.isSelected() );
    txtShader.setEnabled( !chkLockShader.isSelected()
                          & ( 0 == cmbShader.getSelectedIndex() ) );
  }// GEN-LAST:event_chkLockShaderActionPerformed

  private void chkLockSurface1ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockSurface1ActionPerformed
    cmbSurface1.setEnabled( !chkLockSurface1.isSelected() );
    txtSurface1.setEnabled( !chkLockSurface1.isSelected()
                            & ( 1 == cmbSurface1.getSelectedIndex() ) );
  }// GEN-LAST:event_chkLockSurface1ActionPerformed

  private void chkLockSurface2ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockSurface2ActionPerformed
    cmbSurface2.setEnabled( !chkLockSurface2.isSelected() );
    txtSurface2.setEnabled( !chkLockSurface2.isSelected()
                            & ( 1 == cmbSurface2.getSelectedIndex() ) );
  }// GEN-LAST:event_chkLockSurface2ActionPerformed

  private void chkLockToolTextureActionPerformed (
    java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkLockToolTextureActionPerformed
    txtToolTexture.setEnabled( !chkLockToolTexture.isSelected() );
    btnToolTexture.setEnabled( !chkLockToolTexture.isSelected() );
  }// GEN-LAST:event_chkLockToolTextureActionPerformed

  private void chkOnlyMissingActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_chkOnlyMissingActionPerformed
    showTextureFiles();
  }// GEN-LAST:event_chkOnlyMissingActionPerformed

  private void cmbShaderActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_cmbShaderActionPerformed
    txtShader.setEnabled( 0 == cmbShader.getSelectedIndex() );
  }// GEN-LAST:event_cmbShaderActionPerformed

  private void cmbSurface1ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_cmbSurface1ActionPerformed
    txtSurface1.setEnabled( 1 == cmbSurface1.getSelectedIndex() );
  }// GEN-LAST:event_cmbSurface1ActionPerformed

  private void cmbSurface2ActionPerformed ( java.awt.event.ActionEvent evt ) {// GEN-FIRST:event_cmbSurface2ActionPerformed
    txtSurface2.setEnabled( 1 == cmbSurface2.getSelectedIndex() );
  }// GEN-LAST:event_cmbSurface2ActionPerformed

  @Override
  public boolean dispatchKeyEvent ( KeyEvent ke ) {
    if ( KeyEvent.KEY_PRESSED == ke.getID() ) {
      System.out.println( ke.toString() );
      switch ( ke.getKeyCode() ) {
        case KeyEvent.VK_F1:
          generateVMT();
          return true;

        case KeyEvent.VK_F2:
          setAllLocks( true );
          return true;

        case KeyEvent.VK_F3:
          setAllLocks( false );
          return true;

        case KeyEvent.VK_F4:
          toggleAllLocks();
          return true;

        case KeyEvent.VK_F5:
          showTextureFiles();
          return true;

        case KeyEvent.VK_F6:
          resetAllInput();
          return true;
      }
    }
    return false;
  }

  private void generateVMT () {
    if ( -1 == lstFiles.getSelectedIndex() ) {
      return;
    }



    String value = lstFiles.getSelectedValue().toString();
    String path = FilenameUtils.separatorsToSystem( FilenameUtils.concat(
      workPath, FilenameUtils.getBaseName( value ) + ".VMT" ) );


    File fileVMT = new File( path );
    try ( PrintWriter out = new PrintWriter( fileVMT, "UTF-8" ) ) {
      value = ( 0 == cmbShader.getSelectedIndex() ) ? txtShader.getText()
              : cmbShader.getSelectedItem().toString();

      // write the shader
      out.printf( "\"%s\"%n{%n", value );

      writeSpinner( out, nudAlpha, 1F, "$alpha", 4 );
      writeSpinner( out, nudEnvMapContrast, 0F, "$envMapContrast", 3 );
      writeSpinner( out, nudEnvMapContrast, 0F, "$envMapSaturation", 2 );
      writeSpinner( out, nudEnvMapFrame, 0, "$envMapFrame", 3 );





      // write surfaces
      int index = cmbSurface1.getSelectedIndex();
      if ( 0 != index ) {
        value = ( 1 == index ) ? txtSurface1.getText() : cmbSurface1
          .getSelectedItem().toString();

        writeKeyValue( true, out, "$surfaceProp", value, 3 );
      }

      index = cmbSurface2.getSelectedIndex();
      if ( 0 != index ) {
        value = ( 1 == index ) ? txtSurface2.getText() : cmbSurface2
          .getSelectedItem().toString();

        writeKeyValue( true, out, "$surfaceProp2", value, 3 );
      }


      writeKeyValue( !( value = txtKeywords.getText() ).isEmpty(), out,
                     "%keywords", value, 3 );

      writeKeyValue( !( value = txtToolTexture.getText() ).isEmpty(), out,
                     "%toolTexture", value, 3 );
      writeKeyValue( !( value = txtBaseTexture1.getText() ).isEmpty(), out,
                     "$baseTexture", value, 3 );
      writeKeyValue( !( value = txtBaseTexture2.getText() ).isEmpty(), out,
                     "$baseTexture2", value, 3 );
      writeKeyValue( !( value = txtDetailTexture.getText() ).isEmpty(), out,
                     "$detail", value, 4 );
      writeKeyValue( !( value = txtBumpMap1.getText() ).isEmpty(), out,
                     "$bumpMap", value, 3 );
      writeKeyValue( !( value = txtBumpMap2.getText() ).isEmpty(), out,
                     "$bumpMap2", value, 3 );
      writeKeyValue( !( value = txtEnvMap.getText() ).isEmpty(), out,
                     "$envMap", value, 3 );
      writeKeyValue( !( value = txtEnvMapMask.getText() ).isEmpty(), out,
                     "$envMapMask", value, 3 );
      writeKeyValue( !( value = txtNormalMap.getText() ).isEmpty(), out,
                     "$normalMap", value, 3 );
      writeKeyValue( !( value = txtDuDvMap.getText() ).isEmpty(), out,
                     "$DuDvMap", value, 3 );



      value = "1";
      writeKeyValue( chkFlagAdditive.isSelected(), out,
                     "$additive", value, 3 );
      writeKeyValue( chkFlagAlphaTest.isSelected(), out,
                     "$alphaTest", value, 3 );
      writeKeyValue( chkFlagIgnoreZ.isSelected(), out,
                     "$ignoreZ", value, 3 );
      writeKeyValue( chkFlagNoCull.isSelected(), out,
                     "$noCull", value, 4 );
      writeKeyValue( chkFlagNoDecal.isSelected(), out,
                     "$noDecal", value, 3 );
      writeKeyValue( chkFlagNoLOD.isSelected(), out,
                     "$noLOD", value, 3 );
      writeKeyValue( chkFlagPhong.isSelected(), out,
                     "$phong", value, 4 );
      writeKeyValue( chkFlagSelfIllum.isSelected(), out,
                     "$selfIllum", value, 3 );
      writeKeyValue( chkFlagTranslucent.isSelected(), out,
                     "$translucent", value, 3 );
      writeKeyValue( chkFlagVertexAlpha.isSelected(), out,
                     "$vertexAlpha", value, 3 );
      writeKeyValue( chkFlagVertexColor.isSelected(), out,
                     "$vertexColor", value, 3 );



      writeKeyValue( chkCompileClip.isSelected(), out,
                     "%compileClip", value, 3 );
      writeKeyValue( chkCompileDetail.isSelected(), out,
                     "%compileDetail", value, 3 );
      writeKeyValue( chkCompileFog.isSelected(), out,
                     "%compileFog", value, 3 );
      writeKeyValue( chkCompileHint.isSelected(), out,
                     "%compileHint", value, 3 );
      writeKeyValue( chkCompileLadder.isSelected(), out,
                     "%compileLadder", value, 3 );
      writeKeyValue( chkCompileNoDraw.isSelected(), out,
                     "%compileNoDraw", value, 3 );
      writeKeyValue( chkCompileNoLight.isSelected(), out,
                     "%compileNoLight", value, 3 );
      writeKeyValue( chkCompileNonSolid.isSelected(), out,
                     "%compileNonSolid", value, 2 );
      writeKeyValue( chkCompileNpcClip.isSelected(), out,
                     "%compileNpcClip", value, 3 );
      writeKeyValue( chkCompilePassBullets.isSelected(), out,
                     "%compilePassBullets", value, 2 );
      writeKeyValue( chkCompilePlayerClip.isSelected(), out,
                     "%compilePlayerClip", value, 2 );
      writeKeyValue( chkCompilePlayerControlClip.isSelected(), out,
                     "%compilePlayerControlClip", value, 2 );
      writeKeyValue( chkCompileSkip.isSelected(), out,
                     "%compileSkip", value, 3 );
      writeKeyValue( chkCompileSky.isSelected(), out,
                     "%compileSky", value, 3 );
      writeKeyValue( chkCompileTrigger.isSelected(), out,
                     "%compileTrigger", value, 3 );



      // animation code
      if ( animated ) {
        value = nudFrameRate.getValue().toString();
        out.printf( "%n\t\"proxies\"%n" );
        out.printf( "\t{%n" );
        out.printf( "\t\t\"animatedTexture\"%n" );
        out.printf( "\t\t{%n" );
        out.printf( "\t\t\t\"animatedTextureVar\"\t\t\"$baseTexture\"%n" );
        out.printf( "\t\t\t\"animatedTextureFrameNumVar\"\t\"$frame\"%n" );
        out.printf( "\t\t\t\"animatedTextureFrameRate\" \t\"%s\"%n", value );
        out.printf( "\t\t}%n" );
        out.printf( "\t}%n" );
      }

      out.print( "}" );
      out.flush();

      try {
        URL url = this.getClass().getClassLoader().getResource( "blip.wav" );
        AudioInputStream audioIn = AudioSystem.getAudioInputStream( url );
        clipBlip = AudioSystem.getClip();
        clipBlip.open( audioIn );
        clipBlip.start();
      } catch ( LineUnavailableException | UnsupportedAudioFileException |
                IOException ex ) {
        logger.log( Level.SEVERE, null, ex );
      }

    } catch ( FileNotFoundException | UnsupportedEncodingException ex ) {
      logger.log( Level.SEVERE, null, ex );
    }

  }

  /**
   This method is called from within the constructor to initialize the form.
   WARNING: Do NOT modify this code. The content of this method is always
   regenerated by the Form Editor.
   */
  @SuppressWarnings ( "unchecked" )
  // <editor-fold defaultstate="collapsed"
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
    chkFlagAdditive = new javax.swing.JCheckBox();
    chkFlagAlphaTest = new javax.swing.JCheckBox();
    chkFlagNoCull = new javax.swing.JCheckBox();
    chkFlagNoDecal = new javax.swing.JCheckBox();
    chkFlagNoLOD = new javax.swing.JCheckBox();
    chkFlagTranslucent = new javax.swing.JCheckBox();
    chkFlagVertexAlpha = new javax.swing.JCheckBox();
    chkFlagVertexColor = new javax.swing.JCheckBox();
    chkFlagIgnoreZ = new javax.swing.JCheckBox();
    chkFlagPhong = new javax.swing.JCheckBox();
    chkFlagSelfIllum = new javax.swing.JCheckBox();
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
    jPanel3 = new javax.swing.JPanel();
    chkLockEnvMapContrast = new javax.swing.JCheckBox();
    nudEnvMapContrast = new javax.swing.JSpinner();
    jLabel24 = new javax.swing.JLabel();
    chkLockFrameRate = new javax.swing.JCheckBox();
    jLabel7 = new javax.swing.JLabel();
    nudFrameRate = new javax.swing.JSpinner();
    nudAlpha = new javax.swing.JSpinner();
    jLabel19 = new javax.swing.JLabel();
    chkLockAlpha = new javax.swing.JCheckBox();
    chkLockEnvMapSaturation = new javax.swing.JCheckBox();
    jLabel26 = new javax.swing.JLabel();
    nudEnvMapSaturation = new javax.swing.JSpinner();
    chkLockEnvMapFrame = new javax.swing.JCheckBox();
    jLabel27 = new javax.swing.JLabel();
    nudEnvMapFrame = new javax.swing.JSpinner();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("VMTGen");
    setName("frmGUI"); // NOI18N
    setResizable(false);

    panFolders.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Settings"));

    jLabel1.setText("Root Folder");
    jLabel1.setToolTipText("<html>This should point to your materials folder.<br />\nFor example, Steam\\SteamApps\\common\\Team Fortress 2\\tf\\materials</html>");

    txtRootFolder.setEditable(false);
    txtRootFolder.setBackground(java.awt.SystemColor.text);
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
    txtWorkFolder.setName(""); // NOI18N
    txtWorkFolder.setPreferredSize(new java.awt.Dimension(59, 25));

    jLabel2.setText("Working Folder");
    jLabel2.setToolTipText("<html>This should point to where your custom textures are.<br />\n(and where your material files will be)<br />\nFor example, Steam\\SteamApps\\common\\Team Fortress 2\\tf\\materials\\custom\\OuterSpace");

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

    chkFlagAdditive.setText("Additive");
    chkFlagAdditive.setToolTipText("<html>Add the material's colour values to the existing image, instead of performing a multiplication.<br />\nThis means, among other things, that the material will always brighten the world.<br />\nThis is useful for effects like volumetric dust, light sprites, etc...</html>");

    chkFlagAlphaTest.setText("Alpha Test");
    chkFlagAlphaTest.setToolTipText("<html>Translucency can sometimes cause a material to flicker, or cause sorting issues with nearby surfaces.<br />\nIn both cases, consider using $alphatest instead of $translucent when this happens.<br />\nIt drastically lowers quality, but will usually resolve the issue and is much faster to draw.<br />\nIt will also cast flashlight shadows, unlike translucents.</html>");

    chkFlagNoCull.setText("No Cull");
    chkFlagNoCull.setToolTipText("<html>Makes the material appear on the reverse side of the surface it is applied to.<br />\nGenerally only useful when used in conjunction with $translucent or $alpha.<br />\nNote: Has no effect on world brushes (so tie each one to func_detail).<br />\nBug: Cannot be used with $translucent on models. Use $alphatest instead.</html>");

    chkFlagNoDecal.setText("No Decal");
    chkFlagNoDecal.setToolTipText("N/A");

    chkFlagNoLOD.setForeground(new java.awt.Color(10, 36, 106));
    chkFlagNoLOD.setText("No LOD");
    chkFlagNoLOD.setToolTipText("No Level of Detail");

    chkFlagTranslucent.setForeground(new java.awt.Color(10, 36, 106));
    chkFlagTranslucent.setText("Translucent");
    chkFlagTranslucent.setToolTipText("<html>Specifies that the material should be partially see-through.<br />\nThe alpha channel of $basetexture is used to decide translucency per-pixel.<br />\nAny object that has a $translucent material does not affect VIS, and can be seen through by NPCs from any angle.</html>");

    chkFlagVertexAlpha.setText("Vertex Alpha");
    chkFlagVertexAlpha.setToolTipText("<html>Makes the surface derive its alpha values from per-vertex data provided by the engine.<br />\nOnly particles and decals are known to modify their vertex data, but it should be possible to implement your own scenarios too.<br />\nVertex alpha cannot be compiled into a model and is currently unfunctional in Counter-Strike: Global Offensive; Using it will result in the material to turn completely black in-game.</html>");

    chkFlagVertexColor.setText("Vertex Color");
    chkFlagVertexColor.setToolTipText("<html>Makes the surface derive its color values from per-vertex data provided by the engine.<br />\nOnly particles and decals are known to modify their vertex data, but it should be possible to implement your own scenarios too.<br />\nVertex color cannot be compiled into a model and is currently unfunctional in Counter-Strike: Global Offensive; Using it will result in the material to turn completely black in-game.</html>");

    chkFlagIgnoreZ.setText("Ignore Z-Axis");
    chkFlagIgnoreZ.setToolTipText("<html>Used for decals and sprays.<br />\nCannot be used on models to prevent cheating.</html>");

    chkFlagPhong.setText("Phong");
    chkFlagPhong.setToolTipText("Diffuse reflections. It is only available with the VertexLitGeneric shader.");

    chkFlagSelfIllum.setText("Self Illuminated");
    chkFlagSelfIllum.setToolTipText("<html>Makes a material glow in the dark. Shaders commonly support this effect.<br />\nThe effect is masked by default by the alpha channel of $basetexture<br />\nWherever the mask is located, white areas are self-illuminated while black areas are not.<br />\nWarning: Cannot be used with $translucent or similar values on models. Use UnlitGeneric shader instead.</html>\n");

    javax.swing.GroupLayout panFlagsLayout = new javax.swing.GroupLayout(panFlags);
    panFlags.setLayout(panFlagsLayout);
    panFlagsLayout.setHorizontalGroup(
      panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panFlagsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(chkFlagIgnoreZ)
          .addComponent(chkFlagNoCull)
          .addComponent(chkFlagNoDecal)
          .addComponent(chkFlagAlphaTest)
          .addComponent(chkFlagNoLOD)
          .addComponent(chkFlagAdditive))
        .addGap(33, 33, 33)
        .addGroup(panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(chkFlagPhong)
          .addComponent(chkFlagVertexColor)
          .addComponent(chkFlagTranslucent)
          .addComponent(chkFlagVertexAlpha)
          .addComponent(chkFlagSelfIllum))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    panFlagsLayout.setVerticalGroup(
      panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panFlagsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkFlagAdditive)
          .addComponent(chkFlagPhong))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkFlagAlphaTest)
          .addComponent(chkFlagSelfIllum))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkFlagIgnoreZ)
          .addComponent(chkFlagTranslucent))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkFlagNoCull)
          .addComponent(chkFlagVertexAlpha))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chkFlagNoDecal)
          .addComponent(chkFlagVertexColor))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(chkFlagNoLOD)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    panTexture.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Textures"));

    jLabel8.setText("Base Texture 1");
    jLabel8.setToolTipText("Defines an albedo texture, in most cases, this is REQUIRED.");

    jLabel9.setText("Base Texture 2");
    jLabel9.setToolTipText("Defines a secondary albedo texture, commonly used for dual surfaces such as grass/dirt blends.");

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
    jLabel10.setToolTipText("<html>Specifies a texture with which will add high-resolution detail when the material is viewed up close,<br />\nby darkening or lightening the albedo texture appropriately.</html>");

    txtDetailTexture.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    btnDetailTexture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_16x16.png"))); // NOI18N
    btnDetailTexture.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnDetailTextureActionPerformed(evt);
      }
    });

    jLabel11.setText("Tool Texture");
    jLabel11.setToolTipText("<html>Used to blend texture previews in Hammer Editor.<br />\nWithout a tooltexture, Hammer will only show your first $basetexture,<br />\nmaking it impossible to see the blend without compiling.");

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
    jLabel12.setToolTipText("<html>Specifies a texture that will provide three-dimensional lighting information for a material.<br />\nThe texture is a bump map, but the process it is used for is called normal mapping.<br />\nThe two terms are often used interchangeably, however.<br />\nWarning: $bumpmap will disable prop_static's per-vertex lighting.<br />\nNot enough data is stored in the vertices for normal mapping, so the engine has no choice but to fall back.<br />\nNote: In the Water shader, $bumpmap is for a DX8 du/dv map. Use $normalmap instead.</html>");

    jLabel13.setText("Bump Map 2");
    jLabel13.setToolTipText("<html>Specifies a texture that will provide three-dimensional lighting information for a material.<br />\nThe texture is a bump map, but the process it is used for is called normal mapping.<br />\nThe two terms are often used interchangeably, however.<br />\nWarning: $bumpmap will disable prop_static's per-vertex lighting.<br />\nNot enough data is stored in the vertices for normal mapping, so the engine has no choice but to fall back.<br />\nNote: In the Water shader, $bumpmap is for a DX8 du/dv map. Use $normalmap instead.</html>");

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
    jLabel14.setToolTipText("<html>Creates specular reflections, which are seen on smooth surfaces.<br />\nIt does this by defining an \"environment map\" (specifically a cubemap) to draw as a reflection;<br />\nnormally that of the nearest env_cubemap entity. The reflection is not dynamic.<br />\nThe other form of reflection supported by Source is the diffuse phong type.</html>");

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
    jLabel15.setToolTipText("<html>Defines a specular mask which affects how strongly each pixel of a material reflects light from the $envmap.<br />\nThe mask should be a greyscale image in which entirely reflective areas are white and entirely matte areas are black.<br />\nFor diffuse type specularity which does not rely on $envmap, see $phong.<br />\nWarning: $envmapmask will not work in model materials using $bumpmap.</html>");

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
    jLabel16.setToolTipText("Normal Maps are used to simulate three-dimensional details on a two-dimensional surface by manipulating its lighting.");

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
    jLabel17.setToolTipText("<html>Note: $dudvmap is now defunct and is replaced by the <b>Refract</b> shader.<br />\nThough, the Water shader still uses a du/dv map for $bumpmap.<br />\ndu/dv maps are used for DirectX 8 refractions.</html>");

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
    jLabel3.setToolTipText("Determines how an object or texture should be drawn");

    chkLockShader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockShader.setRequestFocusEnabled(false);
    chkLockShader.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockShader.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockShaderActionPerformed(evt);
      }
    });

    jLabel4.setText("Surface 1");
    jLabel4.setToolTipText("Defines the physical properties of an object including friction and density, collision/footstep sounds, the effect of bullet impacts and, if the object is destructible, health and gib type.");

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
    jLabel5.setToolTipText("Defines the physical properties of an object including friction and density, collision/footstep sounds, the effect of bullet impacts and, if the object is destructible, health and gib type.");

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
    jLabel6.setToolTipText("<html>Used to set a keyword filter that can be filtered in hammer for easier texture finding.<br />\neach keyword should be separated by a comma.<br />\nVMTGen automatically determines keywords based on the texture's filename, by converting hyphens and underscores to commas.</html>");

    txtKeywords.setDisabledTextColor(new java.awt.Color(255, 0, 0));

    chkLockKeywords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockKeywords.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockKeywords.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockKeywordsActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout panOptionsLayout = new javax.swing.GroupLayout(panOptions);
    panOptions.setLayout(panOptionsLayout);
    panOptionsLayout.setHorizontalGroup(
      panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panOptionsLayout.createSequentialGroup()
        .addContainerGap()
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
            .addComponent(txtKeywords))
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
              .addComponent(txtSurface2, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
              .addComponent(txtSurface1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(txtShader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    panFiles.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Texture Files"));

    chkOnlyMissing.setText("Only Missing");
    chkOnlyMissing.setToolTipText("If checked, only VTF texures that do not have their associated VMT material files present will be shown in the list below.");
    chkOnlyMissing.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkOnlyMissingActionPerformed(evt);
      }
    });

    lstFiles.setModel(new DefaultListModel());
    lstFiles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    lstFiles.setPreferredSize(null);
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
      .addGroup(panFilesLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(chkOnlyMissing)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );
    panFilesLayout.setVerticalGroup(
      panFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panFilesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(chkOnlyMissing)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
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
    chkCompileTrigger.setToolTipText("Compiles the texture as a TRIGGER texture, meaning your texture will behave the same as the tools/toolstrigger.");

    chkCompileSky.setText("Sky");
    chkCompileSky.setToolTipText("Compiles the texture as a SKY texture, meaning your texture will behave as a SKYBOX.");

    chkCompileSkip.setText("Skip");
    chkCompileSkip.setToolTipText("Compiles the texture as a SKIP texture, meaning your texture will behave the same as the tools/toolsskip.");

    chkCompilePlayerClip.setText("Player Clip");
    chkCompilePlayerClip.setToolTipText("Compiles the texture as a PLAYER CLIP texture, meaning your texture will behave the same as the tools/toolsplayerclip.");

    chkCompileNoDraw.setText("No Draw");
    chkCompileNoDraw.setToolTipText("Compiles the texture as a NO DRAW texture, meaning your texture will behave the same as the tools/toolsnodraw.");

    chkCompilePassBullets.setText("Pass Bullets");
    chkCompilePassBullets.setToolTipText("Compiles the texture as non-solid to bullets, meaning your texture can be shot through, which is useful for foliage and fences.");

    chkCompileOrigin.setText("Origin");
    chkCompileOrigin.setToolTipText("N/A");

    chkCompileNoLight.setText("No Light");
    chkCompileNoLight.setToolTipText("Description not available, but this compile flag was found on tools/areaportal.");

    chkCompileNpcClip.setText("NPC Clip");
    chkCompileNpcClip.setToolTipText("Compiles the texture as a NPC CLIP texture, meaning your texture will behave the same as the tools/toolsnpcclip.");

    chkCompileLadder.setText("Ladder");
    chkCompileLadder.setToolTipText("Compiles the texture as a LADDER texture, meaning your texture will behave the same as the tools/toolsladder.");

    chkCompileHint.setText("Hint");
    chkCompileHint.setToolTipText("Compiles the texture as a HINT texture, meaning your texture will behave the same as the tools/toolshint.");

    chkCompileNonSolid.setText("Non-Solid");
    chkCompileNonSolid.setToolTipText("Compiles the texture as a NON-SOLID texture, brushes with this texture will be compiled as Non-Solid, which is useful for lighting and foliage effects.");

    chkCompileDetail.setText("Detail");
    chkCompileDetail.setToolTipText("N/A");

    chkCompileClip.setText("Clip");
    chkCompileClip.setToolTipText("Compiles the texture as a CLIP texture, meaning your texture will behave the same as the tools/toolsclip.");

    chkCompileFog.setText("Fog");
    chkCompileFog.setToolTipText("Compiles the texture as a FOG texture, meaning your texture will behave the same as the tools/toolsfog.");

    chkCompilePlayerControlClip.setText("Player Control Clip");
    chkCompilePlayerControlClip.setToolTipText("Compiles the texture as a PLAYER CONTROL CLIP texture, meaning your texture will behave the same as the tools/toolsplayercontrolclip.");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addGap(51, 51, 51)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(chkCompileOrigin)
              .addComponent(chkCompileNpcClip)
              .addComponent(chkCompilePassBullets)
              .addComponent(chkCompilePlayerClip)
              .addComponent(chkCompilePlayerControlClip)
              .addComponent(chkCompileSkip)
              .addComponent(chkCompileSky)
              .addComponent(chkCompileTrigger)))
          .addComponent(chkCompileFog)))
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

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Misc"));

    chkLockEnvMapContrast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockEnvMapContrast.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockEnvMapContrast.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockEnvMapContrastActionPerformed(evt);
      }
    });

    nudEnvMapContrast.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
    nudEnvMapContrast.setToolTipText("");

    jLabel24.setText("Environment Map Contrast");
    jLabel24.setToolTipText("<html>Controls the contrast of the reflection.<br />\n0 is natural contrast, while 1 is the full squaring of the color (i.e. color*color).<br />\nTip: Use higher contrasts to diminish relatively darker areas and increase \"hot spots\". <br />\nNote: Will not work when Phong is enabled.</html>");

    chkLockFrameRate.setEnabled(false);
    chkLockFrameRate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockFrameRate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockFrameRate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockFrameRateActionPerformed(evt);
      }
    });

    jLabel7.setText("Frame Rate");
    jLabel7.setToolTipText("How many frames per second to render an Animated Texture");

    nudFrameRate.setModel(new javax.swing.SpinnerNumberModel(0, 0, 999999, 1));
    nudFrameRate.setEnabled(false);
    nudFrameRate.setPreferredSize(new java.awt.Dimension(80, 18));

    nudAlpha.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
    nudAlpha.setPreferredSize(new java.awt.Dimension(80, 18));

    jLabel19.setText("Alpha");
    jLabel19.setToolTipText("<html>Scales the opacity of an entire material by the given value.<br />\n1 is entirely opaque, 0 is invisible.<br />\nIf any material on a brush has alpha, the brush will stop affecting VIS and become entirely transparent to NPCs from every angle.<br />\nThis may be used with the <b>translucent</b> flag.</html>");

    chkLockAlpha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked_16x16.png"))); // NOI18N
    chkLockAlpha.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/locked_16x16.png"))); // NOI18N
    chkLockAlpha.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockAlphaActionPerformed(evt);
      }
    });

    chkLockEnvMapSaturation.setIcon(new javax.swing.ImageIcon("C:\\Users\\Xyphos\\Documents\\NetBeansProjects\\VMTGen\\src\\main\\resources\\unlocked_16x16.png")); // NOI18N
    chkLockEnvMapSaturation.setSelectedIcon(new javax.swing.ImageIcon("C:\\Users\\Xyphos\\Documents\\NetBeansProjects\\VMTGen\\src\\main\\resources\\locked_16x16.png")); // NOI18N
    chkLockEnvMapSaturation.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockEnvMapSaturationActionPerformed(evt);
      }
    });

    jLabel26.setText("Environment Map Saturation");
    jLabel26.setToolTipText("<html>Controls the colour saturation of the reflection.<br />\n0 is greyscale, while 1 is natural saturation.<br />\nNote: Will not work when Phong is enabled.</html>");

    nudEnvMapSaturation.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
    nudEnvMapSaturation.setToolTipText("");

    chkLockEnvMapFrame.setEnabled(false);
    chkLockEnvMapFrame.setIcon(new javax.swing.ImageIcon("C:\\Users\\Xyphos\\Documents\\NetBeansProjects\\VMTGen\\src\\main\\resources\\unlocked_16x16.png")); // NOI18N
    chkLockEnvMapFrame.setSelectedIcon(new javax.swing.ImageIcon("C:\\Users\\Xyphos\\Documents\\NetBeansProjects\\VMTGen\\src\\main\\resources\\locked_16x16.png")); // NOI18N
    chkLockEnvMapFrame.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chkLockEnvMapFrameActionPerformed(evt);
      }
    });

    jLabel27.setText("Environment Map Frame");
    jLabel27.setToolTipText("The frame to start an animated cubemap on.");

    nudEnvMapFrame.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));
    nudEnvMapFrame.setEnabled(false);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(chkLockAlpha)
          .addComponent(chkLockEnvMapContrast)
          .addComponent(chkLockEnvMapSaturation)
          .addComponent(chkLockEnvMapFrame)
          .addComponent(chkLockFrameRate))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel19)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(nudAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel7)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(nudFrameRate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel27)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(nudEnvMapFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel26)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
            .addComponent(nudEnvMapContrast, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel24)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(nudEnvMapSaturation, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(nudAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel19))
          .addComponent(chkLockAlpha))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(nudEnvMapSaturation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel24))
          .addComponent(chkLockEnvMapContrast))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(nudEnvMapContrast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel26))
          .addComponent(chkLockEnvMapSaturation))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(nudEnvMapFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel27))
          .addComponent(chkLockEnvMapFrame))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(chkLockFrameRate)
          .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel7)
            .addComponent(nudFrameRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(panFolders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(panTexture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(panFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(panOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(panFlags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(panFolders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(panFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(panOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(panTexture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(panFlags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void lafSpinner ( JSpinner s ) {
    ( ( JSpinner.NumberEditor ) s.getEditor() ).getTextField()
      .setDisabledTextColor( Color.RED );
  }

  private void lstFilesValueChanged ( javax.swing.event.ListSelectionEvent evt ) {// GEN-FIRST:event_lstFilesValueChanged
    if ( !evt.getValueIsAdjusting() && ( -1 != lstFiles.getSelectedIndex() ) ) {
      String file = lstFiles.getSelectedValue().toString();

      // set keywords based on file name
      setKeywords( FilenameUtils.getBaseName( file ).replace( "_", "," )
        .replace( "-", "," ) );

      String path = FilenameUtils.separatorsToUnix(
        FilenameUtils.concat( basePath,
                              FilenameUtils.getBaseName( file ) ) )
        .replaceFirst( "/",
                       "" );

      setBaseTexture1( path );

      // read the vtf header
      file = FilenameUtils.concat( workPath, file );
      File fileVTF = new File( file );

      try ( LittleEndianDataInputStream in = new LittleEndianDataInputStream(
        new FileInputStream( fileVTF ) ) ) {

        int sig = in.readInt();
        if ( SIGNATURE_VTF != sig ) {
          throw new IOException( "Not a VTF file" );
        }

        if ( 0x10 != in.skipBytes( 0x10 ) ) {
          throw new IOException( "skip failure" );
        }

        int flags = in.readInt();
        frameCount = in.readShort();
        in.close(); // don't need any more information

        chkFlagNoLOD.setSelected( 0 != ( 0x200 & flags ) );
        chkFlagTranslucent.setSelected( 0 != ( 0x3000 & flags ) );

        if ( animated = ( 1 < frameCount ) ) {
          setFrameRate( frameCount );
          ( ( SpinnerNumberModel ) nudEnvMapFrame.getModel() )
            .setMaximum( frameCount );
        }

        nudFrameRate.setEnabled( animated
                                 & !chkLockFrameRate.isSelected() );
        nudEnvMapFrame.setEnabled( animated
                                   & !chkLockEnvMapFrame.isSelected() );

        chkLockFrameRate.setEnabled( animated );
        chkLockEnvMapFrame.setEnabled( animated );

      } catch ( FileNotFoundException ex ) {
        logger.log( Level.SEVERE, null, ex );
      } catch ( IOException ex ) {
        logger.log( Level.SEVERE, null, ex );
      }
    }
  }// GEN-LAST:event_lstFilesValueChanged

  public static void main ( String args[] ) {
    try {
      javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager
        .getCrossPlatformLookAndFeelClassName() );

    } catch ( ClassNotFoundException | InstantiationException |
              IllegalAccessException | UnsupportedLookAndFeelException ex ) {
      logger.log( Level.SEVERE, null, ex );
    }

    /*
     Create and display the form
     */ java.awt.EventQueue.invokeLater( new Runnable() {
      @Override
      public void run () {
        GUI gui = new GUI();
        gui.pack();

        // Global keyboard hook
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
          .addKeyEventDispatcher( gui );

        gui.setVisible( true );
      }
    } );
  }

  private void resetAllInput () {
    setShaderIndex( ShaderDefault );
    setSurface1Index( SurfaceDefault );
    setSurface2Index( 0 );
    setKeywords( EMPTY_STRING );
    setFrameRate( 0 );
    setBaseTexture1( EMPTY_STRING );
    setBaseTexture2( EMPTY_STRING );
    setDetailTexture( EMPTY_STRING );
    setToolTexture( EMPTY_STRING );
    setBumpMap1( EMPTY_STRING );
    setBumpMap2( EMPTY_STRING );
    setEnvMap( EMPTY_STRING );
    setEnvMapMask( EMPTY_STRING );
    setNormalMap( EMPTY_STRING );
    setDuDvMap( EMPTY_STRING );

    setAlpha( 1F );
    setEnvMapContrast( 0F );
    setEnvMapSaturation( 0F );
    setEnvMapFrame( 0 );

    chkFlagAdditive.setSelected( false );
    chkFlagAlphaTest.setSelected( false );
    chkFlagIgnoreZ.setSelected( false );
    chkFlagPhong.setSelected( false );
    chkFlagNoCull.setSelected( false );
    chkFlagNoDecal.setSelected( false );
    chkFlagSelfIllum.setSelected( false );
    chkFlagVertexAlpha.setSelected( false );
    chkFlagVertexColor.setSelected( false );

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

  private String selectVTF () {
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
        .replaceFirst( "/",
                       "" );
    }

    return null;
  }

  private void setAllLocks ( boolean locked ) {
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

    chkLockAlpha.setSelected( locked );
    nudAlpha.setEnabled( !locked );

    chkLockEnvMapContrast.setSelected( locked );
    nudEnvMapContrast.setEnabled( !locked );

    chkLockEnvMapSaturation.setSelected( locked );
    nudEnvMapSaturation.setEnabled( !locked );

    chkLockEnvMapFrame.setSelected( locked );
    nudEnvMapFrame.setEnabled( !locked & animated );
  }

  private void setAlpha ( float i ) {
    if ( !chkLockAlpha.isSelected() ) {
      nudAlpha.setValue( i );
    }
  }

  private void setBaseTexture1 ( String path ) {
    if ( !chkLockBaseTexture1.isSelected() ) {
      txtBaseTexture1.setText( path );
    }
  }

  private void setBaseTexture2 ( String path ) {
    if ( !chkLockBaseTexture2.isSelected() ) {
      txtBaseTexture2.setText( path );
    }
  }

  private void setBumpMap1 ( String path ) {
    if ( !chkLockBumpMap1.isSelected() ) {
      txtBumpMap1.setText( path );
    }
  }

  private void setBumpMap2 ( String path ) {
    if ( !chkLockBumpMap2.isSelected() ) {
      txtBumpMap2.setText( path );
    }
  }

  private void setDetailTexture ( String path ) {
    if ( !chkLockDetailTexture.isSelected() ) {
      txtDetailTexture.setText( path );
    }
  }

  private void setDuDvMap ( String path ) {
    if ( !chkLockDuDvMap.isSelected() ) {
      txtDuDvMap.setText( path );
    }
  }

  private void setEnvMap ( String path ) {
    if ( !chkLockEnvMap.isSelected() ) {
      txtEnvMap.setText( path );
    }
  }

  private void setEnvMapContrast ( float val ) {
    if ( !chkLockEnvMapContrast.isSelected() ) {
      ( ( SpinnerNumberModel ) nudEnvMapContrast.getModel() ).setValue( val );
    }
  }

  private void setEnvMapFrame ( int val ) {
    if ( !chkLockEnvMapFrame.isSelected() ) {
      ( ( SpinnerNumberModel ) nudEnvMapFrame.getModel() ).setValue( val );
    }
  }

  private void setEnvMapMask ( String path ) {
    if ( !chkLockEnvMapMask.isSelected() ) {
      txtEnvMapMask.setText( path );
    }
  }

  private void setEnvMapSaturation ( float val ) {
    if ( !chkLockEnvMapSaturation.isSelected() ) {
      ( ( SpinnerNumberModel ) nudEnvMapSaturation.getModel() ).setValue( val );
    }
  }

  private void setFrameRate ( int i ) {
    if ( !chkLockFrameRate.isSelected() ) {
      nudFrameRate.setValue( i );
    }
  }

  private void setKeywords ( String keywords ) {
    if ( !chkLockKeywords.isSelected() ) {
      txtKeywords.setText( keywords );
    }
  }

  private void setNormalMap ( String path ) {
    if ( !chkLockNormalMap.isSelected() ) {
      txtNormalMap.setText( path );
    }
  }

  private void setShaderIndex ( int i ) {
    if ( !chkLockSurface1.isSelected() ) {
      cmbShader.setSelectedIndex( i );
    }
  }

  private void setSurface1Index ( int i ) {
    if ( !chkLockSurface1.isSelected() ) {
      cmbSurface1.setSelectedIndex( i );
    }
  }

  private void setSurface2Index ( int i ) {
    if ( !chkLockSurface2.isSelected() ) {
      cmbSurface2.setSelectedIndex( i );
    }
  }

  private void setToolTexture ( String path ) {
    if ( !chkLockToolTexture.isSelected() ) {
      txtToolTexture.setText( path );
    }
  }

  private void showTextureFiles () {
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
          //name = FilenameUtils.getBaseName( full );
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
          if ( FilenameUtils.getBaseName( itr.next() ).equalsIgnoreCase(
            baseName ) ) {
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

  private void toggleAllLocks () {
    boolean locked;

    locked = !chkLockShader.isSelected();
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

    locked = !chkLockAlpha.isSelected();
    chkLockAlpha.setSelected( locked );
    nudAlpha.setEnabled( !locked );

    locked = !chkLockEnvMapContrast.isSelected();
    chkLockEnvMapContrast.setSelected( locked );
    nudEnvMapContrast.setEnabled( !locked );

    locked = !chkLockEnvMapSaturation.isSelected();
    chkLockEnvMapSaturation.setSelected( locked );
    nudEnvMapSaturation.setEnabled( !locked );

    locked = !chkLockEnvMapFrame.isSelected();
    chkLockEnvMapFrame.setSelected( locked );
    nudEnvMapFrame.setEnabled( !locked & animated );
  }

  private void writeKeyValue ( boolean condition, PrintWriter out, String key,
                               String value, int padding ) {
    if ( condition ) {
      out.printf( "\t\"%s\"", key );
      for ( int i = 0; i < padding; i++ ) {
        out.print( "\t" );
      }
      out.printf( "\"%s\"%n", value );
    }
  }

  private void writeSpinner ( PrintWriter out, JSpinner spinner,
                              Number defaultValue, String key, int padding ) {

    Number value = ( ( SpinnerNumberModel ) spinner.getModel() ).getNumber();
    writeKeyValue( !defaultValue.equals( value ), out,
                   key, value.toString(), padding );
  }
  /**
   @param args
               the command line arguments
   */
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
  private javax.swing.JCheckBox chkFlagAdditive;
  private javax.swing.JCheckBox chkFlagAlphaTest;
  private javax.swing.JCheckBox chkFlagIgnoreZ;
  private javax.swing.JCheckBox chkFlagNoCull;
  private javax.swing.JCheckBox chkFlagNoDecal;
  private javax.swing.JCheckBox chkFlagNoLOD;
  private javax.swing.JCheckBox chkFlagPhong;
  private javax.swing.JCheckBox chkFlagSelfIllum;
  private javax.swing.JCheckBox chkFlagTranslucent;
  private javax.swing.JCheckBox chkFlagVertexAlpha;
  private javax.swing.JCheckBox chkFlagVertexColor;
  private javax.swing.JCheckBox chkLockAlpha;
  private javax.swing.JCheckBox chkLockBaseTexture1;
  private javax.swing.JCheckBox chkLockBaseTexture2;
  private javax.swing.JCheckBox chkLockBumpMap1;
  private javax.swing.JCheckBox chkLockBumpMap2;
  private javax.swing.JCheckBox chkLockDetailTexture;
  private javax.swing.JCheckBox chkLockDuDvMap;
  private javax.swing.JCheckBox chkLockEnvMap;
  private javax.swing.JCheckBox chkLockEnvMapContrast;
  private javax.swing.JCheckBox chkLockEnvMapFrame;
  private javax.swing.JCheckBox chkLockEnvMapMask;
  private javax.swing.JCheckBox chkLockEnvMapSaturation;
  private javax.swing.JCheckBox chkLockFrameRate;
  private javax.swing.JCheckBox chkLockKeywords;
  private javax.swing.JCheckBox chkLockNormalMap;
  private javax.swing.JCheckBox chkLockShader;
  private javax.swing.JCheckBox chkLockSurface1;
  private javax.swing.JCheckBox chkLockSurface2;
  private javax.swing.JCheckBox chkLockToolTexture;
  private javax.swing.JCheckBox chkOnlyMissing;
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
  private javax.swing.JLabel jLabel24;
  private javax.swing.JLabel jLabel25;
  private javax.swing.JLabel jLabel26;
  private javax.swing.JLabel jLabel27;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JList lstFiles;
  private javax.swing.JSpinner nudAlpha;
  private javax.swing.JSpinner nudEnvMapContrast;
  private javax.swing.JSpinner nudEnvMapFrame;
  private javax.swing.JSpinner nudEnvMapSaturation;
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


