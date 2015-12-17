/**
 * @ author María Díaz Fuentes
 * @ version 1.0
 *
 * 5. La tripulación de un drakkar vikingo comparte una marmita con un almuerzo
 * a base de m anguilas cocinadas al eneldo. Cuando un vikingo quiere
 * comer, se sirve una anguila. Si ya no quedan, avisa al vikingo cocinero para
 * que proceda a llenar la marmita de nuevo, utilizando las inagotables provisiones
 * de anguilas disponibles. Desarrolle un programa en java que modele esta curiosa
 * situación, y provea la sincronización necesaria utilizando semáforos de clase
 * java.util.concurrent.Semaphore. Llame a su programa drakkarVikingo.java.
 */
import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class drakkarVikingo extends Thread {

 	private static final int CONCINERO = 0;
 	private static final int TRIPULANTE = 1;
 	
    private static boolean [] Marmita; // Si es true hay una anguila, si es false no.

    private static int Ptr = 0; 

    private int tipoTripulante; 

    private int numeroTripulante;

    private static Semaphore SemMarmita; 
    
    private static Semaphore SemTripulante = new Semaphore(0); 
    
    private static Semaphore SemMutex = new Semaphore(1);
	private static Scanner sc; 

    
    public drakkarVikingo(int tipo, int numero) {
    	tipoTripulante = tipo;
    	numeroTripulante = numero;
    }
    
    public void run() {
    	switch(tipoTripulante) {
      		case CONCINERO :
      			while (true) {      				
      				try { SemMarmita.acquire(); } catch ( InterruptedException e) {} // Wait Exclusión mútua para acceso a la Marmita para rellenar
      				
      				try { SemMutex.acquire(); } catch ( InterruptedException e) {} // Wait Exclusión mútua para acceso a índice de relleno de la Marmita

					if (Ptr == 0) { // Condición en la que el Cocinero tiene que rellenar la Marmita
						while (Ptr < Marmita.length) {
		  					Marmita[Ptr] = true;
	  						Ptr++;
	  					}
	  					System.out.print("Cocinero " + this.numeroTripulante + " rellena Marmita [" + Ptr + "] La Marmita queda "); // Producción
						System.out.print("[");
						for (int i = 0; i < Marmita.length; i++)
						{
							if (i != Marmita.lenght)
								System.out.print(Marmita[i] ? "A " : "- ");
							else
								System.out.print(Marmita[i] ? "A" : "-");
						}
						System.out.println("]");
					}
      				SemMutex.release(); // Signal Fin Exclusión mútua para acceso a índice de relleno de la Marmita
      				
      				SemTripulante.release();  // Signal Sincronización con Tripulantes: Primero llenar marmita, luego comer
      			}

      		case TRIPULANTE :
      			while (true) {
      				try { SemTripulante.acquire(); } catch ( InterruptedException e){} // Wait Sincronización con Cocinero: Primero llenar marmita, luego comer
      				      				
      				try { SemMutex.acquire(); } catch ( InterruptedException e) {} // Wait Exclusión mútua para acceso a índice de consumo de la Marmita

      				if (Ptr > 0) { // Condición en la que los tripulantes pueden consumen anguilas (cuando hay)
						System.out.print("Tripulante " + this.numeroTripulante + " come la anguila " + Ptr + " La Marmita queda ");
      					Ptr--;
						Marmita[Ptr] = false;
						System.out.print("[");
						for (int i = 0; i < Marmita.length; ++i)
							System.out.print(Marmita[i] ? "A " : "- ");
						System.out.println("]");
      				}

      				SemMutex.release(); // Signal Fin Exclusión mútua para acceso a índice de consumo de la Marmita      				
      				
      				SemMarmita.release(); // Signal Exclusión mútua para acceso a la Marmita para consumir

      			}
    	}
    }
    
    public static void main(String[] args) {
        
        drakkarVikingo [] Tripulacion; 
        
        int nTrip = 0, tamMarmita = 0;
        sc = new Scanner(System.in);
    	
    	while (nTrip <= 0) {
    		System.out.print("Introduzca el número de tripulantes (incluido el cocinero): ");
     		nTrip = sc.nextInt();
    	}

    	while (tamMarmita <= 0) {
    		System.out.print("Introduzca el tamaño de la Marmita (m): ");
     		tamMarmita = sc.nextInt();
    	}
 
    	SemMarmita = new Semaphore(tamMarmita);
   	
    	Marmita = new boolean[tamMarmita];
    	
    	Tripulacion = new drakkarVikingo[1 + nTrip]; // 1 es el cocinero, el resto es la tripulación
    		
    	for (int i = 0; i < Tripulacion.length; i++)
    		Tripulacion[i] = new drakkarVikingo(i < 1 ? CONCINERO : TRIPULANTE, i);

    	for (int i = 0; i < Tripulacion.length; i++)
    		Tripulacion[i].start();
        
    }
}
