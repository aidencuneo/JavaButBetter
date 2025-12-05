@REM This script packages the latest compiler (src_out) into a jar file and saves it as the latest version
version=%1

@REM Package into jar
jar -cf compiler/JavaBB.jar src_out/JavaBB.class

@REM Copy src, src_out, and the new jar into the compiler_versions folder
cp src compiler_versions/${version}
cp src_out compiler_versions/${version}
cp compiler/JavaBB.jar compiler_versions/${version}
