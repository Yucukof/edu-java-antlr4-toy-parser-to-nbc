lexer grammar Variables;

// Fragments

fragment LETTER: 'A'..'Z' | 'a'..'z' ;
fragment DIGIT: ZERO | NONZERO;
fragment NONZERO: '1'..'9' ;
fragment ZERO: '0';

NUMBER: (DIGIT)+;

// Types

BOOLEAN: 'boolean'|'BOOLEAN';
INTEGER: 'integer'|'INTEGER';
SQUARE: 'square'|'SQUARE';
VOID: 'void'|'VOID';

// Common Variable Definitions

ID: LETTER (LETTER | DIGIT)*;