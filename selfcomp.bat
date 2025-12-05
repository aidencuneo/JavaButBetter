@REM This script compiles the latest compiler into src_out (self compilation)
cd src_out
java JavaBB ../src

@REM Compile src_out
javac -Xmaxerrs 20 JavaBB.java
