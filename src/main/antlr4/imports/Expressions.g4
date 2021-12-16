grammar Expressions;

import Words;

reference: variable
         | function
;

variable: ID ('[' expression (',' expression)? ']')?
;

function: ID '(' (expression (',' expression)*)? ')'
;

expression: subExpression
;

subExpression: subExpression operation subExpression
             | value
;

operation:
     op=(  '+'
         | '-'
         | '*'
         | '/'
         | '%'
         | AND
         | OR
         | '>'
         | '<'
         | '='
        )
;


value: integer
     | bool
     | square
     | reference
     | '(' expression ')'
;

integer: neg='-'? NUMBER
       | intVariable
;

intVariable : position
            | count
            | LIFE
;

position: LATITUDE | LONGITUDE | GRID SIZE
;

count: (MAP | RADIO | RADAR | AMMO | FRUITS | SODA) COUNT
;

bool: boolValue
    | boolLocation
    | boolNegation
;

boolValue: (TRUE | FALSE)
;

boolLocation: (ENNEMI | GRAAL) IS (NORTH | SOUTH | EAST | WEST)
;

boolNegation: NOT expression
;

square: squareValue
      | squareNearby
;

squareValue: DIRT
           | ROCK
           | VINES
           | ZOMBIE
           | PLAYER
           | ENNEMI
           | MAP
           | RADAR
           | RADIO
           | AMMO
           | FRUITS
           | SODA
           | GRAAL
;

squareNearby: NEARBY '[' expression ',' expression ']'
;