package fr.n7.stl.minijava.ast.type.declaration;

public enum ElementKind {
    
    OBJECT,
    CLASS,
    METHOD,
    CONSTRUCTOR;
    
    @Override
    public String toString() {
        switch (this) {
        case OBJECT : return "";
        case CLASS : return "static ";
        case METHOD : return "";
        case CONSTRUCTOR : return "";
        default: throw new IllegalArgumentException( "The default case should never be triggered.");
        }
    }

}