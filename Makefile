# Makefile for importing salma-hayek and terminator as jars

.PHONY: all clean

LIBS = lib/salma-hayek.jar lib/terminator.jar lib/libpty.jnilib lib/libposix.jnilib
SRC = ../salma-hayek ../terminator


all: ${LIBS}

clean:
	rm ${LIBS}

lib/%.jar: ../% lib
	jar cf $@ -C $</.generated/classes .
	
lib/%.jnilib:
	cp `find ${SRC} -name \`echo $@ | sed "s/lib\///"\`` lib
	
lib:
	mkdir -p lib
