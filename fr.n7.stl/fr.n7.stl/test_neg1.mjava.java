abstract class Animal {
    public abstract void crier();
}

public class Main {
    public static void main(String[] args) {
        // Ceci DOIT lever une erreur de sémantique (Type verification failed)
        Animal a = new Animal(); 
    }
}