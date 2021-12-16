grammar Types;

import Words;

type: scalar | array
;

scalar: INTEGER | BOOLEAN | SQUARE
;

array: scalar '[' NUMBER (',' NUMBER)? ']'
;