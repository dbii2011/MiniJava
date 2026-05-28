package fr.n7.stl.minijava.expression;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;

public abstract class AbstractAttribute <ObjectKind extends Expression> implements Expression {
    
    protected ObjectKind object;
    protected String name;
    protected AttributeDeclaration attribute; // Déjà typé correctement avec le travail du Membre 1 !

    public AbstractAttribute(ObjectKind _object, String _name) {
        this.object = _object;
        this.name = _name;
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        // On collecte et résout d'abord l'objet à gauche du point (ex: 'obj')
        return this.object.collectAndPartialResolve(_scope);
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        // 1. On termine la résolution complète de l'objet à gauche du point
        if (!this.object.completeResolve(_scope)) {
            return false;
        }

        // 2. En orienté objet, on cherche l'attribut dans la classe de l'objet
        Type targetType = this.object.getType();
        if (targetType != null && targetType instanceof ClassType) {
            ClassDeclaration classDecl = ((ClassType) targetType).declaration;
            
            if (classDecl != null) {
                this.attribute = classDecl.getAttribute(this.name);
                if (this.attribute != null) {
                    return true;
                }
            }
        }
        
        System.err.println("Erreur sémantique : L'attribut " + this.name + " n'est pas défini dans ce contexte.");
        return false;
    }

    @Override
    public Type getType() {
        // Le type de l'accès à l'attribut est tout simplement le type de l'attribut lui-même.
        // On utilise la méthode de la classe d'Imane (s'il s'agit bien de getType(), ce qui est la norme STL)
        if (this.attribute != null) {
            return this.attribute.getType();
        }
        return null;
    }
    
    @Override
    public String toString() {
        String image = "";
        image += this.object;
        image += ".";
        image += this.name;
        return image;
    }
}