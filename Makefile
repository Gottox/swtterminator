# Makefile for importing salma-hayek and terminator as jars

.PHONY: all clean

LIBS = lib/salma-hayek.jar lib/terminator.jar lib/libpty.jnilib lib/libposix.jnilib
SRC = ../salma-hayek ../terminator


all: ${LIBS}

clean:
	rm ${LIBS}

lib/%.jar: ../% lib
	jar cf $@ -C $</.generated/classes .
	
lib/%.jnilib: ${SRC} lib
	cp `find ${SRC} -name \`basename $@\`` lib
	
lib:
	mkdir -p lib
	
../%:
	if [ -e "$@" ] then \
		cd $@ && svn update \
	else \
		svn checkout http://software.jessies.org/svn/`basename $@`/trunk/ $@ \
	fi
	make -C $@