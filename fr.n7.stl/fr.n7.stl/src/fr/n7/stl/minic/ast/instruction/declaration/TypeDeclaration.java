package fr.n7.stl.minic.ast.instruction.declaration;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a type declaration.
 * @author Marc Pantel
 *
 */
public class TypeDeclaration implements DeclarationInstruction {

	/**
	 * Name of the declared type
	 */
	private String name;
	
	/**
	 * AST node for the type associated to the name
	 */
	private Type type;

	/**
	 * Builds an AST node for a type declaration
	 * @param _name : Name of the declared type
	 * @param _type : AST node for the type associated to the name
	 */
	public TypeDeclaration(String _name, Type _type) {
		this.name = _name;
		this.type = _type;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
<<<<<<< HEAD
		if (_scope.accepts(this)) {
			_scope.register(this);
			return true;
		} else {
			fr.n7.stl.util.Logger.error("Le type " + this.name + " est déjà défini.");
			return false;
		}
	}
	
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		// on n'a pas besoin de savoir dans quelle fonction on se trouve
		return this.collectAndPartialResolve(_scope);

=======
	    // 1. On enregistre la déclaration (méthode void)
	    _scope.register(this);
	    
	    // 2. On retourne true pour indiquer que la collecte a réussi
	    return true;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
	    // Même logique, le container n'influence pas l'enregistrement du type
	    return this.collectAndPartialResolve(_scope);
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
<<<<<<< HEAD
		return this.type.completeResolve(_scope);
=======
	    // On s'assure que le type associé est résolu
	    return this.type.completeResolve(_scope);
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

	/**
	 * Provide the type associated to a name in a type declaration.
	 * @return Type from the declaration.
	 */
	public Type getType() {
		return this.type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "typedef " + this.type + " " + this.name + ";\n";
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		return _factory.createFragment();
	}
	
}
