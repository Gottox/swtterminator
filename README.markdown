SWT-Terminator
==============

SWT-Terminator is a port of the great Terminator Terminal Application for Java
to Eclipse.

Building
--------
At the moment, we're using a combination of Makefiles and Eclipse for building. Make sure, you have JDK, Eclipse, and GNU Make installed.

- Create a new Workspace

- Import this repository to your Workspace

- Call the Makefile (the Eclipse Builder is already configured, so this should
  should)
  - the Makefile should create two new directories in the Workspacefolder:
    salma-hayek and terminator. Those are needed for building.

- Afterwards the eclipse project should build properly.

License
-------
swtterminator is Licensed under MIT/X License
swtterminator.awtcompat is licensed under EPL
