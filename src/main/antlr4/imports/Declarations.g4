grammar Declarations;

import Instructions
     , Types
     , Words;

varDecl: varType ';'
;

fctDecl: ID AS FUNCTION '(' (varType (',' varType)* )? ')' ':' (scalar | VOID)
         (DECLARE LOCAL varDecl+)?
         DO instruction*
            (RETURN (expression | VOID))?
         DONE
;

varType: ID AS type
;