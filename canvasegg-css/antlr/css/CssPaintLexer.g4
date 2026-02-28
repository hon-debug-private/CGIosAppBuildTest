lexer grammar CssPaintLexer;

RGB : 'rgb' ;
RGBA : 'rgba' ;
HSL : 'hsl' ;
HSLA : 'hsla' ;
LAB : 'lab' ;
LCH : 'lch' ;
OKLAB : 'oklab' ;
OKLCH : 'oklch' ;
HWB : 'hwb' ;
LIGHT_DARK: 'light-dark' ;
COLOR_MIX: 'color-mix' ;
IN : 'in' ;
DEG : 'deg' | 'rad' | 'grad' | 'turn' ;

LPAREN : '(' ;
RPAREN : ')' ;
COMMA : ',' ;
SLASH : '/' ;
VAR : 'var' ;
VAR_PREFIX : '--';

PERCENT : '%' ;

FROM : 'from' ;
CALC_START : 'calc(' -> pushMode(CALC_MODE);

HASH_HEX
    : '#' HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE
    | '#' HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE
    | '#' HEX_VALUE HEX_VALUE HEX_VALUE HEX_VALUE
    | '#' HEX_VALUE HEX_VALUE HEX_VALUE
    ;

fragment HEX_VALUE : [0-9a-fA-F] ;

NUMBER : '-'? [0-9]+ ('.' [0-9]+)? ;
IDENTIFIER : [a-zA-Z_] [a-zA-Z0-9_-]* ;

WS : [ \t\r\n]+ -> skip ;

mode CALC_MODE;
    CALC_CONTENT : ( ~[()] | '(' CALC_CONTENT* ')' )+ ;

    CALC_END : ')' -> popMode;
