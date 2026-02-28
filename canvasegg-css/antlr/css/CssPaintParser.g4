parser grammar CssPaintParser;

options { tokenVocab=CssPaintLexer; }

paint
    : (baseColor | lightDark) EOF
    ;

baseColor
    : hexColor
    | rgbColor
    | hslColor
    | hwbColor
    | labColor
    | lchColor
    | namedColor
    ;

hexColor
    : HASH_HEX
    ;

rgbColor
    : (RGB | RGBA) LPAREN component COMMA? component COMMA? component (SLASH alpha)? RPAREN #absoluteRgbColor
    | RGB LPAREN FROM baseColor relativeComponent relativeComponent relativeComponent (SLASH alpha)? RPAREN #relativeRgbColor
    ;

hslColor
    : (HSL | HSLA) LPAREN hue COMMA? saturation COMMA? lightnessPercent (SLASH alpha)? RPAREN
    ;

hwbColor
    : HWB LPAREN hue whiteness blackness (SLASH alpha)? RPAREN
    ;

labColor
    : labLike LPAREN lightness labComponent labComponent (SLASH alpha)? RPAREN
    ;

lchColor
    : lchLike LPAREN FROM baseColor lchComponent lchComponent lchComponent (SLASH alpha)? RPAREN
    | lchLike LPAREN lightness chroma hue (SLASH alpha)? RPAREN
    ;

lightDark
    : LIGHT_DARK LPAREN baseColor COMMA baseColor RPAREN
    ;

namedColor
    : IDENTIFIER
    ;

labLike : LAB | OKLAB ;
lchLike : LCH | OKLCH ;
relativeComponent : component | IDENTIFIER ;
percentValue : NUMBER PERCENT? ;
percent : NUMBER PERCENT? ;
component: percentValue ;
percentComponent : percent ;
saturation : percentComponent ;
lightness : component ;
lightnessPercent: percentComponent ;
hue : NUMBER DEG? ;
alpha : NUMBER | percentValue ;
whiteness : percentComponent ;
blackness : percentComponent ;
labComponent : component ;
lchComponent
    : NUMBER | percent | DEG
    | calcExpression
    ;
calcExpression
    : CALC_START CALC_CONTENT CALC_END
    ;
chroma : NUMBER | percentValue ;
var : VAR LPAREN VAR_PREFIX IDENTIFIER RPAREN ;