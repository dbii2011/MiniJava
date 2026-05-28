class CompteBancaire {
    private int solde;

    public CompteBancaire() {
        this.solde = 1000; // Légal, on est à l'intérieur
    }
}

public class Main {
    public static void main(String[] args) {
        CompteBancaire monCompte = new CompteBancaire();
        // Ceci DOIT lever une erreur de sémantique (Droit d'accès refusé)
        print monCompte.solde; 
    }
}