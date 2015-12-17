/**
 * @author María Díaz Fuentes
 * @version 1.0
 * 
 * 2. Utilizando cerrojos de clase ReentrantLock y Condition, adapte con
 * 	  estas herramientas el monitor de los lectores/escritores que utilizó en la práctica
 * 	  anterior. Renombre al monitor como RWMonitorAN.java.
 */
 
import java.io.RandomAccessFile;
import java.util.concurrent.locks.*;
 
public class RWMonitorAN {
	
  private static final String FileName = "datos.txt";

  private final ReentrantLock cerrojo = new ReentrantLock();
  private final Condition CondWriting = cerrojo.newCondition();	
  private final Condition CondReaders = cerrojo.newCondition();		
	
  volatile int readers;
  volatile boolean writing;
  
  RandomAccessFile RAF;
  
  public RWMonitorAN() {
  	
  	readers = 0;
  	writing = false;

	try {
		 RAF = new RandomAccessFile(FileName, "rw");
	} catch (java.io.IOException e) {}
  }
  
  public void StartRead() throws InterruptedException {
	cerrojo.lock();
	
	try {
	    while (writing)
			CondReaders.await();

	    readers = readers + 1;
	    
	    String name = Thread.currentThread().getName();
		name = name.replace("Thread-", "");
		int numThread = Integer.parseInt(name);
		String dato;
	
		try {
	
			RAF.seek(0);
	
			while (RAF.getFilePointer() != RAF.length()) {
				dato = RAF.readLine();
		    	System.out.println("Lector " + numThread + " inicia lectura ->\t"  + dato);
			}
	
		} catch (java.io.IOException e) { System.err.println("Error de lectura"); }
	    
	    CondWriting.signalAll();
    
	} finally {
		cerrojo.unlock();
	}
  }

  public void EndRead() throws InterruptedException {
  	cerrojo.lock();
  	try {
	    readers = readers - 1;
	    if (readers == 0)
	    	CondWriting.signal();

	    String name = Thread.currentThread().getName();
		name = name.replace("Thread-", "");
		int numThread = Integer.parseInt(name);

	    System.out.println("Lector " + name + " finaliza lectura");
  	} finally {
		cerrojo.unlock();
	} 
  }
  
  public void StartWrite() throws InterruptedException {
  	cerrojo.lock();
  	try {
	    while (writing || (readers != 0))
	      CondWriting.await();

	    writing = true;
	      	
	    String name = Thread.currentThread().getName();
		name = name.replace("Thread-", "");
		int numThread = Integer.parseInt(name);
		long pos;
	
		try {
			
			pos = RAF.length();
			RAF.seek(pos);
			RAF.writeBytes(name);
			RAF.writeBytes(" ");

	    	System.out.println("Escritor " + numThread + " inicia escritura -> Escribiendo en la posicion " + pos);
	
		} catch (java.io.IOException e) { System.err.println("Error de escritura"); }

  	} finally {
		cerrojo.unlock();
	} 
  }
  
  public void EndWrite() throws InterruptedException {
  	cerrojo.lock();
  	try {
	    writing = false;
	    CondReaders.signalAll(); 
	    CondWriting.signal();
	
	    String name = Thread.currentThread().getName();
		name = name.replace("Thread-", "");
		int numThread = Integer.parseInt(name);
	
	    System.out.println("Escritor " + numThread + " finaliza escritura");
  	} finally {
		cerrojo.unlock();
	} 
  }
  
  public void finalizar() throws java.io.IOException {
  	RAF.setLength(9);
  }
}


class Lector extends Thread { 

	private RWMonitorAN fichero;

	public Lector(RWMonitorAN fichero) { 
		this.fichero = fichero;
	} 
		
	private void Esperar(int n) { 
		try { 
			sleep(n);
		} catch (Exception e) {}
	} 
		
	public void run() {
		try {
			fichero.StartRead();
			Esperar((int) Math.random()*100);
			fichero.EndRead();
		} catch (InterruptedException e) {}
	}
}


class Escritor extends Thread { 
	private RWMonitorAN fichero;

	public Escritor(RWMonitorAN fichero) {
		
		 this.fichero = fichero;
	}
	private void Esperar(int n) {
		 try {
		 	sleep(n);
		 } catch (Exception e) {}
	}
	
	public void run() {
		try {
			fichero.StartWrite();
			Esperar((int) Math.random()*100);
			fichero.EndWrite();
		} catch (InterruptedException e) {}
	}
}
