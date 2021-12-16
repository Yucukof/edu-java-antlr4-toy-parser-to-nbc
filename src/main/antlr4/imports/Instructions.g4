grammar Instructions;

import Expressions
     , Words;

instruction: skip='skip'                                # SkipInstr
           | IF expression THEN body (ELSE body)? DONE  # IfInstr
           | WHILE expression DO body DONE              # WhileInstr
           | SET variable TO expression                 # SetInstr
           | COMPUTE expression                         # ComputeInstr
           | NEXT action                                # NextInstr
;

body: instruction+
;

action: (MOVE|SHOOT) (NORTH|SOUTH|EAST|WEST)
      | USE (MAP|RADAR|RADIO|FRUITS|SODA)
      | DO NOTHING
;
