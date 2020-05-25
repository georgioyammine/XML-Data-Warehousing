# XDW - XML Data Warehousing

XDW is a tool in Java for **XML Data Warehousing**, **Version Control** and **Temporal Querying**, where huge number of files or versions need to be stored. The tool we developed uses **XDP - XML Diff and Patch** to get **diff** files and therefore **save space**. The application stores the **last version of the document along with the backward deltas** in order to query the past.

This Application is part of a project in the Lebanese American University, Course:  Intelligent Data Processing and Applications (COE 543/743).

This Tool is built by **Georgio Yammine** and **Rami Naffah**.

App Version: 1.1 - backward compatible with the older version.

Tools Used: **Java 8**, **JavaFX** and **FXML** with **JFoenix** and **TilesFX** Libraries.

Project and **runnable** **jar** file created in **eclipse**.

**exe** created using **launch4j**.



## Preview
![Welcome Screen](/images/welcomeOpen.png)
![Welcome Screen Pinned](/images/welcomeOpenP.png)
![Welcome New](/images/welcomeNew.png)
![Loading Screen](/images/loading.png)
![Home Screen](/images/Home.png)
![Versions Screen](/images/Versions.png)
![QueryA Screen](/images/QueryA.png)
![QueryC Screen](/images/QueryC.PNG)
![User Screen](/images/User.png)
![About Screen](/images/About.png)

## Set as default app for .xdw files
To set this app as the default for XML data warehousing projects (.xdw),File.xdw right click -> properties -> [open with:] -> Change -> select [XDW _ XML Data Warehouse.exe]. 

## Delete history
![Default App](/images/DefaultApp.png)

To delete history(Recent/Pinned), delete history.cfg file.