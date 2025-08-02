@REM This script recompiles the compiler
cd compiler
java JavaBB ../src
cd ..

@REM Compile src_out
cd src_out
javac JavaBB.java
