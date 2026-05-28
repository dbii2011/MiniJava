package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.expression.AbstractSuper;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class SuperAccess extends AbstractSuper<AccessibleExpression> implements AccessibleExpression {
	
	protected Declaration superDeclaration;

	public SuperAccess() {
		super();
	}
	
	@Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        if (_scope.knows("super")) {
            this.superDeclaration = _scope.get("super");
            return true;
        }
        System.err.println("Erreur : Utilisation de 'super' invalide dans ce contexte.");
        return false;
    }

    @Override
    public Type getType() {
        if (this.superDeclaration != null) {
            return this.superDeclaration.getType();
        }
        return null;
    }

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		// En mémoire, 'super' fait référence à la même instance que 'this'
		frag.add(_factory.createLoad(Register.LB, -1, 1));
		return frag;
	}

}
