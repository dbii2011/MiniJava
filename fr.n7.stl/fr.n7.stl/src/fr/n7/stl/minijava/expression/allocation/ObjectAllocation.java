package fr.n7.stl.minijava.expression.allocation;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import java.util.Iterator;
import java.util.List;

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
        return ((ClassDeclaration) this.classDeclaration).getType();
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        // 1. Arguments D'ABORD
        for (AccessibleExpression arg : this.arguments) {
            _result.append(arg.getCode(_factory));
        }
        
        // 2. MAlloc (crée l'adresse de 'this' au sommet de la pile)
        // On récupère la vraie taille dynamique calculée par la ClassDeclaration
        int classSize = ((ClassDeclaration) this.classDeclaration).getInstanceSize(); 
        
        _result.add(_factory.createLoadL(classSize));
        _result.add(Library.MAlloc);
        
        // 3. Appel du constructeur
        String constructorLabel = "constructor_" + this.name;
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
