package Practicas;

public class Objeto {

	 private String nombre;
     private int peso;
     private int valor;

     public Objeto(String n, int p, int v) {
         nombre = n;
         peso = p;
         valor = v;
     }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
     
}
