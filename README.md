VMTGen
======

Valve Material Type file generator

VMTGen is a tool meant to quickly generate Valve Material Type files for your
custom textures with the least number of clicks and user inputs possible, while
allowing settings to be locked and persist between multiple textures.
(as opposed to having to re-input each texture's settings via VTFEdit.)

### [Download](https://github.com/Xyphos/VMTGen/releases/tag/1.1-BETA-rc3) Version 1.1-BETA-rc3 (stand-alone executable)
####(requires [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html) to run)

# SCREENSHOTS

### GUI
![Interface](https://raw.github.com/Xyphos/VMTGen/master/doc/screenshot.png)

### File List
![File list](https://raw.github.com/Xyphos/VMTGen/master/doc/files.png)

### Input Locks
![Input Locks](https://raw.github.com/Xyphos/VMTGen/master/doc/lock-input.png)

### Keyboard Driven
![Keyboard Driven](https://raw.github.com/Xyphos/VMTGen/master/doc/hotkeys.png)

### Supports Animated Textures
![Supports Animation](https://raw.github.com/Xyphos/VMTGen/master/doc/material.png)

======

## IMPORTANT FORKING INFORMATION
This Netbeans project uses [Maven](http://maven.apache.org/) for dependency management.
While I tried to keep the dependencies lowered to a minimum, here is what you need:

1. Apache [commons-io 2.4](http://commons.apache.org/proper/commons-io/download_io.cgi) (uses FilenameUtils)
2. Google [guava 15.0-rc1](https://code.google.com/p/guava-libraries/) (uses LittleEndianDataInputStream)
