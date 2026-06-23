@REM This script recompiles the compiler into src_out
java -jar jbb.jar src

@REM Compile src_out
cd src_out && javac JavaBB.java
