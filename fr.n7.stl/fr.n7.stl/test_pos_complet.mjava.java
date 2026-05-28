// TEST DE LA GRILLE : Classes Abstraites, Héritage et Droits d'accès
abstract class Forme {
    // TEST : Droit d'accès protected
    protected int x;
    protected int y;

    // TEST : Constructeur parent
    public Forme(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    // TEST : Méthode abstraite (pas de corps)
    public abstract int calculerAire();
}

class Rectangle extends Forme {
    // TEST : Droit d'accès private
    private int largeur;
    private int hauteur;

    // TEST : Attribut de Classe (static)
    public static int compteurRectangles = 0;

    // TEST : Constructeur enfant
    public Rectangle(int _x, int _y, int _l, int _h) {
        super(_x, _y); // Appel au constructeur parent
        this.largeur = _l;
        this.hauteur = _h;
        // Modification d'un attribut statique
        compteurRectangles = compteurRectangles + 1; 
    }

    // TEST : Redéfinition d'une méthode abstraite
    public int calculerAire() {
        return this.largeur * this.hauteur;
    }

    // TEST : Méthode de Classe (static)
    public static void afficherCompteur() {
        print compteurRectangles;
    }
}

public class Main {
    public static void main(String[] args) {
        // 1. Appel de méthode statique
        Rectangle.afficherCompteur(); // Doit afficher 0

        // 2. Instanciation
        Rectangle r1 = new Rectangle(0, 0, 10, 5);
        Rectangle r2 = new Rectangle(10, 10, 2, 2);

        // 3. Appel de méthode statique après modification
        Rectangle.afficherCompteur(); // Doit afficher 2

        // 4. TEST : Polymorphisme (Héritage)
        Forme f = r1;
        
        // 5. Appel de méthode dynamique
        print f.calculerAire(); // Doit afficher 50 (10 * 5)
    }
}