@REM This script packages the latest compiler (src_out) into a jar file and saves it as the latest version
@echo off

set version=%1

@REM Check if version given
if "%version%"=="" (
    echo Version not given
    exit
)

@echo on

@REM Package into jar
cd src_out
jar cfe ../JavaBB.jar JavaBB *.class
cd ..

@REM Copy src, src_out, and the new jar into the compiler_versions folder
mkdir compiler_versions\%version%\
mkdir compiler_versions\%version%\src
mkdir compiler_versions\%version%\src_out
xcopy /e /y src compiler_versions\%version%\src
xcopy /e /y src_out compiler_versions\%version%\src_out
copy /y JavaBB.jar compiler_versions\%version%\JavaBB.jar

@REM Copy src_out so that it is used as the new compiler
xcopy /e /y src_out compiler\
