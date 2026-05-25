package fr.n7.stl.minijava.expression.allocation;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ObjectAllocation  implements AccessibleExpression, AssignableExpression {
	
	protected String name;
	
	protected List<AccessibleExpression> arguments;
	protected Declaration classDeclaration; // Membre 1 : La déclaration de la classe à instancier (pour connaître la taille)
	// Membre 2 : L'étiquette du constructeur associé à cette classe (pour l

	public ObjectAllocation(String _name, List<AccessibleExpression> _arguments) {
		this.name = _name;
		this.arguments = _arguments;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean _result = true;
        
        // 1. On vérifie et collecte le contexte pour chacun des arguments passés au constructeur
        for (AccessibleExpression arg : this.arguments) {
            _result &= arg.collectAndPartialResolve(_scope);
        }
        
        // 2. On vérifie si la classe qu'on veut instancier existe bien dans l'environnement courant
        if (!_scope.knows(this.name)) {
            System.err.println("Erreur sémantique : La classe " + this.name + " n'est pas définie.");
            return false;
        }
        
        // On récupère la déclaration de la classe (Gérée par le Membre 1)
        this.classDeclaration = _scope.get(this.name);
        
        return  _result;
    }

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean _result = true;
        
        // On termine la résolution complète de tous les arguments
        for (AccessibleExpression arg : this.arguments) {
            _result &= arg.completeResolve(_scope);
        }
        
        return _result;
    }

	@Override
	public Type getType() {
        // En attendant que le Membre 1 termine 'ClassType', on caste la déclaration 
        // de la classe en tant que Type (vu que ClassDeclaration implémente Type dans votre AST).
        return (Type) this.classDeclaration;
        
        // REPERE COLLABORATION MEMBRE 1 : Dès que ClassType est prêt, remplace par :
        // return new ClassType(this.name);
    }

	@Override
	public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        /* =================================================================
         * 1. ALLOCATION DYNAMIQUE (Inspirée de PointerAllocation de miniC)
         * ================================================================= */
        // Tu dois demander au Membre 1 de fournir une méthode (ex: getLength()) 
        // dans ClassDeclaration pour connaître la taille totale occupée par les attributs.
        int classSize = 1; // Valeur par défaut pour éviter les crashs, à remplacer par : this.classDeclaration.getLength();
        
        // On pousse la taille de la classe sur la pile
        _result.add(_factory.createLoadL(classSize));
        
        // On appelle MAlloc pour réserver l'espace dans le Tas (Heap). 
        // L'adresse de l'objet est maintenant au sommet de la pile.
        _result.add(Library.MAlloc);
        
        /* =================================================================
         * 2. ÉVALUATION DES ARGUMENTS DU CONSTRUCTEUR
         * ================================================================= */
        // On génère le code TAM pour empiler la valeur de chaque argument passé au `new`
        for (AccessibleExpression arg : this.arguments) {
            _result.append(arg.getCode(_factory));
        }
        
        /* =================================================================
         * 3. APPEL DU CONSTRUCTEUR (Nouveauté miniJava)
         * ================================================================= */
        // Tu dois demander au Membre 2 de te fournir l'étiquette (Label) du constructeur 
        // associé à cette classe pour pouvoir faire le saut de fonction.
        String constructorLabel = "init_" + this.name; // Exemple arbitraire de label
        
        // On fait un CALL vers le constructeur. 
        // Il utilisera l'adresse de l'objet (this) et les arguments présents sur la pile.
        _result.add(_factory.createCall(constructorLabel, Register.SB)); 
        
        return _result;
    }

	
	@Override
	public String toString() {
		String image = "";
		image += "new " + this.name + "( ";
		Iterator<AccessibleExpression> iterator = this.arguments.iterator();
		if (iterator.hasNext()) {
			AccessibleExpression argument = iterator.next();
			image += argument;
			while (iterator.hasNext()) {
				 argument = iterator.next();
				 image += " ," + argument;
			}
		}
		image += ")";
		return image;
	}

}
