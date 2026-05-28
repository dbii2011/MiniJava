package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.expression.AbstractThis;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ThisAccess extends AbstractThis<AccessibleExpression> implements AccessibleExpression {
	
	public ThisAccess() {
		super();
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return true; 
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		// 'this' est le premier paramètre caché empilé lors d'un appel de méthode objet.
		// Dans l'architecture standard TAM pour la POO, il se trouve au début des paramètres (souvent 3[LB])
		frag.add(_factory.createLoad(Register.LB, -1, 1));
		return frag;
	}

}
