##Java Concurrency - Practical exercises

I want to share the solution I have given for the **producer–consumer problem** and to the **readers–writers problem** using **semaphores** and a **monitor** with **conditions variables** respectively.
The goal of this two practical exercises is to** control the concurrent access** to a shared resource.
I was asked to implement these solutions for an assignment for the “Programación Concurrente” (Concurrent programming) module which I took at university (2012-2013). 

* * *

###Drakkar Vikingo
####Producer–Consumer problem

Some Vikings shipped on a drakkar have to share a marmite filled with eels for lunch. Vikings cannot eat while other viking is eating, 
so they would have to wait until one finishes to eat the pertinent eel. Once the marmite is empty all of them would have to wait until the cooker would fill it again.
####Java API classes utilized
- java.lang.**Thread**
- java.util.concurrent.**Semaphore**

#### Demo
<div class="align-center">
![demo](http://i1030.photobucket.com/albums/y369/MariaPhotoB/Drakkar_zpsxc7f55t5.gif)
</div>

<br><br>

###RWMonitor
####Readers-Writers problem
Some readers and writers are sharing the same file. Multiple readers can read at the same time but they won't be able to read once a writer is writing.
Writers can write when no writers or readers are accessing the file.

####Java API classes and interfaces utilized
- java.lang.**Thread**
- java.lang **Interface Runnable**
- java.util.concurrent.locks.**ReentrantLock**
- java.util.concurrent.locks **Interface Condition**
- 
#### Output example
<div class="align-center">
![Image](http://i1030.photobucket.com/albums/y369/MariaPhotoB/Monitor01_zpsoqobsdxc.png)
</div>

* * *

### License
This project is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0). 

<br>

### Run the programmes
You must have installed and configured the Java Development Kit (JDK).

#### **Command Prompt**
1. Go to &nbsp;`Directory_Name` directory
2. **javac** `File_Name`.java (Repeat for all files)
3. **java**  &nbsp;  `Executable_File_Name`

#### **IDE (e.g., Eclipse)**
Create a new **Java project** and copy the java files to the **src** folder.
