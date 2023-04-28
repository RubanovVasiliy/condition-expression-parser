grammar LogFilter;


filter: expression;

expression: orExpr;

orExpr: andExpr ('||' andExpr)*;

andExpr: atom ('&' atom)*;

atom: column compOp value | '(' expression ')' ;

column: 'column[' INT ']';

compOp: '<' | '>' | '=' | '<>' ;

value: STRING | INT;

STRING: '\'' (~'\'')* '\'';

INT: [0-9]+;

WS: [ \t\r\n]+ -> skip;
