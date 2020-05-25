Requires java 8 or JRE 1.8.0_xxx.

use XDW - XML Data Warehouse[ - Debug].bat to debug/run the jar app. (bat file code: @echo off
cmd /C start java[w] -jar "%~dp0XDW - XML Data Warehouse.jar" "%1")

Use XDW _ XML Data Warehouse - Debug.bat to keep cmd open while running the app to check for any exception or any message.

To set this app as the default for XML data warehousing projects (.xdw),File.xdw right click -> properties -> [open with:] -> Change -> select [XDW - XML Data Warehouse[ - Debug].bat]. 

To delete history, delete history.cfg file.

[ ] : optional


/////////////////////////////////////////// XDP - XML Diff and Patch //////////////////////////////////////////////
/output/ folder is for XDP - XML Diff and Patch app in which all output Diffs and output patched files are stored.
costs.cfg is for the XDP - XML Diff and Patch in which it stores the user-pre defined costs.