package Practicas;

import java.util.*; 

public class Mochila{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); //Para leer datos del usuario
        ArrayList<Objeto> objetos = new ArrayList<>(); //Lista donde guardamos los objetos

        //Pedimos al usuario la capacidad máxima de la mochila
        System.out.print("Capacidad de la mochila: ");
        int capacidad = sc.nextInt();
        sc.nextLine(); //Limpiamos el buffer del Scanner

        //Pedimos cuántos objetos quiere introducir
        System.out.print("Número de objetos: ");
        int num = sc.nextInt();
        sc.nextLine();

        //Bucle para pedir los datos de cada objeto
        for (int i = 0; i < num; i++) {
            System.out.println("\nObjeto " + (i + 1));
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Peso: ");
            int peso = sc.nextInt();
            System.out.print("Valor: ");
            int valor = sc.nextInt();
            sc.nextLine(); // Limpiamos el buffer

            //Creamos el objeto y lo añadimos a la lista
            objetos.add(new Objeto(nombre, peso, valor));
        }

        //Creamos la tabla para la programación dinámica
        //num+1 pq necesitamos la fila 0 que es mingún objeto y capacidad+1 pq necesitamos la columna 0 que es capacidad vacia
        int[][] tabla = new int[num + 1][capacidad + 1];

        //Rellenamos la tabla con los valores óptimos
        for (int i = 1; i <= num; i++) {
            Objeto obj = objetos.get(i - 1); //Obtenemos el objeto actual, i-1 pq lista objetos empieza en 0 y la tabla en 1
            for (int j = 0; j <= capacidad; j++) {
                if (obj.getPeso() <= j) {
                    //Podemos meter el objeto: elegimos entre meterlo o no
                    tabla[i][j] = Math.max(tabla[i - 1][j], tabla[i - 1][j - obj.getPeso()] + obj.getValor()); //valor que ya tenía con el espacio restante + valor del objeto nuevo.
                } else {
                    //No cabe: copiamos el valor anterior
                    tabla[i][j] = tabla[i - 1][j];
                }
            }
        }

        //Ahora reconstruimos la solución: qué objetos se han metido
        ArrayList<Objeto> elegidos = new ArrayList<>();
        int pesoTotal = 0;
        int valorTotal = tabla[num][capacidad]; //Valor máximo conseguido
        int espacio = capacidad;

        //Recorremos la tabla hacia atrás para ver qué objetos se han usado
        for (int i = num; i > 0 && espacio > 0; i--) {
            if (tabla[i][espacio] != tabla[i - 1][espacio]) { // Este objeto se usa: lo añadimos a la lista, sumamos su peso y restamos el espacio
            	//Si la fila cambia, es porque meter ese objeto mejora la mochila
                //Este objeto se ha usado
                Objeto obj = objetos.get(i - 1);
                elegidos.add(obj);
                pesoTotal += obj.getPeso();
                espacio -= obj.getPeso(); //Reducimos el espacio disponible
            }
        }

        //Mostramos los objetos seleccionados y el resumen final
        System.out.println("\nObjetos elegidos:");
        for (Objeto o : elegidos) {
            System.out.println("- " + o.getNombre() + " (Peso: " + o.getPeso() + ", Valor: " + o.getValor() + ")");
        }

        System.out.println("Peso total: " + pesoTotal);
        System.out.println("Valor total: " + valorTotal);

        sc.close(); //Cerramos el Scanner
    }
}
