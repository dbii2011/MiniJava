package fr.n7.stl.minijava.ast.type.declaration;

import java.util.List;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.scope.SymbolTable;

public class MainDeclaration implements Instruction {
	
	protected String name;
	
	protected List<Declaration> declarations;
	
	protected Block main;

    protected SymbolTable mainScope;

	public MainDeclaration(String _name, List<Declaration> _declarations, Block _main) {
		this.name = _name;
		this.declarations = _declarations;
		this.main = _main;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		this.mainScope = new SymbolTable(_scope);
		boolean ok = true;
		for (Declaration decl : this.declarations) {
			if (decl instanceof Instruction) {
				ok = ok && ((Instruction)decl).collectAndPartialResolve(this.mainScope);
			} else {
				if (this.mainScope.accepts(decl)) {
					this.mainScope.register(decl);
				} else {
					ok = false;
				}
			}
		}
		ok = ok && this.main.collectAndPartialResolve(this.mainScope);
		return ok;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		// TODO Auto-generated method stub
		return this.collectAndPartialResolve(_scope);
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		boolean ok = true;
		for (Declaration decl : this.declarations) {
			if (decl instanceof Instruction) {
				ok = ok && ((Instruction)decl).completeResolve(this.mainScope);
			}
		}
		ok = ok && this.main.completeResolve(this.mainScope);
		return ok;
	}

	@Override
	public boolean checkType() {
		// TODO Auto-generated method stub
		boolean ok = true;
		for (Declaration decl : this.declarations) {
			if (decl instanceof Instruction) {
				ok = ok && ((Instruction)decl).checkType();
			}
		}
		ok = ok && this.main.checkType();
		return ok;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		// TODO Auto-generated method stub
		int currentOffset = _offset;
		for (Declaration decl : this.declarations) {
			if (decl instanceof Instruction) {
				currentOffset += ((Instruction)decl).allocateMemory(Register.SB, currentOffset);
			}
		}
		this.main.allocateMemory(Register.SB, currentOffset);
		return 0;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		// TODO Auto-generated method stub
		Fragment frag = _factory.createFragment();
		for (Declaration decl : this.declarations) {
			if (decl instanceof Instruction) {
				frag.append(((Instruction)decl).getCode(_factory));
			}
		}
		// Appel du bloc main
		frag.append(this.main.getCode(_factory));
		return frag;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		String image = "";
		image += "public class " + this.name + " ";
		image += "{\n";
		image += "\n";
		for (Declaration uneDeclaration : this.declarations) {
			image += uneDeclaration;
			image += "\n";
		}
		image += "\tpublic static void Main( String[] args) ";
		image += this.main;
		image += "\n";
		image += "}\n";
		return image;
	}

}
