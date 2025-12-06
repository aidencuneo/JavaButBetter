@REM This script runs the most recent compiler on the 'src_jbb' directory
cd src_out
java JavaBB ../src_jbb

@REM Compile and run the resulting code
cd ../src_jbb_out
javac Code.java
java Code
