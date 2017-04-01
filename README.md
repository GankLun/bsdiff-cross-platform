# bsdiff-cross-platform
bsdiff and bspatch which generates binary diff file in server and binary patch file in client

## DiffBinary
-------------
* DiffHelper.startDiffByCmd(String oldApkPath, String newApkPath, String patchPath) that bsdiff by commond in server

* DiffHelper.startDiff(String oldApkPath, String newApkPath, String patchPath) that bsdiff by JNI invoke in server,which will load dynamic link library.

* java -jar diffbinary2.jar oldApkPath newApkPath patchPath by commond in server
