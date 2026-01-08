@echo off
echo Building MidnightMediaPlayer...

jpackage ^
 --input . ^
 --name MidnightMediaPlayer ^
 --main-jar MidnightMediaPlayer.jar ^
 --main-class com.example.Main ^
 --type exe ^
 --dest ./output

echo.
echo Build finished.
pause
