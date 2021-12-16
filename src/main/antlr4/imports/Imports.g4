grammar Imports;

impDecl: keyword=('import'|'IMPORT') FILENAME
;

FILENAME: LETTER (LETTER | DIGIT)*'.wld';

fragment LETTER: 'A'..'Z' | 'a'..'z' ;
fragment DIGIT: '0'..'9';