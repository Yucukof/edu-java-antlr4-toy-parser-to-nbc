grammar Grammar;

import Declarations
     , Imports
     , Instructions
     , Clauses
     , Words;

root: world | strategy;

world: DECLARE AND RETAIN
       (declaration)*
       instruction*
       clauseDefault
;

strategy: DECLARE AND RETAIN
          impDecl?
          (declaration)*
          WHEN YOUR TURN
          clauseWhen*
          clauseDefault
;

declaration: varDecl | fctDecl
;