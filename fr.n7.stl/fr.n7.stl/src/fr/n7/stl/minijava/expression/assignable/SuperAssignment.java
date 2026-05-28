package fr.n7.stl.minijava.expression.assignable;

import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minijava.expression.AbstractSuper;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.tam.ast.Register;

public class SuperAssignment extends AbstractSuper<AssignableExpression> implements AssignableExpression {

	public SuperAssignment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		// super pointe vers la même adresse mémoire que this
		frag.add(_factory.createLoad(Register.LB, -1, 1));
		return frag;
	}

}
