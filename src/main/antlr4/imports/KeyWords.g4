lexer grammar KeyWords;

// Words

RETAIN: 'retain'|'RETAIN';
IMPORT: 'import'|'IMPORT';
BY: 'by'|'BY';
DEFAULT: 'default'|'DEFAULT';

WHILE: 'while'|'WHILE';
WHEN: 'when'|'WHEN';
DO: 'do'|'DO';
AS: 'as'|'AS';
SET: 'set'|'SET';
TO: 'to'|'TO';
COMPUTE: 'compute'|'COMPUTE';
NEXT: 'next'|'NEXT';
DONE: 'done'|'DONE';

IF: 'if'|'IF';
THEN: 'then'|'THEN';
ELSE: 'else'|'ELSE';
AND: 'and'|'AND';
OR: 'or'|'OR';
NOT: 'not'|'NOT';
TRUE: 'true'|'TRUE';
FALSE: 'false'|'FALSE';
IS: 'is'|'IS';

DECLARE: 'declare'|'DECLARE';
LOCAL: 'local'|'LOCAL';
GLOBAL: 'global'|'GLOBAL';
FUNCTION: 'function'|'FUNCTION';
RETURN: 'return'|'RETURN';

NEARBY: 'nearby'|'NEARBY';
COUNT: 'count'|'COUNT';
YOUR: 'your'|'YOUR';
TURN: 'turn'|'TURN';

// Comments -> ignored

COMMENT: '/*' .*? '*/' -> skip ;

// Whitespaces -> ignored

NEWLINE: '\r'? '\n'  -> skip ;
WS: [ \t]+ -> skip ;