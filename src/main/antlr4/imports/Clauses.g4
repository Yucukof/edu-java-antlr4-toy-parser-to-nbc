grammar Clauses;

import Expressions
     , Declarations
	 , Instructions
	 , Words;

clauseWhen: WHEN expression
            (DECLARE LOCAL varDecl+)?
            DO instruction+
            DONE
;

clauseDefault: BY DEFAULT
               (DECLARE LOCAL (varDecl)+)?
               DO instruction+
               DONE
;
