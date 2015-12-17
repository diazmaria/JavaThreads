/**
 * @author María Díaz Fuentes
 * @version 1.00
 */

import java.io.*;

public class UsaRWMonitorAN {
   
    public UsaRWMonitorAN() {
    }
   
	  public static void main(String[] args) throws InterruptedException, IOException {
	  	
	  	int LECTORES = 15;
	  	int ESCRITORES = 5;
	  	
	  	RWMonitorAN Monitor = new RWMonitorAN();
	  	
	  	Lector [] Lectores = new Lector[LECTORES];
	  	Escritor [] Escritores = new Escritor[ESCRITORES];
	  	
	  	for (int i = 0; i < LECTORES; ++i)
	  		Lectores[i] = new Lector(Monitor);
	  	
	  	for (int i = 0; i < ESCRITORES; ++i)
	  		Escritores[i] = new Escritor(Monitor);
	
	  	for (int i = 0; i < (LECTORES > ESCRITORES ? LECTORES : ESCRITORES); i++) {
	  		if (i < ESCRITORES)
	  			Escritores[i].start();
	  		if (i < LECTORES)
	  			Lectores[i].start();
	  	}
	  	
	  	for (int i = 0; i < (LECTORES > ESCRITORES ? LECTORES : ESCRITORES); i++) {
	  		if (i < ESCRITORES)
	  			Escritores[i].join();
	  		
	  		if (i < LECTORES)
	  			Lectores[i].join();
	  	}
	  	
	  	Monitor.finalizar();
	  	
	  }
}