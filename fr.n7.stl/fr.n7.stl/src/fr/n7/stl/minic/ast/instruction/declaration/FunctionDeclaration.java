/**
 * 
 */
package fr.n7.stl.minic.ast.instruction.declaration;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.scope.SymbolTable;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for a function declaration.
 * @author Marc Pantel
 */
public class FunctionDeclaration implements DeclarationInstruction {

	protected String name;
	protected Type type;
	protected List<ParameterDeclaration> parameters;
	protected Block body;

	/** Label TAM du début de la fonction (pour les appels CALL) */
	protected String label;

    protected String startLabel = null;
    public String getStartLabel(TAMFactory _factory) {
        if (this.startLabel == null) {
            this.startLabel = String.valueOf(_factory.createLabelNumber());
        }
        return this.startLabel;
    }

	public List<ParameterDeclaration> getParameters() {
		return parameters;
	}

	public FunctionDeclaration(String _name, Type _type, List<ParameterDeclaration> _parameters, Block _body) {
		this.name = _name;
		this.type = _type;
		this.parameters = _parameters;
		this.body = _body;
		this.label = "func" + _name;
	}

	@Override
	public String toString() {
		String _result = this.type + " " + this.name + "( ";
		Iterator<ParameterDeclaration> _iter = this.parameters.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " ," + _iter.next();
			}
		}
		return _result + " )" + this.body;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	/** Retourne le label TAM associé à cette fonction */
	public String getLabel() {
		return this.label;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// On enregistre la fonction dans la portée courante
		if (_scope.accepts(this)) {
			_scope.register(this);
		} else {
			Logger.error("Function " + this.name + " is already declared.");
			return false;
		}
		// un scope local pour les parametres et le corps
		HierarchicalScope<Declaration> localScope = new SymbolTable(_scope);
		for (ParameterDeclaration param : this.parameters) {
			if (localScope.accepts(param)) {
				localScope.register(param);
			} else {
				Logger.error("Parameter " + param.getName() + " is already declared in function " + this.name);
				return false;
			}
		}
		return this.body.collectAndPartialResolve(localScope, this);
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		return this.collectAndPartialResolve(_scope);
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		HierarchicalScope<Declaration> localScope = new SymbolTable(_scope);
		for (ParameterDeclaration param : this.parameters) {
			if (localScope.accepts(param)) {
				localScope.register(param);
			}
		}
		boolean ok = this.type.completeResolve(_scope);
		ok &= this.body.completeResolve(localScope);
		return ok;
	}

	@Override
	public boolean checkType() {
		return this.body.checkType();
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		// les paramètres sont empilés avant la fonction donc leurs offsets sont negatifs par rapport à LB
		int paramOffset = 0;
		for (ParameterDeclaration param : this.parameters) {
			paramOffset += param.getType().length();
		}

		int currentParamOffset = -paramOffset;
		for (ParameterDeclaration param : this.parameters) {
			param.offset = currentParamOffset;
			currentParamOffset += param.getType().length();
		}
		// les variables locales sont allouées à partir de 3 (0,1 et 2 sont réservés par TAM)
		this.body.allocateMemory(Register.LB, 3);

		return 0;
	}

	@Override
		public Fragment getCode(TAMFactory _factory) {
			Fragment frag = _factory.createFragment();

			String endFuncLabel = "Fin_Func_" + _factory.createLabelNumber();
			
			// on saute tjrs la definition de la fonction
			frag.add(_factory.createJump(endFuncLabel));
			
			Fragment bodyFrag = this.body.getCode(_factory);
			bodyFrag.addPrefix(this.label);
			frag.append(bodyFrag);
			frag.addSuffix(endFuncLabel);

			return frag;
		}

}