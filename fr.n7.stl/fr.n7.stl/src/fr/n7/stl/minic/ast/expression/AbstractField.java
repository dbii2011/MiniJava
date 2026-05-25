package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.NamedType;
import fr.n7.stl.minic.ast.type.RecordType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minic.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.minic.ast.type.AtomicType;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractField<RecordKind extends Expression> implements Expression {

	protected RecordKind record;
	protected String name;
	protected FieldDeclaration field;
	
	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public AbstractField(RecordKind _record, String _name) {
		this.record = _record;
		this.name = _name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.record + "." + this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// On transmet la collecte à l'expression de l'enregistrement
		return this.record.collectAndPartialResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// On résout l'enregistrement
		boolean res = this.record.completeResolve(_scope);
		
		// On cherche à lier le champ à sa déclaration dans la structure
		Type recordType = this.record.getType();
		
		// Si le type est caché derrière un Typedef (NamedType)
		if (recordType instanceof NamedType) {
			recordType = ((NamedType) recordType).getType();
		}
		
		// Si on a bien affaire à une structure
		if (recordType instanceof RecordType) {
			this.field = ((RecordType) recordType).get(this.name);
		}
		
		return res && (this.field != null);
	}

	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
		if (this.field != null) {
			return this.field.getType();
		}
		
		Type recordType = this.record.getType();
		if (recordType instanceof NamedType) {
			recordType = ((NamedType) recordType).getType();
		}
		
		if (recordType instanceof RecordType) {
			FieldDeclaration fd = ((RecordType) recordType).get(this.name);
			if (fd != null) {
				return fd.getType();
			}
		}
		return AtomicType.ErrorType;
	}

}