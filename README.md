jteg
====
Java TEG repository

This is a java server side implementation of the notirous teg game (http://sourceforge.net/projects/teg/)

This clone is fully written in java with layer separation , providing the ability implement/reimplement new features with ease.


Building the server
--------------------

In order to build the server , do the following
* Clone
* Go to trunk/server directory and run the build file with the "fatJar" target using ant either from eclipse or the command line.
* Assuming you are in trunk/server run
    java -cp "target/server.full.jar;"  org.maharshak.teg.server.infra.Server
* Enjoy your local server :)

